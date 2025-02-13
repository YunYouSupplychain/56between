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
            jp.post("${ctx}/sys/common/bms/settleObject/save", params, function (data) {
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
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            } else {
                $("#settleObjectCode").prop("readonly", true);
                $("#settleObjectName").prop("readonly", true);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        });

        function settleAfterSelect(row) {
            if (row) {
                $('#settleObjectName').val(row.ebcuNameCn);
            }
        }

    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysBmsSettleObject" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>结算对象代码：</label></td>
            <td class="width-12">
                <input id="settleType" value="SETTLEMENT" type="hidden"/>
                <sys:grid title="选择结算对象" url="${ctx}/sys/common/bms/customer/grid" cssClass="form-control required"
                          fieldId="" fieldName="" fieldKeyName=""
                          displayFieldId="settleObjectCode" displayFieldName="settleObjectCode"
                          displayFieldKeyName="ebcuCustomerNo" displayFieldValue="${sysBmsSettleObject.settleObjectCode}"
                          fieldLabels="结算对象代码|结算对象名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                          searchLabels="结算对象代码|结算对象名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                          queryParams="ebcuType|dataSet" queryParamValues="settleType|dataSet"
                          afterSelect="settleAfterSelect"/>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>结算对象名称：</label></td>
            <td class="width-12">
                <form:input path="settleObjectName" htmlEscape="false" class="form-control required" readonly="true"/>
            </td>
            <td class="width-8"><label class="pull-right">结算类别：</label></td>
            <td class="width-12">
                <form:select path="settleCategory" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_SETTLEMENT_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-8"><label class="pull-right">结算方式：</label></td>
            <td class="width-12">
                <form:select path="settleType" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('BMS_SETTLEMENT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
            <td class="width-12">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysBmsSettleObject.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysBmsSettleObject.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">联系人：</label></td>
            <td class="width-12">
                <form:input path="contacts" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-8"><label class="pull-right">电话：</label></td>
            <td class="width-12">
                <form:input path="telephone" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-8"><label class="pull-right">开户银行：</label></td>
            <td class="width-12">
                <form:input path="bank" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-8"><label class="pull-right">银行账户：</label></td>
            <td class="width-12">
                <form:input path="bankAccount" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-8"><label class="pull-right">银行账户名：</label></td>
            <td class="width-12">
                <form:input path="bankAccountName" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">地址：</label></td>
            <td class="width-12" colspan="3">
                <form:input path="address" htmlEscape="false" class="form-control"/>
            </td>
            <td class="width-8"><label class="pull-right">备注：</label></td>
            <td class="width-12" colspan="5">
                <form:input path="remarks" htmlEscape="false" class="form-control"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>