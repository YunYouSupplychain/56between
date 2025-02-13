package com.yunyou.modules.tms.applet.web;

import com.yunyou.common.ResultMessage;
import com.yunyou.common.json.AjaxJson;
import com.yunyou.core.web.BaseController;
import com.yunyou.modules.sys.utils.DictUtils;
import com.yunyou.modules.tms.app.entity.TmAppUserInfo;
import com.yunyou.modules.tms.applet.action.TmAppAction;
import com.yunyou.modules.tms.applet.entity.request.*;
import com.yunyou.modules.tms.common.TmsConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * app后台Controller
 *
 * @author zyf
 * @version 2020-06-30
 */
@Controller
@RequestMapping(value = "/api/tms/app")
public class TmAppController extends BaseController {

    @Autowired
    private TmAppAction action;

    /**
     * app注册
     */
    @ResponseBody
    @PostMapping(value = "appRegister")
    public AjaxJson appRegister(@RequestBody TmAppUserInfo userInfo) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.appRegister(userInfo);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * app登录
     */
    @ResponseBody
    @PostMapping(value = "appLogin")
    public AjaxJson appLogin(@RequestBody TmAppLoginRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.appLogin(request.getCode(), request.getPassword(), request.getLoginType());
        if (message.isSuccess()) {
            j.put("userInfo", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 清除缓存
     */
    @ResponseBody
    @PostMapping(value = "clearCache")
    public AjaxJson clearCache(@RequestBody TmAppLoginRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.appLogin(request.getCode(), request.getPassword(), request.getLoginType());
        if (message.isSuccess()) {
            j.put("userInfo", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 校验用户名
     */
    @ResponseBody
    @PostMapping(value = "checkLoginName")
    public AjaxJson checkLoginName(@RequestBody TmAppLoginRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.checkUser(request.getCode());
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 修改保存用户信息
     */
    @ResponseBody
    @PostMapping(value = "saveUserInfo")
    public AjaxJson saveUserInfo(@RequestBody TmAppUserInfo userInfo) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.saveUserInfo(userInfo);
        if (message.isSuccess()) {
            j.put("userInfo", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 查询数据字典
     */
    @ResponseBody
    @PostMapping(value = "/getDictList")
    public AjaxJson getDictList(@RequestBody TmAppGetDictListRequest request) {
        AjaxJson j = new AjaxJson();

        String[] dictTypes = request.getDictType().split(",");
        for (String dictType : dictTypes) {
            j.put(dictType, DictUtils.getDictList(dictType));
        }
        return j;
    }

    /**
     * 需求下单-查询商品
     */
    @ResponseBody
    @PostMapping(value = "/appCustomerOrder/skuList")
    public AjaxJson querySkuList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.querySkuList(request);
        if (message.isSuccess()) {
            j.put("skuList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 需求下单-生成需求订单
     */
    @ResponseBody
    @PostMapping(value = "/appCustomerOrder/createOrder")
    public AjaxJson createCustomerOrder(@RequestBody TmAppCreateCustomerOrderRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.createCustomerOrder(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 需求订单列表
     */
    @ResponseBody
    @PostMapping(value = "/appCustomerOrder/orderList")
    public AjaxJson customerOrderList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryCustomerOrder(request);
        if (message.isSuccess()) {
            j.put("orderList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 删除需求订单
     */
    @ResponseBody
    @PostMapping(value = "/appCustomerOrder/delete")
    public AjaxJson deleteCustomerOrder(@RequestBody TmAppDeleteCustomerOrderRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.deleteCustomerOrder(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 派车单列表
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/orderList")
    public AjaxJson dispatchOrderList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            ResultMessage message = action.queryDispatchOrder(request);
            if (message.isSuccess()) {
                j.put("orderList", message.getData());
            }
            j.setSuccess(message.isSuccess());
            j.setMsg(message.getMessage());
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg(e.getMessage());
        }
        return j;
    }

    /**
     * 获取派车单信息
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/getDispatchOrder")
    public AjaxJson getDispatchOrder(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.getDispatchOrder(request);
        if (message.isSuccess()) {
            j.put("dispatchOrder", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 获取车辆信息
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/getVehicleInfo")
    @SuppressWarnings("unchecked")
    public AjaxJson getVehicleInfo(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.getVehicleInfo(request);
        if (message.isSuccess()) {
            j.setBody((LinkedHashMap<String, Object>) message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 保存派车单现金费用信息
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/saveCashAmount")
    public AjaxJson saveDispatchOrderCashAmount(@RequestBody TmAppDispatchOrderSaveRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.saveDispatchOrderCashAmount(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 获取出车安全检查表
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/getVehicleSafetyCheck")
    public AjaxJson getVehicleSafetyCheck(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.getVehicleSafetyCheckIntraday(request);
        if (message.isSuccess()) {
            j.put("vehicleSafetyCheck", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 保存出车安全检查表
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/saveVehicleSafetyCheck")
    public AjaxJson saveVehicleSafetyCheck(@RequestBody TmAppCreateVehicleSafetyCheckRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.saveVehicleSafetyCheck(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 发车
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/depart")
    public AjaxJson depart(@RequestBody TmAppDepartRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.depart(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 派车单关闭
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/finish")
    public AjaxJson finish(@RequestBody TmAppDispatchOrderFinishRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.finish(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 派车单配送点列表
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/siteList")
    public AjaxJson dispatchOrderSiteList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryDispatchOrderSiteList(request);
        if (message.isSuccess()) {
            j.put("siteList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 派车单运输订单列表
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/transportOrderList")
    public AjaxJson dispatchTransportOrderList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryDispatchTransportOrderList(request);
        if (message.isSuccess()) {
            j.put("orderList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 派车单运输订单商品列表
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/transportOrderDetailList")
    public AjaxJson dispatchTransportOrderDetailList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryDispatchTransportOrderDetailList(request);
        if (message.isSuccess()) {
            j.put("detailList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 派车单标签列表
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/labelList")
    public AjaxJson dispatchLabelList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryDispatchLabelList(request);
        if (message.isSuccess()) {
            j.put("labelList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 网点交接
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/handoverConfirm")
    public AjaxJson handoverConfirm(@RequestBody TmAppHandoverConfirmRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.handoverConfirm(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 用户揽收/签收
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/receiveOrSignConfirm")
    public AjaxJson receiveOrSignConfirm(@RequestBody TmAppHandoverConfirmRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.receiveOrSignConfirm(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 图片上传
     */
    @ResponseBody
    @PostMapping(value = "/photoUpload")
    public AjaxJson photoUpload(HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String fileType = params.getParameter("fileType");      // 文件类型
        String preName = params.getParameter("preName");        // 文件名前缀
        String orderNo = params.getParameter("orderNo");        // 关联单号
        String dispatchNo = params.getParameter("dispatchNo");  // 派车单号 （派车单号 + 网点编码 取交接单号）
        String outletCode = params.getParameter("outletCode");  // 网点编码
        String labelNo = params.getParameter("labelNo");  // 标签号
        String userId = params.getParameter("userId");
        String imgUrl = params.getParameter("imgUrl");
        String index = params.getParameter("index");
        MultipartFile file;
        BufferedOutputStream stream;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (file.isEmpty()) {
                j.setSuccess(false);
                j.setMsg("第" + i + "个文件为空！");
                continue;
            }
            try {
                byte[] bytes = file.getBytes();
                // 生成jpeg图片
                String os = System.getProperty("os.name");
                String prePath;
                if (os != null && os.toLowerCase().contains("linux")) {
                    prePath = System.getProperty("user.dir").replace("bin", "webapps");
                } else {
                    prePath = System.getProperty("user.dir");
                }
                // 照片路径增加日期
                String ymd = new SimpleDateFormat("yyyyMMdd").format(new Date());
                // 照片目录相对路径
                String filePath = TmsConstants.IMG_UPLOAD_PRE + TmsConstants.LINUX_SEPARATOR + fileType + TmsConstants.LINUX_SEPARATOR + ymd;
                // 文件对象，照片目录绝对路径
                File updateLoadFile = new File(prePath + filePath);
                if (!updateLoadFile.exists()) {//不存在则创建文件夹
                    updateLoadFile.mkdirs();
                }
                // 照片文件名（单据号 + 000 + 照片序号.png）
                String fileName = preName + "000" + index + ".png";
                // 照片相对路径
                String imgFilePath = filePath + TmsConstants.LINUX_SEPARATOR + fileName;
                stream = new BufferedOutputStream(Files.newOutputStream(new File(prePath + imgFilePath).toPath()));
                stream.write(bytes);
                stream.close();
                // 图片上传结束，插入附件表信息
                TmAppAttachementRequest saveRequest = new TmAppAttachementRequest();
                saveRequest.setUserId(userId);
                saveRequest.setOrderType(fileType);
                saveRequest.setDispatchNo(dispatchNo);
                saveRequest.setOutletCode(outletCode);
                saveRequest.setOrderNo(orderNo);
                saveRequest.setLabelNo(labelNo);
                saveRequest.setFileName(fileName);
                saveRequest.setImgFilePath(imgFilePath);
                saveRequest.setImgUrl(imgUrl);
                action.saveTmAttachement(saveRequest);
            } catch (Exception e) {
                j.setSuccess(false);
                j.setMsg("第" + i + "个文件上传失败！" + e.getMessage());
            }
        }
        return j;
    }

    /**
     * 异常上报
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/exceptionConfirm")
    public AjaxJson exceptionConfirm(@RequestBody TmAppExceptionConfirmRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.exceptionConfirm(request);
        if (message.isSuccess()) {
            j.put("order", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 异常处理单列表
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/exceptionHandleBillList")
    public AjaxJson queryExceptionHandleBillList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryExceptionHandleBill(request);
        if (message.isSuccess()) {
            j.put("orderList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 交接单列表
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/handoverOrderList")
    public AjaxJson handoverOrderList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryHandoverOrder(request);
        if (message.isSuccess()) {
            j.put("orderList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 查询车辆列表
     */
    @ResponseBody
    @PostMapping(value = "/appRepairOrder/vehicleList")
    public AjaxJson queryVehicleList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryVehicleListAll(request);
        if (message.isSuccess()) {
            j.put("vehicleList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 保存维修工单信息
     */
    @ResponseBody
    @PostMapping(value = "/appRepairOrder/saveRepairOrder")
    public AjaxJson saveRepairOrder(@RequestBody TmAppSaveRepairOrderRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.saveRepairOrder(request);
        if (message.isSuccess()) {
            j.put("order", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 维修单列表
     */
    @ResponseBody
    @PostMapping(value = "/appRepairOrder/orderList")
    public AjaxJson repairOrderList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryRepairOrder(request);
        if (message.isSuccess()) {
            j.put("orderList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 维修完成确认
     */
    @ResponseBody
    @PostMapping(value = "/appRepairOrder/finishRepairOrder")
    public AjaxJson finishRepairOrder(@RequestBody TmAppSaveRepairOrderRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.finishRepairOrder(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 获取派车装罐信息
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/getDispatchTankInfo")
    public AjaxJson getDispatchTankInfo(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.getDispatchTankInfo(request);
        if (message.isSuccess()) {
            j.put("dispatchTankInfo", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 保存派车装罐信息
     */
    @ResponseBody
    @PostMapping(value = "/appDispatchOrder/saveDispatchTankInfo")
    public AjaxJson saveDispatchTankInfo(@RequestBody TmAppCreateDispatchTankInfoRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.saveDispatchTankInfo(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 司机工资列表(每月)
     */
    @ResponseBody
    @PostMapping(value = "/salary/driverSalaryList")
    @SuppressWarnings("unchecked")
    public AjaxJson driverSalaryList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.driverSalaryList(request);
        if (message.isSuccess()) {
            j.setBody((LinkedHashMap<String, Object>) message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 司机工资明细（日）
     */
    @ResponseBody
    @PostMapping(value = "/salary/driverSalaryDayInfo")
    @SuppressWarnings("unchecked")
    public AjaxJson driverSalaryDayInfo(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.driverSalaryDayInfo(request);
        if (message.isSuccess()) {
            j.setBody((LinkedHashMap<String, Object>) message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 安全检查列表
     */
    @ResponseBody
    @PostMapping(value = "/appSafetyCheck/checkList")
    public AjaxJson checkList(@RequestBody TmAppOrderQueryRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.queryCheckList(request);
        if (message.isSuccess()) {
            j.put("orderList", message.getData());
        }
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }

    /**
     * 安全检查确认
     */
    @ResponseBody
    @PostMapping(value = "/appSafetyCheck/confirm")
    public AjaxJson confirm(@RequestBody TmAppCreateVehicleSafetyCheckRequest request) {
        AjaxJson j = new AjaxJson();

        ResultMessage message = action.safetyCheckConfirm(request);
        j.setSuccess(message.isSuccess());
        j.setMsg(message.getMessage());
        return j;
    }
}