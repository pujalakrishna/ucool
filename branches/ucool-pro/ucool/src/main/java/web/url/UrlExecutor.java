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
     * 为debug模式特殊处理url请求，不走cache
     *
     *
     * @param filePath of type String
     * @param realUrl  of type String
     * @param fullUrl  of type String
     * @param out      of type PrintWriter
     * @param personConfig
     * @author zhangting
     * @since 10-10-29 上午9:51
     */
    public void doDebugUrlRule(String filePath, String realUrl, String fullUrl, PrintWriter out, PersonConfig personConfig) {
        if (findAssetsFile(filePath, personConfig)) {
            this.fileEditor.pushFileOutputStream(out, loadExistFileStream(filePath, "gbk", personConfig), filePath);
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
     *
     * @param filePath of type String
     * @param personConfig
     * @return boolean
     * @author zhangting
     * @since 2010-8-19 14:49:26
     */
    private boolean findAssetsFile(String filePath, PersonConfig personConfig) {
        if (personConfig.isEnableAssets()) {
            StringBuilder sb = new StringBuilder();
            sb.append(configCenter.getWebRoot()).append(personConfig.getUcoolAssetsRoot()).append(filePath);
            return this.fileEditor.findFile(sb.toString());
        }
        return false;
    }

    /**
     * 根据编码返回新的文件流
     *
     *
     * @param filePath of type String
     * @param encoding of type String
     * @param personConfig
     * @return InputStreamReader
     */
    private InputStreamReader loadExistFileStream(String filePath, String encoding, PersonConfig personConfig) {
        String root = personConfig.getUcoolAssetsRoot();
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
