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
                    jp.post("${ctx}/wms/weigh/banQinWeighMachineInfo/save", $('#inputForm').serialize(), function (data) {
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
        });
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="banQinWeighMachineInfoEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-20"><label class="pull-right">设备编码</label></td>
            <td class="width-80">
                <form:input path="machineNo" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <tr>
            <td class="width-20"><label class="pull-right">机构</label></td>
            <td class="width-80">
                <input type="hidden" id="currentOrgId">
                <sys:popSelect url="${ctx}/sys/office/companyData" title="选择机构" cssClass="form-control"
                               fieldId="orgId" fieldName="orgId" fieldKeyName="id" fieldValue="${banQinWeighMachineInfoEntity.orgId}"
                               displayFieldId="orgName" displayFieldName="orgName" displayFieldKeyName="name" displayFieldValue="${banQinWeighMachineInfoEntity.orgName}"
                               selectButtonId="orgSelectButton" deleteButtonId="orgDelButton"
                               fieldLabels="仓别编码|仓别名称" fieldKeys="code|name"
                               searchLabels="仓别编码|仓别名称" searchKeys="code|name"
                               queryParams="id" queryParamValues="currentOrgId">
                </sys:popSelect>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>