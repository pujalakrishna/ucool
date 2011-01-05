package web.handler.impl;

import common.UrlTools;
import web.handler.Handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:34:55
 */
public class PngHandler implements Handler {

    private UrlTools urlTools;

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    @Override
    public void doHandler(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String fullUrl = (String) request.getAttribute("fullUrl");
        //临时处理下png
        if(fullUrl.indexOf("png") != -1) {
            response.setContentType("image/png");
        } else if(fullUrl.indexOf("gif") != -1) {
            response.setContentType("image/gif");
        } else {
            response.setContentType("image/x-icon");
        }

        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());//输出缓冲流
        try {
            URL url = new URL(fullUrl);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            byte[] data = new byte[4096];
            int size = in.read(data);
            while (size != -1) {
                bos.write(data, 0, size);
                size = in.read(data);
            }
            in.close();
            bos.flush();//清空输出缓冲流
            bos.close();
            in.close();
        } catch (Exception e) {
//            bos.write(new Byte("file not find"));
        }
        bos.flush();
    }
}
