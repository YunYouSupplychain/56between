package com.yunyou.core.service;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.DataEntity;
import com.yunyou.core.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * Service基类
 *
 * @author yunyou
 */
@Transactional(readOnly = true)
public abstract class CrudService<M extends BaseMapper<T>, T extends DataEntity<T>> extends BaseService {

    /**
     * 持久层对象
     */
    @Autowired
    protected M mapper;

    /**
     * 获取单条数据
     */
    public T get(String id) {
        return mapper.get(id);
    }

    /**
     * 获取单条数据
     */
    public T get(T entity) {
        return mapper.get(entity);
    }

    /**
     * 查询列表数据
     */
    public List<T> findList(T entity) {
        dataRuleFilter(entity);
        return mapper.findList(entity);
    }

    /**
     * 查询分页数据
     */
    public Page<T> findPage(Page<T> page, T entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findList(entity));
        return page;
    }

    /**
     * 保存数据（插入或更新）
     */
    @Transactional
    public void save(T entity) {
        if (entity.getIsNewRecord()) {
            entity.preInsert();
            mapper.insert(entity);
        } else {
            entity.preUpdate();
            int rows = mapper.update(entity);
            if (rows == 0) {
                throw new GlobalException("数据已过期");
            }
        }
    }

    /**
     * 删除数据
     */
    @Transactional
    public void delete(T entity) {
        mapper.delete(entity);
    }


    /**
     * 删除全部数据
     */
    @Deprecated
    @Transactional
    public void deleteAll(Collection<T> entities) {
        for (T entity : entities) {
            mapper.delete(entity);
        }
    }

    /**
     * 删除全部数据
     */
    @Deprecated
    @Transactional
    public void deleteAllByLogic(Collection<T> entities) {
        for (T entity : entities) {
            mapper.deleteByLogic(entity);
        }
    }

    /**
     * 获取单条数据
     */
    public T findUniqueByProperty(String propertyName, Object value) {
        return mapper.findUniqueByProperty(propertyName, value);
    }

    /**
     * 动态sql
     */
    public List<Object> executeSelectSql(String sql) {
        return mapper.execSelectSql(sql);
    }

    @Transactional
    public void executeInsertSql(String sql) {
        mapper.execInsertSql(sql);
    }

    @Transactional
    public void executeUpdateSql(String sql) {
        mapper.execUpdateSql(sql);
    }

    @Transactional
    public void executeDeleteSql(String sql) {
        mapper.execDeleteSql(sql);
    }

}