<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>用户管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var validateForm;
        var $treeTable; // 父页面table表格id
        var $topIndex;//openDialog的 dialog index
        function doSubmit(treeTable, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if (validateForm.form()) {
                $treeTable = treeTable;
                $topIndex = index;
                $("#inputForm").submit();
                return true;
            }
            return false;
        }

        $(document).ready(function () {
            $("#name").focus();
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.loading();
                    jp.post("${ctx}/sys/area/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            var current_id = data.body.area.id;
                            var target = $treeTable.get(current_id);
                            var old_parent_id = target.attr("pid") == undefined ? '0' : target.attr("pid");
                            var current_parent_id = data.body.area.parentId;
                            var current_parent_ids = data.body.area.parentIds;
                            if (old_parent_id == current_parent_id) {
                                if (current_parent_id == '0') {
                                    $treeTable.refreshPoint(-1);
                                } else {
                                    $treeTable.refreshPoint(current_parent_id);
                                }
                            } else {
                                $treeTable.del(current_id);//刷新删除旧节点
                                $treeTable.initParents(current_parent_ids, "0");
                            }
                            jp.success(data.msg);
                        } else {
                            jp.error(data.msg);
                        }
                        jp.close($topIndex);//关闭dialog
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
<form:form id="inputForm" modelAttribute="area" action="${ctx}/sys/area/save" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="remarks"/>
    <sys:message content="${message}"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-15"><label class="pull-right">上级区域</label></td>
            <td class="width-35">
                <sys:treeselect id="area" name="parent.id" value="${area.parent.id}"
                                labelName="parent.name" labelValue="${area.parent.name}"
                                title="区域" url="/sys/area/treeData" extId="${area.id}" cssClass="form-control m-s"/>
            </td>
            <td class="width-15"><label class="pull-right">区域名称</label></td>
            <td class="width-35">
                <form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right"><font color="red">*</font>区域编码</label></td>
            <td class="width-35">
                <form:input path="code" htmlEscape="false" maxlength="50" class="form-control required"/>
            </td>
            <td class="width-15"><label class="pull-right">区域类型</label></td>
            <td class="width-35">
                <form:select path="type" class="form-control">
                    <form:options items="${fns:getDictList('sys_area_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>