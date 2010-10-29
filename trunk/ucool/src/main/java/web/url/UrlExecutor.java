package web.url;

import biz.file.FileEditor;
import common.ConfigCenter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

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
     * 去除request依赖的执行url rule的方法
     *
     * @param filePath of type String  /p/app/tc/detail_v2.css
     * @param realUrl  of type String   http://xxxx.css
     * @param fullUrl  用于记录最初的url
     * @param isOnline
     * @param out      of type PrintWriter
     */
    public void doUrlRule(String filePath, String realUrl, String fullUrl, boolean isOnline, PrintWriter out) {
        if ("true".equals(configCenter.getUcoolCacheAutoClean())) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(configCenter.getLastCleanTime());
            calendar.add(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(configCenter.getUcoolCacheCleanPeriod()));
            if (!calendar.after(Calendar.getInstance())) {
                fileEditor.removeDirectory(configCenter.getWebRoot() + configCenter.getUcoolCacheRootOnline());
                fileEditor.removeDirectory(configCenter.getWebRoot() + configCenter.getUcoolCacheRootDaily());
            }
        }
        /**
         * 查找本地文件，没有的话再找缓存，没有缓存的从线上下载，再走缓存。
         */
        if (findAssetsFile(filePath)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, false, isOnline));
        } else if (findCacheFile(filePath, isOnline)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, true, isOnline));
        } else {
            if (cacheUrlFile(filePath, realUrl, isOnline)) {
                this.fileEditor.pushFile(out, loadExistFile(filePath, true, isOnline));
            } else {
                //最后的保障，如果缓存失败了，从线上取吧
                readUrlFile(fullUrl, out);
            }
        }
    }

    /**
     * 为debug模式特殊处理url请求，不走cache
     *
     * @param filePath of type String
     * @param realUrl  of type String
     * @param fullUrl  of type String
     * @param isOnline of type boolean
     * @param out      of type PrintWriter
     * @author zhangting
     * @since 10-10-29 上午9:51
     */
    public void doDebugUrlRule(String filePath, String realUrl, String fullUrl, boolean isOnline, PrintWriter out) {
        /**
         * 查找本地文件，没有的话再找缓存，没有缓存的从线上下载，再走缓存。
         */
        if (findAssetsFile(filePath)) {
            this.fileEditor.pushFile(out, loadExistFile(filePath, false, isOnline));
        } else {
            if (!readUrlFile(realUrl, out)) {
                //最后的保障，如果缓存失败了，从线上取吧
                readUrlFile(fullUrl, out);
            }
        }
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
        sb.append(configCenter.getWebRoot()).append(configCenter.getUcoolAssetsRoot()).append(filePath);
        return this.fileEditor.findFile(sb.toString());
    }

    /**
     * 从cache目录查找替补文件
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
     * 把对应域的文件缓存起来
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
            if (((HttpURLConnection) url.openConnection()).getResponseCode() == 404) {
                return false;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(getCacheString(isOnline)).append(filePath);
            //先创建目录和文件，再往里写数据
            fileEditor.createDirectory(sb.toString());
            //缓存文件
            return fileEditor.saveFile(sb.toString(), in);
        } catch (IOException e) {
            //log
        }
        return false;
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
     * 返回线上或者daily下的cache路径
     *
     * @param isOnline
     * @return String
     * @author <a href="mailto:zhangting@taobao.com">zhangting</a>
     * Created on 2010-9-30
     */
    private String getCacheString(boolean isOnline) {
        if (isOnline) {
            return configCenter.getUcoolCacheRootOnline();
        } else {
            return configCenter.getUcoolCacheRootDaily();
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
            if (((HttpURLConnection) url.openConnection()).getResponseCode() == 404) {
                return false;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            fileEditor.pushStream(out, in);
            return true;
        } catch (Exception e) {
        }
//        out.flush();
        return false;
    }
}
