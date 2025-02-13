<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库区管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinCdWhZoneList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">库区列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinCdWhZone">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="库区编码">库区编码</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="zoneCode" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="库区名称">库区名称</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="zoneName" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="库区类型">库区类型</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="type" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_ZONE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="所属区域">所属区域</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhArea/grid" title="选择区域" cssClass="form-control"
                                                       fieldId="areaCode" fieldName="areaCode" fieldKeyName="areaCode" fieldValue="" allowInput="true"
                                                       displayFieldId="areaName" displayFieldName="areaName" displayFieldKeyName="areaName" displayFieldValue=""
                                                       selectButtonId="areaSelectId" deleteButtonId="areaDeleteId"
                                                       fieldLabels="区域编码|区域名称" fieldKeys="areaCode|areaName" inputSearchKey="areaCodeAndName"
                                                       searchLabels="区域编码|区域名称" searchKeys="areaCode|areaName">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="basicdata:banQinCdWhZone:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhZone:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhZone:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhZone:import">
                    <button id="btnImport" class="btn btn-info"><i class="fa fa-file-excel-o"></i> 导入</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basicdata:banQinCdWhZone:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i>导出</button>
                </shiro:hasPermission>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="basicdata:banQinCdWhZone:printBarcode">
                            <li><a id="printBarcode" onclick="printBarcode()">打印库区标签</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="basicdata:banQinCdWhZone:printQrcode">
                            <li><a id="printQrcode" onclick="printQrcode()">打印库区标签(二维码)</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinCdWhZoneTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>