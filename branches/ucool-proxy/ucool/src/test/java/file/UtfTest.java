package file;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: zhangting
 * Date: 11-3-16
 * Time: 上午11:20
 * To change this template use File | Settings | File Templates.
 */
public class UtfTest {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\dev\\ucool-proxy\\ucool\\src\\test\\java\\file\\tshop-loader.js"), "utf-8"));
        String line = in.readLine();
        in.close();

        byte[] allbytes = line.getBytes("UTF-8");
        for (int i = 0; i < allbytes.length; i++) {
            int tmp = allbytes[i];
            String hexString = Integer.toHexString(tmp);
            // 1个byte变成16进制的，只需要2位就可以表示了，取后面两位，去掉前面的符号填充
            hexString = hexString.substring(hexString.length() - 2);
            System.out.print(hexString.toUpperCase());
            System.out.print(" ");
        }
//        System.out.println(line);
        
    }
}
