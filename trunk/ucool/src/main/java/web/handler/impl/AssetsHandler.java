package web.handler.impl;

import common.ConfigCenter;
import common.HttpTools;
import common.Switcher;
import web.handler.Handler;
import web.url.UrlExecutor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * css��js��handler
 *
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:26:39
 */
public class AssetsHandler implements Handler {

    private ConfigCenter configCenter;

    private Switcher switcher;

    private UrlExecutor urlExecutor;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    protected ConfigCenter getConfigCenter() {
        return configCenter;
    }

    public void setSwitcher(Switcher switcher) {
        this.switcher = switcher;
    }

    protected Switcher getSwitcher() {
        return switcher;
    }

    public void setUrlExecutor(UrlExecutor urlExecutor) {
        this.urlExecutor = urlExecutor;
    }

    protected UrlExecutor getUrlExecutor() {
        return urlExecutor;
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
        /**
         * ��Ҫ��֮ǰ��������:
         *  1����ȡ����·��filePath��realUrl
         *  2�������debugģʽ��url��ǰ����
         *  3����domain����ip��ǰ����
         *  4���ж��Ƿ������ϻ���daily
         */
        String filePath = (String) request.getAttribute("filePath");
        String realUrl = (String) request.getAttribute("realUrl");
        String fullUrl = realUrl;
        boolean isDebugMode = switcher.isAssetsDebugMode() || HttpTools.isReferDebug(request);
        boolean isOnline = configCenter.getUcoolOnlineDomain().indexOf(request.getServerName()) != -1;
        if (isDebugMode) {
            filePath = debugMode(filePath, fullUrl);
            realUrl = debugMode(realUrl, fullUrl);
        }
        realUrl = urlFilter(realUrl, isOnline);
        fullUrl = urlFilter(fullUrl, isOnline);

        response.setCharacterEncoding("gbk");
        if(filePath.indexOf(".css") != -1) {
            response.setContentType("text/css");
        } else {
            response.setContentType("application/x-javascript");
        }
        PrintWriter out = response.getWriter();
        //����debug�����е�ֱ����source������cache
        if (isDebugMode) {
            if ("false".equals(configCenter.getUcoolAssetsDebugCache())) {
                urlExecutor.doDebugUrlRule(filePath, realUrl, fullUrl, isOnline, out);
            } else {
                urlExecutor.doUrlRule(filePath, realUrl, fullUrl, isOnline, isDebugMode, out);
            }
        } else {
            urlExecutor.doUrlRule(filePath, realUrl, fullUrl, isOnline, isDebugMode, out);
        }

    }

    /**
     * ��������������ļ��е�������������ip
     *
     * @param url of type String
     * @param isOnline
     * @return String
     */
    protected String urlFilter(String url, boolean isOnline) {
        /**
         * ��ֹ��λ�����ص�����ѭ��
         * ����һ�ֿ�����ֱ�ӷ��ʱ�������ip�����û����
         */
        if(url.indexOf("127.0.0.1") != -1) {
            return url.replace("127.0.0.1", configCenter.getUcoolOnlineIp());
        }
        if (isOnline) {
            for (String d : configCenter.getUcoolOnlineDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, configCenter.getUcoolOnlineIp());
                }
            }
        } else {
            for (String d : configCenter.getUcoolDailyDomain().split(HttpTools.filterSpecialChar(","))) {
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
     * @param fullUrl
     * @return
     */
    protected String debugMode(String filePath, String fullUrl) {
        if (filePath.indexOf("-min") != -1) {
            filePath = filePath.replace("-min", "");
        } else {
            if(filePath.indexOf(".source.") != -1) {
                return filePath;
            }
            //������ʹ�����õ��ļ������⴦��
            for (String filterString : configCenter.getUcoolAssetsDebugCorrectStrings()) {
                if(fullUrl.indexOf(filterString) != -1) {
                    return filePath;
                }
            }
            if (filePath.endsWith(".css")) {
                filePath = filePath.replace(".css", ".source.css");
            } else {
                filePath = filePath.replace(".js", ".source.js");
            }
        }
        return filePath;
    }

}
