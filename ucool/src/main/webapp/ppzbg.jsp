<%--
  Created by IntelliJ IDEA.
  User: czy
  Date: 2010-9-25
  Time: 10:10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=gbk" language="java" %>
<%@ page import="biz.file.FileEditor" %>
<%@ page import="common.ConfigCenter" %>
<%@ page import="common.PersonConfig" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="web.handler.impl.PersonConfigHandler" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
    PersonConfig personConfig = (PersonConfig) wac.getBean("personConfigHandler");
    PersonConfigHandler personConfigHandler = (PersonConfigHandler) wac.getBean("personConfigHandler");
    UserDAO userDAO = (UserDAO) wac.getBean("userDAO");
    String pid = request.getParameter("pid");
    String callback = request.getParameter("callback");

    personConfigHandler.doHandler(request);
    int srcConfig = personConfig.getUserDO().getConfig();
    if (pid != null) {
        String tState = null;
        if (pid.equalsIgnoreCase("assetsdebugswitch")) {
            if (personConfig.isUcoolAssetsDebug()) {
                tState = "false";
            } else {
                tState = "true";
            }
            personConfig.setUcoolAssetsDebug(!personConfig.isUcoolAssetsDebug());
            userDAO.updateConfig(personConfig.getUserDO(), srcConfig);
        } else if (pid.equalsIgnoreCase("cleanOnlineCache")) {
            fileEditor.removeDirectory(configCenter.getWebRoot() + personConfig.getUcoolCacheRoot());
            tState = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date());
        } else if(pid.equalsIgnoreCase("bindPrepub")) {
            personConfig.setPrepub(!personConfig.isPrepub());
            userDAO.updateConfig(personConfig.getUserDO(), srcConfig);
            tState = personConfig.isPrepub()?"true":"false";
        } else if(pid.equalsIgnoreCase("enableAssets")) {
            personConfig.setEnableAssets(!personConfig.isEnableAssets());
            userDAO.updateConfig(personConfig.getUserDO(), srcConfig);
            tState = personConfig.isEnableAssets()?"true":"false";
        }

        if (callback != null) {
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + tState + "\');");
        }
    }
%>