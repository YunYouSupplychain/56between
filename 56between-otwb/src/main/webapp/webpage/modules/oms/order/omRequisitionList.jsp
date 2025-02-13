<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>调拨单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="omRequisitionList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="ownerType" value="OWNER"/>
    <input id="parentId" value="${fns:getUser().office.id}" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">调拨单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="omRequisitionEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">调拨单号</label></td>
                                    <td class="width-15">
                                        <form:input path="reqNo" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_RO_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单日期(从)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateFm'>
                                            <input type='text' name="orderDateFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单日期(到)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateTo'>
                                            <input type='text' name="orderDateTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">订单类型</label></td>
                                    <td class="width-15">
                                        <form:select path="orderType" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_BUSINESS_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">到货时间(从)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='planArrivalTimeFm'>
                                            <input type='text' name="planArrivalTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">到货时间(到)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='planArrivalTimeTo'>
                                            <input type='text' name="planArrivalTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">货主</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control" allowInput="true"
                                                       fieldId="owner" fieldName="owner" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ownerType"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">源仓库</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/sys/office/companyData" title="选择仓库" cssClass="form-control"
                                                       fieldId="fmOrgId" fieldName="fmOrgId" fieldKeyName="id" fieldValue=""
                                                       displayFieldId="fmOrgName" displayFieldName="fmOrgName" displayFieldKeyName="name" displayFieldValue=""
                                                       selectButtonId="fmOrgSBtnId" deleteButtonId="fmOrgDBtnId"
                                                       fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                                       searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                                       queryParams="id" queryParamValues="parentId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">目标仓库</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/sys/office/companyData" title="选择仓库" cssClass="form-control"
                                                       fieldId="toOrgId" fieldName="toOrgId" fieldKeyName="id" fieldValue=""
                                                       displayFieldId="toOrgName" displayFieldName="toOrgName" displayFieldKeyName="name" displayFieldValue=""
                                                       selectButtonId="toOrgSBtnId" deleteButtonId="toOrgDBtnId"
                                                       fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                                       searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                                       queryParams="id" queryParamValues="parentId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">运输方式</label></td>
                                    <td class="width-15">
                                        <form:select path="transportMode" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_TRANSPORT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
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
                <shiro:hasPermission name="oms:order:requisition:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="oms:order:requisition:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="oms:order:requisition:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="oms:order:requisition:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="oms:order:requisition:audit:cancel">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()">取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="oms:order:requisition:task:gen">
                    <button id="createTask" class="btn btn-primary" disabled onclick="createTask()"> 生成任务</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="oms:order:requisition:close">
                    <button id="closeOrder" class="btn btn-primary" disabled onclick="closeOrder()"> 关闭</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="oms:order:requisition:cancel">
                    <button id="cancelOrder" class="btn btn-primary" disabled onclick="cancelOrder()"> 取消</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>

            <!-- 表格 -->
            <table id="omRequisitionTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>