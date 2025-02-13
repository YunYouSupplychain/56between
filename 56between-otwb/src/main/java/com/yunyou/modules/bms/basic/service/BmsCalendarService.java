package com.yunyou.modules.bms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.bms.basic.entity.BmsCalendar;
import com.yunyou.modules.bms.basic.entity.extend.BmsCalendarEntity;
import com.yunyou.modules.bms.basic.entity.template.BmsCalendarTemplate;
import com.yunyou.modules.bms.basic.mapper.BmsCalendarMapper;
import com.yunyou.modules.bms.common.BmsException;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.service.OfficeService;
import com.yunyou.modules.sys.utils.DictUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class BmsCalendarService extends CrudService<BmsCalendarMapper, BmsCalendar> {
    @Autowired
    private OfficeService officeService;

    @SuppressWarnings("unchecked")
    public Page<BmsCalendarEntity> findPage(Page page, BmsCalendarEntity qEntity) {
        dataRuleFilter(qEntity);
        qEntity.setPage(page);
        page.setList(mapper.findPage(qEntity));
        return page;
    }

    public BmsCalendarEntity getEntity(String id) {
        return mapper.getEntity(id);
    }

    public BmsCalendar getByDate(Date date, String orgId) {
        BmsCalendar calendar = mapper.getByDate(date, orgId);
        return calendar == null ? new BmsCalendar() : calendar;
    }

    public void saveValidator(BmsCalendarEntity entity) {
        if (entity.getDate() == null) {
            throw new BmsException("日期不能为空");
        }
        if (StringUtils.isBlank(entity.getType())) {
            throw new BmsException("类型不能为空");
        }
        BmsCalendar holidays = this.getByDate(entity.getDate(), entity.getOrgId());
        if (holidays != null && !holidays.getId().equals(entity.getId())) {
            throw new BmsException("日期已存在");
        }
    }

    @Transactional
    public void saveEntity(BmsCalendarEntity entity) {
        this.saveValidator(entity);

        super.save(entity);
    }

    @Transactional
    public void removeEntity(String id) {
        super.delete(super.get(id));
    }

    @Transactional
    public void importFile(BmsCalendarTemplate data) {
        if (data.getDate() == null) {
            throw new BmsException("日期不能为空");
        }
        if (StringUtils.isBlank(data.getType())) {
            throw new BmsException("类型不能为空");
        }
        if (StringUtils.isBlank(data.getOrgCode())) {
            throw new BmsException("机构编码不能为空");
        }
        Office office = officeService.getByCode(data.getOrgCode());
        if (office == null) {
            throw new BmsException("机构不存在");
        }
        String type = DictUtils.getDictValue(data.getType(), "BMS_CALENDAR_TYPE", null);
        if (StringUtils.isBlank(type)) {
            throw new BmsException("无效的类型");
        }
        BmsCalendarEntity entity = new BmsCalendarEntity();
        BeanUtils.copyProperties(data, entity);

        entity.setType(type);
        entity.setOrgId(office.getId());
        this.saveEntity(entity);
    }
}
