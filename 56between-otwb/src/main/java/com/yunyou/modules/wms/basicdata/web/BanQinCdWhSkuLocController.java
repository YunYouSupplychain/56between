package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhSkuLocEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhSkuLocService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 商品拣货位Controller
 *
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhSkuLoc")
public class BanQinCdWhSkuLocController extends BaseController {
    @Autowired
    private BanQinCdWhSkuLocService banQinCdWhSkuLocService;

    /**
     * 商品拣货位列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinCdWhSkuLocEntity banQinCdWhSkuLocEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(banQinCdWhSkuLocService.findPage(new Page<BanQinCdWhSkuLocEntity>(request, response), banQinCdWhSkuLocEntity));
    }

    /**
     * 保存商品拣货位
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhSku:saveDetail")
    @RequestMapping(value = "save")
    public AjaxJson save(BanQinCdWhSkuLocEntity banQinCdWhSkuLocEntity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinCdWhSkuLocEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据！");
            return j;
        }
        try {
            banQinCdWhSkuLocService.saveEntity(banQinCdWhSkuLocEntity);
            j.setMsg("保存成功");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除商品拣货位
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdWhSku:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            banQinCdWhSkuLocService.delete(banQinCdWhSkuLocService.get(id));
        }
        j.setMsg("删除商品拣货位成功");
        return j;
    }
}