package common;

import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.Properties;

/**
 * 包含所有配置的开关文件，具体参数含义见config.properties
 *
 * @author <a href="mailto:zhangting@taobao.com">张挺</a>
 * @since 2010-9-20 13:32:35
 */
public class ConfigCenter implements InitializingBean {

    /**
     * 以下配置在属性文件中不存在，只是为了方便查找文件而保存根目录绝对路径
     */
    private String webRoot;

    /**
     * 以下为配置文件使用
     */

    //当前环境local or vm
    private String ucoolEnv;

    //daily的域名
    private String ucoolDailyDomain;
    //线上域名
    private String ucoolOnlineDomain;
    //daily ip
    private String ucoolDailyIp;
    //线上ip
    private String ucoolOnlineIp;

    //是否开启assest的自动清理
    private String ucoolAssetsAutoClean;
    //是否开启cache目录的自动清理
    private String ucoolCacheAutoClean;
    //cache目录清理周期
    private String ucoolCacheCleanPeriod;
    //assets目录清理周期
    private String ucoolAssetsCleanPeriod;

    //cache根目录
    private String ucoolCacheRoot;
    //daily的cache根目录
    private String ucoolCacheRootDaily;
    //线上的cache根目录
    private String ucoolCacheRootOnline;
    //assets目录
    private String ucoolAssetsRoot;

    //是否开启assets调试功能
    private String ucoolAssetsDebug;
    //是否在调试时使用cache
    private String ucoolAssetsDebugCache;

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

    public String getUcoolCacheRootDaily() {
        return ucoolCacheRootDaily;
    }

    public void setUcoolCacheRootDaily(String ucoolCacheRootDaily) {
        this.ucoolCacheRootDaily = ucoolCacheRootDaily;
    }

    public String getUcoolCacheRootOnline() {
        return ucoolCacheRootOnline;
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
     * 用于pz.jsp的css调用
     *
     * @param state of type String
     * @return String
     */
    public String getStateStyle(String state) {
        return state.equals("true")?"open":"closed";
    }

    /**
     * 用于pz.jsp的状态调用
     *
     * @param state of type String
     * @return String
     */
    public String getStateOper(String state) {
        return state.equals("true")?"点击关闭":"点击打开";
    }

    /**
     * 用于pz.jsp的状态调用
     *
     * @param state of type String
     * @return String
     */
    public String getCurState(String state) {
        return state.equals("true")?"已打开":"已关闭";
    }

    public static void main(String[] args) throws Exception {
        ConfigCenter configCenter = ConfigCenter.class.newInstance();
        configCenter.afterPropertiesSet();
    }
}
