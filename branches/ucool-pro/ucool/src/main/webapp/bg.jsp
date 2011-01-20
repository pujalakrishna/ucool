<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="common.PersonConfig" %>
<%@ page import="common.tools.DirSyncTools" %>
<%--
  Created by IntelliJ IDEA.
  User: zhangting
  Date: 11-1-20
  Time: 下午3:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="biz.file.FileEditor" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="common.PersonConfig" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="web.handler.impl.PersonConfigHandler" %>
<%@ page import="dao.DirDAO" %>
<%@ page import="common.DirMapping" %>
<%@ page import="common.tools.DirSyncTools" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
    PersonConfigHandler personConfigHandler = (PersonConfigHandler) wac.getBean("personConfigHandler");
    UserDAO userDAO = (UserDAO) wac.getBean("userDAO");
    DirDAO dirDAO = (DirDAO) wac.getBean("dirDAO");
    DirMapping dirMapping = (DirMapping) wac.getBean("dirMapping");
    String pid = request.getParameter("pid");
    String callback = request.getParameter("callback");
    DirSyncTools dirSyncTools = (DirSyncTools) wac.getBean("dirSyncTools");

    PersonConfig personConfig = personConfigHandler.doHandler(request);

    if (pid.equalsIgnoreCase("fuckie")) {
        request.getSession().removeAttribute("personConfig");
        request.getSession().invalidate();
    }
%>