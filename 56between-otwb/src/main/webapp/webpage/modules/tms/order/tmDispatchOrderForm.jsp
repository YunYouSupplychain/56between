<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>派车单管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmDispatchOrderForm.js" %>
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
    <shiro:hasPermission name="order:tmDispatchOrder:edit">
        <a id="save" class="btn btn-primary" disabled onclick="save()"> 保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmDispatchOrder:audit">
        <a id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmDispatchOrder:cancelAudit">
        <a id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()"> 取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmDispatchOrder:depart">
        <a id="depart" class="btn btn-primary" disabled onclick="depart()"> 发车</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="order:tmDispatchOrder:annex">
        <a id="annex" class="btn btn-primary btn-sm" onclick="annex()">附件</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="tmDispatchOrderEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="account"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="dataSource"/>
    <form:hidden path="recVer"/>
    <form:hidden path="totalAmount"/>
    <form:hidden path="dispatchPlanNo"/>
    <sys:message content="${message}"/>
    <%--基本信息--%>
    <div class="tabs-container" style="min-height: 250px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#basicInfo" aria-expanded="true">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tr>
                        <td class="width-10"><label class="pull-right">派车单号</label></td>
                        <td class="width-15">
                            <form:input path="dispatchNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">派车时间</label></td>
                        <td class="width-15">
                            <sys:datetime id="dispatchTime" name="dispatchTime" cssClass="form-control required" value="${tmDispatchOrderEntity.dispatchTime}"/>
                        </td>
                        <td class="width-10"><label class="pull-right">派车单状态</label></td>
                        <td class="width-15">
                            <form:select path="dispatchStatus" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_DISPATCH_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">派车单类型</label></td>
                        <td class="width-15">
                            <form:select path="dispatchType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_DISPATCH_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">运输方式</label></td>
                        <td class="width-15">
                            <form:select path="transportType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TRANSPORT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">派车网点</label></td>
                        <td class="width-15">
                            <sys:grid title="派车网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="dispatchOutletCode" fieldName="dispatchOutletCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmDispatchOrderEntity.dispatchOutletCode}"
                                      displayFieldId="dispatchOutletName" displayFieldName="dispatchOutletName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmDispatchOrderEntity.dispatchOutletName}"
                                      fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                      cssClass="form-control required" readonly="false"/>
                        </td>
                        <td class="width-10"><label class="pull-right">总件数</label></td>
                        <td class="width-15">
                            <form:input path="totalQty" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)" maxlength="18" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">实收件数</label></td>
                        <td class="width-15">
                            <form:input path="receivedQty" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)" maxlength="18" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">总重量</label></td>
                        <td class="width-15">
                            <form:input path="totalWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)" maxlength="18" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">总体积</label></td>
                        <td class="width-15">
                            <form:input path="totalCubic" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)" maxlength="18" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预付金额</label></td>
                        <td class="width-15">
                            <form:input path="prepaidAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)" maxlength="18" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">派车人</label></td>
                        <td class="width-15">
                            <form:input path="dispatcher" htmlEscape="false" class="form-control" maxlength="35"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">班次</label></td>
                        <td class="width-15">
                            <form:input path="shift" htmlEscape="false" class="form-control" maxlength="35"/>
                        </td>
                        <td class="width-10"><label class="pull-right">月台</label></td>
                        <td class="width-15">
                            <form:input path="platform" htmlEscape="false" class="form-control" maxlength="35"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否异常</label></td>
                        <td class="width-15">
                            <form:select path="isException" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">是否APP录入</label></td>
                        <td class="width-15">
                            <form:select path="isAppInput" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">机构</label></td>
                        <td class="width-15">
                            <form:input path="orgName" htmlEscape="false" class="form-control" maxlength="35" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">备注信息</label></td>
                        <td colspan="5">
                            <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="255"/>
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
                        <td class="width-10"><label class="pull-right asterisk">承运商</label></td>
                        <td class="width-15">
                            <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="carrierCode" fieldName="carrierCode"
                                      fieldKeyName="transportObjCode" fieldValue="${tmDispatchOrderEntity.carrierCode}"
                                      displayFieldId="carrierName" displayFieldName="carrierName"
                                      displayFieldKeyName="transportObjName" displayFieldValue="${tmDispatchOrderEntity.carrierName}"
                                      fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="carrierType|baseOrgId"
                                      cssClass="form-control required" afterSelect="carrierSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">车牌号</label></td>
                        <td class="width-15">
                            <sys:grid title="车辆" url="${ctx}/tms/basic/tmVehicle/grid"
                                      fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                      displayFieldId="carNo" displayFieldName="carNo"
                                      displayFieldKeyName="carNo" displayFieldValue="${tmDispatchOrderEntity.carNo}"
                                      fieldLabels="车牌号|承运商|设备类型" fieldKeys="carNo|carrierName|transportEquipmentTypeName"
                                      searchLabels="车牌号" searchKeys="carNo"
                                      queryParams="carrierCode|orgId|opOrgId" queryParamValues="carrierCode|baseOrgId|orgId"
                                      cssClass="form-control" afterSelect="carSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">司机</label></td>
                        <td class="width-15">
                            <sys:grid title="司机" url="${ctx}/tms/basic/tmDriver/grid"
                                      fieldId="driver" fieldName="driver"
                                      fieldKeyName="code" fieldValue="${tmDispatchOrderEntity.driver}"
                                      displayFieldId="driverName" displayFieldName="driverName"
                                      displayFieldKeyName="name" displayFieldValue="${tmDispatchOrderEntity.driverName}"
                                      fieldLabels="编码|姓名|承运商" fieldKeys="code|name|carrierName"
                                      searchLabels="编码|姓名" searchKeys="code|name"
                                      queryParams="carrierCode|orgId" queryParamValues="carrierCode|baseOrgId"
                                      cssClass="form-control" afterSelect="driverSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">副驾驶</label></td>
                        <td class="width-15">
                            <sys:grid title="副驾驶" url="${ctx}/tms/basic/tmDriver/grid"
                                      fieldId="copilot" fieldName="copilot"
                                      fieldKeyName="code" fieldValue="${tmDispatchOrderEntity.copilot}"
                                      displayFieldId="copilotName" displayFieldName="copilotName"
                                      displayFieldKeyName="name" displayFieldValue="${tmDispatchOrderEntity.copilotName}"
                                      fieldLabels="编码|姓名|承运商" fieldKeys="code|name|carrierName"
                                      searchLabels="编码|姓名" searchKeys="code|name"
                                      queryParams="carrierCode|orgId" queryParamValues="carrierCode|baseOrgId"
                                      cssClass="form-control" afterSelect="driverSelect"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">司机电话</label></td>
                        <td class="width-15">
                            <form:input path="driverTel" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                        <td class="width-10"><label class="pull-right">起始地</label></td>
                        <td class="width-15">
                            <sys:area id="startAreaId" name="startAreaId" value="${tmDispatchOrderEntity.startAreaId}"
                                      labelName="startAreaName" labelValue="${tmDispatchOrderEntity.startAreaName}"
                                      allowSearch="true" cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">目的地</label></td>
                        <td class="width-15">
                            <sys:area id="endAreaId" name="endAreaId" value="${tmDispatchOrderEntity.endAreaId}"
                                      labelName="endAreaName" labelValue="${tmDispatchOrderEntity.endAreaName}"
                                      allowSearch="true" cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发车时间</label></td>
                        <td class="width-15">
                            <sys:datetime id="departureTime" name="departureTime" cssClass="form-control" value="${tmDispatchOrderEntity.departureTime}" disabled="true"/>
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
        <li class="active"><a data-toggle="tab" href="#tmDispatchOrderSiteInfo" aria-expanded="true">配送点</a></li>
        <li class=""><a data-toggle="tab" href="#tmDispatchOrderLabelInfo" aria-expanded="true">派车标签</a></li>
        <li class=""><a data-toggle="tab" href="#orderListInfo" aria-expanded="true">订单列表</a></li>
    </ul>
    <div class="tab-content">
        <%--配送点--%>
        <div id="tmDispatchOrderSiteInfo" class="tab-pane fade in active">
            <div id="tmDispatchOrderSiteToolbar" style="margin-top: 10px;margin-bottom: 10px;">
                <shiro:hasPermission name="tms:order:dispatch:site:add">
                    <a id="site_add" class="btn btn-primary" disabled onclick="addSite()"> 新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:dispatch:site:edit">
                    <a id="site_save" class="btn btn-primary" disabled onclick="saveSite()"> 保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:dispatch:site:del">
                    <a id="site_remove" class="btn btn-danger" disabled onclick="delSite()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <div class="fixed-table-container" style="padding-bottom: 0;">
                <form id="tmDispatchOrderSiteForm">
                    <table id="tmDispatchOrderSiteTable" class="table text-nowrap" data-toolbar="#tmDispatchOrderSiteToolbar">
                        <thead>
                        <tr>
                            <th class="hide"></th>
                            <th style="width:36px;vertical-align:middle">
                                <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                            </th>
                            <th style="width: 36px;">顺序</th>
                            <th style="width: 400px;">配送对象编码*</th>
                            <th style="width: 400px;">配送对象名称</th>
                            <th style="width: 300px;">提货/送货</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody id="tmDispatchOrderSiteList">
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
        <%--派车标签--%>
        <div id="tmDispatchOrderLabelInfo" class="tab-pane fade">
            <div id="orderLabelToolbar">
                <shiro:hasPermission name="tms:order:dispatch:label:data">
                    <a id="orderLabel_show" class="btn btn-primary" disabled onclick="showLabel()"> 查看标签</a>
                </shiro:hasPermission>
            </div>
            <h3 style="margin-top: 10px;font-weight: bold">标签总数：<span id="labelNum"></span></h3>
            <table id="tmDispatchOrderLabelTable" class="text-nowrap" data-toolbar="#orderLabelToolbar"></table>
        </div>
        <%--订单列表--%>
        <div id="orderListInfo" class="tab-pane fade">
            <div id="orderTransportToolbar">
                <shiro:hasPermission name="tms:order:dispatch:transport:data">
                    <a id="orderTransport_show" class="btn btn-primary" disabled onclick="showTransport()"> 查看订单</a>
                </shiro:hasPermission>
            </div>
            <h3 style="margin-top: 10px;font-weight: bold">订单总数：<span id="transportNum"></span></h3>
            <table id="transportTable" class="text-nowrap" data-toolbar="#orderTransportToolbar"></table>
        </div>
    </div>
</div>
<%--选择订单弹出画面--%>
<div id="selectModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="padding-left: 10px">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><span class="dispatchSite" style="font-weight: bold;"></span>&nbsp;&nbsp;配载订单</h4>
            </div>
            <div class="modal-body">
                <div class="tabs-container" style="margin-left: 10px;">
                    <ul id="selectModalTabs" class="nav nav-tabs">
                        <li class="active"><a data-toggle="tab" href="#page1" aria-expanded="true" data-page="page1">派车单内网点</a></li>
                        <li class=""><a data-toggle="tab" href="#page2" aria-expanded="true" data-page="page2">派车单外网点</a></li>
                        <li class=""><a data-toggle="tab" href="#page3" aria-expanded="true" data-page="page3">未指定送货网点</a></li>
                    </ul>
                    <div class="tab-content">
                        <div id="page1" class="tab-pane fade in active">
                            <form class="searchForm form">
                                <table class="table well">
                                    <tr>
                                        <td class="width-10"><label class="pull-right">提货/送货</label></td>
                                        <td class="width-15">
                                            <select name="receiveShip" class="form-control"></select>
                                        </td>
                                        <td class="width-10"><label class="pull-right">运输单号</label></td>
                                        <td class="width-15">
                                            <input name="transportNo" maxlength="20" class=" form-control"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">客户单号</label></td>
                                        <td class="width-15">
                                            <input name="customerNo" maxlength="20" class=" form-control"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">订单类型</label></td>
                                        <td class="width-15">
                                            <select name="orderType" class="form-control m-b">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('TMS_TRANSPORT_ORDER_TYPE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-10"><label class="pull-right">订单时间从</label></td>
                                        <td class="width-15">
                                            <div class='input-group date'>
                                                <input type='text' name="transportOrderTimeFm" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td class="width-10"><label class="pull-right">订单时间到</label></td>
                                        <td class="width-15">
                                            <div class='input-group date'>
                                                <input type='text' name="transportOrderTimeTo" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td class="width-10"><label class="pull-right">委托方</label></td>
                                        <td class="width-15">
                                            <sys:grid title="委托方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                      fieldId="tabPage1_principalCode" fieldName="principalCode" fieldKeyName="transportObjCode"
                                                      displayFieldId="tabPage1_principalName" displayFieldName="principalName" displayFieldKeyName="transportObjName"
                                                      fieldLabels="委托方编码|委托方名称" fieldKeys="transportObjCode|transportObjName"
                                                      searchLabels="委托方编码|委托方名称" searchKeys="transportObjCode|transportObjName"
                                                      queryParams="transportObjType|orgId" queryParamValues="principalType|baseOrgId"
                                                      cssClass="form-control"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">客户</label></td>
                                        <td class="width-15">
                                            <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                      fieldId="tabPage1_customerCode" fieldName="customerCode" fieldKeyName="transportObjCode"
                                                      displayFieldId="tabPage1_customerName" displayFieldName="customerName" displayFieldKeyName="transportObjName"
                                                      fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                                      searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                                      queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                                      cssClass="form-control"/>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                            <div class="left" style="float: left;border: 1px solid #ddd;padding: 1px;margin-right: 5px;">
                                <div style="width: 100%;text-align: center"><h4>可选</h4></div>
                                <div class="toolbar">
                                    <a class="btn btn-primary search" data-page="page1"> 查询</a>
                                    <a class="btn btn-primary reset" data-page="page1"> 重置</a>
                                    <a class="btn btn-primary confirm" data-page="page1"> 确认</a>
                                    <a class="btn btn-primary confirm-all" data-page="page1"> 全确认</a>
                                    <a class="btn btn-default left-refresh" data-page="page1"><i class="fa fa-refresh"></i> 刷新</a>
                                </div>
                                <div class="left-tab" style="overflow-y: auto">
                                    <table class="left-tab text-nowrap" data-page="page1"></table>
                                </div>
                            </div>
                            <div class="right" style="float: left;border: 1px solid #ddd;padding: 1px;">
                                <div style="width: 100%;text-align: center"><h4>已选</h4></div>
                                <div class="toolbar">
                                    <a class="btn btn-primary un-confirm" data-page="page1"> 取消确认</a>
                                    <a class="btn btn-primary un-confirm-all" data-page="page1"> 全取消确认</a>
                                    <a class="btn btn-default right-refresh" data-page="page1"><i class="fa fa-refresh"></i> 刷新</a>
                                </div>
                                <div class="right-tab" style="overflow-y: auto">
                                    <table class="right-tab text-nowrap" data-page="page1"></table>
                                </div>
                            </div>
                        </div>
                        <div id="page2" class="tab-pane fade">
                            <form class="searchForm form">
                                <table class="table well">
                                    <tr>
                                        <td class="width-10"><label class="pull-right">提货/送货</label></td>
                                        <td class="width-15">
                                            <select name="receiveShip" class="form-control"></select>
                                        </td>
                                        <td class="width-10"><label class="pull-right">运输单号</label></td>
                                        <td class="width-15">
                                            <input name="transportNo" maxlength="20" class=" form-control"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">客户单号</label></td>
                                        <td class="width-15">
                                            <input name="customerNo" maxlength="20" class=" form-control"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">订单类型</label></td>
                                        <td class="width-15">
                                            <select name="orderType" class="form-control m-b">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('TMS_TRANSPORT_ORDER_TYPE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-10"><label class="pull-right">订单时间从</label></td>
                                        <td class="width-15">
                                            <div class='input-group date'>
                                                <input type='text' name="transportOrderTimeFm" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td class="width-10"><label class="pull-right">订单时间到</label></td>
                                        <td class="width-15">
                                            <div class='input-group date'>
                                                <input type='text' name="transportOrderTimeTo" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td class="width-10"><label class="pull-right">委托方</label></td>
                                        <td class="width-15">
                                            <sys:grid title="委托方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                      fieldId="tabPage2_principalCode" fieldName="principalCode" fieldKeyName="transportObjCode"
                                                      displayFieldId="tabPage2_principalName" displayFieldName="principalName" displayFieldKeyName="transportObjName"
                                                      fieldLabels="委托方编码|委托方名称" fieldKeys="transportObjCode|transportObjName"
                                                      searchLabels="委托方编码|委托方名称" searchKeys="transportObjCode|transportObjName"
                                                      queryParams="transportObjType|orgId" queryParamValues="principalType|baseOrgId"
                                                      cssClass="form-control"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">客户</label></td>
                                        <td class="width-15">
                                            <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                      fieldId="tabPage2_customerCode" fieldName="customerCode" fieldKeyName="transportObjCode"
                                                      displayFieldId="tabPage2_customerName" displayFieldName="customerName" displayFieldKeyName="transportObjName"
                                                      fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                                      searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                                      queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                                      cssClass="form-control"/>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                            <div class="left" style="float: left;border: 1px solid #ddd;padding: 1px;margin-right: 5px;">
                                <div style="width: 100%;text-align: center"><h4>可选</h4></div>
                                <div class="toolbar">
                                    <a class="btn btn-primary search" data-page="page2"> 查询</a>
                                    <a class="btn btn-primary reset" data-page="page2"> 重置</a>
                                    <a class="btn btn-primary confirm" data-page="page2"> 确认</a>
                                    <a class="btn btn-primary confirm-all" data-page="page2"> 全确认</a>
                                    <a class="btn btn-default left-refresh" data-page="page2"><i class="fa fa-refresh"></i> 刷新</a>
                                </div>
                                <div class="left-tab" style="overflow-y: auto">
                                    <table class="left-tab text-nowrap" data-page="page2"></table>
                                </div>
                            </div>
                            <div class="right" style="float: left;border: 1px solid #ddd;padding: 1px;">
                                <div style="width: 100%;text-align: center"><h4>已选</h4></div>
                                <div class="toolbar">
                                    <a class="btn btn-primary un-confirm" data-page="page2"> 取消确认</a>
                                    <a class="btn btn-primary un-confirm-all" data-page="page2"> 全取消确认</a>
                                    <a class="btn btn-default right-refresh" data-page="page2"><i class="fa fa-refresh"></i> 刷新</a>
                                </div>
                                <div class="right-tab" style="overflow-y: auto">
                                    <table class="right-tab text-nowrap" data-page="page2"></table>
                                </div>
                            </div>
                        </div>
                        <div id="page3" class="tab-pane fade">
                            <form class="searchForm form">
                                <table class="table well">
                                    <tr>
                                        <td class="width-10"><label class="pull-right">提货/送货</label></td>
                                        <td class="width-15">
                                            <select name="receiveShip" class="form-control"></select>
                                        </td>
                                        <td class="width-10"><label class="pull-right">运输单号</label></td>
                                        <td class="width-15">
                                            <input name="transportNo" maxlength="20" class=" form-control"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">客户单号</label></td>
                                        <td class="width-15">
                                            <input name="customerNo" maxlength="20" class=" form-control"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">订单类型</label></td>
                                        <td class="width-15">
                                            <select name="orderType" class="form-control m-b">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('TMS_TRANSPORT_ORDER_TYPE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-10"><label class="pull-right">订单时间从</label></td>
                                        <td class="width-15">
                                            <div class='input-group date'>
                                                <input type='text' name="transportOrderTimeFm" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td class="width-10"><label class="pull-right">订单时间到</label></td>
                                        <td class="width-15">
                                            <div class='input-group date'>
                                                <input type='text' name="transportOrderTimeTo" class="form-control"/>
                                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                            </div>
                                        </td>
                                        <td class="width-10"><label class="pull-right">委托方</label></td>
                                        <td class="width-15">
                                            <sys:grid title="委托方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                      fieldId="tabPage3_principalCode" fieldName="principalCode" fieldKeyName="transportObjCode"
                                                      displayFieldId="tabPage3_principalName" displayFieldName="principalName" displayFieldKeyName="transportObjName"
                                                      fieldLabels="委托方编码|委托方名称" fieldKeys="transportObjCode|transportObjName"
                                                      searchLabels="委托方编码|委托方名称" searchKeys="transportObjCode|transportObjName"
                                                      queryParams="transportObjType|orgId" queryParamValues="principalType|baseOrgId"
                                                      cssClass="form-control"/>
                                        </td>
                                        <td class="width-10"><label class="pull-right">客户</label></td>
                                        <td class="width-15">
                                            <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                      fieldId="tabPage3_customerCode" fieldName="customerCode" fieldKeyName="transportObjCode"
                                                      displayFieldId="tabPage3_customerName" displayFieldName="customerName" displayFieldKeyName="transportObjName"
                                                      fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                                      searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                                      queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                                      cssClass="form-control"/>
                                        </td>
                                    </tr>
                                </table>
                            </form>
                            <div class="left" style="float: left;border: 1px solid #ddd;padding: 1px;margin-right: 5px;">
                                <div style="width: 100%;text-align: center"><h4>可选</h4></div>
                                <div class="toolbar">
                                    <a class="btn btn-primary search" data-page="page3"> 查询</a>
                                    <a class="btn btn-primary reset" data-page="page3"> 重置</a>
                                    <a class="btn btn-primary confirm" data-page="page3"> 确认</a>
                                    <a class="btn btn-primary confirm-all" data-page="page3"> 全确认</a>
                                    <a class="btn btn-default left-refresh" data-page="page3"><i class="fa fa-refresh"></i> 刷新</a>
                                </div>
                                <div class="left-tab" style="overflow-y: auto">
                                    <table class="left-tab text-nowrap" data-page="page3"></table>
                                </div>
                            </div>
                            <div class="right" style="float: left;border: 1px solid #ddd;padding: 1px;">
                                <div style="width: 100%;text-align: center"><h4>已选</h4></div>
                                <div class="toolbar">
                                    <a class="btn btn-primary un-confirm" data-page="page3"> 取消确认</a>
                                    <a class="btn btn-primary un-confirm-all" data-page="page3"> 全取消确认</a>
                                    <a class="btn btn-default right-refresh" data-page="page3"><i class="fa fa-refresh"></i> 刷新</a>
                                </div>
                                <div class="right-tab" style="overflow-y: auto">
                                    <table class="right-tab text-nowrap" data-page="page3"></table>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer" style="border: 0;">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<div id="selectedModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="padding-left: 10px">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title"><span class="dispatchSite" style="font-weight: bold;"></span>&nbsp;&nbsp;已配载订单</h4>
            </div>
            <div class="modal-body">
                <table id="selectedTable" class="text-nowrap"></table>
            </div>
            <div class="modal-footer" style="border: 0;">
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
                        <shiro:hasPermission name="order:tmDispatchOrder:uploadAnnex">
                            <a class="btn btn-primary btn-sm" id="uploadAnnex" onclick="uploadAnnex()">上传附件</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="order:tmDispatchOrder:removeAnnex">
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
<script type="text/template" id="tmDispatchOrderSiteTpl">//<!--
<tr id="tmDispatchOrderSiteList{{idx}}" data-index="{{idx}}">
    <td class="hide">
        <input type="hidden" id="tmDispatchOrderSiteList{{idx}}_id" name="tmDispatchOrderSiteList[{{idx}}].id" value="{{row.id}}"/>
        <input type="hidden" id="tmDispatchOrderSiteList{{idx}}_dispatchNo" name="tmDispatchOrderSiteList[{{idx}}].dispatchNo" value="{{row.dispatchNo}}"/>
        <input type="hidden" id="tmDispatchOrderSiteList{{idx}}_orgId" name="tmDispatchOrderSiteList[{{idx}}].orgId" value="{{row.orgId}}"/>
        <input type="hidden" id="tmDispatchOrderSiteList{{idx}}_recVer" name="tmDispatchOrderSiteList[{{idx}}].recVer" value="{{row.recVer}}"/>
        <input type="hidden" id="tmDispatchOrderSiteList{{idx}}_baseOrgId" name="tmDispatchOrderSiteList[{{idx}}].baseOrgId" value="{{row.baseOrgId}}"/>
        <input type="hidden" id="tmDispatchOrderSiteList{{idx}}_dispatchSeq" name="tmDispatchOrderSiteList[{{idx}}].dispatchSeq" value="{{row.dispatchSeq}}"/>
        <input type="hidden" id="tmDispatchOrderSiteList{{idx}}_outletName" name="tmDispatchOrderSiteList[{{idx}}].outletName" value="{{row.outletName}}"/>
        <input type="hidden" id="tmDispatchOrderSiteList{{idx}}_oldOutletCode" value="{{row.outletCode}}"/>
        <input type="hidden" id="tmDispatchOrderSiteList{{idx}}_oldReceiveShip" value="{{row.receiveShip}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td style="vertical-align: middle">
        {{row.dispatchSeq}}
    </td>
    <td>
        <sys:grid title="配送对象" url="${ctx}/tms/basic/tmTransportObj/outletGrid" cssClass="form-control required" readonly="true"
                  fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                  displayFieldId="tmDispatchOrderSiteList{{idx}}_outletCode" displayFieldName="tmDispatchOrderSiteList[{{idx}}].outletCode" displayFieldKeyName="transportObjCode" displayFieldValue="{{row.outletCode}}"
                  fieldLabels="配送对象编码|配送对象名称" fieldKeys="transportObjCode|transportObjName" searchLabels="配送对象编码|配送对象名称" searchKeys="transportObjCode|transportObjName"
                  queryParams="orgId|carrierCode" queryParamValues="baseOrgId|carrierCode"
                  afterSelect="transportObjSelect({{idx}})"/>
    </td>
    <td>
        {{row.outletName}}
    </td>
    <td>
        <select id="tmDispatchOrderSiteList{{idx}}_receiveShip" name="tmDispatchOrderSiteList[{{idx}}].receiveShip" class="form-control selectpicker" data-value="{{row.receiveShip}}" multiple>
            <c:forEach items="${fns:getDictList('TMS_RECEIVE_SHIP')}" var="dict">
            <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <a id="tmDispatchOrderSiteList{{idx}}Btn" class="btn btn-primary" onclick="openSelectModal({{idx}})" disabled> 选择订单</a>
        <a class="btn btn-primary" onclick="openSelectedModal({{idx}})"> 已选订单</a>
    </td>
</tr>//-->
</script>
</body>
</html>