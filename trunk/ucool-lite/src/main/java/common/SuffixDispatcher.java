package common;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:18:10
 */
public class SuffixDispatcher {

    private DispatchMapping dispatchMapping;


    public void setDispatchMapping(DispatchMapping dispatchMapping) {
        this.dispatchMapping = dispatchMapping;
    }

    /**
     * Method dispatch ...
     *
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     */
    public void dispatch(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        //配置页
        if(request.getRequestURI().equals("/pz")) {
            request.getRequestDispatcher("pz.jsp").forward(request, response);
            return;
        }
        String url = (String) request.getAttribute("realUrl");
        
        //assets用的最多，所以先判断
        if (url.indexOf(".js") != -1 || url.indexOf(".css") != -1) {
            if(url.indexOf("??") != -1) {
                //支持combo文件
                this.dispatchMapping.getMapping("combo").doHandler(request, response);
            } else {
                this.dispatchMapping.getMapping("assets").doHandler(request, response);
            }
        } else if (url.indexOf(".png") != -1 || url.indexOf(".gif") != -1 || url.indexOf(".ico") != -1) {
            this.dispatchMapping.getMapping("png").doHandler(request, response);
        } else if (url.indexOf(".htm") != -1) {
            this.dispatchMapping.getMapping("htm").doHandler(request, response);
        } else {
            this.dispatchMapping.getMapping("other").doHandler(request, response);
        }
    }

}
