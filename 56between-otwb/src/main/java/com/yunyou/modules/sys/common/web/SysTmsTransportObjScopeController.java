package com.yunyou.modules.sys.common.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataToTmsAction;
import com.yunyou.modules.sys.common.entity.SysTmsCarrierRouteRelation;
import com.yunyou.modules.sys.common.entity.SysTmsTransportObjScope;
import com.yunyou.modules.sys.common.entity.extend.SysTmsTransportObjScopeEntity;
import com.yunyou.modules.sys.common.service.SysTmsCarrierRouteRelationService;
import com.yunyou.modules.sys.common.service.SysTmsTransportObjScopeService;
import com.yunyou.modules.sys.common.service.SysTmsTransportScopeService;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务对象服务范围Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/transportObjScope")
public class SysTmsTransportObjScopeController extends BaseController {
    @Autowired
    private SysTmsTransportObjScopeService sysTmsTransportObjScopeService;
    @Autowired
    private SysTmsTransportScopeService sysTmsTransportScopeService;
    @Autowired
    private SysTmsCarrierRouteRelationService sysTmsCarrierRouteRelationService;
    @Autowired
    private SyncPlatformDataToTmsAction syncPlatformDataToTmsAction;

    @ModelAttribute
    public SysTmsTransportObjScopeEntity get(@RequestParam(required = false) String id) {
        SysTmsTransportObjScopeEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsTransportObjScopeService.getEntity(id);
        }
        if (entity == null) {
            entity = new SysTmsTransportObjScopeEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:tms:transportObjScope:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysTmsTransportObjScopeList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportObjScope:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysTmsTransportObjScopeEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsTransportObjScopeService.findPage(new Page<SysTmsTransportObjScopeEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:tms:transportObjScope:view", "sys:common:tms:transportObjScope:add", "sys:common:tms:transportObjScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysTmsTransportObjScopeEntity sysTmsTransportObjScopeEntity, Model model) {
        model.addAttribute("sysTmsTransportObjScopeEntity", sysTmsTransportObjScopeEntity);
        return "modules/sys/common/sysTmsTransportObjScopeForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:transportObjScope:add", "sys:common:tms:transportObjScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsTransportObjScope sysTmsTransportObjScope, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsTransportObjScope)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsTransportObjScopeService.saveValidator(sysTmsTransportObjScope);
            sysTmsTransportObjScopeService.save(sysTmsTransportObjScope);
            j.put("entity", sysTmsTransportObjScope);
        } catch (GlobalException e) {
            if (logger.isInfoEnabled()) {
                logger.info(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (DuplicateKeyException e) {
            if (logger.isWarnEnabled()) {
                logger.warn(null, e);
            }
            j.setSuccess(false);
            j.setMsg("数据已存在");
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error(null, e);
            }
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportObjScope:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            SysTmsTransportObjScope sysTmsTransportObjScope = sysTmsTransportObjScopeService.get(id);
            try {
                sysTmsTransportObjScopeService.delete(sysTmsTransportObjScope);
            } catch (Exception e) {
                logger.error("删除id=[" + id + "]", e);
                errMsg.append("<br>").append("[").append(sysTmsTransportObjScope.getTransportScopeCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportObjScope:sync")
    @RequestMapping(value = "syncSelect", method = RequestMethod.POST)
    public AjaxJson syncSelect(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                syncPlatformDataToTmsAction.sync(sysTmsTransportObjScopeService.get(id));
            }
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportObjScope:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll(SysTmsTransportObjScope entity) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(entity.getDataSet())) {
                entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
            }
            sysTmsTransportObjScopeService.findList(entity).forEach(syncPlatformDataToTmsAction::sync);
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 生成承运商路由
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:transportObjScope:genCarrierRoute")
    @RequestMapping(value = "genCarrierRoute")
    public AjaxJson genCarrierRoute(String dataSet) {
        AjaxJson j = new AjaxJson();
        try {
            if (StringUtils.isBlank(dataSet)) {
                dataSet = SysDataSetUtils.getUserDataSet().getCode();
            }
            List<SysTmsTransportObjScope> list = sysTmsTransportObjScopeService.findCarrierScope(null, TmsConstants.TRANSPORT_SCOPE_TYPE_1, dataSet);
            // 按承运商分组构建
            Map<String, List<SysTmsTransportObjScope>> map = list.stream().filter(o -> StringUtils.isNotBlank(o.getTransportObjCode())).collect(Collectors.groupingBy(SysTmsTransportObjScope::getTransportObjCode));
            for (Map.Entry<String, List<SysTmsTransportObjScope>> entry : map.entrySet()) {
                // 起始地业务范围
                List<SysTmsTransportObjScope> originList = entry.getValue();
                // 目的地业务范围
                List<SysTmsTransportObjScope> destinationList = sysTmsTransportObjScopeService.findCarrierScope(entry.getKey(), TmsConstants.TRANSPORT_SCOPE_TYPE_2, dataSet);
                if (CollectionUtil.isEmpty(destinationList)) continue;

                List<Area> originAreaList = Lists.newArrayList(), destinationAreaList = Lists.newArrayList();
                // 起始地
                List<String> originScopeList = originList.stream().map(SysTmsTransportObjScope::getTransportScopeCode).distinct().collect(Collectors.toList());
                for (String code : originScopeList) {
                    originAreaList.addAll(sysTmsTransportScopeService.findAreaByScopeCode(code, dataSet));
                }
                // 目的地
                List<String> destinationScopeList = destinationList.stream().map(SysTmsTransportObjScope::getTransportScopeCode).distinct().collect(Collectors.toList());
                for (String code : destinationScopeList) {
                    destinationAreaList.addAll(sysTmsTransportScopeService.findAreaByScopeCode(code, dataSet));
                }
                // 构建承运商路由
                for (Area originArea : originAreaList) {
                    for (Area destinationArea : destinationAreaList) {
                        SysTmsCarrierRouteRelation sysTmsCarrierRouteRelation = new SysTmsCarrierRouteRelation();
                        sysTmsCarrierRouteRelation.setId("");
                        sysTmsCarrierRouteRelation.setCode(originArea.getCode() + "-" + destinationArea.getCode());
                        sysTmsCarrierRouteRelation.setName(originArea.getName() + "-" + destinationArea.getName());
                        sysTmsCarrierRouteRelation.setCarrierCode(entry.getKey());
                        sysTmsCarrierRouteRelation.setOriginId(originArea.getId());
                        sysTmsCarrierRouteRelation.setDestinationId(destinationArea.getId());
                        sysTmsCarrierRouteRelation.setDataSet(dataSet);
                        try {
                            sysTmsCarrierRouteRelationService.saveValidator(sysTmsCarrierRouteRelation);
                            sysTmsCarrierRouteRelationService.save(sysTmsCarrierRouteRelation);
                        } catch (TmsException e) {
                            logger.debug("生成承运商路由：" + e.getMessage() + "," + JSON.toJSONString(sysTmsCarrierRouteRelation));
                        }
                    }
                }
            }
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
}