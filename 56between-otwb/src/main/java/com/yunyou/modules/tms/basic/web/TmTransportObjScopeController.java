package com.yunyou.modules.tms.basic.web;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.tms.basic.entity.TmCarrierRouteRelation;
import com.yunyou.modules.tms.basic.entity.TmTransportObjScope;
import com.yunyou.modules.tms.basic.entity.extend.TmTransportObjScopeEntity;
import com.yunyou.modules.tms.basic.service.TmCarrierRouteRelationService;
import com.yunyou.modules.tms.basic.service.TmTransportObjScopeService;
import com.yunyou.modules.tms.basic.service.TmTransportScopeService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务对象服务范围Controller
 *
 * @author liujianhua
 * @version 2020-02-20
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmTransportObjScope")
public class TmTransportObjScopeController extends BaseController {

    @Autowired
    private TmTransportObjScopeService tmTransportObjScopeService;
    @Autowired
    private TmTransportScopeService tmTransportScopeService;
    @Autowired
    private TmCarrierRouteRelationService tmCarrierRouteRelationService;

    @ModelAttribute
    public TmTransportObjScopeEntity get(@RequestParam(required = false) String id) {
        TmTransportObjScopeEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmTransportObjScopeService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmTransportObjScopeEntity();
        }
        return entity;
    }

    /**
     * 业务对象服务范围列表页面
     */
    @RequiresPermissions("basic:tmTransportObjScope:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmTransportObjScopeList";
    }

    /**
     * 业务对象服务范围列表数据
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportObjScope:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmTransportObjScope tmTransportObjScope, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<TmTransportObjScopeEntity> page = tmTransportObjScopeService.findPage(new Page<TmTransportObjScopeEntity>(request, response), tmTransportObjScope);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑业务对象服务范围表单页面
     */
    @RequiresPermissions(value = {"basic:tmTransportObjScope:view", "basic:tmTransportObjScope:add", "basic:tmTransportObjScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmTransportObjScopeEntity tmTransportObjScopeEntity, Model model) {
        model.addAttribute("tmTransportObjScopeEntity", tmTransportObjScopeEntity);
        return "modules/tms/basic/tmTransportObjScopeForm";
    }

    /**
     * 保存业务对象服务范围
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmTransportObjScope:add", "basic:tmTransportObjScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmTransportObjScope tmTransportObjScope, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmTransportObjScope)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmTransportObjScopeService.saveValidator(tmTransportObjScope);
            tmTransportObjScopeService.save(tmTransportObjScope);
            j.put("entity", tmTransportObjScope);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除业务对象服务范围
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportObjScope:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmTransportObjScope tmTransportObjScope, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            tmTransportObjScopeService.delete(tmTransportObjScope);
        } catch (Exception e) {
            logger.error("删除业务对象服务范围id=[" + tmTransportObjScope.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除业务对象服务范围
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportObjScope:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            TmTransportObjScope tmTransportObjScope = tmTransportObjScopeService.get(id);
            try {
                tmTransportObjScopeService.delete(tmTransportObjScope);
            } catch (Exception e) {
                logger.error("删除业务对象服务范围id=[" + id + "]", e);
                errMsg.append("<br>").append("业务对象服务范围[").append(tmTransportObjScope.getTransportScopeCode()).append("]删除失败");
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg("操作成功，其中" + errMsg.toString());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportObjScope:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(TmTransportObjScope tmTransportObjScope, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "业务对象服务范围" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<TmTransportObjScopeEntity> page = tmTransportObjScopeService.findPage(new Page<TmTransportObjScopeEntity>(request, response, -1), tmTransportObjScope);
            new ExportExcel("业务对象服务范围", TmTransportObjScope.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出业务对象服务范围记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("basic:tmTransportObjScope:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<TmTransportObjScope> list = ei.getDataList(TmTransportObjScope.class);
            for (TmTransportObjScope tmTransportObjScope : list) {
                try {
                    tmTransportObjScopeService.save(tmTransportObjScope);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条业务对象服务范围记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条业务对象服务范围记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入业务对象服务范围失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmTransportObjScope/?repage";
    }

    /**
     * 下载导入业务对象服务范围数据模板
     */
    @RequiresPermissions("basic:tmTransportObjScope:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "业务对象服务范围数据导入模板.xlsx";
            List<TmTransportObjScope> list = Lists.newArrayList();
            new ExportExcel("业务对象服务范围数据", TmTransportObjScope.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/tms/basic/tmTransportObjScope/?repage";
    }

    /**
     * 生成承运商路由
     */
    @ResponseBody
    @RequiresPermissions("basic:tmTransportObjScope:genCarrierRoute")
    @RequestMapping(value = "genCarrierRoute")
    public AjaxJson genCarrierRoute(String orgId) {
        AjaxJson j = new AjaxJson();
        try {
            List<TmTransportObjScope> list = tmTransportObjScopeService.findCarrierScope(null, TmsConstants.TRANSPORT_SCOPE_TYPE_1, orgId);
            // 按承运商分组构建
            Map<String, List<TmTransportObjScope>> map = list.stream().filter(o -> StringUtils.isNotBlank(o.getTransportObjCode())).collect(Collectors.groupingBy(TmTransportObjScope::getTransportObjCode));
            for (Map.Entry<String, List<TmTransportObjScope>> entry : map.entrySet()) {
                // 起始地业务范围
                List<TmTransportObjScope> originList = entry.getValue();
                // 目的地业务范围
                List<TmTransportObjScope> destinationList = tmTransportObjScopeService.findCarrierScope(entry.getKey(), TmsConstants.TRANSPORT_SCOPE_TYPE_2, orgId);
                if (CollectionUtil.isEmpty(destinationList)) continue;

                List<Area> originAreaList = Lists.newArrayList(), destinationAreaList = Lists.newArrayList();
                // 起始地
                List<String> originScopeList = originList.stream().map(TmTransportObjScope::getTransportScopeCode).distinct().collect(Collectors.toList());
                for (String code : originScopeList) {
                    originAreaList.addAll(tmTransportScopeService.findAreaByScopeCode(code, orgId));
                }
                // 目的地
                List<String> destinationScopeList = destinationList.stream().map(TmTransportObjScope::getTransportScopeCode).distinct().collect(Collectors.toList());
                for (String code : destinationScopeList) {
                    destinationAreaList.addAll(tmTransportScopeService.findAreaByScopeCode(code, orgId));
                }
                // 构建承运商路由
                for (Area originArea : originAreaList) {
                    for (Area destinationArea : destinationAreaList) {
                        TmCarrierRouteRelation tmCarrierRouteRelation = new TmCarrierRouteRelation();
                        tmCarrierRouteRelation.setId("");
                        tmCarrierRouteRelation.setCode(originArea.getCode() + "-" + destinationArea.getCode());
                        tmCarrierRouteRelation.setName(originArea.getName() + "-" + destinationArea.getName());
                        tmCarrierRouteRelation.setCarrierCode(entry.getKey());
                        tmCarrierRouteRelation.setOriginId(originArea.getId());
                        tmCarrierRouteRelation.setDestinationId(destinationArea.getId());
                        tmCarrierRouteRelation.setOrgId(orgId);
                        try {
                            tmCarrierRouteRelationService.saveValidator(tmCarrierRouteRelation);
                            tmCarrierRouteRelationService.save(tmCarrierRouteRelation);
                        } catch (TmsException e) {
                            logger.debug("生成承运商路由：" + e.getMessage() + "," + JSON.toJSONString(tmCarrierRouteRelation));
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