package common;

import dao.UserDAO;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-3 ионГ12:16
 */
public class DirSwitcher {
    
    private UserDAO userDAO;

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean switchToDir(String dirName) {
        return true;
    }

    public String getBindDir(String hostName) {
        return null;
    }
}
