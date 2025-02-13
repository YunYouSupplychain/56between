package com.yunyou.modules.interfaces.edi.mapper;

import com.yunyou.core.persistence.annotation.MyBatisMapper;
import com.yunyou.modules.interfaces.edi.entity.EdiLog;

import java.util.List;

@MyBatisMapper
public interface EdiLogMapper {

    void save(EdiLog log);

    List<EdiLog> findList(EdiLog log);

}
