<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>循环级别管理</title>
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
                    jp.post("${ctx}/wms/basicdata/banQinCdWhCycle/save", $('#inputForm').serialize(), function (data) {
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
                $('#cycleCode').prop('readonly', true);
            }
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="banQinCdWhCycle" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <div class="form-group">
        <label class="col-sm-2 control-label"><font color="red">*</font>循环级别编码</label>
        <div class="col-sm-10">
            <form:input path="cycleCode" htmlEscape="false" class="form-control required" maxlength="32"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label"><font color="red">*</font>循环级别名称</label>
        <div class="col-sm-10">
            <form:input path="cycleName" htmlEscape="false" class="form-control required" maxlength="64"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">循环周期</label>
        <div class="col-sm-10">
            <form:input path="cycleLife" htmlEscape="false" class="form-control " onkeyup="value=value.replace(/[^\d]/g,'')"/>
        </div>
    </div>
</form:form>
</body>
</html>