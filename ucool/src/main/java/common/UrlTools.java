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
     * @param url      of type String
     * @return String
     */
    public String urlFilter(String url) {
        /**
         * 防止定位到本地导致自循环
         * 还有一种可能是直接访问本地内网ip，这个没法子
         */
        if (url.indexOf("127.0.0.1") != -1) {
            return url.replace("127.0.0.1", "a.tbcdn.cn");
        }

        if(url.indexOf(configCenter.getUcoolProxyIp()) != -1) {
            return url.replace(configCenter.getUcoolProxyIp(), "a.tbcdn.cn");
        }
        return url;
    }

    /**
     * 根据url规则返回读取的字符集
     * @param url
     * @return
     */
    public String getReadCharSet(String url) {
        String encoding = "utf-8";
        //  在这里使用配置的文件作特殊处理，把给定的文件使用gbk编码
        if (url.indexOf(".source.") != -1) {
            encoding = "gbk";
        }
        return encoding;
    }
}
