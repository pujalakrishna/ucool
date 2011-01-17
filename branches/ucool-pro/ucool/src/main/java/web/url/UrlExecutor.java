package web.url;

import biz.file.FileEditor;
import common.ConfigCenter;
import common.PersonConfig;
import common.UrlTools;

import java.io.*;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-10-2 13:33:26
 */
public class UrlExecutor {

    private FileEditor fileEditor;

    private ConfigCenter configCenter;

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * Ϊdebugģʽ���⴦��url���󣬲���cache
     *
     * @param filePath of type String
     * @param realUrl  of type String
     * @param fullUrl  of type String
     * @param isOnline of type boolean
     * @param out      of type PrintWriter
     * @author zhangting
     * @since 10-10-29 ����9:51
     */
    public void doDebugUrlRule(String filePath, String realUrl, String fullUrl, boolean isOnline, PrintWriter out) {
        if (findAssetsFile(filePath)) {
            this.fileEditor.pushFileOutputStream(out, loadExistFileStream(filePath, "gbk", false, isOnline), filePath);
        } else {
            if (!readUrlFile(realUrl, out)) {
                //���ı��ϣ��������ʧ���ˣ�������ȡ��
                readUrlFile(fullUrl, out);
            }
        }
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
        if (personConfig.isEnableAssets()) {
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(personConfig.getUcoolAssetsRoot()).append(filePath);
            return this.fileEditor.findFile(sb.toString());
        }
        return false;
    }

    /**
     * ��cacheĿ¼�����油�ļ�
     *
     * @param filePath    of type String
     * @param isOnline
     * @return
     * @author zhangting
     * @since 2010-8-19 14:50:35
     */
    private boolean findCacheFile(String filePath, boolean isOnline) {
        StringBuilder sb = new StringBuilder();
        sb.append(configCenter.getWebRoot()).append(getCacheString(isOnline)).append(filePath);
        //��������ҵ��ˣ�ֱ�ӷ����ˣ�������߼������ܵ�����
        return this.fileEditor.findFile(sb.toString());
    }

    /**
     * �Ѷ�Ӧ����ļ���������
     *
     * @param filePath
     * @param realUrl
     * @param isOnline
     * @return boolean
     * @author zhangting
     * @since 2010-8-19 15:44:07
     */
    private boolean cacheUrlFile(String filePath, String realUrl, boolean isOnline) {
        try {
            URL url = new URL(realUrl);
            String encoding = "gbk";
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(getCacheString(isOnline)).append(filePath);
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
     * ���ݱ��뷵���µ��ļ���
     *
     * @param filePath of type String
     * @param encoding of type String
     * @param isCache  of type boolean
     * @param isOnline of type boolean
     * @return InputStreamReader
     */
    private InputStreamReader loadExistFileStream(String filePath, String encoding, boolean isCache, boolean isOnline) {
        String root = isCache ? getCacheString(isOnline) : personConfig.getUcoolAssetsRoot();
        StringBuilder sb = new StringBuilder();
        sb.append(configCenter.getWebRoot()).append(root).append(filePath);
        try {
            return this.fileEditor.loadFileStream(sb.toString(), encoding);
        } catch (FileNotFoundException e) {

        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }

    /**
     * �������ϻ���daily�µ�cache·��
     *
     * @param isOnline
     * @return String
     * @author <a href="mailto:zhangting@taobao.com">zhangting</a>
     * Created on 2010-9-30
     */
    private String getCacheString(boolean isOnline) {
        return personConfig.getUcoolCacheRoot();
    }


    /**
     * Method readUrlFile ...
     *
     * @param fullUrl of type String
     * @param out     of type PrintWriter
     * @return
     */
    private boolean readUrlFile(String fullUrl, PrintWriter out) {
        try {
            URL url = new URL(fullUrl);
            String encoding = "gbk";
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
            fileEditor.pushStream(out, in, fullUrl, false);
            return true;
        } catch (Exception e) {
        }
//        out.flush();
        return false;
    }
}
