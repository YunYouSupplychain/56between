package com.yunyou.modules.wms.report.web;

import com.google.common.collect.Lists;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.report.entity.WmRepOutLotEntity;
import com.yunyou.modules.wms.report.entity.extend.BanQinWmRepOutLotExportEntity;
import com.yunyou.modules.wms.report.service.BanQinWmRepOutLotService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 出库批次明细报表Controller
 *
 * @author ZYF
 * @version 2020-12-07
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/report/banQinWmRepOutLot")
public class BanQinWmRepOutLotController extends BaseController {
    @Autowired
    private BanQinWmRepOutLotService banQinWmRepOutLotService;

    @ModelAttribute
    public WmRepOutLotEntity get(@RequestParam(required = false) String id) {
        return new WmRepOutLotEntity();
    }

    /**
     * 出库批次明细报表页面
     */
    @RequiresPermissions("report:banQinWmRepOutLot:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/report/banQinWmRepOutLotList";
    }

    /**
     * 出库批次明细报表数据
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmRepOutLot:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WmRepOutLotEntity wmRepOutLotEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        if (StringUtils.isNotBlank(wmRepOutLotEntity.getStatuss())) {
            wmRepOutLotEntity.setStatusList(Arrays.asList(wmRepOutLotEntity.getStatuss().split(",")));
        }
        Page<WmRepOutLotEntity> page = banQinWmRepOutLotService.findPage(new Page<WmRepOutLotEntity>(request, response), wmRepOutLotEntity);
        return getBootstrapData(page);
    }


    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("report:banQinWmRepOutLot:export")
    @RequestMapping(value = "export", method = RequestMethod.POST)
    public AjaxJson exportFile(WmRepOutLotEntity entity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "分配明细" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<WmRepOutLotEntity> page = banQinWmRepOutLotService.findPage(new Page<WmRepOutLotEntity>(request, response, -1), entity);
            List<WmRepOutLotEntity> pageList = page.getList();
            List<BanQinWmRepOutLotExportEntity> exportEntityList = Lists.newArrayList();
            for (WmRepOutLotEntity allocEntity : pageList) {
                BanQinWmRepOutLotExportEntity exportEntity = new BanQinWmRepOutLotExportEntity();
                BeanUtils.copyProperties(allocEntity, exportEntity);
                if (exportEntity.getLotAtt01() != null) {
                    exportEntity.setLotAtt01String(DateUtils.formatDate(exportEntity.getLotAtt01(), "yyyy-MM-dd"));
                }
                if (exportEntity.getLotAtt02() != null) {
                    exportEntity.setLotAtt02String(DateUtils.formatDate(exportEntity.getLotAtt02(), "yyyy-MM-dd"));
                }
                if (exportEntity.getLotAtt03() != null) {
                    exportEntity.setLotAtt03String(DateUtils.formatDate(exportEntity.getLotAtt03(), "yyyy-MM-dd"));
                }
                exportEntityList.add(exportEntity);
            }
            new ExportExcel("", BanQinWmRepOutLotExportEntity.class).setDataList(exportEntityList).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出分配明细记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }
}