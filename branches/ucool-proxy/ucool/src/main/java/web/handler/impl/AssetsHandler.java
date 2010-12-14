package web.handler.impl;

import common.UrlTools;
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


    private UrlExecutor urlExecutor;

    private UrlTools urlTools;

    public void setUrlExecutor(UrlExecutor urlExecutor) {
        this.urlExecutor = urlExecutor;
    }

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
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
        String fullUrl = (String) request.getAttribute("fullUrl");

        fullUrl = urlTools.urlFilter(fullUrl);

        response.setCharacterEncoding("gbk");
        if (fullUrl.indexOf(".css") != -1) {
            response.setContentType("text/css");
        } else {
            response.setContentType("application/x-javascript");
        }
        PrintWriter out = response.getWriter();
        //����debug�����е�ֱ����source������cache
        urlExecutor.doDebugUrlRule(fullUrl, out);

    }

}
