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
    int srcConfig = 5;
    if (personConfig.personConfigValid()) {
        srcConfig = personConfig.getUserDO().getConfig();
    }
    if (pid != null) {
        String tState = null;
        if (pid.equalsIgnoreCase("assetsdebugswitch")) {
            if (!personConfig.personConfigValid()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'personConfig validate fail\');");
                return;
            }
            //sync dir
            if (dirSyncTools.sync(personConfig.getDirId(), configCenter.getWebRoot() + personConfig.getUcoolAssetsRoot(), personConfig)) {
                //set session
                request.getSession().setAttribute("personConfig", personConfig.getConfigString());
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory is deleted\');");
                return;
            }
            personConfig.setUcoolAssetsDebug(!personConfig.isUcoolAssetsDebug());
            dirDAO.updateConfig(personConfig.getDirDO(), srcConfig);
            //update dirmapping
            dirMapping.getDir(personConfig.getDirId()).setConfig(personConfig.getDirDO().getConfig());
            tState = personConfig.isUcoolAssetsDebug() ? "true" : "false";
        } else if (pid.equalsIgnoreCase("cleanOnlineCache")) {
            fileEditor.removeDirectory(configCenter.getWebRoot() + personConfig.getUcoolCacheRoot());
            tState = new SimpleDateFormat("yyyy��MM��dd�� HHʱmm��ss��").format(new Date());
        } else if (pid.equalsIgnoreCase("bindPrepub")) {
            if (!personConfig.personConfigValid()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'personConfig validate fail\');");
                return;
            }
            //sync dir
            if (dirSyncTools.sync(personConfig.getDirId(), configCenter.getWebRoot() + personConfig.getUcoolAssetsRoot(), personConfig)) {
                //set session
                request.getSession().setAttribute("personConfig", personConfig.getConfigString());
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory is deleted\');");
                return;
            }
            personConfig.setPrepub(!personConfig.isPrepub());
            dirDAO.updateConfig(personConfig.getDirDO(), srcConfig);
            //update dirmapping
            dirMapping.getDir(personConfig.getDirId()).setConfig(personConfig.getDirDO().getConfig());
            tState = personConfig.isPrepub() ? "true" : "false";
        } else if (pid.equalsIgnoreCase("enableAssets")) {
            if (!personConfig.personConfigValid()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'personConfig validate fail\');");
                return;
            }
            //sync dir
            if (dirSyncTools.sync(personConfig.getDirId(), configCenter.getWebRoot() + personConfig.getUcoolAssetsRoot(), personConfig)) {
                //set session
                request.getSession().setAttribute("personConfig", personConfig.getConfigString());
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory is deleted\');");
                return;
            }
            personConfig.setEnableAssets(!personConfig.isEnableAssets());
            dirDAO.updateConfig(personConfig.getDirDO(), srcConfig);
            //update dirmapping
            dirMapping.getDir(personConfig.getDirId()).setConfig(personConfig.getDirDO().getConfig());
            tState = personConfig.isEnableAssets() ? "true" : "false";
        } else if (pid.equalsIgnoreCase("bindDir")) {
            //first create a new dir
            String dir = request.getParameter("dir");

            boolean op = true;
            if (dir == null || dir.isEmpty()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory's name is null\');");
                return;
            }
            DirDO dirDO = null;
            if ("-1".equals(dir)) {
                dirDO = new DirDO();
                dirDO.setId(-1L);
            } else {
                dirDO = dirMapping.getDirByName(dir);
                if (dirDO == null) {
                    //create new dir
                    dirDO = new DirDO();
                    dirDO.setName(dir);
                    if(!fileEditor.findFile(configCenter.getWebRoot() + configCenter.getUcoolAssetsRoot() + "/" + dir)) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'create dir error\');");
                        return;
                    }
                    op = dirDAO.createNewDir(dirDO);
                    if (!op) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'create dir error\');");
                        return;
                    }
                    if (dirDO.getId() != 0) {
                        //add new dir to memory
                        dirMapping.addDir(dirDO);
                    }
                } else {
                    //sync dir
                    if (dirSyncTools.sync(dirDO.getId(), configCenter.getWebRoot() + configCenter.getUcoolAssetsRoot() + "/" + dir, personConfig)) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'dir has delete\');");
                        //set session
                        request.getSession().setAttribute("personConfig", personConfig.getConfigString());
                        return;
                    }
                }
            }

            //secord create a new user and bind dir
            Long srcDirId = personConfig.getDirId();
            personConfig.setDirDO(dirDO);
            personConfig.getUserDO().setDirId(dirDO.getId());
            //personConfig.getDirId() < 0��ʾ��ȡ���󶨵����û�
            if (personConfig.isNewUser() && srcDirId == 0) {
                personConfig.setNewUser(false);
                //TODO session�����ڣ����������¡���NND
                op = userDAO.createNewUser(personConfig.getUserDO());
                if (!op) {
                    out.print(callback + "(\'" + pid + "\',\'error\', \'create user error\');");
                    return;
                }
            } else {
                personConfig.setNewUser(false);
                if (!personConfig.getDirId().equals(srcDirId)) {
                    op = userDAO.updateDir(personConfig.getUserDO().getId(), personConfig.getDirId(), srcDirId);
                    if (!op) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'update config error\');");
                        return;
                    }
                }
            }
            //set session
            request.getSession().setAttribute("personConfig", personConfig.getConfigString());
            if (personConfig.getDirId() == -1) {
                //ȡ���󶨵����
                out.print(callback + "(\'" + pid + "\',\'ok\', \'cancel\');");
            } else {
                out.print(callback + "(\'" + pid + "\',\'ok\', \'" + personConfig.getDirDO().getConfig() + "\');");
            }
            return;
        }

        if (callback != null) {
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + tState + "\');");
        }
    }
%>