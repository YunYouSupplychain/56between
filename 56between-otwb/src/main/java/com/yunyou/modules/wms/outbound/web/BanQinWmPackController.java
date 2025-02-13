package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.GenNoType;
import com.yunyou.modules.sys.service.SynchronizedNoService;
import com.yunyou.modules.wms.common.entity.WmsConstants;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPackEntity;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoAllocEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundSoService;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedHashMap;

/**
 * 打包Controller
 *
 * @author WMJ
 * @version 2019-09-18
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmPack")
public class BanQinWmPackController extends BaseController {
    @Autowired
    private BanQinOutboundSoService outboundSoService;
    @Autowired
    protected SynchronizedNoService noService;

    /**
     * 打包列表页面
     */
    @RequiresPermissions("outbound:banQinWmPack:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmPackList";
    }

    @RequestMapping(value = {"newList"})
    public String newList() {
        return "modules/wms/outbound/banQinWmPackNewList";
    }

    /**
     * 打包列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public AjaxJson data(BanQinWmSoAllocEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.getPackByCondition(entity);
            if (msg.isSuccess()) {
                Page<BanQinWmSoAllocEntity> page = new Page<>();
                page.setList(((BanQinWmPackEntity) msg.getData()).getAllocItems());
                j.put("entity", getBootstrapData(page));
            } else {
                j.setSuccess(false);
                j.setMsg(msg.getMessage());
            }
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getTrackingNoByToId")
    public AjaxJson getTrackingNoByToId(BanQinWmSoAllocEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            String result = outboundSoService.getTrackingNoByToId(entity.getToId(), entity.getOrgId());
            LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
            map.put("entity", result);
            j.setBody(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getPackageRelationAndQtyUom")
    public AjaxJson getPackageRelationAndQtyUom(BanQinWmSoAllocEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.getPackageRelationAndQtyUom(entity.getPackCode(), entity.getUom(), entity.getOrgId());
            LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
            map.put("entity", msg.getData());
            j.setBody(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getCdWhPackageRelation")
    public AjaxJson getCdWhPackageRelation(BanQinWmSoAllocEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.getCdWhPackageRelation(entity.getPackCode(), entity.getUomQty(), entity.getQtyEa(), entity.getOrgId());
            LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
            map.put("entity", msg.getData());
            j.setBody(map);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "updateTraceId")
    public AjaxJson updateTraceId(@RequestBody BanQinWmPackEntity entity) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.updateTraceId(entity.getAllocItems(), entity.getToId(), entity.getSoSerialList(), entity.getSoTrackingNo(), entity.getIsCheck(), entity.getIsPrintContainer(), entity.getIsPrintLabel(), entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
            LinkedHashMap<String, Object> map = Maps.newLinkedHashMap();
            map.put("imageList", msg.getData());
            j.setBody(map);
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "cartonNoGen")
    public AjaxJson cartonNoGen() {
        AjaxJson j = new AjaxJson();
        j.put("entity", noService.getDocumentNo(GenNoType.WM_TRACE_ID.name()));
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "cancelPack")
    public AjaxJson cancelPack(@RequestBody BanQinWmSoAllocEntity entity) {
        AjaxJson j = new AjaxJson();

        try {
            ResultMessage msg = outboundSoService.cancelPack(entity.getAllocIds(), WmsConstants.YES, entity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

}