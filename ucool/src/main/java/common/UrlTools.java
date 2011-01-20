package common;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-11-4 下午10:39
 */
public class UrlTools {

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * 把请求的在配置文件中的所有域名换成ip
     *
     *
     *
     * @param url      of type String
     * @param env
     * @return String
     */
    public String urlFilter(String url, String env) {
        /**
         * 防止定位到本地导致自循环
         */
        if(url.indexOf("127.0.0.1") != -1) {
            if("daily".equals(env)) {
                return url.replace("127.0.0.1", "assets.daily.taobao.net");
            } else if("prepub".equals(env)) {
                return url.replace("127.0.0.1", configCenter.getUcoolPrepubIp());
            } else {
                return url.replace("127.0.0.1", "a.tbcdn.cn");
            }
        }

        if(url.indexOf(configCenter.getUcoolProxyIp()) != -1) {
            if("daily".equals(env)) {
                return url.replace(configCenter.getUcoolProxyIp(), "assets.daily.taobao.net");
            } else if("prepub".equals(env)) {
                return url.replace(configCenter.getUcoolProxyIp(), configCenter.getUcoolPrepubIp());
            } else {
                return url.replace(configCenter.getUcoolProxyIp(), "a.tbcdn.cn");
            }
        }
        return url;
    }

    /**
     * 根据url规则返回读取的字符集
     * @return
     * @param fullUrl
     */
    public String getReadCharSet(String fullUrl) {
        String encoding = "gbk";
        //  在这里使用配置的文件作特殊处理，把给定的文件使用gbk编码
        for (String enCodingString : configCenter.getUcoolAssetsEncodingCorrectStrings()) {
            if (fullUrl.indexOf(enCodingString) != -1) {
                encoding = "utf-8";
                break;
            }
        }
        return encoding;
    }

    /**
     * 这个方法肯定在urlFilter这个方法之后被调用，所以线上的链接一定是有a.tbcdn.cn
     * @param fullUrl
     * @return
     */
    public boolean isOnline(String fullUrl) {
        return fullUrl.indexOf("a.tbcdn.cn") != -1;
    }
}
