package com.yunyou.modules.sys.mapper;

import com.yunyou.core.persistence.TreeMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.sys.entity.Office;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 机构MAPPER接口
 *
 * @author yunyou
 * @version 2017-05-16
 */
@MyBatisMapper
public interface OfficeMapper extends TreeMapper<Office> {

    Office getByCode(String code);

    List<Office> findCompanyData(Office office);

    List<String> findChildIdsByParentId(@Param(value = "parentId") String parentId);

    List<Office> getByName(String name);

    List<Office> findChildrenByParentId(@Param("parentId") String parentId);

    List<Office> findOutletMatchedOrg(Office office);
}
