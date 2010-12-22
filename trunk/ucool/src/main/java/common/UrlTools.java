package common;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-11-4 ����10:39
 */
public class UrlTools {

    private ConfigCenter configCenter;

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    /**
     * ��������������ļ��е�������������ip
     *
     * @param url      of type String
     * @param isOnline
     * @return String
     */
    public String urlFilter(String url, boolean isOnline) {
        /**
         * ��ֹ��λ�����ص�����ѭ��
         * ����һ�ֿ�����ֱ�ӷ��ʱ�������ip�����û����
         */
        if (url.indexOf("127.0.0.1") != -1) {
            return url.replace("127.0.0.1", getUsefullIp());
        }
        if (url.indexOf("localhost") != -1) {
            return url.replace("localhost", getUsefullIp());
        }
        if (isOnline) {
            for (String d : configCenter.getUcoolOnlineDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, getUsefullIp());
                }
            }
        } else {
            for (String d : configCenter.getUcoolDailyDomain().split(HttpTools.filterSpecialChar(","))) {
                if (url.indexOf(d) != -1) {
                    return url.replace(d, configCenter.getUcoolDailyIp());
                }
            }
        }
        return url;
    }

    /**
     *  ���ݵ�ǰ���û�ȡ��Ч��ip
     *
     * @return the usefullIp (type String) of this UrlTools object.
     */
    private String getUsefullIp() {
        return configCenter.isPrepub() ? configCenter.getUcoolPrepubIp() : configCenter.getUcoolOnlineIp();
    }

    /**
     * TODO �������
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
            //������ʹ�����õ��ļ������⴦��
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
