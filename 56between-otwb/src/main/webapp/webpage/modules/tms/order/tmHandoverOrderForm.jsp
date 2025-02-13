<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>交接单管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmHandoverOrderForm.js" %>
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
</div>
<div style="margin-left: 10px;">
    <shiro:hasPermission name="tms:order:tmHandoverOrder:edit">
        <a id="save" class="btn btn-primary" disabled onclick="save()"> 保存</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="tmHandoverOrderEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <%--基本信息--%>
    <div class="tabs-container" style="min-height: 150px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#basicInfo" aria-expanded="true">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tr>
                        <td class="width-10"><label class="pull-right">交接单号</label></td>
                        <td class="width-15">
                            <form:input path="handoverNo" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">交接单状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control " disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_HANDOVER_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">派车单号</label></td>
                        <td class="width-15">
                            <form:input path="dispatchNo" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">派车时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='dispatchTime'>
                                <input type='text' name="dispatchTime" class="form-control required"
                                       value="<fmt:formatDate value="${tmHandoverOrderEntity.dispatchTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">派车网点</label></td>
                        <td class="width-15">
                            <sys:grid title="派车网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="dispatchOutletCode" fieldName="dispatchOutletCode" fieldKeyName="transportObjCode" fieldValue="${tmHandoverOrderEntity.dispatchOutletCode}"
                                      displayFieldId="dispatchOutletName" displayFieldName="dispatchOutletName" displayFieldKeyName="transportObjName" displayFieldValue="${tmHandoverOrderEntity.dispatchOutletName}"
                                      fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                      cssClass="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">配送网点</label></td>
                        <td class="width-15">
                            <sys:grid title="派车网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="deliveryOutletCode" fieldName="deliveryOutletCode" fieldKeyName="transportObjCode" fieldValue="${tmHandoverOrderEntity.deliveryOutletCode}"
                                      displayFieldId="deliveryOutletName" displayFieldName="deliveryOutletName" displayFieldKeyName="transportObjName" displayFieldValue="${tmHandoverOrderEntity.deliveryOutletName}"
                                      fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                      cssClass="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right">交接人</label></td>
                        <td class="width-15">
                            <form:input path="handoverPerson" htmlEscape="false" class="form-control " maxlength="35"/>
                        </td>
                        <td class="width-10"><label class="pull-right">备注信息</label></td>
                        <td colspan="3">
                            <form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    <%--承运信息--%>
    <div class="tabs-container" style="min-height: 100px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#carrierInfo" aria-expanded="true">承运信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="carrierInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tr>
                        <td class="width-10"><label class="pull-right">承运商</label></td>
                        <td class="width-15">
                            <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="carrierCode" fieldName="carrierCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmHandoverOrderEntity.carrierCode}"
                                      displayFieldId="carrierName" displayFieldName="carrierName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmHandoverOrderEntity.carrierName}"
                                      fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="carrierType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">车牌号</label></td>
                        <td class="width-15">
                            <sys:grid title="车辆" url="${ctx}/tms/basic/tmVehicle/grid"
                                      fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                      displayFieldId="carNo" displayFieldName="carNo"
                                      displayFieldKeyName="carNo" displayFieldValue="${tmHandoverOrderEntity.carNo}"
                                      fieldLabels="车牌号|承运商|设备类型" fieldKeys="carNo|carrierName|transportEquipmentTypeName"
                                      searchLabels="车牌号" searchKeys="carNo"
                                      queryParams="carrierCode|orgId" queryParamValues="carrierCode|baseOrgId"
                                      cssClass="form-control" afterSelect="carSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">司机</label></td>
                        <td class="width-15">
                            <sys:grid title="司机" url="${ctx}/tms/basic/tmDriver/grid"
                                      fieldId="driver" fieldName="driver" fieldKeyName="code" fieldValue="${tmHandoverOrderEntity.driver}"
                                      displayFieldId="driverName" displayFieldName="driverName"
                                      displayFieldKeyName="name" displayFieldValue="${tmHandoverOrderEntity.driverName}"
                                      fieldLabels="编码|姓名|承运商" fieldKeys="code|name|carrierName" searchLabels="编码|姓名" searchKeys="code|name"
                                      queryParams="carrierCode|orgId" queryParamValues="carrierCode|baseOrgId"
                                      cssClass="form-control" afterSelect="driverSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="driverTel" htmlEscape="false" class="form-control " maxlength="20"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
<%--配送点、发货标签、订单列表--%>
<div class="tabs-container" style="margin-left: 10px;max-height: 500px;">
    <ul id="detailTabs" class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#tmHandoverOrderLabelInfo" aria-expanded="true">交接标签</a></li>
        <li class=""><a data-toggle="tab" href="#tmHandoverOrderSkuInfo" aria-expanded="true">商品明细</a></li>
        <li class=""><a data-toggle="tab" href="#tmHandoverOrderImgInfo" aria-expanded="true">交接图片</a></li>
    </ul>
    <div class="tab-content">
        <%--交接标签--%>
        <div id="tmHandoverOrderLabelInfo" class="tab-pane fade in active">
            <h3 style="font-weight: bold">标签总数：<span id="labelNum"></span></h3>
            <table id="tmHandoverOrderLabelTable" class="text-nowrap"></table>
        </div>
        <%--商品明细--%>
        <div id="tmHandoverOrderSkuInfo" class="tab-pane fade">
            <div id="skuDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="tms:order:tmHandoverOrder:sku:edit">
                    <a class="btn btn-primary" id="skuDetail_edit" disabled onclick="editSku()">修改</a>
                </shiro:hasPermission>
            </div>
            <table id="tmHandoverOrderSkuTable" class="text-nowrap"></table>
        </div>
        <%--图片列表--%>
        <div id="tmHandoverOrderImgInfo" class="tab-pane fade">
            <table id="tmHandoverOrderImgTable" class="text-nowrap"></table>
        </div>
    </div>
</div>
<div class="tab-content">
    <!-- 商品信息修改弹出框 -->
    <div class="modal fade" id="skuEditModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog" style="width:400px">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">商品信息修改</h4>
                </div>
                <div class="modal-body" id="skuInfo">
                    <div id="collapse1" class="accordion-body collapse in">
                        <div class="accordion-inner">
                            <form:form id="detailSaveForm" modelAttribute="tmHandoverOrderSkuEntity" class="form">
                                <input id="detail_id" name="id" type="hidden"/>
                                <input id="detail_handoverNo" name="handoverNo" type="hidden"/>
                                <input id="detail_transportNo" name="transportNo" type="hidden"/>
                                <input id="detail_customerNo" name="customerNo" type="hidden"/>
                                <input id="detail_ownerCode" name="ownerCode" type="hidden"/>
                                <input id="detail_skuCode" name="skuCode" type="hidden"/>
                                <input id="detail_orderQty" name="orderQty" type="hidden"/>
                                <input id="detail_receiveShip" name="receiveShip" type="hidden"/>
                                <input id="detail_orgId" name="orgId" type="hidden"/>
                                <input id="detail_recVer" name="recVer" type="hidden"/>
                                <input id="detail_baseOrgId" name="baseOrgId" type="hidden"/>
                                <table class="table">
                                    <tr>
                                        <td><label class="pull-right asterisk">实际数量</label></td>
                                        <td>
                                            <input id="detail_actualQty" name="actualQty" htmlEscape="false" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)" class="form-control required"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td><label class="pull-right asterisk">卸货时长</label></td>
                                        <td>
                                            <input id="detail_unloadingTime" name="unloadingTime" htmlEscape="false" maxlength="64" onkeyup="bq.numberValidator(this, 2, 0)" class="form-control required"/>
                                        </td>
                                    </tr>
                                </table>
                            </form:form>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="skuConfirmSave()">确认</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal -->
    </div>
</div>
</body>
</html>