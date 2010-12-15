package web.url;

import biz.file.FileEditor;
import common.ConfigCenter;

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
     * ȥ��request������ִ��url rule�ķ���
     *
     * @param filePath    of type String  /p/app/tc/detail_v2.css
     * @param realUrl     of type String   http://xxxx.css
     * @param fullUrl     ���ڼ�¼�����url
     * @param isOnline
     * @param isDebugMode
     * @param out         of type PrintWriter
     */
    public void doUrlRule(String filePath, String realUrl, String fullUrl, boolean isOnline, boolean isDebugMode, PrintWriter out) {
        autoCleanCache();
        /**
         * ���ұ����ļ���û�еĻ����һ��棬û�л���Ĵ��������أ����߻��档
         */
        if (findAssetsFile(filePath)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, false, isOnline));
        } else if (findCacheFile(filePath, realUrl, isOnline, isDebugMode)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, true, isOnline));
        } else {
            if (cacheUrlFile(filePath, realUrl, isOnline)) {
                this.fileEditor.pushFile(out, loadExistFile(filePath, true, isOnline));
            } else {
                if (isDebugMode) {
                    //debug mode���������-min��Դ�ļ�a.js�����������a.source.js������������ﴦ��
                    //����������Ǿ�˵�����϶�û�и��ļ�����ʹ����ѹ�����ļ�Ҳû���⣬ֻҪ��֤�����ܵ�����cache
                    filePath = filePath.replace(".source", "");
                    realUrl = realUrl.replace(".source", "");
                    doUrlRuleCopy(filePath, realUrl, fullUrl, isOnline, isDebugMode, out);
                } else {
                    //���ı��ϣ��������ʧ���ˣ�������ȡ��
                    readUrlFile(fullUrl, out);
                }
            }
        }
    }

    /**
     * Method doUrlRule 's Copy ...
     * Ϊdebug mode��ֱ�ӷ��ʴ�-min��Դ����������������kissy.js
     *
     * @param filePath of type String
     * @param realUrl of type String
     * @param fullUrl of type String
     * @param isOnline of type boolean
     * @param isDebugMode of type boolean
     * @param out of type PrintWriter
     */
    public void doUrlRuleCopy(String filePath, String realUrl, String fullUrl, boolean isOnline, boolean isDebugMode, PrintWriter out) {
        if (findAssetsFile(filePath)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, false, isOnline));
        } else if (findCacheFile(filePath, realUrl, isOnline, isDebugMode)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, true, isOnline));
        } else if (cacheUrlFile(filePath, realUrl, isOnline)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, true, isOnline));
        } else {
            //���ı��ϣ��������ʧ���ˣ�������ȡ��
            readUrlFile(fullUrl, out);
        }
    }

    /**
     * Method autoCleanCache ...
     */
    private void autoCleanCache() {
        if ("true".equals(configCenter.getUcoolCacheAutoClean())) {
            //��ֹ����ͬʱ����ɾ��
            configCenter.setUcoolCacheAutoClean("false");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(configCenter.getLastCleanTime());
            calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(configCenter.getUcoolCacheCleanPeriod()));
            if (!calendar.after(Calendar.getInstance())) {
                fileEditor.removeDirectory(configCenter.getWebRoot() + configCenter.getUcoolCacheRoot());
                configCenter.setLastCleanTime(new Date());
            }
            configCenter.setUcoolCacheAutoClean("true");
        }
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
        /**
         * ���ұ����ļ���û�еĻ����һ��棬û�л���Ĵ��������أ����߻��档
         */
        if (findAssetsFile(filePath)) {
            this.fileEditor.pushFileOutputStream(out, loadExistFileStream(filePath, "gbk", false, isOnline));
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
        if (configCenter.isEnableAssets()) {
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(configCenter.getUcoolAssetsRoot()).append(filePath);
            return this.fileEditor.findFile(sb.toString());
        }
        return false;
    }

    /**
     * ��cacheĿ¼�����油�ļ�
     *
     * @param filePath    of type String
     * @param realUrl
     * @param isOnline
     * @param isDebugMode @return boolean
     * @return
     * @author zhangting
     * @since 2010-8-19 14:50:35
     */
    private boolean findCacheFile(String filePath, String realUrl, boolean isOnline, Boolean isDebugMode) {
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
            //  ������ʹ�����õ��ļ������⴦���Ѹ������ļ�ʹ��utf-8����
//            for (String enCodingString : configCenter.getUcoolAssetsEncodingCorrectStrings()) {
//                if (realUrl.indexOf(enCodingString) != -1) {
//                    encoding = "utf-8";
//                    break;
//                }
//            }
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
     * ���ݱ��뷵���µ��ļ���
     *
     * @param filePath of type String
     * @param encoding of type String
     * @param isCache of type boolean
     * @param isOnline of type boolean
     * @return InputStreamReader
     */
    private InputStreamReader loadExistFileStream(String filePath, String encoding, boolean isCache, boolean isOnline) {
        String root = isCache ? getCacheString(isOnline) : configCenter.getUcoolAssetsRoot();
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
        return configCenter.getUcoolCacheRoot();
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
            //  ������ʹ�����õ��ļ������⴦���Ѹ������ļ�ʹ��utf-8����
//            for (String enCodingString : configCenter.getUcoolAssetsEncodingCorrectStrings()) {
//                if (fullUrl.indexOf(enCodingString) != -1) {
//                    encoding = "utf-8";
//                    break;
//                }
//            }
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
            return fileEditor.pushStream(out, in);
        } catch (Exception e) {
            System.out.println("");
        }
//        out.flush();
        return false;
    }
}
