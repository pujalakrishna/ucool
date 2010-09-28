package web.handler.impl;

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
     * @throws java.io.IOException      when
     * @throws javax.servlet.ServletException when
     */
    public void doHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //http://a.tbcdn.cn/p/??header/header-min.css,
        // fp/2010c/fp-base-min.css,fp/2010c/fp-channel-min.css,
        // fp/2010c/fp-product-min.css,fp/2010c/fp-mall-min.css,
        // fp/2010c/fp-category-min.css,fp/2010c/fp-sub-min.css,
        // fp/2010c/fp-gdp4p-min.css,fp/2010c/fp-css3-min.css,
        // fp/2010c/fp-misc-min.css?t=20100902.css

        /**
         * TODO combo的文件必须拆开后再combo输出
         *  1、如何支持调试？返回的是再次combo？
         */
        response.setCharacterEncoding("gbk");
        PrintWriter out = response.getWriter();

        readUrlFile(out, request);
    }
}
