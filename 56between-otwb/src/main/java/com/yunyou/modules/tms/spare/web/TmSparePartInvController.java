package com.yunyou.modules.tms.spare.web;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.spare.entity.extend.TmSparePartInvEntity;
import com.yunyou.modules.tms.spare.service.TmSparePartInvService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping(value = "${adminPath}/tms/spare/inv")
public class TmSparePartInvController extends BaseController {
    @Autowired
    private TmSparePartInvService tmSparePartInvService;

    @ModelAttribute
    public TmSparePartInvEntity get(String id) {
        TmSparePartInvEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmSparePartInvService.getEntity(id);
        }
        if (entity == null) {
            entity = new TmSparePartInvEntity();
        }
        return entity;
    }

    @RequiresPermissions("tms:spare:inv:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/spare/tmSparePartInvList";
    }

    @ResponseBody
    @RequiresPermissions("tms:spare:inv:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmSparePartInvEntity qEntity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmSparePartInvService.findPage(new Page<TmSparePartInvEntity>(request, response), qEntity));
    }
}
