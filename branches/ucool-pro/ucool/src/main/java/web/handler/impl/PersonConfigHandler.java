package web.handler.impl;

import common.PersonConfig;
import dao.UserDAO;
import dao.entity.UserDO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-10 下午10:58
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
//        String configString = (String) request.getSession().getAttribute("personConfig");
//        if (configString == null) {
            /**
             * 有2种情况：
             * 1、数据库中没有绑定的新人
             * 2、有绑定，但是session失效了的老用户
             */
            String remoteHost = request.getRemoteHost();
//            InetAddress IP = InetAddress.getByName(request.getRemoteAddr());
//            String remoteHost = IP.getHostName();
            if(remoteHost == null) {
                remoteHost = request.getRemoteAddr();
            }
            UserDO personInfo = this.userDAO.getPersonInfo(remoteHost);
            if (personInfo != null) {
                personConfig.setUserDO(personInfo);
            } else {
                personConfig.setUserDO(new UserDO());
                personConfig.getUserDO().setHostName(remoteHost);
                //没在数据库查询到数据，肯定是新人
                personConfig.setNewUser(true);
            }
            //set session
//            request.getSession().setAttribute("personConfig", personConfig.getConfigString());
//        } else {
//            // get session
//            personConfig.parseConfigString(configString);
//            //TODO parse fail?
//        }
        return personConfig;
    }

}
