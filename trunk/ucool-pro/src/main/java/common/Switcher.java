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
     * 是否开启了assets debug
     * @return
     */
    public boolean isAssetsDebugMode() {
        return this.configCenter.getUcoolAssetsDebug().equals("true");
    }

    /**
     * 设置assets debug
     * @return
     */
    public void setAssetsDebugMode(String state) {
        this.configCenter.setUcoolAssetsDebug(state);
    }
}
