package web.handler.impl;

import common.DirMapping;
import common.PersonConfig;
import dao.UserDAO;
import dao.entity.UserDO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-10 ����10:58
 */
public class PersonConfigHandler {
    private UserDAO userDAO;

    private PersonConfig personConfig;

    private DirMapping dirMapping;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setPersonConfig(PersonConfig personConfig) {
        this.personConfig = personConfig;
    }

    public void setDirMapping(DirMapping dirMapping) {
        this.dirMapping = dirMapping;
    }

    public PersonConfig doHandler(HttpServletRequest request)
            throws IOException, ServletException {
        String configString = (String) request.getSession().getAttribute("personConfig");
        if (configString == null) {
            /**
             * ��2�������
             * 1�����ݿ���û�а󶨵�����
             * 2���а󶨣�����ʧЧ�˵����û�
             */
            String remoteHost = request.getRemoteHost();
            UserDO personInfo = this.userDAO.getPersonInfo(remoteHost);
            if (personInfo != null) {
                personConfig.setUserDO(personInfo);
                personConfig.setDirDO(dirMapping.getDir(personConfig.getDirId()));
            } else {
                //û�����ݿ��ѯ�����ݣ��϶�������
                personConfig.setNewUser(true);
            }
            //set session
            request.getSession().setAttribute("personConfig", personConfig.getConfigString());
        } else {
            // get session
            personConfig.parseConfigString(configString);
            personConfig.setDirDO(dirMapping.getDir(personConfig.getDirId()));
            //TODO parse fail?
        }
        return personConfig;
    }

}
