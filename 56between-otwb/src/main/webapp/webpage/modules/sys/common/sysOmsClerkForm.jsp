<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务员管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            var validate = bq.headerSubmitCheck("#inputForm");
            if (validate.isSuccess) {
                $table = table;
                $topIndex = index;
                jp.loading();
                var disabledObjs = bq.openDisabled("#inputForm");
                var params = $('#inputForm').serialize();
                bq.closeDisabled(disabledObjs);
                jp.post("${ctx}/sys/common/oms/clerk/save", params, function (data) {
                    if (data.success) {
                        $table.bootstrapTable('refresh');
                        jp.success(data.msg);
                        jp.close($topIndex);//关闭dialog
                    } else {
                        jp.bqError(data.msg);
                    }
                });
                return true;
            } else {
                jp.bqError(validate.msg);
            }
            return false;
        }

        $(document).ready(function () {
            if (!$("#id").val()) {
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            } else {
                $('#clerkCode').prop('readonly', true);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        });
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysOmsClerk" class="form-horizontal">
    <form:hidden path="id"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>业务员代码</label></td>
            <td class="width-80">
                <form:input path="clerkCode" htmlEscape="false" class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>业务员名称</label></td>
            <td class="width-80">
                <form:input path="clerkName" htmlEscape="false" class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right">联系电话</label></td>
            <td class="width-80">
                <form:input path="phone" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right">数据套</label></td>
            <td class="width-80">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysOmsClerk.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysOmsClerk.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>