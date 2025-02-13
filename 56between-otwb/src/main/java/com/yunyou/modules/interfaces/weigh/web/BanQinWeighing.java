package com.yunyou.modules.interfaces.weigh.web;

import com.alibaba.fastjson.JSONObject;
import com.yunyou.common.utils.SpringContextHolder;
import com.yunyou.common.utils.StringUtils;
import com.yunyou.modules.interfaces.log.service.InterfaceLogService;
import com.yunyou.modules.interfaces.weigh.entity.BanQinWeighingConstant;
import com.yunyou.modules.interfaces.weigh.entity.BanQinWeighingRequest;
import com.yunyou.modules.interfaces.weigh.entity.BanQinWeighingResultMessage;
import com.yunyou.modules.interfaces.weigh.service.BanQinWeighingInterfaceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 称重设备称重
 *
 * @author zyf
 * @version 2019-07-05
 */
public class BanQinWeighing extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private BanQinWeighingInterfaceService banQinWeighingInterfaceService;
    private InterfaceLogService interfaceLogService;

    public void init() {
        banQinWeighingInterfaceService = SpringContextHolder.getBean("banQinWeighingInterfaceService");
        interfaceLogService = SpringContextHolder.getBean("interfaceLogService");
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");

        BanQinWeighingResultMessage msg = new BanQinWeighingResultMessage();
        // 接收json
        StringBuilder sb = new StringBuilder("");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) request.getInputStream(), StandardCharsets.UTF_8));
            String temp;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
            }
            br.close();
            if (StringUtils.isNotBlank(sb.toString())) {
                BanQinWeighingRequest requestData = JSONObject.parseObject(sb.toString(), BanQinWeighingRequest.class);
                if (requestData == null) {
                    msg.setMsgInfo("解析错误！");
                    msg.setIsSuccess(false);
                } else {
                    msg = banQinWeighingInterfaceService.banQinWeighingHandle(requestData);
                }
            } else {
                msg.setMsgInfo("请求数据为空");
                msg.setIsSuccess(false);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            msg.setMsgInfo("IO异常：");
            msg.setIsSuccess(false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            msg.setMsgInfo(e.getMessage());
            msg.setIsSuccess(false);
        }
        // 构建返回json
        StringBuilder reMsg = new StringBuilder("");
        reMsg.append("{\"CODE\":");
        reMsg.append(msg.getIsSuccess() ? 1 : 0).append(",");
        reMsg.append("\"MSG\":\"");
        reMsg.append(msg.getMsgInfo());
        reMsg.append("\"}");
        // 保存日志
        interfaceLogService.saveLogNew(BanQinWeighingConstant.BQ_WMS_WEIGHING, msg.getIsSuccess() ? "Y" : "N", msg.getMsgInfo(), sb.toString(), reMsg.toString(), "", BanQinWeighingConstant.WEIGH_USER, BanQinWeighingConstant.HANDLE_DIR_RECEIVE);
        // 转成数据流
        InputStream is = new ByteArrayInputStream(reMsg.toString().getBytes());
        // 输出到画面
        ServletOutputStream op = response.getOutputStream();
        int len;
        byte[] buff = new byte[4096];
        while ((len = is.read(buff)) != -1) {
            op.write(buff, 0, len);
        }
        op.flush();
    }
}
