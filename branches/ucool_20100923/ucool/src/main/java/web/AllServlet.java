package web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.*;
import java.net.URL;

/**
 * @author <a href="mailto:zhangting@taobao.com">��ͦ</a>
 * @since 2010-8-10 14:27:45
 */
public class AllServlet extends javax.servlet.http.HttpServlet {

    private String realPath;
    private static String ROOTPATH = "assets";
    private static String CACHEPATH = "cache";
    private static String TBCDN_DOMAIN = "http://115.238.23.242";

    /**
     * Method doPost ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     *
     * @throws ServletException when
     * @throws java.io.IOException      when
     * @author zhangting
     * @since 2010-8-11 9:29:14
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws javax.servlet.ServletException, IOException {
        if(realPath == null) {
            realPath = this.getServletContext().getRealPath("/");
        }

        String filePath = request.getRequestURI();

        response.setCharacterEncoding("gbk");
        PrintWriter out = response.getWriter();
        if (filePath.indexOf(".js") > 0 || filePath.indexOf(".css") > 0) {
            /**
             * ���ұ����ļ���û�еĻ����һ��棬û�л���Ĵ��������أ����߻��档
             */
            if (findLocalFile(filePath)) {
                pushFile(out, loadExistFile(filePath, false));
            } else if (findCacheFile(filePath)) {
                pushFile(out, loadExistFile(filePath, true));
            } else {
                if (cacheUrlFile(request)) {
                    pushFile(out, loadExistFile(filePath, true));
                } else {
                    //�������ʧ���ˣ���������ʵʵ������ȡ��
                    readUrlFile(out, request, true);
                }
            }
        } else {
            readUrlFile(out, request, false);
        }
    }

    /**
     * �����ϵ��ļ���������
     *
     * @param request of type String
     *
     * @return boolean
     *
     * @author zhangting
     * @since 2010-8-19 15:44:07
     */
    private boolean cacheUrlFile(HttpServletRequest request) {
        String allUrl = request.getRequestURL().toString();
        //���ص��Ե�ʱ���ֹ��ѭ��
        if(allUrl.indexOf("a.tbcdn.cn") == -1
                && allUrl.indexOf("assets.taobaocdn.com") == -1
                && allUrl.indexOf("assets.daily.taobao.net") == -1) {
            allUrl = TBCDN_DOMAIN + request.getRequestURI();
        }
        try {
            URL url = new URL(allUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            //�ȴ���Ŀ¼���ļ���������д����
            createDirectory(realPath + CACHEPATH + request.getRequestURI());

            File file = new File(realPath + CACHEPATH + request.getRequestURI());
            if (file.canWrite()) {
                FileWriter writer = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(writer);
                while ((line = in.readLine()) != null) {
                    bw.write(line);
                    bw.newLine();
                }
                bw.flush();
                in.close();
                bw.close();
                writer.close();
                return true;
            }
            return false;
        } catch (IOException e) {
        }
        return false;
    }

    private void pushFile(PrintWriter out, FileReader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while ((line = in.readLine()) != null) {
            out.println(line);
        }
        in.close();
        reader.close();
        out.flush();
    }

    /**
     * Method doGet ...
     *
     * @param request  of type HttpServletRequest
     * @param response of type HttpServletResponse
     *
     * @throws ServletException when
     * @throws java.io.IOException      when
     * @author zhangting
     * @since 2010-8-11 9:29:14
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }


    /**
     * Method readUrlFile ...
     *
     * @param out        of type PrintWriter
     * @param request of type String
     *
     * @throws javax.servlet.ServletException when
     * @throws java.io.IOException                    when
     * @author zhangting
     * @since 2010-8-11 9:29:00
     */
    private void readUrlFile(PrintWriter out, HttpServletRequest request, boolean force)
            throws javax.servlet.ServletException, IOException {
        String allUrl = request.getRequestURL().toString();
        if(force) {
            allUrl = TBCDN_DOMAIN + request.getRequestURI();
        } else {
            //���ص��Ե�ʱ���ֹ��ѭ��
            if (allUrl.indexOf("a.tbcdn.cn") == -1
                    && allUrl.indexOf("assets.taobaocdn.com") == -1
                    && allUrl.indexOf("assets.daily.taobao.net") == -1) {
                allUrl = TBCDN_DOMAIN + request.getRequestURI();
            }
        }
        
        try{
            URL url = new URL(allUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                out.println(line);
            }
            in.close();
        } catch(Exception e) {
            out.println("file not find");
        }
        out.flush();
    }

    /**
     * ��ѯ�ļ��Ƿ����
     *
     * @param filePath of type String
     *
     * @return boolean
     *
     * @author zhangting
     * @since 2010-8-16 13:51:20
     */
    private boolean findFile(String filePath) {
        File file = new File(realPath + filePath);
        return file.exists();
    }

    /**
     * ��assetsĿ¼�в��ұ����޸Ĺ����ļ�
     *
     * @param filePath of type String
     *
     * @return boolean
     *
     * @author zhangting
     * @since 2010-8-19 14:49:26
     */
    private boolean findLocalFile(String filePath) {
        File file = new File(realPath + ROOTPATH);
        if(!file.exists()) {
            file.mkdirs();
        }
        return findFile(ROOTPATH + filePath);
    }

    /**
     * ��cacheĿ¼�����油�ļ�
     *
     * @param filePath of type String
     *
     * @return boolean
     *
     * @author zhangting
     * @since 2010-8-19 14:50:35
     */
    private boolean findCacheFile(String filePath) {
        return findFile(CACHEPATH + filePath);
    }

    /**
     * ���ر����ļ����߻����ļ�
     *
     * @param filePath of type String
     * @param isCache  of type boolean
     *
     * @return FileInputStream
     *
     * @author zhangting
     * @since 2010-8-19 15:22:02
     */
    private FileReader loadExistFile(String filePath, boolean isCache) {
        String root = isCache ? CACHEPATH : ROOTPATH;
        root = realPath + root;
        try {
            return new FileReader(root + filePath);
        } catch (FileNotFoundException e) {

        }
        return null;
    }

    /**
     * ֧�ִ������༶Ŀ¼���ļ�
     *
     * @param filePath of type String
     *
     * @throws java.io.IOException when
     * @author zhangting
     * @since 2010-8-19 16:16:53
     */
    private void createDirectory(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.close();
    }

}
