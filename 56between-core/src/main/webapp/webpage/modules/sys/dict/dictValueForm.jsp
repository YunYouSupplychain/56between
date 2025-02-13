<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>字典管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if (validateForm.form()) {
                $table = table;
                $topIndex = index;
                $("#inputForm").submit();
                return true;
            }
            return false;
        }

        $(document).ready(function () {
            $("#label").focus();
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.loading();
                    jp.post("${ctx}/sys/dict/saveDictValue", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                        } else {
                            jp.error(data.msg);
                        }
                        jp.close($topIndex);//关闭dialog
                    });
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
<form:form id="inputForm" modelAttribute="dictValue" method="post" class="form-horizontal">
    <input type="hidden" name="dictValueId" value="${dictValue.id }"/>
    <form:hidden path="dictType.id"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-10"><label class="pull-right"><font color="red">*</font>标签</label></td>
            <td class="width-35"><form:input path="label" htmlEscape="false" maxlength="50" class="form-control required"/></td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right"><font color="red">*</font>键值</label></td>
            <td class="width-35"><form:input path="value" htmlEscape="false" maxlength="50" class="form-control required"/></td>
        </tr>
        <tr>
            <td class="width-10"><label class="pull-right"><font color="red">*</font>排序</label></td>
            <td class="width-35"><form:input path="sort" htmlEscape="false" maxlength="50" class="form-control required"/></td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>