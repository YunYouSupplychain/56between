<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>订单校验管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmOrderCheckList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">订单校验列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmSoDetailEntity" class="form form-horizontal well clearfix">
                            <table class="table table-striped table-bordered table-condensed">
                                <tbody>
                                <tr>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="波次单号：">波次单号：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:input path="waveNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="出库单号：">出库单号：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:input path="soNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="货主：">货主：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <input id="ownerType" value="OWNER" type="hidden">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;"><label class="label-item single-overflow pull-right">商品：</label></td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                       fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                       displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                       selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="收货人：">收货人：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <input id="consigneeType" value="CONSIGNEE" type="hidden">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择收货人" cssClass="form-control"
                                                       fieldId="consigneeCode" fieldName="consigneeCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="consigneeName" displayFieldName="consigneeName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="consigneeSelectId" deleteButtonId="consigneeDeleteId"
                                                       fieldLabels="收货人编码|收货人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="收货人编码|收货人名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="consigneeType">
                                        </sys:popSelect>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="订单时间从：">&nbsp;订单时间从：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <div class='input-group date' id='beginOrderTime'>
                                            <input type='text' name="beginOrderTime" class="form-control"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="订单时间到：">&nbsp;订单时间到：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <div class='input-group date' id='endOrderTime'>
                                            <input type='text' name="endOrderTime" class="form-control"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="状态：">状态：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_SO_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="outbound:banQinWmOrderCheck:alloc">
                    <button id="alloc" class="btn btn-primary" disabled onclick="alloc()">分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmOrderCheck:allocAvg">
                    <button id="allocAvg" class="btn btn-primary" disabled onclick="allocAvg()">平均分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmOrderCheck:allocByCon">
                    <button id="allocByCon" class="btn btn-primary" disabled onclick="allocByCon()">按条件分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmOrderCheck:manuaAlloc">
                    <button id="manuaAlloc" class="btn btn-primary" disabled onclick="manuaAlloc()">手工分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmOrderCheck:picking">
                    <button id="picking" class="btn btn-primary" disabled onclick="picking()">拣货确认</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">更多<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="outbound:banQinWmOrderCheck:cancelAlloc">
                            <li><a id="cancelAlloc" onclick="cancelAlloc()">取消分配</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmOrderCheck:cancelPicking">
                            <li><a id="cancelPicking" onclick="cancelPicking()">取消拣货</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
               <%-- <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                    </ul>
                </div>--%>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmOrderCheckTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 手工分配 -->
<div class="modal fade" id="manuaAllocInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 80%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">分配信息</h4>
            </div>
            <div class="modal-body">
                <form:form id="allocDetailForm" modelAttribute="banQinWmSoDetailEntity" method="post" class="form-horizontal">
                    <input type="hidden" id="allocDetail_id" name="id"/>
                    <input type="hidden" id="allocDetail_orgId" name="orgId"/>
                    <input type="hidden" id="allocDetail_qtyPkEa" name="qtyPkEa"/>
                    <input type="hidden" id="allocDetail_qtyPkUom" name="qtyPkUom"/>
                    <input type="hidden" id="allocDetail_uomQty" name="uomQty"/>
                    <input type="hidden" id="allocDetail_consigneeCode" name="consigneeCode"/>
                    <input type="hidden" id="allocDetail_packWeight" name="packWeight"/>
                    <input type="hidden" id="allocDetail_asnNo" name="asnNo"/>
                    <input type="hidden" id="allocDetail_asnLineNo" name="asnLineNo"/>
                    <input type="hidden" id="allocDetail_rcvLineNo" name="rcvLineNo"/>
                    <input type="hidden" id="allocDetail_cdOutStep" name="cdOutStep"/>
                    <input type="hidden" id="allocDetail_remarks" name="remarks"/>
                    <input type="hidden" id="allocDetail_packStatus" name="packStatus"/>
                    <input type="hidden" id="allocDetail_trackingNo" name="trackingNo"/>
                        <%--手工分配行商品--%>
                    <input type="hidden" id="allocDetail_skuCodeParam"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td width="20%">
                                <label class="pull-left">分配Id：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">波次单号：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">出库单号：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">行号：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">预配ID：</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <input id="allocDetail_allocId" name="allocId" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_waveNo" name="waveNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_soNo" name="soNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_preallocId" name="preallocId" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <label class="pull-left">状态：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">货主：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>商品：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">批次号：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">库位：</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <select id="allocDetail_status" name="status" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_ALLOC_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="20%">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="allocDetail_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                               displayFieldId="allocDetail_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="allocDetail_ownerSelectId" deleteButtonId="allocDetail_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td width="20%">
                                <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotLoc/data" title="选择商品批次库位库存" cssClass="form-control required"
                                               fieldId="allocDetail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="allocDetail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="allocDetail_skuSelectId" deleteButtonId="allocDetail_skuDeleteId"
                                               fieldLabels="货主编码|货主名称|商品编码|商品名称|批次号|库位|跟踪号|库存数|库存可用数|冻结数|分配数|拣货数|上架待出数|移动待出数|补货待出数|批次属性01|批次属性02|批次属性03|批次属性04|批次属性05|批次属性06|批次属性07|批次属性08|批次属性09|批次属性10|批次属性11|批次属性12"
                                               fieldKeys="ownerCode|ownerName|skuCode|skuName|lotNum|locCode|traceId|qty|qtyAvailable|qtyHold|qtyAlloc|qtyPk|qtyPaOut|qtyMvOut|qtyRpOut|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                               searchLabels="批次号|库位|跟踪号|生产日期|失效日期|入库日期|品质|批次属性5|批次属性6|批次属性7|批次属性8|批次属性9|批次属性10|批次属性11|批次属性12" searchKeys="lotNum|locCode|traceId|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12" inputSearchKey="codeAndName"
                                               queryParams="ownerCode|skuCode" queryParamValues="ownerCode|allocDetail_skuCodeParam"
                                               concatId="allocDetail_lotNum,allocDetail_locCode,allocDetail_traceId,allocDetail_packCode,allocDetail_packDesc,allocDetail_uom,allocDetail_uomDesc,allocDetail_qtyUom,allocDetail_qtyEa,allocDetail_uomQty"
                                               concatName="lotNum,locCode,traceId,packCode,packDesc,printUom,uomDesc,qtyAvailable,qtyAvailable,uomQty">
                                </sys:popSelect>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
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
                            <td width="20%">
                                <label class="pull-left">跟踪号：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">包装规格：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>包装单位：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>目标库位：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>目标跟踪号：</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <div class="input-group" style="width: 100%">
                                    <input id="allocDetail_traceId" name="traceId" htmlEscape="false" class="form-control" readonly>
                                </div>
                            </td>
                            <td width="20%">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                               fieldId="allocDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                               displayFieldId="allocDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                               selectButtonId="allocDetail_packSelectId" deleteButtonId="allocDetail_packDeleteId"
                                               fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                               searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName" disabled="disabled">
                                </sys:popSelect>
                            </td>
                            <td width="20%">
                                <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                               fieldId="allocDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                               displayFieldId="allocDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                               selectButtonId="allocDetail_uomSelectId" deleteButtonId="allocDetail_uomDeleteId"
                                               fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                               searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                               queryParams="packCode" queryParamValues="allocDetail_packCode" afterSelect="afterSelectAllocPack">
                                </sys:popSelect>
                            </td>
                            <td width="20%">
                                <input id="locUseType" value="SS" type="hidden">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                               fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                               displayFieldId="allocDetail_toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                               selectButtonId="allocDetail_toLocSelectId" deleteButtonId="allocDetail_toLocDeleteId"
                                               fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                               searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName"
                                               queryParams="locUseType" queryParamValues="locUseType">
                                </sys:popSelect>
                            </td>
                            <td width="20%">
                                <div class="input-group" style="width: 100%">
                                    <input id="allocDetail_toId" name="toId" class="form-control required" type="text" maxlength="32">
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <label class="pull-left">待分配数量：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left"><font color="red">*</font>数量：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">数量EA：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">拣货人：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">拣货时间：</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <input id="allocDetail_reAllocQty" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_qtyUom" name="qtyUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="allocSoChange()">
                            </td>
                            <td width="20%">
                                <input id="allocDetail_qtyEa" name="qtyEa" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_pickOp" name="pickOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <div class='input-group form_datetime' id='allocDetail_pickTimeF'>
                                    <input id="allocDetail_pickTime" name="pickTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <label class="pull-left">复核人：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">复核时间：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">复核状态：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">打包人：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">打包时间：</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <input id="allocDetail_checkOp" name="checkOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <div class='input-group form_datetime' id='allocDetail_checkTimeF'>
                                    <input id="allocDetail_checkTime" name="checkTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                </div>
                            </td>
                            <td width="20%">
                                <select id="allocDetail_checkStatus" name="checkStatus" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_CHECK_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_packOp" name="packOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <div class='input-group form_datetime' id='allocDetail_packTimeF'>
                                    <input id="allocDetail_packTime" name="packTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <label class="pull-left">发货人：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">发货时间：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">越库类型：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">打包箱号：</label>
                            </td>
                            <td width="20%">
                                <label class="pull-left">拣货单号：</label>
                            </td>
                        </tr>
                        <tr>
                            <td width="20%">
                                <input id="allocDetail_shipOp" name="shipOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td width="20%">
                                <div class='input-group form_datetime' id='allocDetail_shipTimeF'>
                                    <input id="allocDetail_shipTime" name="shipTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                </div>
                            </td>
                            <td width="20%">
                                <select id="allocDetail_cdType" name="cdType" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_CD_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_caseNo" name="caseNo" class="form-control" readonly/>
                            </td>
                            <td width="20%">
                                <input id="allocDetail_pickNo" name="pickNo" class="form-control" type="text" readonly>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="allocConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>