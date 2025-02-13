package com.yunyou.modules.sys.common.web;

import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.common.entity.extend.SysBmsSkuEntity;
import com.yunyou.modules.sys.common.service.SysBmsSkuService;
import com.yunyou.modules.sys.utils.SysDataSetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 结算商品Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/common/bms/sku")
public class SysBmsSkuController extends BaseController {
    @Autowired
    private SysBmsSkuService sysBmsSkuService;

    /**
     * 列表数据(弹出框)
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(SysBmsSkuEntity entity, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isBlank(entity.getDataSet())) {
            entity.setDataSet(SysDataSetUtils.getUserDataSet().getCode());
        }
        return getBootstrapData(sysBmsSkuService.findGrid(new Page<>(request, response), entity));
    }

}