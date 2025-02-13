<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>上架任务管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function deleteAll() {
            var rows = [];
            var disabledObjs = bq.openDisabled('#inputForm');
            var row = $('#inputForm').serializeJSON();
            rows.push(row);
            bq.closeDisabled(disabledObjs);
            jp.confirm('确认要删除该上架任务记录吗？', function () {
                jp.loading();
                $.ajax({
                    headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
                    type: 'POST',
                    dataType: "json",
                    data: JSON.stringify(rows),
                    url: "${ctx}/wms/task/banQinWmTaskPa/deleteAll",
                    success: function (data) {
                        if (data.success) {
                            jp.success(data.msg);
                            jp.close(parent.layer.getFrameIndex(window.name));
                        } else {
                            jp.bqError(data.msg);
                        }
                    }
                });
            })
        }

        function confirm() {
            var rows = [];
            var disabledObjs = bq.openDisabled('#inputForm');
            var row = $('#inputForm').serializeJSON();
            rows.push(row);
            bq.closeDisabled(disabledObjs);
            jp.loading();
            $.ajax({
                headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
                type: 'POST',
                dataType: "json",
                data: JSON.stringify(rows),
                url: "${ctx}/wms/task/banQinWmTaskPa/putAway",
                success: function (data) {
                    if (data.success) {
                        window.location = "${ctx}/wms/task/banQinWmTaskPa/form?id=" + $('#id').val();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                }
            });
        }
    </script>
</head>
<body>
<div id="toolbar" style="width: 100%; padding-left: 10px;">
    <shiro:hasPermission name="task:banQinWmTaskPa:del">
        <a id="remove" class="btn btn-danger" onclick="deleteAll()">删除上架任务</a>
    </shiro:hasPermission>
    <shiro:hasPermission name="task:banQinWmTaskPa:putAway">
        <a id="confirm" class="btn btn-primary" onclick="confirm()">上架确认</a>
    </shiro:hasPermission>
</div>
<form:form id="inputForm" modelAttribute="banQinWmTaskPaEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <form:hidden path="qtyPaEa"/>
    <form:hidden path="orderType"/>
    <form:hidden path="traceId"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">上架任务Id</label></td>
                <td class="width-15">
                    <form:input path="paId" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">行号</label></td>
                <td class="width-15">
                    <form:input path="lineNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">状态</label></td>
                <td class="width-15">
                    <form:select path="status" class="form-control m-b" disabled="true">
                        <form:options items="${fns:getDictList('SYS_WM_TASK_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">单据号</label></td>
                <td class="width-15">
                    <form:input path="orderNo" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">货主</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                   fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${banQinWmTaskPaEntity.ownerCode}"
                                   displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinWmTaskPaEntity.ownerName}"
                                   selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" disabled="disabled">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">商品编码</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                   displayFieldId="skuCode" displayFieldName="skuCode" displayFieldKeyName="skuCode" displayFieldValue="${banQinWmTaskPaEntity.skuCode}"
                                   selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                   fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" disabled="disabled">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">商品名称</label></td>
                <td class="width-15">
                    <form:input path="skuName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">批次号</label></td>
                <td class="width-15">
                    <form:input path="lotNum" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">源库位</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                   displayFieldId="fmLoc" displayFieldName="fmLoc" displayFieldKeyName="locCode" displayFieldValue="${banQinWmTaskPaEntity.fmLoc}"
                                   selectButtonId="fmLocSelectId" deleteButtonId="fmLocDeleteId"
                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" disabled="disabled">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">源跟踪号</label></td>
                <td class="width-15">
                    <form:input path="fmId" htmlEscape="false" class="form-control" disabled="true"/>
                </td>
                <td class="width-10"><label class="pull-right">推荐库位</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                   displayFieldId="suggestLoc" displayFieldName="suggestLoc" displayFieldKeyName="locCode" displayFieldValue="${banQinWmTaskPaEntity.suggestLoc}"
                                   selectButtonId="suggestLocSelectId" deleteButtonId="suggestLocDeleteId"
                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" disabled="disabled">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right"><font color="red">*</font>上架库位</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                   displayFieldId="toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue="${banQinWmTaskPaEntity.toLoc}"
                                   selectButtonId="toLocSelectId" deleteButtonId="toLocDeleteId"
                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                    </sys:popSelect>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right"><font color="red">*</font>上架跟踪号</label></td>
                <td class="width-15">
                    <form:input path="toId" htmlEscape="false" class="form-control required"/>
                </td>
                <td class="width-10"><label class="pull-right">包装规格</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackage/grid" title="选择包装" cssClass="form-control"
                                   fieldId="packCode" fieldName="packCode" fieldKeyName="cdpaCode" fieldValue="${banQinWmTaskPaEntity.packCode}"
                                   displayFieldId="_packDesc" displayFieldName="packDesc" displayFieldKeyName="cdpaFormat" displayFieldValue="${banQinWmTaskPaEntity.packDesc}"
                                   selectButtonId="packSelectId" deleteButtonId="packDeleteId"
                                   fieldLabels="包装代码|包装类型|包装规格" fieldKeys="cdpaCode|cdpaType|cdpaFormat"
                                   searchLabels="包装代码|包装类型|包装规格" searchKeys="cdpaCode|cdpaType|cdpaFormat" disabled="disabled">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">包装单位</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhPackageRelation/grid" title="选择包装单位" cssClass="form-control required"
                                   fieldId="uom" fieldName="uom" fieldKeyName="cdprUnitLevel" fieldValue="${banQinWmTaskPaEntity.uom}"
                                   displayFieldId="_uomDesc" displayFieldName="uomDesc" displayFieldKeyName="cdprDesc" displayFieldValue="${banQinWmTaskPaEntity.uomDesc}"
                                   selectButtonId="uomSelectId" deleteButtonId="uomDeleteId"
                                   fieldLabels="包装单位|数量|描述" fieldKeys="cdprUnitLevel|cdprQuantity|cdprDesc"
                                   searchLabels="包装单位|描述" searchKeys="cdprUnitLevel|cdprDesc" disabled="disabled">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">上架库位指定规则</label></td>
                <td class="width-15">
                    <form:select path="reserveCode" class="form-control m-b" disabled="true">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('SYS_WM_RESERVE_CODE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">上架规则</label></td>
                <td class="width-15">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdRulePaHeader/grid" title="选择上架规则" cssClass="form-control"
                                   fieldId="paRule" fieldName="paRule" fieldKeyName="ruleCode" fieldValue="${banQinWmTaskPaEntity.paRule}"
                                   displayFieldId="paRuleName" displayFieldName="paRuleName" displayFieldKeyName="ruleName" displayFieldValue="${banQinWmTaskPaEntity.paRuleName}"
                                   selectButtonId="paRuleSelectId" deleteButtonId="paRuleDeleteId" disabled="disabled"
                                   fieldLabels="上架规则代码|上架规则名称" fieldKeys="ruleCode|ruleName"
                                   searchLabels="上架规则代码|上架规则名称" searchKeys="ruleCode|ruleName">
                    </sys:popSelect>
                </td>
                <td class="width-10"><label class="pull-right">上架时间</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='paTimeF'>
                        <input id="paTime" name="paTime" class="form-control" value="<fmt:formatDate value="${banQinWmTaskPaEntity.paTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                        </span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right">上架数</label></td>
                <td class="width-15">
                    <form:input path="qtyPaUom" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right"><font color="red">*</font>上架数EA</label></td>
                <td class="width-15">
                    <form:input path="currentPaQtyEa" htmlEscape="false" class="form-control required"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">上架人</label></td>
                <td class="width-15">
                    <form:input path="paOp" htmlEscape="false" class="form-control" maxlength="32"/>
                </td>
                <td class="width-10"><label class="pull-right">备注</label></td>
                <td class="width-15" colspan="5">
                    <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="128"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
</body>
</html>