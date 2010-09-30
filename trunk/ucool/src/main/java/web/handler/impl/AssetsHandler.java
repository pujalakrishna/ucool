package web.handler.impl;

import biz.file.FileEditor;
import common.ConfigCenter;
import common.Switcher;
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

    /**
     * @deprecated �Ѿ�����cacheRootDaily��cacheRootOnline
     */
    @Deprecated
    private String cacheRoot;

    private String cacheRootDaily;

    private String cacheRootOnline;    

    protected FileEditor fileEditor;

    private ConfigCenter configCenter;

    private Switcher switcher;

    private boolean isOnline = false;

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setSwitcher(Switcher switcher) {
        this.switcher = switcher;
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
        if(configCenter.getUcoolOnlineDomain().indexOf(request.getServerName()) != -1) {
            this.isOnline = true;
        } else {
            this.isOnline = false;
        }
        String filePath = request.getRequestURI();
        if (switcher.isAssetsDebugMode()) {
            filePath = debugMode(filePath);
        }
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

    protected void initHandler() {
        if (this.realPath == null || this.assetsRoot == null) {
            this.realPath = this.configCenter.getWebRoot();
            this.assetsRoot = this.configCenter.getUcoolAssetsRoot();
            this.cacheRoot = this.configCenter.getUcoolCacheRoot();
            this.cacheRootDaily = this.configCenter.getUcoolCacheRootDaily();
            this.cacheRootOnline = this.configCenter.getUcoolCacheRootOnline();
        }
    }

    /**
     * �Ѷ�Ӧ����ļ���������
     *
     * @param request of type String
     * @return boolean
     * @author zhangting
     * @since 2010-8-19 15:44:07
     */
    protected boolean cacheUrlFile(HttpServletRequest request) {
        String allUrl = request.getRequestURL().toString();
        String filePath = request.getRequestURI();
        if (switcher.isAssetsDebugMode()) {
            allUrl = debugMode(allUrl);
            filePath = debugMode(filePath);
        }
        allUrl = urlFilter(allUrl, false);

        try {
            URL url = new URL(allUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            sb.append(realPath).append(getCacheString()).append(filePath);
            //�ȴ���Ŀ¼���ļ���������д����
            fileEditor.createDirectory(sb.toString());
            //�����ļ�
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
    protected void readUrlFile(PrintWriter out, HttpServletRequest request)
            throws javax.servlet.ServletException, IOException {
        String allUrl = request.getRequestURL().toString() + "?" + request.getQueryString();
        if(request.getRequestURI().equals("/combo")) {
            allUrl = (String) request.getAttribute("comboUrl");
        }
        //����ǿ���������ˣ�һ��Ҳ���ᵽ��һ����daily��������ͳһ��
        allUrl = urlFilter(allUrl, true);

        try {
            URL url = new URL(allUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            fileEditor.pushStream(out, in);
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
    protected boolean findAssetsFile(String filePath) {
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
    protected boolean findCacheFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(realPath).append(getCacheString()).append(filePath);
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
    protected FileReader loadExistFile(String filePath, boolean isCache) {
        String root = isCache ? getCacheString() : assetsRoot;
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
    private String urlFilter(String url, boolean force) {
        if (force || this.isOnline) {
            for (String d : configCenter.getUcoolOnlineDomain().split(",")) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, configCenter.getUcoolOnlineIp());
                }
            }
        } else {
            for (String d : configCenter.getUcoolDailyDomain().split(",")) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, configCenter.getUcoolDailyIp());
                }
            }
        }
        return url;
    }

    /**
     * TODO �������
     * @param filePath
     * @return
     */
    private String debugMode(String filePath) {
        if (filePath.indexOf("-min") != -1) {
            filePath = filePath.replace("-min", "");
        } else {
            if(filePath.indexOf(".source.") != -1) {
                return filePath;
            }
            if (filePath.endsWith(".css")) {
                filePath = filePath.replace(".css", ".source.css");
            } else {
                filePath = filePath.replace(".js", ".source.js");
            }
        }
        return filePath;
    }

    /**
     * �������ϻ���daily�µ�cache·��
     *
     * @author <a href="mailto:zhangting@taobao.com">zhangting</a>
     * Created on 2010-9-30
     * @return String
     */
    private String getCacheString() {
        if(this.isOnline) {
            return configCenter.getUcoolCacheRootOnline();
        } else {
            return configCenter.getUcoolCacheRootDaily();
        }
    }
}
