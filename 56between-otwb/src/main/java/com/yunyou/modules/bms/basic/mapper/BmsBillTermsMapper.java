package com.yunyou.modules.bms.basic.mapper;

import com.yunyou.core.persistence.BaseMapper;
import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.bms.basic.entity.BmsBillTerms;
import com.yunyou.modules.bms.basic.entity.extend.BmsBillTermsEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 计费条款MAPPER接口
 *
 * @author Jianhua Liu
 * @version 2019-05-27
 */
@MyBatisMapper
public interface BmsBillTermsMapper extends BaseMapper<BmsBillTerms> {

    BmsBillTermsEntity getEntity(String id);

    List<BmsBillTerms> findPage(BmsBillTermsEntity entity);

    BmsBillTerms getByCode(@Param("code") String code);

    void remove(@Param("code") String code);
}