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
     * �Ƿ��Ǳ��ػ���
     *
     * @return the local (type boolean) of this Switcher object.
     */
    public boolean isLocal() {
        return this.configCenter.getUcoolEnv().equalsIgnoreCase("local");
    }

    /**
     * �Ƿ�cacheĿ¼�Զ�����
     * @return
     */
    public boolean isAutoCacheClean() {
        return this.configCenter.getUcoolCacheAutoClean().equals("true");
    }

    /**
     * �Ƿ�assetsĿ¼�Զ�����
     * @return
     */
    public boolean isAutoAssetsClean() {
        return this.configCenter.getUcoolAssetsAutoClean().equals("true");
    }
}
