package common;

import dao.UserDO;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-11 ����12:21
 */
public class PersonConfig {

    private UserDO userDO = new UserDO();

    // ������û��ڶ��ι��������ֵ��Ϊ true�ˣ������Ͳ����ظ���ѯ���ݿ���
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
