package com.yunyou.modules.sys.common.web;

import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.SysDataSetOrgRelation;
import com.yunyou.modules.sys.common.entity.extend.SysDataSetOrgRelationEntity;
import com.yunyou.modules.sys.common.service.SysDataSetOrgRelationService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 数据套机构关系Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/dataSetOrgRelation")
public class SysDataSetOrgRelationController extends BaseController {
    @Autowired
    private SysDataSetOrgRelationService sysDataSetOrgRelationService;

    /**
     * 批量删除
     */
    @ResponseBody
    @RequiresPermissions("sys:common:dataSetOrgRelation:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            sysDataSetOrgRelationService.delete(new SysDataSetOrgRelation(id));
        }
        j.setMsg("删除成功");
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysDataSetOrgRelationEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysDataSetOrgRelationService.findGrid(new Page<SysDataSetOrgRelationEntity>(request, response), entity));
    }

}