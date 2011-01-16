package dao;

import dao.entity.UserDO;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-3 ����12:16
 */
public interface UserDAO {
    /**
     * Method getPersonDirectory ...
     *
     * @param hostName of type String
     * @return String
     */
    UserDO getPersonInfo(String hostName);

    boolean createNewUser(UserDO userDO);
}
