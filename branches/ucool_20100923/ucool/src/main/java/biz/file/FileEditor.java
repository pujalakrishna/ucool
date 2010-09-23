package biz.file;

import java.io.*;

/**
 * @author <a href="mailto:czy88840616@gmail.com">czy</a>
 * @since 2010-9-23 14:17:50
 */
public class FileEditor {
    /**
     * 查询文件是否存在
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
     * Method loadFile ...
     *
     * @param filePath of type String
     * @return FileReader
     * @throws FileNotFoundException when
     */
    public FileReader loadFile(String filePath) throws FileNotFoundException {
        return new FileReader(filePath);
    }

    /**
     * Method safeLoadFile ...
     *
     * @param filePath of type String
     * @return FileReader
     */
    public FileReader safeLoadFile(String filePath) {
        if (findFile(filePath)) {
            try {
                return new FileReader(filePath);
            } catch (Exception e) {
                //log
            }
            return null;
        }
        return null;
    }

    /**
     * 支持创建带多级目录的文件
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
     * Method pushFile ...
     *
     * @param out    of type PrintWriter
     * @param reader of type FileReader
     * @throws IOException when
     */
    public void pushFile(PrintWriter out, FileReader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        pushStream(out, in);
    }

    /**
     * Method pushStream ...
     *
     * @param out of type PrintWriter
     * @param in of type BufferedReader
     * @throws IOException when
     */
    public void pushStream(PrintWriter out, BufferedReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            out.println(line);
        }
        in.close();
        out.flush();
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
    }

}
