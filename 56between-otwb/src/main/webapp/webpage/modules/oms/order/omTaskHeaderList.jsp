<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>作业任务管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="omTaskHeaderList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="ownerType" value="OWNER"/>
    <input type="hidden" id="customerType" value="CONSIGNEE"/>
    <input type="hidden" id="supplierType" value="SUPPLIER"/>
    <input type="hidden" id="carrierType" value="CARRIER"/>
    <input type="hidden" id="principalType" value="CUSTOMER"/>
    <input type="hidden" id="parentId"/>
    <input type="hidden" id="orgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">作业任务列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="omTaskHeaderEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">作业任务号</label></td>
                                    <td class="width-15">
                                        <form:input path="taskNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">供应链订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="chainNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商流订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="businessNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">任务状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_TASK_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">任务类型</label></td>
                                    <td class="width-15">
                                        <form:select path="taskType" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_TASK_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">下发系统</label></td>
                                    <td class="width-15">
                                        <form:select path="pushSystem" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_PUSH_SYSTEM')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">下发机构</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择机构" url="${ctx}/sys/office/companyData" cssClass="form-control"
                                                  fieldId="warehouse" fieldName="warehouse" fieldKeyName="id"
                                                  displayFieldId="warehouseName" displayFieldName="warehouseName" displayFieldKeyName="name"
                                                  fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                                  searchLabels="机构编码|机构名称" searchKeys="code|name"
                                                  queryParams="id" queryParamValues="parentId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">订单日期(从)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateFm'>
                                            <input type='text' name="orderDateFm" class="form-control" title="订单日期(从)"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单日期(到)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateTo'>
                                            <input type='text' name="orderDateTo" class="form-control" title="订单日期(到)"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">货主</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择货主" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                                  fieldId="owner" fieldName="owner" fieldKeyName="ebcuCustomerNo"
                                                  displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn"
                                                  fieldLabels="客户编码|客户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                  searchLabels="客户编码|客户名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                  queryParams="ebcuType|orgId" queryParamValues="ownerType|orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择客户" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                                  fieldId="customer" fieldName="customer" fieldKeyName="ebcuCustomerNo"
                                                  displayFieldId="customerName" displayFieldName="customerName" displayFieldKeyName="ebcuNameCn"
                                                  fieldLabels="客户编码|客户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                  searchLabels="客户编码|客户名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                  queryParams="ebcuType|orgId" queryParamValues="customerType|orgId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">供应商</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择供应商" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                                  fieldId="supplierCode" fieldName="supplierCode" fieldKeyName="ebcuCustomerNo"
                                                  displayFieldId="supplierName" displayFieldName="supplierName" displayFieldKeyName="ebcuNameCn"
                                                  fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                  searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                  queryParams="ebcuType|orgId" queryParamValues="supplierType|orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">委托方</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择委托方" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                                  fieldId="principal" fieldName="principal" fieldKeyName="ebcuCustomerNo"
                                                  displayFieldId="principalName" displayFieldName="principalName" displayFieldKeyName="ebcuNameCn"
                                                  fieldLabels="委托方编码|委托方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                  searchLabels="委托方编码|委托方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                  queryParams="ebcuType|orgId" queryParamValues="principalType|orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择承运商" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                                  fieldId="carrier" fieldName="carrier" fieldKeyName="ebcuCustomerNo"
                                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn"
                                                  fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                  searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                  queryParams="ebcuType|orgId" queryParamValues="carrierType|orgId"/>
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
                <shiro:hasPermission name="order:omTaskHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omTaskHeader:merge">
                    <button id="mergeTask" class="btn btn-primary" disabled onclick="mergeTask()"> 任务合并</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omTaskHeader:restore">
                    <button id="restoreTask" class="btn btn-primary" disabled onclick="restoreTask()">任务还原</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omTaskHeader:alloc">
                    <button id="allocTask" class="btn btn-primary" disabled onclick="allocTask()"> 分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omTaskHeader:unAlloc">
                    <button id="unAllocTask" class="btn btn-primary" disabled onclick="unAllocTask()"> 取消分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omTaskHeader:carrierAlloc">
                    <button id="carrierAlloc" class="btn btn-primary" disabled onclick="carrierAlloc()">承运商分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omTaskHeader:carrierDesignate">
                    <button id="carrierDesignate" class="btn btn-primary" disabled data-toggle="modal" data-target="#carrierDesignateModal">承运商指定</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omTaskHeader:send">
                    <button id="sendTask" class="btn btn-primary" disabled onclick="sendTask()"> 任务下发</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="omTaskHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 承运商指定弹出窗 -->
<div id="carrierDesignateModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:400px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">承运商指定</h4>
            </div>
            <div class="modal-body">
                <form id="carrierDesignateForm" class="form">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td style="width: 80px;padding-top: 5px;"><label class="pull-right" style="">承运商</label></td>
                            <td style="width: 220px;padding-top: 5px;">
                                <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择承运商" cssClass="form-control" allowInput="true"
                                               fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                               displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId" inputSearchKey="codeAndName"
                                               fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                               queryParams="ebcuType" queryParamValues="carrierType">
                                </sys:popSelect>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="carrierDesignate()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>