package com.yunyou.modules.sys.common.web;

import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetail;
import com.yunyou.modules.sys.common.entity.SysWmsRuleWvDetailOrder;
import com.yunyou.modules.sys.common.service.SysWmsRuleWvDetailOrderService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;

/**
 * 波次规则订单限制Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/wms/ruleWvDetailOrder")
public class SysWmsRuleWvDetailOrderController extends BaseController {
    @Autowired
    private SysWmsRuleWvDetailOrderService sysWmsRuleWvDetailOrderService;

    /**
     * 订单限制列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public List<SysWmsRuleWvDetailOrder> data(SysWmsRuleWvDetailOrder entity) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return sysWmsRuleWvDetailOrderService.findList(entity);
    }

    /**
     * 保存
     */
    @RequestMapping(value = "save")
    @ResponseBody
    public AjaxJson save(SysWmsRuleWvDetail sysWmsRuleWvDetail) throws Exception {
        AjaxJson j = new AjaxJson();
        try {
            String result = sysWmsRuleWvDetailOrderService.saveEntity(sysWmsRuleWvDetail.getRuleWvDetailOrderList());
            j.put("sql", result);
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
     * 批量删除订单限制
     */
    @ResponseBody
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        sysWmsRuleWvDetailOrderService.deleteEntity(Arrays.asList(ids.split(",")));
        j.setMsg("删除成功");
        return j;
    }

}