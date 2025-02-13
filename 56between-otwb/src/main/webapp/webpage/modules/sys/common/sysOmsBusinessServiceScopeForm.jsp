<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务服务范围管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            jp.loading();
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.alert(validate.msg);
                return false;
            }
            jp.post("${ctx}/sys/common/oms/businessServiceScope/save", $('#inputForm').serialize(), function (data) {
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
<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-md-12">
            <form:form id="inputForm" modelAttribute="sysOmsBusinessServiceScope" class="form-horizontal">
                <form:hidden path="id"/>
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-20"><label class="pull-right">编码</label></td>
                        <td class="width-80">
                            <form:input path="groupCode" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20"><label class="pull-right"><font color="red">*</font>名称</label></td>
                        <td class="width-80">
                            <form:input path="groupName" htmlEscape="false" class="form-control required"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20"><label class="pull-right"><font color="red">*</font>数据套</label></td>
                        <td class="width-80">
                            <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                                      fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysOmsBusinessServiceScope.dataSet}"
                                      displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysOmsBusinessServiceScope.dataSetName}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20"><label class="pull-right">备注信息</label></td>
                        <td class="width-80">
                            <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="256"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </form:form>
        </div>
    </div>
</div>
</body>
</html>