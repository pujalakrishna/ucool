<%--
  Created by IntelliJ IDEA.
  User: czy
  Date: 2010-9-25
  Time: 10:10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    String pid = request.getParameter("pid");
    String callback = request.getParameter("callback");
    
    if (pid != null && pid.equalsIgnoreCase("assetsdebug")) {
        String tState;
        if (configCenter.getUcoolAssetsDebug().equals("true")) {
            tState = "false";
        } else {
            tState = "true";
        }
        configCenter.setUcoolAssetsDebug(tState);
        if (callback != null) {
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + tState + "\');");
        }
    }
%>