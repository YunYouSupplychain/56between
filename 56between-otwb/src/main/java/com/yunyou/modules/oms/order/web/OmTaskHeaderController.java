package com.yunyou.modules.oms.order.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.service.PushTaskService;
import com.yunyou.modules.oms.common.OmsException;
import com.yunyou.modules.oms.order.entity.OmTaskHeader;
import com.yunyou.modules.oms.order.entity.OmTaskHeaderEntity;
import com.yunyou.modules.oms.order.manager.OmTaskRemoveManager;
import com.yunyou.modules.oms.order.manager.OmTaskAllocManager;
import com.yunyou.modules.oms.order.service.OmTaskHeaderService;
import com.yunyou.modules.oms.order.manager.OmTaskMergeRestoreManager;
import com.yunyou.modules.wms.outbound.entity.extend.BanQinWmSoImportOrderNoQueryEntity;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 供应链作业任务Controller
 *
 * @author WMJ
 * @version 2019-04-21
 */
@Controller
@RequestMapping(value = "${adminPath}/oms/order/omTaskHeader")
public class OmTaskHeaderController extends BaseController {
    @Autowired
    private OmTaskHeaderService omTaskHeaderService;
    @Autowired
    private OmTaskRemoveManager omTaskRemoveManager;
    @Autowired
    private OmTaskMergeRestoreManager omTaskMergeRestoreManager;
    @Autowired
    private OmTaskAllocManager omTaskAllocManager;
    @Autowired
    private PushTaskService pushTaskService;

    @ModelAttribute
    public OmTaskHeaderEntity get(@RequestParam(required = false) String id) {
        OmTaskHeaderEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = omTaskHeaderService.getEntity(id);
        }
        if (entity == null) {
            entity = new OmTaskHeaderEntity();
        }
        return entity;
    }

    /**
     * 供应链作业任务列表页面
     */
    @RequiresPermissions("order:omTaskHeader:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/oms/order/omTaskHeaderList";
    }

    /**
     * 供应链作业任务列表数据
     */
    @ResponseBody
    @RequiresPermissions("order:omTaskHeader:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(OmTaskHeaderEntity omTaskHeaderEntity, HttpServletRequest request, HttpServletResponse response) {
        try {
            if (null != omTaskHeaderEntity.getCustomerNoFile()) {
                ImportExcel customerNoExcel = new ImportExcel(omTaskHeaderEntity.getCustomerNoFile(), 1, 0);
                List<BanQinWmSoImportOrderNoQueryEntity> customerNoList = customerNoExcel.getDataList(BanQinWmSoImportOrderNoQueryEntity.class);
                if (CollectionUtil.isNotEmpty(customerNoList)) {
                    omTaskHeaderEntity.setCustomerNoList(customerNoList.stream().map(BanQinWmSoImportOrderNoQueryEntity::getOrderNo).collect(Collectors.toList()));
                }
            }
            if (null != omTaskHeaderEntity.getExtendNoFile()) {
                ImportExcel extendNoExcel = new ImportExcel(omTaskHeaderEntity.getExtendNoFile(), 1, 0);
                List<BanQinWmSoImportOrderNoQueryEntity> extendNoList = extendNoExcel.getDataList(BanQinWmSoImportOrderNoQueryEntity.class);
                if (CollectionUtil.isNotEmpty(extendNoList)) {
                    omTaskHeaderEntity.setExtendNoList(extendNoList.stream().map(BanQinWmSoImportOrderNoQueryEntity::getOrderNo).collect(Collectors.toList()));
                }
            }
        } catch (Exception ignored) {
        }
        Page<OmTaskHeaderEntity> page = omTaskHeaderService.findPage(new Page<OmTaskHeaderEntity>(request, response), omTaskHeaderEntity);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑供应链作业任务表单页面
     */
    @RequiresPermissions(value = {"order:omTaskHeader:view", "order:omTaskHeader:add", "order:omTaskHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(OmTaskHeaderEntity omTaskHeaderEntity, Model model) {
        model.addAttribute("omTaskHeaderEntity", omTaskHeaderEntity);
        return "modules/oms/order/omTaskHeaderForm";
    }

    /**
     * 保存供应链作业任务
     */
    @ResponseBody
    @RequiresPermissions(value = {"order:omTaskHeader:add", "order:omTaskHeader:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(OmTaskHeaderEntity omTaskHeaderEntity, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, omTaskHeaderEntity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }

        try {
            omTaskHeaderService.save(omTaskHeaderEntity);
            j.setSuccess(true);
            j.setMsg("保存供应链作业任务成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除供应链作业任务
     */
    @ResponseBody
    @RequiresPermissions("order:omTaskHeader:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            for (String id : ids.split(",")) {
                omTaskRemoveManager.remove(id, false, true);
            }
            j.setMsg("删除作业任务成功");
        } catch (OmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detail")
    public OmTaskHeader detail(String id) {
        return omTaskHeaderService.get(id);
    }

    /**
     * 描述：分配
     *
     * @author Jianhua on 2019/5/9
     */
    @ResponseBody
    @RequiresPermissions("order:omTaskHeader:alloc")
    @RequestMapping(value = "allocate")
    public AjaxJson allocate(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            try {
                omTaskAllocManager.allocate(id);
            } catch (OmsException e) {
                errMsg.append("<br>").append(e.getMessage());
            } catch (Exception e) {
                logger.error("", e);
                errMsg.append("<br>").append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(j.getMsg() + "，其中" + errMsg);
        }
        return j;
    }

    /**
     * 描述：取消分配
     *
     * @author ZYF on 2019/6/12
     */
    @ResponseBody
    @RequiresPermissions("order:omTaskHeader:unAlloc")
    @RequestMapping(value = "unAllocate")
    public AjaxJson unAllocate(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            try {
                omTaskAllocManager.unAllocate(id);
            } catch (OmsException e) {
                errMsg.append("<br>").append(e.getMessage());
            } catch (Exception e) {
                logger.error("", e);
                errMsg.append("<br>").append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(j.getMsg() + "，其中" + errMsg);
        }
        return j;
    }

    /**
     * 描述：任务下发
     *
     * @author Jianhua on 2019/5/9
     */
    @ResponseBody
    @RequiresPermissions("order:omTaskHeader:send")
    @RequestMapping(value = "sendTask")
    public AjaxJson sendTask(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            try {
                pushTaskService.pushTask(omTaskHeaderService.get(id));
            } catch (OmsException e) {
                errMsg.append("<br>").append(e.getMessage());
            } catch (Exception e) {
                logger.error("", e);
                errMsg.append("<br>").append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(j.getMsg() + "，其中" + errMsg);
        }
        return j;
    }

    /**
     * 描述：任务合并
     * currOrgId 当前机构ID
     *
     * @author Jianhua on 2019/5/5
     */
    @ResponseBody
    @RequiresPermissions("order:omTaskHeader:merge")
    @RequestMapping(value = "mergeTask")
    public AjaxJson mergeTask(@RequestParam String ids, @RequestParam String currOrgId) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        try {
            List<OmTaskHeader> taskList = Lists.newArrayList();
            Arrays.stream(ids.split(",")).forEach(id -> {
                OmTaskHeader omTaskHeader = omTaskHeaderService.get(id);
                if (omTaskHeader != null) {
                    taskList.add(omTaskHeader);
                }
            });
            omTaskMergeRestoreManager.merge(taskList, currOrgId);

            j.setSuccess(true);
            j.setMsg("任务合并成功");
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：任务还原
     *
     * @author Jianhua on 2019/5/5
     */
    @ResponseBody
    @RequiresPermissions("order:omTaskHeader:restore")
    @RequestMapping(value = "restore")
    public AjaxJson restoreTask(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        try {
            String[] idAddr = ids.split(",");
            for (String id : idAddr) {
                omTaskMergeRestoreManager.restoreTask(id);
            }
            j.setSuccess(true);
            j.setMsg("任务还原成功");
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：承运商分配
     *
     * @author Jianhua on 2019/5/5
     */
    @ResponseBody
    @RequiresPermissions("order:omTaskHeader:carrierAlloc")
    @RequestMapping(value = "carrierAlloc")
    public AjaxJson carrierAlloc(@RequestParam String ids) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] taskIds = ids.split(",");
        for (String taskId : taskIds) {
            try {
                omTaskHeaderService.carrierAlloc(taskId);
            } catch (Exception e) {
                logger.error("供应链任务-承运商分配", e);
                errMsg.append("<br>").append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(j.getMsg() + "，其中" + errMsg);
        }
        return j;
    }

    /**
     * 描述：承运商指定
     *
     * @author Jianhua on 2019/5/5
     */
    @ResponseBody
    @RequiresPermissions("order:omTaskHeader:carrierDesignate")
    @RequestMapping(value = "carrierDesignate")
    public AjaxJson carrierDesignate(@RequestParam String ids, @RequestParam String carrier) {
        AjaxJson j = new AjaxJson();
        if (StringUtils.isBlank(ids)) {
            j.setSuccess(false);
            j.setMsg("请选择记录");
            return j;
        }

        StringBuilder errMsg = new StringBuilder();
        String[] taskIds = ids.split(",");
        for (String taskId : taskIds) {
            try {
                omTaskHeaderService.carrierDesignate(taskId, carrier);
            } catch (Exception e) {
                logger.error("供应链任务-承运商指定", e);
                errMsg.append("<br>").append(e.getMessage());
            }
        }
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(j.getMsg() + "，其中" + errMsg);
        }
        return j;
    }

}