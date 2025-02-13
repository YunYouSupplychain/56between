<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>入库单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmAsnHeaderForm.js" %>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="inbound:banQinWmAsnHeader:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inbound:banQinWmAsnHeader:duplicate">
        <a class="btn btn-primary" id="header_duplicate" onclick="duplicate()">复制</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inbound:banQinWmAsnHeader:audit">
        <a class="btn btn-primary" id="header_audit" onclick="audit()">审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inbound:banQinWmAsnHeader:cancelAudit">
        <a class="btn btn-primary" id="header_cancelAudit" onclick="cancelAudit()">取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inbound:banQinWmAsnHeader:costAlloc">
        <a class="btn btn-primary" id="header_costAlloc" onclick="costAlloc()">采购成本分摊</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inbound:banQinWmAsnHeader:createQc">
        <a class="btn btn-primary" id="header_createQc" onclick="createQc()">生成质检单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inbound:banQinWmAsnHeader:closeOrder">
        <a class="btn btn-primary" id="header_closeOrder" onclick="closeOrder()">关闭订单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inbound:banQinWmAsnHeader:cancelOrder">
        <a class="btn btn-primary" id="header_cancelOrder" onclick="cancelOrder()">取消订单</a>
    </shiro:hasPermission>
    <div class="btn-group">
        <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown">更多<span class="caret"></span></a>
        <ul class="dropdown-menu">
            <shiro:hasPermission name="inbound:banQinWmAsnHeader:holdOrder">
                <li><a id="header_holdOrder" onclick="holdOrder()">冻结订单</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="inbound:banQinWmAsnHeader:cancelHold">
                <li><a id="header_cancelHold" onclick="cancelHold()">取消冻结</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="inbound:banQinWmAsnHeader:createVoucherNo">
                <li><a id="header_createVoucherNo" onclick="createVoucherNo()">生成凭证号</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="inbound:banQinWmAsnHeader:cancelVoucherNo">
                <li><a id="header_cancelVoucherNo" onclick="cancelVoucherNo()">取消凭证号</a></li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>
<form:form id="inputForm" modelAttribute="banQinWmAsnEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:250px">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
            <li class=""><a data-toggle="tab" href="#supplierInfo" aria-expanded="true">供应商</a></li>
            <li class=""><a data-toggle="tab" href="#carrierInfo" aria-expanded="true">承运商</a></li>
            <li class=""><a data-toggle="tab" href="#settlementInfo" aria-expanded="true">结算人</a></li>
            <li class=""><a data-toggle="tab" href="#reservedInfo" aria-expanded="true">预留信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">入库单号</label></td>
                        <td class="width-15">
                            <form:input path="asnNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>入库单类型</label></td>
                        <td class="width-15">
                            <form:select path="asnType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_ASN_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_ASN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">审核状态</label></td>
                        <td class="width-15">
                            <form:select path="auditStatus" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>货主编码</label></td>
                        <td class="width-15">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="ownerCode" displayFieldName="ownerCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmAsnEntity.ownerCode}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="ownerType"
                                           concatId="ownerName" concatName="ebcuNameCn">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">货主名称</label></td>
                        <td class="width-15">
                            <form:input path="ownerName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">订单时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='orderTime'>
                                <input id="orderTimeId" name="orderTime" class="form-control" value="<fmt:formatDate value="${banQinWmAsnEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">优先级别</label></td>
                        <td class="width-15">
                            <form:select path="priority" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_PRIORITY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">预计到货时间从</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='fmEta'>
                                <input type='text' name="fmEta" class="form-control" value="<fmt:formatDate value="${banQinWmAsnEntity.fmEta}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">预计到货时间到</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='toEta'>
                                <input type='text' name="toEta" class="form-control" value="<fmt:formatDate value="${banQinWmAsnEntity.toEta}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">冻结状态</label></td>
                        <td class="width-15">
                            <form:select path="holdStatus" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_ORDER_HOLD_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">质检状态</label></td>
                        <td class="width-15">
                            <form:select path="qcStatus" class="form-control" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_QC_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">审核人</label></td>
                        <td class="width-15">
                            <form:input path="auditOp" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">审核时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='auditTime'>
                                <input type='text' name="auditTime" readonly="true" class="form-control" value="<fmt:formatDate value="${banQinWmAsnEntity.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">物流单号</label></td>
                        <td class="width-15">
                            <form:input path="logisticNo" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">备注</label></td>
                        <td class="width-15" colspan="7">
                            <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="255"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="supplierInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">供应商编码</label></td>
                        <td class="width-15">
                            <input id="supplierType" value="SUPPLIER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择供应商" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="supplierCode" displayFieldName="supplierCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmAsnEntity.supplierCode}"
                                           selectButtonId="supplierSelectId" deleteButtonId="supplierDeleteId"
                                           fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="supplierType"
                                           concatId="supplierName,supplierTel,supplierFax,supplierIndustryType,supplierAddress"
                                           concatName="ebcuNameCn,ebcuTel,ebcuFax,ebcuIndustryType,ebcuAddress">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">供应商名称</label></td>
                        <td class="width-15">
                            <form:input path="supplierName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">电话</label></td>
                        <td class="width-15">
                            <form:input path="supplierTel" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">传真</label></td>
                        <td class="width-15">
                            <form:input path="supplierFax" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">行业类型</label></td>
                        <td class="width-15">
                            <form:select path="supplierIndustryType" class="form-control m-b" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_INDUSTRY_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">地址</label></td>
                        <td class="width-15" colspan="5">
                            <form:input path="supplierAddress" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="carrierInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">承运商</label></td>
                        <td class="width-15">
                            <input id="carrierType" value="CARRIER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择承运商" cssClass="form-control"
                                           fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="${banQinWmAsnEntity.carrierCode}" allowInput="true"
                                           displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinWmAsnEntity.carrierName}"
                                           selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                           fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="carrierType"
                                           concatId="carrierName,carrierTel,carrierFax,carrierIndustryType,carrierAddress"
                                           concatName="ebcuNameCn,ebcuTel,ebcuFax,ebcuIndustryType,ebcuAddress">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">司机</label></td>
                        <td class="width-15">
                            <form:input path="driver" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">车牌号</label></td>
                        <td class="width-15">
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">电话</label></td>
                        <td class="width-15">
                            <form:input path="tel" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">运费金额</label></td>
                        <td class="width-15">
                            <form:input path="freightAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="settlementInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">结算人编码</label></td>
                        <td class="width-15">
                            <input id="settleType" value="SETTLE" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择结算人" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="settleCode" displayFieldName="settleCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmAsnEntity.settleCode}"
                                           selectButtonId="settleSelectId" deleteButtonId="settleDeleteId"
                                           fieldLabels="结算人编码|结算人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="结算人编码|结算人名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="settleType"
                                           concatId="settleName,settleTel,settleFax,settleIndustryType,settleAddress"
                                           concatName="ebcuNameCn,ebcuTel,ebcuFax,ebcuIndustryType,ebcuAddress">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">结算人名称</label></td>
                        <td class="width-15">
                            <form:input path="settleName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">电话</label></td>
                        <td class="width-15">
                            <form:input path="settleTel" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">传真</label></td>
                        <td class="width-15">
                            <form:input path="settleFax" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">行业类型</label></td>
                        <td class="width-15">
                            <form:select path="settleIndustryType" class="form-control m-b" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_INDUSTRY_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">地址</label></td>
                        <td class="width-15" colspan="5">
                            <form:input path="settleAddress" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="reservedInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">商流订单号</label></td>
                        <td class="width-15">
                            <form:input path="def1" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">供应链订单号</label></td>
                        <td class="width-15">
                            <form:input path="def2" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">作业任务号</label></td>
                        <td class="width-15">
                            <form:input path="def3" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">客户订单号</label></td>
                        <td class="width-15">
                            <form:input path="def4" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">订单来源</label></td>
                        <td class="width-15">
                            <form:input path="def5" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义6</label></td>
                        <td class="width-15">
                            <form:input path="def6" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义7</label></td>
                        <td class="width-15">
                            <form:input path="def7" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义8</label></td>
                        <td class="width-15">
                            <form:input path="def8" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">自定义9</label></td>
                        <td class="width-15">
                            <form:input path="def9" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义10</label></td>
                        <td class="width-15">
                            <form:input path="def10" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<div class="tabs-container">
    <ul id="detailTab" class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#skuDetailInfo" aria-expanded="true" onclick="detailTabChange(0)">商品明细</a></li>
        <li class=""><a data-toggle="tab" href="#receiveDetailInfo" aria-expanded="true" onclick="detailTabChange(1)">收货明细</a></li>
        <li class=""><a data-toggle="tab" href="#paTaskInfo" aria-expanded="true" onclick="initWmAsnPaTaskTab()">上架任务</a></li>
        <li class=""><a data-toggle="tab" href="#serialInfo" aria-expanded="true" onclick="initWmAsnSerialTab()">序列号</a></li>
    </ul>
    <div class="tab-content">
        <div id="skuDetailInfo" class="tab-pane fade in active">
            <div id="skuDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="inbound:banQinWmAsnDetail:add">
                    <a class="btn btn-primary" id="skuDetail_add" onclick="addSkuDetail()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetail:save">
                    <a class="btn btn-primary" id="skuDetail_save" onclick="saveSkuDetail()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetail:remove">
                    <a class="btn btn-danger" id="skuDetail_remove" onclick="removeSkuDetail()">删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetail:duplicate">
                    <a class="btn btn-primary" id="skuDetail_duplicate" onclick="duplicateSkuDetail()">复制</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetail:palletize">
                    <a class="btn btn-primary" id="skuDetail_palletize" onclick="palletizeSkuDetail()">码盘</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetail:cancelPalletize">
                    <a class="btn btn-primary" id="skuDetail_cancelPalletize" onclick="cancelPalletizeSkuDetail()">取消码盘</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetail:createQc">
                    <a class="btn btn-primary" id="skuDetail_createQc" onclick="createQcSkuDetail()">生成质检单</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetail:cancel">
                    <a class="btn btn-primary" id="skuDetail_cancel" onclick="cancelSkuDetail()">取消订单行</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetail:apportionWeight">
                    <a class="btn btn-primary" id="skuDetail_apportionWeight" onclick="apportionWeightSkuDetail()">分摊重量</a>
                </shiro:hasPermission>
            </div>
            <div id="skuDetail_tab-left">
                <table id="wmAsnDetailTable" class="table well text-nowrap"></table>
            </div>
            <div id="skuDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="skuDetailForm" method="post" class="form">
                    <input type="hidden" id="skuDetail_id" name="id"/>
                    <input type="hidden" id="skuDetail_orgId" name="orgId"/>
                    <input type="hidden" id="skuDetail_asnNo" name="asnNo"/>
                    <input type="hidden" id="skuDetail_ownerCode" name="ownerCode"/>
                    <input type="hidden" id="skuDetail_headId" name="headId"/>
                    <input type="hidden" id="skuDetail_logisticNo" name="logisticNo"/>
                    <input type="hidden" id="skuDetail_uomQty" name="uomQty"/>
                    <input type="hidden" id="skuDetail_plQty" name="plQty"/>
                    <div class="tabs-container" style="margin: 0">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#skuDetail_baseInfo" aria-expanded="true">基本信息</a></li>
                            <li class=""><a data-toggle="tab" href="#skuDetail_lotInfo" aria-expanded="true">批次属性</a></li>
                            <li class=""><a data-toggle="tab" href="#skuDetail_qcInfo" aria-expanded="true">质检明细</a></li>
                            <li class=""><a data-toggle="tab" href="#skuDetail_reservedInfo" aria-expanded="false">预留信息</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="skuDetail_baseInfo" class="tab-pane fade in active">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">行号</label></td>
                                        <td class="width-25"><label class="pull-left">状态</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">商品编码</label></td>
                                        <td class="width-25"><label class="pull-left">商品名称</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <select id="skuDetail_status" name="status" class="form-control m-b">
                                                <c:forEach items="${fns:getDictList('SYS_WM_ASN_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control required"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_skuCode" displayFieldName="skuCode" displayFieldKeyName="skuCode" displayFieldValue=""
                                                           selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                           fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                           searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                           concatId="skuDetail_skuName,skuDetail_packCode,skuDetail_packDesc,skuDetail_price,skuDetail_uom,skuDetail_uomDesc,skuDetail_reserveCode,skuDetail_paRule,skuDetail_paRuleName,skuDetail_uomQty"
                                                           concatName="skuName,packCode,cdpaFormat,price,rcvUom,rcvUomName,reserveCode,paRule,paRuleName,uomQty"
                                                           queryParams="ownerCode" queryParamValues="ownerCode" afterSelect="afterSelectSku">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_skuName" name="skuName" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left asterisk">包装规格</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">包装单位</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">预收数</label></td>
                                        <td class="width-25"><label class="pull-left">预收数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control required"
                                                           fieldId="skuDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                                           displayFieldId="skuDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                           selectButtonId="skuDetail_packSelectId" deleteButtonId="skuDetail_packDeleteId"
                                                           fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                                           searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                                           fieldId="skuDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                           selectButtonId="skuDetail_uomSelectId" deleteButtonId="skuDetail_uomDeleteId"
                                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                                           queryParams="packCode" queryParamValues="skuDetail_packCode" afterSelect="afterSelectDetailPack">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyAsnUom" name="qtyAsnUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="qtyAsnChange()">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyAsnEa" name="qtyAsnEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">已收数</label></td>
                                        <td class="width-25"><label class="pull-left">已收数EA</label></td>
                                        <td class="width-25"><label class="pull-left">计划收货库位</label></td>
                                        <td class="width-25"><label class="pull-left">计划跟踪号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyRcvUom" name="qtyRcvUom" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyRcvEa" name="qtyRcvEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择计划收货库位" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_planToLoc" displayFieldName="planToLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="skuDetail_planToLocSelectId" deleteButtonId="skuDetail_planToLocDeleteId"
                                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_traceId" name="traceId" htmlEscape="false" class="form-control" maxlength="32">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">单价</label></td>
                                        <td class="width-25"><label class="pull-left">码盘标识</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">上架库位指定规则</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">上架规则</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_price" name="price" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_isPalletize" type="checkbox" name="isPalletize" htmlEscape="false" class="myCheckbox">
                                        </td>
                                        <td class="width-25">
                                            <select id="skuDetail_reserveCode" name="reserveCode" class="form-control m-b required">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_RESERVE_CODE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRulePaHeader/grid" title="选择上架规则" cssClass="form-control required"
                                                           fieldId="skuDetail_paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="skuDetail_paRuleSelectId" deleteButtonId="skuDetail_paRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">物流单行号</label></td>
                                        <td class="width-25"><label class="pull-left">采购单号</label></td>
                                        <td class="width-25"><label class="pull-left">采购单行号</label></td>
                                        <td class="width-25"><label class="pull-left">总重量</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_logisticLineNo" name="logisticLineNo" htmlEscape="false" class="form-control" maxlength="4">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_poNo" name="poNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_poLineNo" name="poLineNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_totalWeight" name="totalWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 8, 0)">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">入库时间</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_inboundTime" name="inboundTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="skuDetail_lotInfo" class="tab-pane fade">
                                <table id="skuDetailLotAttTab" class="bq-table"></table>
                            </div>
                            <div id="skuDetail_qcInfo" class="tab-pane fade">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">是否质检管理</label></td>
                                        <td class="width-25"><label class="pull-left">质检状态</label></td>
                                        <td class="width-25"><label class="pull-left" id="skuDetail_qcPhaseLabel">质检阶段</label></td>
                                        <td class="width-25"><label class="pull-left" id="skuDetail_qcRuleLabel">质检规则</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_isQc" name="isQc" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isQcChange(this.checked)">
                                        </td>
                                        <td class="width-25">
                                            <select id="skuDetail_qcStatus" name="qcStatus" class="form-control m-b" disabled>
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_QC_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <select id="skuDetail_qcPhase" name="qcPhase" class="form-control m-b">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_QC_PHASE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleQcHeader/grid" title="选择质检规则" cssClass="form-control"
                                                           fieldId="skuDetail_qcRule" fieldName="qcRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_qcRuleName" displayFieldName="qcRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="skuDetail_qcRuleSelectId" deleteButtonId="skuDetail_qcRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left" id="skuDetail_itemGroupLabel">质检项</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhQcItemHeader/grid" title="选择质检项" cssClass="form-control"
                                                           fieldId="skuDetail_itemGroupCode" fieldName="itemGroupCode" fieldKeyName="itemGroupCode" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_itemGroupName" displayFieldName="itemGroupName" displayFieldKeyName="itemGroupName" displayFieldValue=""
                                                           selectButtonId="skuDetail_itemGroupSelectId" deleteButtonId="skuDetail_itemGroupDeleteId"
                                                           fieldLabels="质检项编码|质检项名称" fieldKeys="itemGroupCode|itemGroupName"
                                                           searchLabels="质检项编码|质检项名称" searchKeys="itemGroupCode|itemGroupName" inputSearchKey="itemGroupCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                                <table id="wmAsnDetailQcTable" class="table well text-nowrap"></table>
                            </div>
                            <div id="skuDetail_reservedInfo" class="tab-pane fade">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义1</label></td>
                                        <td class="width-25"><label class="pull-left">自定义2</label></td>
                                        <td class="width-25"><label class="pull-left">自定义3</label></td>
                                        <td class="width-25"><label class="pull-left">自定义4</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_def1" name="def1" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_def2" name="def2" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_def3" name="def3" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_def4" name="def4" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义5</label></td>
                                        <td class="width-25"><label class="pull-left">自定义6</label></td>
                                        <td class="width-25"><label class="pull-left">自定义7</label></td>
                                        <td class="width-25"><label class="pull-left">自定义8</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_def5" name="def5" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_def6" name="def6" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_def7" name="def7" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_def8" name="def8" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义9</label></td>
                                        <td class="width-25"><label class="pull-left">自定义10</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_def9" name="def9" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_def10" name="def10" htmlEscape="false" class="form-control" maxlength="64">
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
        <div id="receiveDetailInfo" class="tab-pane fade">
            <div id="receiveDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="inbound:banQinWmAsnDetailReceive:arrangeLoc">
                    <a class="btn btn-primary" id="receiveDetail_arrangeLoc" onclick="arrangeLocReceiveDetail()">安排库位</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetailReceive:cancelArrangeLoc">
                    <a class="btn btn-primary" id="receiveDetail_cancelArrangeLoc" onclick="cancelArrangeLocReceiveDetail()">取消安排库位</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetailReceive:receiveConfirm">
                    <a class="btn btn-primary" id="receiveDetail_receiveConfirm" onclick="receiveConfirmReceiveDetail()">收货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetailReceive:cancelReceive">
                    <a class="btn btn-primary" id="receiveDetail_cancelReceive" onclick="cancelReceiveReceiveDetail()">取消收货</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetailReceive:createTaskPa">
                    <a class="btn btn-primary" id="receiveDetail_createTaskPa" onclick="createTaskPaReceiveDetail()">生成上架任务</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetailReceive:createVoucherNo">
                    <a class="btn btn-primary" id="receiveDetail_createVoucherNo" onclick="createVoucherNoReceiveDetail()">生成凭证号</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnDetailReceive:cancelVoucherNo">
                    <a class="btn btn-primary" id="receiveDetail_cancelVoucherNo" onclick="cancelVoucherNoReceiveDetail()">取消凭证号</a>
                </shiro:hasPermission>
                <a class="btn btn-primary" id="receiveDetail_printTraceLabel" onclick="printTraceLabelReceiveDetail()">打印托盘标签</a>
                <a class="btn btn-primary" id="receiveDetail_printTraceLabelQrCode" onclick="printTraceLabelQrCodeReceiveDetail()">打印托盘标签(二维码)</a>
            </div>
            <div id="receiveDetail_tab-left">
                <table id="wmAsnDetailReceiveTable" class="table well text-nowrap"></table>
            </div>
            <div id="receiveDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="receiveDetailForm" method="post" class="form">
                    <input type="hidden" id="receiveDetail_id" name="id"/>
                    <input type="hidden" id="receiveDetail_orgId" name="orgId"/>
                    <input type="hidden" id="receiveDetail_asnNo" name="asnNo"/>
                    <input type="hidden" id="receiveDetail_ownerCode" name="ownerCode"/>
                    <input type="hidden" id="receiveDetail_headId" name="headId"/>
                    <input type="hidden" id="receiveDetail_logisticNo" name="logisticNo"/>
                    <input type="hidden" id="receiveDetail_logisticLineNo" name="logisticLineNo"/>
                    <input type="hidden" id="receiveDetail_poNo" name="poNo"/>
                    <input type="hidden" id="receiveDetail_poLineNo" name="poLineNo"/>
                    <input type="hidden" id="receiveDetail_planId" name="planId"/>
                    <input type="hidden" id="receiveDetail_soNo" name="soNo"/>
                    <input type="hidden" id="receiveDetail_soLineNo" name="soLineNo"/>
                    <input type="hidden" id="receiveDetail_cdType" name="cdType"/>
                    <input type="hidden" id="receiveDetail_cdRcvId" name="cdRcvId"/>
                    <input type="hidden" id="receiveDetail_qcRcvId" name="qcRcvId"/>
                    <input type="hidden" id="receiveDetail_qtyRcvEa" name="qtyRcvEa"/>
                    <input type="hidden" id="receiveDetail_qtyRcvUom" name="qtyRcvUom"/>
                    <input type="hidden" id="receiveDetail_uomQty" name="uomQty"/>
                    <input type="hidden" id="receiveDetail_isQc" name="isQc"/>
                    <input type="hidden" id="receiveDetail_qcStatus" name="qcStatus"/>
                    <input type="hidden" id="receiveDetail_qcPhase" name="qcPhase"/>
                    <input type="hidden" id="receiveDetail_qcRule" name="qcRule"/>
                    <input type="hidden" id="receiveDetail_itemGroupCode" name="itemGroupCode"/>
                    <input type="hidden" id="receiveDetail_cubic" name="cubic"/>
                    <input type="hidden" id="receiveDetail_grossWeight" name="grossWeight"/>
                    <input type="hidden" id="receiveDetail_netWeight" name="netWeight"/>
                    <div class="tabs-container" style="margin: 0">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#receiveDetail_baseInfo" aria-expanded="true">基本信息</a></li>
                            <li class=""><a data-toggle="tab" href="#receiveDetail_lotInfo" aria-expanded="false">批次属性</a></li>
                            <li class=""><a data-toggle="tab" href="#receiveDetail_reservedInfo" aria-expanded="false">预留信息</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="receiveDetail_baseInfo" class="tab-pane fade in active">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">行号</label></td>
                                        <td class="width-25"><label class="pull-left">状态</label></td>
                                        <td class="width-25"><label class="pull-left">入库单行号</label></td>
                                        <td class="width-25"><label class="pull-left">商品</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="receiveDetail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <select id="receiveDetail_status" name="status" class="form-control m-b" disabled>
                                                <c:forEach items="${fns:getDictList('SYS_WM_ASN_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_asnLineNo" name="asnLineNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                           fieldId="receiveDetail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                                           displayFieldId="receiveDetail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                           selectButtonId="receiveDetail_skuSelectId" deleteButtonId="receiveDetail_skuDeleteId"
                                                           fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                           searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">包装规格</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">包装单位</label></td>
                                        <td class="width-25"><label class="pull-left">预收数</label></td>
                                        <td class="width-25"><label class="pull-left">预收数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control required"
                                                           fieldId="receiveDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                                           displayFieldId="receiveDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                           selectButtonId="receiveDetail_packSelectId" deleteButtonId="receiveDetail_packDeleteId"
                                                           fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                                           searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                                           fieldId="receiveDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                                           displayFieldId="receiveDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                           selectButtonId="receiveDetail_uomSelectId" deleteButtonId="receiveDetail_uomDeleteId"
                                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                                           queryParams="packCode" queryParamValues="receiveDetail_packCode" afterSelect="afterSelectReceivePack">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_qtyPlanUom" name="qtyPlanUom" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_qtyPlanEa" name="qtyPlanEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">可收数</label></td>
                                        <td class="width-25"><label class="pull-left">可收数EA</label></td>
                                        <td class="width-25"><label class="pull-left">单价</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">收货库位</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="receiveDetail_currentQtyRcvUom" name="currentQtyRcvUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="qtyRCvChange()">
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_currentQtyRcvEa" name="currentQtyRcvEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_price" name="price" htmlEscape="false" class="form-control">
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择收货库位" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="receiveDetail_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="receiveDetail_toLocSelectId" deleteButtonId="receiveDetail_toLocDeleteId"
                                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">计划上架库位</label></td>
                                        <td class="width-25"><label class="pull-left">上架任务Id</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">上架库位指定规则</label></td>
                                        <td class="width-25"><label class="pull-left">上架规则</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择计划上架库位" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                           displayFieldId="receiveDetail_planPaLoc" displayFieldName="planPaLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="receiveDetail_planPaLocSelectId" deleteButtonId="receiveDetail_planPaLocDeleteId"
                                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_paId" name="paId" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <select id="receiveDetail_reserveCode" name="reserveCode" class="form-control m-b required">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_RESERVE_CODE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRulePaHeader/grid" title="选择上架规则" cssClass="form-control required"
                                                           fieldId="receiveDetail_paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="receiveDetail_paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="receiveDetail_paRuleSelectId" deleteButtonId="receiveDetail_paRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">收货时间</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">收货跟踪号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <div class='input-group form_datetime' id='receiveDetail_rcvTimeF'>
                                                <input id="receiveDetail_rcvTime" name="rcvTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/>
                                                <span class="input-group-addon">
                                                    <span class="glyphicon glyphicon-calendar"></span>
                                                </span>
                                            </div>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_toId" name="toId" class="form-control m-b required"/>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="receiveDetail_lotInfo" class="tab-pane fade">
                                <table id="receiveDetailLotAttTab" class="bq-table"></table>
                            </div>
                            <div id="receiveDetail_reservedInfo" class="tab-pane fade">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义1</label></td>
                                        <td class="width-25"><label class="pull-left">自定义2</label></td>
                                        <td class="width-25"><label class="pull-left">自定义3</label></td>
                                        <td class="width-25"><label class="pull-left">自定义4</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="receiveDetail_def1" name="def1" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_def2" name="def2" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_def3" name="def3" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_def4" name="def4" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义5</label></td>
                                        <td class="width-25"><label class="pull-left">自定义6</label></td>
                                        <td class="width-25"><label class="pull-left">自定义7</label></td>
                                        <td class="width-25"><label class="pull-left">自定义8</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="receiveDetail_def5" name="def5" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_def6" name="def6" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_def7" name="def7" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_def8" name="def8" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义9</label></td>
                                        <td class="width-25"><label class="pull-left">自定义10</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="receiveDetail_def9" name="def9" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="receiveDetail_def10" name="def10" htmlEscape="false" class="form-control" readonly>
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
        <div id="paTaskInfo" class="tab-pane fade">
            <div id="paTaskToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="inbound:banQinWmTaskPa:del">
                    <a class="btn btn-primary" id="paTask_remove" onclick="removePaTask()">删除上架任务</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmTaskPa:putAway">
                    <a class="btn btn-primary" id="paTask_confirm" onclick="confirmPaTask()">上架确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmTaskPa:printPaTask">
                    <a class="btn btn-primary" id="paTask_print" onclick="printPaTask()">打印上架任务单</a>
                </shiro:hasPermission>
            </div>
            <div id="paTask_tab-left">
                <table id="wmAsnPaTaskTable" class="table well text-nowrap"></table>
            </div>
            <div id="paTask_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="paTaskForm" method="post" class="form">
                    <input type="hidden" id="paTask_id" name="id"/>
                    <input type="hidden" id="paTask_orgId" name="orgId"/>
                    <input type="hidden" id="paTask_qtyPaEa" name="qtyPaEa"/>
                    <input type="hidden" id="paTask_orderType" name="orderType"/>
                    <input type="hidden" id="paTask_uomQty" name="uomQty"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25"><label class="pull-left">上架任务Id</label></td>
                            <td class="width-25"><label class="pull-left">行号</label></td>
                            <td class="width-25"><label class="pull-left">状态</label></td>
                            <td class="width-25"><label class="pull-left">单据号</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="paTask_paId" name="paId" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="paTask_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <select id="paTask_status" name="status" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_TASK_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <input id="paTask_orderNo" name="orderNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">货主</label></td>
                            <td class="width-25"><label class="pull-left">商品编码</label></td>
                            <td class="width-25"><label class="pull-left">商品名称</label></td>
                            <td class="width-25"><label class="pull-left">包装规格</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="paTask_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                               displayFieldId="paTask_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="paTask_ownerSelectId" deleteButtonId="paTask_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                               displayFieldId="paTask_skuCode" displayFieldName="skuCode" displayFieldKeyName="skuCode" displayFieldValue=""
                                               selectButtonId="paTask_skuSelectId" deleteButtonId="paTask_skuDeleteId"
                                               fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="paTask_skuName" name="skuName" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                               fieldId="paTask_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                               displayFieldId="paTask_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                               selectButtonId="paTask_packSelectId" deleteButtonId="paTask_packDeleteId"
                                               fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                               searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" disabled="disabled">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">上架库位指定规则</label></td>
                            <td class="width-25"><label class="pull-left">上架规则</label></td>
                            <td class="width-25"><label class="pull-left">包装单位</label></td>
                            <td class="width-25"><label class="pull-left">批次号</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <select id="paTask_reserveCode" name="reserveCode" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_RESERVE_CODE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRulePaHeader/grid" title="选择上架规则" cssClass="form-control"
                                               fieldId="paTask_paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue=""
                                               displayFieldId="paTask_paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                               selectButtonId="paTask_paRuleSelectId" deleteButtonId="paTask_paRuleDeleteId"
                                               fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                               searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control"
                                               fieldId="paTask_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue=""
                                               displayFieldId="paTask_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                               selectButtonId="paTask_uomSelectId" deleteButtonId="paTask_uomDeleteId"
                                               fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                               searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="paTask_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">源库位</label></td>
                            <td class="width-25"><label class="pull-left">源跟踪号</label></td>
                            <td class="width-25"><label class="pull-left">推荐库位</label></td>
                            <td class="width-25"><label class="pull-left asterisk">上架库位</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择源库位" cssClass="form-control"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                               displayFieldId="paTask_fmLoc" displayFieldName="fmLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="paTask_fmLocSelectId" deleteButtonId="paTask_fmLocDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="paTask_fmId" name="fmId" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择推荐库位" cssClass="form-control"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                               displayFieldId="paTask_suggestLoc" displayFieldName="suggestLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="paTask_suggestLocSelectId" deleteButtonId="paTask_suggestLocDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择上架库位" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="paTask_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="paTask_toLocSelectId" deleteButtonId="paTask_toLocDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left asterisk">上架跟踪号</label></td>
                            <td class="width-25"><label class="pull-left">上架数</label></td>
                            <td class="width-25"><label class="pull-left">上架数EA</label></td>
                            <td class="width-25"><label class="pull-left">上架时间</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="paTask_toId" name="toId" htmlEscape="false" class="form-control required" maxlength="64">
                            </td>
                            <td class="width-25">
                                <input id="paTask_qtyPaUom" name="qtyPaUom" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="paTask_currentPaQtyEa" name="currentPaQtyEa" onkeyup="bq.numberValidator(this, 2, 0);" oninput="qtyTaskChange()" htmlEscape="false" class="form-control">
                            </td>
                            <td class="width-25">
                                <div class='input-group form_datetime' id='paTask_paTimeF'>
                                    <input id="paTask_paTime" name="paTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">上架人</label></td>
                            <td class="width-25"><label class="pull-left">备注</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="paTask_paOp" name="paOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="paTask_remarks" name="remarks" htmlEscape="false" class="form-control" maxlength="64">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
        <div id="serialInfo" class="tab-pane fade">
            <div id="serialToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="inbound:banQinWmAsnSerial:add">
                    <a class="btn btn-primary" id="serial_add" onclick="addSerial()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnSerial:edit">
                    <a class="btn btn-primary" id="serial_save" onclick="saveSerial()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnSerial:del">
                    <a class="btn btn-danger" id="serial_remove" onclick="removeSerial()">删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnSerial:import">
                    <a class="btn btn-primary" id="serial_import" onclick="importSerial()">导入</a>
                </shiro:hasPermission>
            </div>
            <div id="serial_tab-left">
                <table id="wmAsnSerialTable" class="table well text-nowrap"></table>
            </div>
            <div id="serial_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="serialForm" method="post" class="form">
                    <input type="hidden" id="serial_id" name="id"/>
                    <input type="hidden" id="serial_orgId" name="orgId"/>
                    <input type="hidden" id="serial_headId" name="headId"/>
                    <input type="hidden" id="serial_ownerCode" name="ownerCode"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25"><label class="pull-left">入库单号</label></td>
                            <td class="width-25"><label class="pull-left">收货明细行号</label></td>
                            <td class="width-25"><label class="pull-left">状态</label></td>
                            <td class="width-25"><label class="pull-left asterisk">商品</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="serial_asnNo" name="asnNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="serial_rcvLineNo" name="rcvLineNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <select id="serial_status" name="status" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_ASN_SERIAL_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/inbound/banQinWmAsnDetailReceive/grid" title="选择商品" cssClass="form-control required"
                                               fieldId="serial_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="serial_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="serial_skuSelectId" deleteButtonId="serial_skuDeleteId"
                                               fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left asterisk">序列号</label></td>
                            <td class="width-25"><label class="pull-left">批次号</label></td>
                            <td class="width-25"><label class="pull-left">扫描人</label></td>
                            <td class="width-25"><label class="pull-left">扫描时间</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="serial_serialNo" name="serialNo" htmlEscape="false" class="form-control required">
                            </td>
                            <td class="width-25">
                                <input id="serial_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="serial_scanOp" name="scanOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <div class='input-group form_datetime'>
                                    <input id="serial_scanTime" name="scanTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">数据来源</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="serial_dataSource" name="dataSource" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
    </div>
</div>
<!-- 分摊策略 -->
<div class="modal fade" id="apportionRuleModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">选择分摊策略</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td width="30%"><label class="pull-right">分摊策略</label></td>
                        <td width="70%">
                            <select id="apportion_rule" name="status" class="form-control m-b">
                                <option value=""></option>
                                <c:forEach items="${fns:getDictList('SYS_WM_APPORTION_RULE')}" var="dict">
                                    <option value="${dict.value}">${dict.label}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="apportionRuleConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
<!-- Excel上传弹出框 -->
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form class="form-horizontal" id="fileUploadForm" enctype="multipart/form-data">
        <div class="modal-dialog" style="width:500px">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">上传文件</h4>
                </div>
                <div class="modal-body">
                    <table class="table well">
                        <tbody>
                        <tr>
                            <td class="active" style="width: 70%;">
                                <input type="file" id="uploadFileName" name="file"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="downloadTemplate()">下载导入模板</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="uploadFile()">上传</button>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>