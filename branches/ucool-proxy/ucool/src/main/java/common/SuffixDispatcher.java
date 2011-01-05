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

    private UrlTools urlTools;

    public void setDispatchMapping(DispatchMapping dispatchMapping) {
        this.dispatchMapping = dispatchMapping;
    }

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    /**
     * Method dispatch ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     */
    public void dispatch(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {

        String fullUrl = (String) request.getAttribute("fullUrl");
        String env = request.getParameter("env");
        env = (env == null) ? "online" : env;
        fullUrl = urlTools.urlFilter(fullUrl, env);
        request.setAttribute("fullUrl", fullUrl);

        //assets用的最多，所以先判断
        if (fullUrl.indexOf(".js") != -1 || fullUrl.indexOf(".css") != -1) {
            this.dispatchMapping.getMapping("assets").doHandler(request, response);
        } else if (fullUrl.indexOf(".png") != -1 || fullUrl.indexOf(".gif") != -1 || fullUrl.indexOf(".ico") != -1) {
            //图片处理 目前有png,gif,ico
            this.dispatchMapping.getMapping("png").doHandler(request, response);
        } else if (fullUrl.indexOf(".htm") != -1) {
            // htm页面处理
            this.dispatchMapping.getMapping("htm").doHandler(request, response);
        } else {
            // 其他格式的处理，目前包括swf和xml
            this.dispatchMapping.getMapping("other").doHandler(request, response);
        }
    }

}
