<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>上架任务管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmTaskPaList.js" %>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">上架任务列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmTaskPaEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="上架任务Id：">上架任务Id</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="paId" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="上架任务行号：">上架任务行号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="lineNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="状态：">状态</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_TASK_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="订单号：">订单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="orderNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="货主：">货主</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                       queryParams="ebcuType" queryParamValues="ownerType" inputSearchKey="codeAndName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="商品：">商品</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                       fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                       displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                       selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                       queryParams="ownerCode" queryParamValues="ownerCode">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="批次号：">批次号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="lotNum" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="源库位：">源库位</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择源库位" cssClass="form-control"
                                                       fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                       displayFieldId="fmLoc" displayFieldName="fmLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                       selectButtonId="fmLocSelectId" deleteButtonId="fmLocDeleteId"
                                                       fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                       searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="源跟踪号：">源跟踪号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="fmId" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="推荐库位：">推荐库位</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择推荐库位" cssClass="form-control"
                                                       fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                       displayFieldId="suggestLoc" displayFieldName="suggestLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                       selectButtonId="suggestLocSelectId" deleteButtonId="suggestLocDeleteId"
                                                       fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                       searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
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
                <shiro:hasPermission name="task:banQinWmTaskPa:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除上架任务</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="task:banQinWmTaskPa:putAway">
                    <button id="confirm" class="btn btn-primary" disabled onclick="confirm()">上架确认</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="task:banQinWmTaskPa:printPaTask">
                    <button id="confirm" class="btn btn-primary" onclick="printPaTask()">打印上架任务单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="task:banQinWmTaskPa:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmTaskPaTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>