package com.yunyou.modules.wms.outbound.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.wms.common.entity.WarehouseException;
import com.yunyou.modules.wms.outbound.entity.*;
import com.yunyou.modules.wms.outbound.service.BanQinWmOutHandoverService;
import com.yunyou.modules.wms.report.entity.OutHandoverListLabel;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 描述：交接单Controller
 *
 * @author Jianhua on 2020-2-6
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/outbound/banQinWmOutHandover")
public class BanQinWmOutHandoverController extends BaseController {
    @Autowired
    private BanQinWmOutHandoverService banQinWmOutHandoverService;

    @ModelAttribute
    public BanQinWmOutHandoverHeaderEntity get(String id) {
        BanQinWmOutHandoverHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinWmOutHandoverService.getEntity(id);
        }
        return entity == null ? new BanQinWmOutHandoverHeaderEntity() : entity;
    }

    @RequiresPermissions("outbound:banQinWmOutHandover:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/wms/outbound/banQinWmOutHandoverList";
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmOutHandover:list")
    @RequestMapping("data")
    public Map<String, Object> data(BanQinWmOutHandoverHeaderEntity queryEntity, HttpServletRequest request, HttpServletResponse response) {
        Page<BanQinWmOutHandoverHeaderEntity> page = banQinWmOutHandoverService.findPage(new Page<BanQinWmOutHandoverHeaderEntity>(request, response), queryEntity);
        return getBootstrapData(page);
    }

    @RequiresPermissions("outbound:banQinWmOutHandover:list")
    @RequestMapping("form")
    public String form(BanQinWmOutHandoverHeaderEntity entity, Model model) {
        model.addAttribute("banQinWmOutHandoverHeaderEntity", entity);
        return "modules/wms/outbound/banQinWmOutHandoverForm";
    }

    @ResponseBody
    @RequiresPermissions("outbound:banQinWmOutHandover:list")
    @RequestMapping("detailData")
    public Map<String, Object> detailData(BanQinWmOutHandoverDetail queryEntity, HttpServletRequest request, HttpServletResponse response) {
        Page<BanQinWmOutHandoverDetail> page = banQinWmOutHandoverService.findDetailPage(new Page<>(request, response), queryEntity);
        return getBootstrapData(page);
    }

    /**
     * 描述：生成交接单
     *
     * @author Jianhua on 2020-2-10
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmOutHandover:gen")
    @RequestMapping("gen")
    public AjaxJson gen(BanQinWmOutHandoverGenCondition condition) {
        AjaxJson j = new AjaxJson();
        try {
            condition.setHandoverOp(UserUtils.getUser().getName());
            condition.setHandoverTime(new Date());
            banQinWmOutHandoverService.genHandoverOrder(condition);
        } catch (WarehouseException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：删除交接单
     *
     * @author Jianhua on 2020-2-10
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmOutHandover:del")
    @RequestMapping("deleteAll")
    public AjaxJson delete(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
        }

        StringBuffer errMsg = new StringBuffer();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                banQinWmOutHandoverService.deleteEntity(id);
            } catch (Exception e) {
                errMsg.append("<br>").append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    /**
     * 描述：删除交接单明细
     *
     * @author Jianhua on 2020-2-10
     */
    @ResponseBody
    @RequiresPermissions("outbound:banQinWmOutHandover:del")
    @RequestMapping("delDetail")
    public AjaxJson delDetail(String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
        }

        StringBuffer errMsg = new StringBuffer();
        String[] idArr = ids.split(",");
        for (String id : idArr) {
            try {
                banQinWmOutHandoverService.deleteDetail(id);
            } catch (Exception e) {
                errMsg.append("<br>").append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setMsg(errMsg.toString());
        }
        return j;
    }

    /**
     * 描述：打印交接清单
     *
     * @author Jianhua on 2020-2-10
     */
    @RequiresPermissions("outbound:banQinWmOutHandover:printHandoverList")
    @RequestMapping(value = "/printHandoverList")
    public String printHandoverList(String id, Model model) {
        List<OutHandoverListLabel> result = banQinWmOutHandoverService.getOutHandoverList(id);
        // 报表数据源
        JRDataSource jrDataSource = new JRBeanCollectionDataSource(result);
        // 动态指定报表模板url
        model.addAttribute("url", "classpath:/jasper/outHandoverList.jasper");
        model.addAttribute("format", "pdf");
        model.addAttribute("jrMainDataSource", jrDataSource);
        return "iReportView";
    }
}
