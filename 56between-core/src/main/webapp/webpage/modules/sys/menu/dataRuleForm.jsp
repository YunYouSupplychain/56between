<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>数据权限管理</title>
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
                    jp.post("${ctx}/sys/dataRule/save", $('#inputForm').serialize(), function (data) {
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
<div class="row">
    <div class="col-md-12">
        <div class="panel panel-primary">
            <div class="panel-body">
                <form:form id="inputForm" modelAttribute="dataRule" action="${ctx}/sys/dataRule/save" method="post" class="form-horizontal">
                    <form:hidden path="id"/>
                    <form:hidden path="menuId"/>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">数据规则名称</label>
                        <div class="col-sm-10">
                            <form:input path="name" htmlEscape="false" class="form-control" maxlength="32"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">规则实体类</label>
                        <div class="col-sm-10">
                            <form:input path="className" htmlEscape="false" class="form-control" maxlength="128"/>
                            <span class="help-inline">请输入要进行数据过滤的的实体类名(不含包名,例如User),将对findList和findAllList进行数据过滤。</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">规则字段</label>
                        <div class="col-sm-10">
                            <form:input path="field" htmlEscape="false" class="form-control" maxlength="64"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">规则条件</label>
                        <div class="col-sm-10">
                            <form:select path="express" class="form-control ">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('data_rule_express')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">规则值</label>
                        <div class="col-sm-10">
                            <form:input path="value" htmlEscape="false" class="form-control" maxlength="64"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">自定义sql</label>
                        <div class="col-sm-10">
                            <form:textarea path="sqlSegment" htmlEscape="false" rows="4" maxlength="1256" class="form-control" cssStyle="resize: none;"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">备注信息</label>
                        <div class="col-sm-10">
                            <form:textarea path="remarks" htmlEscape="false" rows="1" maxlength="255" class="form-control" cssStyle="resize: none;"/>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</div>
</body>
</html>