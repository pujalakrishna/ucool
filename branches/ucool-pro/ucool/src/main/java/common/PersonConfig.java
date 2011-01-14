package common;

import dao.UserDO;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-11 上午12:21
 */
public class PersonConfig {

    private UserDO userDO = new UserDO();

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    // 如果新用户第二次过来，这个值就为 true了，这样就不用重复查询数据库了
    private boolean newUser = false;

    public boolean isUcoolAssetsDebug() {
        if(personConfigValid()) {
            return this.userDO.isEnableDebug();
        } else {
            return configCenter.isUcoolAssetsDebug();
        }
    }

    public void setUcoolAssetsDebug(boolean enableDebug) {
        this.userDO.setEnableDebug(enableDebug);
    }

    public boolean isPrepub() {
        if(personConfigValid()) {
            return this.userDO.isEnablePrepub();
        } else {
            return configCenter.isPrepub();
        }
    }

    public void setPrepub(boolean enablePrepub) {
        this.userDO.setEnablePrepub(enablePrepub);
    }

    public boolean isEnableAssets() {
        if(personConfigValid()) {
            return this.userDO.isEnableAssets();
        } else {
            return configCenter.isEnableAssets();
        }
    }

    public void setEnableAssets(boolean enableAssets) {
        this.userDO.setEnableAssets(enableAssets);
    }

    public String getUcoolCacheRoot() {
        if(personConfigValid()) {
            return getDir() + "/" + configCenter.getUcoolCacheRoot();
        } else {
            return configCenter.getUcoolCacheRoot();
        }
    }

    public String getUcoolAssetsRoot() {
        if(personConfigValid()) {
            return getDir() + "/" + configCenter.getUcoolAssetsRoot();
        } else {
            return configCenter.getUcoolAssetsRoot();
        }
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

    /**
     * 解析配置字符串
     *
     * @param configString of type String
     */
    public void parseConfigString(String configString) {
        // set userDO
        String[] configStrings = configString.split(HttpTools.filterSpecialChar(":"));
        userDO.setId(Long.valueOf(configStrings[0]));
        userDO.setHostName(configStrings[1]);
        userDO.setDir(configStrings[2]);
        userDO.setConfig(Integer.parseInt(configStrings[3]));
        this.setNewUser(configStrings[4].equals("true"));
    }

    /**
     * 得到配置字符串
     *
     * @return the configString (type String) of this PersonConfig object.
     */
    public String getConfigString() {
        // get userDO
        StringBuffer sb = new StringBuffer();
        sb.append(userDO.getId()).append(":");
        sb.append(userDO.getHostName()).append(":");
        sb.append(userDO.getDirId()).append(":");
        sb.append(this.isNewUser());
        return sb.toString();
    }

    /**
     * 是否满足个人配置的校验，后期相当重要的方法，所有取值前都要走一遍
     * 
     * @return boolean
     */
    private boolean personConfigValid() {
        return !isNewUser();
    }
}
