<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>需求计划管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmDemandPlanForm.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER">
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="customerType" value="OWNER">
    <input type="hidden" id="outletType" value="OUTLET">
    <input type="hidden" id="carrierType" value="CARRIER">
    <input type="hidden" id="shipType" value="SHIP">
    <input type="hidden" id="consigneeType" value="CONSIGNEE">
</div>

<div style="margin-left: 10px;">
    <shiro:hasPermission name="tms:order:tmDemandPlan:edit">
        <a id="save" class="btn btn-primary btn-sm" disabled onclick="save()"> 保存</a>
    </shiro:hasPermission>
</div>

<form:form id="inputForm" modelAttribute="tmDemandPlanEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <%--基本信息--%>
    <div class="tabs-container" style="max-height: 300px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#basicInfo" aria-expanded="true">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table table-bordered">
                    <tr>
                        <td style="width:8%;"><label class="pull-right">需求计划单号：</label></td>
                        <td style="width:12%;">
                            <form:input path="planOrderNo" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                        <td style="width:8%;"><label class="pull-right"><font color="red">*</font>订单时间：</label></td>
                        <td style="width:12%;">
                            <div class='input-group form_datetime' id='orderTime'>
                                <input type='text' name="orderTime" class="form-control required"
                                       value="<fmt:formatDate value="${tmDemandPlanEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width:8%;"><label class="pull-right"><font color="red">*</font>到达时间：</label></td>
                        <td style="width:12%;">
                            <div class='input-group form_datetime' id='arrivalTime'>
                                <input type='text' name="arrivalTime" class="form-control required"
                                       value="<fmt:formatDate value="${tmDemandPlanEntity.arrivalTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width:8%;"><label class="pull-right">状态：</label></td>
                        <td style="width:12%;">
                            <form:select path="status" class="form-control " disabled="true">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_DEMAND_PLAN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td style="width:8%;"><label class="pull-right"><font color="red">*</font>客户：</label></td>
                        <td style="width:12%;">
                            <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="transportObjCode" fieldValue="${tmDemandPlanEntity.ownerCode}"
                                      displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="transportObjName" displayFieldValue="${tmDemandPlanEntity.ownerName}"
                                      fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                      cssClass="form-control required"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
<%--明细信息列表--%>
<div class="tabs-container" style="margin-left: 10px;max-height: 500px;">
    <ul class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#demandPlanDetailInfo" aria-expanded="true">需求明细信息</a></li>
    </ul>
    <div class="tab-content">
        <%--订单明细信息--%>
        <div id="demandPlanDetailInfo" class="tab-pane fade in active">
            <div id="demandPlanDetailToolbar" style="padding-bottom: 10px; padding-top: 10px;">
                <shiro:hasPermission name="tms:order:tmDemandPlan:detail:add">
                    <a id="demandPlanDetail_add" class="btn btn-primary btn-sm" disabled onclick="addDetail()"> 新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmDemandPlan:detail:save">
                    <a id="demandPlanDetail_save" class="btn btn-primary btn-sm" disabled onclick="saveDetail()"> 保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmDemandPlan:detail:del">
                    <a id="demandPlanDetail_remove" class="btn btn-danger btn-sm" disabled onclick="delDetail()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <div class="fixed-table-container" style="padding-bottom: 0;">
                <form id="demandPlanDetailForm">
                    <table id="demandPlanDetailTable" class="table table-hover table-striped text-nowrap" data-toolbar="#demandPlanDetailToolbar">
                        <thead>
                        <th class="hide"></th>
                        <th style="width:36px;vertical-align:middle">
                            <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                        </th>
                        <th>物料编码*</th>
                        <th>物料名称</th>
                        <th>数量*</th>
                        </thead>
                        <tbody id="tmDemandPlanDetailList">
                        </tbody>
                    </table>
                </form>
            </div>
        </div>
    </div>
</div>
<script type="text/template" id="tmDemandPlanDetailTpl">//<!--
<tr id="tmDemandPlanDetailList{{idx}}" data-index="{{idx}}">
    <td class="hide">
        <input type="hidden" id="tmDemandPlanDetailList{{idx}}_id" name="tmDemandPlanDetailList[{{idx}}].id" value="{{row.id}}"/>
        <input type="hidden" id="tmDemandPlanDetailList{{idx}}_planOrderNo" name="tmDemandPlanDetailList[{{idx}}].planOrderNo" value="{{row.planOrderNo}}"/>
        <input type="hidden" id="tmDemandPlanDetailList{{idx}}_ownerCode" name="tmDemandPlanDetailList[{{idx}}].ownerCode" value="{{row.ownerCode}}"/>
        <input type="hidden" id="tmDemandPlanDetailList{{idx}}_orgId" name="tmDemandPlanDetailList[{{idx}}].orgId" value="{{row.orgId}}"/>
        <input type="hidden" id="tmDemandPlanDetailList{{idx}}_baseOrgId" name="tmDemandPlanDetailList[{{idx}}].baseOrgId" value="{{row.baseOrgId}}"/>
        <input type="hidden" id="tmDemandPlanDetailList{{idx}}_recVer" name="tmDemandPlanDetailList[{{idx}}].recVer" value="{{row.recVer}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td>
        <sys:grid title="商品" url="${ctx}/tms/basic/tmItem/grid" fieldId="" fieldName="" fieldKeyName=""
                  displayFieldId="tmDemandPlanDetailList{{idx}}_skuCode" displayFieldName="tmDemandPlanDetailList[{{idx}}].skuCode" displayFieldKeyName="skuCode" displayFieldValue="{{row.skuCode}}"
                  fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName" searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                  queryParams="ownerCode|orgId" queryParamValues="ownerCode|baseOrgId"
                  cssClass="form-control required" afterSelect="demandPlanDetailSkuSelect({{idx}})"/>
    </td>
    <td>
        <input id="tmDemandPlanDetailList{{idx}}_skuName" name="tmDemandPlanDetailList[{{idx}}].skuName" class="form-control" value="{{row.skuName}}" readonly maxlength="20"/>
    </td>
    <td>
        <input id="tmDemandPlanDetailList{{idx}}_qty" name="tmDemandPlanDetailList[{{idx}}].qty" class="form-control required" value="{{row.qty}}" onkeyup="bq.numberValidator(this, 4, 0);"  maxlength="18"/>
    </td>
</tr>//-->
</script>
</body>
</html>