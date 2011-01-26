package dao.entity;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-10 下午11:08
 */
public class UserDO {
    private Long id = 0L;

    private String hostName;

    private String name;

    private int config = 5;

    // 如果新用户第二次过来，这个值就为 true了，这样就不用重复查询数据库了
    private boolean newUser = false;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConfig() {
        return config;
    }

    public void setConfig(int config) {
        this.config = config;
    }


    public boolean isEnableDebug() {
        return (config & 1) == 1;
    }

    public void setEnableDebug(boolean enableDebug) {
        if (enableDebug) {
            config = config | 1 << 0;
        } else {
            config = config & ~(1 << 0);
        }

    }

    public boolean isEnablePrepub() {
        return (config & 2) == 2;
    }

    public void setEnablePrepub(boolean enablePrepub) {
        if (enablePrepub) {
            config = config | 1 << 1;
        } else {
            config = config & ~(1 << 1);
        }
    }

    public boolean isEnableAssets() {
        return (config & 4) == 4;
    }

    public void setEnableAssets(boolean enableAssets) {
        if (enableAssets) {
            config = config | 1 << 2;
        } else {
            config = config & ~(1 << 2);
        }
    }
}
