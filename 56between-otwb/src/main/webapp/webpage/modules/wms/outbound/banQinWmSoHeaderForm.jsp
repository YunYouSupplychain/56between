<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>出库单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmSoHeaderForm.js" %>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="outbound:banQinWmSoHeader:edit">
        <a class="btn btn-primary" id="header_save" onclick="save()">保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:duplicate">
        <a class="btn btn-primary" id="header_duplicate" onclick="duplicate()">复制</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:audit">
        <a class="btn btn-primary" id="header_audit" onclick="audit()">审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelAudit">
        <a class="btn btn-primary" id="header_cancelAudit" onclick="cancelAudit()">取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:generateWave">
        <a class="btn btn-primary" id="header_generateWave" onclick="generateWave()">生成波次</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:alloc">
        <a class="btn btn-primary" id="header_alloc" onclick="alloc()">分配</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:picking">
        <a class="btn btn-primary" id="header_picking" onclick="picking()">拣货确认</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:shipment">
        <a class="btn btn-primary" id="header_shipment" onclick="shipment()">发货确认</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:closeOrder">
        <a class="btn btn-primary" id="header_closeOrder" onclick="closeOrder()">关闭订单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelOrder">
        <a class="btn btn-primary" id="header_cancelOrder" onclick="cancelOrder()">取消订单</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:updateConsigneeInfo">
        <a class="btn btn-primary" id="header_updateConsigneeInfo" onclick="updateConsigneeInfo()">更新收货信息</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="outbound:banQinWmSoHeader:updateCarrierInfo">
        <button id="updateCarrierInfo" class="btn btn-primary" onclick="updateCarrierInfo()">更新承运商信息</button>
    </shiro:hasPermission>
    <div class="btn-group">
        <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown">更多<span class="caret"></span></a>
        <ul class="dropdown-menu">
            <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelAlloc">
                <li><a id="header_cancelAlloc" onclick="cancelAlloc()">取消分配</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelPicking">
                <li><a id="header_cancelPicking" onclick="cancelPicking()">取消拣货</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelShipment">
                <li><a id="header_cancelShipment" onclick="cancelShipment()">取消发货</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmSoHeader:generateLd">
                <li><a id="header_generateLd" onclick="generateLd()">生成装车单</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmSoHeader:ldTransfer">
                <li><a id="header_ldTransfer" onclick="ldTransfer()">装车交接</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmSoHeader:ldCancelTransfer">
                <li><a id="header_ldCancelTransfer" onclick="ldCancelTransfer()">取消交接</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmSoHeader:holdOrder">
                <li><a id="header_holdOrder" onclick="holdOrder()">冻结订单</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelHold">
                <li><a id="header_cancelHold" onclick="cancelHold()">取消冻结</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="outbound:banQinWmSoHeader:intercept">
                <li><a id="header_intercept" onclick="intercept()">拦截订单</a></li>
            </shiro:hasPermission>
        </ul>
    </div>
</div>
<form:form id="inputForm" modelAttribute="banQinWmSoEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="waveNo"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height:250px">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#baseInfo" aria-expanded="true">基础信息</a></li>
            <li class=""><a data-toggle="tab" href="#carrierInfo" aria-expanded="true">承运商</a></li>
            <li class=""><a data-toggle="tab" href="#consigneeInfo" aria-expanded="true">收货人</a></li>
            <li class=""><a data-toggle="tab" href="#settlementInfo" aria-expanded="true">结算人</a></li>
            <li class=""><a data-toggle="tab" href="#reservedInfo" aria-expanded="true">预留信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="baseInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">出库单号</label></td>
                        <td class="width-15">
                            <form:input path="soNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right"><font color="red">*</font>出库单类型</label></td>
                        <td class="width-15">
                            <form:select path="soType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_SO_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_SO_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                                           displayFieldId="ownerCode" displayFieldName="ownerCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmSoEntity.ownerCode}"
                                           selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
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
                                <input id="orderTimeId" name="orderTime" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmSoEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">拦截状态</label></td>
                        <td class="width-15">
                            <form:select path="interceptStatus" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_INTERCEPT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">预计发货时间从</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='fmEta'>
                                <input type='text' name="fmEtd" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmSoEntity.fmEtd}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">预计发货时间到</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='toEta'>
                                <input type='text' name="toEtd" class="form-control"
                                       value="<fmt:formatDate value="${banQinWmSoEntity.toEtd}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">物流单号</label></td>
                        <td class="width-15">
                            <form:input path="logisticNo" htmlEscape="false" class="form-control" maxlength="32"/>
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
                        <td class="width-10"><label class="pull-right">审核人</label></td>
                        <td class="width-15">
                            <form:input path="auditOp" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">审核时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='auditTime'>
                                <input type='text' name="auditTime" class="form-control" readonly
                                       value="<fmt:formatDate value="${banQinWmSoEntity.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">冻结状态</label></td>
                        <td class="width-15">
                            <form:select path="holdStatus" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_ORDER_HOLD_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">打包状态</label></td>
                        <td class="width-15">
                            <form:select path="packStatus" class="form-control required" disabled="true">
                                <form:options items="${fns:getDictList('SYS_WM_PACK_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
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
            <div id="carrierInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">承运商编码</label></td>
                        <td class="width-15">
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
                        <td class="width-10"><label class="pull-right">承运商名称</label></td>
                        <td class="width-15">
                            <form:input path="carrierName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">运输类型</label></td>
                        <td class="width-15">
                            <form:select path="transType" class="form-control m-b" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">路线</label></td>
                        <td class="width-15">
                            <form:input path="line" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">站点</label></td>
                        <td class="width-15">
                            <form:input path="stop" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">车牌号</label></td>
                        <td class="width-15">
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">驾驶员</label></td>
                        <td class="width-15">
                            <form:input path="driver" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">电话</label></td>
                        <td class="width-15">
                            <form:input path="driverTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">装车状态</label></td>
                        <td class="width-15">
                            <form:select path="ldStatus" class="form-control m-b" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_LD_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="consigneeInfo" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货人编码</label></td>
                        <td class="width-15">
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
                        <td class="width-10"><label class="pull-right">收货人名称</label></td>
                        <td class="width-15">
                            <form:input path="consigneeName" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人电话</label></td>
                        <td class="width-15">
                            <form:input path="consigneeTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人传真</label></td>
                        <td class="width-15">
                            <form:input path="consigneeFax" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货人地址</label></td>
                        <td class="width-15" colspan="7">
                            <form:input path="consigneeAddr" htmlEscape="false" class="form-control" maxlength="512"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">联系人名称</label></td>
                        <td class="width-15">
                            <form:input path="contactName" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系人电话</label></td>
                        <td class="width-15">
                            <form:input path="contactTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系人传真</label></td>
                        <td class="width-15">
                            <form:input path="contactFax" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系人Email</label></td>
                        <td class="width-15">
                            <form:input path="contactEmail" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">联系人地址</label></td>
                        <td class="width-15" colspan="7">
                            <form:input path="contactAddr" htmlEscape="false" class="form-control" maxlength="512"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">快递单号</label></td>
                        <td class="width-15">
                            <form:input path="trackingNo" htmlEscape="false" class="form-control" maxlength="32"/>
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
                                           displayFieldId="settleCode" displayFieldName="settleCode" displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${banQinWmSoEntity.settleCode}"
                                           selectButtonId="settleSelectId" deleteButtonId="settleDeleteId"
                                           fieldLabels="结算人编码|结算人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           searchLabels="结算人编码|结算人名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="settleType"
                                           concatId="settleName,settleTel,settleFax,settleIndustryType,settleAddress" concatName="ebcuNameCn,ebcuTel,ebcuFax,ebcuIndustryType,ebcuAddress">
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
                            <form:input path="def1" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">供应链订单号</label></td>
                        <td class="width-15">
                            <form:input path="def2" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">作业任务号</label></td>
                        <td class="width-15">
                            <form:input path="def3" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">客户订单号</label></td>
                        <td class="width-15">
                            <form:input path="def5" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">自定义4</label></td>
                        <td class="width-15">
                            <form:input path="def4" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义6</label></td>
                        <td class="width-15">
                            <form:input path="def6" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义7</label></td>
                        <td class="width-15">
                            <form:input path="def7" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义8</label></td>
                        <td class="width-15">
                            <form:input path="def8" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">自定义9</label></td>
                        <td class="width-15">
                            <form:input path="def9" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义10</label></td>
                        <td class="width-15">
                            <form:input path="def10" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义11</label></td>
                        <td class="width-15">
                            <form:input path="def11" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义12</label></td>
                        <td class="width-15">
                            <form:input path="def12" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">业务订单类型</label></td>
                        <td class="width-15">
                            <form:input path="def13" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义14</label></td>
                        <td class="width-15">
                            <form:input path="def14" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义15</label></td>
                        <td class="width-15">
                            <form:input path="def15" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义16</label></td>
                        <td class="width-15">
                            <form:input path="def16" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">联系人三级地址</label></td>
                        <td class="width-15">
                            <form:input path="def17" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义18</label></td>
                        <td class="width-15">
                            <form:input path="def18" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义19</label></td>
                        <td class="width-15">
                            <form:input path="def19" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">自定义20</label></td>
                        <td class="width-15">
                            <form:input path="def20" htmlEscape="false" class="form-control" maxlength="32"/>
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
        <li class=""><a data-toggle="tab" href="#allocDetailInfo" aria-expanded="true" onclick="detailTabChange(1)">分配明细</a></li>
        <li class=""><a data-toggle="tab" href="#serialInfo" aria-expanded="true">序列号</a></li>
        <li class=""><a data-toggle="tab" href="#cancelAllocLog" aria-expanded="true">取消分配日志</a></li>
    </ul>
    <div class="tab-content">
        <div id="skuDetailInfo" class="tab-pane fade in active">
            <div id="skuDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="outbound:banQinWmSoDetail:add">
                    <a class="btn btn-primary" id="skuDetail_add" onclick="addSkuDetail()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoDetail:save">
                    <a class="btn btn-primary" id="skuDetail_save" onclick="saveSkuDetail()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoDetail:remove">
                    <a class="btn btn-danger" id="skuDetail_remove" onclick="removeSkuDetail()">删除</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoDetail:duplicate">
                    <a class="btn btn-primary" id="skuDetail_duplicate" onclick="duplicateSkuDetail()">复制</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoDetail:alloc">
                    <a class="btn btn-primary" id="skuDetail_alloc" onclick="allocSkuDetail()">分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoDetail:manuaAlloc">
                    <a class="btn btn-primary" id="skuDetail_manuaAlloc" onclick="manuaAllocSkuDetail()">手工分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoDetail:picking">
                    <a class="btn btn-primary" id="skuDetail_picking" onclick="pickingSkuDetail()">拣货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoDetail:cancelAlloc">
                    <a class="btn btn-primary" id="skuDetail_cancelAlloc" onclick="cancelAllocSkuDetail()">取消分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoDetail:cancelPicking">
                    <a class="btn btn-primary" id="skuDetail_cancelPicking" onclick="cancelPickingSkuDetail()">取消拣货</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoDetail:cancel">
                    <a class="btn btn-primary" id="skuDetail_cancel" onclick="cancelSkuDetail()">取消订单行</a>
                </shiro:hasPermission>
            </div>
            <div id="skuDetail_tab-left">
                <table id="wmSoDetailTable" class="table well text-nowrap"></table>
            </div>
            <div id="skuDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="skuDetailForm" method="post" class="form">
                    <input type="hidden" id="skuDetail_id" name="id"/>
                    <input type="hidden" id="skuDetail_orgId" name="orgId"/>
                    <input type="hidden" id="skuDetail_soNo" name="soNo"/>
                    <input type="hidden" id="skuDetail_ownerCode" name="ownerCode"/>
                    <input type="hidden" id="skuDetail_headId" name="headId"/>
                    <input type="hidden" id="skuDetail_logisticNo" name="logisticNo"/>
                    <input type="hidden" id="skuDetail_uomQty" name="uomQty"/>
                    <input type="hidden" id="skuDetail_preallocRule" name="preallocRule"/>
                    <input type="hidden" id="skuDetail_qtyPreallocUom" name="qtyPreallocUom"/>
                    <input type="hidden" id="skuDetail_qtyPreallocEa" name="qtyPreallocEa"/>
                    <div class="tabs-container" style="margin: 0">
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
                                                <c:forEach items="${fns:getDictList('SYS_WM_SO_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_skuCode" displayFieldName="skuCode" displayFieldKeyName="skuCode" displayFieldValue=""
                                                           selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                           fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                           searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                           concatId="skuDetail_skuName,skuDetail_packCode,skuDetail_packDesc,skuDetail_price,skuDetail_uom,skuDetail_uomDesc,skuDetail_rotationRule,skuDetail_rotationRuleName,skuDetail_allocRule,skuDetail_allocRuleName,skuDetail_preallocRule,skuDetail_preallocRuleName,skuDetail_uomQty"
                                                           concatName="skuName,packCode,cdpaFormat,price,rcvUom,rcvUomName,rotationRule,rotationRuleName,allocRule,allocRuleName,preallocRule,preallocRuleName,uomQty"
                                                           queryParams="ownerCode" queryParamValues="ownerCode" afterSelect="afterSelectSku">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_skuName" name="skuName" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">包装规格</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">包装单位</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">订货数</label></td>
                                        <td class="width-25"><label class="pull-left">订货数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                                           fieldId="skuDetail_packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue=""
                                                           displayFieldId="skuDetail_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue=""
                                                           selectButtonId="skuDetail_packSelectId" deleteButtonId="skuDetail_packDeleteId"
                                                           fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat" inputSearchKey="codeAndName"
                                                           searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" disabled="disabled">
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
                                            <input id="skuDetail_qtySoUom" name="qtySoUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="qtySoChange()">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtySoEa" name="qtySoEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">分配数</label></td>
                                        <td class="width-25"><label class="pull-left">分配数EA</label></td>
                                        <td class="width-25"><label class="pull-left">拣货数</label></td>
                                        <td class="width-25"><label class="pull-left">拣货数EA</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyAllocUom" name="qtyAllocUom" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyAllocEa" name="qtyAllocEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyPkUom" name="qtyPkUom" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyPkEa" name="qtyPkEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">发货数</label></td>
                                        <td class="width-25"><label class="pull-left">发货数EA</label></td>
                                        <td class="width-25"><label class="pull-left">单价</label></td>
                                        <td class="width-25"><label class="pull-left asterisk">库存周转规则</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyShipUom" name="qtyShipUom" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_qtyShipEa" name="qtyShipEa" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_price" name="price" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0);">
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleRotationHeader/grid" title="选择库存周转规则" cssClass="form-control required"
                                                           fieldId="skuDetail_rotationRule" fieldName="rotationRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_rotationRuleName" displayFieldName="rotationRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="skuDetail_rotationRuleSelectId" deleteButtonId="skuDetail_rotationRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left asterisk">分配规则</label></td>
                                        <td class="width-25"><label class="pull-left">区域</label></td>
                                        <td class="width-25"><label class="pull-left">拣货区</label></td>
                                        <td class="width-25"><label class="pull-left">拣货位</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleAllocHeader/grid" title="选择分配规则" cssClass="form-control required"
                                                           fieldId="skuDetail_allocRule" fieldName="allocRule" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_allocRuleName" displayFieldName="allocRuleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                                           selectButtonId="skuDetail_allocRuleSelectId" deleteButtonId="skuDetail_allocRuleDeleteId"
                                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhArea/grid" title="选择区域" cssClass="form-control"
                                                           fieldId="skuDetail_areaCode" fieldName="areaCode" fieldKeyName="areaCode" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_areaName" displayFieldName="areaName" displayFieldKeyName="areaName" displayFieldValue=""
                                                           selectButtonId="skuDetail_areaSelectId" deleteButtonId="skuDetail_areaDeleteId"
                                                           fieldLabels="区域编码|区域名称" fieldKeys="areaCode|areaName"
                                                           searchLabels="区域编码|区域名称" searchKeys="areaCode|areaName" inputSearchKey="areaCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="拣货区" cssClass="form-control"
                                                           fieldId="skuDetail_zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue=""
                                                           selectButtonId="skuDetail_zoneSelectId" deleteButtonId="skuDetail_zoneDeleteId"
                                                           fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                                           searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName">
                                            </sys:popSelect>
                                        </td>
                                        <td class="width-25">
                                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择拣货位" cssClass="form-control"
                                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                           displayFieldId="skuDetail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                           selectButtonId="skuDetail_locSelectId" deleteButtonId="skuDetail_locDeleteId"
                                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                            </sys:popSelect>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">跟踪号</label></td>
                                        <td class="width-25"><label class="pull-left">装车状态</label></td>
                                        <td class="width-25"><label class="pull-left">越库类型</label></td>
                                        <td class="width-25"><label class="pull-left">出库时间</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_traceId" name="traceId" htmlEscape="false" class="form-control" maxlength="32">
                                        </td>
                                        <td class="width-25">
                                            <select id="skuDetail_ldStatus" name="ldStatus" class="form-control m-b" disabled>
                                                <c:forEach items="${fns:getDictList('SYS_WM_LD_STATUS')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <select id="skuDetail_cdType" name="cdType" class="form-control m-b" disabled>
                                                <c:forEach items="${fns:getDictList('SYS_WM_CD_TYPE')}" var="dict">
                                                    <option value="${dict.value}">${dict.label}</option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_outboundTime" name="outboundTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="width-25"><label class="pull-left">物流单行号</label></td>
                                        <td class="width-25"><label class="pull-left">销售单号</label></td>
                                        <td class="width-25"><label class="pull-left">销售单行号</label></td>
                                    </tr>
                                    <tr>
                                        <td class="width-25">
                                            <input id="skuDetail_logisticLineNo" name="logisticLineNo" htmlEscape="false" class="form-control" maxlength="4">
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_saleNo" name="saleNo" htmlEscape="false" class="form-control" readonly>
                                        </td>
                                        <td class="width-25">
                                            <input id="skuDetail_saleLineNo" name="saleLineNo" htmlEscape="false" class="form-control" readonly>
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
        <div id="allocDetailInfo" class="tab-pane fade">
            <div id="allocDetailToolbar" style="width: 100%; padding: 5px 0;">
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:add">
                    <a class="btn btn-primary" id="allocDetail_add" onclick="addAllocDetail()">新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:save">
                    <a class="btn btn-primary" id="allocDetail_save" onclick="saveAllocDetail()">保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:picking">
                    <a class="btn btn-primary" id="allocDetail_pick" onclick="pickAllocDetail()">拣货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:manualPicking">
                    <a class="btn btn-primary" id="allocDetail_manualPick" onclick="manualPickAllocDetail()">手工拣货</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:shipment">
                    <a class="btn btn-primary" id="allocDetail_shipment" onclick="shipmentAllocDetail()">发货确认</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:cancelAlloc">
                    <a class="btn btn-primary" id="allocDetail_cancelAlloc" onclick="cancelAllocAllocDetail()">取消分配</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:cancelPick">
                    <a class="btn btn-primary" id="allocDetail_cancelPick" onclick="cancelPickAllocDetail()">取消拣货</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:cancelShipment">
                    <a class="btn btn-primary" id="allocDetail_cancelShipment" onclick="cancelShipmentAllocDetail()">取消发货</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:printWaybill">
                    <a class="btn btn-primary" id="allocDetail_printWaybill" onclick="printWayBill()">打印面单</a>
                </shiro:hasPermission>
            </div>
            <div id="allocDetail_tab-left">
                <table id="wmSoAllocTable" class="table well text-nowrap"></table>
            </div>
            <div id="allocDetail_tab-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                <form:form id="allocDetailForm" method="post" class="form">
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
                    <input type="hidden" id="allocDetail_preallocId" name="preallocId"/>
                    <%--手工分配行商品--%>
                    <input type="hidden" id="allocDetail_skuCodeParam"/>
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td class="width-25"><label class="pull-left">分配Id</label></td>
                            <td class="width-25"><label class="pull-left">波次单号</label></td>
                            <td class="width-25"><label class="pull-left">出库单号</label></td>
                            <td class="width-25"><label class="pull-left">行号</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_allocId" name="allocId" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_waveNo" name="waveNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_soNo" name="soNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_lineNo" name="lineNo" htmlEscape="false" class="form-control" readonly>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">越库类型</label></td>
                            <td class="width-25"><label class="pull-left">状态</label></td>
                            <td class="width-25"><label class="pull-left">货主</label></td>
                            <td class="width-25"><label class="pull-left asterisk">商品</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <select id="allocDetail_cdType" name="cdType" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_CD_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td class="width-25">
                                <select id="allocDetail_status" name="status" class="form-control m-b" disabled>
                                    <c:forEach items="${fns:getDictList('SYS_WM_ALLOC_STATUS')}" var="dict">
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
                                <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotLoc/data" title="选择商品批次库位库存" cssClass="form-control required"
                                               fieldId="allocDetail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="allocDetail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="allocDetail_skuSelectId" deleteButtonId="allocDetail_skuDeleteId"
                                               fieldLabels="货主编码|货主名称|商品编码|商品名称|批次号|库位|跟踪号|库存数|库存可用数|冻结数|分配数|拣货数|上架待出数|移动待出数|补货待出数|批次属性01|批次属性02|批次属性03|批次属性04|批次属性05|批次属性06|批次属性07|批次属性08|批次属性09|批次属性10|批次属性11|批次属性12"
                                               fieldKeys="ownerCode|ownerName|skuCode|skuName|lotNum|locCode|traceId|qty|qtyAvailable|qtyHold|qtyAlloc|qtyPk|qtyPaOut|qtyMvOut|qtyRpOut|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                               searchLabels="批次号|库位|跟踪号|生产日期|失效日期|入库日期|品质|批次属性5|批次属性6|批次属性7|批次属性8|批次属性9|批次属性10|批次属性11|批次属性12" searchKeys="lotNum|locCode|traceId|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12" inputSearchKey="codeAndName"
                                               queryParams="ownerCode|skuCode" queryParamValues="ownerCode|allocDetail_skuCodeParam"
                                               concatId="allocDetail_lotNum,allocDetail_locCode,allocDetail_traceId,allocDetail_packCode,allocDetail_packDesc,allocDetail_uom,allocDetail_uomDesc,allocDetail_qtyUom,allocDetail_qtyEa,allocDetail_uomQty"
                                               concatName="lotNum,locCode,traceId,packCode,packDesc,printUom,uomDesc,qtyAvailable,qtyAvailable,uomQty"
                                               afterSelect="afterSelectAllocSku">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">批次号</label></td>
                            <td class="width-25"><label class="pull-left">库位</label></td>
                            <td class="width-25"><label class="pull-left">跟踪号</label></td>
                            <td class="width-25"><label class="pull-left">包装规格</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_lotNum" name="lotNum" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
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
                            <td class="width-25">
                                <div class="input-group" style="width: 100%">
                                    <input id="allocDetail_traceId" name="traceId" htmlEscape="false" class="form-control" readonly>
                                </div>
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
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left asterisk">包装单位</label></td>
                            <td class="width-25"><label class="pull-left asterisk">数量</label></td>
                            <td class="width-25"><label class="pull-left">数量EA</label></td>
                            <td class="width-25"><label class="pull-left asterisk">目标库位</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                               fieldId="allocDetail_uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                               displayFieldId="allocDetail_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                               selectButtonId="allocDetail_uomSelectId" deleteButtonId="allocDetail_uomDeleteId"
                                               fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                               searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                               queryParams="packCode" queryParamValues="allocDetail_packCode" afterSelect="afterSelectAllocPack">
                                </sys:popSelect>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_qtyUom" name="qtyUom" htmlEscape="false" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="allocSoChange()">
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_qtyEa" name="qtyEa" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
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
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left asterisk">目标跟踪号</label></td>
                            <td class="width-25"><label class="pull-left">拣货单号</label></td>
                            <td class="width-25"><label class="pull-left">拣货人</label></td>
                            <td class="width-25"><label class="pull-left">拣货时间</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <div class="input-group" style="width: 100%">
                                    <input id="allocDetail_toId" name="toId" class="form-control required" type="text" maxlength="32">
                                </div>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_pickNo" name="pickNo" class="form-control" type="text" readonly>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_pickOp" name="pickOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <div class='input-group form_datetime' id='allocDetail_pickTimeF'>
                                    <input id="allocDetail_pickTime" name="pickTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">复核人</label></td>
                            <td class="width-25"><label class="pull-left">复核时间</label></td>
                            <td class="width-25"><label class="pull-left">复核状态</label></td>
                            <td class="width-25"><label class="pull-left">打包箱号</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_checkOp" name="checkOp" htmlEscape="false" class="form-control" readonly>
                            </td>
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
                                <input id="allocDetail_caseNo" name="caseNo" class="form-control" readonly/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">打包人</label></td>
                            <td class="width-25"><label class="pull-left">打包时间</label></td>
                            <td class="width-25"><label class="pull-left">发货人</label></td>
                            <td class="width-25"><label class="pull-left">发货时间</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_packOp" name="packOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <div class='input-group form_datetime' id='allocDetail_packTimeF'>
                                    <input id="allocDetail_packTime" name="packTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-25">
                                <input id="allocDetail_shipOp" name="shipOp" htmlEscape="false" class="form-control" readonly>
                            </td>
                            <td class="width-25">
                                <div class='input-group form_datetime' id='allocDetail_shipTimeF'>
                                    <input id="allocDetail_shipTime" name="shipTime" class="form-control" pattern="yyyy-MM-dd HH:mm:ss" readonly/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-25"><label class="pull-left">快递单号</label></td>
                        </tr>
                        <tr>
                            <td class="width-25">
                                <input id="allocDetail_trackingNo" name="trackingNo" class="form-control" readonly/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form:form>
            </div>
        </div>
        <div id="serialInfo" class="tab-pane fade">
            <table id="serialTable" class="table well text-nowrap"></table>
        </div>
        <div id="cancelAllocLog" class="tab-pane fade">
            <div id="cancelAllocLogToolbar" style="width: 100%; padding: 5px 0;">
                <a class="btn btn-primary" id="cancelAlloc_createPaTask" onclick="createPaTaskCancelAlloc()">生成上架任务</a>
            </div>
            <table id="cancelAllocTable" class="table well text-nowrap"></table>
        </div>
    </div>
</div>

<!-- 波次规则 -->
<div class="modal fade" id="wvRuleModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">波次规则</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td width="30%"><label class="pull-right">波次规则:</label></td>
                        <td width="70%">
                            <input id="wvParams" type="hidden">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleWvHeader/grid" title="选择规则" cssClass="form-control"
                                           fieldId="ruleCode" fieldName="ruleCode" fieldKeyName="ruleCode" fieldValue="" allowInput="true"
                                           displayFieldId="ruleName" displayFieldName="ruleName" displayFieldKeyName="ruleName" displayFieldValue=""
                                           selectButtonId="ruleSelectId" deleteButtonId="ruleDeleteId"
                                           fieldLabels="规则编码|规则名称" fieldKeys="ruleCode|ruleName"
                                           searchLabels="规则编码|规则名称" searchKeys="ruleCode|ruleName" inputSearchKey="ruleCodeAndName">
                            </sys:popSelect>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="wvRuleConfirm()">确认</button>
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
                        <td class="width-25">
                            <input id="isTaskPa" type="checkBox" class="myCheckbox" onclick="isTaskPaChange(this.checked)">
                        </td>
                        <td width="80%">
                            <label class="pull-left">是否生成上架任务</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-25">
                            <input type="radio" id="allocLoc" name="loc" class="myRadio">
                        </td>
                        <td width="80%">
                            <label class="pull-left">推荐分配库位</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-25">
                            <input type="radio" id="paRuleLoc" name="loc" class="myRadio">
                        </td>
                        <td width="80%">
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
<!-- 手工拣货确认 -->
<div class="modal fade" id="manualPickModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">手工拣货</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td width="33%"><label class="pull-left"><font color="red">*</font>包装单位:</label></td>
                        <td width="33%"><label class="pull-left"><font color="red">*</font>拣货库位:</label></td>
                        <td width="33%"><label class="pull-left"><font color="red">*</font>目标库位:</label></td>
                    </tr>
                    <tr>
                        <td width="33%">
                            <sys:popSelect url="${ctx}//wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                           fieldId="pickUom" fieldName="pickUom" fieldKeyName="cdprUnitLevel" fieldValue="" allowInput="true"
                                           displayFieldId="pickUomDesc" displayFieldName="pickUomDesc" displayFieldKeyName="cdprDesc" displayFieldValue=""
                                           selectButtonId="pickUomSelectId" deleteButtonId="pickUomDeleteId"
                                           fieldLabels="包装单位|数量|包装描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                           searchLabels="包装单位|数量|包装描述" searchKeys="cdprUnitLevel|cdprQuantity|cdprDesc" inputSearchKey="codeAndName"
                                           queryParams="packCode" queryParamValues="pickPackCode" afterSelect="afterSelectPickPack">
                            </sys:popSelect>
                        </td>
                        <td width="33%">
                            <sys:popSelect url="${ctx}/wms/inventory/banQinWmInvLotLoc/data" title="选择商品批次库位库存" cssClass="form-control required"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="pickLoc" displayFieldName="pickLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                           selectButtonId="pickLocSelectId" deleteButtonId="pickLocDeleteId"
                                           fieldLabels="货主编码|货主名称|商品编码|商品名称|批次号|库位|跟踪号|库存数|库存可用数|冻结数|分配数|拣货数|上架待出数|移动待出数|补货待出数|批次属性01|批次属性02|批次属性03|批次属性04|批次属性05|批次属性05|批次属性07|批次属性08|批次属性09|批次属性10|批次属性11|批次属性12"
                                           fieldKeys="ownerCode|ownerName|skuCode|skuName|lotNum|locCode|traceId|qty|qtyAvailable|qtyHold|qtyAlloc|qtyPk|qtyPaOut|qtyMvOut|qtyRpOut|lotAtt01|lotAtt02|lotAtt03|lotAtt04|lotAtt05|lotAtt06|lotAtt07|lotAtt08|lotAtt09|lotAtt10|lotAtt11|lotAtt12"
                                           searchLabels="批次号|库位|跟踪号" searchKeys="lotNum|locCode|traceId" inputSearchKey="codeAndName"
                                           queryParams="ownerCode|skuCode" queryParamValues="ownerCode|pickSkuCode"
                                           concatId="pickLotNum,pickTraceId" concatName="lotNum,traceId">
                            </sys:popSelect>
                            <input id="pickSkuCode" type="hidden">
                            <input id="pickLotNum" type="hidden">
                            <input id="pickTraceId" type="hidden">
                            <input id="pickUomQty" type="hidden">
                            <input id="pickPackCode" type="hidden">
                        </td>
                        <td width="33%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                           displayFieldId="pickToLoc" displayFieldName="pickToLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                           selectButtonId="pickToLocSelectId" deleteButtonId="pickToLocDeleteId"
                                           fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                           searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName"
                                           queryParams="locUseType" queryParamValues="locUseType">
                            </sys:popSelect>
                        </td>
                    </tr>
                    <tr>
                        <td width="33%"><label class="pull-left"><font color="red">*</font>拣货数量:</label></td>
                        <td width="33%"><label class="pull-left">数量EA:</label></td>
                        <td width="33%"><label class="pull-left"><font color="red">*</font>目标跟踪号:</label></td>
                    </tr>
                    <tr>
                        <td width="33%"><input id="pickQtyUom" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0);" oninput="pickSoChange()"></td>
                        <td width="33%"><input id="pickQtyEa" class="form-control" readonly></td>
                        <td width="33%"><input id="pickToId" class="form-control"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="manualPickConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
<!-- 更新收货信息 -->
<div class="modal fade" id="updateConsigneeInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">收货信息</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td><label class="pull-left">收货人:</label></td>
                        <td><label class="pull-left">收货人电话:</label></td>
                        <td><label class="pull-left">收货人区域:</label></td>
                    </tr>
                    <tr>
                        <td><input id="consignee_update" class="form-control" maxlength="64"></td>
                        <td><input id="consigneeTel_update" class="form-control" maxlength="32"></td>
                        <td><input id="consigneeArea_update" class="form-control" placeholder="示例：江苏省:苏州市:吴中区" maxlength="64"></td>
                    </tr>
                    <tr>
                        <td><label class="pull-left">收货人地址:</label></td>
                    </tr>
                    <tr>
                        <td colspan="3"><input id="consigneeAddress_update" class="form-control"></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="updateConsigneeInfoConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
<!-- 更新承运商信息 -->
<div class="modal fade" id="updateCarrierInfoModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">承运商信息</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td><label class="pull-left">承运商:</label></td>
                    </tr>
                    <tr>
                        <td>
                            <input id="carrier_carrierType" value="CARRIER" type="hidden">
                            <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                           fieldId="carrier_carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                           displayFieldId="carrier_carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                           selectButtonId="carrier_carrierSelectId" deleteButtonId="carrier_carrierDeleteId"
                                           fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                           queryParams="ebcuType" queryParamValues="carrier_carrierType">
                            </sys:popSelect>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="updateCarrierInfoConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0>
    <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
</body>
</html>