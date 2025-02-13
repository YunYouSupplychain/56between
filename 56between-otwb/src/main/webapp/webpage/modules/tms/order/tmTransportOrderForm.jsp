<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输订单信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmTransportOrderForm.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER">
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="customerType" value="OWNER">
    <input type="hidden" id="outletType" value="OUTLET">
    <input type="hidden" id="carrierType" value="CARRIER">
    <input type="hidden" id="shipType" value="SHIP">
    <input type="hidden" id="consigneeType" value="CONSIGNEE">
    <input type="hidden" id="dispatchType" value="6">
</div>
<div style="margin-left: 10px;">
    <shiro:hasPermission name="order:tmTransportOrder:edit">
        <a id="saveOrder" class="btn btn-primary" disabled onclick="saveOrder()"> 保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmTransportOrder:audit">
        <a id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmTransportOrder:cancelAudit">
        <a id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()"> 取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="tms:order:transport:label:add">
        <button id="addLabel" class="btn btn-primary" disabled onclick="openAddLabelModal()"> 添加标签</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmTransportOrder:receive">
        <a id="receive" class="btn btn-primary" disabled onclick="receive()"> 揽收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmTransportOrder:cancelReceive">
        <a id="cancelReceive" class="btn btn-primary" disabled onclick="cancelReceive()"> 取消揽收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmTransportOrder:sign">
        <a id="sign" class="btn btn-primary" disabled onclick="openSignModal()"> 签收</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmTransportOrder:receipt">
        <a id="receipt" class="btn btn-primary" disabled onclick="openReceiptModal()"> 回单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmTransportOrder:annex">
        <a id="annex" class="btn btn-primary btn-sm" onclick="annex()">附件</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="tmTransportOrderEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="dataSource"/>
    <form:hidden path="orderDelivery.id"/>
    <form:hidden path="orderDelivery.orgId"/>
    <form:hidden path="orderDelivery.baseOrgId"/>
    <form:hidden path="orderDelivery.recVer"/>
    <sys:message content="${message}"/>
    <%--基本信息、配送信息、费用信息--%>
    <div class="tabs-container" style="min-height: 250px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#basicInfo" aria-expanded="true">基本信息</a></li>
            <li class=""><a data-toggle="tab" href="#dispatchInfo" aria-expanded="true">配送信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tr>
                        <td class="width-10"><label class="pull-right">运输单号</label></td>
                        <td class="width-15">
                            <form:input path="transportNo" htmlEscape="false" class="form-control" maxlength="20" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">订单时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='orderTime'>
                                <input type='text' name="orderTime" class="form-control required"
                                       value="<fmt:formatDate value="${tmTransportOrderEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">订单类型</label></td>
                        <td class="width-15">
                            <form:select path="orderType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TRANSPORT_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">订单状态</label></td>
                        <td class="width-15">
                            <form:select path="orderStatus" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TRANSPORT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">客户</label></td>
                        <td class="width-15">
                            <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="customerCode" fieldName="customerCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportOrderEntity.customerCode}"
                                      displayFieldId="customerName" displayFieldName="customerName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportOrderEntity.customerName}"
                                      fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                      cssClass="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">运输方式</label></td>
                        <td class="width-15">
                            <form:select path="transportMethod" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TRANSPORT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">交货方式</label></td>
                        <td class="width-15">
                            <form:select path="deliveryMethod" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_DELIVERY_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">预计到达时间</label></td>
                        <td class="width-15">
                            <div class='input-group date form_datetime' id='planArriveTime'>
                                <input type='text' name="orderDelivery.planArriveTime" class="form-control"
                                       value="<fmt:formatDate value="${tmTransportOrderEntity.orderDelivery.planArriveTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">提货网点</label></td>
                        <td class="width-15">
                            <sys:grid title="选择提货网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="receiveOutletCode" fieldName="receiveOutletCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportOrderEntity.receiveOutletCode}"
                                      displayFieldId="receiveOutletName" displayFieldName="receiveOutletName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportOrderEntity.receiveOutletName}"
                                      fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">配送网点</label></td>
                        <td class="width-15">
                            <sys:grid title="网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="outletCode" fieldName="outletCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportOrderEntity.outletCode}"
                                      displayFieldId="outletName" displayFieldName="outletName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportOrderEntity.outletName}"
                                      fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">委托方</label></td>
                        <td class="width-15">
                            <sys:grid title="委托方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="principalCode" fieldName="principalCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportOrderEntity.principalCode}"
                                      displayFieldId="principalName" displayFieldName="principalName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportOrderEntity.principalName}"
                                      fieldLabels="委托方编码|委托方名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="委托方编码|委托方名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="principalType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">客户单号</label></td>
                        <td class="width-15">
                            <form:input path="customerNo" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">物流单号</label></td>
                        <td class="width-15">
                            <form:input path="trackingNo" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                        <td class="width-10"><label class="pull-right">来源单号</label></td>
                        <td class="width-15">
                            <form:input path="sourceNo" htmlEscape="false" class="form-control" maxlength="20" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">回单份数</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.receiptCount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)" maxlength="35"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">备注信息</label></td>
                        <td colspan="7">
                            <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="255"/>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="dispatchInfo" class="tab-pane fade">
                <table class="table well">
                    <tr>
                        <td class="width-10"><label class="pull-right">承运商</label></td>
                        <td class="width-15">
                            <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="carrierCode" fieldName="orderDelivery.carrierCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportOrderEntity.orderDelivery.carrierCode}"
                                      displayFieldId="carrierName" displayFieldName="orderDelivery.carrierName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportOrderEntity.orderDelivery.carrierName}"
                                      fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="carrierType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">车型</label></td>
                        <td class="width-15">
                            <sys:grid title="车型" url="${ctx}/tms/basic/tmVehicleType/grid"
                                      fieldId="carType" fieldName="orderDelivery.carType"
                                      fieldKeyName="code" fieldValue="${tmTransportOrderEntity.orderDelivery.carType}"
                                      displayFieldId="carTypeName" displayFieldName="carTypeName"
                                      displayFieldKeyName="name" displayFieldValue="${tmTransportOrderEntity.orderDelivery.carTypeName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"
                                      queryParams="orgId" queryParamValues="baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">补充说明</label></td>
                        <td colspan="3">
                            <form:input path="orderDelivery.remarks" htmlEscape="false" class="form-control" maxlength="255"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">件数</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.totalEaQty" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)" maxlength="18" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">托盘数</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.totalPlQty" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)" maxlength="18"/>
                        </td>
                        <td class="width-10"><label class="pull-right">重量</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.totalWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)" maxlength="18" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">体积</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.totalCubic" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)" maxlength="18" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">到付金额</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.paidAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)" maxlength="18"/>
                        </td>
                        <td class="width-10"><label class="pull-right">实际发货重量</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.actualShipWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)" maxlength="18"/>
                        </td>
                        <td class="width-10"><label class="pull-right">虚拟开单</label></td>
                        <td class="width-15">
                            <form:select path="orderDelivery.virtualBillingStatus" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">是否异常</label></td>
                        <td class="width-15">
                            <form:select path="orderDelivery.isException" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">签收状态</label></td>
                        <td class="width-15">
                            <form:select path="orderDelivery.signStatus" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">签收时间</label></td>
                        <td class="width-15">
                            <div class='input-group date form_datetime' id='signTime'>
                                <input type='text' name="orderDelivery.signTime" class="form-control" readonly
                                       value="<fmt:formatDate value="${tmTransportOrderEntity.orderDelivery.signTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">签收人</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.signBy" htmlEscape="false" class="form-control" readonly="true" maxlength="35"/>
                        </td>
                        <td class="width-10"><label class="pull-right">签收备注</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.signRemarks" htmlEscape="false" class="form-control" readonly="true" maxlength="255"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">回单状态</label></td>
                        <td class="width-15">
                            <form:select path="orderDelivery.receiptStatus" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">回单时间</label></td>
                        <td class="width-15">
                            <div class='input-group date form_datetime' id='receiptTime'>
                                <input type='text' name="orderDelivery.receiptTime" class="form-control" readonly
                                       value="<fmt:formatDate value="${tmTransportOrderEntity.orderDelivery.receiptTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">回单人</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.receiptBy" htmlEscape="false" class="form-control" readonly="true" maxlength="35"/>
                        </td>
                        <td class="width-10"><label class="pull-right">回单备注</label></td>
                        <td class="width-15">
                            <form:input path="orderDelivery.receiptRemarks" htmlEscape="false" class="form-control" readonly="true" maxlength="255"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <div class="tabs-container" style="min-height: 200px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#transportInfo" aria-expanded="true">运输信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="transportInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tr>
                        <td class="width-10"><label class="pull-right">发货方</label></td>
                        <td class="width-15">
                            <sys:grid title="发货方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="shipCode" fieldName="shipCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportOrderEntity.shipCode}"
                                      displayFieldId="shipName" displayFieldName="shipName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportOrderEntity.shipName}"
                                      fieldLabels="发货方编码|发货方名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="发货方编码|发货方名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="shipType|baseOrgId"
                                      cssClass="form-control" afterSelect="shipSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发货城市</label></td>
                        <td colspan="3">
                            <sys:area id="shipCityId" name="shipCityId" value="${tmTransportOrderEntity.shipCityId}"
                                      labelName="shipCity" labelValue="${tmTransportOrderEntity.shipCity}"
                                      cssClass="form-control" allowSearch="true" showFullName="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发货城市名称</label></td>
                        <td class="width-15">
                            <form:input path="shipCityName" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">发货人</label></td>
                        <td class="width-15">
                            <form:input path="shipper" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发货人电话</label></td>
                        <td class="width-15">
                            <form:input path="shipperTel" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发货地址</label></td>
                        <td colspan="3">
                            <form:input path="shipAddress" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货方</label></td>
                        <td class="width-15">
                            <sys:grid title="收货方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="consigneeCode" fieldName="consigneeCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmTransportOrderEntity.consigneeCode}"
                                      displayFieldId="consigneeName" displayFieldName="consigneeName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmTransportOrderEntity.consigneeName}"
                                      fieldLabels="收货方编码|收货方名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="收货方编码|收货方名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="consigneeType|baseOrgId"
                                      cssClass="form-control" afterSelect="consigneeSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">目的地城市</label></td>
                        <td class="width-15" colspan="3">
                            <sys:area id="consigneeCityId" name="consigneeCityId" value="${tmTransportOrderEntity.consigneeCityId}"
                                      labelName="consigneeCity" labelValue="${tmTransportOrderEntity.consigneeCity}"
                                      cssClass="form-control" allowSearch="true" showFullName="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货城市名称</label></td>
                        <td class="width-15">
                            <form:input path="consigneeCityName" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货人</label></td>
                        <td class="width-15">
                            <form:input path="consignee" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人电话</label></td>
                        <td class="width-15">
                            <form:input path="consigneeTel" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货地址</label></td>
                        <td class="width-15" colspan="3">
                            <form:input path="consigneeAddress" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">指定签收人</label></td>
                        <td class="width-15">
                            <form:input path="designatedSignBy" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
<%--订单明细信息、订单标签信息--%>
<div class="tabs-container" style="margin-left: 10px;max-height: 500px;">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#orderDetailInfo" aria-expanded="true">订单明细信息</a></li>
        <li class=""><a data-toggle="tab" href="#orderLabelInfo" aria-expanded="true">订单标签信息</a></li>
    </ul>
    <div class="tab-content">
        <%--订单明细信息--%>
        <div id="orderDetailInfo" class="tab-pane fade in active">
            <div id="orderDetailToolbar" style="margin: 10px 0;">
                <shiro:hasPermission name="tms:order:transport:sku:add">
                    <a id="orderDetail_add" class="btn btn-primary" disabled onclick="addOrderDetail()"> 新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:transport:sku:save">
                    <a id="orderDetail_save" class="btn btn-primary" disabled onclick="saveOrderDetail()"> 保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:transport:sku:del">
                    <a id="orderDetail_remove" class="btn btn-danger" disabled onclick="delOrderDetail()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <div class="fixed-table-container" style="padding-bottom: 0;">
                <form id="orderDetailForm">
                    <table id="orderDetailTable" class="table text-nowrap" data-toolbar="#orderDetailToolbar">
                        <thead>
                        <tr>
                            <th class="hide"></th>
                            <th style="width:36px;vertical-align:middle">
                                <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                            </th>
                            <th class="asterisk">物料编码</th>
                            <th>物料名称</th>
                            <th class="asterisk">数量</th>
                            <th>重量</th>
                            <th>体积</th>
                        </tr>
                        </thead>
                        <tbody id="tmTransportOrderSkuList">
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
        <%--订单标签信息--%>
        <div id="orderLabelInfo" class="tab-pane fade">
            <div id="orderLabelToolbar">
                <shiro:hasPermission name="tms:order:transport:label:data">
                    <a id="orderLabel_show" class="btn btn-primary" disabled onclick="showLabel()"> 查看标签</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:transport:label:del">
                    <a id="orderLabel_del" class="btn btn-danger" disabled onclick="delLabel()"> 删除标签</a>
                </shiro:hasPermission>
            </div>
            <table id="orderLabelTable" class="text-nowrap" data-toolbar="#orderLabelToolbar"></table>
        </div>
    </div>
</div>
<%--添加标签--%>
<div id="addLabelModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:1200px; height: 500px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">添加标签</h4>
            </div>
            <div class="modal-body">
                <form id="addLabelForm" class="form-horizontal">
                    <table class="table table-striped well table-condensed">
                        <tr>
                            <td style="width: 10%;"><label class="pull-right asterisk">标签件数</label></td>
                            <td style="width: 15%;">
                                <input id="labelQty" name="labelQty" class="form-control required" maxlength="18" onkeyup="bq.numberValidator(this, 0, 0)"/>
                            </td>
                            <td style="width: 10%;"><label class="pull-right">商品</label></td>
                            <td style="width: 15%;">
                                <input type="hidden" id="addLabel_transportNo"/>
                                <input type="hidden" id="addLabel_orgId"/>
                                <input type="hidden" id="lineNo"/>
                                <sys:grid title="选择商品" url="${ctx}/tms/order/transport/sku/page" cssClass="form-control"
                                          fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode"
                                          displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName"
                                          fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                          searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                          queryParams="transportNo|orgId" queryParamValues="addLabel_transportNo|addLabel_orgId"
                                          afterSelect="afterSelectSku"/>
                            </td>
                            <td style="width: 10%;"><label class="pull-right">总数量</label></td>
                            <td style="width: 15%;">
                                <input id="totalQty" name="totalQty" class="form-control" maxlength="18" onkeyup="bq.numberValidator(this, 4, 0)"/>
                            </td>
                            <td style="width: 10%;"><label class="pull-right">总重量</label></td>
                            <td style="width: 15%;">
                                <input id="totalWeight" name="totalWeight" class="form-control" maxlength="18" onkeyup="bq.numberValidator(this, 4, 0)"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 10%;"><label class="pull-right">总体积</label></td>
                            <td style="width: 15%;">
                                <input id="totalCubic" name="totalCubic" class="form-control" maxlength="18" onkeyup="bq.numberValidator(this, 4, 0)"/>
                            </td>
                            <td style="width: 10%;"></td>
                            <td style="width: 15%;"></td>
                            <td style="width: 10%;"></td>
                            <td style="width: 15%;"></td>
                            <td style="width: 10%;"></td>
                            <td style="width: 15%;"></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="addLabel()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%--签收--%>
<div id="signModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:1200px; height: 500px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">签收信息</h4>
            </div>
            <div class="modal-body">
                <form id="signForm" class="form-horizontal">
                    <table class="table table-striped well table-condensed">
                        <tr>
                            <td style="width: 13%;"><label class="pull-right asterisk">签收人</label></td>
                            <td style="width: 20%;">
                                <label for="sign_signBy"><input id="sign_signBy" name="signBy" class="form-control required" maxlength="35"/></label>
                            </td>
                            <td style="width: 13%;"><label class="pull-right asterisk">签收时间</label></td>
                            <td style="width: 20%;">
                                <div class='input-group date form_datetime' id='sign_signTime'>
                                    <input type='text' name="sign_signTime" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td style="width: 13%;"><label class="pull-right">备注</label></td>
                            <td style="width: 20%;">
                                <label for="remarks"><input id="sign_remarks" name="sign_remarks" class="form-control" maxlength="255"/></label>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="sign()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%--回单--%>
<div id="receiptModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:1200px; height: 500px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">回单信息</h4>
            </div>
            <div class="modal-body">
                <form id="receiptForm" class="form-horizontal">
                    <table class="table well">
                        <tr>
                            <td style="width: 10%;"><label class="pull-right asterisk">回单人</label></td>
                            <td style="width: 15%;">
                                <label for="receipt_receiptBy"><input id="receipt_receiptBy" name="receiptBy" class="form-control required" maxlength="35"/></label>
                            </td>
                            <td style="width: 10%;"><label class="pull-right asterisk">回单时间</label></td>
                            <td style="width: 15%;">
                                <div class='input-group date form_datetime' id='receipt_receiptTime'>
                                    <input type='text' name="receiptTime" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td style="width: 10%;"><label class="pull-right">备注</label></td>
                            <td style="width: 15%;">
                                <label for="remarks"><input id="receipt_remarks" name="remarks" class="form-control" maxlength="255"/></label>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="receipt()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%--附件--%>
<div id="annexModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1500px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">附件列表</h4>
            </div>
            <div class="modal-body">
                <form id="annexForm" class="form-horizontal" style="margin-top: 0">
                    <div id="annexToolbar">
                        <shiro:hasPermission name="order:tmTransportOrder:uploadAnnex">
                            <a class="btn btn-primary btn-sm" id="uploadAnnex" onclick="uploadAnnex()">上传附件</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="order:tmTransportOrder:removeAnnex">
                            <a class="btn btn-primary btn-sm" onclick="removeAnnex()">删除附件</a>
                        </shiro:hasPermission>
                    </div>
                    <table id="annexTable" class="text-nowrap" data-toolbar="#annexToolbar"></table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%--附件上传--%>
<div id="uploadFileModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 500px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">上传文件(限制大小10M)</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="uploadFileForm" enctype="multipart/form-data">
                    <table class="table well">
                        <tbody>
                        <tr>
                            <td class="active" style="width: 70%;">
                                <input type="file" id="uploadFile" name="file"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="uploadFileFnc()">上传</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<script type="text/template" id="tmTransportOrderSkuTpl">//<!--
<tr id="tmTransportOrderSkuList{{idx}}" data-index="{{idx}}">
    <td class="hide">
        <input type="hidden" id="tmTransportOrderSkuList{{idx}}_id" name="tmTransportOrderSkuList[{{idx}}].id" value="{{row.id}}"/>
        <input type="hidden" id="tmTransportOrderSkuList{{idx}}_transportNo" name="tmTransportOrderSkuList[{{idx}}].transportNo" value="{{row.transportNo}}"/>
        <input type="hidden" id="tmTransportOrderSkuList{{idx}}_lineNo" name="tmTransportOrderSkuList[{{idx}}].lineNo" value="{{row.lineNo}}"/>
        <input type="hidden" id="tmTransportOrderSkuList{{idx}}_ownerCode" name="tmTransportOrderSkuList[{{idx}}].ownerCode" value="{{row.ownerCode}}"/>
        <input type="hidden" id="tmTransportOrderSkuList{{idx}}_orgId" name="tmTransportOrderSkuList[{{idx}}].orgId" value="{{row.orgId}}"/>
        <input type="hidden" id="tmTransportOrderSkuList{{idx}}_baseOrgId" name="tmTransportOrderSkuList[{{idx}}].baseOrgId" value="{{row.baseOrgId}}"/>
        <input type="hidden" id="tmTransportOrderSkuList{{idx}}_recVer" name="tmTransportOrderSkuList[{{idx}}].recVer" value="{{row.recVer}}"/>
        <input type="hidden" id="orderDetail{{idx}}_skuWeight" value="{{row.skuWeight}}"/>
        <input type="hidden" id="orderDetail{{idx}}_skuCubic" value="{{row.skuCubic}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td>
        <sys:grid title="商品" url="${ctx}/tms/basic/tmItem/grid" fieldId="" fieldName="" fieldKeyName=""
                  displayFieldId="tmTransportOrderSkuList{{idx}}_skuCode" displayFieldName="tmTransportOrderSkuList[{{idx}}].skuCode" displayFieldKeyName="skuCode" displayFieldValue="{{row.skuCode}}"
                  fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName" searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                  queryParams="ownerCode|orgId" queryParamValues="customerCode|baseOrgId"
                  cssClass="form-control required" afterSelect="orderDetailSkuSelect({{idx}})"/>
    </td>
    <td>
        <input id="tmTransportOrderSkuList{{idx}}_skuName" name="tmTransportOrderSkuList[{{idx}}].skuName" class="form-control" value="{{row.skuName}}" readonly maxlength="20"/>
    </td>
    <td>
        <input id="tmTransportOrderSkuList{{idx}}_qty" name="tmTransportOrderSkuList[{{idx}}].qty" class="form-control required" value="{{row.qty}}" onkeyup="bq.numberValidator(this, 4, 0);orderDetailSkuQtyChange({{idx}});"  maxlength="18"/>
    </td>
    <td>
        <input id="tmTransportOrderSkuList{{idx}}_weight" name="tmTransportOrderSkuList[{{idx}}].weight" class="form-control" value="{{row.weight}}" onkeyup="bq.numberValidator(this, 4, 0)" maxlength="18"/>
    </td>
    <td>
        <input id="tmTransportOrderSkuList{{idx}}_cubic" name="tmTransportOrderSkuList[{{idx}}].cubic" class="form-control" value="{{row.cubic}}" onkeyup="bq.numberValidator(this, 4, 0)" maxlength="18"/>
    </td>
</tr>//-->
</script>
<script type="text/template" id="tmTransportOrderCostTpl">//<!--
<tr id="tmTransportOrderCostList{{idx}}" data-index="{{idx}}">
    <td class="hide">
        <input type="hidden" id="tmTransportOrderCostList{{idx}}_id" name="tmTransportOrderCostList[{{idx}}].id" value="{{row.id}}"/>
        <input type="hidden" id="tmTransportOrderCostList{{idx}}_transportNo" name="tmTransportOrderCostList[{{idx}}].transportNo" value="{{row.transportNo}}"/>
        <input type="hidden" id="tmTransportOrderCostList{{idx}}_orgId" name="tmTransportOrderCostList[{{idx}}].orgId" value="{{row.orgId}}"/>
        <input type="hidden" id="tmTransportOrderCostList{{idx}}_baseOrgId" name="tmTransportOrderCostList[{{idx}}].baseOrgId" value="{{row.baseOrgId}}"/>
        <input type="hidden" id="tmTransportOrderCostList{{idx}}_recVer" name="tmTransportOrderCostList[{{idx}}].recVer" value="{{row.recVer}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td>
        <select id="tmTransportOrderCostList{{idx}}_costType" name="tmTransportOrderCostList[{{idx}}].costType" data-value="{{row.costType}}" class="form-control m-b">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('TMS_COST_TYPE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <select id="tmTransportOrderCostList{{idx}}_settlementMethod" name="tmTransportOrderCostList[{{idx}}].settlementMethod" data-value="{{row.settlementMethod}}" class="form-control m-b">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('BMS_SETTLEMENT_TYPE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <select id="tmTransportOrderCostList{{idx}}_settlementType" name="tmTransportOrderCostList[{{idx}}].settlementType" data-value="{{row.settlementType}}" class="form-control m-b">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('TMS_SETTLEMENT_TYPE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <input id="tmTransportOrderCostList{{idx}}_amount" name="tmTransportOrderCostList[{{idx}}].amount" class="form-control" value="{{row.amount}}" onkeyup="bq.numberValidator(this, 4, 0)"  maxlength="18"/>
    </td>
    <td>
        <sys:grid title="结算对象" url="${ctx}/tms/basic/tmTransportObj/settleGrid"
                  fieldId="tmTransportOrderCostList{{idx}}_settlementCode" fieldName="tmTransportOrderCostList[{{idx}}].settlementCode"
                  fieldKeyName="transportObjCode" fieldValue="{{row.settlementCode}}"
                  displayFieldId="tmTransportOrderCostList{{idx}}_settlementName" displayFieldName="tmTransportOrderCostList[{{idx}}].settlementName"
                  displayFieldKeyName="transportObjName" displayFieldValue="{{row.settlementName}}"
                  fieldLabels="结算对象编码|结算对象名称" fieldKeys="transportObjCode|transportObjName"
                  searchLabels="结算对象编码|结算对象名称" searchKeys="transportObjCode|transportObjName"
                  queryParams="transportObjType|orgId" queryParamValues="tmTransportOrderCostList{{idx}}_settlementType|baseOrgId"
                  cssClass="form-control"/>
    </td>
    <td>
        <select id="tmTransportOrderCostList{{idx}}_isBill" name="tmTransportOrderCostList[{{idx}}].isBill" data-value="{{row.isBill}}" class="form-control m-b">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('SYS_YES_NO')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <input id="tmTransportOrderCostList{{idx}}_billVoucher" name="tmTransportOrderCostList[{{idx}}].billVoucher" class="form-control" maxlength="20"/>
    </td>
</tr>//-->
</script>
</body>
</html>
