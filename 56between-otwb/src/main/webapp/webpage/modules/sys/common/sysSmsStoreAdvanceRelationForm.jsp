<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>门店垫资方关联关系管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if (validateForm.form()) {
                $table = table;
                $topIndex = index;
                jp.loading();
                $("#inputForm").submit();
                return true;
            }
            return false;
        }

        $(document).ready(function () {
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.post("${ctx}/sys/common/sms/storeAdvanceRelation/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.error(data.msg);
                        }
                    })
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            if (!$('#id').val()) {
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
<div class="hide">
    <input id="storeType" type="hidden" value="CONSIGNEE"/>
    <input id="advanceType" type="hidden" value="ADVANCE"/>
</div>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysSmsStoreAdvanceRelationEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="oldStoreCode"/>
    <form:hidden path="oldAdvanceCode"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>门店</label></td>
            <td class="width-80">
                <sys:grid title="选择门店" url="${ctx}/sys/common/sms/customer/grid" cssClass="form-control required"
                          fieldId="storeCode" fieldName="storeCode" fieldKeyName="code" fieldValue="${sysSmsStoreAdvanceRelationEntity.storeCode}"
                          displayFieldId="storeName" displayFieldName="storeName" displayFieldKeyName="nameCn" displayFieldValue="${sysSmsStoreAdvanceRelationEntity.storeName}"
                          fieldLabels="门店编码|门店名称" fieldKeys="code|nameCn"
                          searchLabels="门店编码|门店名称" searchKeys="code|nameCn"
                          queryParams="dataSet|customerType" queryParamValues="dataSet|storeType"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>垫资方</label></td>
            <td class="width-80">
                <sys:grid title="选择垫资方" url="${ctx}/sys/common/sms/customer/grid" cssClass="form-control required"
                          fieldId="advanceCode" fieldName="advanceCode" fieldKeyName="code" fieldValue="${sysSmsStoreAdvanceRelationEntity.advanceCode}"
                          displayFieldId="advanceName" displayFieldName="advanceName" displayFieldKeyName="nameCn" displayFieldValue="${sysSmsStoreAdvanceRelationEntity.advanceName}"
                          fieldLabels="垫资方编码|垫资方名称" fieldKeys="code|nameCn"
                          searchLabels="垫资方编码|垫资方名称" searchKeys="code|nameCn"
                          queryParams="dataSet|customerType" queryParamValues="dataSet|advanceType"/>
            </td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right"><font color="red">*</font>数据套</label></td>
            <td class="width-20">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysSmsStoreAdvanceRelationEntity.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysSmsStoreAdvanceRelationEntity.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>