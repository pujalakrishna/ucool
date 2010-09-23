package web.handler.impl;

import biz.file.FileEditor;
import common.ConfigCenter;
import web.handler.Handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;

/**
 * css和js的handler
 *
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:26:39
 */
public class AssetsHandler implements Handler {

    private String realPath;

    private String assetsRoot;
    private String cacheRoot;

    private FileEditor fileEditor;

    private ConfigCenter configCenter;

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * Method doHandler ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws IOException      when
     * @throws ServletException when
     */
    public void doHandler(HttpServletRequest request,
                          HttpServletResponse response) throws IOException, ServletException {
        initHandler();
        String filePath = request.getRequestURI();
        response.setCharacterEncoding("gbk");
        PrintWriter out = response.getWriter();
        /**
         * 查找本地文件，没有的话再找缓存，没有缓存的从线上下载，再走缓存。
         */
        if (findAssetsFile(filePath)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, false));
        } else if (findCacheFile(filePath)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, true));
        } else {
            if (cacheUrlFile(request)) {
                this.fileEditor.pushFile(out, loadExistFile(filePath, true));
            } else {
                //如果缓存失败了，还是老老实实从线上取吧
                readUrlFile(out, request);
            }
        }
    }

    private void initHandler() {
        if (this.realPath == null || this.assetsRoot == null) {
            this.realPath = this.configCenter.getWebRoot();
            this.assetsRoot = this.configCenter.getUcoolAssetsRoot();
            this.cacheRoot = this.configCenter.getUcoolCacheRoot();
        }
    }

    /**
     * 把线上的文件缓存起来
     *
     * @param request of type String
     * @return boolean
     * @author zhangting
     * @since 2010-8-19 15:44:07
     */
    private boolean cacheUrlFile(HttpServletRequest request) {
        String allUrl = request.getRequestURL().toString();
        allUrl = urlFilter(allUrl);

        try {
            URL url = new URL(allUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            sb.append(realPath).append(cacheRoot).append(request.getRequestURI());
            //先创建目录和文件，再往里写数据
            fileEditor.createDirectory(sb.toString());

            return fileEditor.saveFile(sb.toString(), in);
        } catch (IOException e) {
            //log
        }
        return false;
    }


    /**
     * Method readUrlFile ...
     *
     * @param out     of type PrintWriter
     * @param request of type String
     * @throws javax.servlet.ServletException when
     * @throws java.io.IOException            when
     * @author zhangting
     * @since 2010-8-11 9:29:00
     */
    private void readUrlFile(PrintWriter out, HttpServletRequest request)
            throws javax.servlet.ServletException, IOException {
        String allUrl = request.getRequestURL().toString();
        allUrl = urlFilter(allUrl);

        try {
            URL url = new URL(allUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            fileEditor.pushStream(out, in);
            String line;
            while ((line = in.readLine()) != null) {
                out.println(line);
            }
            in.close();
        } catch (Exception e) {
            out.println("file not find");
        }
        out.flush();
    }

    /**
     * 从assets目录中查找本地修改过的文件
     *
     * @param filePath of type String
     * @return boolean
     * @author zhangting
     * @since 2010-8-19 14:49:26
     */
    private boolean findAssetsFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(realPath).append(assetsRoot).append(filePath);
        return this.fileEditor.findFile(sb.toString());
    }

    /**
     * 从cache目录查找替补文件
     *
     * @param filePath of type String
     * @return boolean
     * @author zhangting
     * @since 2010-8-19 14:50:35
     */
    private boolean findCacheFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(realPath).append(cacheRoot).append(filePath);
        return this.fileEditor.findFile(sb.toString());
    }

    /**
     * 加载本地文件或者缓存文件
     *
     * @param filePath of type String
     * @param isCache  of type boolean
     * @return FileInputStream
     * @author zhangting
     * @since 2010-8-19 15:22:02
     */
    private FileReader loadExistFile(String filePath, boolean isCache) {
        String root = isCache ? cacheRoot : assetsRoot;
        StringBuilder sb = new StringBuilder();
        sb.append(realPath).append(root).append(filePath);
        try {
            return this.fileEditor.loadFile(sb.toString());
        } catch (FileNotFoundException e) {

        }
        return null;
    }

    /**
     * 把请求的在配置文件中的所有域名换成ip
     *
     * @param url of type String
     * @return String
     */
    private String urlFilter(String url) {
        //如果是daily，直接替换，如果是线上，需要判断，线上有多个域名的情况
        String devDomain = configCenter.getUcoolDailyDomain();
        String onlineDomain = configCenter.getUcoolOnlineDomain();

        if (url.indexOf(devDomain) != -1) {
            //TODO 这里后期还是要考虑下需求
            return url.replace(devDomain, configCenter.getUcoolOnlineIp());
        } else {
            for (String d : configCenter.getUcoolOnlineDomain().split(",")) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, configCenter.getUcoolOnlineIp());
                }
            }
        }
        return url;
    }
}
