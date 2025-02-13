<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输订单信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmDirectDispatchForm.js" %>
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
<form:form id="inputForm" modelAttribute="tmDirectDispatch" class="form">
    <form:hidden path="ids"/>
    <form:hidden path="dispatchOutletCode"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <%--调度信息--%>
    <div class="tabs-container" style="min-height: 180px;">
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">派车时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='dispatchTime'>
                                <input type='text' name="dispatchTime" class="form-control required"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">派车类型</label></td>
                        <td class="width-15">
                            <form:select path="dispatchType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_DISPATCH_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">班次</label></td>
                        <td class="width-15">
                            <form:input path="shift" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                        <td class="width-10"><label class="pull-right">月台</label></td>
                        <td class="width-15">
                            <form:input path="platform" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">承运商</label></td>
                        <td class="width-15">
                            <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode"
                                      displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName"
                                      fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="carrierType|baseOrgId"
                                      cssClass="form-control required" afterSelect="carrierSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">车牌号</label></td>
                        <td class="width-15">
                            <sys:grid title="车辆" url="${ctx}/tms/basic/tmVehicle/grid"
                                      fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                      displayFieldId="carNo" displayFieldName="carNo" displayFieldKeyName="carNo"
                                      fieldLabels="车牌号|承运商|设备类型" fieldKeys="carNo|carrierName|transportEquipmentTypeName"
                                      searchLabels="车牌号" searchKeys="carNo"
                                      queryParams="carrierCode|orgId|opOrgId" queryParamValues="carrierCode|baseOrgId|orgId"
                                      cssClass="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right">司机</label></td>
                        <td class="width-15">
                            <sys:grid title="司机" url="${ctx}/tms/basic/tmDriver/grid"
                                      fieldId="driver" fieldName="driver" fieldKeyName="code"
                                      displayFieldId="driverName" displayFieldName="driverName" displayFieldKeyName="name"
                                      fieldLabels="编码|姓名|承运商" fieldKeys="code|name|carrierName"
                                      searchLabels="编码|姓名" searchKeys="code|name"
                                      queryParams="carrierCode|orgId" queryParamValues="carrierCode|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">司机电话</label></td>
                        <td class="width-15">
                            <form:input path="driverTel" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">派车人</label></td>
                        <td class="width-15">
                            <form:input path="dispatcher" htmlEscape="false" class="form-control" maxlength="35"/>
                        </td>
                        <td class="width-10"><label class="pull-right">起始地</label></td>
                        <td class="width-15">
                            <sys:area id="startAreaId" name="startAreaId" labelName="startAreaName"
                                      allowSearch="true" cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">目的地</label></td>
                        <td class="width-15">
                            <sys:area id="endAreaId" name="endAreaId" labelName="endAreaName"
                                      allowSearch="true" cssClass="form-control"/>
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
        </div>
    </div>
</form:form>
<%--订单标签信息--%>
<div class="tabs-container" style="margin-left: 10px;max-height: 500px;">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#orderLabelInfo" aria-expanded="true">订单标签信息</a></li>
    </ul>
    <div class="tab-content">
        <%--订单标签信息--%>
        <div id="orderLabelInfo" class="tab-pane fade in active">
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmTransportOrderLabelEntity" class="form form-horizontal well clearfix">
                            <form:hidden path="transportIdList"/>
                            <table class="table table-striped table-bordered table-condensed">
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">运输订单：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="transportNo" htmlEscape="false" class=" form-control" maxlength="64" />
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">是否配载：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="hasDispatch" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">标签：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="labelNo" htmlEscape="false" class=" form-control" maxlength="64" />
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">商品：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="skuCode" htmlEscape="false" class=" form-control" maxlength="64" />
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">发货方：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:grid title="选择发货方" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="shipCode" fieldName="shipCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="shipName" displayFieldName="shipName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="发货方编码|发货方名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="发货方编码|发货方名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="shipType|orgId"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">收货方：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:grid title="选择收货方" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="收货方编码|收货方名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="收货方编码|收货方名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="consigneeType|orgId"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">提货网点：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:grid title="选择网点" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="receiveOutletCode" fieldName="receiveOutletCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="receiveOutletName" displayFieldName="receiveOutletName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="outletType|orgId"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">配送网点：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:grid title="选择网点" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="outletCode" fieldName="outletCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="outletName" displayFieldName="outletName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="outletType|orgId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">发货城市：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:area id="shipCityId" name="shipCityId" labelName="shipCity"
                                                  cssClass="form-control" allowSearch="true" showFullName="true"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">发货地址：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="shipAddress" htmlEscape="false" class=" form-control" maxlength="64" />
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">收货城市：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:area id="consigneeCityId" name="consigneeCityId" labelName="consigneeCity"
                                                  cssClass="form-control" allowSearch="true" showFullName="true"/>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">收货地址：</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:input path="consigneeAddress" htmlEscape="false" class=" form-control" maxlength="64" />
                                    </td>
                                </tr>
                            </table>
                            <div class="col-xs-12 col-sm-6 col-md-4">
                                <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                                <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-refresh"></i> 重置</a>
                            </div>
                        </form:form>
                    </div>
                </div>
            </div>
            <table id="orderLabelTable" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>