package com.yunyou.modules.wms.outbound.web;

import com.google.common.collect.Lists;
import com.yunyou.common.ResultMessage;
import com.yunyou.common.config.Global;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.common.entity.ProcessByCode;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetail;
import com.yunyou.modules.wms.outbound.entity.BanQinWmSoDetailEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundDuplicateService;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundSoService;
import com.yunyou.modules.wms.outbound.service.BanQinWmSoDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * 出库单明细Controller
 *
 * @author WMJ
 * @version 2019-02-14
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmSoDetail")
public class BanQinWmSoDetailController extends BaseController {
    @Autowired
    private BanQinWmSoDetailService banQinWmSoDetailService;
    @Autowired
    private BanQinOutboundSoService outboundSoService;
    @Autowired
    private BanQinOutboundDuplicateService outboundDuplicateService;

    /**
     * 出库单明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmSoDetailEntity banQinWmSoDetailEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmSoDetailEntity> page = banQinWmSoDetailService.findPage(new Page<BanQinWmSoDetailEntity>(request, response), banQinWmSoDetailEntity);
        return getBootstrapData(page);
    }

    /**
     * 保存出库单明细
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:save")
    @RequestMapping(value = "save")
    public AjaxJson save(@RequestBody BanQinWmSoDetailEntity banQinWmSoDetailEntity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, banQinWmSoDetailEntity)) {
            j.setSuccess(false);
            j.setMsg("非法数据");
            return j;
        }
        try {
            ResultMessage msg = outboundSoService.saveSoDetailEntity(banQinWmSoDetailEntity);
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除出库单明细
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:remove")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.removeSoDetailEntity(banQinWmSoDetailEntity.getSoNo(), banQinWmSoDetailEntity.getLineNo().split(","), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 出库单明细复制
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:duplicate")
    @RequestMapping(value = "duplicate")
    public AjaxJson duplicate(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundDuplicateService.duplicateSoDetailEntity(banQinWmSoDetailEntity.getSoNo(), banQinWmSoDetailEntity.getLineNo(), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 预配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:preAlloc")
    @RequestMapping(value = "preAlloc")
    public AjaxJson preAlloc(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.preallocBySoLine(ProcessByCode.BY_SO_LINE.getCode(), banQinWmSoDetailEntity.getSoNo(), banQinWmSoDetailEntity.getLineNo().split(","), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }
    
    /**
     * 分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:alloc")
    @RequestMapping(value = "alloc")
    public AjaxJson alloc(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.allocBySoLine(ProcessByCode.BY_SO_LINE.getCode(), banQinWmSoDetailEntity.getSoNo(), banQinWmSoDetailEntity.getLineNo().split(","), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 拣货确认
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:picking")
    @RequestMapping(value = "picking")
    public AjaxJson picking(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.pickingBySoLine(ProcessByCode.BY_SO_LINE.getCode(), banQinWmSoDetailEntity.getSoNo(), banQinWmSoDetailEntity.getLineNo().split(","), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消预配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:cancelPreAlloc")
    @RequestMapping(value = "cancelPreAlloc")
    public AjaxJson cancelPreAlloc(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelPreallocBySoLine(ProcessByCode.BY_SO_LINE.getCode(), banQinWmSoDetailEntity.getSoNo(), banQinWmSoDetailEntity.getLineNo().split(","), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消分配
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:cancelAlloc")
    @RequestMapping(value = "cancelAlloc")
    public AjaxJson cancelAlloc(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelAllocBySoLine(ProcessByCode.BY_SO_LINE.getCode(), banQinWmSoDetailEntity.getSoNo(), banQinWmSoDetailEntity.getLineNo().split(","), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消拣货
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:cancelPicking")
    @RequestMapping(value = "cancelPicking")
    public AjaxJson cancelPicking(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelPickingBySoLine(ProcessByCode.BY_SO_LINE.getCode(), banQinWmSoDetailEntity.getSoNo(), banQinWmSoDetailEntity.getLineNo().split(","), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 取消订单行
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmSoDetail:cancel")
    @RequestMapping(value = "cancel")
    public AjaxJson cancel(BanQinWmSoDetailEntity banQinWmSoDetailEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = outboundSoService.cancelSoDetail(banQinWmSoDetailEntity.getSoNo(), banQinWmSoDetailEntity.getLineNo().split(","), banQinWmSoDetailEntity.getOrgId());
            j.setSuccess(msg.isSuccess());
            j.setMsg(msg.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(BanQinWmSoDetail banQinWmSoDetail, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "出库单明细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmSoDetail> page = banQinWmSoDetailService.findPage(new Page<BanQinWmSoDetail>(request, response, -1), banQinWmSoDetail);
            new ExportExcel("出库单明细", BanQinWmSoDetail.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出出库单明细记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @RequiresPermissions("outbound:banQinWmSoDetail:import")
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<BanQinWmSoDetail> list = ei.getDataList(BanQinWmSoDetail.class);
            for (BanQinWmSoDetail banQinWmSoDetail : list) {
                try {
                    banQinWmSoDetailService.save(banQinWmSoDetail);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条出库单明细记录。");
            }
            addMessage(redirectAttributes, "已成功导入 " + successNum + " 条出库单明细记录" + failureMsg);
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入出库单明细失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/outbound/banQinWmSoDetail/?repage";
    }

    /**
     * 下载导入出库单明细数据模板
     */
    @RequiresPermissions("outbound:banQinWmSoDetail:import")
    @RequestMapping(value = "import/template")
    public String importFileTemplate(HttpServletResponse response, RedirectAttributes redirectAttributes) {
        try {
            String fileName = "出库单明细数据导入模板.xlsx";
            List<BanQinWmSoDetail> list = Lists.newArrayList();
            new ExportExcel("出库单明细数据", BanQinWmSoDetail.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            addMessage(redirectAttributes, "导入模板下载失败！失败信息：" + e.getMessage());
        }
        return "redirect:" + Global.getAdminPath() + "/wms/outbound/banQinWmSoDetail/?repage";
    }

}