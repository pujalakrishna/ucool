package web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * @author <a href="mailto:zhangting@taobao.com">张挺</a>
 * @since 2010-8-20 14:38:41
 */
public class PngServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
//临时处理下png
        response.setContentType("image/png");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());//输出缓冲流
        try {
            URL url = new URL("http://a.tbcdn.cn" + request.getRequestURI());
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            String line;
            byte[] data = new byte[4096];
            int size = 0;
            size = in.read(data);
            while (size != -1) {
                bos.write(data, 0, size);
                size = in.read(data);
            }
            in.close();
            bos.flush();//清空输出缓冲流
            bos.close();
            in.close();
        } catch (Exception e) {
            bos.write(new Byte("file not find"));
        }
        bos.flush();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
