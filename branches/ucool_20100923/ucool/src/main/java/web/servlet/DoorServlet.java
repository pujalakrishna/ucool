package web.servlet;

import common.ConfigCenter;
import common.SuffixDispatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="mailto:zhangting@taobao.com">张挺</a>
 * @since 2010-9-20 15:06:19
 */
public class DoorServlet extends HttpServlet {

    private ConfigCenter configCenter;

    private SuffixDispatcher suffixDispatcher;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setSuffixDispatcher(SuffixDispatcher suffixDispatcher) {
        this.suffixDispatcher = suffixDispatcher;
    }

    /**
     * Method doPost ...
     *
     * @author zhangting
     * @since 2010-9-20 15:37:01
     *
     * @param request of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @throws ServletException when
     * @throws IOException when
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        initBean();
        setRootPath();
        //begin to dispatch
        this.suffixDispatcher.dispatch(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Method initBean ...
     */
    private void initBean(){
        if(configCenter == null || suffixDispatcher == null ) {
            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
            setConfigCenter((ConfigCenter) context.getBean("configCenter"));
            setSuffixDispatcher((SuffixDispatcher) context.getBean("suffixDispatcher"));
        }
    }

    /**
     * 设置一下根目录的绝对路径
     */
    private void setRootPath(){
        if(configCenter != null && configCenter.getWebRoot() == null) {
            configCenter.setWebRoot(this.getServletContext().getRealPath("/"));
        }
    }
}
