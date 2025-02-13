<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>波次管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmWvHeaderForm.js" %>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="outbound:banQinWmSoHeader:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:alloc">
        <a class="btn btn-primary" id="header_alloc" onclick="alloc()">分配</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmWvHeader:dispathPk">
        <button class="btn btn-primary" id="header_dispathPk" onclick="dispathPk()">分派拣货</button>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmWvHeader:picking">
        <a class="btn btn-primary" id="header_picking" onclick="picking()">拣货确认</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmWvHeader:shipment">
        <a class="btn btn-primary" id="header_shipment" onclick="shipment()">发货确认</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmWvHeader:getWaybill">
        <a class="btn btn-primary" id="header_getWaybill" onclick="getWaybill()">获取面单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmWvHeader:printWaybill">
        <a class="btn btn-primary" id="header_printWaybill" onclick="printWaybill()">打印面单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmWvHeader:getAndPrintWaybill">
        <a class="btn btn-primary" id="header_getAndPrintWaybill" onclick="getAndPrintWaybill()">获取面单并打印</a>
    </shiro:hasPermission>
    <div class="btn-group">
        <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown">更多<span class="caret"></span></a>
        <ul class="dropdown-menu">
            <shiro:hasPermission name="outbound:banQinWmWvHeader:cancelAlloc">
                <li><a id="header_cancelAlloc" onclick="cancelAlloc()">取消分配</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmWvHeader:cancelPicking">
                <li><a id="header_cancelPicking" onclick="cancelPicking()">取消拣货</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmWvHeader:cancelShipment">
                <li><a id="header_cancelShipment" onclick="cancelShipment()">取消发货</a></li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>
<form:form id="inputForm" modelAttribute="banQinWmWvHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:140px">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">波次单号</label></td>
                <td class="width-15">
                    <form:input path="waveNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">状态</label></td>
                <td class="width-15">
                    <form:select path="status" class="form-control required" disabled="true">
                        <form:options items="${fns:getDictList('SYS_WM_WV_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">波次生成规则</label></td>
                <td class="width-15" colspan="3">
                    <form:input path="remarks" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">打印次数</label></td>
                <td class="width-15">
                    <form:input path="def1" htmlEscape="false" class="form-control" maxlength="64" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">是否获取过面单</label></td>
                <td class="width-15">
                    <form:select path="def2" class="form-control" disabled="true">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">是否打印过面单</label></td>
                <td class="width-15">
                    <form:select path="def3" class="form-control" disabled="true">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
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
</form:form>
<div class="tabs-container">
    <ul id="detailTab" class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#wvSoInfo" aria-expanded="true" onclick="detailTabChange(0)">发运订单</a></li>
        <li class=""><a data-toggle="tab" href="#wvDetailInfo" aria-expanded="true" onclick="detailTabChange(1)">波次明细</a></li>
        <li class=""><a data-toggle="tab" href="#allocDetailInfo" aria-expanded="true" onclick="detailTabChange(2)">分配明细</a></li>
        <li class=""><a data-toggle="tab" href="#cancelAllocLog" aria-expanded="true">取消分配日志</a></li>
    </ul>
    <div class="tab-content">
        <div id="wvSoInfo" class="tab-pane fade in active">
            <div id="wvSoToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="outbound:banQinWmWvDetail:soAlloc">
                    <a class="btn btn-primary" id="so_alloc" onclick="soAlloc()">分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:soPicking">
                    <a class="btn btn-primary" id="so_picking" onclick="soPicking()">拣货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:soShipment">
                    <a class="btn btn-primary" id="so_shipment" onclick="soShipment()">发货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:soCancelAlloc">
                    <a class="btn btn-primary" id="so_cancelAlloc" onclick="soCancelAlloc()">取消分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:soCancelPicking">
                    <a class="btn btn-primary" id="so_cancelPicking" onclick="soCancelPicking()">取消拣货</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:soCancelShipment">
                    <a class="btn btn-primary" id="so_cancelShipment" onclick="soCancelShipment()">取消发货</a>
                </shiro:hasPermission>
            </div>
            <table id="wvSoTable" class="table well text-nowrap"></table>
        </div>
        <div id="wvDetailInfo" class="tab-pane fade">
            <div id="wvDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="outbound:banQinWmWvDetail:wvAlloc">
                    <a class="btn btn-primary" id="wv_alloc" onclick="wvAlloc()">分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:wvManualAlloc">
                    <a class="btn btn-primary" id="wv_manualAlloc" onclick="wvManualAlloc()">手工分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:wvCancelAlloc">
                    <a class="btn btn-primary" id="wv_cancelAlloc" onclick="wvCancelAlloc()">取消分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:wvPrintLabel">
                    <a class="btn btn-primary" id="wv_printLabel" onclick="printLabel()">打印标签</a>
                </shiro:hasPermission>
            </div>
            <table id="wvDetailTable" class="table well text-nowrap"></table>
        </div>
        <div id="allocDetailInfo" class="tab-pane fade">
            <div id="allocDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="outbound:banQinWmWvDetail:allocAdd">
                    <a class="btn btn-primary" id="alloc_add" onclick="allocAdd()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:allocSave">
                    <a class="btn btn-primary" id="alloc_save" onclick="allocSave()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:allocPick">
                    <a class="btn btn-primary" id="alloc_pick" onclick="allocPick()">拣货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:allocShipment">
                    <a class="btn btn-primary" id="alloc_shipment" onclick="allocShipment()">发货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:allocCancelAlloc">
                    <a class="btn btn-primary" id="alloc_cancelAlloc" onclick="allocCancelAlloc()">取消分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:allocCancelPick">
                    <a class="btn btn-primary" id="alloc_cancelPick" onclick="allocCancelPick()">取消拣货</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmWvDetail:allocCancelShipment">
                    <a class="btn btn-primary" id="alloc_cancelShipment" onclick="allocCancelShipment()">取消发货</a>
                </shiro:hasPermission>
            </div>
            <div id="allocDetail_tab-left">
                <table id="wmWvAllocTable" class="table well text-nowrap"></table>
            </div>
            <div id="allocDetail_tab-right" style="overflow: scroll; height: 550px; border: 1px solid #dddddd; display: none;">
                <form:form id="allocDetailForm" method="post" class="form-horizontal">
                    <input type="hidden" id="allocDetail_id" name="id"/>
                    <input type="hidden" id="allocDetail_orgId" name="orgId"/>
                    <input type="hidden" id="allocDetail_qtyPkEa" name="qtyPkEa"/>
                    <input type="hidden" id="allocDetail_qtyPkUom" name="qtyPkUom"/>
                    <input type="hidden" id="allocDetail_uomQty" name="uomQty"/>
                    <input type="hidden" id="allocDetail_remarks" name="remarks"/>
                    <input type="hidden" id="allocDetail_preallocId" name="preallocId"/>
                    <%--手工分配行商品--%>
                    <input type="hidden" id="allocDetail_skuCodeParam"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-20">
                                <label class="pull-left">分配Id</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">波次单号</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">出库单号</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">行号</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">越库类型</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <input id="allocDetail_allocId" name="allocId" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_waveNo" name="waveNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_soNo" name="soNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-20">
                                <select id="allocDetail_cdType" name="cdType" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_CD_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <label class="pull-left">状态</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">货主</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left"><font color="red">*</font>商品</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">批次号</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">库位</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <select id="allocDetail_status" name="status" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_SO_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-20">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="allocDetail_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                               displayFieldId="allocDetail_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="allocDetail_ownerSelectId" deleteButtonId="allocDetail_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-20">
                                <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotLoc/data" title="选择商品" cssClass="form-control required"
                                               fieldId="allocDetail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="allocDetail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="allocDetail_skuSelectId" deleteButtonId="allocDetail_skuDeleteId"
                                               fieldLabels="货主编码|货主名称|商品编码|商品名称|批次号|库位|跟踪号|库存数|库存可用数|冻结数|分配数|拣货数|上架待出数|移动待出数|补货待出数|批次属性01|批次属性02|批次属性03|批次属性04|批次属性05|批次属性05|批次属性07|批次属性08|批次属性09|批次属性10|批次属性11|批次属性12"
                                               fieldKeys="ownerCode|ownerName|skuCode|skuName|lotNum|locCode|traceId|qty|qtyAvailable|qtyHold|qtyAlloc|qtyPk|qtyPaOut|qtyMvOut|qtyRpOut|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                               searchLabels="批次号|库位|跟踪号" searchKeys="lotNum|locCode|traceId" inputSearchKey="codeAndName"
                                               queryParams="ownerCode|skuCode" queryParamValues="allocDetail_ownerCode|allocDetail_skuCodeParam"
                                               concatId="allocDetail_lotNum,allocDetail_locCode,allocDetail_traceId,allocDetail_packCode,allocDetail_packDesc,allocDetail_uom,allocDetail_uomDesc,allocDetail_qtyUom,allocDetail_qtyEa,allocDetail_uomQty"
                                               concatName="lotNum,locCode,traceId,packCode,packDesc,printUom,uomDesc,qtyAvailable,qtyAvailable,uomQty">
                                </sys:popSelect>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-20">
                                <div class="input-group" style="width: 100%">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                   displayFieldId="allocDetail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                   selectButtonId="allocDetail_locSelectId" deleteButtonId="allocDetail_locDeleteId"
                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName" disabled="disabled">
                                    </sys:popSelect>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <label class="pull-left">跟踪号</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">包装规格</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left"><font color="red">*</font>包装单位</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left"><font color="red">*</font>数量</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">数量EA</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <div class="input-group" style="width: 100%">
                                    <input id="allocDetail_traceId" name="traceId" htmlEscape="false" class="form-control" readonly>
                                </div>
                            </td>
                            <td class="width-20">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                               fieldId="allocDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                               displayFieldId="allocDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                               selectButtonId="allocDetail_packSelectId" deleteButtonId="allocDetail_packDeleteId"
                                               fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                               searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-20">
                                <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                               fieldId="allocDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                               displayFieldId="allocDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                               selectButtonId="allocDetail_uomSelectId" deleteButtonId="allocDetail_uomDeleteId"
                                               fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                               searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                               queryParams="packCode" queryParamValues="skuDetail_packCode" afterSelect="afterSelectAllocPack">
                                </sys:popSelect>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_qtyUom" name="qtyUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="allocSoChange()">
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_qtyEa" name="qtyEa" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <label class="pull-left"><font color="red">*</font>目标库位</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left"><font color="red">*</font>目标跟踪号</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">拣货单号</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">拣货人</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">拣货时间</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                               displayFieldId="allocDetail_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="allocDetail_toLocSelectId" deleteButtonId="allocDetail_toLocDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-20">
                                <div class="input-group" style="width: 100%">
                                    <input id="allocDetail_toId" name="toId" class="form-control required" type="text" maxlength="32">
                                </div>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_pickNo" name="pickNo" class="form-control" type="text" readonly>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_pickOp" name="pickOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-20">
                                <div class='input-group form_datetime' id='allocDetail_pickTimeF'>
                                    <input id="allocDetail_pickTime" name="pickTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <label class="pull-left">复核人</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">复核时间</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">复核状态</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">打包人</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">打包时间</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <input id="allocDetail_checkOp" name="checkOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-20">
                                <div class='input-group form_datetime' id='allocDetail_checkTimeF'>
                                    <input id="allocDetail_checkTime" name="checkTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                </div>
                            </td>
                            <td class="width-20">
                                <select id="allocDetail_checkStatus" name="checkStatus" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_CHECK_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_packOp" name="packOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-20">
                                <div class='input-group form_datetime' id='allocDetail_packTimeF'>
                                    <input id="allocDetail_packTime" name="packTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <label class="pull-left">发货人</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">发货时间</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">快递单号</label>
                            </td>
                            <td class="width-20">
                                <label class="pull-left">打包箱号</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-20">
                                <input id="allocDetail_shipOp" name="shipOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-20">
                                <div class='input-group form_datetime' id='allocDetail_shipTimeF'>
                                    <input id="allocDetail_shipTime" name="shipTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                </div>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_trackingNo" name="trackingNo" class="form-control" readonly/>
                            </td>
                            <td class="width-20">
                                <input id="allocDetail_caseNo" name="caseNo" class="form-control" readonly/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
        <div id="cancelAllocLog" class="tab-pane fade">
            <div id="cancelAllocLogToolbar" style="width: 100%; padding: 5px 0;">
                <a class="btn btn-primary" id="cancelAlloc_createPaTask" onclick="createPaTaskCancelAlloc()">生成上架任务</a>
            </div>
            <table id="cancelAllocTable" class="table well text-nowrap"></table>
        </div>
    </div>
</div>

<!-- 分派拣货 -->
<div class="modal fade" id="dispatchPKModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 500px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">分派拣货</h4>
            </div>
            <div class="modal-body">
                <form id="dispatchPKForm">
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-20"><label class="pull-left">分库区</label></td>
                        </tr>
                        <tr>
                            <td class="width-20"><input id="isByZone" type="checkbox" class="myCheckbox" name="isByZone" onclick="isByZoneChange(this.checked)"></td>
                        </tr>
                        <tr>
                            <td class="width-20"><label class="pull-left">托盘任务</label></td>
                            <td class="width-40"><label class="pull-left">任务数</label></td>
                            <td class="width-40"><label class="pull-left">浮动比例%</label></td>
                        </tr>
                        <tr>
                            <td class="width-20"><input id="isPlTask" type="checkbox" class="myCheckbox" name="isPlTask" onclick="isPlTaskChange(this.checked)"></td>
                            <td class="width-40"><input id="plLimit" type="text" class="form-control" name="plLimit" readonly></td>
                            <td class="width-40"><input id="plFloat" type="text" class="form-control" name="plFloat" readonly></td>
                        </tr>
                        <tr>
                            <td class="width-20"><label class="pull-left">箱拣任务</label></td>
                            <td class="width-40"><label class="pull-left">任务数</label></td>
                            <td class="width-40"><label class="pull-left">浮动比例%</label></td>
                        </tr>
                        <tr>
                            <td class="width-20"><input id="isCsTask" type="checkbox" class="myCheckbox" name="isCsTask" onclick="isCsTaskChange(this.checked)"></td>
                            <td class="width-40"><input id="csLimit" type="text" class="form-control" name="csLimit" readonly></td>
                            <td class="width-40"><input id="csFloat" type="text" class="form-control" name="csFloat" readonly></td>
                        </tr>
                        <tr>
                            <td class="width-20"><label class="pull-left">件拣任务</label></td>
                            <td class="width-40"><label class="pull-left">任务数</label></td>
                            <td class="width-40"><label class="pull-left">浮动比例%</label></td>
                        </tr>
                        <tr>
                            <td class="width-20"><input id="isEaTask" type="checkbox" class="myCheckbox" name="isEaTask" onclick="isEaTaskChange(this.checked)"></td>
                            <td class="width-40"><input id="eaLimit" type="text" class="form-control" name="eaLimit" readonly></td>
                            <td class="width-40"><input id="eaFloat" type="text" class="form-control" name="eaFloat" readonly></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="dispatchPKConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>

<!-- 生成上架任务 -->
<div class="modal fade" id="createTaskPaModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">生成上架任务</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td class="width-20">
                            <input id="isTaskPa" type="checkBox" class="myCheckbox" onclick="isTaskPaChange(this.checked)">
                        </td>
                        <td class="width-80">
                            <label class="pull-left">是否生成上架任务</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20">
                            <input type="radio" id="allocLoc" name="loc" class="myRadio">
                        </td>
                        <td class="width-80">
                            <label class="pull-left">推荐分配库位</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20">
                            <input type="radio" id="paRuleLoc" name="loc" class="myRadio">
                        </td>
                        <td class="width-80">
                            <label class="pull-left">上架规则计算库位</label>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="createTaskPaConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>

<!-- 打印标签 -->
<div class="modal fade" id="printLabelModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">打印标签</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td class="width-40">
                            <label class="pull-left">商品</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-40">
                            <sys:popSelect url="${ctx}/wms/outbound/banQinWmSoAlloc/skuData" title="选择商品" cssClass="form-control"
                                           fieldId="printLabel_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                           displayFieldId="printLabel_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                           selectButtonId="printLabel_skuSelectId" deleteButtonId="printLabel_skuDeleteId"
                                           fieldLabels="货主编码|货主名称|商品编码|商品名称"
                                           fieldKeys="ownerCode|ownerName|skuCode|skuName"
                                           searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                           queryParams="waveNo" queryParamValues="waveNo">
                            </sys:popSelect>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="printLabelConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>

<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
    <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
</body>
</html>