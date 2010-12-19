package web.handler.impl;

import common.*;
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

    private UrlTools urlTools;

    private PersonConfig personConfig;

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

    public void setPersonConfig(PersonConfig personConfig) {
        this.personConfig = personConfig;
    }

    protected PersonConfig getPersonConfig() {
        return personConfig;
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
        //����debug�����е�ֱ����source������cache
        //daily��Ԥ��ֻ��һ̨������û��Ҫ��cache��
        if (!isOnline || personConfig.isPrepub()) {
            urlExecutor.doDebugUrlRule(filePath, realUrl, fullUrl, isOnline, out);
        } else {
            urlExecutor.doUrlRule(filePath, realUrl, fullUrl, isOnline, isDebugMode, out);
        }
    }

}
