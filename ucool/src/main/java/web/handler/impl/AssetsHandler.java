package web.handler.impl;

import common.ConfigCenter;
import common.HttpTools;
import common.Switcher;
import common.UrlTools;
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

    private UrlTools urlTools;

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

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    protected UrlTools getUrlTools() {
        return urlTools;
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
        realUrl = attachOper(realUrl, request);
        String fullUrl = realUrl;
        boolean isDebugMode = switcher.isAssetsDebugMode() || HttpTools.isReferDebug(request);
        boolean isOnline = configCenter.getUcoolOnlineDomain().indexOf(request.getServerName()) != -1;
        if (isDebugMode) {
            filePath = urlTools.debugMode(filePath, fullUrl);
            realUrl = urlTools.debugMode(realUrl, fullUrl);
        }
        realUrl = urlTools.urlFilter(realUrl, isOnline);
        fullUrl = urlTools.urlFilter(fullUrl, isOnline);

        response.setCharacterEncoding("gbk");
        if(filePath.indexOf(".css") != -1) {
            response.setContentType("text/css");
        } else {
            response.setContentType("application/x-javascript");
        }
        PrintWriter out = response.getWriter();
        //尝试debug下所有的直接走source，不走cache
        //线上缓存已经迁移至ucool-proxy
        urlExecutor.doDebugUrlRule(filePath, realUrl, fullUrl, isOnline, isDebugMode, out);
    }

    public String attachOper(String fullUrl, HttpServletRequest request) {
        String op = (String) request.getAttribute("op");
        boolean referClean = HttpTools.isReferClean(request);
        if((op!= null && !op.isEmpty()) || referClean) {
            if(fullUrl.indexOf("?") != -1) {
                fullUrl += "&";
            } else {
                fullUrl += "?";
            }
            if(referClean) {
                op = "clean";
            }
            fullUrl += ("op="+op);
        }
        return fullUrl;
    }

}
