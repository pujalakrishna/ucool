package web;

import biz.JsonpParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Map;

/**
 * @author <a href="mailto:zhangting@taobao.com">’≈Õ¶</a>
 * @since 2010-8-30 13:27:20
 */
public class JsonpServlet extends HttpServlet {

    private String realPath;    

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        if (realPath == null) {
            realPath = this.getServletContext().getRealPath("/");
        }
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
        response.setHeader("Expires", "0");//∑¿÷π±ªproxy
        
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

}
