<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>移动单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmMvForm.js" %>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="inventory:banQinWmMv:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmMv:audit">
        <a class="btn btn-primary" id="header_audit" onclick="audit()">审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmMv:cancelAudit">
        <a class="btn btn-primary" id="header_cancelAudit" onclick="cancelAudit()">取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmMv:executeMv">
        <a class="btn btn-primary" id="header_executeMv" onclick="executeMv()">执行移动</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmMv:closeOrder">
        <a class="btn btn-primary" id="header_closeOrder" onclick="closeOrder()">关闭订单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmMv:cancelOrder">
        <a class="btn btn-primary" id="header_cancelOrder" onclick="cancelOrder()">取消订单</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmMvEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">移动单号</label></td>
                <td class="width-15">
                    <form:input path="mvNo" htmlEscape="false" class="form-control "/>
                </td>
                <td class="width-10"><label class="pull-right asterisk">货主</label></td>
                <td class="width-15">
                    <input id="ownerType" value="OWNER" type="hidden">
                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主"
                                   cssClass="form-control required"
                                   fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo"
                                   fieldValue="${banQinWmMvEntity.ownerCode}" allowInput="true"
                                   displayFieldId="ownerName" displayFieldName="ownerName"
                                   displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinWmMvEntity.ownerName}"
                                   selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                   inputSearchKey="codeAndName"
                                   queryParams="ebcuType" queryParamValues="ownerType">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">状态</label></td>
                <td class="width-15">
                    <form:select path="status" class="form-control m-b">
                        <form:options items="${fns:getDictList('SYS_WM_MV_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">审核状态</label></td>
                <td class="width-15">
                    <form:select path="auditStatus" class="form-control m-b">
                        <form:options items="${fns:getDictList('SYS_WM_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                               value="<fmt:formatDate value="${banQinWmMvEntity.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right">审核意见</label></td>
                <td class="width-15" colspan="3">
                    <form:input path="auditComment" htmlEscape="false" class="form-control " maxlength="512"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">移动时间</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='mvTime'>
                        <input type='text' name="mvTime" class="form-control"
                               value="<fmt:formatDate value="${banQinWmMvEntity.mvTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right">移动操作人</label></td>
                <td class="width-15">
                    <form:input path="mvOp" htmlEscape="false" class="form-control "/>
                </td>
                <td class="width-10"><label class="pull-right">移动原因</label></td>
                <td class="width-15">
                    <form:select path="reasonCode" class="form-control m-b">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('SYS_WM_MV_REASON')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">原因描述</label></td>
                <td class="width-15">
                    <form:input path="reason" htmlEscape="false" class="form-control " maxlength="512"/>
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
        <shiro:hasPermission name="inventory:banQinWmMvDetail:add">
            <a class="btn btn-primary" id="detail_add" onclick="addDetail()">新增</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmMvDetail:edit">
            <a class="btn btn-primary" id="detail_save" onclick="saveDetail()">保存</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmMvDetail:del">
            <a class="btn btn-danger" id="detail_remove" onclick="removeDetail()">删除</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmMvDetail:executeMv">
            <a class="btn btn-primary" id="detail_executeMv" onclick="executeMvDetail()">执行移动</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="inventory:banQinWmMvDetail:cancelDetail">
            <a class="btn btn-primary" id="detail_cancel" onclick="cancelDetail()">取消订单</a>
        </shiro:hasPermission>
    </div>
    <div id="tab1-left">
        <table id="wmMvDetailTable" class="text-nowrap"></table>
    </div>
    <div id="tab1-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
        <form:form id="inputForm1" method="post" class="form">
            <input type="hidden" id="detail_id" name="id"/>
            <input type="hidden" id="detail_orgId" name="orgId"/>
            <input type="hidden" id="detail_headerId" name="headerId"/>
            <input type="hidden" id="detail_mvNo" name="mvNo"/>
            <input type="hidden" id="detail_ownerCode" name="ownerCode"/>
            <input type="hidden" id="detail_fmCdprQuantity" name="fmCdprQuantity"/>
            <input type="hidden" id="detail_toCdprQuantity" name="toCdprQuantity"/>
            <div class="tabs-container" style="margin: 0 0 10px 0;">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#tab-1-1" aria-expanded="true">移动信息</a></li>
                    <li class=""><a data-toggle="tab" href="#tab-1-2" aria-expanded="true">批次属性</a></li>
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
                                <td class="width-25">
                                    <label class="pull-left asterisk">商品</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">包装规格</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_lineNo" name="lineNo" readonly htmlEscape="false" class="form-control"/>
                                </td>
                                <td class="width-25">
                                    <select id="detail_status" disabled name="status" class="form-control m-b">
                                        <c:forEach items="${fns:getDictList('SYS_WM_MV_STATUS')}" var="dict">
                                            <option value="${dict.value}">${dict.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvMv/data" title="选择商品批次库位库存"
                                                   cssClass="form-control"
                                                   fieldId="detail_skuCode" fieldName="skuCode" fieldKeyName="skuCode"
                                                   fieldValue="" allowInput="true"
                                                   displayFieldId="detail_skuName" displayFieldName="skuName"
                                                   displayFieldKeyName="skuName" displayFieldValue=""
                                                   selectButtonId="detail_skuSelectId"
                                                   deleteButtonId="detail_skuDeleteId"
                                                   fieldLabels="货主编码|货主名称|商品编码|商品名称|批次号|库位|跟踪号|库存数|库存可用数|冻结数|分配数|拣货数|上架待出数|移动待出数|补货待出数|批次属性01|批次属性02|批次属性03|批次属性04|批次属性05|批次属性05|批次属性07|批次属性08|批次属性09|批次属性10|批次属性11|批次属性12"
                                                   fieldKeys="ownerCode|ownerName|skuCode|skuName|lotNum|fmLoc|fmTraceId|fmQty|availableQty|qtyHold|qtyAlloc|qtyPk|qtyPaOut|qtyMvOut|qtyRpOut|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                                   searchLabels="商品编码|批次号|库位|跟踪号"
                                                   searchKeys="skuCode|lotNum|locCode|traceId"
                                                   inputSearchKey="codeAndName"
                                                   concatId="detail_fmLocCode,detail_fmTraceId,detail_lotNum,detail_packCode,detail_packDesc,detail_fmUom,detail_fmUomDesc,detail_fmCdprQuantity,detail_toUom,detail_toUomDesc,detail_toCdprQuantity"
                                                   concatName="fmLoc,fmTraceId,lotNum,packCode,cdpaFormat,toUom,toUomDesc,cdprQuantity,toUom,toUomDesc,cdprQuantity"
                                                   queryParams="ownerCode" queryParamValues="ownerCode"
                                                   afterSelect="afterSelectFmSku">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装"
                                                   cssClass="form-control"
                                                   fieldId="detail_packCode" fieldName="packCode"
                                                   fieldKeyName="cdpaCode" fieldValue=""
                                                   displayFieldId="detail_packDesc" displayFieldName="packDesc"
                                                   displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                   selectButtonId="detail_packSelectId"
                                                   deleteButtonId="detail_packDeleteId"
                                                   fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                                   searchLabels="包装代码|包装类型|包装规格"
                                                   searchKeys="cdpaCode|cdpaType|cdpaFormat"
                                                   inputSearchKey="codeAndName" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">批次号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">源库位</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">源跟踪号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left">源包装单位</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotAtt/data" title="选择批次"
                                                   cssClass="form-control"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                   displayFieldId="detail_lotNum" displayFieldName="lotNum"
                                                   displayFieldKeyName="lotNum" displayFieldValue=""
                                                   selectButtonId="detail_lotSelectId"
                                                   deleteButtonId="detail_lotDeleteId"
                                                   fieldLabels="批次号|商品编码|货主编码|生产日期|失效日期|入库日期|批次属性4"
                                                   fieldKeys="lotNum|skuCode|ownerCode|lotAtt01|lotAtt02|lotAtt03|lotAtt04"
                                                   searchLabels="批次号" searchKeys="lotNum" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位"
                                                   cssClass="form-control"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                   displayFieldId="detail_fmLocCode" displayFieldName="fmLocCode"
                                                   displayFieldKeyName="locCode" displayFieldValue=""
                                                   selectButtonId="detail_fmLocSelectId"
                                                   deleteButtonId="detail_fmLocDeleteId"
                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName"
                                                   inputSearchKey="codeAndName" disabled="disabled">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <input id="detail_fmTraceId" disabled name="fmTraceId" htmlEscape="false" class="form-control "/>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid"
                                                   title="选择包装单位" cssClass="form-control required"
                                                   fieldId="detail_fmUom" fieldName="fmUom" fieldKeyName="cdprUnitLevel"
                                                   fieldValue="" disabled="disabled"
                                                   displayFieldId="detail_fmUomDesc" displayFieldName="fmUomDesc"
                                                   displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                   selectButtonId="detail_fmUomSelectId"
                                                   deleteButtonId="detail_fmUomDeleteId"
                                                   fieldLabels="包装单位|数量|包装描述"
                                                   fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                   searchLabels="包装单位|数量|包装描述"
                                                   searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                   inputSearchKey="codeAndName"
                                                   queryParams="packCode" queryParamValues="detail_fmPack"
                                                   afterSelect="afterSelectFmPack">
                                    </sys:popSelect>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left asterisk">目标库位</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">目标跟踪号</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">目标包装单位</label>
                                </td>
                                <td class="width-25">
                                    <label class="pull-left asterisk">移动操作数</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位"
                                                   cssClass="form-control required" allowInput="true"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                   displayFieldId="detail_toLocCode" displayFieldName="toLocCode"
                                                   displayFieldKeyName="locCode" displayFieldValue=""
                                                   selectButtonId="detail_toLocSelectId"
                                                   deleteButtonId="detail_toLocDeleteId"
                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName"
                                                   inputSearchKey="codeAndName">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <input id="detail_toTraceId" name="toTraceId" htmlEscape="false" class="form-control required" max="32"/>
                                </td>
                                <td class="width-25">
                                    <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid"
                                                   title="选择包装单位" cssClass="form-control required"
                                                   fieldId="detail_toUom" fieldName="toUom" fieldKeyName="cdprUnitLevel"
                                                   fieldValue="" allowInput="true"
                                                   displayFieldId="detail_toUomDesc" displayFieldName="toUomDesc"
                                                   displayFieldKeyName="cdprDesc" displayFieldValue=""
                                                   selectButtonId="detail_toUomSelectId"
                                                   deleteButtonId="detail_toUomDeleteId"
                                                   fieldLabels="包装单位|数量|包装描述"
                                                   fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                   searchLabels="包装单位|数量|包装描述"
                                                   searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                                   inputSearchKey="codeAndName"
                                                   queryParams="packCode" queryParamValues="detail_packCode"
                                                   afterSelect="afterSelectToPack">
                                    </sys:popSelect>
                                </td>
                                <td class="width-25">
                                    <input id="detail_qtyMvUom" name="qtyMvUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 0, 0)" oninput="toQtyChange()"/>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <label class="pull-left">移动操作数EA</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-25">
                                    <input id="detail_qtyMvEa" name="qtyMvEa" htmlEscape="false" class="form-control" readonly/>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div id="tab-1-2" class="tab-pane fade in">
                        <table id="detailFmLotAttTab" class="bq-table"></table>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>
</body>
</html>