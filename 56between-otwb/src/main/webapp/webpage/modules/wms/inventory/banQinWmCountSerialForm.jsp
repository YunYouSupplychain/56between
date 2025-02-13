<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存序列号盘点管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmCountSerialForm.js" %>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="inventory:banQinWmCountSerial:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountSerial:createCount">
        <a class="btn btn-primary" id="header_createCount" onclick="createCount()">生成普通盘点任务</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountSerial:cancelCountTask">
        <a class="btn btn-primary" id="header_cancelCountTask" onclick="cancelCountTask()">取消盘点任务</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountSerial:closeCount">
        <a class="btn btn-primary" id="header_closeCount" onclick="closeCount()">关闭盘点</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountSerial:cancelCount">
        <a class="btn btn-primary" id="header_cancelCount" onclick="cancelCount()">取消盘点</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="inventory:banQinWmCountSerial:createAd">
        <a class="btn btn-primary" id="header_createAd" onclick="createAd()">生成调整单</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmCountHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="isSerial"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:240px">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
            <li class=""><a data-toggle="tab" href="#conditionInfo" aria-expanded="true">盘点条件</a></li>
            <li class=""><a data-toggle="tab" href="#lotInfo" aria-expanded="true">批次属性</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">盘点单号</label></td>
                        <td class="width-15">
                            <form:input path="countNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control m-b" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">盘点单类型</label></td>
                        <td class="width-15">
                            <form:select path="countType" class="form-control m-b" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">盘点范围</label></td>
                        <td class="width-15">
                            <form:select path="countRange" class="form-control m-b required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_RANGE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">静态/动态盘点</label></td>
                        <td class="width-15">
                            <form:select path="countMethod" class="form-control m-b required">
                                <form:option value=""/>
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">明盘/盲盘</label></td>
                        <td class="width-15">
                            <form:select path="countMode" class="form-control m-b required">
                                <form:option value=""/>
                                <form:options items="${fns:getDictList('SYS_WM_COUNT_MODE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">监盘人</label></td>
                        <td class="width-15">
                            <form:input path="monitorOp" htmlEscape="false" class="form-control " maxlength="32"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="conditionInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">货主</label></td>
                        <td class="width-15">
                            <input id="ownerType" value="OWNER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                           fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${banQinWmCountHeaderEntity.ownerCode}" allowInput="true"
                                           displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinWmCountHeaderEntity.ownerName}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="ownerType" isMultiSelected="true" afterSelect="selectOwner">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">商品</label></td>
                        <td class="width-15">
                            <input id="isSerial" value="Y" type="hidden">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                           fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="${banQinWmCountHeaderEntity.skuCode}" allowInput="true"
                                           displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue="${banQinWmCountHeaderEntity.skuName}"
                                           selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                           fieldLabels="货主编码|货主名称|商品编码|商品名称" fieldKeys="ownerCode|ownerName|skuCode|skuName"
                                           searchLabels="货主编码|商品编码|商品名称" searchKeys="ownerCode|skuCode|skuName" inputSearchKey="skuCodeAndName"
                                           queryParams="ownerCodes|isSerial" queryParamValues="ownerCode|isSerial" isMultiSelected="true" afterSelect="selectSku">
                            </sys:popSelect>
                        </td>
                        <td class="width-10"><label class="pull-right">批次号</label></td>
                        <td class="width-15">
                            <form:input path="lotNum" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"></td>
                        <td class="width-15">
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="lotInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">生产日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='lotAtt01'>
                                <input name="lotAtt01" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmCountHeaderEntity.lotAtt01}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">失效日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='lotAtt02'>
                                <input name="lotAtt02" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmCountHeaderEntity.lotAtt02}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">入库日期</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='lotAtt03'>
                                <input name="lotAtt03" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmCountHeaderEntity.lotAtt03}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">品质</label></td>
                        <td class="width-15">
                            <form:select path="lotAtt04" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_QC_ATT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">批次属性05</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt05" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性06</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt06" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性07</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt07" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性08</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt08" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">批次属性09</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt09" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性10</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt10" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性11</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt11" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">批次属性12</label></td>
                        <td class="width-15">
                            <form:input path="lotAtt12" htmlEscape="false" class="form-control" maxlength="32"/>
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
        <li class="active"><a data-toggle="tab" href="#commonCount" aria-expanded="true" onclick="detailTabChange(0)">普通盘点</a></li>
        <li class=""><a data-toggle="tab" href="#serialCount" aria-expanded="true" onclick="detailTabChange(1)">序列号盘点</a></li>
    </ul>
    <div class="tab-content">
        <div id="commonCount" class="tab-pane fade in active">
            <div id="detailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="inventory:banQinWmTaskCountSerial:confirmSerialCount">
                    <a class="btn btn-primary" id="detail_confirm" onclick="confirmDetail()">盘点确认</a>
                </shiro:hasPermission>
            </div>
            <div id="tab1-left">
                <table id="wmTaskCountTable" class="table text-nowrap"></table>
            </div>
            <div id="tab1-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="inputForm1" method="post" class="form">
                    <input type="hidden" id="detail_id" name="id"/>
                    <input type="hidden" id="detail_orgId" name="orgId"/>
                    <input type="hidden" id="detail_countNo" name="countNo"/>
                    <input type="hidden" id="detail_headerId" name="headerId"/>
                    <input type="hidden" id="detail_countOp" name="countOp"/>
                    <input type="hidden" id="detail_countMethod" name="countMethod"/>
                    <input type="hidden" id="detail_countMode" name="countMode"/>
                    <input type="hidden" id="detail_packCode" name="packCode"/>
                    <input type="hidden" id="detail_packDesc" name="packDesc"/>
                    <input type="hidden" id="detail_uom" name="uom"/>
                    <input type="hidden" id="detail_dataSource" name="dataSource"/>
                    <input type="hidden" id="detail_uomDesc" name="uomDesc"/>
                    <input type="hidden" id="detail_qtyCountUom" name="qtyCountUom"/>
                    <input type="hidden" id="detail_uomQuantity" name="uomQuantity"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">盘点任务Id</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">盘点状态</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">货主</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">商品</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="detail_countId" name="countId" htmlEscape="false" class="form-control" readonly="readonly"/>
                            </td>
                            <td class="width-25">
                                <select id="detail_status" name="status" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_TASK_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                               fieldId="detail_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="detail_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="detail_ownerSelectId" deleteButtonId="detail_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerType">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control required"
                                               fieldId="detail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="skuCode" allowInput="true"
                                               displayFieldId="detail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="detail_skuSelectId" deleteButtonId="detail_skuDeleteId"
                                               fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                               concatId="detail_skuName,detail_packCode,detail_packDesc,detail_uom,detail_cdprDesc,detail_cdprQuantity"
                                               concatName="skuName,packCode,cdpaFormat,rcvUom,rcvUomName,uomQty"
                                               queryParams="ownerCode" queryParamValues="detail_ownerCode" afterSelect="afterSelectSku">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">批次号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">库位</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">跟踪号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">库存数EA</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="detail_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly="readonly"/>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="detail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="detail_locSelectId" deleteButtonId="detail_locDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="detail_traceId" name="traceId" htmlEscape="false" class="form-control required" maxlength="32"/>
                            </td>
                            <td class="width-25">
                                <input id="detail_qty" name="qty" htmlEscape="false" class="form-control" readonly/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left asterisk">盘点数EA</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">损益数EA</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="detail_qtyCountEa" name="qtyCountEa" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                            </td>
                            <td class="width-25">
                                <input id="detail_qtyDiff" name="qtyDiff" htmlEscape="false" class="form-control" readonly/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <table id="detailLotAttTab" class="bq-table"></table>
                </form:form>
            </div>
        </div>
        <div id="serialCount" class="tab-pane fade in">
            <div id="serialToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="inventory:banQinWmTaskCountSerial:comfirmSerial">
                    <a class="btn btn-primary" id="serial_comfirmSerial" onclick="comfirmSerHandler()">盘点确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmTaskCountSerial:comfirmLoss">
                    <a class="btn btn-primary" id="serial_comfirmLoss" onclick="comfirmLossHandler()">盘亏确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmTaskCountSerial:deleteSerial">
                    <a class="btn btn-primary" id="serial_deleteSerial" onclick="deleteSerHandler()">取消盘点确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmTaskCountSerial:addSerial">
                    <a class="btn btn-primary" id="serial_addSerial" onclick="addSerHandler()">新增</a>
                </shiro:hasPermission>
            </div>
            <div id="tab2-left" class="div-left">
                <table id="wmTaskCountSerialTable" class="table text-nowrap"></table>
            </div>
            <div id="tab2-right" class="div-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="inputForm2" method="post" class="form">
                    <input type="hidden" id="serial_id" name="id"/>
                    <input type="hidden" id="serial_orgId" name="orgId"/>
                    <input type="hidden" id="serial_countNo" name="countNo"/>
                    <input type="hidden" id="serial_headerId" name="headerId"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">盘点状态</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left">盘点结果</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">货主</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">商品</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <select id="serial_status" name="status" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_TASK_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <select id="serial_countResult" name="countResult" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_COUNT_RESULT')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control required"
                                               fieldId="serial_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="serial_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="serial_ownerSelectId" deleteButtonId="serial_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerType" afterSelect="afterSelectOwner">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control required"
                                               fieldId="serial_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="skuCode" allowInput="true"
                                               displayFieldId="serial_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="serial_skuSelectId" deleteButtonId="serial_skuDeleteId"
                                               fieldLabels="货主编码|货主名称|商品编码|商品名称" fieldKeys="ownerCode|ownerName|skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                               concatId="serial_skuName,serial_ownerCode,serial_ownerName" concatName="skuName,ownerCode,ownerName"
                                               queryParams="ownerCode|isSerial" queryParamValues="serial_ownerCode|isSerial" afterSelect="afterSelectSku">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <label class="pull-left">批次号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">库位</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">跟踪号</label>
                            </td>
                            <td class="width-25">
                                <label class="pull-left asterisk">序列号</label>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotAtt/data" title="选择批次" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="serial_lotNum" displayFieldName="lotNum" displayFieldKeyName="lotNum" displayFieldValue=""
                                               selectButtonId="serial_lotNumSelectId" deleteButtonId="serial_lotNumDeleteId"
                                               fieldLabels="批次号|商品编码|商品名称|货主编码|商品名称|生产日期|失效日期|入库日期|品质|批次属性5|批次属性6|批次属性7|批次属性8|批次属性9|批次属性10|批次属性11|批次属性12"
                                               fieldKeys="lotNum|skuCode|skuName|ownerCode|ownerName|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                               searchLabels="批次号" searchKeys="lotNum" inputSearchKey="codeAndName"
                                               concatId="detail_skuName,serial_ownerCode,serial_ownerName" concatName="skuName,ownerCode,ownerName"
                                               queryParams="ownerCode|skuCode" queryParamValues="serial_ownerCode|serial_skuCode">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="serial_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="serial_locSelectId" deleteButtonId="serial_locDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="serial_traceId" name="traceId" htmlEscape="false" class="form-control required" maxlength="32"/>
                            </td>
                            <td class="width-25">
                                <input id="serial_serialNo" name="serialNo" htmlEscape="false" class="form-control"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <table id="serialLotAttTab" class="bq-table"></table>
                </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>