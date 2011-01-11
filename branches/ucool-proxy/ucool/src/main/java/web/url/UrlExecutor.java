package web.url;

import biz.file.FileEditor;
import common.ConfigCenter;
import common.UrlTools;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-10-2 13:33:26
 */
public class UrlExecutor {

    private FileEditor fileEditor;

    private UrlTools urlTools;

    private ConfigCenter configCenter;

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * 为debug模式特殊处理url请求，不走cache
     *
     * @param fullUrl of type String
     * @param out     of type PrintWriter
     * @author zhangting
     * @since 10-10-29 上午9:51
     */
    public void doDebugUrlRule(String filePath, String fullUrl, PrintWriter out) {
        if(urlTools.isOnline(fullUrl)){
            //find cache
            if (findCacheFile(filePath)) {
                this.fileEditor.pushFileOutputStream(out, loadExistFileStream(filePath, "gbk", true), filePath);
            } else if (cacheUrlFile(filePath, fullUrl)) {
                this.fileEditor.pushFileOutputStream(out, loadExistFileStream(filePath, "gbk", true), filePath);
            } else {
                readUrlFile(fullUrl, out);
            }
        } else {
            readUrlFile(fullUrl, out);
        }
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

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), urlTools.getReadCharSet(fullUrl)));
            fileEditor.pushStream(out, in);
            return true;
        } catch (Exception e) {
            out.println("/*not found*/");
        }
        return false;
    }

    /**
     * 从cache目录查找替补文件
     *
     * @param filePath    of type String
     * @return
     * @author zhangting
     * @since 2010-8-19 14:50:35
     */
    private boolean findCacheFile(String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(configCenter.getWebRoot()).append(getCacheString()).append(filePath);
        //这里如果找到了，直接返回了，下面的逻辑尽可能的少走
        return this.fileEditor.findFile(sb.toString());
    }

    /**
     * 根据编码返回新的文件流
     *
     * @param filePath of type String
     * @param encoding of type String
     * @param isCache  of type boolean
     * @return InputStreamReader
     */
    private InputStreamReader loadExistFileStream(String filePath, String encoding, boolean isCache) {
        String root = isCache ? getCacheString() : "";
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
     * 返回线上或者daily下的cache路径
     *
     * @return String
     * @author <a href="mailto:zhangting@taobao.com">zhangting</a>
     * Created on 2010-9-30
     */
    private String getCacheString() {
        return configCenter.getUcoolCacheRoot();
    }

    /**
     * 把对应域的文件缓存起来
     *
     * @param filePath
     * @param realUrl
     * @return boolean
     * @author zhangting
     * @since 2010-8-19 15:44:07
     */
    private boolean cacheUrlFile(String filePath, String realUrl) {
        try {
            URL url = new URL(realUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), urlTools.getReadCharSet(realUrl)));
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(getCacheString()).append(filePath);
            //先创建目录和文件，再往里写数据
            fileEditor.createDirectory(sb.toString());
            //缓存文件
            return fileEditor.saveFile(sb.toString(), in);
        } catch (IOException e) {
            //log
        }
        return false;
    }

}
