package com.yunyou.modules.wms.basicdata.web;

import java.util.List;
import java.util.Map;

import com.yunyou.core.persistence.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yunyou.core.web.BaseController;
import com.yunyou.modules.wms.basicdata.entity.BanQinCdWhLotDetail;
import com.yunyou.modules.wms.basicdata.service.BanQinCdWhLotDetailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 批次属性明细Controller
 * @author WMJ
 * @version 2019-01-25
 */
@Controller
@RequestMapping(value = "${adminPath}/wms/basicdata/banQinCdWhLotDetail")
public class BanQinCdWhLotDetailController extends BaseController {

	@Autowired
	private BanQinCdWhLotDetailService banQinCdWhLotDetailService;
	
    /**
	 * 批次属性明细列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public List<BanQinCdWhLotDetail> data(BanQinCdWhLotDetail banQinCdWhLotDetail) {
	    return banQinCdWhLotDetailService.findList(banQinCdWhLotDetail);
	}

    /**
     * 批次属性列表数据
     */
    @ResponseBody
    @RequestMapping(value = "grid")
    public Map<String, Object> grid(BanQinCdWhLotDetail banQinCdWhLotDetail, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<BanQinCdWhLotDetail> page = banQinCdWhLotDetailService.findPage(new Page<BanQinCdWhLotDetail>(request, response), banQinCdWhLotDetail);
        return getBootstrapData(page);
    }
	
    /**
     * 批次属性明细初始化
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "initialList")
	public List<BanQinCdWhLotDetail> initialList() {
        return banQinCdWhLotDetailService.initialList();
    }

    /**
     * 获取批次属性控件
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getInfo")
    public List<BanQinCdWhLotDetail> getInfo(String ownerCode, String skuCode, String orgId) {
        return banQinCdWhLotDetailService.getInfo(ownerCode, skuCode, orgId);
    }

}