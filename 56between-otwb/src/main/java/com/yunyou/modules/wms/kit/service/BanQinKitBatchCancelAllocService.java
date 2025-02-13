package com.yunyou.modules.wms.kit.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.kit.entity.extend.BanQinWmTaskKitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 描述：加工管理-子件取消分配- 批量
 * <p>
 * create by Jianhua on 2019/8/25
 */
@Service
public class BanQinKitBatchCancelAllocService {
    @Autowired
    protected BanQinWmTaskKitService banQinWmTaskKitService;
    @Autowired
    protected BanQinKitCancelAllocService banQinKitCancelAllocService;

    /**
     * 描述：批量取消分配
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage kitBatchCancelAlloc(String processByCode, String kitNo, List<Object> noList, String orgId) {
        ResultMessage msg = new ResultMessage();
        // 2、获取加工任务,完全分配状态
        List<BanQinWmTaskKitEntity> taskKitEntitys = banQinWmTaskKitService.getEntityByProcess(processByCode, kitNo, noList, WmsCodeMaster.SUB_KIT_FULL_ALLOC.getCode(), orgId);

        // 按任务取消分配
        ResultMessage message = kitBatchCancelAlloc(taskKitEntitys);
        // 消息处理
        if (!StringUtils.isBlank(message.getMessage())) {
            msg.addMessage(message.getMessage());
        }
        if (StringUtils.isBlank(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }
        return msg;
    }

    /**
     * 描述：批量取消分配
     * <p>
     * create by Jianhua on 2019/8/25
     */
    public ResultMessage kitBatchCancelAlloc(List<BanQinWmTaskKitEntity> entitys) {
        ResultMessage msg = new ResultMessage();
        for (BanQinWmTaskKitEntity entity : entitys) {
            try {
                banQinKitCancelAllocService.kitCancelAlloc(entity);
            } catch (WarehouseException e) {
                msg.addMessage(e.getMessage());
                msg.setSuccess(false);
            }
        }
        // 消息处理
        if (StringUtils.isBlank(msg.getMessage())) {
            msg.addMessage("操作成功");
            msg.setSuccess(true);
        }
        return msg;
    }
}
