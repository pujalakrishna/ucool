package common;

import org.springframework.beans.factory.InitializingBean;

import java.util.Date;
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
     * ���������������ļ��в����ڣ�ֻ��Ϊ�˷�������ļ��������Ŀ¼����·��
     */
    private String webRoot;

    /**
     * �������ʱ��
     */
    private Date lastCleanTime;

    /**
     * ���������ļ�����������ЩĿ¼�µ�assets��debugģʽĬ����ȡxx.js/css������source
     */
    private String[] ucoolAssetsDebugCorrectStrings;

    /**
     * ��ǰ�Ƿ���Ԥ��ip
     */
    private boolean isPrepub;

    /**
     * �Ƿ�ʹ�ñ��ص�assets
     */
    private boolean isEnableAssets = true;
    
    /**
     * ����Ϊ�����ļ�ʹ��
     */

    //��ǰ����local or vm
    private String ucoolEnv;

    //daily������
    private String ucoolDailyDomain;
    //��������
    private String ucoolOnlineDomain;
    //daily ip
    private String ucoolDailyIp;
    //����ip
    private String ucoolOnlineIp;
    //Ԥ��ip
    private String ucoolPrepubIp;
    //combo�ķָ�����
    private String ucoolComboDecollator;

    //�Ƿ���assest���Զ�����
    private String ucoolAssetsAutoClean;
    //�Ƿ���cacheĿ¼���Զ�����
    private String ucoolCacheAutoClean;
    //cacheĿ¼��������
    private String ucoolCacheCleanPeriod;
    //assetsĿ¼��������
    private String ucoolAssetsCleanPeriod;

    //cache��Ŀ¼
    private String ucoolCacheRoot;
    //daily��cache��Ŀ¼
    private String ucoolCacheRootDaily;
    //���ϵ�cache��Ŀ¼
    private String ucoolCacheRootOnline;
    //Ԥ����cache��Ŀ¼
    private String ucoolCacheRootPrepub;
    //assetsĿ¼
    private String ucoolAssetsRoot;

    //�Ƿ���assets���Թ���
    private String ucoolAssetsDebug;
    //�Ƿ��ڵ���ʱʹ��cache
    private String ucoolAssetsDebugCache;
    //����debugʱ�����ļ���
    private String ucoolAssetsDebugCorrect;

    public String getWebRoot() {
        return webRoot;
    }

    public void setWebRoot(String webRoot) {
        this.webRoot = webRoot;
    }

    public boolean isPrepub() {
        return isPrepub;
    }

    public void setPrepub(boolean prepub) {
        isPrepub = prepub;
    }

    public boolean isEnableAssets() {
        return isEnableAssets;
    }

    public void setEnableAssets(boolean enableAssets) {
        isEnableAssets = enableAssets;
    }

    public String getUcoolEnv() {
        return ucoolEnv;
    }

    public void setUcoolEnv(String ucoolEnv) {
        this.ucoolEnv = ucoolEnv;
    }

    public String[] getUcoolAssetsDebugCorrectStrings() {
        return ucoolAssetsDebugCorrectStrings;
    }

    public void setUcoolAssetsDebugCorrectStrings(String[] ucoolAssetsDebugCorrectStrings) {
        this.ucoolAssetsDebugCorrectStrings = ucoolAssetsDebugCorrectStrings;
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

    public String getUcoolCacheRootDaily() {
        return this.ucoolCacheRoot + "/" + ucoolCacheRootDaily;
    }

    public void setUcoolCacheRootDaily(String ucoolCacheRootDaily) {
        this.ucoolCacheRootDaily = ucoolCacheRootDaily;
    }

    public String getUcoolCacheRootOnline() {
        return this.ucoolCacheRoot + "/" + ucoolCacheRootOnline;
    }

    public void setUcoolCacheRootOnline(String ucoolCacheRootOnline) {
        this.ucoolCacheRootOnline = ucoolCacheRootOnline;
    }

    public String getUcoolAssetsDebugCache() {
        return ucoolAssetsDebugCache;
    }

    public void setUcoolAssetsDebugCache(String ucoolAssetsDebugCache) {
        this.ucoolAssetsDebugCache = ucoolAssetsDebugCache;
    }

    public Date getLastCleanTime() {
        return lastCleanTime;
    }

    public void setLastCleanTime(Date lastCleanTime) {
        this.lastCleanTime = lastCleanTime;
    }

    public String getUcoolComboDecollator() {
        return ucoolComboDecollator;
    }

    public void setUcoolComboDecollator(String ucoolComboDecollator) {
        this.ucoolComboDecollator = ucoolComboDecollator;
    }

    public String getUcoolAssetsDebugCorrect() {
        return ucoolAssetsDebugCorrect;
    }

    public void setUcoolAssetsDebugCorrect(String ucoolAssetsDebugCorrect) {
        this.ucoolAssetsDebugCorrect = ucoolAssetsDebugCorrect;
    }

    public String getUcoolPrepubIp() {
        return ucoolPrepubIp;
    }

    public void setUcoolPrepubIp(String ucoolPrepubIp) {
        this.ucoolPrepubIp = ucoolPrepubIp;
    }

    public String getUcoolCacheRootPrepub() {
        return this.ucoolCacheRoot + "/" + ucoolCacheRootPrepub;
    }

    public void setUcoolCacheRootPrepub(String ucoolCacheRootPrepub) {
        this.ucoolCacheRootPrepub = ucoolCacheRootPrepub;
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
                String[] keySplits = key.split(HttpTools.filterSpecialChar("."));
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
    public String getStateStringStyle(String state) {
        return state.equals("true")?"switch-open":"switch-close";
    }

    public String getStateStyle(boolean status) {
        return status ?"switch-open":"switch-close";
    }

    public static void main(String[] args) throws Exception {
        ConfigCenter configCenter = ConfigCenter.class.newInstance();
        configCenter.afterPropertiesSet();
    }
}