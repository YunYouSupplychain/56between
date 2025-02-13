package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.inbound.service.BanQinInboundPaOperationService;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskPaEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskPaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 描述：删除上架任务
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitRemoveTaskPaService {
    @Autowired
    protected BanQinInboundPaOperationService banQinInboundPaOperationService;
    @Autowired
    protected BanQinWmTaskPaService banQinWmTaskPaService;

    /**
     * 描述：删除上架任务 删除新建状态的上架任务，扣减推荐库位的上架待入数
     */
    @Transactional
    public ResultMessage kitRemovePutaway(BanQinWmTaskPaEntity wmTaskPaEntity) throws WarehouseException {
        ResultMessage message = new ResultMessage();
        try {
            banQinInboundPaOperationService.removePutaway(wmTaskPaEntity);
            // 如果paId不存在，但在加工结果明细里有存在，则需置空
            banQinWmTaskPaService.updatePaIdByKitRemoveTaskPa(wmTaskPaEntity.getPaId(), wmTaskPaEntity.getOrgId());
        } catch (GlobalException e) {
            message.setSuccess(false);
            message.setMessage(e.getMessage());
        }
        return message;
    }

}