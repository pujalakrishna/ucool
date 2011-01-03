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

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
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

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), urlTools.getReadCharSet(fullUrl)));
            fileEditor.pushStream(out, in);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
}
