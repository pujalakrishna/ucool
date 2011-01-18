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
<%@ page import="dao.DirDAO" %>
<%@ page import="dao.entity.DirDO" %>
<%@ page import="common.DirMapping" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
    PersonConfigHandler personConfigHandler = (PersonConfigHandler) wac.getBean("personConfigHandler");
    UserDAO userDAO = (UserDAO) wac.getBean("userDAO");
    DirDAO dirDAO = (DirDAO) wac.getBean("dirDAO");
    String pid = request.getParameter("pid");
    String callback = request.getParameter("callback");

    PersonConfig personConfig = personConfigHandler.doHandler(request);
    int srcConfig = 5;
    if(!personConfig.isNewUser()) {
        srcConfig = personConfig.getDirDO().getConfig();
    }
    if (pid != null) {
        String tState = null;
        if (pid.equalsIgnoreCase("assetsdebugswitch")) {
            if (personConfig.isUcoolAssetsDebug()) {
                tState = "false";
            } else {
                tState = "true";
            }
            personConfig.setUcoolAssetsDebug(!personConfig.isUcoolAssetsDebug());
            dirDAO.updateConfig(personConfig.getUserDO(), srcConfig);
        } else if (pid.equalsIgnoreCase("cleanOnlineCache")) {
            fileEditor.removeDirectory(configCenter.getWebRoot() + personConfig.getUcoolCacheRoot());
            tState = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��").format(new Date());
        } else if(pid.equalsIgnoreCase("bindPrepub")) {
            personConfig.setPrepub(!personConfig.isPrepub());
            dirDAO.updateConfig(personConfig.getUserDO(), srcConfig);
            tState = personConfig.isPrepub()?"true":"false";
        } else if(pid.equalsIgnoreCase("enableAssets")) {
            personConfig.setEnableAssets(!personConfig.isEnableAssets());
            dirDAO.updateConfig(personConfig.getUserDO(), srcConfig);
            tState = personConfig.isEnableAssets()?"true":"false";
        } else if(pid.equalsIgnoreCase("bindDir")) {
            //first create a new dir
            String dir = request.getParameter("dir");
            boolean op = true;
            if(dir == null || dir.isEmpty()) {
                return;
            }
            DirDO dirDO = dirDAO.getDirByName(dir);
            if(dirDO == null) {
                //create new dir
                dirDO = new DirDO();
                dirDO.setName(dir);
                op = dirDAO.createNewDir(dirDO);
                if(!op) {
                    out.print(callback + "(\'" + pid + "\',\'error\', \'create dir error\');");
                    return;
                }
                if(dirDO.getId() != 0)  {
                    //add new dir to memory
                    DirMapping dirMapping = (DirMapping) wac.getBean("dirMapping");
                    dirMapping.addDir(dirDO);
                }
            }
            //secord create a new user and bind dir
            Long srcDirId = personConfig.getDirId();
            personConfig.setDirDO(dirDO);
            personConfig.getUserDO().setDirId(dirDO.getId());
            if(personConfig.isNewUser()) {
                personConfig.setNewUser(false);
                //TODO session�����ڣ����������¡���NND
                op = userDAO.createNewUser(personConfig.getUserDO());
                if(!op) {
                    out.print(callback + "(\'" + pid + "\',\'error\', \'create user error\');");
                    return;
                }
            } else {
                if(personConfig.getDirId() != srcDirId) {
                    op = userDAO.updateDir(personConfig.getUserDO(), srcDirId);
                    if (!op) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'update config error\');");
                        return;
                    }
                }
            }
            //set session
            request.getSession().setAttribute("personConfig", personConfig.getConfigString());
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + personConfig.getConfigString() + "\');");
            return;
        }

        if (callback != null) {
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + tState + "\');");
        }
    }
%>