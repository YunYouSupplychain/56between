<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存数据管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsInventoryDataList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="orgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">库存数据列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsInventoryDataEntity" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">日期从</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='dateFm'>
                                            <input type='text' name="dateFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">日期到</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='dateTo'>
                                            <input type='text' name="dateTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">供应商编码</label></td>
                                    <td class="width-15">
                                        <form:input path="supplierCode" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">供应商名称</label></td>
                                    <td class="width-15">
                                        <form:input path="supplierName" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">货主编码</label></td>
                                    <td class="width-15">
                                        <form:input path="ownerCode" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">货主名称</label></td>
                                    <td class="width-15">
                                        <form:input path="ownerName" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品编码</label></td>
                                    <td class="width-15">
                                        <form:input path="skuCode" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商品名称</label></td>
                                    <td class="width-15">
                                        <form:input path="skuName" htmlEscape="false" maxlength="35" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-8"><label class="pull-right">品类</label></td>
                                    <td class="width-12">
                                        <sys:grid title="选择商品分类" url="${ctx}/bms/basic/skuClassification/grid" cssClass="form-control"
                                                  fieldId="skuClass" fieldName="skuClass" fieldKeyName="code"
                                                  displayFieldId="skuClassName" displayFieldName="skuClassName" displayFieldKeyName="name"
                                                  fieldLabels="编码|名称" fieldKeys="code|name"
                                                  searchLabels="编码|名称" searchKeys="code|name"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">库存模式</label></td>
                                    <td class="width-15">
                                        <form:select path="businessModel" htmlEscape="false" cssClass="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BUSINESS_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width:8%;vertical-align:middle;"><label class="pull-right">是否参与计费</label></td>
                                    <td style="width:12%;vertical-align:middle;">
                                        <form:select path="isFee" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                <shiro:hasPermission name="business:bmsInventoryData:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="business:bmsInventoryData:cancelFee">
                    <button id="cancelFee" class="btn btn-primary" disabled onclick="isFee('N')"> 不计费</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="business:bmsInventoryData:addFee">
                    <button id="addFee" class="btn btn-primary" disabled onclick="isFee('Y')"> 加入计费</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="business:bmsInventoryData:pull">
                    <button id="pull" class="btn btn-primary" onclick="pull()"> 同步数据</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="business:bmsInventoryData:import">
                    <button id="btnImport" class="btn btn-info"><i class="fa fa-file-excel-o"></i> 导入</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="business:bmsInventoryData:export">
                    <button id="export" class="btn btn-info" onclick="exportExcel()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo"><i class="fa fa-search"></i> 检索</a>
            </div>
            <!-- 表格 -->
            <table id="bmsInventoryDataTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 同步数据弹出窗 -->
<div id="pullModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:700px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">同步数据</h4>
            </div>
            <div class="modal-body">
                <form id="pullDataForm" class="form" enctype="multipart/form-data">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td style="width: 80px;padding-top: 5px;"><label class="pull-right asterisk">日期从</label></td>
                            <td style="width: 220px;padding-top: 5px;">
                                <div class='input-group form_datetime' id='fmDate'>
                                    <input type='text' name="fmDate" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td style="width: 80px;padding-top: 5px;"><label class="pull-right asterisk">日期到</label></td>
                            <td style="width: 220px;padding-top: 5px;">
                                <div class='input-group form_datetime' id='toDate'>
                                    <input type='text' name="toDate" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 80px;padding-top: 5px;"><label class="pull-right">数据来源</label></td>
                            <td style="width: 220px;padding-top: 5px;">
                                <select id="dataSources" name="dataSources" class="form-control">
                                    <option></option>
                                    <option value="SMS">对账</option>
                                    <option value="WMS">仓储</option>
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="pullData()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>