package com.yunyou.core.persistence;

import java.util.List;

/**
 * DAO支持类实现
 *
 * @param <T>
 */
public interface TreeMapper<T extends TreeEntity<T>> extends BaseMapper<T> {

    /**
     * 找到所有子节点
     */
    List<T> findByParentIdsLike(T entity);

    List<T> getChildren(String parentId);

    /**
     * 更新所有父节点字段
     */
    int updateParentIds(T entity);

    int updateSort(T entity);

}