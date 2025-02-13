package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsBillSubject;
import com.yunyou.modules.bms.basic.entity.extend.BmsBillSubjectEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 费用科目MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@MyBatisMapper
public interface BmsBillSubjectMapper extends BaseMapper<BmsBillSubject> {

    List<BmsBillSubjectEntity> findPage(BmsBillSubject entity);

    List<BmsBillSubject> findGrid(BmsBillSubjectEntity entity);

    BmsBillSubject getByCode(@Param("code") String code, @Param("orgId") String orgId);

    void remove(@Param("code") String code, @Param("orgId") String orgId);
}