<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>转移单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmTfHeaderForm.js" %>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="inventory:banQinWmTfHeader:edit">
        <a class="btn btn-primary btn-sm" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmTfHeader:audit">
        <a class="btn btn-primary btn-sm" id="header_audit" onclick="audit()">审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmTfHeader:cancelAudit">
        <a class="btn btn-primary btn-sm" id="header_cancelAudit" onclick="cancelAudit()">取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmTfHeader:transfer">
        <a class="btn btn-primary btn-sm" id="header_transfer" onclick="transfer()">执行转移</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmTfHeader:closeOrder">
        <a class="btn btn-primary btn-sm" id="header_closeOrder" onclick="closeOrder()">关闭订单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmTfHeader:cancelOrder">
        <a class="btn btn-primary btn-sm" id="header_cancelOrder" onclick="cancelOrder()">取消订单</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmTfHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">转移单号</label></td>
                <td class="width-15">
                    <form:input path="tfNo" htmlEscape="false" class="form-control "/>
                </td>
                <td class="width-10"><label class="pull-right"><font color="red">*</font>货主</label></td>
                <td class="width-15">
                    <input id="ownerType" value="OWNER" type="hidden">
                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                   fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${banQinWmTfHeaderEntity.ownerCode}" allowInput="true"
                                   displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinWmTfHeaderEntity.ownerName}"
                                   selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                   queryParams="ebcuType" queryParamValues="ownerType">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">状态</label></td>
                <td class="width-15">
                    <form:select path="status" class="form-control m-b">
                        <form:options items="${fns:getDictList('SYS_WM_TF_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">审核状态</label></td>
                <td class="width-15">
                    <form:select path="auditStatus" class="form-control m-b">
                        <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">审核人</label></td>
                <td class="width-15">
                    <form:input path="auditOp" htmlEscape="false" class="form-control "/>
                </td>
                <td class="width-10"><label class="pull-right">审核时间</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime'>
                        <input type='text' id="auditTime" name="auditTime" class="form-control"
                               value="<fmt:formatDate value="${banQinWmTfHeaderEntity.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right">审核意见</label></td>
                <td class="width-15" colspan="3">
                    <form:input path="auditComment" htmlEscape="false" class="form-control " maxlength="512"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">转移原因</label></td>
                <td class="width-15">
                    <form:select path="reasonCode" class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('SYS_WM_TF_REASON')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">原因描述</label></td>
                <td class="width-15">
                    <form:input path="reason" htmlEscape="false" class="form-control " maxlength="512"/>
                </td>
                <td class="width-10"><label class="pull-right">转移操作人</label></td>
                <td class="width-15">
                    <form:input path="tfOp" htmlEscape="false" class="form-control "/>
                </td>
                <td class="width-10"><label class="pull-right">转移时间</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime'>
                        <input type='text' id='tfTime' name="tfTime" class="form-control"
                               value="<fmt:formatDate value="${banQinWmTfHeaderEntity.tfTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">自定义1</label></td>
                <td class="width-15">
                    <form:input path="def1" htmlEscape="false" class="form-control " maxlength="64"/>
                </td>
                <td class="width-10"><label class="pull-right">自定义2</label></td>
                <td class="width-15">
                    <form:input path="def2" htmlEscape="false" class="form-control " maxlength="64"/>
                </td>
                <td class="width-10"><label class="pull-right">自定义3</label></td>
                <td class="width-15">
                    <form:input path="def3" htmlEscape="false" class="form-control " maxlength="64"/>
                </td>
                <td class="width-10"><label class="pull-right">自定义4</label></td>
                <td class="width-15">
                    <form:input path="def4" htmlEscape="false" class="form-control " maxlength="64"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<div class="tabs-container">
    <div id="detailToolbar" style="width: 100%; padding: 5px 0;">
        <shiro:hasPermission name="inventory:banQinWmTfDetail:addDetail">
            <a class="btn btn-primary btn-sm" id="detail_add" onclick="addDetail()">新增</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmTfDetail:saveDetail">
            <a class="btn btn-primary btn-sm" id="detail_save" onclick="saveDetail()">保存</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmTfDetail:removeDetail">
            <a class="btn btn-danger btn-sm" id="detail_remove" onclick="removeDetail()">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmTfDetail:transferDetail">
            <a class="btn btn-primary btn-sm" id="detail_transfer" onclick="transferDetail()">执行转移</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmTfDetail:cancelDetail">
            <a class="btn btn-primary btn-sm" id="detail_cancel" onclick="cancelDetail()">取消订单</a>
        </shiro:hasPermission>
    </div>
    <div id="tab1-left">
        <table id="wmTfDetailTable" class="well text-nowrap"></table>
    </div>
    <div id="tab1-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
        <form:form id="inputForm1" method="post" class="form">
            <input type="hidden" id="detail_id" name="id"/>
            <input type="hidden" id="detail_orgId" name="orgId"/>
            <input type="hidden" id="detail_headerId" name="headerId"/>
            <input type="hidden" id="detail_tfNo" name="tfNo"/>
            <input type="hidden" id="detail_fmCdprQuantity" name="fmCdprQuantity"/>
            <input type="hidden" id="detail_toCdprQuantity" name="toCdprQuantity"/>
            <div class="tabs-container" style="margin: 0 0 10px 0;">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#tab-1-1" aria-expanded="true">转移信息</a></li>
                    <li class=""><a data-toggle="tab" href="#tab-1-2" aria-expanded="true">批次属性</a></li>
                    <%--<li class=""><a data-toggle="tab" href="#tab-1-3" aria-expanded="true">序列号</a></li>--%>
                </ul>
                <div class="tab-content">
                    <div id="tab-1-1" class="tab-pane fade in active">
                        <table class="bq-table">
                            <tbody>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">行号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">状态</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_lineNo" name="lineNo" readonly htmlEscape="false" class="form-control"/>
                                </td>
                                <td class="width-25">
                                    <select id="detail_status" disabled name="status" class="form-control m-b">
                                        <c:forEach items="${fns:getDictList('SYS_WM_TF_STATUS')}" var="dict">
                                            <option value="${dict.value}">${dict.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">源货主</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">源商品</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">源库位</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">源批次</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                   fieldId="detail_fmOwner" fieldName="fmOwner" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                   displayFieldId="detail_fmOwnerName" displayFieldName="fmOwnerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                   selectButtonId="detail_fmOwnerSelectId" deleteButtonId="detail_fmOwnerDeleteId"
                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotLoc/data" title="选择商品批次库位库存" cssClass="form-control"
                                                   fieldId="detail_fmSku" fieldName="fmSku" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_fmSkuName" displayFieldName="fmSkuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                   selectButtonId="detail_fmSkuSelectId" deleteButtonId="detail_fmSkuDeleteId"
                                                   fieldLabels="货主编码|货主名称|商品编码|商品名称|批次号|库位|跟踪号|库存数|库存可用数|冻结数|分配数|拣货数|上架待出数|移动待出数|补货待出数|批次属性01|批次属性02|批次属性03|批次属性04|批次属性05|批次属性05|批次属性07|批次属性08|批次属性09|批次属性10|批次属性11|批次属性12"
                                                   fieldKeys="ownerCode|ownerName|skuCode|skuName|lotNum|locCode|traceId|qty|qtyAvailable|qtyHold|qtyAlloc|qtyPk|qtyPaOut|qtyMvOut|qtyRpOut|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                                   searchLabels="商品编码|批次号|库位|跟踪号" searchKeys="skuCode|lotNum|locCode|traceId" inputSearchKey="codeAndName"
                                                   concatId="detail_fmOwner,detail_fmOwnerName,detail_fmLoc,detail_fmId,detail_toLoc,detail_toId,detail_fmLot,detail_fmPack,detail_fmPackDesc,detail_fmUom,detail_fmUomDesc,detail_fmQtyUom,detail_fmCdprQuantity"
                                                   concatName="ownerCode,ownerName,locCode,traceId,locCode,traceId,lotNum,packCode,packDesc,printUom,uomDesc,qtyAvailable,uomQty"
                                                   queryParams="ownerCode" queryParamValues="ownerCode"
                                                   afterSelect="afterSelectFmSku">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                   displayFieldId="detail_fmLoc" displayFieldName="fmLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                   selectButtonId="detail_fmLocSelectId" deleteButtonId="detail_fmLocDeleteId"
                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotAtt/data" title="选择批次" cssClass="form-control"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                   displayFieldId="detail_fmLot" displayFieldName="fmLot" displayFieldKeyName="lotNum" displayFieldValue=""
                                                   selectButtonId="detail_fmLotSelectId" deleteButtonId="detail_fmLotDeleteId"
                                                   fieldLabels="批次号|商品编码|货主编码|生产日期|失效日期|入库日期|批次属性4" fieldKeys="lotNum|skuCode|ownerCode|lotAtt01|lotAtt02|lotAtt03|lotAtt04"
                                                   searchLabels="批次号" searchKeys="lotNum" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">源跟踪号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">源包装规格</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">源包装单位</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">转出数</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_fmId" disabled name="fmId" htmlEscape="false" class="form-control "/>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                                   fieldId="detail_fmPack" fieldName="fmPack" fieldKeyName="cdpaCode" fieldValue=""
                                                   displayFieldId="detail_fmPackDesc" displayFieldName="fmPackDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                   selectButtonId="detail_fmPackSelectId" deleteButtonId="detail_fmPackDeleteId"
                                                   fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                                   searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                                   fieldId="detail_fmUom" fieldName="fmUom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_fmUomDesc" displayFieldName="fmUomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                   selectButtonId="detail_fmUomSelectId" deleteButtonId="detail_fmUomDeleteId"
                                                   fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                   searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                                   queryParams="packCode" queryParamValues="detail_fmPack" afterSelect="afterSelectFmPack">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <input id="detail_fmQtyUom" name="fmQtyUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="fmQtyChange(this)"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">转出数EA</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_fmQtyEa" name="fmQtyEa" htmlEscape="false" class="form-control" readonly/>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left asterisk">目标货主</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">目标商品</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">目标库位</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">目标批次</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                   fieldId="detail_toOwner" fieldName="toOwner" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_toOwnerName" displayFieldName="toOwnerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                   selectButtonId="detail_toOwnerSelectId" deleteButtonId="detail_toOwnerDeleteId"
                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                   queryParams="ebcuType" queryParamValues="ownerType"
                                                   afterSelect="afterSelectToOwner">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择目标商品" cssClass="form-control"
                                                   fieldId="detail_toSku" fieldName="toSku" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_toSkuName" displayFieldName="toSkuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                   selectButtonId="detail_toSkuSelectId" deleteButtonId="detail_toSkuDeleteId"
                                                   fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                   concatId="detail_toPack,detail_toPackDesc,detail_toUom,detail_toUomDesc,detail_toCdprQuantity"
                                                   concatName="packCode,cdpaFormat,printUom,printUomName,uomQty"
                                                   queryParams="ownerCode" queryParamValues="detail_toOwner"
                                                   afterSelect="afterSelectToSku">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                   selectButtonId="detail_toLocSelectId" deleteButtonId="detail_toLocDeleteId"
                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotAtt/data" title="选择批次" cssClass="form-control"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_toLot" displayFieldName="toLot" displayFieldKeyName="lotNum" displayFieldValue=""
                                                   selectButtonId="detail_toLotSelectId" deleteButtonId="detail_toLotDeleteId"
                                                   fieldLabels="批次号|商品编码|货主编码|生产日期|失效日期|入库日期|批次属性4" fieldKeys="lotNum|skuCode|ownerCode|lotAtt01|lotAtt02|lotAtt03|lotAtt04"
                                                   searchLabels="批次号" searchKeys="lotNum" inputSearchKey="codeAndName"
                                                   queryParams="ownerCode|skuCode" queryParamValues="detail_toOwner|detail_toSku">
                                    </sys:popSelect>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">目标跟踪号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">目标包装规格</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">目标包装单位</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">转入数</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_toId" name="toId" htmlEscape="false" class="form-control " max="32"/>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control required"
                                                   fieldId="detail_toPack" fieldName="toPack" fieldKeyName="cdpaCode" fieldValue=""
                                                   displayFieldId="detail_toPackDesc" displayFieldName="toPackDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                   selectButtonId="detail_toPackSelectId" deleteButtonId="detail_toPackDeleteId"
                                                   fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                                   searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                                   fieldId="detail_toUom" fieldName="toUom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_toUomDesc" displayFieldName="toUomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                   selectButtonId="detail_toUomSelectId" deleteButtonId="detail_toUomDeleteId"
                                                   fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                   searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                                   queryParams="packCode" queryParamValues="detail_fmPack" afterSelect="afterSelectToPack">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <input id="detail_toQtyUom" name="toQtyUom" htmlEscape="false" class="form-control required" onkeyup="value=value.replace(/[^\d]/g,'')" oninput="toQtyChange()"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">转入数EA</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_toQtyEa" name="toQtyEa" htmlEscape="false" class="form-control" readonly/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div id="tab-1-2" class="tab-pane fade in">
                        <div style="width: 48%; float: left;">
                            <h5 style="background-color: #2c3e50; height: 30px; line-height: 30px; color: white;">&nbsp;&nbsp;源批次属性</h5>
                            <table id="detailFmLotAttTab" style="border: 1px solid #dddddd;" class="bq-table"></table>
                        </div>
                        <div style="width: 2%; float: left;">&nbsp;</div>
                        <div style="width: 48%; float: left;">
                            <h5 style="background-color: #2c3e50; height: 30px; line-height: 30px; color: white;">&nbsp;&nbsp;目标批次属性</h5>
                            <table id="detailToLotAttTab" style="border: 1px solid #dddddd;" class="bq-table"></table>
                        </div>
                    </div>
                    <div id="tab-1-3" class="tab-pane fade in">
                        <table class="bq-table">
                            <tbody>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">转出序列号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">是否扣数</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">转入序列号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">是否扣数</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="outSerial" htmlEscape="false" class="form-control "/>
                                </td>
                                <td class="width-25">
                                    <input id="outIsDedu" type="checkbox" class="myCheckbox" disabled/>
                                </td>
                                <td class="width-25">
                                    <input id="inSerial" htmlEscape="false" class="form-control "/>
                                </td>
                                <td class="width-25">
                                    <input id="inIsDedu" type="checkbox" class="myCheckbox" disabled/>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">是否更改</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">锁定转出</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">锁定转入</label>
                                </td>
                                <td class="width-25">
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="isModiff" type="checkbox" class="myCheckbox" disabled/>
                                </td>
                                <td width="20">
                                    <input id="lockOut" type="checkbox" class="myCheckbox" disabled/>
                                </td>
                                <td class="width-25">
                                    <input id="lockIn" type="checkbox" class="myCheckbox" disabled/>
                                </td>
                                <td class="width-25">
                                </td>
                            </tr>
                            </tbody>
                        </table>
                        <a class="btn btn-danger btn-sm" id="serial_remove" onclick="">删除</a>
                        <table class="table well text-nowrap">
                            <thead>
                            <tr>
                                <th class="hide"></th>
                                <th>转移方式</th>
                                <th>货主</th>
                                <th>商品</th>
                                <th>批次号</th>
                                <th>序列号</th>
                                <th width="10">&nbsp;</th>
                            </tr>
                            </thead>
                            <tbody id="wmsAdSerialDetailList">
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