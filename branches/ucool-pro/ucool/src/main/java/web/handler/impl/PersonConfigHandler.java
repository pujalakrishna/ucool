package web.handler.impl;

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

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setPersonConfig(PersonConfig personConfig) {
        this.personConfig = personConfig;
    }

    public PersonConfig doHandler(HttpServletRequest request)
            throws IOException, ServletException {
        String remoteHost = request.getRemoteHost();
        if (remoteHost == null) {
            remoteHost = request.getRemoteAddr();
        }
        UserDO personInfo = this.userDAO.getPersonInfo(remoteHost);
        if (personInfo != null) {
            personConfig.setUserDO(personInfo);
        } else {
            personConfig.setUserDO(new UserDO());
            personConfig.getUserDO().setHostName(remoteHost);
            //û�����ݿ��ѯ�����ݣ��϶�������
            personConfig.getUserDO().setNewUser(true);
        }
        return personConfig;
    }

}
