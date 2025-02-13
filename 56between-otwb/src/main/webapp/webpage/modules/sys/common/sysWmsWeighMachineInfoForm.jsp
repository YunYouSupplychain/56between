<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>称重设备表管理</title>
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
                    jp.post("${ctx}/sys/common/wms/weighMachineInfo/save", $('#inputForm').serialize(), function (data) {
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

            init();
        });

        function init() {
            if (!$('#id').val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            } else {
                $('#machineNo').prop('readonly', true);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysWmsWeighMachineInfoEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right">设备编码：</label></td>
            <td class="width-35">
                <form:input path="machineNo" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-15 active"><label class="pull-right">机构：</label></td>
            <td class="width-35">
                <sys:popSelect url="${ctx}/sys/office/companyData" title="选择机构" cssClass="form-control"
                               fieldId="orgId" fieldName="orgId" fieldKeyName="id" fieldValue="${sysWmsWeighMachineInfoEntity.orgId}"
                               displayFieldId="orgName" displayFieldName="orgName" displayFieldKeyName="name" displayFieldValue="${sysWmsWeighMachineInfoEntity.orgName}"
                               selectButtonId="orgSelectButton" deleteButtonId="orgDelButton"
                               fieldLabels="仓别编码|仓别名称" fieldKeys="code|name"
                               searchLabels="仓别编码|仓别名称" searchKeys="code|name">
                </sys:popSelect>
            </td>
        </tr>
        <tr>
            <td class="width-15 active"><label class="pull-right">数据套：</label></td>
            <td class="width-35">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysWmsWeighMachineInfoEntity.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysWmsWeighMachineInfoEntity.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
            <td class="width-15 active"></td>
            <td class="width-35"></td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>