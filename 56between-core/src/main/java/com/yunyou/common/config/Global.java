package com.yunyou.common.config;

import com.google.common.collect.Maps;
import com.yunyou.common.utils.StringUtils;
import org.springframework.core.env.Environment;

import java.io.File;
import java.util.Map;

/**
 * 全局配置类
 *
 * @author yunyou
 */
public class Global {

    /**
     * 显示
     */
    public static final String SHOW = "1";
    /**
     * 隐藏
     */
    public static final String HIDE = "0";
    /**
     * 是
     */
    public static final String YES = "1";
    /**
     * 否
     */
    public static final String NO = "0";
    /**
     * Y/N
     */
    public static final String Y = "Y";
    public static final String N = "N";
    /**
     * true/false
     */
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    /**
     * 树节点根节点
     */
    public static final String TREE_ROOT_NODE = "0";
    /**
     * 批量操作SQL每次最大操作数据量
     */
    public static final int DB_SQL_MAX_OP_QTY = 999;
    /**
     * 上传文件基础存放目录
     */
    public static final String USER_FILES_DIR_NAME = "userfiles";
    /**
     * 用户二维码存放目录
     */
    public static final String USER_QRCODE_DIR_NAME = "qrcode";
    /**
     * 用户图片存放目录
     */
    public static final String USER_IMAGES_DIR_NAME = "images";
    /**
     * 上下文环境配置
     */
    private static Environment env;
    /**
     * 保存全局属性值
     */
    private static final Map<String, String> CONFIG_MAP = Maps.newHashMap();

    /**
     * 初始化上下文环境配置
     *
     * @param environment
     */
    public static void setEnvironment(Environment environment) {
        env = environment;
    }

    /**
     * 获取系统配置
     *
     * @see Environment
     */
    public static String getConfig(String key) {
        String value = null;
        if (CONFIG_MAP.containsKey(key)) {
            value = CONFIG_MAP.get(key);
        }
        if (value == null) {
            value = env.getProperty(key);
            CONFIG_MAP.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 获取管理端根路径
     */
    public static String getAdminPath() {
        return getConfig("adminPath");
    }

    /**
     * 获取前端根路径
     */
    public static String getFrontPath() {
        return getConfig("frontPath");
    }

    /**
     * 获取URL后缀
     */
    public static String getUrlSuffix() {
        return getConfig("urlSuffix");
    }

    /**
     * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
     */
    public static Boolean isDemoMode() {
        return Y.equals(System.getenv("APP_DEMO"));
    }

    /**
     * 获取缓存文件的根目录
     */
    public static String getUserFilesBaseDir() {
        return System.getProperty("user.home") + File.separator + USER_FILES_DIR_NAME + File.separator;
    }

}
