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
     * �Ƿ�����assets debug
     * @return
     */
    public boolean isAssetsDebugMode() {
        return this.configCenter.isUcoolAssetsDebug();
    }

    /**
     * ����assets debug
     * @return
     */
    public void setAssetsDebugMode(String state) {
        this.configCenter.setUcoolAssetsDebug(state);
    }
}
