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
     * 去除request依赖的执行url rule的方法
     *
     * @param filePath    of type String  /p/app/tc/detail_v2.css
     * @param realUrl     of type String   http://xxxx.css
     * @param fullUrl     用于记录最初的url
     * @param isOnline
     * @param isDebugMode
     * @param out         of type PrintWriter
     */
    public void doUrlRule(String filePath, String realUrl, String fullUrl, boolean isOnline, boolean isDebugMode, PrintWriter out) {
        autoCleanCache();
        /**
         * 查找本地文件，没有的话再找缓存，没有缓存的从线上下载，再走缓存。
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
                    //debug mode下如果请求-min的源文件a.js，会出现请求a.source.js的情况，到这里处理
                    //如果到这里那就说明线上都没有改文件，即使返回压缩的文件也没问题，只要保证尽可能的命中cache
                    filePath = filePath.replace(".source", "");
                    realUrl = realUrl.replace(".source", "");
                    doUrlRuleCopy(filePath, realUrl, fullUrl, isOnline, isDebugMode, out);
                } else {
                    //最后的保障，如果缓存失败了，从线上取吧
                    readUrlFile(fullUrl, out);
                }
            }
        }
    }

    /**
     * Method doUrlRule 's Copy ...
     * 为debug mode下直接访问带-min的源码而创建，例如访问kissy.js
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
            //最后的保障，如果缓存失败了，从线上取吧
            readUrlFile(fullUrl, out);
        }
    }

    /**
     * Method autoCleanCache ...
     */
    private void autoCleanCache() {
        if ("true".equals(configCenter.getUcoolCacheAutoClean())) {
            //防止并发同时进行删除
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
            this.fileEditor.pushFileOutputStream(out, loadExistFileStream(filePath, "gbk", false, isOnline));
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
        if (configCenter.isEnableAssets()) {
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(configCenter.getUcoolAssetsRoot()).append(filePath);
            return this.fileEditor.findFile(sb.toString());
        }
        return false;
    }

    /**
     * 从cache目录查找替补文件
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
        //这里如果找到了，直接返回了，下面的逻辑尽可能的少走
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
            String encoding = "gbk";
            //  在这里使用配置的文件作特殊处理，把给定的文件使用utf-8编码
//            for (String enCodingString : configCenter.getUcoolAssetsEncodingCorrectStrings()) {
//                if (realUrl.indexOf(enCodingString) != -1) {
//                    encoding = "utf-8";
//                    break;
//                }
//            }
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
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
     * 根据编码返回新的文件流
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
     * 返回线上或者daily下的cache路径
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
            //  在这里使用配置的文件作特殊处理，把给定的文件使用utf-8编码
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
