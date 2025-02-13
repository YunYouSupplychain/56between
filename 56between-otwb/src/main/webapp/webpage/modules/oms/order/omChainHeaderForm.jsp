<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>供应链订单管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="omChainHeaderForm.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="ownerType" value="OWNER"/>
    <input type="hidden" id="customerType" value="CONSIGNEE"/>
    <input type="hidden" id="supplierType" value="SUPPLIER"/>
    <input type="hidden" id="principalType" value="CUSTOMER"/>
    <input type="hidden" id="carrierType" value="CARRIER"/>
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="shipperType" value="SHIPPER"/>
    <input type="hidden" id="parentId"/>
</div>
<form:form id="inputForm" modelAttribute="omChainHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="dataSource"/>
    <form:hidden path="orderSource"/>
    <form:hidden path="sourceOrderNo"/>
    <form:hidden path="sourceOrderType"/>
    <form:hidden path="sourceOrderId"/>
    <form:hidden path="preSaleNo"/>
    <form:hidden path="sendOrderNo"/>
    <form:hidden path="handleStatus"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="min-height: 230px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#orderInfo" aria-expanded="true">基本信息</a></li>
            <li class=""><a data-toggle="tab" href="#consigneeInfo" aria-expanded="true">收货信息</a></li>
            <li class=""><a data-toggle="tab" href="#shipperInfo" aria-expanded="true">发货信息</a></li>
            <li class=""><a data-toggle="tab" href="#logisticsInfo" aria-expanded="true">物流信息</a></li>
            <li class=""><a data-toggle="tab" href="#settleInfo" aria-expanded="true">结算信息</a></li>
            <li class=""><a data-toggle="tab" href="#businessInfo" aria-expanded="true">商业信息</a></li>
            <li class=""><a data-toggle="tab" href="#otherInfo" aria-expanded="true">其它信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="orderInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">供应链订单号</label></td>
                        <td class="width-15">
                            <form:input path="chainNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">业务订单类型</label></td>
                        <td class="width-15">
                            <form:select path="businessOrderType" class="form-control required" onchange="businessOrderTypeChange()">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_BUSINESS_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">订单时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='orderDate'>
                                <input type='text' name="orderDate" class="form-control required"
                                       value="<fmt:formatDate value="${omChainHeaderEntity.orderDate}" pattern="yyyy-MM-dd hh:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">订单状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_CHAIN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">货主</label></td>
                        <td class="width-15">
                            <sys:grid title="选择货主" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control required"
                                      fieldId="owner" fieldName="owner"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omChainHeaderEntity.owner}"
                                      displayFieldId="ownerName" displayFieldName="ownerName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omChainHeaderEntity.ownerName}"
                                      fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="ownerType|orgId"
                                      afterSelect="afterSelectOwner"/>
                        </td>
                        <td class="width-10"><label class="pull-right">客户</label></td>
                        <td class="width-15">
                            <sys:grid title="选择客户" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                      fieldId="customer" fieldName="customer"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omChainHeaderEntity.customer}"
                                      displayFieldId="customerName" displayFieldName="customerName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omChainHeaderEntity.customerName}"
                                      fieldLabels="客户编码|客户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="客户编码|客户名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="customerType|orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">供应商</label></td>
                        <td class="width-15">
                            <sys:grid title="选择供应商" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                      fieldId="supplierCode" fieldName="supplierCode"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omChainHeaderEntity.supplierCode}"
                                      displayFieldId="supplierName" displayFieldName="supplierName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omChainHeaderEntity.supplierName}"
                                      fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="supplierType|orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">委托方</label></td>
                        <td class="width-15">
                            <sys:grid title="选择委托方" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                      fieldId="principal" fieldName="principal"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omChainHeaderEntity.principal}"
                                      displayFieldId="principalName" displayFieldName="principalName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omChainHeaderEntity.principalName}"
                                      fieldLabels="委托方编码|委托方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="委托方编码|委托方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="principalType|orgId"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">客户订单号</label></td>
                        <td class="width-15">
                            <form:input path="customerNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商流订单号</label></td>
                        <td class="width-15">
                            <form:input path="businessNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">是否校验库存充足</label></td>
                        <td class="width-15">
                            <form:select path="isAvailableStock" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">下发机构</label></td>
                        <td class="width-15">
                            <sys:grid title="选择仓库" url="${ctx}/sys/office/companyData" cssClass="form-control"
                                      fieldId="warehouse" fieldName="warehouse"
                                      fieldKeyName="id" fieldValue="${omChainHeaderEntity.warehouse}"
                                      displayFieldId="warehouseName" displayFieldName="warehouseName"
                                      displayFieldKeyName="name" displayFieldValue="${omChainHeaderEntity.warehouseName}"
                                      fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                      searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                      queryParams="id" queryParamValues="parentId"/>
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
            <div id="consigneeInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货方</label></td>
                        <td class="width-15">
                            <sys:grid title="选择收货方" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                      fieldId="consigneeCode" fieldName="consigneeCode"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omChainHeaderEntity.consigneeCode}"
                                      displayFieldId="consigneeName" displayFieldName="consigneeName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omChainHeaderEntity.consigneeName}"
                                      fieldLabels="收货方编码|收货方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="收货方编码|收货方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="customerType|orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预计到货时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='arrivalTime'>
                                <input type='text' name="arrivalTime" class="form-control"
                                       value="<fmt:formatDate value="${omChainHeaderEntity.arrivalTime}" pattern="yyyy-MM-dd HH:mm"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人</label></td>
                        <td class="width-15">
                            <form:input path="consignee" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="consigneeTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货人区域</label></td>
                        <td class="width-15">
                            <sys:treeselect title="选择收货人区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="consigneeArea" name="consigneeArea" value="${omChainHeaderEntity.consigneeArea}"
                                            labelName="area.name" labelValue="${omChainHeaderEntity.consigneeAreaName}"/>
                        </td>
                        <td class="width-10"><label class="pull-right">三级地址</label></td>
                        <td class="width-15">
                            <form:input path="consigneeAddressArea" htmlEscape="false" class="form-control" maxlength="128" placeholder="示例：上海:上海市:静安区"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人地址</label></td>
                        <td class="width-15" colspan="3">
                            <form:input path="consigneeAddress" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="shipperInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">发货方</label></td>
                        <td class="width-15">
                            <sys:grid title="选择发货方" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                      fieldId="shipperCode" fieldName="shipperCode"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omChainHeaderEntity.shipperCode}"
                                      displayFieldId="shipperName" displayFieldName="shipperName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omChainHeaderEntity.shipperName}"
                                      fieldLabels="发货方编码|发货方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="发货方编码|发货方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="shipperType|orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预计发货时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='deliveryDate'>
                                <input type='text' name="deliveryDate" class="form-control"
                                       value="<fmt:formatDate value="${omChainHeaderEntity.deliveryDate}" pattern="yyyy-MM-dd HH:mm"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">发货人</label></td>
                        <td class="width-15">
                            <form:input path="shipper" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="shipperTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">发货区域</label></td>
                        <td class="width-15">
                            <sys:treeselect title="选择发货区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="shipperArea" name="shipperArea" value="${omChainHeaderEntity.shipperArea}"
                                            labelName="area.name" labelValue="${omChainHeaderEntity.shipperAreaName}"/>
                        </td>
                        <td class="width-10"><label class="pull-right">三级地址</label></td>
                        <td class="width-15">
                            <form:input path="shipperAddressArea" htmlEscape="false" class="form-control" maxlength="128" placeholder="示例：上海:上海市:静安区"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发货人地址</label></td>
                        <td class="width-15" colspan="3">
                            <form:input path="shipperAddress" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="logisticsInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">承运商</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择承运商" cssClass="form-control"
                                           fieldId="carrier" fieldName="carrier"
                                           fieldKeyName="ebcuCustomerNo" fieldValue="${omChainHeaderEntity.carrier}"
                                           displayFieldId="carrierName" displayFieldName="carrierName"
                                           displayFieldKeyName="ebcuNameCn" displayFieldValue="${omChainHeaderEntity.carrierName}"
                                           selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                           fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="carrierType"
                                           allowInput="true" inputSearchKey="codeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">运输方式</label></td>
                        <td class="width-15">
                            <form:select path="transportMode" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TRANSPORT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">物流单号</label></td>
                        <td class="width-15">
                            <form:input path="logisticsNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">车牌号</label></td>
                        <td class="width-15">
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">司机</label></td>
                        <td class="width-15">
                            <form:input path="driver" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="contactTel" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="settleInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">结算对象</label></td>
                        <td class="width-15">
                            <sys:grid title="选择结算方" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                      fieldId="settlement" fieldName="settlement"
                                      fieldKeyName="ebcuCustomerNo" fieldValue="${omChainHeaderEntity.settlement}"
                                      displayFieldId="settlementName" displayFieldName="settlementName"
                                      displayFieldKeyName="ebcuNameCn" displayFieldValue="${omChainHeaderEntity.settlementName}"
                                      fieldLabels="结算方编码|结算方名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="结算方编码|结算方名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="settlementType|orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">结算方式</label></td>
                        <td class="width-15">
                            <form:select path="settleMode" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('BMS_SETTLEMENT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">币种</label></td>
                        <td class="width-15">
                            <form:select path="currency" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_CURRENCY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">汇率</label></td>
                        <td class="width-15">
                            <form:input path="exchangeRate" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 6, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">合同号</label></td>
                        <td class="width-15">
                            <form:input path="contractNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">付款时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='payDate'>
                                <input type='text' name="payDate" class="form-control"
                                       value="<fmt:formatDate value="${omChainHeaderEntity.payDate}" pattern="yyyy-MM-dd HH:mm"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">支付状态</label></td>
                        <td class="width-15">
                            <form:select path="payStatus" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_PAY_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">预付金额</label></td>
                        <td class="width-15">
                            <form:input path="prepaidAmount" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">运费金额</label></td>
                        <td class="width-15">
                            <form:input path="freightCharge" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">优惠券金额</label></td>
                        <td class="width-15">
                            <form:input path="couponAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr class="hide">
                        <td class="width-10"><label class="pull-right">合计金额</label></td>
                        <td class="width-15">
                            <form:hidden path="totalAmount"/>
                            <input id="totalAmountV" class="form-control" readonly/>
                        </td>
                        <td class="width-10"><label class="pull-right">合计税额</label></td>
                        <td class="width-15">
                            <form:hidden path="totalTax"/>
                            <input id="totalTaxV" class="form-control" readonly/>
                        </td>
                        <td class="width-10"><label class="pull-right">合计含税金额</label></td>
                        <td class="width-15">
                            <form:hidden path="totalTaxInAmount"/>
                            <input id="totalTaxInAmountV" class="form-control" readonly/>
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
            <div id="otherInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">渠道</label></td>
                        <td class="width-15">
                            <form:select path="channel" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_CHANNEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">店铺</label></td>
                        <td class="width-15">
                            <form:input path="shop" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">会员编号</label></td>
                        <td class="width-15">
                            <form:input path="vipNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">会员级别</label></td>
                        <td class="width-15">
                            <form:select path="vipStatus" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_VIP_LEVEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">业务员</label></td>
                        <td class="width-15">
                            <sys:grid title="选择业务员" url="${ctx}/oms/basic/omClerk/popData" cssClass="form-control"
                                      fieldId="clerkCode" fieldName="clerkCode"
                                      fieldKeyName="clerkCode" fieldValue="${omChainHeaderEntity.clerkCode}"
                                      displayFieldId="clerkName" displayFieldName="clerkName"
                                      displayFieldKeyName="clerkName" displayFieldValue="${omChainHeaderEntity.clerkName}"
                                      fieldLabels="业务员代码|业务员名称" fieldKeys="clerkCode|clerkName"
                                      searchLabels="业务员代码|业务员名称" searchKeys="clerkCode|clerkName"
                                      queryParams="orgId" queryParamValues="orgId"/>
                        </td>
                        <td class="width-10"><label class="pull-right">项目</label></td>
                        <td class="width-15">
                            <form:input path="project" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">制单人</label></td>
                        <td class="width-15">
                            <form:input path="preparedBy" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">销售人</label></td>
                        <td class="width-15">
                            <form:input path="saleBy" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">创建人</label></td>
                        <td class="width-15">
                            <form:input path="createBy.name" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">更新人</label></td>
                        <td class="width-15">
                            <form:input path="updateBy.name" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">审核人</label></td>
                        <td class="width-15">
                            <form:input path="auditBy" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">拦截状态</label></td>
                        <td class="width-15">
                            <form:select path="interceptStatus" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_INTERCEPT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">创建时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime'>
                                <input id='createDate' name="createDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omChainHeaderEntity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">审核时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime'>
                                <input id='auditDate' name="auditDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omChainHeaderEntity.auditDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">更新时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime'>
                                <input id='updateDate' name="updateDate" class="form-control" readonly
                                       value="<fmt:formatDate value="${omChainHeaderEntity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">拦截时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime'>
                                <input id='interceptTime' name="interceptTime" class="form-control" readonly
                                       value="<fmt:formatDate value="${omChainHeaderEntity.interceptTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="tabs-container" style="min-height: 300px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">供应链订单明细</a></li>
        </ul>
        <div class="tab-content" style="overflow-x: auto">
            <div id="tab-1" class="tab-pane fade in active">
                <a class="btn btn-white btn-sm" onclick="addDetail()" title="新增"><i class="fa fa-plus"></i> 新增</a>
                <table class="table well text-nowrap" style="min-width: 1500px;">
                    <thead>
                    <tr>
                        <th><label><input id="allCheckBox" type="checkbox" onclick="allCheckBoxClick();"/></label></th>
                        <th class="hide"></th>
                        <th>行号</th>
                        <th class="asterisk">商品编码</th>
                        <th>商品名称</th>
                        <th>规格</th>
                        <th>单位</th>
                        <th class="asterisk">数量</th>
                        <th class="hide">计划任务数量</th>
                        <th>辅助单位</th>
                        <th>辅助单位数量</th>
                        <th>已生成任务数量</th>
                        <th class="hide">税率</th>
                        <th class="hide">单价</th>
                        <th class="hide">含税单价</th>
                        <th class="hide">金额</th>
                        <th class="hide">税金</th>
                        <th class="hide">含税金额</th>
                        <th class="hide">折扣</th>
                        <th class="hide">成交金额</th>
                        <th class="hide">成交税金</th>
                        <th class="hide">成交价税合计</th>
                        <th>备注</th>
                        <th width="10">&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody id="omChainDetailList">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="omChainDetailTpl">//<!--
<tr id="omChainDetailList{{idx}}">
    <td class="hide">
        <input id="omChainDetailList{{idx}}_id" name="omChainDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="omChainDetailList{{idx}}_delFlag" name="omChainDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="omChainDetailList{{idx}}_chainNo" name="omChainDetailList[{{idx}}].chainNo" type="hidden" value="{{row.chainNo}}"/>
        <input id="omChainDetailList{{idx}}_itemPriceId" name="omChainDetailList[{{idx}}].itemPriceId" type="hidden" value="{{row.itemPriceId}}"/>
        <input id="omChainDetailList{{idx}}_ratio" name="omChainDetailList[{{idx}}].ratio" type="hidden" value="{{row.ratio}}"/>
        <input id="omChainDetailList{{idx}}_riceNum" name="omChainDetailList[{{idx}}].riceNum" type="hidden" value="{{row.riceNum}}"/>
        <input id="omChainDetailList{{idx}}_isAllowAdjustment" name="omChainDetailList[{{idx}}].isAllowAdjustment" type="hidden" value="{{row.isAllowAdjustment}}"/>
    </td>
    <td>
        <input id="omChainDetailList{{idx}}_checkbox" name="omChainDetailList[{{idx}}].checkbox" class="listCheckBox" type="checkbox" onclick="checkboxClick()" />
    </td>
    <td class="width-8">
        <input id="omChainDetailList{{idx}}_lineNo" name="omChainDetailList[{{idx}}].lineNo" type="text" value="{{row.lineNo}}" readonly class="form-control"/>
    </td>
    <td class="width-15">
        <sys:grid title="选择商品" url="${ctx}/oms/basic/omItem/orderSkuGrid" cssClass="form-control required"
                  fieldId="" fieldName="" fieldKeyName=""
                  displayFieldId="omChainDetailList{{idx}}_skuCode" displayFieldName="omChainDetailList[{{idx}}].skuCode"
                  displayFieldKeyName="skuCode" displayFieldValue="{{row.skuCode}}"
                  fieldLabels="商品编码|商品名称|货主编码|货主名称" fieldKeys="skuCode|skuName|ownerCode|ownerName"
                  searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                  queryParams="ownerCode|orgId" queryParamValues="owner|orgId"
                  afterSelect="skuSelectAfter({{idx}})"/>
    </td>
    <td class="width-15">
        <input id="omChainDetailList{{idx}}_skuName" name="omChainDetailList[{{idx}}].skuName" type="text" value="{{row.skuName}}" readonly class="form-control"/>
    </td>
    <td class="width-8">
        <input id="omChainDetailList{{idx}}_spec" name="omChainDetailList[{{idx}}].spec" type="text" value="{{row.spec}}" readonly class="form-control"/>
    </td>
    <td class="width-8">
        <select id="omChainDetailList{{idx}}_unit" name="omChainDetailList[{{idx}}].unit" data-value="{{row.unit}}" class="form-control" >
            <c:forEach items="${fns:getDictList('OMS_ITEM_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td class="width-8">
        <input id="omChainDetailList{{idx}}_qty" name="omChainDetailList[{{idx}}].qty" type="text" value="{{row.qty}}" class="form-control required" onchange="qtyChange({{idx}})" />
    </td>
    <td class="width-8 hide">
        <input id="omChainDetailList{{idx}}_planTaskQty" name="omChainDetailList[{{idx}}].planTaskQty" type="text" value="{{row.planTaskQty}}" class="form-control" readonly />
    </td>
    <td class="width-8">
        <select id="omChainDetailList{{idx}}_auxiliaryUnit" name="omChainDetailList[{{idx}}].auxiliaryUnit" data-value="{{row.auxiliaryUnit}}" class="form-control">
            <c:forEach items="${fns:getDictList('OMS_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td class="width-8">
        <input id="omChainDetailList{{idx}}_auxiliaryQty" name="omChainDetailList[{{idx}}].auxiliaryQty" type="text" value="{{row.auxiliaryQty}}" class="form-control"/>
    </td>
    <td class="width-8">
        <input id="omChainDetailList{{idx}}_taskQty" name="omChainDetailList[{{idx}}].taskQty" type="text" value="{{row.taskQty}}" class="form-control" readonly />
    </td>
    <td class="hidden">
        <select id="omChainDetailList{{idx}}_taxRate" name="omChainDetailList[{{idx}}].taxRate" data-value="{{row.taxRate}}" class="form-control" disabled onchange="priceChange({{idx}})">
            <c:forEach items="${fns:getDictList('OMS_TAX_RATE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td class="hidden">
        <input id="omChainDetailList{{idx}}_price" name="omChainDetailList[{{idx}}].price" type="text" value="{{row.price}}" readonly class="form-control" />
    </td>
    <td class="hidden">
        <input id="omChainDetailList{{idx}}_taxPrice" name="omChainDetailList[{{idx}}].taxPrice" type="text" value="{{row.taxPrice}}" class="form-control"  onkeyup="bq.numberValidator(this, 6, 0);priceChange({{idx}});"/>
    </td>
    <td class="hidden">
        <input id="omChainDetailList{{idx}}_amount" name="omChainDetailList[{{idx}}].amount" type="hidden" value="{{row.amount}}" readonly class="form-control amount"/>
        <input id="omChainDetailList{{idx}}_amountV" type="text" value="{{row.amount}}" readonly class="form-control"/>
    </td>
    <td class="hidden">
        <input id="omChainDetailList{{idx}}_taxMoney" name="omChainDetailList[{{idx}}].taxMoney" type="hidden" value="{{row.taxMoney}}" readonly class="form-control taxMoney"/>
        <input id="omChainDetailList{{idx}}_taxMoneyV" type="text" value="{{row.taxMoney}}" readonly class="form-control"/>
    </td>
    <td class="hidden">
        <input id="omChainDetailList{{idx}}_taxAmount" name="omChainDetailList[{{idx}}].taxAmount" type="hidden" value="{{row.taxAmount}}" readonly class="form-control taxAmount"/>
        <input id="omChainDetailList{{idx}}_taxAmountV" type="text" value="{{row.taxAmount}}" readonly class="form-control"/>
    </td>
    <td class="hidden">
        <input id="omChainDetailList{{idx}}_discount" name="omChainDetailList[{{idx}}].discount" type="text" value="{{row.discount}}" readonly class="form-control" />
    </td>
    <td class="hidden">
        <input id="omChainDetailList{{idx}}_turnover" name="omChainDetailList[{{idx}}].turnover" type="text" value="{{row.turnover}}" readonly class="form-control"/>
    </td>
    <td class="hidden">
        <input id="omChainDetailList{{idx}}_transactionTax" name="omChainDetailList[{{idx}}].transactionTax" type="text" value="{{row.transactionTax}}" readonly class="form-control"/>
    </td>
    <td class="hidden">
        <input id="omChainDetailList{{idx}}_sumTransactionPriceTax" name="omChainDetailList[{{idx}}].sumTransactionPriceTax" type="hidden" value="{{row.sumTransactionPriceTax}}" readonly class="form-control"/>
        <input id="omChainDetailList{{idx}}_sumTransactionPriceTaxV" type="text" value="{{row.sumTransactionPriceTax}}" readonly class="form-control"/>
    </td>
    <td class="width-15">
        <input id="omChainDetailList{{idx}}_remarks" name="omChainDetailList[{{idx}}].remarks" type="text" value="{{row.remarks}}" class="form-control"/>
    </td>
    <td class="text-center" width="10">
        {{#delBtn}}<span class="close" onclick="delRow(this, '#omChainDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
    </td>
</tr>//-->
</script>
</body>
</html>