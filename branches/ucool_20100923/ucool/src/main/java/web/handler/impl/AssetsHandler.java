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
 * css��js��handler
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
         * ���ұ����ļ���û�еĻ����һ��棬û�л���Ĵ��������أ����߻��档
         */
        if (findAssetsFile(filePath)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, false));
        } else if (findCacheFile(filePath)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, true));
        } else {
            if (cacheUrlFile(request)) {
                this.fileEditor.pushFile(out, loadExistFile(filePath, true));
            } else {
                //�������ʧ���ˣ���������ʵʵ������ȡ��
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
     * �����ϵ��ļ���������
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
            //�ȴ���Ŀ¼���ļ���������д����
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
     * ��assetsĿ¼�в��ұ����޸Ĺ����ļ�
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
     * ��cacheĿ¼�����油�ļ�
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
     * ���ر����ļ����߻����ļ�
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
     * ��������������ļ��е�������������ip
     *
     * @param url of type String
     * @return String
     */
    private String urlFilter(String url) {
        //�����daily��ֱ���滻����������ϣ���Ҫ�жϣ������ж�����������
        String devDomain = configCenter.getUcoolDailyDomain();
        String onlineDomain = configCenter.getUcoolOnlineDomain();

        if (url.indexOf(devDomain) != -1) {
            //TODO ������ڻ���Ҫ����������
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
