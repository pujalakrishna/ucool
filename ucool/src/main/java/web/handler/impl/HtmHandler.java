package web.handler.impl;

import common.ConfigCenter;
import common.UrlTools;
import web.handler.Handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

/**
 * jsonpµÄhandler
 *
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:34:28
 */
public class HtmHandler implements Handler {
    /**
     * Method doHandler ...
     *
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws IOException when
     * @throws ServletException when
     */
    @Override
    public void doHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String fullUrl = (String) request.getAttribute("fullUrl");

        response.setCharacterEncoding("gbk");
        response.setHeader("Pragma", "No-cache");//HTTP 1.1
        response.setHeader("Cache-Control", "no-cache");//HTTP 1.0
        response.setHeader("Expires", "0");//·ÀÖ¹±»proxy

        PrintWriter out = response.getWriter();

        try {
            URL url = new URL(fullUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                out.println(line);
            }
            in.close();
        } catch (Exception e) {
            out.println("file not find");
        }
        out.flush();
    }

}
