package com.yunyou.modules.sys.common.web;

import com.yunyou.common.config.Global;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysCommonRegion;
import com.yunyou.modules.sys.common.entity.extend.SysCommonRegionEntity;
import com.yunyou.modules.sys.common.service.SysCommonRegionService;
import com.yunyou.modules.sys.entity.Area;
import com.yunyou.modules.sys.service.AreaService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import com.yunyou.modules.tms.common.TmsException;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 区域Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/region")
public class SysCommonRegionController extends BaseController {
    @Autowired
    private SysCommonRegionService sysCommonRegionService;
    @Autowired
    private AreaService areaService;

    /**
     * 列表页面
     */
    @RequiresPermissions("sys:common:region:list")
    @RequestMapping(value = {"list", ""})
    public String list(Model model) {
        model.addAttribute("sysCommonRegionEntity", new SysCommonRegionEntity());
        return "modules/sys/common/sysCommonRegionList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("sys:common:region:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(SysCommonRegionEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonRegionService.findPage(new Page<SysCommonRegionEntity>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"sys:common:region:view", "sys:common:region:add", "sys:common:region:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(SysCommonRegionEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = sysCommonRegionService.getEntity(entity.getId());
        }
        model.addAttribute("sysCommonRegionEntity", entity);
        return "modules/sys/common/sysCommonRegionForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"sys:common:region:add", "sys:common:region:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(@Valid SysCommonRegion sysCommonRegion, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, sysCommonRegion)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            sysCommonRegionService.save(sysCommonRegion);
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
    @RequiresPermissions("sys:common:region:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysCommonRegionService.delete(new SysCommonRegion(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysCommonRegionEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysCommonRegionService.findGrid(new Page<SysCommonRegionEntity>(request, response), entity));
    }

    /**
     * 区域内地址页面
     */
    @RequestMapping(value = "place")
    public String place(SysCommonRegion sysCommonRegion, Model model) {
        if (StringUtils.isNotBlank(sysCommonRegion.getId())) {
            sysCommonRegion = sysCommonRegionService.get(sysCommonRegion.getId());
        }
        model.addAttribute("sysCommonRegion", sysCommonRegion);
        return "modules/sys/common/sysCommonRegionPlace";
    }

    @ResponseBody
    @RequestMapping(value = "saveArea")
    public AjaxJson saveArea(SysCommonRegion entity) {
        AjaxJson j = new AjaxJson();
        try {
            sysCommonRegionService.saveArea(entity);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData(@RequestParam String bssId, @RequestParam(required = false) String extId, HttpServletResponse response) {
        SysCommonRegion entity = sysCommonRegionService.get(bssId);
        String areaIds = entity != null ? "," + entity.getAreaIds() + "," : "";
        List<Map<String, Object>> mapList = Lists.newArrayList();
        List<Area> list = areaService.findCountToArea();
        for (int i = 0; i < list.size(); i++) {
            Area e = list.get(i);
            if (StringUtils.isBlank(extId) || (!e.getId().equals(extId) && !e.getParentIds().contains("," + extId + ","))) {
                Map<String, Object> map = Maps.newHashMap();
                map.put("id", e.getId());
                if (Global.TREE_ROOT_NODE.equals(e.getParentId())) {
                    map.put("parent", "#");
                    Map<String, Object> state = Maps.newHashMap();
                    state.put("opened", true);
                    map.put("state", state);
                } else {
                    if (i == 0) {
                        map.put("parent", "#");
                    } else {
                        map.put("parent", e.getParentId());
                    }
                }
                if (areaIds.contains("," + e.getId() + ",")) {
                    Map<String, Object> state = Maps.newHashMap();
                    state.put("selected", true);
                    map.put("state", state);
                }
                // 自定义属性
                Map<String, Object> attr = Maps.newHashMap();
                attr.put("code", e.getCode());
                map.put("a_attr", attr);

                map.put("name", e.getName());
                map.put("text", e.getName());
                mapList.add(map);
            }
        }
        return mapList;
    }
}