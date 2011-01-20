package dao.entity;

/**
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-1-14
 * Time: 下午2:53
 * To change this template use File | Settings | File Templates.
 */
public class DirDO {

    /**
     * id<0表示是取消了绑定的老用户
     */
    private Long id = 0L;

    private String name;

    private int config = 5;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if(enableDebug) {
            config = config | 1<<0;
        } else {
            config = config & ~(1<<0);
        }

    }

    public boolean isEnablePrepub() {
        return (config & 2) == 2;
    }

    public void setEnablePrepub(boolean enablePrepub) {
        if(enablePrepub) {
            config = config | 1<<1;
        } else {
            config = config & ~(1<<1);
        }
    }

    public boolean isEnableAssets() {
        return (config & 4) == 4;
    }

    public void setEnableAssets(boolean enableAssets) {
        if(enableAssets) {
            config = config | 1<<2;
        } else {
            config = config & ~(1<<2);
        }
    }

}
