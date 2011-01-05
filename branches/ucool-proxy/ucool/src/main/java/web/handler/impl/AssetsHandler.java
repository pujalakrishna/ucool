package web.handler.impl;

import common.ConfigCenter;
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

    private UrlExecutor urlExecutor;

    public void setUrlExecutor(UrlExecutor urlExecutor) {
        this.urlExecutor = urlExecutor;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
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
        String fullUrl = (String) request.getAttribute("fullUrl");

        response.setCharacterEncoding("gbk");
        if (fullUrl.indexOf(".css") != -1) {
            response.setContentType("text/css");
        } else {
            response.setContentType("application/x-javascript");
        }
        PrintWriter out = response.getWriter();

        urlExecutor.doDebugUrlRule(fullUrl, out);
    }

}
