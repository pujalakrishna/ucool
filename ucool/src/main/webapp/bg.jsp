<%--
  Created by IntelliJ IDEA.
  User: czy
  Date: 11-1-20
  Time: ÏÂÎç10:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="common.ConfigCenter" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    String cache = request.getParameter("cache");
    if("true".equals(cache)) {
        configCenter.setUcoolCacheEnable("true");
        out.print("ok");
    } else {
        configCenter.setUcoolCacheEnable("false");
        out.print("ok");
    }
%>