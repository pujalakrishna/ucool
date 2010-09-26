package web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-24 18:26:00
 */
public class ComboFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String fullUrl = getFullUrl(request, response);
        if(fullUrl.indexOf("??") != -1) {
            request.setAttribute("comboUrl", fullUrl);
            request.getRequestDispatcher("/combo").forward(request, response);
        }

        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {

    }

    /**
     * Method getFullUrl ...
     *
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @return String
     */
    private String getFullUrl(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestURL()).append("?").append(request.getQueryString());
        return sb.toString();
    }

}
