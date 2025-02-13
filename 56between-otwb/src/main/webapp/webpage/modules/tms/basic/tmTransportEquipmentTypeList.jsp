<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输设备类型管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmTransportEquipmentTypeList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">运输设备类型列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmTransportEquipmentTypeEntity">
                            <form:hidden path="orgId" />
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">设备类型编码</label></td>
                                    <td class="width-15">
                                        <form:input path="transportEquipmentTypeCode" htmlEscape="false" class="form-control" maxlength="64"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">中文名称</label></td>
                                    <td class="width-15">
                                        <form:input path="transportEquipmentTypeNameCn" htmlEscape="false" class="form-control" maxlength="64"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">英文名称</label></td>
                                    <td class="width-15">
                                        <form:input path="transportEquipmentTypeNameEn" htmlEscape="false" class="form-control" maxlength="64"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">温度类别</label></td>
                                    <td class="width-15">
                                        <form:select path="temperatureType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_TEMPERATURE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="basic:tmTransportEquipmentType:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmTransportEquipmentType:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmTransportEquipmentType:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmTransportEquipmentType:enable">
                    <button id="enable" class="btn btn-primary" disabled onclick="enable('0')"> 启用</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmTransportEquipmentType:unable">
                    <button id="unable" class="btn btn-primary" disabled onclick="enable('1')"> 停用</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmTransportEquipmentTypeTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>