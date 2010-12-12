package common;

import dao.UserDO;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-11 上午12:21
 */
public class PersonConfig {

    private UserDO userDO = new UserDO();

    // 如果新用户第二次过来，这个值就为 true了，这样就不用重复查询数据库了
    private boolean newUser = false;

    public boolean isEnableDebug() {
        return this.userDO.isEnableDebug();
    }

    public void setEnableDebug(boolean enableDebug) {
        this.userDO.setEnableDebug(enableDebug);
    }

    public boolean isEnablePrepub() {
        return this.userDO.isEnablePrepub();
    }

    public void setEnablePrepub(boolean enablePrepub) {
        this.userDO.setEnablePrepub(enablePrepub);
    }

    public boolean isEnableAssets() {
        return this.userDO.isEnableAssets();
    }

    public void setEnableAssets(boolean enableAssets) {
        this.userDO.setEnableAssets(enableAssets);
    }

    public String getDir() {
        return this.userDO.getDir();
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }
}
