package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetailWv;
import com.yunyou.modules.sys.common.service.SysWmsRuleWvDetailWvService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 波次规则波次限制Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/ruleWvDetailWv")
public class SysWmsRuleWvDetailWvController extends BaseController {
    @Autowired
    private SysWmsRuleWvDetailWvService sysWmsRuleWvDetailWvService;

    /**
     * 列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public List<SysWmsRuleWvDetailWv> data(SysWmsRuleWvDetailWv entity) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return sysWmsRuleWvDetailWvService.findList(entity);
    }

    /**
     * 保存限制
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(SysWmsRuleWvDetail sysWmsRuleWvDetail) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            sysWmsRuleWvDetail.getRuleWvDetailWvList().forEach(sysWmsRuleWvDetailWvService::save);
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
     * 批量删除限制
     */
    @ResponseBody
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysWmsRuleWvDetailWvService.delete(sysWmsRuleWvDetailWvService.get(id));
        }
        j.setMsg("删除成功");
        return j;
    }

}