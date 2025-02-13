<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>供应链订单管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="omChainCreateTaskForm.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="parentId"/>
</div>
<form:form id="inputForm" modelAttribute="omChainHeaderEntity" method="post" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId" />
    <form:hidden path="chainNo" />
    <form:hidden path="businessOrderType" />
    <form:hidden path="isHaveSo" />
    <form:hidden path="owner" />
    <form:hidden path="isAvailableStock" />
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td style="width: 70%;"><label class="pull-left">${omChainHeaderEntity.chainNo}</label></td>
                <td style="width: 10%;"><label class="pull-right asterisk">下发仓库</label></td>
                <td style="width: 20%;">
                    <sys:grid title="选择仓库" url="${ctx}/sys/office/companyData" cssClass="form-control required"
                              fieldId="warehouse" fieldName="warehouse" fieldKeyName="id" fieldValue="${omChainHeaderEntity.warehouse}"
                              displayFieldId="warehouseName" displayFieldName="warehouseName" displayFieldKeyName="name" displayFieldValue="${omChainHeaderEntity.warehouseName}"
                              fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                              searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                              queryParams="id" queryParamValues="parentId"
                              afterSelect="afterSelectOffice"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
    <div class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">供应链订单明细</a></li>
        </ul>
        <div class="tab-content">
            <div id="tab-1" class="tab-pane fade in active" style="overflow: scroll;">
                <table class="table well text-nowrap" style="overflow: scroll; min-width: 700px;">
                    <thead>
                    <tr>
                        <th><label><input id="allCheckBox" type="checkbox" onclick="allCheckBoxClick();"/></label></th>
                        <th class="hide"></th>
                        <th>商品编码</th>
                        <th>商品名称</th>
                        <th>辅助单位</th>
                        <th>辅助单位数量</th>
                        <th>单位</th>
                        <th>数量</th>
                        <th class="asterisk">计划任务数量</th>
                        <th>已生成任务数量</th>
                        <th>库存可用数量</th>
                    </tr>
                    </thead>
                    <tbody id="omChainDetailList">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="omChainDetailTpl">//<!--
<tr id="omChainDetailList{{idx}}">
    <td width="2%"><input id="omChainDetailList{{idx}}_checkbox" name="omChainDetailList[{{idx}}].checkbox" class="listCheckBox" type="checkbox" onclick="checkboxClick()" /></td>
    <td class="hide">
        <input id="omChainDetailList{{idx}}_id" name="omChainDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="omChainDetailList{{idx}}_delFlag" name="omChainDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="omChainDetailList{{idx}}_ratio" name="omChainDetailList[{{idx}}].ratio" type="hidden" value="1"/>
    </td>
    <td style="width: 12%;">
        <input id="omChainDetailList{{idx}}_skuCode" readonly name="omChainDetailList[{{idx}}].skuCode" value="{{row.skuCode}}" data-msg-required="" class="form-control" style="" aria-required="true" type="text">
    </td>
    <td style="width: 12%;">
        <input id="omChainDetailList{{idx}}_skuName" name="omChainDetailList[{{idx}}].skuName" type="text" value="{{row.skuName}}" readonly class="form-control"/>
    </td>
    <td style="width: 8%;">
        <select id="omChainDetailList{{idx}}_auxiliaryUnit" name="omChainDetailList[{{idx}}].auxiliaryUnit" data-value="{{row.auxiliaryUnit}}" class="form-control m-b" disabled>
            <c:forEach items="${fns:getDictList('OMS_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td style="width: 8%;">
        <input id="omChainDetailList{{idx}}_auxiliaryQty" name="omChainDetailList[{{idx}}].auxiliaryQty" type="text" value="{{row.auxiliaryQty}}" readonly class="form-control"/>
    </td>
    <td style="width: 8%;">
        <select id="omChainDetailList{{idx}}_unit" name="omChainDetailList[{{idx}}].unit" data-value="{{row.unit}}" class="form-control m-b" disabled>
            <c:forEach items="${fns:getDictList('OMS_ITEM_UNIT')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td style="width: 8%;">
        <input id="omChainDetailList{{idx}}_qty" name="omChainDetailList[{{idx}}].qty" type="text" value="{{row.qty}}" class="form-control" readonly onkeyup="bq.numberValidator(this, 0, 0)" onchange="qtyChange({{idx}})"/>
    </td>
    <td style="width: 10%;">
        <input id="omChainDetailList{{idx}}_planTaskQty" name="omChainDetailList[{{idx}}].planTaskQty" type="text" value="{{row.planTaskQty}}" class="form-control" onkeyup="bq.numberValidator(this, 0, 0)" onchange="planTaskQtyChange({{idx}})"/>
    </td>
    <td style="width: 8%;">
        <input id="omChainDetailList{{idx}}_taskQty" name="omChainDetailList[{{idx}}].taskQty" type="text" value="{{row.taskQty}}" class="form-control" readonly />
    </td>
    <td style="width: 8%;">
        <input id="omChainDetailList{{idx}}_availableQty" name="omChainDetailList[{{idx}}].availableQty" type="text" value="{{row.availableQty}}" class="form-control" readonly />
    </td>
</tr>//-->
</script>
</body>
</html>