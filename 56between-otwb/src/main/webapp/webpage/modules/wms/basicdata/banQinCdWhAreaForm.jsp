<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>区域管理</title>
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
                    jp.post("${ctx}/wms/basicdata/banQinCdWhArea/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.bqError(data.msg);
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
                $('#orgId').val(jp.getCurrentOrg().orgId);
            } else {
                $('#areaCode').prop('readonly', true);
            }
        }
    </script>
</head>
<body class="bg-white">
    <form:form id="inputForm" modelAttribute="banQinCdWhArea" method="post" class="form-horizontal">
        <form:hidden path="id"/>
        <form:hidden path="recVer"/>
        <form:hidden path="orgId"/>
        <table class="table">
            <tbody>
            <tr>
                <td width="20%"><label class="pull-right"><font color="red">*</font>区域编码</label></td>
                <td width="80%">
                    <form:input path="areaCode" htmlEscape="false" class="form-control required" maxlength="32"/>
                </td>
            </tr>
            <tr>
                <td width="20%"><label class="pull-right"><font color="red">*</font>区域名称</label></td>
                <td width="80%">
                    <form:input path="areaName" htmlEscape="false" class="form-control required" maxlength="32"/>
                </td>
            </tr>
            <tr>
                <td width="20%"><label class="pull-right">备注</label></td>
                <td width="80%">
                    <form:input path="remarks" htmlEscape="false" class="form-control" maxlength="255" cssStyle="resize: none;"/>
                </td>
            </tr>
            </tbody>
        </table>
    </form:form>
</body>
</html>