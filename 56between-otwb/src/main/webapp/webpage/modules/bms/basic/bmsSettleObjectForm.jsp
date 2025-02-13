<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>结算对象管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        function doSubmit($table, $topIndex) {
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.bqError(validate.msg);
                return false;
            }
            jp.loading();
            var disabledObjs = bq.openDisabled("#inputForm");
            var params = $('#inputForm').serialize();
            bq.closeDisabled(disabledObjs);
            jp.post("${ctx}/bms/basic/bmsSettleObject/save", params, function (data) {
                if (data.success) {
                    $table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close($topIndex);//关闭dialog
                } else {
                    jp.bqError(data.msg);
                }
            });
            return true;
        }

        $(document).ready(function () {
            if (!$("#id").val()) {
                $("#orgId").val(jp.getCurrentOrg().orgId);
            } else {
                $("#settleObjectCode").prop("readonly", true);
                $("#settleObjectName").prop("readonly", true);
            }
        });
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="bmsSettleObject" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-8"><label class="pull-right asterisk">结算对象代码</label></td>
            <td class="width-12">
                <input id="settleType" value="SETTLEMENT" type="hidden"/>
                <sys:popSelect fieldId="" fieldKeyName="" fieldName="" fieldValue=""
                               displayFieldId="settleObjectCode" displayFieldKeyName="ebcuCustomerNo"
                               displayFieldName="settleObjectCode" displayFieldValue="${bmsSettleObject.settleObjectCode}"
                               selectButtonId="settleObjectSBtnId" deleteButtonId="settleObjectDBtnId"
                               fieldLabels="结算对象代码|结算对象名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                               searchLabels="结算对象代码|结算对象名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                               queryParams="ebcuType" queryParamValues="settleType"
                               title="选择结算对象" url="${ctx}/bms/basic/bmsCustomer/pop" cssClass="form-control required"
                               concatId="settleObjectName" concatName="ebcuNameCn"/>
            </td>
            <td class="width-8"><label class="pull-right asterisk">结算对象名称</label></td>
            <td class="width-12">
                <form:input path="settleObjectName" htmlEscape="false" class="form-control required" readonly="true"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">结算类别</label></td>
            <td class="width-12">
                <form:select path="settleCategory" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_SETTLEMENT_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-8"><label class="pull-right">结算方式</label></td>
            <td class="width-12">
                <form:select path="settleType" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_SETTLEMENT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">地址</label></td>
            <td class="width-12">
                <form:input path="address" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-8"><label class="pull-right">联系人</label></td>
            <td class="width-12">
                <form:input path="contacts" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">电话</label></td>
            <td class="width-12">
                <form:input path="telephone" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-8"><label class="pull-right">开户银行</label></td>
            <td class="width-12">
                <form:input path="bank" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">银行账户</label></td>
            <td class="width-12">
                <form:input path="bankAccount" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-8"><label class="pull-right">银行账户名</label></td>
            <td class="width-12">
                <form:input path="bankAccountName" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">备注</label></td>
            <td colspan="3">
                <form:input path="remarks" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>