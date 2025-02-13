<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>供应链作业任务管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        $(document).ready(function () {
            init();
            $('#omTaskDetailTable').bootstrapTable("load", ${fns:toJson(omTaskHeaderEntity.omTaskDetailList)});
        });

        function init() {
            $('#taskNo').prop('readonly', true);
            $('#auditBy').prop('readonly', true);
            $('#orderDate').datetimepicker({format: "YYYY-MM-DD"});
            initTable();
        }

        function initTable() {
            $('#omTaskDetailTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "client",// client客户端分页，server服务端分页
                queryParams: function (params) {
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.orgId = jp.getCurrentOrg().orgId;
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'chainNo',
                    title: '供应链订单号',
                    sortable: true
                }, {
                    field: 'lineNo',
                    title: '供应链订单行号',
                    sortable: true
                }, {
                    field: 'allocStatus',
                    title: '分配状态',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_TK_ALLOC_STATUS'))}, value, "-");
                    }
                }, {
                    field: 'skuCode',
                    title: '商品编码',
                    sortable: true
                }, {
                    field: 'skuName',
                    title: '商品名称',
                    sortable: true
                }, {
                    field: 'spec',
                    title: '规格',
                    sortable: true
                }, {
                    field: 'riceNum',
                    title: '属性',
                    sortable: true
                }, {
                    field: 'unit',
                    title: '单位',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_ITEM_UNIT'))}, value, "-");
                    }
                }, {
                    field: 'qty',
                    title: '数量',
                    sortable: true
                }, {
                    field: 'auxiliaryUnit',
                    title: '辅助单位',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_UNIT'))}, value, "-");
                    }
                }, {
                    field: 'auxiliaryQty',
                    title: '辅助单位数量',
                    sortable: true
                }]
            })
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="omTaskHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="min-height: 250px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#orderInfo" aria-expanded="true">订单信息</a></li>
            <li class=""><a data-toggle="tab" href="#logisticsInfo" aria-expanded="true">物流信息</a></li>
            <li class=""><a data-toggle="tab" href="#businessInfo" aria-expanded="true">商业信息</a></li>
            <li class=""><a data-toggle="tab" href="#settleInfo" aria-expanded="true">结算信息</a></li>
            <li class=""><a data-toggle="tab" href="#auditInfo" aria-expanded="true">审核信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="orderInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">作业任务号</label></td>
                        <td class="width-15">
                            <form:input path="taskNo" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">订单日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='orderDate' disabled="true">
                                <input type='text' name="orderDate" readonly class="form-control required"
                                       value="<fmt:formatDate value="${omTaskHeaderEntity.orderDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">任务状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control" disabled="true">
                                <form:options items="${fns:getDictList('OMS_TASK_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">任务类型</label></td>
                        <td class="width-15">
                            <form:select path="taskType" class="form-control required" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_TASK_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">供应链订单号</label></td>
                        <td class="width-15">
                            <form:input path="chainNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商流订单号</label></td>
                        <td class="width-15">
                            <form:input path="businessNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">客户订单号</label></td>
                        <td class="width-15">
                            <form:input path="customerNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预计到货时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='arrivalTime'>
                                <input type='text' name="arrivalTime" class="form-control"
                                       value="<fmt:formatDate value="${omTaskHeaderEntity.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">货主</label></td>
                        <td class="width-15">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择供应商" cssClass="form-control required"
                                           fieldId="owner" fieldName="owner"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omTaskHeaderEntity.owner}"
                                           displayFieldId="ownerName" displayFieldName="ownerName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omTaskHeaderEntity.ownerName}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="ownerType">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">客户</label></td>
                        <td class="width-15">
                            <input id="customerType" value="CONSIGNEE" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择客户" cssClass="form-control"
                                           fieldId="customer" fieldName="customer"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omTaskHeaderEntity.customer}"
                                           displayFieldId="customerName" displayFieldName="customerName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omTaskHeaderEntity.customerName}"
                                           selectButtonId="customerSelectId" deleteButtonId="customerDeleteId"
                                           fieldLabels="客户编码|客户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="客户编码|客户名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="customerType">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">供应商</label></td>
                        <td class="width-15">
                            <input id="supplierType" value="SUPPLIER" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择供应商" cssClass="form-control"
                                           fieldId="supplierCode" fieldName="supplierCode"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omTaskHeaderEntity.supplierCode}"
                                           displayFieldId="supplierName" displayFieldName="supplierName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omTaskHeaderEntity.supplierName}"
                                           selectButtonId="supplierSelectId" deleteButtonId="supplierDeleteId"
                                           fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="supplierType">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">委托方</label></td>
                        <td class="width-15">
                            <input id="principalType" value="CUSTOMER" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择委托方" cssClass="form-control"
                                           fieldId="principal" fieldName="principal"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omTaskHeaderEntity.principal}"
                                           displayFieldId="principalName" displayFieldName="principalName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omTaskHeaderEntity.principalName}"
                                           selectButtonId="principalSelectId" deleteButtonId="principalDeleteId"
                                           fieldLabels="委托方编码|委托方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="委托方编码|委托方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="principalType">
                            </sys:popSelect>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">下发机构</label></td>
                        <td class="width-15">
                            <input id="parentId" value="${fns:getUser().office.id}" type="hidden">
                            <sys:popSelect url="${ctx}/sys/office/companyData" title="选择下发机构" cssClass="form-control required"
                                           fieldId="warehouse" fieldName="warehouse"
                                           fieldKeyName="id" fieldValue="${omTaskHeaderEntity.warehouse}"
                                           displayFieldId="warehouseName" displayFieldName="warehouseName"
                                           displayFieldKeyName="name" displayFieldValue="${omTaskHeaderEntity.warehouseName}"
                                           selectButtonId="outWarhouseSelectId" deleteButtonId="outWarhouseDeleteId"
                                           fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                           searchLabels="机构编码|机构名称" searchKeys="code|name"
                                           queryParams="id" queryParamValues="parentId">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">收货方</label></td>
                        <td class="width-15">
                            <input type="hidden" id="consigneeType" value="CONSIGNEE"/>
                            <sys:grid title="选择收货方" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                      fieldId="consigneeCode" fieldName="consigneeCode"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omTaskHeaderEntity.consigneeCode}"
                                      displayFieldId="consigneeName" displayFieldName="consigneeName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omTaskHeaderEntity.consigneeName}"
                                      fieldLabels="收货方编码|收货方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="收货方编码|收货方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="consigneeType|orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发货方</label></td>
                        <td class="width-15">
                            <input type="hidden" id="shipperType" value="SHIPPER"/>
                            <sys:grid title="选择发货方" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                      fieldId="shipperCode" fieldName="shipperCode"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omTaskHeaderEntity.shipperCode}"
                                      displayFieldId="shipperName" displayFieldName="shipperName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omTaskHeaderEntity.shipperName}"
                                      fieldLabels="发货方编码|发货方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="发货方编码|发货方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="shipperType|orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">店铺</label></td>
                        <td class="width-15">
                            <form:input path="shop" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货人</label></td>
                        <td class="width-15">
                            <form:input path="consignee" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="consigneeTel" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货区域</label></td>
                        <td class="width-15">
                            <sys:treeselect title="选择收货区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="consigneeArea" name="consigneeArea" value="${omTaskHeaderEntity.consigneeArea}"
                                            labelName="area.name" labelValue="${omTaskHeaderEntity.consigneeAreaName}"/>
                        </td>
                        <td class="width-10"><label class="pull-right">三级地址</label></td>
                        <td class="width-15">
                            <form:input path="consigneeAddressArea" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货地址</label></td>
                        <td class="width-15" colspan="7">
                            <form:input path="consigneeAddress" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">发货人</label></td>
                        <td class="width-15">
                            <form:input path="shipper" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="shipperTel" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发货区域</label></td>
                        <td class="width-15">
                            <sys:treeselect title="选择发货区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="shipperArea" name="shipperArea" value="${omTaskHeaderEntity.shipperArea}"
                                            labelName="area.name" labelValue="${omTaskHeaderEntity.shipperAreaName}"/>
                        </td>
                        <td class="width-10"><label class="pull-right">三级地址</label></td>
                        <td class="width-15">
                            <form:input path="shipperAddressArea" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">发货地址</label></td>
                        <td class="width-15" colspan="7">
                            <form:input path="shipperAddress" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">备注信息</label></td>
                        <td class="width-15" colspan="7">
                            <form:input path="remarks" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="logisticsInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">物流单号</label></td>
                        <td class="width-15">
                            <form:input path="logisticsNo" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">运输方式</label></td>
                        <td class="width-15">
                            <form:select path="transportMode" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TRANSPORT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">承运商</label></td>
                        <td class="width-15">
                            <input id="carrierType" value="CARRIER" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择承运商" cssClass="form-control"
                                           fieldId="carrier" fieldName="carrier"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omTaskHeaderEntity.carrier}"
                                           displayFieldId="carrierName" displayFieldName="carrierName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omTaskHeaderEntity.carrierName}"
                                           selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                           fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="carrierType">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">车牌号</label></td>
                        <td class="width-15">
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">司机</label></td>
                        <td class="width-15">
                            <form:input path="driver" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="contactTel" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">运费金额</label></td>
                        <td class="width-15">
                            <form:input path="freightCharge" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0);"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="businessInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">税号</label></td>
                        <td class="width-15">
                            <form:input path="taxNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发票抬头</label></td>
                        <td class="width-15">
                            <form:input path="invoice" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发票类型</label></td>
                        <td class="width-15">
                            <form:select path="invoiceType" cssClass="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('BMS_INVOICE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">公司名称</label></td>
                        <td class="width-15">
                            <form:input path="companyName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">开户银行名称</label></td>
                        <td class="width-15">
                            <form:input path="depositBank" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">开户银行账号</label></td>
                        <td class="width-15">
                            <form:input path="depositBankAccount" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="registeredTelephone" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                        <td class="width-10"><label class="pull-right">注册地址</label></td>
                        <td class="width-15">
                            <form:input path="registeredArea" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">纳税人识别号</label></td>
                        <td class="width-15">
                            <form:input path="taxpayerIdentityNumber" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收票人名称</label></td>
                        <td class="width-15">
                            <form:input path="ticketCollectorName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收票人手机</label></td>
                        <td class="width-15">
                            <form:input path="ticketCollectorPhone" htmlEscape="false" class="form-control" maxlength="20"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收票人地址</label></td>
                        <td class="width-15">
                            <form:input path="ticketCollectorAddress" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="settleInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">预付金额</label></td>
                        <td class="width-15">
                            <form:input path="prepaidAmount" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">合同号</label></td>
                        <td class="width-15">
                            <form:input path="contractNo" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">结算对象</label></td>
                        <td class="width-15">
                            <input id="settlementType" value="SETTLEMENT" type="hidden">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择结算方" cssClass="form-control"
                                           fieldId="settlement" fieldName="settlement"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omTaskHeaderEntity.settlement}"
                                           displayFieldId="settlementName" displayFieldName="settlementName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omTaskHeaderEntity.settlementName}"
                                           selectButtonId="settlementSelectId" deleteButtonId="settlementDeleteId"
                                           fieldLabels="结算方编码|结算方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="结算方编码|结算方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="settlementType">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">结算方式</label></td>
                        <td class="width-15">
                            <form:select path="settleMode" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('BMS_SETTLEMENT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">币种</label></td>
                        <td class="width-15">
                            <form:select path="currency" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_CURRENCY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">汇率</label></td>
                        <td class="width-15">
                            <form:input path="exchangeRate" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="auditInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">制单人</label></td>
                        <td class="width-15">
                            <form:input path="preparedBy" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">创建人</label></td>
                        <td class="width-15">
                            <input name="createBy.name" htmlEscape="false" class="form-control" readonly>
                        </td>
                        <td class="width-10"><label class="pull-right">更新人</label></td>
                        <td class="width-15">
                            <input name="updateBy.name" htmlEscape="false" class="form-control" readonly>
                        </td>
                        <td class="width-10"><label class="pull-right">审核人</label></td>
                        <td class="width-15">
                            <form:input path="auditBy" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">销售人</label></td>
                        <td class="width-15">
                            <form:input path="saleBy" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">创建时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime'>
                                <input id='createDate' name="createDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omTaskHeaderEntity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">更新时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime'>
                                <input id='updateDate' name="updateDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omTaskHeaderEntity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">机构</label></td>
                        <td class="width-15">
                            <form:input path="orgName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="tabs-container" style="min-height: 300px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">供应链作业任务明细</a></li>
        </ul>
        <div class="tab-content">
            <div id="tab-1" class="tab-pane fade in active">
                <table id="omTaskDetailTable" class="text-nowrap"></table>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>