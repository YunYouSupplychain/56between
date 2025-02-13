<%@ taglib prefix="for" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>销售单管理</title>
	<meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmSaleHeaderForm.js" %>
</head>
<body>
<div style="width: 100%; height: 280px;">
    <div id="toolbar" style="width: 100%; padding-left: 10px;">
        <shiro:hasPermission name="inbound:banQinWmSaleHeader:save">
            <a class="btn btn-primary btn-sm" id="header_save" onclick="save()">保存</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmSaleHeader:audit">
            <a class="btn btn-primary btn-sm" id="header_audit" onclick="audit()">审核</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmSaleHeader:cancelAudit">
            <a class="btn btn-primary btn-sm" id="header_cancelAudit" onclick="cancelAudit()">取消审核</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmSaleHeader:createSo">
            <a class="btn btn-primary btn-sm" id="header_createAsn" onclick="createSo()">生成So</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmSaleHeader:closeOrder">
            <a class="btn btn-primary btn-sm" id="header_closeOrder" onclick="closeOrder()">关闭订单</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmSaleHeader:cancelOrder">
            <a class="btn btn-primary btn-sm" id="header_cancelOrder" onclick="cancelOrder()">取消订单</a>
        </shiro:hasPermission>
    </div>
    <form:form id="inputForm" modelAttribute="banQinWmSaleEntity" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:240px">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
            <li class=""><a data-toggle="tab" href="#carrierInfo" aria-expanded="true">承运商</a></li>
            <li class=""><a data-toggle="tab" href="#consigneeInfo" aria-expanded="true">承运商</a></li>
            <li class=""><a data-toggle="tab" href="#settlementInfo" aria-expanded="true">承运商</a></li>
            <li class=""><a data-toggle="tab" href="#reservedInfo" aria-expanded="true">预留信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">销售单号：</label></td>
                        <td class="" width="12%">
                            <form:input path="saleNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>销售单类型：</label></td>
                        <td class="" width="12%">
                            <form:select path="saleType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_SALE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">状态：</label></td>
                        <td class="" width="12%">
                            <form:select path="status" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_SALE_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">审核状态：</label></td>
                        <td class="" width="12%">
                            <form:select path="auditStatus" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">优先级别：</label></td>
                        <td class="" width="12%">
                            <form:input path="priority" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>货主编码：</label></td>
                        <td class="" width="12%">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="ownerCode" displayFieldName="ownerCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmSaleEntity.ownerCode}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="ownerType"
                                           concatId="ownerName" concatName="ebcuNameCn">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">货主名称：</label></td>
                        <td class="" width="12%">
                            <form:input path="ownerName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">订单时间：</label></td>
                        <td class="" width="12%">
                            <div class='input-group form_datetime' id='orderTime'>
                                <input name="orderTime" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmSaleEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="" width="8%"><label class="pull-right">预计到货时间从：</label></td>
                        <td class="" width="12%">
                            <div class='input-group form_datetime' id='fmEta'>
                                <input type='text' name="fmEta" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmSaleEntity.fmEta}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="" width="8%"><label class="pull-right">预计到货时间到：</label></td>
                        <td class="" width="12%">
                            <div class='input-group form_datetime' id='toEta'>
                                <input type='text' name="toEta" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmSaleEntity.toEta}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">物流单号：</label></td>
                        <td class="" width="12%">
                            <form:input path="logisticNo" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">审核人：</label></td>
                        <td class="" width="12%">
                            <form:input path="auditOp" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">审核时间：</label></td>
                        <td class="" width="12%">
                            <div class='input-group form_datetime' id='auditTime'>
                                <input type='text' name="auditTime" readonly="true" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmSaleEntity.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="" width="8%"><label class="pull-right">备注：</label></td>
                        <td class="" width="12%" colspan="3">
                            <for:textarea path="remarks" htmlEscape="false" class="form-control" rows="1"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="carrierInfo" class="tab-pane fade">
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">承运商编码：</label></td>
                        <td class="" width="12%">
                            <input id="carrierType" value="CARRIER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择承运商" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="carrierCode" displayFieldName="carrierCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmSoEntity.carrierCode}"
                                           selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                           fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="carrierType"
                                           concatId="carrierName" concatName="ebcuNameCn">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">承运商名称：</label></td>
                        <td class="" width="12%">
                            <form:input path="carrierName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">运输类型：</label></td>
                        <td class="" width="12%">
                            <form:input path="transType" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">路线：</label></td>
                        <td class="" width="12%">
                            <form:input path="line" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">站点：</label></td>
                        <td class="" width="12%">
                            <form:input path="stop" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">车牌号：</label></td>
                        <td class="" width="12%">
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">驾驶员：</label></td>
                        <td class="" width="12%">
                            <form:input path="driver" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">装车状态：</label></td>
                        <td class="" width="12%">
                            <form:select path="ldStatus" class="form-control m-b" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="consigneeInfo" class="tab-pane fade">
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">收货人编码：</label></td>
                        <td class="" width="12%">
                            <input id="consigneeType" value="CONSIGNEE" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择收货人" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="consigneeCode" displayFieldName="consigneeCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmSoEntity.consigneeCode}"
                                           selectButtonId="consigneeSelectId" deleteButtonId="consigneeDeleteId"
                                           fieldLabels="收货人编码|收货人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           searchLabels="收货人编码|收货人名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="consigneeType"
                                           concatId="consigneeName,consigneeTel,consigneeAddr,consigneeFax" concatName="ebcuNameCn,ebcuTel,ebcuAddress,ebcuFax">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">收货人名称：</label></td>
                        <td class="" width="12%">
                            <form:input path="consigneeName" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">收货人电话：</label></td>
                        <td class="" width="12%">
                            <form:input path="consigneeTel" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">收货人地址：</label></td>
                        <td class="" width="12%">
                            <form:input path="consigneeAddr" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">收货人传真：</label></td>
                        <td class="" width="12%">
                            <form:input path="consigneeFax" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">联系人名称：</label></td>
                        <td class="" width="12%">
                            <form:input path="contactName" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">联系人电话：</label></td>
                        <td class="" width="12%">
                            <form:input path="contactTel" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">联系人地址：</label></td>
                        <td class="" width="12%">
                            <form:input path="contactAddr" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">联系人传真：</label></td>
                        <td class="" width="12%">
                            <form:input path="contactFax" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">联系人Email：</label></td>
                        <td class="" width="12%">
                            <form:input path="contactEmail" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">快递单号：</label></td>
                        <td class="" width="12%">
                            <form:input path="trackingNo" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="settlementInfo" class="tab-pane fade">
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">结算人编码：</label></td>
                        <td class="" width="12%">
                            <input id="settleType" value="SETTLE" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择结算人" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="settleCode" displayFieldName="settleCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmSoEntity.settleCode}"
                                           selectButtonId="settleSelectId" deleteButtonId="settleDeleteId"
                                           fieldLabels="结算人编码|结算人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           searchLabels="结算人编码|结算人名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="settleType"
                                           concatId="settleName,settleTel,settleFax,settleIndustryType,settleAddress" concatName="ebcuNameCn,ebcuTel,ebcuFax,ebcuIndustryType,ebcuAddress">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right">结算人名称：</label></td>
                        <td class="" width="12%">
                            <form:input path="settleName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">电话：</label></td>
                        <td class="" width="12%">
                            <form:input path="settleTel" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">传真：</label></td>
                        <td class="" width="12%">
                            <form:input path="settleFax" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">行业类型：</label></td>
                        <td class="" width="12%">
                            <form:select path="settleIndustryType" class="form-control m-b" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_INDUSTRY_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">地址：</label></td>
                        <td class="" width="12%" colspan="3">
                            <form:input path="settleAddress" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="reservedInfo" class="tab-pane fade"`>
                <table class="table table-bordered">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">自定义1：</label></td>
                        <td class="" width="12%">
                            <form:input path="def1" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义2：</label></td>
                        <td class="" width="12%">
                            <form:input path="def2" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义3：</label></td>
                        <td class="" width="12%">
                            <form:input path="def3" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义4：</label></td>
                        <td class="" width="12%">
                            <form:input path="def4" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义5：</label></td>
                        <td class="" width="12%">
                            <form:input path="def5" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">自定义6：</label></td>
                        <td class="" width="12%">
                            <form:input path="def6" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义7：</label></td>
                        <td class="" width="12%">
                            <form:input path="def7" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义8：</label></td>
                        <td class="" width="12%">
                            <form:input path="def8" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义9：</label></td>
                        <td class="" width="12%">
                            <form:input path="def9" htmlEscape="false" class="form-control"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义10：</label></td>
                        <td class="" width="12%">
                            <form:input path="def10" htmlEscape="false" class="form-control"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    </form:form>
</div>
<div style="width: 100%; height: 550px; padding-left: 10px;">
    <div id="skuDetailToolbar" style="width: 100%; padding: 10px 0px;">
        <shiro:hasPermission name="inbound:banQinWmSaleDetail:add">
            <a class="btn btn-primary btn-sm" id="skuDetail_add" onclick="addSkuDetail()">新增</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmSaleDetail:save">
            <a class="btn btn-primary btn-sm" id="skuDetail_save" onclick="saveSkuDetail()">保存</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmSaleDetail:remove">
            <a class="btn btn-danger btn-sm" id="skuDetail_remove" onclick="removeSkuDetail()">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmSaleDetail:duplicate">
            <a class="btn btn-primary btn-sm" id="skuDetail_duplicate" onclick="duplicatSkuDetail()">复制</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmSaleDetail:cancel">
            <a class="btn btn-primary btn-sm" id="skuDetail_cancel" onclick="cancelSkuDetail()">取消订单行</a>
        </shiro:hasPermission>
    </div>
    <div id="skuDetail_tab-left" class="div-left" style="overflow: scroll; height: 500px;">
        <table id="wmSaleDetailTable" class="table table-bordered table-condensed text-nowrap"></table>
    </div>
    <div id="skuDetail_tab-right" class="div-right" style="overflow: scroll; height: 500px; border: 1px solid #dddddd;">
        <form:form id="skuDetailForm" method="post" class="form-horizontal">
            <input type="hidden" id="skuDetail_id" name="id"/>
            <input type="hidden" id="skuDetail_orgId" name="orgId"/>
            <input type="hidden" id="skuDetail_saleNo" name="saleNo"/>
            <input type="hidden" id="skuDetail_ownerCode" name="ownerCode"/>
            <input type="hidden" id="skuDetail_headId" name="headId"/>
            <input type="hidden" id="skuDetail_logisticNo" name="logisticNo"/>
            <input type="hidden" id="skuDetail_uomQty" name="uomQty"/>
            <div class="tabs-container" style="height: 100%; width: 100%;">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#skuDetail_baseInfo" aria-expanded="true">基本信息</a></li>
                    <li class=""><a data-toggle="tab" href="#skuDetail_lotInfo" aria-expanded="true">批次属性</a></li>
                    <li class=""><a data-toggle="tab" href="#skuDetail_reservedInfo" aria-expanded="false">预留信息</a></li>
                </ul>
                <div class="tab-content">
                    <div id="skuDetail_baseInfo" class="tab-pane fade in active">
                        <table class="bq-table">
                            <tbody>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left">行号：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">状态：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left"><font color="red">*</font>商品编码：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">商品名称：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">包装规格：</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <input id="skuDetail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <select id="skuDetail_status" name="status" class="form-control m-b">
                                        <c:forEach items="${fns:getDictList('SYS_WM_SALE_STATUS')}" var="dict">
                                            <option value="${dict.value}">${dict.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td width="20%">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                   displayFieldId="skuDetail_skuCode" displayFieldName="skuCode" displayFieldKeyName="skuCode" displayFieldValue=""
                                                   selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                   fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                   queryParams="ownerCode" queryParamValues="ownerCode"
                                                   concatId="skuDetail_skuName,skuDetail_packCode,skuDetail_packDesc,skuDetail_price,skuDetail_uom,skuDetail_uomDesc,skuDetail_uomQty"
                                                   concatName="skuName,packCode,cdpaFormat,price,rcvUom,rcvUomName,uomQty"
                                                   afterSelect="afterSkuSelect">
                                    </sys:popSelect>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_skuName" name="skuName" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                                   fieldId="skuDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                                   displayFieldId="skuDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                   selectButtonId="skuDetail_packSelectId" deleteButtonId="skuDetail_packDeleteId"
                                                   fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                                   searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left"><font color="red">*</font>包装单位：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left"><font color="red">*</font>销售数：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">销售数EA：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">订货数：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">订货数EA：</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control"
                                                   fieldId="skuDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                                   displayFieldId="skuDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                   selectButtonId="skuDetail_uomSelectId" deleteButtonId="skuDetail_uomDeleteId"
                                                   fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                   searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                   queryParams="packCode" queryParamValues="skuDetail_packCode" afterSelect="afterSelectUom">
                                    </sys:popSelect>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtySaleUom" name="qtySaleUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="qtySaleChange()">
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtySaleEa" name="qtySaleEa" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtySoUom" name="qtySoUom" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtySoEa" name="qtySoEa" htmlEscape="false" class="form-control" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left">分配数：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">分配数EA：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">拣货数：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">拣货数EA：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">发货数：</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <input id="skuDetail_qtyAllocUom" name="qtyAllocUom" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtyAllocEa" name="qtyAllocEa" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtyPkUom" name="qtyPkUom" htmlEscape="false" class="form-control">
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtyPkEa" name="qtyPkEa" htmlEscape="false" class="form-control">
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtyShipUom" name="qtyShipUom" htmlEscape="false" class="form-control">
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left">发货数EA：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">单价：</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <input id="skuDetail_qtyShipEa" name="qtyShipEa" htmlEscape="false" class="form-control">
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_price" name="price" htmlEscape="false" class="form-control">
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div id="skuDetail_lotInfo" class="tab-pane fade">
                        <table id="skuDetailLotAttTab" class="bq-table"></table>
                    </div>
                    <div id="skuDetail_reservedInfo" class="tab-pane fade">
                        <table class="bq-table">
                            <tbody>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left">自定义1：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义2：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义3：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义4：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义5：</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <input id="skuDetail_def1" name="def1" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_def2" name="def2" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_def3" name="def3" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_def4" name="def4" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_def5" name="def5" htmlEscape="false" class="form-control" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left">自定义6：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义7：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义8：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义9：</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义10：</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <input id="skuDetail_def6" name="def6" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_def7" name="def7" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_def8" name="def8" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_def9" name="def9" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_def10" name="def10" htmlEscape="false" class="form-control" readonly>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>