package com.yunyou.modules.sys.service;

/**
 * 创建机构时初始化公共接口
 *
 * @author yunyou
 * @since 2023/3/7 16:41
 */
public interface OfficeInitialize {

    /**
     * 初始化
     *
     * @param orgId 机构ID
     */
    void init(String orgId);
}
