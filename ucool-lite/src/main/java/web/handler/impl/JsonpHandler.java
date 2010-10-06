package web.handler.impl;

import biz.JsonpParser;
import common.ConfigCenter;
import web.handler.Handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;

/**
 * jsonpµÄhandler
 *
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:34:28
 */
public class JsonpHandler implements Handler {

    private String realPath;
    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

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
        initHandler();
        String requestURL = request.getRequestURL().toString();
        String directURL = requestURL;

        Map<String, String> map = JsonpParser.readProperties(realPath);
        for (String s : map.keySet()) {
            if(requestURL.indexOf(s) != -1) {
                directURL = map.get(s);
                break;
            }
        }

        response.setCharacterEncoding("gbk");
        response.setHeader("Pragma", "No-cache");//HTTP 1.1
        response.setHeader("Cache-Control", "no-cache");//HTTP 1.0
        response.setHeader("Expires", "0");//·ÀÖ¹±»proxy

        PrintWriter out = response.getWriter();

        try {
            URL url = new URL(directURL);
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

    private void initHandler() {
        if(this.realPath != null) {
            this.realPath = this.configCenter.getWebRoot();
        }
    }
}
