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
     * @param url      of type String
     * @param isOnline
     * @return String
     */
    public String urlFilter(String url, boolean isOnline) {
        /**
         * 防止定位到本地导致自循环
         * 还有一种可能是直接访问本地内网ip，这个没法子
         */
        if (url.indexOf("127.0.0.1") != -1) {
            return url.replace("127.0.0.1", getUsefullIp());
        }
        if (url.indexOf("localhost") != -1) {
            return url.replace("localhost", getUsefullIp());
        }
        if (isOnline) {
            url = url.replace("assets.daily.taobao.net", "a.tbcdn.cn");
            for (String d : configCenter.getUcoolOnlineDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.indexOf(d) != -1) {
                    if(configCenter.isPrepub()) {
                        if(url.indexOf("?") != -1) {
                            url += "&env=prepub";
                        } else {
                            url += "?env=prepub";
                        }
                    }
                    return url.replace(d, getUsefullIp());
                }
            }
        } else {
            for (String d : configCenter.getUcoolDailyDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.indexOf(d) != -1) {
                    if (url.indexOf("?") != -1) {
                        url += "&env=daily";
                    } else {
                        url += "?env=daily";
                    }
                    return url.replace(d, configCenter.getUcoolDailyIp());
                }
            }
        }
        return url;
    }

    /**
     *  根据当前配置获取有效的ip
     *
     * @return the usefullIp (type String) of this UrlTools object.
     */
    private String getUsefullIp() {
        return configCenter.isPrepub() ? configCenter.getUcoolPrepubIp() : configCenter.getUcoolOnlineIp();
    }

    /**
     * TODO 抽象出来
     *
     * @param filePath
     * @param fullUrl
     * @return
     */
    public String debugMode(String filePath, String fullUrl) {
        if (filePath.indexOf("-min") != -1) {
            filePath = filePath.replace("-min", "");
        } else {
            if (filePath.indexOf(".source.") != -1) {
                return filePath;
            }
            //在这里使用配置的文件作特殊处理
            for (String filterString : configCenter.getUcoolAssetsDebugCorrectStrings()) {
                if (fullUrl.indexOf(filterString) != -1) {
                    return filePath;
                }
            }
            if (filePath.endsWith(".css")) {
                filePath = filePath.replace(".css", ".source.css");
            } else {
                filePath = filePath.replace(".js", ".source.js");
            }
        }
        return filePath;
    }
}
