package com.yunyou.modules.wms.rf.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.modules.sys.entity.User;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.service.BanQinInvReplenishmentService;
import com.yunyou.modules.wms.rf.entity.WMSRF_RP_QueryReplenishmentDetail_Request;
import com.yunyou.modules.wms.rf.entity.WMSRF_RP_ReplenishmentDetail_Response;
import com.yunyou.modules.wms.rf.entity.WMSRF_RP_SaveReplenishment_Request;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskRp;
import com.yunyou.modules.wms.task.entity.BanQinWmTaskRpEntity;
import com.yunyou.modules.wms.task.service.BanQinWmTaskRpService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * RF补货
 *
 * @author WMJ
 * @version 2019/07/10
 */
@Service
public class RfReplenishmentService {
    @Autowired
    private RfPackageConfigService rfPackageConfigService;
    @Autowired
    private BanQinInvReplenishmentService invReplenishmentService;
    @Autowired
    private BanQinWmTaskRpService wmTaskRpService;

    public ResultMessage queryReplenishmentDetail(WMSRF_RP_QueryReplenishmentDetail_Request request) {
        ResultMessage msg = new ResultMessage();
        User user = UserUtils.getUser();
        List<BanQinWmTaskRpEntity> items = wmTaskRpService.rfRPGetRpDetailQuery(request.getRpId(), user.getOffice().getId());
        if (CollectionUtil.isNotEmpty(items)) {
            List<WMSRF_RP_ReplenishmentDetail_Response> result = Lists.newArrayList();
            for (BanQinWmTaskRpEntity entity : items) {
                WMSRF_RP_ReplenishmentDetail_Response response = new WMSRF_RP_ReplenishmentDetail_Response();
                BeanUtils.copyProperties(entity, response);
                // 获取包装配置
                response.setPackageConfigs(this.rfPackageConfigService.getPackageConfigs(entity.getPackCode(), entity.getOrgId()));
                result.add(response);
            }
            msg.setData(result);
        } else {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
        }
        return msg;
    }

    @Transactional
    public ResultMessage saveReplenishment(WMSRF_RP_SaveReplenishment_Request request) {
        ResultMessage msg = new ResultMessage();
        // 1、先获取补货原纪录
        BanQinWmTaskRp wmTaskRpModel = wmTaskRpService.get(request.getId());
        if (null == wmTaskRpModel) {
            msg.setSuccess(false);
            msg.setMessage("查询结果为空");
            return msg;
        }
        // 2、判断记录状态
        if (!wmTaskRpModel.getStatus().equals(WmsCodeMaster.TSK_NEW.getCode())) {
            msg.setSuccess(false);
            msg.setMessage("不是新建状态");
            return msg;
        }
        // 3、将RF界面传入的数值设置到wmTaskRpModel 对象
        wmTaskRpModel.setQtyRpEa(request.getQtyRpEa()); // 补货数量
        wmTaskRpModel.setQtyRpUom(request.getQtyRpUom()); // 补货单位数量
        wmTaskRpModel.setUom(request.getUom()); // 补货单位
        // 4、调用后台接口执行补货
        invReplenishmentService.completeRpTask(wmTaskRpModel);
        msg.setSuccess(true);
        msg.setMessage("操作成功");
        return msg;
    }
}
