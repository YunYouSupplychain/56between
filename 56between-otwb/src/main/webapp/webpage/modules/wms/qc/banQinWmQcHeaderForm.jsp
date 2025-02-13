<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>质检单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmQcHeaderForm.js" %>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="qc:banQinWmQcHeader:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="qc:banQinWmQcHeader:audit">
        <a class="btn btn-primary" id="header_audit" onclick="audit()">审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="qc:banQinWmQcHeader:cancelAudit">
        <a class="btn btn-primary" id="header_cancelAudit" onclick="cancelAudit()">取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="qc:banQinWmQcHeader:closeOrder">
        <a class="btn btn-primary" id="header_closeOrder" onclick="closeOrder()">关闭订单</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmQcEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:240px">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
            <li class=""><a data-toggle="tab" href="#reservedInfo" aria-expanded="true">预留信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">质检单号</label></td>
                        <td class="width-15">
                            <form:input path="qcNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">质检阶段</label></td>
                        <td class="width-15">
                            <form:select path="qcPhase" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_QC_PHASE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_QC_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">审核状态</label></td>
                        <td class="width-15">
                            <form:select path="auditStatus" class="form-control" disabled="true">
                                <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">货主编码</label></td>
                        <td class="width-15">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                           displayFieldId="ownerCode" displayFieldName="ownerCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmQcEntity.ownerCode}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="ownerType"
                                           concatId="ownerName" concatName="ebcuNameCn" disabled="disabled">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">货主名称</label></td>
                        <td class="width-15">
                            <form:input path="ownerName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">订单时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='orderTime'>
                                <input name="orderTime" class="form-control" value="<fmt:formatDate value="${banQinWmQcEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>" readonly/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">优先级别</label></td>
                        <td class="width-15">
                            <form:select path="priority" class="form-control" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_PRIORITY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">单据号</label></td>
                        <td class="width-15">
                            <form:input path="orderNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">单据类型</label></td>
                        <td class="width-15">
                            <form:select path="orderType" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
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
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">预计质检时间从</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='fmEta'>
                                <input type='text' name="fmEtq" class="form-control" value="<fmt:formatDate value="${banQinWmQcEntity.fmEtq}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">预计质检时间到</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='toEta'>
                                <input type='text' name="toEtq" class="form-control" value="<fmt:formatDate value="${banQinWmQcEntity.toEtq}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">备注</label></td>
                        <td class="width-15" colspan="5">
                            <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="512"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="reservedInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">自定义1</label></td>
                        <td class="width-15">
                            <form:input path="def1" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义2</label></td>
                        <td class="width-15">
                            <form:input path="def2" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义3</label></td>
                        <td class="width-15">
                            <form:input path="def3" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义4</label></td>
                        <td class="width-15">
                            <form:input path="def4" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">自定义5</label></td>
                        <td class="width-15">
                            <form:input path="def5" htmlEscape="false" class="form-control" maxlength="64"/>
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
        <li class=""><a data-toggle="tab" href="#skuDetailInfo" aria-expanded="true" onclick="detailTabChange(0)">商品明细</a></li>
        <li class=""><a data-toggle="tab" href="#qcDetailInfo" aria-expanded="true" onclick="detailTabChange(1)">质检明细</a></li>
        <li class=""><a data-toggle="tab" href="#paTaskInfo" aria-expanded="true" onclick="initPaTaskTab()">上架任务</a></li>
    </ul>
    <div class="tab-content">
        <div id="skuDetailInfo" class="tab-pane fade in active">
            <div id="skuDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="qc:banQinWmQcSku:remove">
                    <a class="btn btn-danger" id="skuDetail_remove" onclick="remove()">删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="qc:banQinWmQcSku:qcConfirm">
                    <a class="btn btn-primary" id="skuDetail_qcConfirm" onclick="qcConfirm()">质检确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="qc:banQinWmQcSku:cancelQcConfirm">
                    <a class="btn btn-primary" id="skuDetail_cancelQcConfirm" onclick="cancelQcConfirm()">取消质检确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="qc:banQinWmQcSku:createPaTask">
                    <a class="btn btn-primary" id="skuDetail_createPaTask" onclick="createPaTask()">生成上架任务</a>
                </shiro:hasPermission>
            </div>
            <div id="skuDetail_tab-left">
                <table id="wmQcSkuDetailTable" class="table well text-nowrap"></table>
            </div>
            <div id="skuDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="skuDetailForm" method="post" class="form">
                    <input type="hidden" id="skuDetail_id" name="id"/>
                    <input type="hidden" id="skuDetail_orgId" name="orgId"/>
                    <input type="hidden" id="skuDetail_qcNo" name="qcNo"/>
                    <input type="hidden" id="skuDetail_ownerCode" name="ownerCode"/>
                    <input type="hidden" id="skuDetail_headId" name="headId"/>
                    <div class="tabs-container" style="margin: 0">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#skuDetail_baseInfo" aria-expanded="true">基本信息</a></li>
                            <li class=""><a data-toggle="tab" href="#skuDetail_lotInfo" aria-expanded="true">批次属性</a></li>
                            <li class=""><a data-toggle="tab" href="#skuDetail_itemGroupInfo" aria-expanded="true" onclick="loadItemGroup()">质检项</a></li>
                            <li class=""><a data-toggle="tab" href="#skuDetail_reservedInfo" aria-expanded="false">预留信息</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="skuDetail_baseInfo" class="tab-pane fade in active">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">行号</label></td>
                                        <td class="width-25"><label class="pull-left">状态</label></td>
                                        <td class="width-25"><label class="pull-left">商品编码</label></td>
                                        <td class="width-25"><label class="pull-left">商品名称</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <select id="skuDetail_status" name="status" class="form-control m-b" disabled>
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_QC_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                           displayFieldId="skuDetail_skuCode" displayFieldName="skuCode" displayFieldKeyName="skuCode" displayFieldValue=""
                                                           selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                           fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                           searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                           concatId="skuDetail_skuName,skuDetail_packCode,skuDetail_packDesc,skuDetail_price,skuDetail_uom,skuDetail_uomDesc,skuDetail_reserveCode,skuDetail_paRule,skuDetail_paRuleName,skuDetail_uomQty"
                                                           concatName="skuName,packCode,cdpaFormat,price,rcvUom,rcvUomName,reserveCode,paRule,paRuleName,uomQty"
                                                           queryParams="ownerCode" queryParamValues="ownerCode" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_skuName" name="skuName" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">包装规格</label></td>
                                        <td class="width-25"><label class="pull-left">包装单位</label></td>
                                        <td class="width-25"><label class="pull-left">质检项</label></td>
                                        <td class="width-25"><label class="pull-left">批次号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                                           fieldId="skuDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                                           displayFieldId="skuDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                           selectButtonId="skuDetail_packSelectId" deleteButtonId="skuDetail_packDeleteId"
                                                           fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                                           searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control"
                                                           fieldId="skuDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue=""
                                                           displayFieldId="skuDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                           selectButtonId="skuDetail_uomSelectId" deleteButtonId="skuDetail_uomDeleteId"
                                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                                           queryParams="packCode" queryParamValues="skuDetail_packCode" afterSelect="afterSelectDetailPack" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhQcItemHeader/grid" title="选择质检项" cssClass="form-control"
                                                           fieldId="skuDetail_itemGroupCode" fieldName="itemGroupCode" fieldKeyName="itemGroupCode" fieldValue=""
                                                           displayFieldId="skuDetail_itemGroupName" displayFieldName="itemGroupName" displayFieldKeyName="itemGroupName" displayFieldValue=""
                                                           selectButtonId="skuDetail_itemGroupSelectId" deleteButtonId="skuDetail_itemGroupDeleteId"
                                                           fieldLabels="质检项编码|质检项名称" fieldKeys="itemGroupCode|itemGroupName"
                                                           searchLabels="质检项编码|质检项名称" searchKeys="itemGroupCode|itemGroupName" inputSearchKey="itemGroupCodeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">库位</label></td>
                                        <td class="width-25"><label class="pull-left">跟踪号</label></td>
                                        <td class="width-25"><label class="pull-left">质检规则</label></td>
                                        <td class="width-25"><label class="pull-left">上架规则</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                           displayFieldId="skuDetail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="skuDetail_locCodeSelectId" deleteButtonId="skuDetail_planToLocDeleteId"
                                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_traceId" name="traceId" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleQcHeader/grid" title="选择质检规则" cssClass="form-control"
                                                           fieldId="skuDetail_qcRule" fieldName="qcRule" fieldKeyName="ruleCode" fieldValue=""
                                                           displayFieldId="skuDetail_qcRuleName" displayFieldName="qcRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="skuDetail_qcRuleSelectId" deleteButtonId="skuDetail_qcRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRulePaHeader/grid" title="选择上架规则" cssClass="form-control"
                                                           fieldId="skuDetail_paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="skuDetail_paRuleSelectId" deleteButtonId="skuDetail_paRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">可质检数</label></td>
                                        <td class="width-25"><label class="pull-left">可质检数EA</label></td>
                                        <td class="width-25"><label class="pull-left">计划质检数</label></td>
                                        <td class="width-25"><label class="pull-left">计划质检数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyAvailQcUom" name="qtyAvailQcUom" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyAvailQcEa" name="qtyAvailQcEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyPlanQcUom" name="qtyPlanQcUom" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyPlanQcEa" name="qtyPlanQcEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">质检合格数EA</label></td>
                                        <td class="width-25"><label class="pull-left">质检不合格数EA</label></td>
                                        <td class="width-25"><label class="pull-left">合格率(%)</label></td>
                                        <td class="width-25"><label class="pull-left">合格数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyQcQuaEa" name="qtyQcQuaEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyQcUnquaEa" name="qtyQcUnquaEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_pctQua" name="pctQua" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyQuaEa" name="qtyQuaEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">不合格数EA</label></td>
                                        <td class="width-25" colspan="2"><label class="pull-left">质检处理建议</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">实际质检处理</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyUnquaEa" name="qtyUnquaEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25" colspan="2">
                                            <select id="skuDetail_qcSuggest" name="qcSuggest" htmlEscape="false" class="form-control m-b" disabled>
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_QC_SUGGEST')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <select id="skuDetail_qcActSuggest" name="qcActSuggest" htmlEscape="false" class="form-control m-b required">
                                                <option value=""></option>
                                                <c:forEach items="${fns:getDictList('SYS_WM_QC_SUGGEST')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="skuDetail_lotInfo" class="tab-pane fade">
                                <table id="skuDetailLotAttTab" class="bq-table"></table>
                            </div>
                            <div id="skuDetail_itemGroupInfo" class="tab-pane fade">
                                <table id="itemGroupCodeTable" class="table well text-nowrap"></table>
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
                                        <td class="width-25"><label class="pull-left">单据号</label></td>
                                        <td class="width-25"><label class="pull-left">单据行号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_def9" name="def9" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_def10" name="def10" htmlEscape="false" class="form-control" maxlength="64">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_orderNo" name="orderNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_orderLineNo" name="orderLineNo" htmlEscape="false" class="form-control" readonly>
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
        <div id="qcDetailInfo" class="tab-pane fade">
            <div id="qcDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="qc:banQinWmQcDetail:edit">
                    <a class="btn btn-primary" id="qcDetail_save" onclick="saveQcDetail()">保存</a>
                </shiro:hasPermission>
            </div>
            <div id="qcDetail_tab-left">
                <table id="wmQcDetailTable" class="table well text-nowrap"></table>
            </div>
            <div id="qcDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="qcDetailForm" method="post" class="form">
                    <input type="hidden" id="qcDetail_id" name="id"/>
                    <input type="hidden" id="qcDetail_orgId" name="orgId"/>
                    <input type="hidden" id="qcDetail_headId" name="headId"/>
                    <input type="hidden" id="qcDetail_qcNo" name="qcNo"/>
                    <input type="hidden" id="qcDetail_ownerCode" name="ownerCode"/>
                    <input type="hidden" id="qcDetail_qcRcvId" name="qcRcvId"/>
                    <input type="hidden" id="qcDetail_qcQuaRcvId" name="qcQuaRcvId"/>
                    <input type="hidden" id="qcDetail_qcUnquaRcvId" name="qcUnquaRcvId"/>
                    <div class="tabs-container" style="margin: 0">
                        <ul class="nav nav-tabs">
                            <li class="active"><a data-toggle="tab" href="#qcDetail_baseInfo" aria-expanded="true">基本信息</a></li>
                            <li class=""><a data-toggle="tab" href="#qcDetail_lotInfo" aria-expanded="false">批次属性</a></li>
                            <li class=""><a data-toggle="tab" href="#qcDetail_reservedInfo" aria-expanded="false">预留信息</a></li>
                        </ul>
                        <div class="tab-content">
                            <div id="qcDetail_baseInfo" class="tab-pane fade in active">
                                <table class="bq-table">
                                    <tbody>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">质检单行号</label></td>
                                        <td class="width-25"><label class="pull-left">状态</label></td>
                                        <td class="width-25"><label class="pull-left">商品</label></td>
                                        <td class="width-25"><label class="pull-left">包装规格</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="qcDetail_qcLineNo" name="qcLineNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <select id="qcDetail_status" name="status" class="form-control m-b">
                                                <c:forEach items="${fns:getDictList('SYS_WM_QC_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                           fieldId="qcDetail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                                           displayFieldId="qcDetail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                           selectButtonId="qcDetail_skuSelectId" deleteButtonId="qcDetail_skuDeleteId"
                                                           fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                           searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                                           fieldId="qcDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                                           displayFieldId="qcDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                           selectButtonId="qcDetail_packSelectId" deleteButtonId="qcDetail_packDeleteId"
                                                           fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                                           searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">包装单位</label></td>
                                        <td class="width-25"><label class="pull-left">批次号</label></td>
                                        <td class="width-25"><label class="pull-left">库位</label></td>
                                        <td class="width-25"><label class="pull-left">跟踪号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control"
                                                           fieldId="qcDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue=""
                                                           displayFieldId="qcDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                           selectButtonId="qcDetail_uomSelectId" deleteButtonId="qcDetail_uomDeleteId"
                                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                                           queryParams="packCode" queryParamValues="qcDetail_packCode" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                           displayFieldId="qcDetail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="qcDetail_locCodeSelectId" deleteButtonId="qcDetail_locCodeDeleteId"
                                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_traceId" name="traceId" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">质检规则</label></td>
                                        <td class="width-25"><label class="pull-left">质检项</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">良品库位</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">良品跟踪号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleQcHeader/grid" title="选择质检规则" cssClass="form-control"
                                                           fieldId="qcDetail_qcRule" fieldName="qcRule" fieldKeyName="ruleCode" fieldValue=""
                                                           displayFieldId="qcDetail_qcRuleName" displayFieldName="qcRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="qcDetail_qcRuleSelectId" deleteButtonId="skuDetail_qcRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhQcItemHeader/grid" title="选择质检项" cssClass="form-control"
                                                           fieldId="qcDetail_itemGroupCode" fieldName="itemGroupCode" fieldKeyName="itemGroupCode" fieldValue=""
                                                           displayFieldId="qcDetail_itemGroupName" displayFieldName="itemGroupName" displayFieldKeyName="itemGroupName" displayFieldValue=""
                                                           selectButtonId="qcDetail_itemGroupSelectId" deleteButtonId="skuDetail_itemGroupDeleteId"
                                                           fieldLabels="质检项编码|质检项名称" fieldKeys="itemGroupCode|itemGroupName"
                                                           searchLabels="质检项编码|质检项名称" searchKeys="itemGroupCode|itemGroupName" inputSearchKey="itemGroupCodeAndName" disabled="disabled">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="qcDetail_quaLoc" displayFieldName="quaLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="qcDetail_quaLocSelectId" deleteButtonId="qcDetail_quaLocDeleteId"
                                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_quaTraceId" name="quaTraceId" htmlEscape="false" class="form-control required" maxlength="32">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">良品批次号</label></td>
                                        <td class="width-25"><label class="pull-left">良品上架Id</label></td>
                                        <td class="width-25"><label class="pull-left">合格数EA</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">不良品库位</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="qcDetail_quaLotNum" name="quaLotNum" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_quaPaId" name="quaPaId" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_qtyQuaEa" name="qtyQuaEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="qcDetail_unquaLoc" displayFieldName="unquaLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="qcDetail_unquaLocSelectId" deleteButtonId="qcDetail_unquaLocDeleteId"
                                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left asterisk">不良品跟踪号</label></td>
                                        <td class="width-25"><label class="pull-left">不良品批次号</label></td>
                                        <td class="width-25"><label class="pull-left">不良品上架Id</label></td>
                                        <td class="width-25"><label class="pull-left">不合格数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="qcDetail_unquaTraceId" name="unquaTraceId" htmlEscape="false" class="form-control required" maxlength="32">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_unquaLotNum" name="unquaLotNum" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_unquaPaId" name="unquaPaId" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_qtyUnquaEa" name="qtyUnquaEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">收货行号</label></td>
                                        <td class="width-25"><label class="pull-left">质检时间</label></td>
                                        <td class="width-25"><label class="pull-left">可质检数</label></td>
                                        <td class="width-25"><label class="pull-left">可质检数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="qcDetail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_qcTime" name="qcTime" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_qtyAvailQcUom" name="qtyAvailQcUom" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_qtyAvailQcEa" name="qtyAvailQcEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left asterisk">质检合格数EA</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">质检不合格数EA</label></td>
                                        <td class="width-25" colspan="3"><label class="pull-left">备注</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="qcDetail_qtyQcQuaEa" name="qtyQcQuaEa" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0)">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_qtyQcUnquaEa" name="qtyQcUnquaEa" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0)">
                                        </td>
                                        <td class="width-25" colspan="3">
                                            <input id="qcDetail_remarks" name="remarks" htmlEscape="false" class="form-control" maxlength="512">
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                            <div id="qcDetail_lotInfo" class="tab-pane fade">
                                <table id="qcDetailLotAttTab" class="bq-table"></table>
                            </div>
                            <div id="qcDetail_reservedInfo" class="tab-pane fade">
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
                                            <input id="qcDetail_def1" name="def1" htmlEscape="false" class="form-control">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_def2" name="def2" htmlEscape="false" class="form-control">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_def3" name="def3" htmlEscape="false" class="form-control">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_def4" name="def4" htmlEscape="false" class="form-control">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <label class="pull-left">自定义5</label>
                                        </td>
                                        <td class="width-25">
                                            <label class="pull-left">自定义6</label>
                                        </td>
                                        <td class="width-25">
                                            <label class="pull-left">自定义7</label>
                                        </td>
                                        <td class="width-25">
                                            <label class="pull-left">自定义8</label>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="qcDetail_def5" name="def5" htmlEscape="false" class="form-control">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_def6" name="def6" htmlEscape="false" class="form-control">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_def7" name="def7" htmlEscape="false" class="form-control">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_def8" name="def8" htmlEscape="false" class="form-control">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">自定义9</label></td>
                                        <td class="width-25"><label class="pull-left">自定义10</label></td>
                                        <td class="width-25"><label class="pull-left">单据号</label></td>
                                        <td class="width-25"><label class="pull-left">单据行号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="qcDetail_def9" name="def9" htmlEscape="false" class="form-control">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_def10" name="def10" htmlEscape="false" class="form-control">
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_orderNo" name="orderNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="qcDetail_orderLineNo" name="orderLineNo" htmlEscape="false" class="form-control" readonly>
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
                <shiro:hasPermission name="qc:banQinWmQcDetail:deleteTaskPa">
                    <a class="btn btn-primary" id="paTask_remove" onclick="removePaTask()">删除上架任务</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="qc:banQinWmQcDetail:putAway">
                    <a class="btn btn-primary" id="paTask_confirm" onclick="confirmPaTask()">上架确认</a>
                </shiro:hasPermission>
            </div>
            <div id="paTask_tab-left">
                <table id="wmQcPaTaskTable" class="table well text-nowrap"></table>
            </div>
            <div id="paTask_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="paTaskForm" method="post" class="form">
                    <input type="hidden" id="paTask_id" name="id"/>
                    <input type="hidden" id="paTask_orgId" name="orgId"/>
                    <input type="hidden" id="paTask_qtyPaEa" name="qtyPaEa"/>
                    <input type="hidden" id="paTask_orderType" name="orderType"/>
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
                            <td class="width-25"><label class="pull-left">包装单位</label></td>
                            <td class="width-25"><label class="pull-left">上架库位指定规则</label></td>
                            <td class="width-25"><label class="pull-left">上架规则</label></td>
                            <td class="width-25"><label class="pull-left">批次号</label></td>
                        </tr>
                        <tr>
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
                                <input id="paTask_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">源库位</label></td>
                            <td class="width-25"><label class="pull-left">源跟踪号</label></td>
                            <td class="width-25"><label class="pull-left">上架时间</label></td>
                            <td class="width-25"><label class="pull-left">上架人</label></td>
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
                                <div class='input-group form_datetime' id='paTask_paTimeF'>
                                    <input id="paTask_paTime" name="paTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    <span class="input-group-addon">
                                        <span class="glyphicon glyphicon-calendar"></span>
                                    </span>
                                </div>
                            </td>
                            <td class="width-25">
                                <input id="paTask_paOp" name="paOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">推荐库位</label></td>
                            <td class="width-25"><label class="pull-left asterisk">上架库位</label></td>
                            <td class="width-25"><label class="pull-left asterisk">上架跟踪号</label></td>
                            <td class="width-25"><label class="pull-left">上架数</label></td>
                        </tr>
                        <tr>
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
                            <td class="width-25">
                                <input id="paTask_toId" name="toId" htmlEscape="false" class="form-control required" max="64">
                            </td>
                            <td class="width-25">
                                <input id="paTask_qtyPaUom" name="qtyPaUom" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">上架数EA</label></td>
                            <td class="width-25" colspan="5"><label class="pull-left">备注</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="paTask_currentPaQtyEa" name="currentPaQtyEa" onkeyup="bq.numberValidator(this, 2, 0);" htmlEscape="false" class="form-control">
                            </td>
                            <td class="width-25" colspan="5">
                                <input id="paTask_remarks" name="remarks" htmlEscape="false" class="form-control" maxlength="64">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>