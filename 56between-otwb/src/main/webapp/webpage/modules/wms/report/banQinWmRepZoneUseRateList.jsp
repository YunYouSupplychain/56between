<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库位使用率管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmRepZoneUseRateList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">库位使用率报表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <form:form id="searchForm" modelAttribute="wmRepZoneUseRate" cssClass="form">
                <form:hidden path="orgId"/>
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">库区</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                           fieldId="zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="" allowInput="true"
                                           displayFieldId="zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue=""
                                           selectButtonId="zoneSelectId" deleteButtonId="zoneDeleteId"
                                           fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName"
                                           searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName"/>
                        </td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                    </tr>
                    </tbody>
                </table>
            </form:form>

            <!-- 工具栏 -->
            <div id="toolbar">
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <shiro:hasPermission name="report:wmRepZoneUseRate:export">
                    <a id="export" class="btn btn-info"><i class="fa fa-file-excel-o"></i> 导出</a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="wmRepZoneUseRateTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>