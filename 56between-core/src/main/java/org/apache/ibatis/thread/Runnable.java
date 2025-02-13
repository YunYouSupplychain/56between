package org.apache.ibatis.thread;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.Configuration;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.NestedIOException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 刷新使用进程
 */
public class Runnable implements java.lang.Runnable {

    public static Logger log = Logger.getLogger(Runnable.class);

    private String location;
    private final Configuration configuration;
    /**
     * 是否启动刷新进程
     */
    private static final boolean enabled;
    /**
     * xml文件夹匹配字符串，根据需要修改
     */
    private static String mappingPath;
    /**
     * 延迟刷新秒数
     */
    private static int delaySeconds;
    /**
     * 休眠时间
     */
    private static int sleepSeconds;
    /**
     * 上一次刷新时间
     */
    private Long beforeTime = 0L;
    /**
     * 是否执行刷新
     */
    private static boolean refresh = false;

    static {
        enabled = "true".equals(PropertiesUtil.getString("mybatis.refresh.runnable.enabled"));
        delaySeconds = PropertiesUtil.getInt("mybatis.refresh.runnable.delaySeconds");
        sleepSeconds = PropertiesUtil.getInt("mybatis.refresh.runnable.sleepSeconds");
        mappingPath = PropertiesUtil.getString("mybatis.mappingPath");

        delaySeconds = delaySeconds == 0 ? 50 : delaySeconds;
        sleepSeconds = sleepSeconds == 0 ? 1 : sleepSeconds;
        mappingPath = StringUtils.isBlank(mappingPath) ? "mappings" : mappingPath;

        if (log.isDebugEnabled()) {
            log.debug("[delaySeconds] " + delaySeconds);
            log.debug("[sleepSeconds] " + sleepSeconds);
            log.debug("[mappingPath] " + mappingPath);
        }
    }

    public static boolean isRefresh() {
        return refresh;
    }

    public Runnable(String location, Configuration configuration) {
        this.location = location.replaceAll("\\\\", "/");
        this.configuration = configuration;
    }

    @Override
    public void run() {
        location = location.substring("file [".length(), location.lastIndexOf(mappingPath) + mappingPath.length());
        beforeTime = System.currentTimeMillis();

        if (log.isDebugEnabled()) {
            log.debug("[location] " + location);
            log.debug("[configuration] " + configuration);
        }
        if (enabled) {
            start(this);
        }
    }

    public void start(final Runnable runnable) {
        new Thread(() -> {
            try {
                Thread.sleep(delaySeconds * 1000L);
            } catch (InterruptedException ignored) {
            }
            refresh = true;

            while (true) {
                runnable.refresh(location, beforeTime);

                try {
                    Thread.sleep(sleepSeconds * 1000L);
                } catch (InterruptedException ignored) {
                }
            }
        }).start();
    }

    /**
     * 执行刷新
     *
     * @param filePath   刷新目录
     * @param beforeTime 上次刷新时间
     */
    public void refresh(String filePath, Long beforeTime) {
        // 本次刷新时间
        long refreshTime = System.currentTimeMillis();

        List<File> refreshFiles = this.getRefreshFile(new File(filePath), beforeTime);
        if (!refreshFiles.isEmpty()) {
            log.debug("refresh files:" + refreshFiles.size());
        }
        for (File file : refreshFiles) {
            log.debug("refresh file:" + file.getAbsolutePath());
            log.debug("refresh filename:" + file.getName());

            try {
                SqlSessionFactoryBean.refresh(new FileInputStream(file), file.getAbsolutePath(), configuration);
            } catch (NestedIOException e) {
                log.error(e.getMessage(), e);
            } catch (FileNotFoundException e) {
                log.error("This file not found path " + file.getAbsolutePath(), e);
            }
        }
        // 如果刷新了文件，则修改刷新时间，否则不修改
        if (!refreshFiles.isEmpty()) {
            this.beforeTime = refreshTime;
        }
    }

    /**
     * 获取需要刷新的文件列表
     *
     * @param dir        目录
     * @param beforeTime 上次刷新时间
     * @return 刷新文件列表
     */
    public List<File> getRefreshFile(File dir, Long beforeTime) {
        List<File> refreshes = new ArrayList<>();

        File[] files = dir.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            File file = files[i];
            if (file.isDirectory()) {
                refreshes.addAll(this.getRefreshFile(file, beforeTime));
            } else if (file.isFile()) {
                if (this.check(file, beforeTime)) {
                    refreshes.add(file);
                }
            } else {
                log.debug("error file." + file.getName());
            }
        }
        return refreshes;
    }

    /**
     * 判断文件是否需要刷新
     *
     * @param file       文件
     * @param beforeTime 上次刷新时间
     * @return 需要刷新返回true，否则返回false
     */
    public boolean check(File file, Long beforeTime) {
        return file.lastModified() > beforeTime;
    }

}
