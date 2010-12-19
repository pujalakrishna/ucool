package dao;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-12-10 ÏÂÎç11:08
 */
public class UserDO {
    private Long id = 0L;

    private String hostName;
    
    private String dir="";

    private int config = 5;

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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
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
