package com.yunyou.modules.bms.basic.web;

import com.alibaba.fastjson.JSON;
import com.yunyou.common.exception.GlobalException;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.common.utils.excel.ExportExcel;
import com.yunyou.common.utils.excel.ImportExcel;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.bms.basic.entity.BmsTransportGroup;
import com.yunyou.modules.bms.basic.entity.BmsTransportPrice;
import com.yunyou.modules.bms.basic.entity.BmsTransportSteppedPrice;
import com.yunyou.modules.bms.basic.entity.extend.BmsTransportGroupEntity;
import com.yunyou.modules.bms.basic.entity.extend.BmsTransportPriceEntity;
import com.yunyou.modules.bms.basic.entity.template.BmsTransportPriceExportTemplate;
import com.yunyou.modules.bms.basic.entity.template.BmsTransportPriceTemplate;
import com.yunyou.modules.bms.basic.service.BmsTransportGroupService;
import com.yunyou.modules.bms.basic.service.BmsTransportPriceService;
import com.yunyou.modules.bms.basic.service.BmsTransportSteppedPriceService;
import com.yunyou.modules.bms.common.BmsException;
import com.google.common.collect.Lists;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 描述：运输体系Controller
 *
 * @author liujianhua created on 2019-11-14
 */
@Controller
@RequestMapping(value = "${adminPath}/bms/basic/bmsTransportGroup")
public class BmsTransportGroupController extends BaseController {
    @Autowired
    private BmsTransportGroupService bmsTransportGroupService;
    @Autowired
    private BmsTransportPriceService bmsTransportPriceService;
    @Autowired
    private BmsTransportSteppedPriceService bmsTransportSteppedPriceService;

    @ModelAttribute
    public BmsTransportGroupEntity get(@RequestParam(required = false) String id) {
        BmsTransportGroupEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = bmsTransportGroupService.getEntity(id);
        }
        if (entity == null) {
            entity = new BmsTransportGroupEntity();
        }
        return entity;
    }

    /**
     * 描述：运输体系列表页面
     *
     * @author liujianhua created on 2019-11-14
     */
    @RequiresPermissions("bms:bmsTransportGroup:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/bms/basic/bmsTransportGroupList";
    }

    /**
     * 描述：运输体系列表数据
     *
     * @author liujianhua created on 2019-11-14
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsTransportGroup:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(BmsTransportGroupEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsTransportGroupService.findPage(new Page<>(request, response), entity));
    }

    /**
     * 描述：查看，增加，编辑运输体系表单页面
     *
     * @author liujianhua created on 2019-11-14
     */
    @RequiresPermissions(value = {"bms:bmsTransportGroup:view", "bms:bmsTransportGroup:add", "bms:bmsTransportGroup:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(BmsTransportGroupEntity entity, Model model) {
        model.addAttribute("bmsTransportGroupEntity", entity);
        return "modules/bms/basic/bmsTransportGroupForm";
    }

    /**
     * 描述：保存运输体系
     *
     * @author liujianhua created on 2019-11-14
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsTransportGroup:add", "bms:bmsTransportGroup:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(BmsTransportGroupEntity entity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsTransportGroupService.save(entity);
            j.put("id", entity.getId());
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存运输体系[" + JSON.toJSONString(entity) + "]", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：批量删除运输体系
     *
     * @author liujianhua created on 2019-11-14
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsTransportGroup:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder message = new StringBuilder("操作成功");
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            try {
                bmsTransportGroupService.delete(new BmsTransportGroup(idArray[i]));
            } catch (BmsException e) {
                message.append("\r\n第").append(i + 1).append("行删除失败，").append(e.getMessage());
            } catch (Exception e) {
                logger.error("删除运输体系[id=" + idArray[i] + "]", e);
                message.append("\r\n第").append(i + 1).append("行删除失败，").append(e.getMessage());
            }
        }
        j.setMsg(message.toString());
        return j;
    }

    /**
     * 描述：运输体系复制
     *
     * @author liujianhua created on 2019-11-20
     */
    @ResponseBody
    @RequiresPermissions("bms:bmsTransportGroup:copy")
    @RequestMapping("copy")
    public AjaxJson copy(String id, String orgId) {
        AjaxJson j = new AjaxJson();
        try {
            bmsTransportGroupService.copy(id, orgId);
        } catch (Exception e) {
            logger.error("【BMS运输体系复制】", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }


    /**
     * 描述：运输体系下的运输价格列表数据
     *
     * @author liujianhua created on 2019-11-14
     */
    @ResponseBody
    @RequestMapping(value = "transportPriceData")
    public Map<String, Object> transportPriceData(BmsTransportPriceEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(bmsTransportPriceService.findByTransportCode(new Page<BmsTransportPriceEntity>(request, response), entity));
    }

    /**
     * 描述：运输体系下的运输价格页面
     *
     * @author liujianhua created on 2019-11-15
     */
    @RequiresPermissions("bms:bmsTransportGroup:list")
    @RequestMapping(value = "transportPriceForm")
    public String transportPriceForm(BmsTransportPriceEntity entity, Model model) {
        if (StringUtils.isNotBlank(entity.getId())) {
            entity = bmsTransportPriceService.getEntity(entity.getId());
        } else {
            entity.setPrice(BigDecimal.ONE);
        }
        model.addAttribute("bmsTransportPriceEntity", entity);
        return "modules/bms/basic/bmsTransportPriceForm";
    }

    /**
     * 描述：运输体系下的运输价格保存
     *
     * @author liujianhua created on 2019-11-15
     */
    @ResponseBody
    @RequiresPermissions(value = {"bms:bmsTransportGroup:add", "bms:bmsTransportGroup:edit"}, logical = Logical.OR)
    @RequestMapping(value = "saveTransportPrice")
    public AjaxJson saveTransportPrice(BmsTransportPriceEntity entity, Model model) {
        AjaxJson j = new AjaxJson();
        if (!beanValidator(model, entity)) {
            j.setSuccess(false);
            j.setMsg("非法参数！");
            return j;
        }
        try {
            bmsTransportPriceService.saveEntity(entity);
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        } catch (Exception e) {
            logger.error("保存运输价格[" + JSON.toJSONString(entity) + "]", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：运输体系下的运输价格删除
     *
     * @author liujianhua created on 2019-11-15
     */
    @ResponseBody
    @RequiresPermissions(value = "bms:bmsTransportGroup:del")
    @RequestMapping(value = "deleteTransportPrice")
    public AjaxJson deleteTransportPrice(String ids) {
        AjaxJson j = new AjaxJson();

        StringBuilder message = new StringBuilder("操作成功");
        String[] idArray = ids.split(",");
        for (int i = 0; i < idArray.length; i++) {
            try {
                bmsTransportPriceService.deleteById(idArray[i]);
            } catch (BmsException e) {
                message.append("\r\n第").append(i + 1).append("行删除失败，").append(e.getMessage());
            } catch (Exception e) {
                logger.error("删除运输价格[id=" + idArray[i] + "]", e);
                message.append("\r\n第").append(i + 1).append("行删除失败，").append(e.getMessage());
            }
        }
        j.setMsg(message.toString());
        return j;
    }


    /**
     * 描述：运输价格-阶梯价格
     *
     * @author liujianhua created on 2019-11-15
     */
    @ResponseBody
    @RequestMapping(value = "transportStepPriceData")
    public Map<String, Object> transportStepPriceData(BmsTransportSteppedPrice entity) {
        Page<BmsTransportSteppedPrice> page = new Page<>();
        page.setList(bmsTransportSteppedPriceService.findByFkId(entity.getFkId()));
        return getBootstrapData(page);
    }

    /**
     * 描述：删除运输价格-阶梯价格
     *
     * @author liujianhua created on 2019-11-15
     */
    @ResponseBody
    @RequestMapping(value = "deleteTransportStepPrice")
    public AjaxJson deleteTransportStepPrice(String transportStepPriceId) {
        AjaxJson j = new AjaxJson();
        try {
            bmsTransportSteppedPriceService.delete(new BmsTransportSteppedPrice(transportStepPriceId));
        } catch (BmsException e) {
            j.setSuccess(false);
            j.setMsg("");
        } catch (Exception e) {
            logger.error("删除运输阶梯价格[id=" + transportStepPriceId + "]", e);
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 描述：查看，增加，编辑运输体系表单页面 - 合同运输价格跳转
     *
     * @author liujianhua created on 2021-9-1
     */
    @RequiresPermissions(value = {"bms:bmsTransportGroup:view", "bms:bmsTransportGroup:add", "bms:bmsTransportGroup:edit"}, logical = Logical.OR)
    @RequestMapping(value = "jump/form")
    public String jumpForm(String transportGroupCode, String orgId, Model model) {
        BmsTransportGroupEntity entity;
        if (StringUtils.isBlank(transportGroupCode) || StringUtils.isBlank(orgId)) {
            entity = new BmsTransportGroupEntity();
        } else {
            entity = bmsTransportGroupService.getByCode(transportGroupCode, orgId);
        }
        model.addAttribute("bmsTransportGroupEntity", entity);
        return "modules/bms/basic/bmsTransportGroupForm";
    }


    /**
     * 描述：下载导入运输价格模板
     */
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            new ExportExcel(null, BmsTransportPriceTemplate.class, 1).setDataList(Lists.newArrayList()).write(response, "运输价格导入模板.xlsx").dispose();
        } catch (IOException e) {
            j.setSuccess(false);
            j.setMsg("模板下载失败");
        }
        return j;
    }

    /**
     * 描述：导入运输价格
     */
    @ResponseBody
    @RequestMapping(value = "import/transportPrice", method = RequestMethod.POST)
    public AjaxJson importFile(@RequestParam("id") String id, @RequestParam("file") MultipartFile file) {
        AjaxJson j = new AjaxJson();
        try {
            ImportExcel ei = new ImportExcel(file, 1, 0);
            bmsTransportPriceService.importFile(bmsTransportGroupService.get(id), ei.getDataList(BmsTransportPriceTemplate.class));
            j.setMsg("导入成功");
        } catch (GlobalException e) {
            j.setSuccess(false);
            j.setMsg("导入失败，" + e.getMessage());
        } catch (Exception e) {
            logger.error("", e);
            j.setSuccess(false);
            j.setMsg("导入失败，" + e.getMessage());
        }
        return j;
    }

    /**
     * 描述：导出运输价格
     */
    @ResponseBody
    @RequestMapping(value = "exportPrice", method = RequestMethod.POST)
    public AjaxJson exportFile(@RequestBody BmsTransportPrice entity, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            List<BmsTransportPriceEntity> list = bmsTransportPriceService.findByFkId(entity.getFkId());
            List<BmsTransportPriceExportTemplate> rsList = Lists.newArrayList();
            for (BmsTransportPriceEntity o : list) {
                BmsTransportPriceExportTemplate template = new BmsTransportPriceExportTemplate();
                template.setStartPlaceCode(o.getStartPlaceCode());
                template.setStartPlaceName(o.getStartPlaceName());
                template.setEndPlaceCode(o.getEndPlaceCode());
                template.setEndPlaceName(o.getEndPlaceName());
                template.setCarTypeCode(o.getCarTypeCode());
                template.setCarType(o.getCarType());
                template.setRegionCode(o.getRegionCode());
                template.setRegionName(o.getRegionName());
                template.setPrice(o.getPrice());
                template.setLogisticsPoints(o.getLogisticsPoints());
                for (int i = 0; i < o.getSteppedPrices().size(); i++) {
                    BmsTransportSteppedPrice steppedPrice = o.getSteppedPrices().get(i);
                    if (i == 0) {
                        template.setFm1(steppedPrice.getFm());
                        template.setTo1(steppedPrice.getTo());
                        template.setPrice1(steppedPrice.getPrice());
                    } else if (i == 1) {
                        template.setFm2(steppedPrice.getFm());
                        template.setTo2(steppedPrice.getTo());
                        template.setPrice2(steppedPrice.getPrice());
                    } else if (i == 2) {
                        template.setFm3(steppedPrice.getFm());
                        template.setTo3(steppedPrice.getTo());
                        template.setPrice3(steppedPrice.getPrice());
                    } else if (i == 3) {
                        template.setFm4(steppedPrice.getFm());
                        template.setTo4(steppedPrice.getTo());
                        template.setPrice4(steppedPrice.getPrice());
                    } else if (i == 4) {
                        template.setFm5(steppedPrice.getFm());
                        template.setTo5(steppedPrice.getTo());
                        template.setPrice5(steppedPrice.getPrice());
                    }
                }
                rsList.add(template);
            }
            new ExportExcel(null, BmsTransportPriceExportTemplate.class).setDataList(rsList).write(response, "运输价格.xlsx").dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出运输价格记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

}