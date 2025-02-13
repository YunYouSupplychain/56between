<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>车辆信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmVehicleList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="transportObjType" value="CARRIER"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">车辆信息列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmVehicleEntity">
                            <form:hidden path="orgId" />
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">车牌号</label></td>
                                    <td class="width-15">
                                        <form:input path="carNo" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">运输设备类型</label></td>
                                    <td class="width-15">
                                        <sys:grid title="运输设备类型" url="${ctx}/tms/basic/tmTransportEquipmentType/grid"
                                                  fieldId="transportEquipmentTypeCode" fieldName="transportEquipmentTypeCode" fieldKeyName="transportEquipmentTypeCode"
                                                  displayFieldId="transportEquipmentTypeName" displayFieldName="transportEquipmentTypeName" displayFieldKeyName="transportEquipmentTypeNameCn"
                                                  fieldLabels="运输设备类型编码|运输设备类型名称" fieldKeys="transportEquipmentTypeCode|transportEquipmentTypeNameCn"
                                                  searchLabels="运输设备类型编码|运输设备类型名称" searchKeys="transportEquipmentTypeCode|transportEquipmentTypeNameCn"
                                                  queryParams="orgId" queryParamValues="orgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="transportObjType|orgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">车型</label></td>
                                    <td class="width-15">
                                        <sys:grid title="车型" url="${ctx}/tms/basic/tmVehicleType/grid" cssClass="form-control"
                                                  fieldId="vehicleType" fieldName="vehicleType" fieldKeyName="code"
                                                  displayFieldId="vehicleTypeName" displayFieldName="vehicleTypeName" displayFieldKeyName="name"
                                                  fieldLabels="编码|名称" fieldKeys="code|name"
                                                  searchLabels="编码|名称" searchKeys="code|name"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">车辆类型</label></td>
                                    <td class="width-15">
                                        <form:select path="carType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_CAR_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_VEHICLE_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="basic:tmVehicle:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmVehicle:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmVehicle:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmVehicle:enable">
                    <button id="enable" class="btn btn-primary" disabled onclick="enable('0')"> 启用</button>
                    <button id="unable" class="btn btn-primary" disabled onclick="enable('1')"> 停用</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmVehicle:updateStatus">
                    <button id="updateStatus0" class="btn btn-primary" disabled onclick="updateStatus('0')"> 可用</button>
                    <button id="updateStatus1" class="btn btn-primary" disabled onclick="updateStatus('1')"> 不可用</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmVehicle:isAvailable">
                    <button id="isAvailable" class="btn btn-primary" disabled onclick="isAvailable('0')"> 可用/不可用</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmVehicleTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>