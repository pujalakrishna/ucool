package dao.entity;

/**
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-1-14
 * Time: ÏÂÎç2:53
 * To change this template use File | Settings | File Templates.
 */
public class DirDO {

    private Long id;

    private String name;

    private int config;

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
        config = config | 1;
    }

    public boolean isEnablePrepub() {
        return (config & 2) == 2;
    }

    public void setEnablePrepub(boolean enablePrepub) {
        config = config | 2;
    }

    public boolean isEnableAssets() {
        return (config & 4) == 4;
    }

    public void setEnableAssets(boolean enableAssets) {
        config = config | 4;
    }
}
