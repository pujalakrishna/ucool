package web.handler.impl;

import common.PersonConfig;
import dao.UserDAO;
import dao.UserDO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-10 ����10:58
 */
public class PersonConfigHandler {

    private PersonConfig personConfig;

    private UserDAO userDAO;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setPersonConfig(PersonConfig personConfig) {
        this.personConfig = personConfig;
    }

    public void doHandler(HttpServletRequest request)
            throws IOException, ServletException {
        if (personConfig.getDir().equals("") && !personConfig.isNewUser()) {
            // default��2�ֿ��ܣ�û�а󶨵����û����Ѱ󶨵����ǵ�һ���������û�
            String remoteHost = request.getRemoteHost();
            UserDO personInfo = this.userDAO.getPersonInfo(remoteHost);
            if(personInfo != null) {
                personConfig.setUserDO(personInfo);
            } else {
                //����default����û�����ݿ��ѯ�����ݣ��϶�������
                personConfig.setNewUser(true);
            }
        }
    }

}
