package com.yunyou.modules.interfaces.sfExpress.entity.sfCreateOrder.response;

import java.util.List;

public class RlsInfo {

    /*
        返回调用结果，ERR：调用失败；OK 调用成功
     */
    private String invokeResult;
    /*
        0000（接口参数异常）
        0010（其它异常）
        0001（xml 解析异常）
        0002（字段校验异常）
        0003（票数节点超出最大值，批量请求最大票数为 100 票）
        0004（RLS 获取路由标签的必要字段为空）
        1000 成功
     */
    private String rlsCode;
    private String errorDesc;           // 错误信息
    private List<RlsDetail> rlsDetail;  // 返回结果

    public String getInvokeResult() {
        return invokeResult;
    }

    public void setInvokeResult(String invokeResult) {
        this.invokeResult = invokeResult;
    }

    public String getRlsCode() {
        return rlsCode;
    }

    public void setRlsCode(String rlsCode) {
        this.rlsCode = rlsCode;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public List<RlsDetail> getRlsDetail() {
        return rlsDetail;
    }

    public void setRlsDetail(List<RlsDetail> rlsDetail) {
        this.rlsDetail = rlsDetail;
    }
}
