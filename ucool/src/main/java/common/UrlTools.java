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
     * @param url      of type String
     * @return String
     */
    public String urlFilter(String url) {
        /**
         * ��ֹ��λ�����ص�����ѭ��
         * ����һ�ֿ�����ֱ�ӷ��ʱ�������ip�����û����
         */
        if (url.indexOf("127.0.0.1") != -1) {
            return url.replace("127.0.0.1", "a.tbcdn.cn");
        }

        if(url.indexOf(configCenter.getUcoolProxyIp()) != -1) {
            return url.replace(configCenter.getUcoolProxyIp(), "a.tbcdn.cn");
        }
        return url;
    }
}
