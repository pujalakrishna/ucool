package web.filter;

import common.ConfigCenter;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.scheduling.quartz.CronTriggerBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        HttpServletResponse response = (HttpServletResponse) resp;
        String fullUrl = getFullUrl(request, response);
        request.setAttribute("filePath", request.getRequestURI());
        if (fullUrl.indexOf("??") != -1) {
            request.setAttribute("realUrl", fullUrl);
            request.getRequestDispatcher("/combo").forward(request, response);
        } else {
            request.setAttribute("realUrl", request.getRequestURL().toString());
            chain.doFilter(req, resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        if (configCenter == null) {
            WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
            setConfigCenter((ConfigCenter) context.getBean("configCenter"));
            /**
             * 设置一下根目录的绝对路径
             */
            configCenter.setWebRoot(config.getServletContext().getRealPath("/"));
            //设置一下自动清理的周期
            if ("true".equals(configCenter.getUcoolCacheAutoClean())) {
                Scheduler scheduler = (Scheduler) context.getBean("startQuertz");
                CronTriggerBean trigger = null;
                try {
                    trigger = (CronTriggerBean) scheduler.getTrigger("triggerCleanOnlineTime", Scheduler.DEFAULT_GROUP);
                    if (!"".equals(configCenter.getUcoolCacheCleanPeriod()) && !configCenter.getUcoolCacheCleanPeriod().equals("0 30 12 * * ?")) {
                        if (!scheduler.isShutdown()) {
                            scheduler.shutdown();
                        }
                        trigger.setCronExpression(configCenter.getUcoolCacheCleanPeriod());
                        scheduler.rescheduleJob("triggerCleanOnlineTime", Scheduler.DEFAULT_GROUP, trigger);
                    }
                } catch (Exception e) {
                    //如果挂了，那就恢复默认情况
                    try {
                        trigger = (CronTriggerBean) scheduler.getTrigger("triggerCleanOnlineTime", Scheduler.DEFAULT_GROUP);
                        if (trigger != null) {
                            if(!scheduler.isShutdown()) {
                                scheduler.shutdown();
                            }
                            trigger.setCronExpression("0 30 12 * * ?");
                            scheduler.rescheduleJob("triggerCleanOnlineTime", Scheduler.DEFAULT_GROUP, trigger);
                        }
                    } catch (Exception e1) {
                        try {
                            if (!scheduler.isShutdown()) {
                                scheduler.shutdown();
                            }
                        } catch (SchedulerException e2) {
                        }
                    }
                }
            }
        }
    }

    /**
     * Method getFullUrl ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     * @return String
     */
    private String getFullUrl(HttpServletRequest request, HttpServletResponse response) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getRequestURL());
        if (request.getQueryString() != null) {
            sb.append("?").append(request.getQueryString());
        }
        return sb.toString();
    }

}
