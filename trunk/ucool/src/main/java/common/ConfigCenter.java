package common;

import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.Properties;

/**
 * �����������õĿ����ļ���������������config.properties
 *
 * @author <a href="mailto:zhangting@taobao.com">��ͦ</a>
 * @since 2010-9-20 13:32:35
 */
public class ConfigCenter implements InitializingBean {

    /**
     * �������������ļ��в����ڣ�ֻ��Ϊ�˷�������ļ��������Ŀ¼����·��
     */
    public String webRoot;

    public String ucoolEnv;

    public String ucoolDailyDomain;
    public String ucoolOnlineDomain;
    public String ucoolDailyIp;
    public String ucoolOnlineIp;

    public String ucoolAssetsAutoClean;
    public String ucoolCacheAutoClean;
    public String ucoolCacheCleanPeriod;
    public String ucoolAssetsCleanPeriod;

    public String ucoolCacheRoot;
    public String ucoolAssetsRoot;

    public String ucoolAssetsDebug;

    public String getWebRoot() {
        return webRoot;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public String getUcoolEnv() {
        return ucoolEnv;
    }

    public void setUcoolEnv(String ucoolEnv) {
        this.ucoolEnv = ucoolEnv;
    }

    public String getUcoolDailyDomain() {
        return ucoolDailyDomain;
    }

    public void setUcoolDailyDomain(String ucoolDailyDomain) {
        this.ucoolDailyDomain = ucoolDailyDomain;
    }

    public String getUcoolOnlineDomain() {
        return ucoolOnlineDomain;
    }

    public void setUcoolOnlineDomain(String ucoolOnlineDomain) {
        this.ucoolOnlineDomain = ucoolOnlineDomain;
    }

    public String getUcoolDailyIp() {
        return ucoolDailyIp;
    }

    public void setUcoolDailyIp(String ucoolDailyIp) {
        this.ucoolDailyIp = ucoolDailyIp;
    }

    public String getUcoolOnlineIp() {
        return ucoolOnlineIp;
    }

    public void setUcoolOnlineIp(String ucoolOnlineIp) {
        this.ucoolOnlineIp = ucoolOnlineIp;
    }

    public String getUcoolAssetsAutoClean() {
        return ucoolAssetsAutoClean;
    }

    public void setUcoolAssetsAutoClean(String ucoolAssetsAutoClean) {
        this.ucoolAssetsAutoClean = ucoolAssetsAutoClean;
    }

    public String getUcoolCacheAutoClean() {
        return ucoolCacheAutoClean;
    }

    public void setUcoolCacheAutoClean(String ucoolCacheAutoClean) {
        this.ucoolCacheAutoClean = ucoolCacheAutoClean;
    }

    public String getUcoolCacheCleanPeriod() {
        return ucoolCacheCleanPeriod;
    }

    public void setUcoolCacheCleanPeriod(String ucoolCacheCleanPeriod) {
        this.ucoolCacheCleanPeriod = ucoolCacheCleanPeriod;
    }

    public String getUcoolAssetsCleanPeriod() {
        return ucoolAssetsCleanPeriod;
    }

    public void setUcoolAssetsCleanPeriod(String ucoolAssetsCleanPeriod) {
        this.ucoolAssetsCleanPeriod = ucoolAssetsCleanPeriod;
    }

    public String getUcoolCacheRoot() {
        return ucoolCacheRoot;
    }

    public void setUcoolCacheRoot(String ucoolCacheRoot) {
        this.ucoolCacheRoot = ucoolCacheRoot;
    }

    public String getUcoolAssetsRoot() {
        return ucoolAssetsRoot;
    }

    public void setUcoolAssetsRoot(String ucoolAssetsRoot) {
        this.ucoolAssetsRoot = ucoolAssetsRoot;
    }

    public String getUcoolAssetsDebug() {
        return ucoolAssetsDebug;
    }

    public void setUcoolAssetsDebug(String ucoolAssetsDebug) {
        this.ucoolAssetsDebug = ucoolAssetsDebug;
    }

    /**
     * Method afterPropertiesSet ...
     *
     * @throws Exception when
     * @author zhangting
     * @since 2010-9-20 13:38:17
     */
    @Override public void afterPropertiesSet() throws Exception {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/config.properties"));
        if (!properties.isEmpty()) {
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = entry.getKey().toString();
                String[] keySplits = key.split("\\.");
                StringBuffer realKey = new StringBuffer();
                for (String keySplit : keySplits) {
                    realKey.append(keySplit.toUpperCase().substring(0, 1)).append(keySplit.substring(1));
                }

                //refect to set value
                this.getClass().getDeclaredMethod("set" + realKey.toString(), String.class)
                        .invoke(this, properties.get(key));
            }
        } else {
            //log
        }
    }


    /**
     * ����pz.jsp��css����
     *
     * @param state of type String
     * @return String
     */
    public String getStateStyle(String state) {
        return state.equals("true")?"open":"closed";
    }

    /**
     * ����pz.jsp��״̬����
     *
     * @param state of type String
     * @return String
     */
    public String getStateOper(String state) {
        return state.equals("true")?"����ر�":"�����";
    }

    /**
     * ����pz.jsp��״̬����
     *
     * @param state of type String
     * @return String
     */
    public String getCurState(String state) {
        return state.equals("true")?"�Ѵ�":"�ѹر�";
    }

    public static void main(String[] args) throws Exception {
        ConfigCenter configCenter = ConfigCenter.class.newInstance();
        configCenter.afterPropertiesSet();
    }
}
