package web.handler.impl;

import common.HttpTools;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-24 11:04:09
 */
public class ComboHandler extends AssetsHandler {
    /**
     * Method doHandler ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws java.io.IOException            when
     * @throws javax.servlet.ServletException when
     */
    public void doHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        /**
         * combo url example:
         *
         * http://a.tbcdn.cn/p/??header/header-min.css,
         * fp/2010c/fp-base-min.css,fp/2010c/fp-channel-min.css,
         * fp/2010c/fp-product-min.css,fp/2010c/fp-mall-min.css,
         * fp/2010c/fp-category-min.css,fp/2010c/fp-sub-min.css,
         * fp/2010c/fp-gdp4p-min.css,fp/2010c/fp-css3-min.css,
         * fp/2010c/fp-misc-min.css?t=20100902.css
         *
         */

        /**
         * combo���ļ�����𿪺���combo���
         */
        String realUrl = (String) request.getAttribute("realUrl");
        String filePath = (String) request.getAttribute("filePath"); // e.g.:/p/

        String[] firstCut = realUrl.split(HttpTools.filterSpecialChar(getConfigCenter().getUcoolComboDecollator()));
        String pathPrefix = firstCut[0];    // e.g.:http://a.tbcdn.cn/p/
        String[] allFiles = firstCut[1].split(HttpTools.filterSpecialChar(","));

        response.setCharacterEncoding("gbk");
        if (realUrl.indexOf(".css") != -1) {
            response.setContentType("text/css");
        } else {
            response.setContentType("application/x-javascript");
        }
        PrintWriter out = response.getWriter();

        boolean isOnline = getConfigCenter().getUcoolOnlineDomain().indexOf(request.getServerName()) != -1;
        boolean isDebugMode = getSwitcher().isAssetsDebugMode() || HttpTools.isReferDebug(request);
        for (String everyFile : allFiles) {
            // e.g.:header/header-min.css
            //ƴ������url��Ȼ����߼��͵��ļ���ͬ
            String singleFilePath = filePath + everyFile;
            String singleRealUrl = pathPrefix + everyFile;
            String singleFullUrl = singleRealUrl;

            //��debug����֮ǰ��Ҫ����ʱ���
            singleFilePath = singleFilePath.split(HttpTools.filterSpecialChar("?"))[0];
            //��ȡԴ�ļ�url
            if (isDebugMode) {
                singleFilePath = getUrlTools().debugMode(singleFilePath, singleFullUrl);
                singleRealUrl = getUrlTools().debugMode(singleRealUrl, singleFullUrl);
            }

            singleRealUrl = getUrlTools().urlFilter(singleRealUrl, isOnline);
            singleFullUrl = getUrlTools().urlFilter(singleFullUrl, isOnline);

            //����debug�����е�ֱ����source������cache
            //daily��Ԥ��ֻ��һ̨������û��Ҫ��cache��
            if (!isOnline || getConfigCenter().isPrepub()) {
                getUrlExecutor().doDebugUrlRule(singleFilePath, singleRealUrl, singleFullUrl, isOnline, out);
            } else {
                getUrlExecutor().doUrlRule(singleFilePath, singleRealUrl, singleFullUrl, isOnline, isDebugMode, out);
            }
        }

    }

}
