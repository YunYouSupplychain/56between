<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>装车单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmLdHeaderForm.js" %>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="outbound:banQinWmLdHeader:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmLdHeader:shipment">
        <a class="btn btn-primary" id="header_shipment" onclick="shipment()">发货确认</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmLdHeader:loadDelivery">
        <a class="btn btn-primary" id="header_loadDelivery" onclick="loadDelivery()">装车交接</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmLdHeader:cancelShipment">
        <a class="btn btn-primary" id="header_cancelShipment" onclick="cancelShipment()">取消发货</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmLdHeader:cancelDelivery">
        <a class="btn btn-primary" id="header_cancelDelivery" onclick="cancelDelivery()">取消交接</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmLdEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:200px">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
            <li class=""><a data-toggle="tab" href="#carrierInfo" aria-expanded="true">承运商</a></li>
            <li class=""><a data-toggle="tab" href="#consigneeInfo" aria-expanded="true">收货人</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">装车单号</label></td>
                        <td class="width-15">
                            <form:input path="ldNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_LD_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">出库单状态</label></td>
                        <td class="width-15">
                            <form:select path="soStatus" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_ALLOC_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>装车单类型</label></td>
                        <td class="width-15">
                            <form:select path="ldType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_LD_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">预计装车时间从</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='fmLdTime'>
                                <input id="orderTimeId" name="fmLdTime" class="form-control" value="<fmt:formatDate value="${banQinWmLdEntity.fmLdTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">预计装车时间到</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='toLdTime'>
                                <input type='text' name="toLdTime" class="form-control" value="<fmt:formatDate value="${banQinWmLdEntity.toLdTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">装车交接人</label></td>
                        <td class="width-15">
                            <form:input path="deliverOp" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">装车交接时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='deliverTime'>
                                <input type='text' name="deliverTime" class="form-control" value="<fmt:formatDate value="${banQinWmLdEntity.deliverTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">是否已预约月台</label></td>
                        <td class="width-15">
                            <input id="isAppointDock" name="isAppointDock" type="checkbox" class="myCheckbox"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义1</label></td>
                        <td class="width-15">
                            <form:input path="def1" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义2</label></td>
                        <td class="width-15">
                            <form:input path="def2" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义3</label></td>
                        <td class="width-15">
                            <form:input path="def3" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">自定义4</label></td>
                        <td class="width-15">
                            <form:input path="def4" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义5</label></td>
                        <td class="width-15">
                            <form:input path="def5" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="carrierInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">承运商编码</label></td>
                        <td class="width-15">
                            <input id="carrierType" value="CARRIER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择承运商" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="carrierCode" displayFieldName="carrierCode"
                                           displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmLdEntity.carrierCode}"
                                           selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                           fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="carrierType"
                                           concatId="carrierName" concatName="ebcuNameCn">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">承运商名称</label></td>
                        <td class="width-15">
                            <form:input path="carrierName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">承运商电话</label></td>
                        <td class="width-15">
                            <form:input path="carrierTel" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">承运商地址</label></td>
                        <td class="width-15">
                            <form:input path="carrierAddress" htmlEscape="false" class="form-control" maxlength="512" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">车型</label></td>
                        <td class="width-15">
                            <form:select path="vehicleType" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_CAR_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">驾驶员</label></td>
                        <td class="width-15">
                            <form:input path="driver" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>车牌号</label></td>
                        <td class="width-15">
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control required" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">路线</label></td>
                        <td class="width-15">
                            <form:input path="line" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">联系人名称</label></td>
                        <td class="width-15">
                            <form:input path="carrierContactName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系人电话</label></td>
                        <td class="width-15">
                            <form:input path="carrierContactTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系人地址</label></td>
                        <td class="width-15" colspan="3">
                            <form:input path="carrierContactAddress" htmlEscape="false" class="form-control" maxlength="512"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="consigneeInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货人编码</label></td>
                        <td class="width-15">
                            <input id="consigneeType" value="CONSIGNEE" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择收货人" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="consigneeCode" displayFieldName="consigneeCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmLdEntity.consigneeCode}"
                                           selectButtonId="consigneeSelectId" deleteButtonId="consigneeDeleteId"
                                           fieldLabels="收货人编码|收货人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           searchLabels="收货人编码|收货人名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="consigneeType"
                                           concatId="consigneeName,consigneeTel,consigneeAddr,consigneeFax" concatName="ebcuNameCn,ebcuTel,ebcuAddress,ebcuFax">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人名称</label></td>
                        <td class="width-15">
                            <form:input path="consigneeName" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人电话</label></td>
                        <td class="width-15">
                            <form:input path="consigneeTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人地址</label></td>
                        <td class="width-15">
                            <form:input path="consigneeAddr" htmlEscape="false" class="form-control" maxlength="512"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">联系人名称</label></td>
                        <td class="width-15">
                            <form:input path="contactName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系人电话</label></td>
                        <td class="width-15">
                            <form:input path="contactTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系人地址</label></td>
                        <td class="width-15" colspan="3">
                            <form:input path="contactAddr" htmlEscape="false" class="form-control" maxlength="512"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<div class="tabs-container">
    <ul id="detailTab" class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#orderDetailInfo" aria-expanded="true" onclick="detailTabChange(0)">订单明细</a></li>
        <li class=""><a data-toggle="tab" href="#traceIdlInfo" aria-expanded="true" onclick="detailTabChange(1)">包裹明细</a></li>
        <li class=""><a data-toggle="tab" href="#loadingInfo" aria-expanded="true" onclick="detailTabChange(2)">待装车明细</a></li>
        <li class=""><a data-toggle="tab" href="#cancelInfo" aria-expanded="true" onclick="detailTabChange(3)">已装车明细</a></li>
    </ul>
    <div class="tab-content">
        <div id="orderDetailInfo" class="tab-pane fade in active">
            <div id="orderDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="outbound:banQinWmLdDetail:addSo">
                    <a class="btn btn-primary" id="btn_add_so" onclick="addSoListHandler()">添加订单</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdDetail:removeSo">
                    <a class="btn btn-danger" id="btn_remove_so" onclick="removeSoListHandler()">删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdDetail:loadingSo">
                    <a class="btn btn-primary" id="btn_loading_so" onclick="loadingBySoNoHandler()">装载确认</a>
                </shiro:hasPermission>
            </div>
            <table id="orderDetailTable" class="table well text-nowrap"></table>
        </div>
        <div id="traceIdlInfo" class="tab-pane fade">
            <div id="traceIdToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="outbound:banQinWmLdDetail:scanning">
                    <a class="btn btn-primary" id="btn_scanning" onclick="scanningHandler()">扫描包裹</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdDetail:removeTraceId">
                    <a class="btn btn-danger" id="btn_removeTraceId" onclick="removeTraceIdHandler()">删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdDetail:loadingByTraceId">
                    <a class="btn btn-primary" id="btn_loadingByTraceId" onclick="loadingByTraceIdHandler()">装载确认</a>
                </shiro:hasPermission>
            </div>
            <table id="traceIdTable" class="table well text-nowrap"></table>
        </div>
        <div id="loadingInfo" class="tab-pane fade">
            <div id="loadingToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="outbound:banQinWmLdDetail:addPicking">
                    <a class="btn btn-primary" id="btn_addPicking" onclick="addPickingDetailHandler()">添加拣货明细</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdDetail:removePicking">
                    <a class="btn btn-danger" id="btn_removePicking" onclick="removePickingDetailHandler()">删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdDetail:batchLoading">
                    <a class="btn btn-primary" id="btn_batchLoading" onclick="batchLoadingHandler()">装载确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmLdDetail:ldList">
                    <a class="btn btn-primary" id="btn_ldList" onclick="ldListHandler()">生成装车单</a>
                </shiro:hasPermission>
            </div>
            <table id="loadingTable" class="table well text-nowrap"></table>
        </div>
        <div id="cancelInfo" class="tab-pane fade">
            <div id="cancelToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="outbound:banQinWmLdDetail:cancelLoading">
                    <a class="btn btn-primary" id="btn_cancel" onclick="cancelLoadingHandler()">取消装车</a>
                </shiro:hasPermission>
            </div>
            <table id="cancelTable" class="table well text-nowrap"></table>
        </div>
    </div>
</div>

<div class="modal fade" id="orderDetailModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1100px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">订单明细</h4>
            </div>
            <div class="modal-body" style="background: #ecf0f1;">
                <div style="width: 100%;">
                    <a class="btn btn-primary" onclick="queryAlloc()">查询</a>
                    <a class="btn btn-primary" onclick="resetAlloc()">重置</a>
                    <a class="btn btn-primary" onclick="orderDetailSelectConfirm()">确认</a>
                </div>
                <form id="orderDetail_searchForm1" style="border: 1px solid #dddddd; margin-top: 10px; margin-bottom: 10px; padding-bottom: 10px;">
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td><label class="pull-left">出库单号</label></td>
                            <td><label class="pull-left">货主</label></td>
                            <td><label class="pull-left">出库单类型</label></td>
                            <td><label class="pull-left">物流单号</label></td>
                        </tr>
                        <tr>
                            <td>
                                <input id="orderDetail_soNo" name="soNo" class="form-control" maxlength="64">
                            </td>
                            <td>
                                <input id="ownerType" value="OWNER" type="hidden">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="orderDetail_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="orderDetail_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="orderDetail_ownerSelectId" deleteButtonId="orderDetail_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerType">
                                </sys:popSelect>
                            </td>
                            <td>
                                <select id="orderDetail_soType" name="soType" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_SO_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <input id="orderDetail_logisticNo" name="logisticNo" class="form-control" maxlength="64">
                            </td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">预计发货时间从</label></td>
                            <td><label class="pull-left">预计发货时间到</label></td>
                            <td><label class="pull-left">承运商</label></td>
                            <td><label class="pull-left">收货人</label></td>
                        </tr>
                        <tr>
                            <td><input id="query1_fmEtaFm" name="fmEtaFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query1_fmEtaTo" name="fmEtaTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="orderDetail_carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="orderDetail_carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="orderDetail_carrierSelectId" deleteButtonId="orderDetail_carrierDeleteId"
                                               fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="carrierType">
                                </sys:popSelect>
                            </td>
                            <td>
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="orderDetail_consigneeCode" fieldName="consigneeCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="orderDetail_consigneeName" displayFieldName="consigneeName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="orderDetail_consigneeSelectId" deleteButtonId="orderDetail_consigneeDeleteId"
                                               fieldLabels="收货人编码|收货人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="收货人编码|收货人名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="consigneeType">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">订单时间从</label></td>
                            <td><label class="pull-left">订单时间到</label></td>
                        </tr>
                        <tr>
                            <td><input id="query1_fmOrderDate" name="fmOrderDate" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query1_toOrderDate" name="toOrderDate" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
                <table id="allocDetailTable" class="table well text-nowrap"></table>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="packDetailModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1100px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">包裹明细</h4>
            </div>
            <div class="modal-body" style="background: #ecf0f1;">
                <form id="packDetail_searchForm1" style="border: 1px solid #dddddd; margin-bottom: 10px; padding-bottom: 10px;">
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td><label class="pull-left">箱号</label></td>
                            <td><label class="pull-left">是否扣数</label></td>
                            <td><label class="pull-left">已扫描包裹数</label></td>
                        </tr>
                        <tr>
                            <td><input id="packDetail_traceId" name="traceId" class="form-control" maxlength="64"></td>
                            <td><input id="packDetail_checkNum" name="checkNum" type="checkbox" class="myCheckbox"/></td>
                            <td><input id="packDetail_num" name="num" class="form-control" readonly></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
                <div style="width: 100%; padding-bottom: 10px;">
                    <a class="btn btn-primary" onclick="confirmPackDetail()">确认</a>
                    <a class="btn btn-primary" onclick="removePackDetail()">删除</a>
                </div>
                <table id="packDetailTable" class="table well text-nowrap"></table>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="loadingDetailModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1100px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">待装车明细</h4>
            </div>
            <div class="modal-body" style="background: #ecf0f1;">
                <div style="width: 100%;">
                    <a class="btn btn-primary" onclick="queryLoading()">查询</a>
                    <a class="btn btn-primary" onclick="resetLoading()">重置</a>
                    <a class="btn btn-primary" onclick="loadingDetailSelectConfirm()">确认</a>
                </div>
                <form id="loadingDetail_searchForm1" style="border: 1px solid #dddddd; margin-top: 10px; margin-bottom: 10px; padding-bottom: 10px;">
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td><label class="pull-left">出库单号</label></td>
                            <td><label class="pull-left">货主</label></td>
                            <td><label class="pull-left">商品</label></td>
                            <td><label class="pull-left">物流单号</label></td>
                        </tr>
                        <tr>
                            <td><input id="loadingDetail_soNo" name="soNo" class="form-control" maxlength="64"></td>
                            <td>
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="loadingDetail_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="loadingDetail_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="loadingDetail_ownerSelectId" deleteButtonId="loadingDetail_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerType">
                                </sys:popSelect>
                            </td>
                            <td>
                                <sys:popSelect url="${ctx}/wms/customer/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                               fieldId="loadingDetail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="loadingDetail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="loadingDetail_skuSelectId" deleteButtonId="loadingDetail_skuDeleteId"
                                               fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="codeAndName">
                                </sys:popSelect>
                            </td>
                            <td><input id="loadingDetail_logisticNo" name="logisticNo" class="form-control" maxlength="64"></td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">预计发货时间从</label></td>
                            <td><label class="pull-left">预计发货时间到</label></td>
                            <td><label class="pull-left">订单时间从</label></td>
                            <td><label class="pull-left">订单时间到</label></td>
                        </tr>
                        <tr>
                            <td><input id="loadingDetail_fmEtd" name="fmEtd" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="loadingDetail_toEtd" name="toEtd" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="loadingDetail_fmOrderDate" name="fmOrderDate" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="loadingDetail_toOrderDate" name="toOrderDate" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">收货人</label></td>
                        </tr>
                        <tr>
                            <td>
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="loadingDetail_consigneeCode" fieldName="consigneeCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="loadingDetail_consigneeName" displayFieldName="consigneeName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="loadingDetail_consigneeSelectId" deleteButtonId="loadingDetail_consigneeDeleteId"
                                               fieldLabels="收货人编码|收货人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="收货人编码|收货人名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="consigneeType">
                                </sys:popSelect>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
                <table id="loadingDetailTable" class="table well text-nowrap"></table>
            </div>
        </div>
    </div>
</div>
</body>
</html>