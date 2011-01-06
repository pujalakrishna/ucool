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
     *
     *
     * @param url      of type String
     * @param env
     * @return String
     */
    public String urlFilter(String url, String env) {
        /**
         * ��ֹ��λ�����ص�����ѭ��
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
     * ����url���򷵻ض�ȡ���ַ���
     * @return
     * @param fullUrl
     */
    public String getReadCharSet(String fullUrl) {
        String encoding = "gbk";
        //  ������ʹ�����õ��ļ������⴦���Ѹ������ļ�ʹ��gbk����
        for (String enCodingString : configCenter.getUcoolAssetsEncodingCorrectStrings()) {
            if (fullUrl.indexOf(enCodingString) != -1) {
                encoding = "utf-8";
                break;
            }
        }
        return encoding;
    }
}
