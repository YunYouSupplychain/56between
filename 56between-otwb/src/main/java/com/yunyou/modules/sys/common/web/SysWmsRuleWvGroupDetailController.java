package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvGroupDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvGroupHeader;
import com.yunyou.modules.sys.common.service.SysWmsRuleWvGroupDetailService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 波次规则组明细Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/ruleWvGroupDetail")
public class SysWmsRuleWvGroupDetailController extends BaseController {
    @Autowired
    private SysWmsRuleWvGroupDetailService sysWmsRuleWvGroupDetailService;

    @ModelAttribute
    public SysWmsRuleWvGroupDetail get(@RequestParam(required = false) String id) {
        SysWmsRuleWvGroupDetail entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysWmsRuleWvGroupDetailService.get(id);
        }
        if (entity == null) {
            entity = new SysWmsRuleWvGroupDetail();
        }
        return entity;
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public List<SysWmsRuleWvGroupDetail> data(SysWmsRuleWvGroupDetail entity) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return sysWmsRuleWvGroupDetailService.findList(entity);
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = "sys:common:wms:ruleWvGroup:saveDetail")
    @RequestMapping(value = "save")
    public AjaxJson save(SysWmsRuleWvGroupHeader wmsRuleWvGroupHeader) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            sysWmsRuleWvGroupDetailService.batchSave(wmsRuleWvGroupHeader.getWvGroupDetailList());
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
    @RequiresPermissions("sys:common:wms:ruleWvGroup:removeDetail")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsRuleWvGroupDetailService.delete(sysWmsRuleWvGroupDetailService.get(id));
        }
        j.setMsg("操作成功");
        return j;
    }

}