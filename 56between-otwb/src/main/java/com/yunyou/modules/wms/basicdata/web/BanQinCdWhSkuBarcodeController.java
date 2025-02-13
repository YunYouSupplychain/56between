package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuBarcode;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuBarcodeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 商品条码Controller
 *
 * @author WMJ
 * @version 2019-10-28
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhSkuBarcode")
public class BanQinCdWhSkuBarcodeController extends BaseController {
    @Autowired
    private BanQinCdWhSkuBarcodeService banQinCdWhSkuBarcodeService;

    /**
     * 商品条码列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public List<BanQinCdWhSkuBarcode> data(BanQinCdWhSkuBarcode banQinCdWhSkuBarcode) {
        return banQinCdWhSkuBarcodeService.findList(banQinCdWhSkuBarcode);
    }

    /**
     * 保存商品条码
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhSkuBarcode:saveBarcode")
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhSkuEntity skuEntity) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            banQinCdWhSkuBarcodeService.saveEntity(skuEntity.getBarcodeList());
            j.setMsg("保存成功");
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("条码重复！");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }


    /**
     * 批量删除商品条码
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhSkuBarcode:removeBarcode")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            banQinCdWhSkuBarcodeService.delete(new BanQinCdWhSkuBarcode(id));
        }
        j.setMsg("删除商品条码成功");
        return j;
    }
}