<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>出库单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmSoHeaderList.js" %>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
    <input id="carrierType" value="CARRIER" type="hidden">
    <input id="consigneeType" value="CONSIGNEE" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">出库单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmSoEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden"/>
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">出库单号</label></td>
                                    <td class="width-15">
                                        <form:input path="soNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">物流单号</label></td>
                                    <td class="width-15">
                                        <form:input path="logisticNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">出库单类型</label></td>
                                    <td class="width-15">
                                        <form:select path="soType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_SO_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="def5" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">订单时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='beginOrderTime'>
                                            <input type='text' name="beginOrderTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='endOrderTime'>
                                            <input type='text' name="endOrderTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_SO_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">审核状态</label></td>
                                    <td class="width-15">
                                        <form:select path="auditStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">货主</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择承运商" cssClass="form-control"
                                                       fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                                       fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="carrierType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">收货人</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择收货人" cssClass="form-control"
                                                       fieldId="consigneeCode" fieldName="consigneeCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="consigneeName" displayFieldName="consigneeName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="consigneeSelectId" deleteButtonId="consigneeDeleteId"
                                                       fieldLabels="收货人编码|收货人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="收货人编码|收货人名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="consigneeType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">收货联系人</label></td>
                                    <td class="width-15">
                                        <form:input path="contactName" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">波次号</label></td>
                                    <td class="width-15">
                                        <form:input path="waveNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">冻结状态</label></td>
                                    <td class="width-15">
                                        <form:select path="holdStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_ORDER_HOLD_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">装车状态</label></td>
                                    <td class="width-15">
                                        <form:select path="ldStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_LD_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">是否已下发运输</label></td>
                                    <td class="width-15">
                                        <form:select path="isPushed" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">创建时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='createDateFm'>
                                            <input type='text' name="createDateFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">创建时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='createDateTo'>
                                            <input type='text' name="createDateTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
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
                <shiro:hasPermission name="outbound:banQinWmSoHeader:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:duplicate">
                    <button id="duplicate" class="btn btn-primary" disabled onclick="duplicate()">复制</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()">审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()">取消审核</button>
                </shiro:hasPermission>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">波次生成<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:generateWave">
                            <li><a id="generateWave" onclick="generateWave()">生成波次</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:createWaveByGroup">
                            <li><a id="createWaveByGroup" onclick="createWaveByGroup()">按规则组生成波次</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:alloc">
                    <button id="alloc" class="btn btn-primary" disabled onclick="alloc()">分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:picking">
                    <button id="picking" class="btn btn-primary" disabled onclick="picking()">拣货确认</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:shipment">
                    <button id="shipment" class="btn btn-primary" disabled onclick="shipment()">发货确认</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:closeOrder">
                    <button id="closeOrder" class="btn btn-primary" disabled onclick="closeOrder()">关闭订单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelOrder">
                    <button id="cancelOrder" class="btn btn-primary" disabled onclick="cancelOrder()">取消订单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:import">
                    <button id="importOrder" class="btn btn-info" onclick="importOrder()"><i class="fa fa-file-excel-o"></i>导入</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoHeader:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i>导出</button>
                </shiro:hasPermission>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">更多<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelAlloc">
                            <li><a id="cancelAlloc" onclick="cancelAlloc()">取消分配</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelPicking">
                            <li><a id="cancelPicking" onclick="cancelPicking()">取消拣货</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelShipment">
                            <li><a id="cancelShipment" onclick="cancelShipment()">取消发货</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:generateLd">
                            <li><a id="generateLd" onclick="generateLd()">生成装车单</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:ldTransfer">
                            <li><a id="ldTransfer" onclick="ldTransfer()">装车交接</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:ldCancelTransfer">
                            <li><a id="ldCancelTransfer" onclick="ldCancelTransfer()">取消交接</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:holdOrder">
                            <li><a id="holdOrder" onclick="holdOrder()">冻结订单</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelHold">
                            <li><a id="cancelHold" onclick="cancelHold()">取消冻结</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:intercept">
                            <li><a id="intercept" onclick="intercept()">拦截订单</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:updateConsigneeInfo">
                        <li><a id="updateConsigneeInfo" onclick="updateConsigneeInfo()">更新收货信息</a>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="outbound:banQinWmSoHeader:updateCarrierInfo">
                        <li><a id="updateCarrierInfo" onclick="updateCarrierInfo()">更新承运商信息</a>
                            </shiro:hasPermission>
                    </ul>
                </div>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">下发运输<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:pushToTms">
                            <li><a id="pushToTms" onclick="pushToTms()">下发运输</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:pushToTmsBatch">
                            <li><a id="pushToTmsBatch" onclick="pushToTmsBatch()">批量下发运输</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:cancelPushToTms">
                            <li><a id="cancelPushToTms" onclick="cancelPushToTms()">取消下发运输</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:printPickingOrderLandscape">
                            <li><a id="printPickingOrder" onclick="printPickingOrder()">打印拣货单</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:printPickingOrderLandscape">
                            <li><a id="printPickingOrderLandscape" onclick="printPickingOrderLandscape()">打印拣货单（横版）</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="outbound:banQinWmSoHeader:printShipHandoverOrder">
                            <li><a id="printShipHandoverOrder" onclick="printShipHandoverOrder()">打印出库确认交接单</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmSoHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
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
                        <input id="orderId" type="hidden">
                    </tr>
                    <tr>
                        <td><label class="pull-left">收货人地址:</label></td>
                    </tr>
                    <tr>
                        <td colspan="3"><input id="consigneeAddress_update" class="form-control" maxlength="412"></td>
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
<!-- 波次规则组 -->
<div class="modal fade" id="wvRuleGroupModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1000px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">按波次规则组生成</h4>
            </div>
            <div class="modal-body">
                <form id="waveGroupForm">
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td><label class="pull-left">波次规则编码<font color="red">*</font></label></td>
                            <td><label class="pull-left">波次规则名称</label></td>
                            <td><label class="pull-left">订单时间从<font color="red">*</font></label></td>
                            <td><label class="pull-left">订单时间到<font color="red">*</font></label></td>
                        </tr>
                        <tr>
                            <td>
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRuleWvGroupHeader/grid" title="选择规则" cssClass="form-control required"
                                               fieldId="groupCode" fieldName="groupCode" fieldKeyName="groupCode" fieldValue="" allowInput="true"
                                               displayFieldId="groupName" displayFieldName="groupName" displayFieldKeyName="groupName" displayFieldValue=""
                                               selectButtonId="ruleGroupSelectId" deleteButtonId="ruleGroupDeleteId"
                                               fieldLabels="规则组编码|规则组名称" fieldKeys="groupCode|groupName"
                                               concatId="waveGroupName,ownerCodeC,ownerNameC,skuCodeC,skuNameC,addrArea"
                                               concatName="groupName,ownerCode,ownerName,skuCode,skuName,addrArea"
                                               searchLabels="规则组编码|规则组名称" searchKeys="groupCode|groupName" inputSearchKey="ruleCodeAndName" afterSelect="afterSelectGroup">
                                </sys:popSelect>
                            </td>
                            <td>
                                <input id="waveGroupName" class="form-control" readonly>
                            </td>
                            <td>
                                <div class='input-group form_datetime' id='orderDateFmC'>
                                    <input id="orderDateFm" name="orderDateFm" class="form-control required"/>
                                    <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                                </div>
                            </td>
                            <td>
                                <div class='input-group form_datetime' id='orderDateToC'>
                                    <input id="orderDateTo" name="orderDateTo" class="form-control required"/>
                                    <span class="input-group-addon">
                                    <span class="glyphicon glyphicon-calendar"></span>
                                </span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">货主:</label></td>
                            <td colspan="2"><label class="pull-left">商品:</label></td>
                            <td><label class="pull-left">三级地址:</label></td>
                        </tr>
                        <tr>
                            <td>
                                <input id="ownerTypeC" value="OWNER" type="hidden">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="ownerCodeC" fieldName="ownerCodeC" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="ownerNameC" displayFieldName="ownerNameC" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="ownerCSelectId" deleteButtonId="ownerCDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerTypeC" isMultiSelected="true" afterSelect="selectOwner">
                                </sys:popSelect>
                            </td>
                            <td colspan="2">
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                               fieldId="skuCodeC" fieldName="skuCodeC" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="skuNameC" displayFieldName="skuNameC" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="skuCSelectId" deleteButtonId="skuCDeleteId"
                                               fieldLabels="货主编码|货主名称|商品编码|商品名称" fieldKeys="ownerCode|ownerName|skuCode|skuName"
                                               searchLabels="货主编码|商品编码|商品名称" searchKeys="ownerCode|skuCode|skuName" inputSearchKey="skuCodeAndName"
                                               queryParams="ownerCodes" queryParamValues="ownerCodeC" isMultiSelected="true" afterSelect="selectSku">
                                </sys:popSelect>
                            </td>
                            <td>
                                <input id="addrArea" name="addrArea" class="form-control" maxlength="64">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="wvRuleGroupConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
<!-- Excel上传弹出框 -->
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form class="form-horizontal" id="fileUploadForm" enctype="multipart/form-data">
        <div class="modal-dialog" style="width: 500px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">上传文件</h4>
                </div>
                <div class="modal-body">
                    <table class="table table-striped table-bordered table-condensed">
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
                        <input id="carrier_orderId" type="hidden">
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
</body>
</html>