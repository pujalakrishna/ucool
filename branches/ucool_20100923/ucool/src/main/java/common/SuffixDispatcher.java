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
        String uri = request.getRequestURI();
        if (uri.indexOf(".htm") != -1) {
            this.dispatchMapping.getMapping("htm").doHandler(request, response);
        } else if (uri.indexOf(".png") != -1 || uri.indexOf(".gif") != -1) {
            this.dispatchMapping.getMapping("png").doHandler(request, response);
        } else if (uri.indexOf(".js") != -1 || uri.indexOf(".css") != -1) {
            this.dispatchMapping.getMapping("assets").doHandler(request, response);
        }
    }

}
