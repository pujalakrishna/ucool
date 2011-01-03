package biz.file;

import java.io.*;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 14:17:50
 */
public class FileEditor {
    /**
     * ��ѯ�ļ��Ƿ����
     *
     * @param filePath of type String
     * @return boolean
     * @author zhangting
     * @since 2010-8-16 13:51:20
     */
    public boolean findFile(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * ʹ��fileInputStream�����ļ����������ñ���
     *
     * @param filePath of type String
     * @return FileReader
     * @throws FileNotFoundException when
     */
    public InputStreamReader loadFileStream(String filePath, String encoding)
            throws FileNotFoundException, UnsupportedEncodingException {
        return new InputStreamReader(new FileInputStream(filePath), encoding);
    }

    /**
     * ֧�ִ������༶Ŀ¼���ļ�
     *
     * @param filePath of type String
     * @throws java.io.IOException when
     * @author zhangting
     * @since 2010-8-19 16:16:53
     */
    public void createDirectory(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        file.createNewFile();
        FileWriter fw = new FileWriter(file);
        fw.close();
    }

    /**
     * Method pushStream ...
     *
     * @param out of type PrintWriter
     * @param in of type BufferedReader
     * @param fileUrl ����combo��ʱ��������ĸ��ļ�
     * @throws IOException when
     */
    public boolean pushStream(PrintWriter out, BufferedReader in, String fileUrl, boolean skipCommet) throws IOException {
        String line;
        if ((line = in.readLine()) != null && line.equals("/*not found*/")) {
            in.close();
            return false;
        } else {
            if(!skipCommet) {
                out.println("/*ucool filePath=" + fileUrl + "*/");
            }
            out.println(line);
            out.flush();
        }
        while ((line = in.readLine()) != null) {
            out.println(line);
        }
        in.close();
        out.flush();
        return true;
    }

    public void pushFileOutputStream(PrintWriter out, InputStreamReader reader, String filePath) {
        try {
            BufferedReader in = new BufferedReader(reader);
            pushStream(out, in, filePath, false);
        } catch(Exception e) {
            //���������쳣�������п��ܻ���ʧ�ܣ�����ȡ�����ļ�
        }
    }

    /**
     * Method saveFile ...
     *
     * @param filePath of type String
     * @param in of type BufferedReader
     * @return boolean
     * @throws IOException when
     */
    public boolean saveFile(String filePath, BufferedReader in) throws IOException {
        String line;
        File file = new File(filePath);
        if (file.canWrite()) {
            FileOutputStream writerStream = new FileOutputStream(file);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(writerStream, "utf-8"));
            if ((line = in.readLine()) != null && line.equals("/*not found*/")) {
                bw.close();
                writerStream.close();
                in.close();
                file.delete();
                return false;
            } else {
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
            while ((line = in.readLine()) != null) {
                bw.write(line);
                bw.newLine();
                bw.flush();
            }
            bw.close();
            writerStream.close();
            in.close();
            return true;
        }
        return false;
    }

    public boolean removeDirectory(String filePath) {
        return delAllFile(filePath);
    }

    public boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//��ɾ���ļ���������ļ�
                delFolder(path + "/" + tempList[i]);//��ɾ�����ļ���
                flag = true;
            }
        }
        return flag;
    }

    private void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //ɾ����������������
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //ɾ�����ļ���
        } catch (Exception e) {
            
        }
    }

    public static void main(String[] args) {
        FileEditor editor = new FileEditor();
        editor.removeDirectory("D:\\dev\\ucool\\target\\ucool\\cache\\daily");
    }

}
