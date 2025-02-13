<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输设备类型管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysTmsTransportEquipmentTypeList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">运输设备类型列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysTmsTransportEquipmentTypeEntity" class="form form-horizontal well clearfix">
                            <table class="table table-striped table-bordered table-condensed">
                                <tr>
                                    <td class="width-10"><label class="pull-right">设备类型编码：</label></td>
                                    <td class="width-15">
                                        <form:input path="transportEquipmentTypeCode" htmlEscape="false" class=" form-control" maxlength="64"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">中文名称：</label></td>
                                    <td class="width-15">
                                        <form:input path="transportEquipmentTypeNameCn" htmlEscape="false" class=" form-control" maxlength="64"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">英文名称：</label></td>
                                    <td class="width-15">
                                        <form:input path="transportEquipmentTypeNameEn" htmlEscape="false" class=" form-control" maxlength="64"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">温度类别：</label></td>
                                    <td class="width-15">
                                        <form:select path="temperatureType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_TEMPERATURE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">启用停用：</label></td>
                                    <td class="width-15">
                                        <form:select path="delFlag" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
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
                <shiro:hasPermission name="sys:common:tms:transportEquipmentType:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:transportEquipmentType:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:transportEquipmentType:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:transportEquipmentType:enable">
                    <button id="enable" class="btn btn-primary" disabled onclick="enable('0')"> 启用</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:transportEquipmentType:unable">
                    <button id="unable" class="btn btn-primary" disabled onclick="enable('1')"> 停用</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:tms:transportEquipmentType:sync">
                    <button id="syncSelect" class="btn btn-primary" onclick="syncSelect()"> 同步选择数据</button>
                    <button id="syncAll" class="btn btn-primary" onclick="syncAll()"> 同步全部数据</button>
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