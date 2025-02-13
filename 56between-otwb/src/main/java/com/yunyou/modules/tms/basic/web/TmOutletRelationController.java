package com.yunyou.modules.tms.basic.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yunyou.common.config.Global;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.BaseEntity;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.UserUtils;
import com.yunyou.modules.tms.basic.entity.TmOutletRelation;
import com.yunyou.modules.tms.basic.entity.extend.TmOutletRelationEntity;
import com.yunyou.modules.tms.basic.service.TmOutletRelationService;
import com.yunyou.modules.tms.common.TmsException;

/**
 * 网点拓扑图Controller
 *
 * @author liujianhua
 * @version 2020-03-02
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmOutletRelation")
public class TmOutletRelationController extends BaseController {

    @Autowired
    private TmOutletRelationService tmOutletRelationService;

    @ModelAttribute
    public TmOutletRelation get(@RequestParam(required = false) String id) {
        TmOutletRelation entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmOutletRelationService.get(id);
        }
        if (entity == null) {
            entity = new TmOutletRelation();
        }
        return entity;
    }

    /**
     * 网点拓扑图列表页面
     */
    @RequiresPermissions("basic:tmOutletRelation:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmOutletRelationList";
    }

    /**
     * 查看，增加，编辑网点拓扑图表单页面
     */
    @RequiresPermissions(value = {"basic:tmOutletRelation:view", "basic:tmOutletRelation:add", "basic:tmOutletRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmOutletRelation tmOutletRelation, Model model) {
        if (tmOutletRelation.getParent() != null && StringUtils.isNotBlank(tmOutletRelation.getParent().getId())) {
            tmOutletRelation.setParent(tmOutletRelationService.get(tmOutletRelation.getParent().getId()));
            // 获取排序号，最末节点排序号+30
            if (StringUtils.isBlank(tmOutletRelation.getId())) {
                TmOutletRelation tmOutletRelationChild = new TmOutletRelation();
                tmOutletRelationChild.setParent(new TmOutletRelation(tmOutletRelation.getParent().getId()));
                List<TmOutletRelation> list = tmOutletRelationService.findList(tmOutletRelation);
                if (list.size() > 0) {
                    tmOutletRelation.setSort(list.get(list.size() - 1).getSort());
                    if (tmOutletRelation.getSort() != null) {
                        tmOutletRelation.setSort(tmOutletRelation.getSort() + 30);
                    }
                }
            }
        }
        if (tmOutletRelation.getSort() == null) {
            tmOutletRelation.setSort(30);
        }
        model.addAttribute("tmOutletRelation", tmOutletRelation);
        return "modules/tms/basic/tmOutletRelationForm";
    }

    /**
     * 保存网点拓扑图
     */
    @ResponseBody
    @RequiresPermissions(value = {"basic:tmOutletRelation:add", "basic:tmOutletRelation:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmOutletRelation tmOutletRelation, Model model, RedirectAttributes redirectAttributes) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, tmOutletRelation)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmOutletRelationService.saveValidator(tmOutletRelation);
            tmOutletRelationService.save(tmOutletRelation);
            j.put("entity", tmOutletRelation);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "getChildren")
    public List<TmOutletRelation> getChildren(String parentId, String orgId) {
        if ("-1".equals(parentId)) {//如果是-1，没指定任何父节点，就从根节点开始查找
            parentId = "0";
        }
        return tmOutletRelationService.findChildren(parentId, orgId);
    }

    /**
     * 删除网点拓扑图
     */
    @ResponseBody
    @RequiresPermissions("basic:tmOutletRelation:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmOutletRelation tmOutletRelation, RedirectAttributes redirectAttributes) {
        AjaxJson j = new AjaxJson();
        tmOutletRelationService.delete(tmOutletRelation);
        j.setSuccess(true);
        j.setMsg("删除网点拓扑图成功");
        return j;
    }

    @RequiresPermissions("user")
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam(required = false) String extId, HttpServletResponse response) {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<TmOutletRelation> list = tmOutletRelationService.findList(new TmOutletRelation());
        for (TmOutletRelation e : list) {
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
    @RequiresPermissions("basic:tmOutletRelation:enable")
    @RequestMapping(value = "enable")
    public AjaxJson enable(@RequestParam String id) {
        AjaxJson j = new AjaxJson();

        try {
            TmOutletRelation tmOutletRelation = tmOutletRelationService.get(id);
            tmOutletRelation.setDelFlag(BaseEntity.DEL_FLAG_NORMAL.equals(tmOutletRelation.getDelFlag()) ? BaseEntity.DEL_FLAG_DELETE : BaseEntity.DEL_FLAG_NORMAL);
            tmOutletRelation.setUpdateBy(UserUtils.getUser());
            tmOutletRelation.setUpdateDate(new Date());
            tmOutletRelationService.save(tmOutletRelation);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "route")
    public Map<String, Object> route(TmOutletRelationEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmOutletRelationService.findRoute(new Page<>(request, response), qEntity));
    }

}