package com.yunyou.modules.interfaces.interactive.service;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.utils.IdGen;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.BaseService;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.common.entity.SysCommonSku;
import com.yunyou.modules.sys.common.entity.SysCommonSkuBarcode;
import com.yunyou.modules.sys.common.entity.SysCommonSkuSupplier;
import com.yunyou.modules.sys.common.service.SysCommonSkuService;
import com.yunyou.modules.sys.entity.User;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 同步结算商品Service
 */
@Service
public class SyncSettlementDataService extends BaseService {
    @Autowired
    private SysCommonSkuService sysCommonSkuService;
    @Autowired
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    @Transactional
    public ResultMessage synchroSku(List<SysCommonSku> list, User user) {
        ResultMessage msg = new ResultMessage();
        if (CollectionUtil.isEmpty(list)) {
            msg.setSuccess(false);
            msg.setMessage("未找到可更新的数据");
            return msg;
        }
        List<SysCommonSku> skuList = Lists.newArrayList();
        List<String> checkSupplierList = Lists.newArrayList();
        for (SysCommonSku sysCommonSku : list) {
            List<SysCommonSkuSupplier> skuSupplierList = sysCommonSku.getSkuSupplierList();
            if (CollectionUtil.isEmpty(skuSupplierList)) {
                continue;
            }
            for (SysCommonSkuSupplier skuSupplier : skuSupplierList) {
                String checkDouble = skuSupplier.getSupplierCode() + "-" + sysCommonSku.getSkuCode() + "-" + sysCommonSku.getDataSet();
                if (!checkSupplierList.contains(checkDouble)) {
                    checkSupplierList.add(checkDouble);
                }
                SysCommonSku supplierSku = sysCommonSkuService.getByCode(skuSupplier.getSupplierCode(), sysCommonSku.getSkuCode(), sysCommonSku.getDataSet());
                if (supplierSku == null) {
                    supplierSku = new SysCommonSku();
                    BeanUtils.copyProperties(sysCommonSku, supplierSku);
                    supplierSku.setId(IdGen.uuid());
                    supplierSku.setCreateBy(user);
                    supplierSku.setCreateDate(new Date());
                    supplierSku.setUpdateBy(user);
                    supplierSku.setUpdateDate(new Date());
                    supplierSku.setFilingTime(new Date());
                    supplierSku.setOwnerCode(skuSupplier.getSupplierCode());
                    supplierSku.setSkuSupplierList(Lists.newArrayList());
                    String headerId = supplierSku.getId();
                    List<SysCommonSkuBarcode> skuBarcodeList = sysCommonSku.getSkuBarcodeList().stream().map(t -> {
                        SysCommonSkuBarcode barcode = new SysCommonSkuBarcode();
                        BeanUtils.copyProperties(t, barcode);
                        barcode.setId(IdGen.uuid());
                        barcode.setHeaderId(headerId);
                        barcode.setOwnerCode(skuSupplier.getSupplierCode());
                        return barcode;
                    }).collect(Collectors.toList());

                    supplierSku.setSkuBarcodeList(skuBarcodeList);
                    sysCommonSkuService.setSkuDefault(supplierSku);
                    skuList.add(supplierSku);
                }
            }
        }
        if (CollectionUtil.isNotEmpty(skuList)) {
            sysCommonSkuService.batchInsert(skuList);
            syncPlatformDataCommonAction.syncInsert(skuList.toArray(new SysCommonSku[0]));
        }
        msg.setSuccess(true);
        msg.setMessage("同步成功");
        return msg;
    }
}
