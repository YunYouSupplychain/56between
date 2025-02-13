<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>波次管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmPickHeaderForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="banQinWmPickHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table table-bordered">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">拣货单号</label></td>
                <td class="width-15">
                    <form:input path="pickNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"></td>
                <td class="width-15"></td>
                <td class="width-10"></td>
                <td class="width-15"></td>
                <td class="width-10"></td>
                <td class="width-15"></td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<div class="tabs-container">
    <ul id="detailTab" class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#allocDetailInfo" aria-expanded="true" onclick="detailTabChange(0)">分配明细</a></li>
    </ul>
    <div class="tab-content">
        <div id="allocDetailInfo" class="tab-pane fade in active">
            <div id="allocDetailToolbar" style="width: 100%; padding: 10px 0;">
                <shiro:hasPermission name="outbound:banQinWmPickDetail:allocPick">
                    <a class="btn btn-primary" id="alloc_pick" onclick="allocPick()">拣货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmPickDetail:allocShipment">
                    <a class="btn btn-primary" id="alloc_shipment" onclick="allocShipment()">发货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmPickDetail:allocCancelAlloc">
                    <a class="btn btn-primary" id="alloc_cancelAlloc" onclick="allocCancelAlloc()">取消分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmPickDetail:allocCancelPick">
                    <a class="btn btn-primary" id="alloc_cancelPick" onclick="allocCancelPick()">取消拣货</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmPickDetail:allocCancelShipment">
                    <a class="btn btn-primary" id="alloc_cancelShipment" onclick="allocCancelShipment()">取消发货</a>
                </shiro:hasPermission>
            </div>
            <div id="allocDetail_tab-left">
                <table id="wmPickAllocTable" class="table text-nowrap"></table>
            </div>
            <div id="allocDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="allocDetailForm" method="post" class="form">
                    <input type="hidden" id="allocDetail_id" name="id"/>
                    <input type="hidden" id="allocDetail_orgId" name="orgId"/>
                    <input type="hidden" id="allocDetail_qtyPkEa" name="qtyPkEa"/>
                    <input type="hidden" id="allocDetail_qtyPkUom" name="qtyPkUom"/>
                    <input type="hidden" id="allocDetail_uomQty" name="uomQty"/>
                    <input type="hidden" id="allocDetail_remarks" name="remarks"/>
                    <input type="hidden" id="allocDetail_skuCodeParam"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">分配Id</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">波次单号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">出库单号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">行号</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_allocId" name="allocId" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_waveNo" name="waveNo" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_soNo" name="soNo" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_lineNo" name="lineNo" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">状态</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">货主</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">商品</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">批次号</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <select id="allocDetail_status" name="status" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_SO_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="allocDetail_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                               displayFieldId="allocDetail_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="allocDetail_ownerSelectId" deleteButtonId="allocDetail_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotLoc/data" title="选择商品" cssClass="form-control required"
                                               fieldId="allocDetail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                               displayFieldId="allocDetail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="allocDetail_skuSelectId" deleteButtonId="allocDetail_skuDeleteId"
                                               fieldLabels="货主编码|货主名称|商品编码|商品名称|批次号|库位|跟踪号|库存数|库存可用数|冻结数|分配数|拣货数|上架待出数|移动待出数|补货待出数|批次属性01|批次属性02|批次属性03|批次属性04|批次属性05|批次属性05|批次属性07|批次属性08|批次属性09|批次属性10|批次属性11|批次属性12"
                                               fieldKeys="ownerCode|ownerName|skuCode|skuName|lotNum|locCode|traceId|qty|qtyAvailable|qtyHold|qtyAlloc|qtyPk|qtyPaOut|qtyMvOut|qtyRpOut|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                               searchLabels="批次号|库位|跟踪号" searchKeys="lotNum|locCode|traceId" inputSearchKey="codeAndName"
                                               queryParams="ownerCode|skuCode" queryParamValues="allocDetail_ownerCode|allocDetail_skuCodeParam"
                                               concatId="allocDetail_lotNum,allocDetail_locCode,allocDetail_traceId,allocDetail_packCode,allocDetail_packDesc,allocDetail_uom,allocDetail_uomDesc,allocDetail_qtyUom,allocDetail_qtyEa,allocDetail_uomQty"
                                               concatName="lotNum,locCode,traceId,packCode,packDesc,printUom,uomDesc,qtyAvailable,qtyAvailable,uomQty"
                                               disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_lotNum" name="lotNum" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">库位</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">跟踪号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">包装规格</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">包装单位</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                               displayFieldId="allocDetail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="allocDetail_locSelectId" deleteButtonId="allocDetail_locDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_traceId" name="traceId" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                               fieldId="allocDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                               displayFieldId="allocDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                               selectButtonId="allocDetail_packSelectId" deleteButtonId="allocDetail_packDeleteId"
                                               fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                               searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                               fieldId="allocDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                               displayFieldId="allocDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                               selectButtonId="allocDetail_uomSelectId" deleteButtonId="allocDetail_uomDeleteId"
                                               fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                               searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                               queryParams="packCode" queryParamValues="skuDetail_packCode" afterSelect="afterSelectAllocPack">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left asterisk">数量</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">数量EA</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">目标库位</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">目标跟踪号</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_qtyUom" name="qtyUom" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);"
                                       oninput="allocSoChange()">
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_qtyEa" name="qtyEa" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                               displayFieldId="allocDetail_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="allocDetail_toLocSelectId" deleteButtonId="allocDetail_toLocDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_toId" name="toId" class="form-control required" type="text" maxlength="32">
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">拣货单号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">拣货人</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">拣货时间</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">复核人</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_pickNo" name="pickNo" class="form-control" type="text" readonly>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_pickOp" name="pickOp" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <div class='input-group form_datetime' id='allocDetail_pickTimeF'>
                                    <input id="allocDetail_pickTime" name="pickTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_checkOp" name="checkOp" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">复核时间</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">复核状态</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">打包人</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">打包时间</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <div class='input-group form_datetime' id='allocDetail_checkTimeF'>
                                    <input id="allocDetail_checkTime" name="checkTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-25">
                                <select id="allocDetail_checkStatus" name="checkStatus" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_CHECK_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_packOp" name="packOp" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <div class='input-group form_datetime' id='allocDetail_packTimeF'>
                                    <input id="allocDetail_packTime" name="packTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">发货人</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">发货时间</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">越库类型</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">快递单号</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_shipOp" name="shipOp" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <div class='input-group form_datetime' id='allocDetail_shipTimeF'>
                                    <input id="allocDetail_shipTime" name="shipTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-25">
                                <select id="allocDetail_cdType" name="cdType" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_CD_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_trackingNo" name="trackingNo" class="form-control" readonly/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">打包箱号</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_caseNo" name="caseNo" class="form-control" readonly/>
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