package com.yunyou.modules.wms.inventory.service;

import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.BanQinInventorySerialEntity;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.common.entity.WmsCodeMaster;
import com.yunyou.modules.wms.inventory.entity.BanQinWmActTranSerial;
import com.yunyou.modules.wms.inventory.entity.BanQinWmInvSerial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 序列号库存更新和写序列号库存交易
 * @author WMJ
 * @version 2019-01-28
 */
@Service
public class BanQinInventorySerialService {
	@Autowired
	BanQinWmInvSerialService wmInvSerialService;
	@Autowired
	BanQinWmActTranSerialService wmActTranSerialService;
	@Autowired
	SynchronizedNoService noService;

    /**
     * 序列号库存更新
     * @param entity
     * @throws WarehouseException
     */
    @Transactional
	public void updateInventorySerial(BanQinInventorySerialEntity entity) throws WarehouseException {
		// 获取序列号库存
		BanQinWmInvSerial wmInvSerialModel = wmInvSerialService.getByOwnerCodeAndSkuCodeAndSerialNo(entity.getOwnerCode(), entity.getSkuCode(), entity.getSerialNo(), entity.getOrgId());
		// 当序列号为收货、调增、转入、取消时
		if (WmsCodeMaster.RCV.getCode().equals(entity.getSerialTranType()) || WmsCodeMaster.AIN.getCode().equals(entity.getSerialTranType())
				|| WmsCodeMaster.TIN.getCode().equals(entity.getSerialTranType()) || WmsCodeMaster.CSP.getCode().equals(entity.getSerialTranType())) {
			// 序列号已经存在
			if (wmInvSerialModel != null) {
				throw new WarehouseException("[" + entity.getSerialNo() + "]" + "序列号已经存在");
			}
			// 保存序列号库存
			BanQinWmInvSerial model = new BanQinWmInvSerial();
			model.setOwnerCode(entity.getOwnerCode());
			model.setSkuCode(entity.getSkuCode());
			model.setLotNum(entity.getLotNum());
			model.setSerialNo(entity.getSerialNo());
			model.setOrgId(entity.getOrgId());
			wmInvSerialService.save(model);
			// 写序列号库存交易
			addActTranSerial(entity);
		} else {
			// 序列号不存在
			if (wmInvSerialModel == null) {
				throw new WarehouseException("[" + entity.getSerialNo() + "]" + "序列号不存在");
			}
			// 删除序列号库存
            wmInvSerialService.delete(wmInvSerialModel);
			// 写序列号库存交易
			addActTranSerial(entity);
		}
	}

	@Transactional
	public void addActTranSerial(BanQinInventorySerialEntity entity) {
		BanQinWmActTranSerial wmActTranSerialModel = new BanQinWmActTranSerial();
		wmActTranSerialModel.setLineNo(entity.getLineNo());
		wmActTranSerialModel.setLotNum(entity.getLotNum());
		wmActTranSerialModel.setOrderNo(entity.getOrderNo());
		wmActTranSerialModel.setOrderType(entity.getOrderType());
		wmActTranSerialModel.setOwnerCode(entity.getOwnerCode());
		wmActTranSerialModel.setSkuCode(entity.getSkuCode());
		wmActTranSerialModel.setSerialNo(entity.getSerialNo());
		wmActTranSerialModel.setSerialTranType(entity.getSerialTranType());
		wmActTranSerialModel.setSerialTranId(noService.getDocumentNo(GenNoType.WM_SERIAL_TRAN_ID.name()));
		wmActTranSerialModel.setTranId(entity.getTranId());
		wmActTranSerialModel.setTranOp(UserUtils.getUser().getName());
		wmActTranSerialModel.setTranTime(new Date());
        wmActTranSerialModel.setOrgId(entity.getOrgId());
        wmActTranSerialService.save(wmActTranSerialModel);
	}
}