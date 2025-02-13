package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.time.DateUtils;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.outbound.entity.BanQinWmPickHeaderEntity;
import com.yunyou.modules.wms.outbound.service.BanQinOutboundPickOrderService;
import com.yunyou.modules.wms.outbound.service.BanQinWmPickHeaderService;
import com.yunyou.modules.wms.report.entity.WmPickOrder;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
 * 拣货单Controller
 *
 * @author ZYF
 * @version 2020-05-12
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmPickHeader")
public class BanQinWmPickHeaderController extends BaseController {
    @Autowired
    private BanQinWmPickHeaderService banQinWmPickHeaderService;
    @Autowired
    private BanQinOutboundPickOrderService banQinOutboundPickOrderService;

    @ModelAttribute
    public BanQinWmPickHeaderEntity get(@RequestParam(required = false) String id) {
        BanQinWmPickHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmPickHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new BanQinWmPickHeaderEntity();
        }
        return entity;
    }

    /**
     * 拣货单列表页面
     */
    @RequiresPermissions("outbound:banQinWmPickHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmPickHeaderList";
    }

    /**
     * 拣货单列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(BanQinWmPickHeaderEntity banQinWmPickHeaderEntity, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinWmPickHeaderEntity> page = banQinWmPickHeaderService.findPage(new Page<BanQinWmPickHeaderEntity>(request, response), banQinWmPickHeaderEntity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑拣货单表单页面
     */
    @RequiresPermissions(value = {"outbound:banQinWmPickHeader:view", "outbound:banQinWmPickHeader:add", "outbound:banQinWmPickHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BanQinWmPickHeaderEntity banQinWmPickHeaderEntity, Model model) {
        model.addAttribute("banQinWmPickHeaderEntity", banQinWmPickHeaderEntity);
        return "modules/wms/outbound/banQinWmPickHeaderForm";
    }

    /**
     * 批量删除拣货单
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmPickHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(BanQinWmPickHeaderEntity banQinWmPickHeaderEntity, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage msg = banQinOutboundPickOrderService.removePickEntity(banQinWmPickHeaderEntity.getPickNo().split(","), banQinWmPickHeaderEntity.getOrgId());
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
    public AjaxJson exportFile(BanQinWmPickHeaderEntity banQinWmPickHeaderEntity, HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "拣货单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<BanQinWmPickHeaderEntity> page = banQinWmPickHeaderService.findPage(new Page<BanQinWmPickHeaderEntity>(request, response, -1), banQinWmPickHeaderEntity);
            new ExportExcel("拣货单", BanQinWmPickHeaderEntity.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出拣货单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 打印拣货单
     * @param model
     * @return
     */
    @RequestMapping(value = "/printPickOrder")
    public String printPickOrder(Model model, String ids) {
        List<WmPickOrder> result = banQinWmPickHeaderService.getPickOrder(Arrays.asList(ids.split(",")));
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/wmsPickOrder.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);

        return "iReportView";
    }
}