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
        return refer != null && (refer.indexOf("ucool=debug") != -1);
    }

    public static boolean isReferClean(HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        return refer != null && (refer.indexOf("op=clean") != -1);
    }

    /**
     * ���������ַ�
     * @param input
     * @return
     */
    public static String filterSpecialChar (String input) {
        StringBuffer filtered = new StringBuffer(input.length());
        char c;
        for (int i = 0; i < input.length(); i++) {
            c = input.charAt(i);
            switch (c) {
                case '"':filtered.append("\\\"");break;
                case '\'':filtered.append("\\'");break;
                case '\\':filtered.append("\\\\");break;
                case '?':filtered.append("\\?");break;
                case '.':filtered.append("\\.");break;
                default:filtered.append(c);
            }
        }
        return (filtered.toString());
    }

}
