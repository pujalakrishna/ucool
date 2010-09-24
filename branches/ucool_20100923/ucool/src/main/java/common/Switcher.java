package common;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-24 11:21:26
 */
public class Switcher {
    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * 是否是本地环境
     *
     * @return the local (type boolean) of this Switcher object.
     */
    public boolean isLocal() {
        return this.configCenter.getUcoolEnv().equalsIgnoreCase("local");
    }

    /**
     * 是否cache目录自动清理
     * @return
     */
    public boolean isAutoCacheClean() {
        return this.configCenter.getUcoolCacheAutoClean().equals("true");
    }

    /**
     * 是否assets目录自动清理
     * @return
     */
    public boolean isAutoAssetsClean() {
        return this.configCenter.getUcoolAssetsAutoClean().equals("true");
    }
}
