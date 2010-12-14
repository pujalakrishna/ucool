package web.url;

import biz.file.FileEditor;
import common.ConfigCenter;

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
     * @param fullUrl of type String
     * @param out     of type PrintWriter
     * @author zhangting
     * @since 10-10-29 上午9:51
     */
    public void doDebugUrlRule(String fullUrl, PrintWriter out) {
        readUrlFile(fullUrl, out);
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
            for (String enCodingString : configCenter.getUcoolAssetsEncodingCorrectStrings()) {
                if (fullUrl.indexOf(enCodingString) != -1) {
                    encoding = "utf-8";
                    break;
                }
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), encoding));
            fileEditor.pushStream(out, in);
            return true;
        } catch (Exception e) {
            System.out.println("");
        }
//        out.flush();
        return false;
    }
}
