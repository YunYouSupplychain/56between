package com.yunyou.modules.wms.inbound.service;

import com.google.common.collect.Sets;
import com.yunyou.common.ResultMessage;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.wms.inbound.entity.BanQinWmAsnDetailReceives;
import com.yunyou.modules.wms.inbound.mapper.BanQinWmAsnDetailReceivesMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 收货箱明细Service
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Service
@Transactional(readOnly = true)
public class BanQinWmAsnDetailReceivesService extends CrudService<BanQinWmAsnDetailReceivesMapper, BanQinWmAsnDetailReceives> {

    public BanQinWmAsnDetailReceives get(String id) {
        return super.get(id);
    }

    public List<BanQinWmAsnDetailReceives> findList(BanQinWmAsnDetailReceives banQinWmAsnDetailReceives) {
        return super.findList(banQinWmAsnDetailReceives);
    }

    public Page<BanQinWmAsnDetailReceives> findPage(Page<BanQinWmAsnDetailReceives> page, BanQinWmAsnDetailReceives banQinWmAsnDetailReceives) {
        return super.findPage(page, banQinWmAsnDetailReceives);
    }

    @Transactional
    public void save(BanQinWmAsnDetailReceives banQinWmAsnDetailReceives) {
        super.save(banQinWmAsnDetailReceives);
    }

    @Transactional
    public void delete(BanQinWmAsnDetailReceives banQinWmAsnDetailReceives) {
        super.delete(banQinWmAsnDetailReceives);
    }

    public ResultMessage saveTabAll(List<BanQinWmAsnDetailReceives> models) {
        ResultMessage msg = new ResultMessage();
        // 校验是否存在重复的cartonNo
        List<String> cartonNos = models.stream().map(BanQinWmAsnDetailReceives::getCartonNo).collect(Collectors.toList());
        Set<String> set = Sets.newHashSet();
        boolean flag = true;
        for (String carton : cartonNos) {
            if (!set.add(carton)) {
                flag = false;
            }
        }

        if (flag) {
            for (BanQinWmAsnDetailReceives wmAsnDetailReceives : models) {
                this.save(wmAsnDetailReceives);
            }
            msg.setSuccess(true);
            msg.addMessage("保存成功");
        } else {
            msg.setSuccess(false);
            msg.addMessage("箱号不能重复");
        }

        return msg;
    }

    /**
     * 描述： 根据收货明细ID删除
     *
     * @param receiveIds
     * @author Jianhua on 2019/1/26
     */
    @Transactional
    public void removeByReceiveId(List<String> receiveIds) {
        mapper.removeByReceiveId(receiveIds);
    }
}