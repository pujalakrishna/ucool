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
         * combo的文件必须拆开后再combo输出
         */
        String realUrl = (String) request.getAttribute("realUrl");
        String filePath = (String) request.getAttribute("filePath"); // e.g.:/p/

        String[] firstCut = realUrl.split("\\?\\?");
        String pathPrefix = firstCut[0];    // e.g.:http://a.tbcdn.cn/p/
        String[] allFiles = firstCut[1].split(",");

        response.setCharacterEncoding("gbk");
        if (realUrl.indexOf(".css") != -1) {
            response.setContentType("text/css");
        } else {
            response.setContentType("application/x-javascript");
        }
        PrintWriter out = response.getWriter();

        boolean isOnline = getConfigCenter().getUcoolOnlineDomain().indexOf(request.getServerName()) != -1;

        for (String everyFile : allFiles) {
            // e.g.:header/header-min.css
            //拼出单个url，然后的逻辑和单文件相同
            String singleFilePath = filePath + everyFile;
            String singleRealUrl = pathPrefix + everyFile;
            String singleFullUrl = singleRealUrl;

            //在debug过滤之前还要过滤时间戳
            singleFilePath = singleFilePath.split("\\?")[0];
            //获取源文件url
            if (getSwitcher().isAssetsDebugMode() || HttpTools.isReferDebug(request)) {
                singleFilePath = debugMode(singleFilePath);
                singleRealUrl = debugMode(singleRealUrl);
            }

            singleRealUrl = urlFilter(singleRealUrl, isOnline);
            singleFullUrl = urlFilter(singleFullUrl, isOnline);

            //尝试debug下所有的直接走source，不走cache
            if (getSwitcher().isAssetsDebugMode() || HttpTools.isReferDebug(request)) {
                if ("false".equals(getConfigCenter().getUcoolAssetsDebugCache())) {
                    getUrlExecutor().doDebugUrlRule(singleFilePath, singleRealUrl, singleFullUrl, isOnline, out);
                } else {
                    getUrlExecutor().doUrlRule(singleFilePath, singleRealUrl, singleFullUrl, isOnline, out);
                }
            } else {
                getUrlExecutor().doUrlRule(singleFilePath, singleRealUrl, singleFullUrl, isOnline, out);
            }
        }

    }

}
