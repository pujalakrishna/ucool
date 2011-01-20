package common;

import common.tools.HttpTools;
import dao.entity.DirDO;
import dao.entity.UserDO;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-11 上午12:21
 */
public class PersonConfig {

    private UserDO userDO = new UserDO();

    private DirDO dirDO;

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    // 如果新用户第二次过来，这个值就为 true了，这样就不用重复查询数据库了
    private boolean newUser = false;

    public boolean isUcoolAssetsDebug() {
        if(personConfigValid()) {
            return this.dirDO.isEnableDebug();
        } else {
            return configCenter.isUcoolAssetsDebug();
        }
    }

    public void setUcoolAssetsDebug(boolean enableDebug) {
        this.dirDO.setEnableDebug(enableDebug);
    }

    public boolean isPrepub() {
        if(personConfigValid()) {
            return this.dirDO.isEnablePrepub();
        } else {
            return configCenter.isPrepub();
        }
    }

    public void setPrepub(boolean enablePrepub) {
        this.dirDO.setEnablePrepub(enablePrepub);
    }

    public boolean isEnableAssets() {
        if(personConfigValid()) {
            return this.dirDO.isEnableAssets();
        } else {
            return configCenter.isEnableAssets();
        }
    }

    public void setEnableAssets(boolean enableAssets) {
        this.dirDO.setEnableAssets(enableAssets);
    }

    public String getUcoolCacheRoot() {
        if(personConfigValid()) {
            return dirDO.getName() + "/" + configCenter.getUcoolCacheRoot();
        } else {
            return configCenter.getUcoolCacheRoot();
        }
    }

    public String getUcoolAssetsRoot() {
        if(personConfigValid()) {
            return configCenter.getUcoolAssetsRoot() + "/" + dirDO.getName();
        } else {
            return configCenter.getUcoolAssetsRoot();
        }
    }

    public Long getDirId() {
        return this.userDO.getDirId();
    }

    public UserDO getUserDO() {
        return userDO;
    }

    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }

    /**
     * 判断是否是新人
     * 这里的新人有2种可能：
     * 1、真正的新人，没有任何目录的绑定
     * 2、老用户，但是取消了绑定
     * @return
     */
    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public DirDO getDirDO() {
        return dirDO;
    }

    public void setDirDO(DirDO dirDO) {
        this.dirDO = dirDO;
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
        userDO.setHostName(configStrings[1].equals("null") ? null:configStrings[1]);
        userDO.setDirId(Long.parseLong(configStrings[2]));
        this.setNewUser(configStrings[3].equals("true"));
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
    public boolean personConfigValid() {
        return !isNewUser() && getDirId()>0;
    }
}
