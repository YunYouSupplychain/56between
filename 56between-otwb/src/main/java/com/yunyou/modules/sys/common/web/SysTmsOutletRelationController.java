package com.yunyou.modules.sys.common.web;

import com.yunyou.common.config.Global;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.interfaces.interactive.action.SyncPlatformDataCommonAction;
import com.yunyou.modules.sys.common.entity.SysTmsOutletRelation;
import com.yunyou.modules.sys.common.entity.extend.SysTmsOutletRelationEntity;
import com.yunyou.modules.sys.common.service.SysTmsOutletRelationService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.common.TmsException;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 网点拓扑图Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/tms/outletRelation")
public class SysTmsOutletRelationController extends BaseController {
    @Autowired
    private SysTmsOutletRelationService sysTmsOutletRelationService;
    @Autowired
    private SyncPlatformDataCommonAction syncPlatformDataCommonAction;

    @ModelAttribute
    public SysTmsOutletRelation get(@RequestParam(required = false) String id) {
        SysTmsOutletRelation entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = sysTmsOutletRelationService.get(id);
        }
        if (entity == null) {
            entity = new SysTmsOutletRelation();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:tms:outletRelation:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/sys/common/sysTmsOutletRelationList";
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:tms:outletRelation:view", "sys:common:tms:outletRelation:add", "sys:common:tms:outletRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysTmsOutletRelation sysTmsOutletRelation, Model model) {
        if (sysTmsOutletRelation.getParent() != null && StringUtils.isNotBlank(sysTmsOutletRelation.getParent().getId())) {
            sysTmsOutletRelation.setParent(sysTmsOutletRelationService.get(sysTmsOutletRelation.getParent().getId()));
            // 获取排序号，最末节点排序号+30
            if (StringUtils.isBlank(sysTmsOutletRelation.getId())) {
                SysTmsOutletRelation sysTmsOutletRelationChild = new SysTmsOutletRelation();
                sysTmsOutletRelationChild.setParent(new SysTmsOutletRelation(sysTmsOutletRelation.getParent().getId()));
                List<SysTmsOutletRelation> list = sysTmsOutletRelationService.findList(sysTmsOutletRelation);
                if (list.size() > 0) {
                    sysTmsOutletRelation.setSort(list.get(list.size() - 1).getSort());
                    if (sysTmsOutletRelation.getSort() != null) {
                        sysTmsOutletRelation.setSort(sysTmsOutletRelation.getSort() + 30);
                    }
                }
            }
        }
        if (sysTmsOutletRelation.getSort() == null) {
            sysTmsOutletRelation.setSort(30);
        }
        if (StringUtils.isBlank(sysTmsOutletRelation.getDataSet())) {
            sysTmsOutletRelation.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        model.addAttribute("sysTmsOutletRelation", sysTmsOutletRelation);
        return "modules/sys/common/sysTmsOutletRelationForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:tms:outletRelation:add", "sys:common:tms:outletRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(SysTmsOutletRelation sysTmsOutletRelation, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysTmsOutletRelation)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysTmsOutletRelationService.saveValidator(sysTmsOutletRelation);
            sysTmsOutletRelationService.save(sysTmsOutletRelation);
            j.put("entity", sysTmsOutletRelation);
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

    @ResponseBody
    @RequestMapping(value = "getChildren")
    public List<SysTmsOutletRelation> getChildren(String parentId, String dataSet) {
        if ("-1".equals(parentId)) {//如果是-1，没指定任何父节点，就从根节点开始查找
            parentId = "0";
        }
        if (StringUtils.isBlank(dataSet)) {
            dataSet = SysDataSetUtils.getUserDataSet().getCode();
        }
        return sysTmsOutletRelationService.findChildren(parentId, dataSet);
    }

    /**
     * 删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:outletRelation:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(SysTmsOutletRelation sysTmsOutletRelation) {
        sysTmsOutletRelationService.delete(sysTmsOutletRelation);
        return new AjaxJson();
    }

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<SysTmsOutletRelation> list = sysTmsOutletRelationService.findList(new SysTmsOutletRelation());
        for (SysTmsOutletRelation e : list) {
            if (StringUtils.isBlank(extId) || !extId.equals(e.getId()) && !e.getParentIds().contains("," + extId + ",")) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                map.put("text", e.getName());
                if (StringUtils.isBlank(e.getParentId()) || Global.TREE_ROOT_NODE.equals(e.getParentId())) {
                    map.put("parent", "#");
                    Map<String, Object> state = Maps.newHashMap();
                    state.put("opened", true);
                    map.put("state", state);
                } else {
                    map.put("parent", e.getParentId());
                }
                mapList.add(map);
            }
        }
        return mapList;
    }

    /**
     * 启用/停用
     */
    @ResponseBody
    @RequiresPermissions("sys:common:tms:outletRelation:enable")
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String id) {
        AjaxJson j = new AjaxJson();

        try {
            SysTmsOutletRelation sysTmsOutletRelation = sysTmsOutletRelationService.get(id);
            sysTmsOutletRelation.setDelFlag(BaseEntity.DEL_FLAG_NORMAL.equals(sysTmsOutletRelation.getDelFlag()) ? BaseEntity.DEL_FLAG_DELETE : BaseEntity.DEL_FLAG_NORMAL);
            sysTmsOutletRelation.setUpdateBy(UserUtils.getUser());
            sysTmsOutletRelation.setUpdateDate(new Date());
            sysTmsOutletRelationService.save(sysTmsOutletRelation);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequiresPermissions("sys:common:tms:outletRelation:sync")
    @RequestMapping(value = "syncAll", method = {RequestMethod.GET, RequestMethod.POST})
    public AjaxJson syncAll() {
        AjaxJson j = new AjaxJson();
        try {
            syncPlatformDataCommonAction.syncInsert(sysTmsOutletRelationService.findSync(SysDataSetUtils.getUserDataSet().getCode()).toArray(new SysTmsOutletRelation[0]));
        } catch (Exception e) {
            logger.error("同步异常", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "route")
    public Map<String, Object> route(SysTmsOutletRelationEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysTmsOutletRelationService.findRoute(new Page<>(request, response), entity));
    }

}