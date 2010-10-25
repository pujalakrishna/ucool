package common;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 10-10-24 下午11:52
 */
public class HttpTools {

    /**
     * 判断refer中是否带debug=true
     *
     * @param request of type HttpServletRequest
     * @return boolean
     */
    public static boolean isReferDebug(HttpServletRequest request) {
        String refer = request.getHeader("Referer");
        /**
         * 目前支持2种方式：?debug和debug=true
         */
        if(refer != null && (refer.indexOf("ucool=debug") != -1)) {
            return true;
        }
        return false;
    }
}
