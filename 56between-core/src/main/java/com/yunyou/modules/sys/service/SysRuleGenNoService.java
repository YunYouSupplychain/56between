package com.yunyou.modules.sys.service;

import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.time.DateUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.SysRuleGenNo;
import com.yunyou.modules.sys.mapper.SysRuleGenNoMapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 编号生成规则Service
 *
 * @author 张庆生
 * @version 2017-10-17
 */
@Service
@Transactional(readOnly = true)
public class SysRuleGenNoService extends CrudService<SysRuleGenNoMapper, SysRuleGenNo> {

    /**
     * 规则清空周期 - 天
     */
    private final String CLEAR_CYCLE_DAY = "DAY";

    private String getSerialNo(int length, long serialNo) {
        return String.format("%0" + length + "d", serialNo);
    }

    /**
     * 获取最新编号
     *
     * @param type 编号类型
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String getDocumentNo(String type) {
        SysRuleGenNo noGenRule = mapper.getByType(type);
        String prefix = noGenRule.getPrefix();
        String stamp = noGenRule.getStamp();
        String clearCycle = noGenRule.getClearCycle();
        Date lastUpdateDate = noGenRule.getUpdateDate();
        int serialNoMaxLength = noGenRule.getSerialNoMaxLength();
        int currentSerialNo = noGenRule.getCurrentSerialNo();
        String currentStamp = StringUtils.isBlank(stamp) ? "" : DateUtils.getDate(stamp);
        // 按日流水且最后更新日期与当前不是同一天，则重置当前序号
        if (CLEAR_CYCLE_DAY.equals(clearCycle) && !DateUtil.isSameDay(lastUpdateDate, new Date())) {
            currentSerialNo = 1;
        } else {
            currentSerialNo++;
        }
        String no = prefix + currentStamp + this.getSerialNo(serialNoMaxLength, currentSerialNo);
        mapper.updateByType(type, currentSerialNo);
        return no;
    }

    /**
     * 批量获取编号
     *
     * @param type 编号类型
     * @param size 数量
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<String> getDocumentNo(String type, int size) {
        List<String> nos = Lists.newArrayList();

        SysRuleGenNo noGenRule = mapper.getByType(type);
        String prefix = noGenRule.getPrefix();
        String stamp = noGenRule.getStamp();
        String clearCycle = noGenRule.getClearCycle();
        Date lastUpdateDate = noGenRule.getUpdateDate();
        int serialNoMaxLength = noGenRule.getSerialNoMaxLength();
        int currentSerialNo = noGenRule.getCurrentSerialNo();
        String currentStamp = StringUtils.isBlank(stamp) ? "" : DateUtils.getDate(stamp);
        // 按日流水且最后更新日期与当前不是同一天，则重置当前序号
        if (CLEAR_CYCLE_DAY.equals(clearCycle) && !DateUtil.isSameDay(lastUpdateDate, new Date())) {
            currentSerialNo = 0;
        }
        for (int i = 0; i < size; i++) {
            currentSerialNo++;
            nos.add(prefix + currentStamp + this.getSerialNo(serialNoMaxLength, currentSerialNo));
        }
        mapper.updateByType(type, currentSerialNo);
        return nos;
    }

    @Override
    public Page<SysRuleGenNo> findPage(Page<SysRuleGenNo> page, SysRuleGenNo entity) {
        dataRuleFilter(entity);
        entity.setPage(page);
        page.setList(mapper.findPage(entity));
        return page;
    }
}