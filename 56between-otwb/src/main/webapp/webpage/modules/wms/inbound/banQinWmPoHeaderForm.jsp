<%@ taglib prefix="for" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>采购单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmPoHeaderForm.js" %>
</head>
<body>
<div style="width: 100%; height: 280px;">
    <div id="toolbar" style="width: 100%; padding-left: 10px;">
        <shiro:hasPermission name="inbound:banQinWmPoHeader:edit">
            <a class="btn btn-primary btn-sm" id="header_save" onclick="save()">保存</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmPoHeader:audit">
            <a class="btn btn-primary btn-sm" id="header_audit" onclick="audit()">审核</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmPoHeader:cancelAudit">
            <a class="btn btn-primary btn-sm" id="header_cancelAudit" onclick="cancelAudit()">取消审核</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmPoHeader:createAsn">
            <a class="btn btn-primary btn-sm" id="header_createAsn" onclick="createAsn()">生成ASN</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmPoHeader:closeOrder">
            <a class="btn btn-primary btn-sm" id="header_closeOrder" onclick="closeOrder()">关闭订单</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmPoHeader:cancelOrder">
            <a class="btn btn-primary btn-sm" id="header_cancelOrder" onclick="cancelOrder()">取消订单</a>
        </shiro:hasPermission>
    </div>
    <form:form id="inputForm" modelAttribute="banQinWmPoEntity" method="post" class="form">
        <form:hidden path="id"/>
        <form:hidden path="recVer"/>
        <form:hidden path="orgId"/>
        <sys:message content="${message}"/>
        <div class="tabs-container" style="height:240px">
            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
                <li class=""><a data-toggle="tab" href="#supplierInfo" aria-expanded="true">供应商</a></li>
                <li class=""><a data-toggle="tab" href="#reservedInfo" aria-expanded="true">预留信息</a></li>
            </ul>
            <div class="tab-content">
                <div id="baseInfo" class="tab-pane fade in active">
                    <table class="table well">
                        <tbody>
                        <tr>
                            <td class="width-8"><label class="pull-right">采购单号</label></td>
                            <td class="width-12">
                                <form:input path="poNo" htmlEscape="false" class="form-control" readonly="true"/>
                            </td>
                            <td class="width-8"><label class="pull-right"><font color="red">*</font>采购单类型</label></td>
                            <td class="width-12">
                                <form:select path="poType" class="form-control required">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('SYS_WM_PO_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </td>
                            <td class="width-8"><label class="pull-right">状态</label></td>
                            <td class="width-12">
                                <form:select path="status" class="form-control required" disabled="true">
                                    <form:options items="${fns:getDictList('SYS_WM_PO_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </td>
                            <td class="width-8"><label class="pull-right">审核状态</label></td>
                            <td class="width-12">
                                <form:select path="auditStatus" class="form-control required" disabled="true">
                                    <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </td>
                            <td class="width-8"><label class="pull-right">物流单号</label></td>
                            <td class="width-12">
                                <form:input path="logisticNo" htmlEscape="false" class="form-control"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-8"><label class="pull-right"><font color="red">*</font>货主编码</label></td>
                            <td class="width-12">
                                <input id="ownerType" value="OWNER" type="hidden">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="ownerCode" displayFieldName="ownerCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmPoEntity.ownerCode}"
                                               selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerType"
                                               concatId="ownerName" concatName="ebcuNameCn">
                                </sys:popSelect>
                            </td>
                            <td class="width-8"><label class="pull-right">货主名称</label></td>
                            <td class="width-12">
                                <form:input path="ownerName" htmlEscape="false" class="form-control" readonly="true"/>
                            </td>
                            <td class="width-8"><label class="pull-right">订单时间</label></td>
                            <td class="width-12">
                                <div class='input-group form_datetime' id='orderTime'>
                                    <input name="orderTime" class="form-control"
                                           value="<fmt:formatDate value="${banQinWmPoEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-8"><label class="pull-right">预计到货时间从</label></td>
                            <td class="width-12">
                                <div class='input-group form_datetime' id='fmEta'>
                                    <input type='text' name="fmEta" class="form-control"
                                           value="<fmt:formatDate value="${banQinWmPoEntity.fmEta}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-8"><label class="pull-right">预计到货时间到</label></td>
                            <td class="width-12">
                                <div class='input-group form_datetime' id='toEta'>
                                    <input type='text' name="toEta" class="form-control"
                                           value="<fmt:formatDate value="${banQinWmPoEntity.toEta}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-8"><label class="pull-right">审核人</label></td>
                            <td class="width-12">
                                <form:input path="auditOp" htmlEscape="false" class="form-control" readonly="true"/>
                            </td>
                            <td class="width-8"><label class="pull-right">审核时间</label></td>
                            <td class="width-12">
                                <div class='input-group form_datetime' id='auditTime'>
                                    <input type='text' name="auditTime" readonly="true" class="form-control"
                                           value="<fmt:formatDate value="${banQinWmPoEntity.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-8"><label class="pull-right">备注</label></td>
                            <td class="width-12" colspan="3">
                                <for:textarea path="remarks" htmlEscape="false" class="form-control" rows="1"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div id="supplierInfo" class="tab-pane fade">
                    <table class="table well">
                        <tbody>
                        <tr>
                            <td class="width-8"><label class="pull-right">供应商编码</label></td>
                            <td class="width-12">
                                <input id="supplierType" value="SUPPLIER" type="hidden">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择供应商" cssClass="form-control"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="supplierCode" displayFieldName="supplierCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmPoEntity.supplierCode}"
                                               selectButtonId="supplierSelectId" deleteButtonId="supplierDeleteId"
                                               fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="supplierType"
                                               concatId="supplierName,supplierTel,supplierFax,supplierIndustryType,supplierAddress"
                                               concatName="ebcuNameCn,ebcuTel,ebcuFax,ebcuIndustryType,ebcuAddress">
                                </sys:popSelect>
                            </td>
                            <td class="width-8"><label class="pull-right">供应商名称</label></td>
                            <td class="width-12">
                                <form:input path="supplierName" htmlEscape="false" class="form-control" readonly="true"/>
                            </td>
                            <td class="width-8"><label class="pull-right">电话</label></td>
                            <td class="width-12">
                                <form:input path="supplierTel" htmlEscape="false" class="form-control" readonly="true"/>
                            </td>
                            <td class="width-8"><label class="pull-right">传真</label></td>
                            <td class="width-12">
                                <form:input path="supplierFax" htmlEscape="false" class="form-control" readonly="true"/>
                            </td>
                            <td class="width-8"><label class="pull-right">行业类型</label></td>
                            <td class="width-12">
                                <form:select path="supplierIndustryType" class="form-control m-b" disabled="true">
                                    <form:option value="" label=""/>
                                    <form:options items="${fns:getDictList('SYS_INDUSTRY_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-8"><label class="pull-right">地址</label></td>
                            <td class="width-12" colspan="3">
                                <form:input path="supplierAddress" htmlEscape="false" class="form-control" readonly="true"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div id="reservedInfo" class="tab-pane fade" `>
                    <table class="table well">
                        <tbody>
                        <tr>
                            <td class="width-8"><label class="pull-right">自定义1</label></td>
                            <td class="width-12">
                                <form:input path="def1" htmlEscape="false" class="form-control"/>
                            </td>
                            <td class="width-8"><label class="pull-right">自定义2</label></td>
                            <td class="width-12">
                                <form:input path="def2" htmlEscape="false" class="form-control"/>
                            </td>
                            <td class="width-8"><label class="pull-right">自定义3</label></td>
                            <td class="width-12">
                                <form:input path="def3" htmlEscape="false" class="form-control"/>
                            </td>
                            <td class="width-8"><label class="pull-right">自定义4</label></td>
                            <td class="width-12">
                                <form:input path="def4" htmlEscape="false" class="form-control"/>
                            </td>
                            <td class="width-8"><label class="pull-right">自定义5</label></td>
                            <td class="width-12">
                                <form:input path="def5" htmlEscape="false" class="form-control"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-8"><label class="pull-right">自定义6</label></td>
                            <td class="width-12">
                                <form:input path="def6" htmlEscape="false" class="form-control"/>
                            </td>
                            <td class="width-8"><label class="pull-right">自定义7</label></td>
                            <td class="width-12">
                                <form:input path="def7" htmlEscape="false" class="form-control"/>
                            </td>
                            <td class="width-8"><label class="pull-right">自定义8</label></td>
                            <td class="width-12">
                                <form:input path="def8" htmlEscape="false" class="form-control"/>
                            </td>
                            <td class="width-8"><label class="pull-right">自定义9</label></td>
                            <td class="width-12">
                                <form:input path="def9" htmlEscape="false" class="form-control"/>
                            </td>
                            <td class="width-8"><label class="pull-right">自定义10</label></td>
                            <td class="width-12">
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
        <shiro:hasPermission name="inbound:banQinWmPoDetail:add">
            <a class="btn btn-primary btn-sm" id="skuDetail_add" onclick="addSkuDetail()">新增</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmPoDetail:save">
            <a class="btn btn-primary btn-sm" id="skuDetail_save" onclick="saveSkuDetail()">保存</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmPoDetail:remove">
            <a class="btn btn-danger btn-sm" id="skuDetail_remove" onclick="removeSkuDetail()">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmPoDetail:duplicate">
            <a class="btn btn-primary btn-sm" id="skuDetail_duplicate" onclick="duplicatSkuDetail()">复制</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inbound:banQinWmPoDetail:cancel">
            <a class="btn btn-primary btn-sm" id="skuDetail_cancel" onclick="cancelSkuDetail()">取消订单行</a>
        </shiro:hasPermission>
    </div>
    <div id="skuDetail_tab-left" class="div-left" style="overflow: scroll; height: 500px;">
        <table id="wmPoDetailTable" class="table well text-nowrap"></table>
    </div>
    <div id="skuDetail_tab-right" class="div-right" style="overflow: scroll; height: 500px; border: 1px solid #dddddd;">
        <form:form id="skuDetailForm" method="post" class="form-horizontal">
            <input type="hidden" id="skuDetail_id" name="id"/>
            <input type="hidden" id="skuDetail_orgId" name="orgId"/>
            <input type="hidden" id="skuDetail_poNo" name="poNo"/>
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
                                    <label class="pull-left">行号</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">状态</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left"><font color="red">*</font>商品编码</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">商品名称</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">包装规格</label>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <input id="skuDetail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <select id="skuDetail_status" name="status" class="form-control m-b">
                                        <c:forEach items="${fns:getDictList('SYS_WM_PO_STATUS')}" var="dict">
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
                                    <label class="pull-left"><font color="red">*</font>包装单位</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left"><font color="red">*</font>采购数</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">采购数EA</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">预收数</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">预收数EA</label>
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
                                    <input id="skuDetail_qtyPoUom" name="qtyPoUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="qtyPoChange()">
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtyPoEa" name="qtyPoEa" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtyAsnUom" name="qtyAsnUom" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtyAsnEa" name="qtyAsnEa" htmlEscape="false" class="form-control" readonly>
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <label class="pull-left">已收数</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">已收数EA</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">单价</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">物流单行号</label>
                                </td>
                                <td width="20%">
                                </td>
                            </tr>
                            <tr>
                                <td width="20%">
                                    <input id="skuDetail_qtyRcvUom" name="qtyRcvUom" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_qtyRcvEa" name="qtyRcvEa" htmlEscape="false" class="form-control" readonly>
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_price" name="price" htmlEscape="false" class="form-control">
                                </td>
                                <td width="20%">
                                    <input id="skuDetail_logisticLineNo" name="logisticLineNo" htmlEscape="false" class="form-control">
                                </td>
                                <td width="20%">
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
                                    <label class="pull-left">自定义1</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义2</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义3</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义4</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义5</label>
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
                                    <label class="pull-left">自定义6</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义7</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义8</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义9</label>
                                </td>
                                <td width="20%">
                                    <label class="pull-left">自定义10</label>
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