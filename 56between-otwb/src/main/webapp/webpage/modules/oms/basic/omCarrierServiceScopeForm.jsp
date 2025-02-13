<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>承运商服务范围管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            jp.loading();
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.alert(validate.msg);
                return false;
            }
            jp.post("${ctx}/oms/basic/omCarrierServiceScope/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close(index);//关闭dialog
                } else {
                    jp.alert(data.msg);
                }
            });
            return true;
        }

        $(document).ready(function () {
            if (!$("#id").val()) {
                $("#orgId").val(jp.getCurrentOrg().orgId);
                $("#orgName").val(jp.getCurrentOrg().orgName);
            }
        });
    </script>
</head>
<body class="bg-white">
<div class="hidden">
    <input type="hidden" id="ownerType" value="OWNER">
    <input type="hidden" id="carrierType" value="CARRIER"/>
</div>
<form:form id="inputForm" modelAttribute="omCarrierServiceScopeEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <table class="table">
        <tr>
            <td width="8%"><label class="pull-right"><font color="red">*</font>货主</label></td>
            <td width="12%">
                <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control required"
                               fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${omCarrierServiceScopeEntity.ownerCode}"
                               displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${omCarrierServiceScopeEntity.ownerCode}"
                               selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                               queryParams="ebcuType" queryParamValues="ownerType">
                </sys:popSelect>
            </td>
            <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>承运商</label></td>
            <td style="width: 12%;">
                <sys:popSelect title="选择承运商" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control required"
                               fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="${omCarrierServiceScopeEntity.carrierCode}"
                               displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${omCarrierServiceScopeEntity.carrierName}"
                               fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                               searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                               selectButtonId="carrierSBtnId" deleteButtonId="carrierDBtnId"
                               queryParams="ebcuType" queryParamValues="carrierType"/>
            </td>
        </tr>
        <tr>
            <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>业务服务范围编码</label></td>
            <td style="width: 12%;">
                <sys:popSelect title="选择业务服务范围" url="${ctx}/oms/basic/omBusinessServiceScope/data" cssClass="form-control required" fieldId="" fieldKeyName="" fieldName="" fieldValue=""
                               displayFieldId="groupCode" displayFieldKeyName="groupCode" displayFieldName="groupCode" displayFieldValue="${omCarrierServiceScopeEntity.groupCode}"
                               fieldLabels="业务服务范围编码|业务服务范围名称" fieldKeys="groupCode|groupName"
                               searchLabels="业务服务范围编码|业务服务范围名称" searchKeys="groupCode|groupName"
                               selectButtonId="groupSBtnId" deleteButtonId="groupDBtnId" concatId="groupName" concatName="groupName"/>
            </td>
            <td style="width: 8%;"><label class="pull-right">业务服务范围名称</label></td>
            <td style="width: 12%;">
                <form:input path="groupName" htmlEscape="false" class="form-control " readonly="true"/>
            </td>
        </tr>
        <tr>
            <td style="width: 8%;"><label class="pull-right">机构</label></td>
            <td style="width: 12%;">
                <form:input path="orgName" htmlEscape="false" class="form-control " readonly="true"/>
            </td>
            <td style="width: 8%;"><label class="pull-right">最大重量</label></td>
            <td style="width: 12%;">
                <form:input path="maxWeight" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
            <td style="width: 8%;"><label class="pull-right">最大体积</label></td>
            <td style="width: 12%;">
                <form:input path="maxVolume" htmlEscape="false" class="form-control "/>
            </td>
            <td style="width: 8%;"><label class="pull-right">最大费用</label></td>
            <td style="width: 12%;">
                <form:input path="maxCost" htmlEscape="false" class="form-control "/>
            </td>
        <tr>
            <td style="width: 8%;"><label class="pull-right">备注信息</label></td>
            <td colspan="3">
                <form:input path="remarks" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>