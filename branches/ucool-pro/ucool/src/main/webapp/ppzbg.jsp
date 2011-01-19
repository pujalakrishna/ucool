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
    DirMapping dirMapping = (DirMapping) wac.getBean("dirMapping");
    String pid = request.getParameter("pid");
    String callback = request.getParameter("callback");

    PersonConfig personConfig = personConfigHandler.doHandler(request);
    int srcConfig = 5;
    if(personConfig.personConfigValid()) {
        srcConfig = personConfig.getDirDO().getConfig();
    }
    if (pid != null) {
        String tState = null;
        if (pid.equalsIgnoreCase("assetsdebugswitch")) {
            if (!personConfig.personConfigValid()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'personConfig validate fail\');");
                return;
            }
            personConfig.setUcoolAssetsDebug(!personConfig.isUcoolAssetsDebug());
            dirDAO.updateConfig(personConfig.getDirDO(), srcConfig);
            //update dirmapping
            dirMapping.getDir(personConfig.getDirId()).setConfig(personConfig.getDirDO().getConfig());
            tState = personConfig.isUcoolAssetsDebug()?"true":"false";
        } else if (pid.equalsIgnoreCase("cleanOnlineCache")) {
            fileEditor.removeDirectory(configCenter.getWebRoot() + personConfig.getUcoolCacheRoot());
            tState = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒").format(new Date());
        } else if(pid.equalsIgnoreCase("bindPrepub")) {
            if (!personConfig.personConfigValid()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'personConfig validate fail\');");
                return;
            }
            personConfig.setPrepub(!personConfig.isPrepub());
            dirDAO.updateConfig(personConfig.getDirDO(), srcConfig);
            //update dirmapping
            dirMapping.getDir(personConfig.getDirId()).setConfig(personConfig.getDirDO().getConfig());
            tState = personConfig.isPrepub()?"true":"false";
        } else if(pid.equalsIgnoreCase("enableAssets")) {
            if (!personConfig.personConfigValid()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'personConfig validate fail\');");
                return;
            }
            personConfig.setEnableAssets(!personConfig.isEnableAssets());
            dirDAO.updateConfig(personConfig.getDirDO(), srcConfig);
            //update dirmapping
            dirMapping.getDir(personConfig.getDirId()).setConfig(personConfig.getDirDO().getConfig());
            tState = personConfig.isEnableAssets()?"true":"false";
        } else if(pid.equalsIgnoreCase("bindDir")) {
            //first create a new dir
            String dir = request.getParameter("dir");
            boolean op = true;
            if(dir == null || dir.isEmpty()) {
                out.print(callback + "(\'" + pid + "\',\'error\', \'directory's name is null\');");
                return;
            }
            DirDO dirDO = null;
            if("0".equals(dir)) {
                dirDO = new DirDO();
            } else {
                dirDO = dirDAO.getDirByName(dir);
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
                        dirMapping.addDir(dirDO);
                    }
                }
            }

            //secord create a new user and bind dir
            Long srcDirId = personConfig.getDirId();
            personConfig.setDirDO(dirDO);
            personConfig.getUserDO().setDirId(dirDO.getId());
            if(personConfig.isNewUser()) {
                personConfig.setNewUser(false);
                //TODO session不过期，这个必须更新。。NND
                op = userDAO.createNewUser(personConfig.getUserDO());
                if(!op) {
                    out.print(callback + "(\'" + pid + "\',\'error\', \'create user error\');");
                    return;
                }
            } else {
                if(!personConfig.getDirId().equals(srcDirId)) {
                    op = userDAO.updateDir(personConfig.getUserDO(), srcDirId);
                    if (!op) {
                        out.print(callback + "(\'" + pid + "\',\'error\', \'update config error\');");
                        return;
                    }
                }
            }
            //set session
            request.getSession().setAttribute("personConfig", personConfig.getConfigString());
            if(personConfig.getDirId() == 0 ) {
                //取消绑定的情况
                out.print(callback + "(\'" + pid + "\',\'ok\', \'cancel\');");
            } else {
                out.print(callback + "(\'" + pid + "\',\'ok\', \'" + personConfig.getDirDO().getConfig() + "\');");
            }
            return;
        } else if(pid.equalsIgnoreCase("fuckie")) {
            request.getSession().removeAttribute("personConfig");
            request.getSession().invalidate();
        }

        if (callback != null) {
            out.print(callback + "(\'" + pid + "\',\'ok\', \'" + tState + "\');");
        }
    }
%>