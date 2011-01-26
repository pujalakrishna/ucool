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
<%@ page import="common.tools.DirSyncTools" %>
<%@ page import="java.util.List" %>
<%
    WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
    ConfigCenter configCenter = (ConfigCenter) wac.getBean("configCenter");
    FileEditor fileEditor = (FileEditor) wac.getBean("fileEditor");
    PersonConfigHandler personConfigHandler = (PersonConfigHandler) wac.getBean("personConfigHandler");
    UserDAO userDAO = (UserDAO) wac.getBean("userDAO");
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
            if (dirSyncTools.sync(configCenter.getWebRoot() + personConfig.getUcoolAssetsRoot(), personConfig)) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory is deleted\');");
                return;
            }
            personConfig.setUcoolAssetsDebug(!personConfig.isUcoolAssetsDebug());
            userDAO.updateConfig(personConfig.getUserDO().getId(), personConfig.getUserDO().getConfig(), srcConfig);
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
            if (dirSyncTools.sync(configCenter.getWebRoot() + personConfig.getUcoolAssetsRoot(), personConfig)) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory is deleted\');");
                return;
            }
            personConfig.setPrepub(!personConfig.isPrepub());
            userDAO.updateConfig(personConfig.getUserDO().getId(), personConfig.getUserDO().getConfig(), srcConfig);
            tState = personConfig.isPrepub() ? "true" : "false";
        } else if (pid.equalsIgnoreCase("enableAssets")) {
            if (!personConfig.personConfigValid()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'personConfig validate fail\');");
                return;
            }
            //sync dir
            if (dirSyncTools.sync(configCenter.getWebRoot() + personConfig.getUcoolAssetsRoot(), personConfig)) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory is deleted\');");
                return;
            }
            personConfig.setEnableAssets(!personConfig.isEnableAssets());
            userDAO.updateConfig(personConfig.getUserDO().getId(), personConfig.getUserDO().getConfig(), srcConfig);
            tState = personConfig.isEnableAssets() ? "true" : "false";
        } else if (pid.equalsIgnoreCase("bindDir")) {
            //sync subDir
            if (dirSyncTools.sync(configCenter.getWebRoot() + personConfig.getUcoolAssetsRoot(), personConfig)) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory is deleted\');");
                return;
            }
            String rootDir = request.getParameter("rootDir");
            String subDir = request.getParameter("subDir");

            if (subDir == null || subDir.isEmpty()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory name is null\');");
                return;
            }
            String targetPath = rootDir + "/" + subDir;
            if("-1".equals(rootDir) || "-1".equals(subDir)) {
                targetPath = "";
            }
            //У����Ŀ¼����Ч��
            if(!fileEditor.findFile(configCenter.getWebRoot() + configCenter.getUcoolAssetsRoot() + "/" + targetPath)) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'target directory is not found��please refresh\');");
                return;
            }

            //secord create a new user and bind subDir
            boolean op;
            if (personConfig.isNewUser()) {
                personConfig.setNewUser(false);
                personConfig.getUserDO().setName(targetPath);
                if(userDAO.getPersonInfo(personConfig.getUserDO().getHostName()) != null) {
                    op = userDAO.updateDir(personConfig.getUserDO().getId(), targetPath, personConfig.getUserDO().getName());
                    personConfig.getUserDO().setName(targetPath);
                    if (!op) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'update config error\');");
                        return;
                    }
                } else {
                    op = userDAO.createNewUser(personConfig.getUserDO());
                    if (!op) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'create user error\');");
                        return;
                    }
                }
            } else {
                op = userDAO.updateDir(personConfig.getUserDO().getId(), targetPath, personConfig.getUserDO().getName());
                personConfig.getUserDO().setName(targetPath);
                if (!op) {
                    out.print(callback + "(\'" + pid + "\',\'error\', \'update config error\');");
                    return;
                }
            }
            if (personConfig.getUserDO().getName().equals("")) {
                //ȡ���󶨵����
                out.print(callback + "(\'" + pid + "\',\'ok\', \'cancel\');");
            } else {
                out.print(callback + "(\'" + pid + "\',\'ok\', \'" + personConfig.getUserDO().getConfig() + "\');");
            }
            return;
        } else if("loadSub".equals(pid)) {
            String rootDir = request.getParameter("rootDir");
            if(rootDir == null) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'load sub directory error\');");
                return;
            }
            List<String> subDirs = fileEditor.getAssetsSubDirs(rootDir);
            StringBuilder sb = new StringBuilder();
            for (String subDir : subDirs) {
                sb.append(subDir);
                sb.append(";");
            }
            if(sb.length()>0) {
                sb.deleteCharAt(sb.length()-1);
            }
            tState = sb.toString();
        }

        if (callback != null) {
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + tState + "\');");
        }
    }
%>