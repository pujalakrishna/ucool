package web.handler.impl;

import common.PersonConfig;
import dao.UserDAO;
import dao.UserDO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-10 下午10:58
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
            // default有2种可能，没有绑定的新用户和已绑定但是是第一次来的老用户
            String remoteHost = request.getRemoteHost();
            UserDO personInfo = this.userDAO.getPersonInfo(remoteHost);
            if(personInfo != null) {
                personConfig.setUserDO(personInfo);
            } else {
                //又是default，又没在数据库查询到数据，肯定是新人
                personConfig.setNewUser(true);
            }
        }
    }

}
