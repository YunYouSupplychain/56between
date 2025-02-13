package com.yunyou.modules.tms.order.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.core.persistence.Page;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.tms.common.TmsConstants;
import com.yunyou.modules.tms.order.action.TmExceptionHandleBillAction;
import com.yunyou.modules.tms.order.entity.TmAttachementDetail;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillDetailEntity;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillEntity;
import com.yunyou.modules.tms.order.entity.extend.TmExceptionHandleBillFeeEntity;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 异常处理单Controller
 *
 * @author ZYF
 * @version 2020-08-04
 */
@Controller
@RequestMapping(value = "${adminPath}/tms/order/tmExceptionHandleBill")
public class TmExceptionHandleBillController extends BaseController {
    @Autowired
    private TmExceptionHandleBillAction tmExceptionHandleBillAction;

    @ModelAttribute
    public TmExceptionHandleBillEntity get(@RequestParam(required = false) String id) {
        TmExceptionHandleBillEntity entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = tmExceptionHandleBillAction.getEntity(id);
        }
        if (entity == null) {
            entity = new TmExceptionHandleBillEntity();
        }
        return entity;
    }

    @RequiresPermissions("tms:order:tmExceptionHandle:list")
    @RequestMapping(value = {"list", ""})
    public String list() {
        return "modules/tms/order/tmExceptionHandleBillList";
    }

    @RequiresPermissions(value = {"tms:order:tmExceptionHandle:view", "tms:order:tmExceptionHandle:add", "tms:order:tmExceptionHandle:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(TmExceptionHandleBillEntity entity, Model model) {
        model.addAttribute("tmExceptionHandleBillEntity", entity);
        return "modules/tms/order/tmExceptionHandleBillForm";
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmExceptionHandle:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(TmExceptionHandleBillEntity entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmExceptionHandleBillAction.findPage(new Page<>(request, response), entity));
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:tmExceptionHandle:add", "tms:order:tmExceptionHandle:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(TmExceptionHandleBillEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage message = tmExceptionHandleBillAction.saveEntity(entity);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmExceptionHandle:del")
    @PostMapping(value = "deleteAll")
    public AjaxJson deleteAll(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmExceptionHandleBillAction.batchRemove(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmExceptionHandle:detail:data")
    @RequestMapping(value = "/detail/data")
    public List<TmExceptionHandleBillDetailEntity> findList(TmExceptionHandleBillDetailEntity qEntity) {
        return tmExceptionHandleBillAction.findDetailList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:tmExceptionHandle:detail:add", "tms:order:tmExceptionHandle:detail:save"}, logical = Logical.OR)
    @RequestMapping(value = "/detail/save")
    public AjaxJson detailSave(TmExceptionHandleBillEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage message = tmExceptionHandleBillAction.saveDetailList(entity.getTmExceptionHandleBillDetailList());
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmExceptionHandle:detail:del")
    @PostMapping(value = "/detail/delete")
    public AjaxJson detailDelete(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmExceptionHandleBillAction.batchRemoveDetails(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmExceptionHandle:fee:data")
    @RequestMapping(value = "/feeDetail/data")
    public List<TmExceptionHandleBillFeeEntity> findFeeList(TmExceptionHandleBillFeeEntity qEntity) {
        return tmExceptionHandleBillAction.findFeeDetailList(qEntity);
    }

    @ResponseBody
    @RequiresPermissions(value = {"tms:order:tmExceptionHandle:fee:add", "tms:order:tmExceptionHandle:fee:save"}, logical = Logical.OR)
    @RequestMapping(value = "/feeDetail/save")
    public AjaxJson feeDetailSave(TmExceptionHandleBillEntity entity) {
        AjaxJson j = new AjaxJson();
        ResultMessage message = tmExceptionHandleBillAction.saveFeeDetailList(entity.getTmExceptionHandleBillFeeList());
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        j.put("entity", message.getData());
        return j;
    }

    @ResponseBody
    @RequiresPermissions("tms:order:tmExceptionHandle:fee:del")
    @PostMapping(value = "/feeDetail/delete")
    public AjaxJson feeDetailDelete(@RequestParam("ids") String ids) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = tmExceptionHandleBillAction.batchRemoveFeeDetails(ids.split(","));
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 照片上传
     */
    @ResponseBody
    @PostMapping("uploadPic")
    public AjaxJson uploadPic(@RequestParam(value = "file") MultipartFile file,
                              @RequestParam(value = "billNo") String billNo,
                              @RequestParam(value = "orgId") String orgId,
                              HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        if (file == null) {// 判断上传的文件是否为空
            j.setMsg("没有找到相对应的文件");
            return j;
        }
        String type;// 文件类型
        String fileName = file.getOriginalFilename();// 文件原名称
        // 判断文件类型
        type = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : null;
        if (type == null) {// 判断文件类型是否为空
            j.setMsg("文件类型为空");
            return j;
        }
        boolean isGif = "GIF".equalsIgnoreCase(type);
        boolean isPng = "PNG".equalsIgnoreCase(type);
        boolean isJpg = "JPG".equalsIgnoreCase(type);
        if (!isGif && !isPng && !isJpg) {
            j.setMsg("文件类型不对,请按要求重新上传");
            return j;
        }
        // 生成jpeg图片
        String os = System.getProperty("os.name");
        String prePath;
        if (os != null && os.toLowerCase().contains("linux")) {
            prePath = System.getProperty("catalina.base") + File.separator + "webapps";
        } else {
            prePath = System.getProperty("user.dir");
        }
        // 照片路径增加日期
        Date date = new Date();
        String ymd = new SimpleDateFormat("yyyyMMdd").format(date);
        // 照片目录相对路径
        String filePath = TmsConstants.IMG_UPLOAD_PRE + File.separator + "EXCEPTION" + File.separator + ymd;
        // 文件对象，使用绝对路径
        File updateLoadFile = new File(prePath + filePath);
        if (!updateLoadFile.exists()) {//不存在则创建文件夹
            updateLoadFile.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        // 照片文件名（单据号 + 000 + 照片序号.png）
        String newFileName = billNo + sdf.format(date) + "." + type;
        // 照片相对路径
        String imgFilePath = filePath + File.separator + billNo + sdf.format(date) + "." + type;
        try {
            file.transferTo(new File(prePath + imgFilePath));
        } catch (IllegalStateException | IOException e) {
            logger.error("照片上传失败", e);
            j.setMsg("目录不存在。上传失败");
            return j;
        }
        String imgUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        tmExceptionHandleBillAction.saveImgDetail(billNo, orgId, newFileName, imgFilePath, imgUrl);
        j.setMsg("上传成功");
        return j;
    }

    /**
     * 显示上传图片数据
     */
    @ResponseBody
    @RequestMapping("getPicDetail")
    public Map<String, Object> getPicDetail(TmAttachementDetail entity, HttpServletRequest request, HttpServletResponse response) {
        return getBootstrapData(tmExceptionHandleBillAction.findPicPage(new Page<>(request, response), entity));
    }
}