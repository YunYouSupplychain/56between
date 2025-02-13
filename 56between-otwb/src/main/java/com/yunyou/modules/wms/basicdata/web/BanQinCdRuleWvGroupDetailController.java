package com.yunyou.modules.wms.basicdata.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupDetail;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupDetailEntity;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdRuleWvGroupHeaderEntity;
import com.yunyou.modules.wms.basicdata.service.BanQinCdRuleWvGroupDetailService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 波次规则组明细Controller
 *
 * @author WMJ
 * @version 2020-02-09
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdRuleWvGroupDetail")
public class BanQinCdRuleWvGroupDetailController extends BaseController {

    @Autowired
    private BanQinCdRuleWvGroupDetailService banQinCdRuleWvGroupDetailService;

    @ModelAttribute
    public BanQinCdRuleWvGroupDetail get(@RequestParam(required = false) String id) {
        BanQinCdRuleWvGroupDetail entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = banQinCdRuleWvGroupDetailService.get(id);
        }
        if (entity == null) {
            entity = new BanQinCdRuleWvGroupDetail();
        }
        return entity;
    }

    /**
     * 波次规则组明细列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public List<BanQinCdRuleWvGroupDetailEntity> data(BanQinCdRuleWvGroupDetail banQinCdRuleWvGroupDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
        return banQinCdRuleWvGroupDetailService.findGrid(banQinCdRuleWvGroupDetail);
    }

    /**
     * 保存波次规则组明细
     */
    @RequiresPermissions(value = "basicdata:banQinCdRuleWvGroupDetail:saveDetail")
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(BanQinCdRuleWvGroupHeaderEntity entity, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            List<BanQinCdRuleWvGroupDetail> detailList = entity.getDetailList();
            if (CollectionUtil.isEmpty(detailList)) {
                j.setSuccess(false);
                j.setMsg("当前没有要保存的数据");
                return j;
            }
            banQinCdRuleWvGroupDetailService.saveEntity(entity.getDetailList());
        } catch (DuplicateKeyException e) {
            j.setSuccess(false);
            j.setMsg("行号重复");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }

        return j;
    }

    /**
     * 批量删除波次规则组明细
     */
    @ResponseBody
    @RequiresPermissions("basicdata:banQinCdRuleWvGroupDetail:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            banQinCdRuleWvGroupDetailService.delete(banQinCdRuleWvGroupDetailService.get(id));
        }
        j.setMsg("操作成功");
        return j;
    }

}