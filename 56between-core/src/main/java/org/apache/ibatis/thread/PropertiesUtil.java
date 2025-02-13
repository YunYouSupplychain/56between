package org.apache.ibatis.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

    private static final Properties properties = new Properties();

    static {
        ClassLoader cl = PropertiesUtil.class.getClassLoader();
        try {
            properties.load(cl.getResourceAsStream("mybatis-refresh.properties"));
        } catch (IOException e) {
            logger.error("Load 'mybatis-refresh.properties' file error.");
        }
    }

    public static int getInt(String key) {
        int i = 0;
        try {
            i = Integer.parseInt(getString(key));
        } catch (Exception ignored) {
        }
        return i;
    }

    public static String getString(String key) {
        return properties.getProperty(key);
    }

}
