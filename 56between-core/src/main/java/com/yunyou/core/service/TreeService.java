package com.yunyou.core.service;

import com.yunyou.common.exception.ServiceException;
import com.yunyou.common.utils.Reflections;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.TreeEntity;
import com.yunyou.core.persistence.TreeMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service基类
 *
 * @author yunyou
 * @version 2017-05-16
 */
@Transactional(readOnly = true)
public abstract class TreeService<D extends TreeMapper<T>, T extends TreeEntity<T>> extends CrudService<D, T> {

    @Override
    @Transactional
    public void save(T entity) {
        @SuppressWarnings("unchecked")
        Class<T> entityClass = Reflections.getClassGenricType(getClass(), 1);

        // 如果没有设置父节点，则代表为跟节点，有则获取父节点实体
        if (entity.getParent() == null
            || StringUtils.isBlank(entity.getParentId())
            || "0".equals(entity.getParentId())) {
            entity.setParent(null);
        } else {
            entity.setParent(super.get(entity.getParentId()));
        }
        if (entity.getParent() == null) {
            T parentEntity;
            try {
                parentEntity = entityClass.getConstructor(String.class).newInstance("0");
            } catch (Exception e) {
                throw new ServiceException(e);
            }
            entity.setParent(parentEntity);
            entity.getParent().setParentIds(StringUtils.EMPTY);
        }
        // 获取修改前的parentIds，用于更新子节点的parentIds
        String oldParentIds = entity.getParentIds();
        // 设置新的父节点串
        entity.setParentIds(entity.getParent().getParentIds() + entity.getParent().getId() + ",");
        // 保存或更新实体
        super.save(entity);
        // 更新子节点 parentIds
        T o;
        try {
            o = entityClass.newInstance();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
        o.setParentIds("%," + entity.getId() + ",%");
        List<T> list = mapper.findByParentIdsLike(o);
        for (T e : list) {
            if (e.getParentIds() != null && oldParentIds != null) {
                e.setParentIds(e.getParentIds().replace(oldParentIds, entity.getParentIds()));
                preUpdateChild(entity, e);
                mapper.updateParentIds(e);
            }
        }
    }

    /**
     * 预留接口，用户更新子节点前调用
     */
    protected void preUpdateChild(T entity, T childEntity) {

    }

    public List<T> getChildren(String parentId) {
        return mapper.getChildren(parentId);
    }
}
