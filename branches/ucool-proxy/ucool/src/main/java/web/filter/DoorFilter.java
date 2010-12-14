package web.filter;

import common.ConfigCenter;
import common.HttpTools;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-24 18:26:00
 */
public class DoorFilter implements Filter {

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        String fullUrl = getFullUrl(request);
        //目前只有flash文件用到，需要302
        request.setAttribute("fullUrl", fullUrl);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) throws ServletException {
        if (configCenter == null) {
            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
            setConfigCenter((ConfigCenter) context.getBean("configCenter"));
            /**
             * 设置一下根目录的绝对路径
             */
            configCenter.setWebRoot(config.getServletContext().getRealPath("/"));
            //设置一下初始化时间
            configCenter.setLastCleanTime(new Date());
            //初始化纠正debugmode下的字符串
            configCenter.setUcoolAssetsDebugCorrectStrings(configCenter.getUcoolAssetsDebugCorrect().split(HttpTools.filterSpecialChar(",")));
            //这几个文件走utf-8编码
            configCenter.setUcoolAssetsEncodingCorrectStrings(configCenter.getUcoolAssetsEncodingCorrect().split(HttpTools.filterSpecialChar(",")));
        }
    }

    /**
     * Method getFullUrl ...
     *
     *
     * @param request  of type HttpServletRequest
     * @return String
     */
    private String getFullUrl(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestURL());
        if (request.getQueryString() != null) {
            sb.append("?").append(request.getQueryString());
        }
        return sb.toString();
    }

}
