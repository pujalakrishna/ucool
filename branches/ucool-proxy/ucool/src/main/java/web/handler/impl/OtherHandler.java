package web.handler.impl;

import biz.file.FileEditor;
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
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-10-2 20:12:54
 */
public class OtherHandler implements Handler {

    private FileEditor fileEditor;


    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    @Override
    public void doHandler(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String fullUrl = (String) request.getAttribute("fullUrl");
        
        if (fullUrl.indexOf(".swf") != -1) {
            //哥对flash没办法，无论怎么取都无法正确展现，只好302
            response.sendRedirect(fullUrl);
        } else if (fullUrl.indexOf(".xml") != -1) {
            response.setContentType("text/xml");

            PrintWriter out = response.getWriter();
            try {
                URL url = new URL(fullUrl);
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                fileEditor.pushStream(out, in);
            } catch (Exception e) {
            }
            out.flush();
        }

    }
}
