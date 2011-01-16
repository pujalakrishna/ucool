package common;

import biz.file.FileEditor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 13:18:10
 */
public class SuffixDispatcher {

    private DispatchMapping dispatchMapping;

    private UrlTools urlTools;

    private ConfigCenter configCenter;

    private FileEditor fileEditor;

    public void setDispatchMapping(DispatchMapping dispatchMapping) {
        this.dispatchMapping = dispatchMapping;
    }

    public void setUrlTools(UrlTools urlTools) {
        this.urlTools = urlTools;
    }

    public void setConfigCenter(ConfigCenter configCenter) {
        this.configCenter = configCenter;
    }

    public void setFileEditor(FileEditor fileEditor) {
        this.fileEditor = fileEditor;
    }

    /**
     * Method dispatch ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     */
    public void dispatch(HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {

        String fullUrl = (String) request.getAttribute("fullUrl");
        String env = request.getParameter("env");
        env = (env == null) ? "online" : env;
        fullUrl = urlTools.urlFilter(fullUrl, env);
        request.setAttribute("fullUrl", fullUrl);

        //assets�õ���࣬�������ж�
        if (fullUrl.indexOf(".js") != -1 || fullUrl.indexOf(".css") != -1) {
            doCleanUp(request);
            this.dispatchMapping.getMapping("assets").doHandler(request, response);
        } else if (fullUrl.indexOf(".png") != -1 || fullUrl.indexOf(".gif") != -1 || fullUrl.indexOf(".ico") != -1) {
            //ͼƬ���� Ŀǰ��png,gif,ico
            this.dispatchMapping.getMapping("png").doHandler(request, response);
        } else if (fullUrl.indexOf(".htm") != -1) {
            // htmҳ�洦��
            this.dispatchMapping.getMapping("htm").doHandler(request, response);
        } else {
            // ������ʽ�Ĵ���Ŀǰ����swf��xml
            this.dispatchMapping.getMapping("other").doHandler(request, response);
        }
    }

    private void doCleanUp(HttpServletRequest request) {
        if (HttpTools.isReferClean(request) || "clean".equals(request.getParameter("op"))) {
            File file = new File(configCenter.getWebRoot() + configCenter.getUcoolCacheRoot() + request.getRequestURI());
            if(file.exists()) {
                file.delete();
            }
        }
    }

}
