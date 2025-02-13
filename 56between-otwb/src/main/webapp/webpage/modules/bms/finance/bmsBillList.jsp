<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>账单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="bmsBillList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="opOrgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">账单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsBill" cssClass="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">账单号</label></td>
                                    <td class="width-15">
                                        <form:input path="confirmNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BILL_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">结算对象</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control "
                                                  fieldId="settleObjCode" fieldName="settleObjCode" fieldKeyName="settleObjectCode"
                                                  displayFieldId="settleName" displayFieldName="settleName" displayFieldKeyName="settleObjectName"
                                                  fieldLabels="结算对象代码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                                                  searchLabels="结算对象代码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                                                  queryParams="orgId" queryParamValues="opOrgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">结算对象名称</label></td>
                                    <td class="width-15">
                                        <form:input path="settleObjName" htmlEscape="false" class="form-control "/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">创建时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='createTimeFm'>
                                            <input type='text' name="createTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">创建时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='createTimeTo'>
                                            <input type='text' name="createTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">创建人</label></td>
                                    <td class="width-15">
                                        <form:input path="createBy.name" htmlEscape="false" class="form-control "/>
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
                <shiro:hasPermission name="bms:bmsBill:add">
                    <button id="add" class="btn btn-primary" onclick="add()">新增</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsBill:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsBill:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsBill:export">
                    <button id="export" class="btn btn-info" disabled onclick="exportExcel()"><i class="fa fa-file-excel-o"></i> 导出费用明细</button>
                    <button id="export1" class="btn btn-info" disabled onclick="exportExcel1()"><i class="fa fa-file-excel-o"></i> 导出费用统计</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsBillTable" data-toolbar="#toolbar" class="text-nowrap"></table>
            <div id="footer" style="display: none;">
                <table class="table-condensed" style="border-collapse: collapse;">
                    <tr>
                        <td><label class="pull-left">总金额：<span id="total"></span></label></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>