package web.url;

import biz.file.FileEditor;
import common.ConfigCenter;

import java.io.*;
import java.net.URL;

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
     * ȥ��request������ִ��url rule�ķ���
     *
     * @param filePath of type String
     * @param realUrl of type String
     * @param out of type PrintWriter
     */
    public void doUrlRule(String filePath, String realUrl, boolean isOnline, PrintWriter out) {
        /**
         * �һ��棬û�л���Ĵ��������أ����߻��档
         */
        if (findCacheFile(filePath, isOnline)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, isOnline, true));
        } else {
            if (cacheUrlFile(filePath, realUrl, isOnline)) {
                this.fileEditor.pushFile(out, loadExistFile(filePath, isOnline, true));
            } else {
                //���ı��ϣ��������ʧ���ˣ�������ȡ��
                readUrlFile(realUrl, out);
            }
        }
    }

    /**
     * ��cacheĿ¼�����油�ļ�
     *
     * @param filePath of type String
     * @param isOnline
     * @return boolean
     * @author zhangting
     * @since 2010-8-19 14:50:35
     */
    private boolean findCacheFile(String filePath, boolean isOnline) {
        StringBuilder sb = new StringBuilder();
        sb.append(configCenter.getWebRoot()).append(getCacheString(isOnline)).append(filePath);
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
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
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
     * ���ر����ļ����߻����ļ�
     *
     * @param filePath of type String
     * @param isCache  of type boolean
     * @return FileInputStream
     * @author zhangting
     * @since 2010-8-19 15:22:02
     */
    private FileReader loadExistFile(String filePath, boolean isCache, boolean isOnline) {
        String root = isCache ? getCacheString(isOnline) : configCenter.getUcoolAssetsRoot();
        StringBuilder sb = new StringBuilder();
        sb.append(configCenter.getWebRoot()).append(root).append(filePath);
        try {
            return this.fileEditor.loadFile(sb.toString());
        } catch (FileNotFoundException e) {

        }
        return null;
    }

    /**
     * �������ϻ���daily�µ�cache·��
     *
     * @param isOnline
     * @author <a href="mailto:zhangting@taobao.com">zhangting</a>
     * Created on 2010-9-30
     * @return String
     */
    private String getCacheString(boolean isOnline) {
        if(isOnline) {
            return configCenter.getUcoolCacheRootOnline();
        } else {
            return configCenter.getUcoolCacheRootDaily();
        }
    }


    /**
     * Method readUrlFile ...
     *
     * @param realUrl of type String
     * @param out of type PrintWriter
     */
    private void readUrlFile(String realUrl, PrintWriter out) {
        try {
            URL url = new URL(realUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            fileEditor.pushStream(out, in);
        } catch (Exception e) {
        }
//        out.flush();
    }
}
