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
<%@ page import="biz.file.FileEditor" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
    String pid = request.getParameter("pid");
    String callback = request.getParameter("callback");

    if (pid != null) {
        String tState = null;
        if (pid.equalsIgnoreCase("assetsdebugswitch")) {
            if (configCenter.getUcoolAssetsDebug().equals("true")) {
                tState = "false";
            } else {
                tState = "true";
            }
            configCenter.setUcoolAssetsDebug(tState);
        } else if (pid.equalsIgnoreCase("cleanOnlineCache")) {
            fileEditor.removeDirectory(configCenter.getWebRoot() + configCenter.getUcoolCacheRootOnline());
            tState = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��").format(new Date());
        } else if (pid.equalsIgnoreCase("cleanDailyCache")) {
            fileEditor.removeDirectory(configCenter.getWebRoot() + configCenter.getUcoolCacheRootDaily());
            tState = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��").format(new Date());
        } else if(pid.equalsIgnoreCase("cleanPrepubCache")) {
            fileEditor.removeDirectory(configCenter.getWebRoot() + configCenter.getUcoolCacheRootPrepub());
            tState = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��").format(new Date());
        } else if(pid.equalsIgnoreCase("bindPrepub")) {
            configCenter.setPrepub(!configCenter.isPrepub());
            tState = configCenter.isPrepub()?"true":"false";
        } else if(pid.equalsIgnoreCase("enableAssets")) {
            configCenter.setEnableAssets(!configCenter.isEnableAssets());
            tState = configCenter.isEnableAssets()?"true":"false";
        }

        if (callback != null) {
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + tState + "\');");
        }
    }
%>