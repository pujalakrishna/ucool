package biz;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:zhangting@taobao.com">уем╕</a>
 * @since 2010-8-30 14:35:48
 */
public class JsonpParser {
    
    private static String GETSCRIPT_XML = "getscript.properties";
    
    /**
     * Method readProperties ...
     *
     * @param rootPath
     * @author zhangting
     * @since 2010-8-30 15:25:28
     * @return Map<String, String>
     */
    public static Map<String, String> readProperties(String rootPath) {
        Map<String, String> map = new HashMap<String, String>();

        try {
            FileReader fileReader = new FileReader(rootPath + GETSCRIPT_XML);
            Properties p = new Properties();
            p.load(fileReader);
            return (Map)p;
        } catch (IOException e) {
        }

        return map;
    }

}
