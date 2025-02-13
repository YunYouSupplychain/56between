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
            jp.post("${ctx}/sys/common/oms/carrierServiceScope/save", $('#inputForm').serialize(), function (data) {
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
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        });
    </script>
</head>
<body class="bg-white">
<div class="hidden">
    <input type="hidden" id="ownerType" value="OWNER">
    <input type="hidden" id="carrierType" value="CARRIER"/>
</div>
<form:form id="inputForm" modelAttribute="sysOmsCarrierServiceScopeEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="oldOwnerCode"/>
    <form:hidden path="oldCarrierCode"/>
    <form:hidden path="oldGroupCode"/>
    <table class="table">
        <tbody>
        <tr>
            <td style="width: 15%;"><label class="pull-right"><font color="red">*</font>货主</label></td>
            <td style="width: 35%;">
                <sys:grid title="选择货主" url="${ctx}/sys/common/oms/customer/grid" cssClass="form-control required"
                          fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${sysOmsCarrierServiceScopeEntity.ownerCode}"
                          displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${sysOmsCarrierServiceScopeEntity.ownerName}"
                          fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                          searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                          queryParams="ebcuType|dataSet" queryParamValues="ownerType|dataSet"/>
            </td>
            <td style="width: 15%;"><label class="pull-right"><font color="red">*</font>承运商</label></td>
            <td style="width: 35%;">
                <sys:grid title="选择货主" url="${ctx}/sys/common/oms/customer/grid" cssClass="form-control required"
                          fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="${sysOmsCarrierServiceScopeEntity.carrierCode}"
                          displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${sysOmsCarrierServiceScopeEntity.carrierName}"
                          fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                          searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                          queryParams="ebcuType|dataSet" queryParamValues="carrierType|dataSet"/>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;"><label class="pull-right"><font color="red">*</font>业务服务范围编码</label></td>
            <td style="width: 35%;">
                <sys:grid title="选择业务服务范围" url="${ctx}/sys/common/oms/businessServiceScope/grid" cssClass="form-control required"
                          fieldId="groupCode" fieldName="groupCode" fieldKeyName="groupCode" fieldValue="${sysOmsCarrierServiceScopeEntity.groupCode}"
                          displayFieldId="groupName" displayFieldName="groupName" displayFieldKeyName="groupName" displayFieldValue="${sysOmsCarrierServiceScopeEntity.groupName}"
                          fieldLabels="业务服务范围编码|业务服务范围名称" fieldKeys="groupCode|groupName"
                          searchLabels="业务服务范围编码|业务服务范围名称" searchKeys="groupCode|groupName"
                          queryParams="dataSet" queryParamValues="dataSet"/>
            </td>
            <td style="width: 15%;"><label class="pull-right"><font color="red">*</font>数据套</label></td>
            <td style="width: 35%;">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysOmsCarrierServiceScopeEntity.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysOmsCarrierServiceScopeEntity.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;"><label class="pull-right">最大重量</label></td>
            <td style="width: 35%;">
                <form:input path="maxWeight" htmlEscape="false" class="form-control "/>
            </td>
            <td style="width: 15%;"><label class="pull-right">最大体积</label></td>
            <td style="width: 35%;">
                <form:input path="maxVolume" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td style="width: 15%;"><label class="pull-right">最大费用</label></td>
            <td style="width: 35%;">
                <form:input path="maxCost" htmlEscape="false" class="form-control "/>
            </td>
            <td style="width: 15%;"><label class="pull-right">备注信息</label></td>
            <td style="width: 35%;">
                <form:input path="remarks" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>