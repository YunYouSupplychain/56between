package com.yunyou.modules.tms.basic.service;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.service.CrudService;
import com.yunyou.modules.sys.entity.Office;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmItemBarcode;
import com.yunyou.modules.tms.basic.mapper.TmItemBarcodeMapper;
import com.yunyou.modules.tms.common.TmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商品条码信息Service
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Service
@Transactional(readOnly = true)
public class TmItemBarcodeService extends CrudService<TmItemBarcodeMapper, TmItemBarcode> {

    @Transactional
    public void remove(String ownerCode, String skuCode, String orgId) {
        mapper.remove(ownerCode, skuCode, orgId);
    }

    public void saveValidator(TmItemBarcode tmItemBarcode) {
        if (StringUtils.isBlank(tmItemBarcode.getOwnerCode())) {
            throw new TmsException("货主编码不能为空");
        }
        if (StringUtils.isBlank(tmItemBarcode.getSkuCode())) {
            throw new TmsException("商品编码不能为空");
        }
        if (StringUtils.isBlank(tmItemBarcode.getBarcode())) {
            throw new TmsException("条码不能为空");
        }
        Office organizationCenter = UserUtils.getOrgCenter(tmItemBarcode.getOrgId());
        if (StringUtils.isBlank(organizationCenter.getId())) {
            throw new TmsException("找不到归属组织中心，不能操作");
        }
        List<TmItemBarcode> list = findList(new TmItemBarcode(tmItemBarcode.getOwnerCode(), tmItemBarcode.getSkuCode(), tmItemBarcode.getBarcode(), tmItemBarcode.getOrgId()));
        if (CollectionUtil.isNotEmpty(list)) {
            if (list.stream().anyMatch(o -> !o.getId().equals(tmItemBarcode.getId()))) {
                throw new TmsException("条码[" + tmItemBarcode.getBarcode() + "]已存在");
            }
        }
        List<TmItemBarcode> list1 = findList(new TmItemBarcode(tmItemBarcode.getOwnerCode(), tmItemBarcode.getSkuCode(), tmItemBarcode.getOrgId()));
        if (CollectionUtil.isNotEmpty(list1)) {
            if (list1.stream().anyMatch(o -> !o.getId().equals(tmItemBarcode.getId()) && "Y".equals(tmItemBarcode.getIsDefault()))) {
                throw new TmsException("货主[" + tmItemBarcode.getOwnerCode() + "]商品[" + tmItemBarcode.getSkuCode() + "]已存在默认条码");
            }
        }

    }
}