<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>调拨单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="omRequisitionForm.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="ownerType" value="OWNER"/>
    <input type="hidden" id="carrierType" value="CARRIER"/>
    <input type="hidden" id="parentId" value="${fns:getUser().office.id}"/>
</div>
<div style="margin: 0 10px;">
    <shiro:hasPermission name="oms:order:requisition:edit">
        <a id="save" class="btn btn-primary" disabled onclick="save()"> 保存</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="oms:order:requisition:audit">
        <a id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="oms:order:requisition:audit:cancel">
        <a id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()"> 取消审核</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="oms:order:requisition:close">
        <a id="close" class="btn btn-primary" disabled onclick="closeOrder()"> 关闭</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="oms:order:requisition:cancel">
        <a id="cancel" class="btn btn-primary" disabled onclick="cancelOrder()"> 取消</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="omRequisitionEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container" style="height: 220px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#head-tab-1" aria-expanded="true">基本信息</a></li>
            <li class=""><a data-toggle="tab" href="#head-tab-2" aria-expanded="true">源仓库信息</a></li>
            <li class=""><a data-toggle="tab" href="#head-tab-3" aria-expanded="true">目标仓库信息</a></li>
            <li class=""><a data-toggle="tab" href="#head-tab-4" aria-expanded="true">物流信息</a></li>
            <li class=""><a data-toggle="tab" href="#head-tab-5" aria-expanded="true">审核信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="head-tab-1" class="tab-pane fade in active">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">调拨单号</label></td>
                        <td class="width-15">
                            <form:input path="reqNo" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right"><label style="color: red">*</label>订单时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='orderTime'>
                                <input type='text' name="orderTime" class="form-control required"
                                       value="<fmt:formatDate value="${omRequisitionEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right"><label style="color: red">*</label>订单类型</label></td>
                        <td class="width-15">
                            <form:select path="orderType" class="form-control required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_BUSINESS_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">订单状态</label></td>
                        <td class="width-15">
                            <form:select path="status" class="form-control" disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('OMS_RO_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right"><label style="color: red">*</label>货主编码</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control required" allowInput="true"
                                           fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                           displayFieldId="ownerCode" displayFieldName="ownerCode" 
                                           displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${omRequisitionEntity.ownerCode}"
                                           selectButtonId="ownerSBtnId" deleteButtonId="ownerDBtnId"
                                           fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="ownerType" afterSelect="ownerSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">货主名称</label></td>
                        <td class="width-15">
                            <form:input path="ownerName" htmlEscape="false" class="form-control" maxlength="64" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right"><label style="color: red">*</label>源仓库</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/sys/office/companyData" title="选择仓库" cssClass="form-control required" allowInput="true"
                                           fieldId="fmOrgId" fieldName="fmOrgId"
                                           fieldKeyName="id" fieldValue="${omRequisitionEntity.fmOrgId}"
                                           displayFieldId="fmOrgName" displayFieldName="fmOrgName"
                                           displayFieldKeyName="name" displayFieldValue="${omRequisitionEntity.fmOrgName}"
                                           selectButtonId="fmOrgSBtnId" deleteButtonId="fmOrgDBtnId"
                                           fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                           searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                           queryParams="id|type" queryParamValues="parentId"/>
                        </td>
                        <td class="width-10"><label class="pull-right"><label style="color: red">*</label>目标仓库</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/sys/office/companyData" title="选择仓库" cssClass="form-control required" allowInput="true"
                                           fieldId="toOrgId" fieldName="toOrgId"
                                           fieldKeyName="id" fieldValue="${omRequisitionEntity.toOrgId}"
                                           displayFieldId="toOrgName" displayFieldName="toOrgName"
                                           displayFieldKeyName="name" displayFieldValue="${omRequisitionEntity.toOrgName}"
                                           selectButtonId="toOrgSBtnId" deleteButtonId="toOrgDBtnId"
                                           fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                           searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                           queryParams="id|type" queryParamValues="parentId"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">客户订单号</label></td>
                        <td class="width-15">
                            <form:input path="customerNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">调拨原因</label></td>
                        <td class="width-15">
                            <form:input path="reqReason" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">备注信息</label></td>
                        <td colspan="3">
                            <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="255"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="head-tab-2" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">发货人</label></td>
                        <td class="width-15">
                            <form:input path="shipper" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="shipperTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">发货时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='planShipTime'>
                                <input type='text' name="planShipTime" class="form-control"
                                       value="<fmt:formatDate value="${omRequisitionEntity.planShipTime}" pattern="yyyy-MM-dd HH:mm"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">发货人区域</label></td>
                        <td class="width-15">
                            <sys:treeselect title="选择发货人区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="shipAreaId" name="shipAreaId" value="${omRequisitionEntity.shipAreaId}"
                                            labelName="area.name" labelValue="${omRequisitionEntity.shipAreaName}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">三级地址</label></td>
                        <td class="width-15">
                            <form:input path="shipArea" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                        <td class="width-10"><label class="pull-right">详细地址</label></td>
                        <td colspan="5">
                            <form:input path="shipAddress" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="head-tab-3" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">收货人</label></td>
                        <td class="width-15">
                            <form:input path="consignee" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="consigneeTel" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="width-10"><label class="pull-right">预计到货时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='planArrivalTime'>
                                <input type='text' name="planArrivalTime" class="form-control"
                                       value="<fmt:formatDate value="${omRequisitionEntity.planArrivalTime}" pattern="yyyy-MM-dd HH:mm"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">收货人区域</label></td>
                        <td class="width-15">
                            <sys:treeselect title="选择收货人区域" url="/sys/area/treeData" cssClass="form-control"
                                            id="consigneeAreaId" name="consigneeAreaId" value="${omRequisitionEntity.consigneeAreaId}"
                                            labelName="area.name" labelValue="${omRequisitionEntity.consigneeAreaName}"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">三级地址</label></td>
                        <td class="width-15">
                            <form:input path="consigneeArea" htmlEscape="false" class="form-control" maxlength="128"/>
                        </td>
                        <td class="width-10"><label class="pull-right">详细地址</label></td>
                        <td colspan="5">
                            <form:input path="consigneeAddress" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="head-tab-4" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">承运商</label></td>
                        <td class="width-15">
                            <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择承运商" cssClass="form-control" allowInput="true" inputSearchKey="codeAndName"
                                           fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="${omRequisitionEntity.carrierCode}"
                                           displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${omRequisitionEntity.carrierName}"
                                           selectButtonId="carrierSBtnId" deleteButtonId="carrierDBtnId"
                                           fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                           searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                           queryParams="ebcuType" queryParamValues="carrierType" afterSelect="carrierSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">运输方式</label></td>
                        <td class="width-15">
                            <form:select path="transportMode" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_TRANSPORT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="width-10"><label class="pull-right">物流单号</label></td>
                        <td class="width-15">
                            <form:input path="logisticsNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">运费</label></td>
                        <td class="width-15">
                            <form:input path="freightAmount" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">车牌号</label></td>
                        <td class="width-15">
                            <form:input path="vehicleNo" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">司机</label></td>
                        <td class="width-15">
                            <form:input path="driver" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="width-10"><label class="pull-right">联系电话</label></td>
                        <td class="width-15">
                            <form:input path="driverTel" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div id="head-tab-5" class="tab-pane fade">
                <table class="table well">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">创建人</label></td>
                        <td class="width-15">
                            <form:input path="createBy.name" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">审核人</label></td>
                        <td class="width-15">
                            <form:input path="auditBy.name" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">更新人</label></td>
                        <td class="width-15">
                            <form:input path="updateBy.name" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">机构</label></td>
                        <td class="width-15">
                            <form:input path="orgName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">创建时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='createDate'>
                                <input name="createDate" class="form-control" readonly value="<fmt:formatDate value="${omRequisitionEntity.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">审核时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='auditDate'>
                                <input name="auditTime" class="form-control" readonly value="<fmt:formatDate value="${omRequisitionEntity.auditTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td class="width-10"><label class="pull-right">更新时间</label></td>
                        <td class="width-15">
                            <div class='input-group form_datetime' id='updateDate'>
                                <input name="updateDate" class="form-control" readonly value="<fmt:formatDate value="${omRequisitionEntity.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>

<form id="omRequisitionDetailForm" class="form">
    <div class="tabs-container" style="min-height: 300px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#detail-tab-1" aria-expanded="true">订单明细</a></li>
        </ul>
        <div class="tab-content" style="overflow-x: auto;">
            <div id="detail-tab-1" class="tab-pane fade in active">
                <div>
                    <shiro:hasPermission name="oms:order:requisition:detail:add">
                        <a id="addDetail" class="btn btn-primary" disabled onclick="addDetail()"> 新增</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="oms:order:requisition:detail:edit">
                        <a id="saveDetail" class="btn btn-primary" disabled onclick="saveDetail()"> 保存</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="oms:order:requisition:detail:del">
                        <a id="removeDetail" class="btn btn-danger" disabled onclick="removeDetail()"> 删除</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="oms:order:requisition:task:gen">
                        <a id="createTask" class="btn btn-primary" disabled onclick="createTask()"> 生成任务</a>
                    </shiro:hasPermission>
                </div>
                <table class="table well text-nowrap" style="min-width: 1500px;">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th style="width:36px;vertical-align:middle">
                            <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                        </th>
                        <th>行号</th>
                        <th class="asterisk">商品编码</th>
                        <th>商品名称</th>
                        <th>商品规格</th>
                        <th>单位</th>
                        <th class="asterisk">数量</th>
                        <th>辅助单位</th>
                        <th>辅助单位数量</th>
                        <th>计划任务数</th>
                        <th>已生成任务数</th>
                        <th>备注</th>
                        <th width="10">&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody id="omRequisitionDetailList">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form>
<script type="text/template" id="omRequisitionDetailTpl">//<!--
<tr id="omRequisitionDetailList{{idx}}" data-index="{{idx}}">
    <td class="hide">
        <input id="omRequisitionDetailList{{idx}}_id" name="omRequisitionDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="omRequisitionDetailList{{idx}}_delFlag" name="omRequisitionDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="omRequisitionDetailList{{idx}}_recVer" name="omRequisitionDetailList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
        <input id="omRequisitionDetailList{{idx}}_reqNo" name="omRequisitionDetailList[{{idx}}].reqNo" type="hidden" value="{{row.reqNo}}"/>
        <input id="omRequisitionDetailList{{idx}}_ownerCode" name="omRequisitionDetailList[{{idx}}].ownerCode" type="hidden" value="{{row.ownerCode}}"/>
        <input id="omRequisitionDetailList{{idx}}_orgId" name="omRequisitionDetailList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="omRequisitionDetailList{{idx}}_def1" name="omRequisitionDetailList[{{idx}}].def1" type="hidden" value="{{row.def1}}"/>
        <input id="omRequisitionDetailList{{idx}}_def2" name="omRequisitionDetailList[{{idx}}].def2" type="hidden" value="{{row.def2}}"/>
        <input id="omRequisitionDetailList{{idx}}_def3" name="omRequisitionDetailList[{{idx}}].def3" type="hidden" value="{{row.def3}}"/>
        <input id="omRequisitionDetailList{{idx}}_def4" name="omRequisitionDetailList[{{idx}}].def4" type="hidden" value="{{row.def4}}"/>
        <input id="omRequisitionDetailList{{idx}}_def5" name="omRequisitionDetailList[{{idx}}].def5" type="hidden" value="{{row.def5}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td>
        <input id="omRequisitionDetailList{{idx}}_lineNo" name="omRequisitionDetailList[{{idx}}].lineNo" type="text" value="{{row.lineNo}}" class="form-control lineNo" readonly/>
    </td>
    <td>
        <sys:popSelect url="${ctx}/oms/basic/omItem/orderSkuGrid" title="选择商品" cssClass="form-control required" allowInput="true" inputSearchKey="codeAndName"
                       fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                       displayFieldId="omRequisitionDetailList{{idx}}_skuCode" displayFieldName="omRequisitionDetailList[{{idx}}].skuCode" displayFieldKeyName="skuCode" displayFieldValue="{{row.skuCode}}"
                       selectButtonId="omRequisitionDetailList{{idx}}_carrierSBtnId" deleteButtonId="omRequisitionDetailList{{idx}}_carrierDBtnId"
                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                       queryParams="ownerCode" queryParamValues="ownerCode" afterSelect="skuSelect({{idx}})"/>
    </td>
    <td>
        <input id="omRequisitionDetailList{{idx}}_skuName" name="omRequisitionDetailList[{{idx}}].skuName" type="text" value="{{row.skuName}}" class="form-control" readonly/>
    </td>
    <td>
        <input id="omRequisitionDetailList{{idx}}_skuSpec" name="omRequisitionDetailList[{{idx}}].skuSpec" type="text" value="{{row.skuSpec}}" class="form-control"/>
    </td>
    <td>
        <select id="omRequisitionDetailList{{idx}}_uom" name="omRequisitionDetailList[{{idx}}].uom" data-value="{{row.uom}}" class="form-control m-b">
            <c:forEach items="${fns:getDictList('OMS_ITEM_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <input id="omRequisitionDetailList{{idx}}_qty" name="omRequisitionDetailList[{{idx}}].qty" type="text" value="{{row.qty}}" class="form-control required" onkeyup="bq.numberValidator(this, 0, 0);"/>
    </td>
    <td>
        <select id="omRequisitionDetailList{{idx}}_auxiliaryUom" name="omRequisitionDetailList[{{idx}}].auxiliaryUom" data-value="{{row.auxiliaryUom}}" class="form-control m-b">
            <c:forEach items="${fns:getDictList('OMS_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <input id="omRequisitionDetailList{{idx}}_auxiliaryQty" name="omRequisitionDetailList[{{idx}}].auxiliaryQty" type="text" value="{{row.auxiliaryQty}}" class="form-control"/>
    </td>
    <td>
        <input id="omRequisitionDetailList{{idx}}_planTaskQty" name="omRequisitionDetailList[{{idx}}].planTaskQty" type="text" value="{{row.planTaskQty}}" class="form-control" onkeyup="bq.numberValidator(this, 0, 0);"/>
    </td>
    <td>
        <input id="omRequisitionDetailList{{idx}}_taskQty" name="omRequisitionDetailList[{{idx}}].taskQty" type="text" value="{{row.taskQty}}" class="form-control" readonly/>
    </td>
    <td width="10%">
        <input id="omRequisitionDetailList{{idx}}_remarks" name="omRequisitionDetailList[{{idx}}].remarks" type="text" value="{{row.remarks}}" class="form-control"/>
    </td>
</tr>//-->
</script>
</body>
</html>