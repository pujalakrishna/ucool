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
 * css和js的handler
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
         * 需要在之前做的事情:
         *  1、获取本地路径filePath和realUrl
         *  2、如果是debug模式将url提前处理
         *  3、把domain换成ip提前处理
         *  4、判断是否是线上还是daily
         */
        String filePath = (String) request.getAttribute("filePath");
        String realUrl = (String) request.getAttribute("realUrl");
        String fullUrl = realUrl;
        boolean isOnline = configCenter.getUcoolOnlineDomain().indexOf(request.getServerName()) != -1;
        if (switcher.isAssetsDebugMode() || HttpTools.isReferDebug(request)) {
            //bugfix 首页，不知道php里加了什么，在debugmode下部分js会自动请求源文件
            if(filePath.indexOf("fp-direct-promo.js") != -1) {
                filePath = filePath.replace("fp-direct-promo.js", "fp-direct-promo-min.js");
                realUrl = realUrl.replace("fp-direct-promo.js", "fp-direct-promo-min.js");
            }
            filePath = debugMode(filePath);
            realUrl = debugMode(realUrl);
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

        urlExecutor.doUrlRule(filePath, realUrl, fullUrl, isOnline, out);
    }

    /**
     * 把请求的在配置文件中的所有域名换成ip
     *
     * @param url of type String
     * @param isOnline
     * @return String
     */
    protected String urlFilter(String url, boolean isOnline) {
        /**
         * 防止定位到本地导致自循环
         * 还有一种可能是直接访问本地内网ip，这个没法子
         */
        if(url.indexOf("127.0.0.1") != -1) {
            return url.replace("127.0.0.1", configCenter.getUcoolOnlineIp());
        }
        if (isOnline) {
            for (String d : configCenter.getUcoolOnlineDomain().split(",")) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, configCenter.getUcoolOnlineIp());
                }
            }
        } else {
            for (String d : configCenter.getUcoolDailyDomain().split(",")) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, configCenter.getUcoolDailyIp());
                }
            }
        }
        return url;
    }

    /**
     * TODO 抽象出来
     * @param filePath
     * @return
     */
    protected String debugMode(String filePath) {
        if (filePath.indexOf("-min") != -1) {
            filePath = filePath.replace("-min", "");
        } else {
            if(filePath.indexOf(".source.") != -1) {
                return filePath;
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
