package common;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-10-24 ����11:52
 */
public class HttpTools {

    /**
     * �ж�refer���Ƿ��debug=true
     *
     * @param request of type HttpServletRequest
     * @return boolean
     */
    public static boolean isReferDebug(HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        /**
         * Ŀǰ֧��2�ַ�ʽ��?debug��debug=true
         */
        if(refer != null && (refer.indexOf("ucool=debug") != -1)) {
            return true;
        }
        return false;
    }
}
