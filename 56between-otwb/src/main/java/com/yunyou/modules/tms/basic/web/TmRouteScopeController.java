package com.yunyou.modules.tms.basic.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.collection.CollectionUtil;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.basic.entity.TmObjRoute;
import com.yunyou.modules.tms.basic.entity.TmRouteScope;
import com.yunyou.modules.tms.basic.entity.extend.TmRouteScopeEntity;
import com.yunyou.modules.tms.basic.entity.extend.TmRouteScopeObjEntity;
import com.yunyou.modules.tms.basic.service.TmObjRouteService;
import com.yunyou.modules.tms.basic.service.TmRouteScopeService;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.common.TmsException;
import com.yunyou.modules.tms.common.map.MapUtil;
import com.yunyou.modules.tms.common.map.exception.AddressToPointException;
import com.yunyou.modules.tms.common.map.exception.RoutePlanningException;
import com.yunyou.modules.tms.common.map.geo.Point;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 路由范围Controller
 *
 * @author liujianhua
 * @version 2021-10-15
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/basic/tmRouteScope")
public class TmRouteScopeController extends BaseController {
    @Autowired
    private TmRouteScopeService tmRouteScopeService;
    @Autowired
    private TmObjRouteService tmObjRouteService;

    @ModelAttribute
    public TmRouteScopeEntity get(@RequestParam(required = false) String id) {
        TmRouteScopeEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmRouteScopeService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmRouteScopeEntity();
        }
        return entity;
    }

    /**
     * 列表页面
     */
    @RequiresPermissions("tms:basic:routeScope:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/basic/tmRouteScopeList";
    }

    /**
     * 列表数据
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:routeScope:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmRouteScopeEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmRouteScopeService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 查看，增加，编辑表单页面
     */
    @RequiresPermissions(value = {"tms:basic:routeScope:view", "tms:basic:routeScope:add", "tms:basic:routeScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmRouteScopeEntity entity, Model model) {
        model.addAttribute("tmRouteScopeEntity", entity);
        return "modules/tms/basic/tmRouteScopeForm";
    }

    /**
     * 保存
     */
    @ResponseBody
    @RequiresPermissions(value = {"tms:basic:routeScope:add", "tms:basic:routeScope:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(@RequestBody TmRouteScopeEntity entity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            tmRouteScopeService.saveEntity(entity);
        } catch (TmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 删除
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:routeScope:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(TmRouteScope tmRouteScope) {
        AjaxJson j = new AjaxJson();
        try {
            tmRouteScopeService.delete(tmRouteScope);
        } catch (Exception e) {
            logger.error("删除id=[" + tmRouteScope.getId() + "]", e);
            j.setSuccess(false);
            j.setMsg("操作失败" + e.getMessage());
        }
        return j;
    }

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:routeScope:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        String[] idArray = ids.split(",");
        for (String id : idArray) {
            tmRouteScopeService.delete(new TmRouteScope(id));
        }
        return j;
    }

    /**
     * 生成路由
     */
    @ResponseBody
    @RequiresPermissions("tms:basic:routeScope:genRoute")
    @RequestMapping(value = "genRoute")
    public AjaxJson genRoute(String ids) {
        AjaxJson j = new AjaxJson();
        try {
            String[] idArray = ids.split(",");
            for (String id : idArray) {
                this.genRoute(tmRouteScopeService.getEntity(id));
            }
        } catch (TmsException e) {
            logger.info(e.getMessage());
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    private void genRoute(TmRouteScopeEntity entity) {
        List<TmRouteScopeObjEntity> objList = entity.getObjList();
        if (CollectionUtil.isEmpty(objList)) {
            return;
        }
        List<TmRouteScopeObjEntity> startList = objList.stream().filter(o -> TmsConstants.TRANSPORT_SCOPE_TYPE_1.equals(o.getScopeType())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(startList)) {
            return;
        }
        List<TmRouteScopeObjEntity> endList = objList.stream().filter(o -> TmsConstants.TRANSPORT_SCOPE_TYPE_2.equals(o.getScopeType())).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(endList)) {
            return;
        }

        StringBuilder msg = new StringBuilder();
        for (TmRouteScopeObjEntity startEntity : startList) {
            String startObjCode = startEntity.getTransportObjCode();
            String startObjAddress = startEntity.getTransportObjAddress();
            Point startPoint;
            if (StringUtils.isBlank(startObjCode) || StringUtils.isBlank(startObjAddress)) {
                continue;
            }
            try {
                startPoint = MapUtil.getPoint(startObjAddress);
            } catch (AddressToPointException | IOException e) {
                msg.append("<br>【").append(startEntity.getTransportObjCode()).append("/").append(startEntity.getTransportObjName()).append("】").append(e.getMessage());
                continue;
            }

            for (TmRouteScopeObjEntity endEntity : endList) {
                String endObjCode = endEntity.getTransportObjCode();
                String endObjAddress = endEntity.getTransportObjAddress();

                TmObjRoute tmObjRoute = tmObjRouteService.getByCode(entity.getCarrierCode(), startObjCode, endObjCode, entity.getOrgId());
                if (tmObjRoute == null && StringUtils.isNotBlank(endObjAddress) && !startObjCode.equals(endObjCode)) {
                    Point endPoint;
                    try {
                        endPoint = MapUtil.getPoint(endObjAddress);
                    } catch (AddressToPointException | IOException e) {
                        msg.append("<br>【").append(endEntity.getTransportObjCode()).append("/").append(endEntity.getTransportObjName()).append("】").append(e.getMessage());
                        continue;
                    }
                    Double distance;
                    try {
                        distance = MapUtil.getMaxDirectionDistance(startPoint, endPoint);
                    } catch (RoutePlanningException | IOException e) {
                        msg.append("<br>【").append(startEntity.getTransportObjCode()).append("/").append(startEntity.getTransportObjName()).append("】-【").append(endEntity.getTransportObjCode()).append("/").append(endEntity.getTransportObjName()).append("】，").append(e.getMessage());
                        continue;
                    }
                    tmObjRoute = new TmObjRoute();
                    tmObjRoute.setCarrierCode(entity.getCarrierCode());
                    tmObjRoute.setStartObjCode(startObjCode);
                    tmObjRoute.setStartObjAddress(startObjAddress);
                    tmObjRoute.setEndObjCode(endObjCode);
                    tmObjRoute.setEndObjAddress(endObjAddress);
                    tmObjRoute.setAuditStatus("00");
                    tmObjRoute.setMileage(distance);
                    tmObjRoute.setOrgId(entity.getOrgId());
                    tmObjRouteService.save(tmObjRoute);
                }
            }
        }
        if (StringUtils.isNotBlank(msg)) {
            msg.insert(0, entity.getName() + " 生成业务路由失败记录如下：");
            throw new TmsException(msg.toString());
        }
    }
}