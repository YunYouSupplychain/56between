package com.yunyou.modules.interfaces.interactive.action;

import com.yunyou.common.ResultMessage;
import com.yunyou.core.action.BaseAction;
import com.yunyou.modules.interfaces.interactive.entity.InteractionConstants;
import com.yunyou.modules.interfaces.interactive.service.SyncSettlementDataService;
import com.yunyou.modules.sys.common.entity.SysCommonDataSet;
import com.yunyou.modules.sys.common.entity.SysCommonSku;
import com.yunyou.modules.sys.common.service.SysCommonDataSetService;
import com.yunyou.modules.sys.common.service.SysCommonSkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SyncSettlementDataAction extends BaseAction {
    @Autowired
    private SyncSettlementDataService syncSettlementDataService;
    @Autowired
    private SysCommonDataSetService sysCommonDataSetService;
    @Autowired
    private SysCommonSkuService sysCommonSkuService;

    public void synchroSkuAutoDate() {
        List<SysCommonDataSet> dataSetList = sysCommonDataSetService.findList(new SysCommonDataSet());
        for (SysCommonDataSet dataSet : dataSetList) {
            try {
                SysCommonSku con = new SysCommonSku();
                con.setDataSet(dataSet.getCode());
                List<SysCommonSku> syncList = sysCommonSkuService.findSync(con);
                ResultMessage msg;
                for (int i = 0; i < syncList.size(); i += 999) {
                    if (syncList.size() - i < 999) {
                        msg = syncSettlementDataService.synchroSku(syncList.subList(i, syncList.size()), InteractionConstants.SYN_USER);
                    } else {
                        msg = syncSettlementDataService.synchroSku(syncList.subList(i, i + 999), InteractionConstants.SYN_USER);
                    }
                    if (!msg.isSuccess()) {
                        logger.error("同步数据套[ " + dataSet.getCode() + " ]结算商品失败，" + msg.getMessage());
                    }
                }
            } catch (Exception e) {
                logger.error("同步数据套[ " + dataSet.getCode() + " ]结算商品失败，" + e.getMessage());
            }
        }
    }
}
