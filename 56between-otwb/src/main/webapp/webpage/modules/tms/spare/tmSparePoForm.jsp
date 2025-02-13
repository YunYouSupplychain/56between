<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>备件入库管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmSparePoForm.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="supplierType" value="SUPPLIER"/>
</div>
<div style="margin-left: 10px;">
    <shiro:hasPermission name="tms:spare:po:edit">
        <a id="save" class="btn btn-primary" onclick="save()"> 保存</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="tmSparePoEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="baseOrgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td style="width:8%;"><label class="pull-right">入库单号：</label></td>
            <td style="width:12%;">
                <form:input path="sparePoNo" cssClass="form-control" htmlEscape="false" maxlength="20" readonly="true"/>
            </td>
            <td style="width:8%;"><label class="pull-right">状态：</label></td>
            <td style="width:12%;">
                <form:select path="orderStatus" class="form-control " disabled="true">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('TMS_SPARE_ASN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td style="width:8%;"><label class="pull-right"><font color="red">*</font>订单时间：</label></td>
            <td style="width:12%;">
                <div class='input-group date' id='orderTime'>
                    <input type='text' name="orderTime" class="form-control required"
                           value="<fmt:formatDate value="${tmSparePoEntity.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
            </td>
            <td style="width:8%;"><label class="pull-right"><font color="red">*</font>类型：</label></td>
            <td style="width:12%;">
                <form:select path="orderType" class="form-control required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('TMS_SPARE_ASN_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td style="width:8%;"><label class="pull-right">客户单号：</label></td>
            <td style="width:12%;">
                <form:input path="customerNo" cssClass="form-control" htmlEscape="false" maxlength="20"/>
            </td>
        </tr>
        <tr>
            <td style="width:8%;"><label class="pull-right">操作人：</label></td>
            <td style="width:12%;">
                <form:input path="operator" cssClass="form-control" htmlEscape="false" maxlength="20"/>
            </td>
            <td style="width:8%;"></td>
            <td style="width:12%;"></td>
            <td style="width:8%;"></td>
            <td style="width:12%;"></td>
            <td style="width:8%;"></td>
            <td style="width:12%;"></td>
            <td style="width:8%;"></td>
            <td style="width:12%;"></td>
        </tr>
        </tbody>
    </table>
</form:form>
<div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="#detail" data-toggle="tab" aria-expanded="true">订单明细</a></li>
        <li class=""><a href="#scan" data-toggle="tab" aria-expanded="true">扫描明细</a></li>
    </ul>
    <div class="tab-content">
        <div id="detail" class="tab-pane fade in active">
            <div style="margin-top: 5px;margin-bottom: 5px;">
                <shiro:hasPermission name="tms:spare:po:detail:add">
                    <a id="addDetail" class="btn btn-primary" disabled onclick="addDetail()"> 新增</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:spare:po:detail:edit">
                    <a id="saveDetail" class="btn btn-primary" disabled onclick="saveDetail()"> 保存</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:spare:po:detail:del">
                    <a id="removeDetail" class="btn btn-primary" disabled onclick="removeDetail()"> 删除</a>
                </shiro:hasPermission>
            </div>
            <form id="tmSparePoDetailForm">
                <table id="tmSparePoDetailTable" class="table table-hover table-striped text-nowrap">
                    <thead>
                    <th class="hide"></th>
                    <th style="width:36px;vertical-align:middle">
                        <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                    </th>
                    <th>行号</th>
                    <th><font color="red">*</font>备件</th>
                    <th>供应商</th>
                    <th><font color="red">*</font>订单数量</th>
                    <th>单价</th>
                    </thead>
                    <tbody id="tmSparePoDetailList"></tbody>
                </table>
            </form>
        </div>
        <div id="scan" class="tab-pane fade">
            <table id="tmSparePoScanInfoTable" class="text-nowrap"></table>
        </div>
    </div>
</div>
<script type="text/template" id="tmSparePoDetailTpl">//<!--
<tr id="tmSparePoDetailList{{idx}}" data-index="{{idx}}">
    <td class="hide">
        <input type="hidden" id="tmSparePoDetailList{{idx}}_id" name="tmSparePoDetailList[{{idx}}].id" value="{{row.id}}"/>
        <input type="hidden" id="tmSparePoDetailList{{idx}}_sparePoNo" name="tmSparePoDetailList[{{idx}}].sparePoNo" value="{{row.sparePoNo}}"/>
        <input type="hidden" id="tmSparePoDetailList{{idx}}_orgId" name="tmSparePoDetailList[{{idx}}].orgId" value="{{row.orgId}}"/>
        <input type="hidden" id="tmSparePoDetailList{{idx}}_baseOrgId" name="tmSparePoDetailList[{{idx}}].baseOrgId" value="{{row.baseOrgId}}"/>
        <input type="hidden" id="tmSparePoDetailList{{idx}}_recVer" name="tmSparePoDetailList[{{idx}}].recVer" value="{{row.recVer}}"/>
    </td>
    <td style="vertical-align: middle">
        <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
    </td>
    <td>
        <input id="tmSparePoDetailList{{idx}}_lineNo" name="tmSparePoDetailList[{{idx}}].lineNo" class="form-control lineNo" value="{{row.lineNo}}" readonly/>
    </td>
    <td>
        <sys:grid title="选择备件" url="${ctx}/tms/basic/tmFitting/grid" cssClass="form-control required"
                  fieldId="tmSparePoDetailList{{idx}}_fittingCode" fieldName="tmSparePoDetailList[{{idx}}].fittingCode" fieldKeyName="fittingCode" fieldValue="{{row.fittingCode}}"
                  displayFieldId="tmSparePoDetailList{{idx}}_fittingName" displayFieldName="tmSparePoDetailList[{{idx}}].fittingName" displayFieldKeyName="fittingName" displayFieldValue="{{row.fittingName}}"
                  fieldLabels="备件编码|备件名称" fieldKeys="fittingCode|fittingName"
                  searchLabels="备件编码|备件名称" searchKeys="fittingCode|fittingName"
                  queryParams="orgId" queryParamValues="baseOrgId" afterSelect="fittingSelect({{idx}})"/>
    </td>
    <td>
        <sys:grid title="选择供应商" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                  fieldId="tmSparePoDetailList{{idx}}_supplierCode" fieldName="tmSparePoDetailList[{{idx}}].supplierCode" fieldKeyName="transportObjCode" fieldValue="{{row.supplierCode}}"
                  displayFieldId="tmSparePoDetailList{{idx}}_supplierName" displayFieldName="tmSparePoDetailList[{{idx}}].supplierName" displayFieldKeyName="transportObjName" displayFieldValue="{{row.supplierName}}"
                  fieldLabels="供应商编码|供应商名称" fieldKeys="transportObjCode|transportObjName"
                  searchLabels="供应商编码|供应商名称" searchKeys="transportObjCode|transportObjName"
                  queryParams="orgId|transportObjType" queryParamValues="baseOrgId|supplierType"/>
    </td>
    <td>
        <input id="tmSparePoDetailList{{idx}}_poQty" name="tmSparePoDetailList[{{idx}}].poQty" class="form-control required" value="{{row.poQty}}" onkeyup="bq.numberValidator(this, 6, 0)" maxlength="18"/>
    </td>
    <td>
        <input id="tmSparePoDetailList{{idx}}_price" name="tmSparePoDetailList[{{idx}}].price" class="form-control" value="{{row.price}}" onkeyup="bq.numberValidator(this, 6, 0)" maxlength="18"/>
    </td>
</tr>//-->
</script>
</body>
</html>