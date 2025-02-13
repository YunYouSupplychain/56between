<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>角色管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/treeview.jsp" %>
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
            $("#name").focus();

            validateForm = $("#inputForm").validate({
                rules: {
                    name: {remote: "${ctx}/sys/role/checkName?oldName=" + encodeURIComponent("${role.name}")},//设置了远程验证，在初始化时必须预先调用一次。
                    enname: {remote: "${ctx}/sys/role/checkEnname?oldEnname=" + encodeURIComponent("${role.enname}")}
                },
                messages: {
                    name: {remote: "角色名已存在"},
                    enname: {remote: "英文名已存在"}
                },
                submitHandler: function (form) {
                    jp.post("${ctx}/sys/role/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
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
<form:form id="inputForm" modelAttribute="role" autocomplete="off" action="${ctx}/sys/role/save" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="menuIds"/>
    <sys:message content="${message}"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-15"><label class="pull-right">归属机构</label></td>
            <td class="width-35"><sys:treeselect id="office" name="office.id" value="${role.office.id}" labelName="office.name" labelValue="${role.office.name}" title="机构" url="/sys/office/treeData" cssClass="form-control "/></td>
            <td class="width-15"><label class="pull-right"><font color="red">*</font>角色名称</label></td>
            <td class="width-35"><input id="oldName" name="oldName" type="hidden" value="${role.name}"><form:input path="name" htmlEscape="false" maxlength="50" class="form-control required"/></td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right"><font color="red">*</font>英文名称:</label></td>
            <td class="width-35"><input id="oldEnname" name="oldEnname" type="hidden" value="${role.enname}"><form:input path="enname" htmlEscape="false" maxlength="50" class="form-control required"/>
            </td>
            <td class="width-15"><label class="pull-right">角色类型:</label></td>
            <td class="width-35">
                <form:select path="roleType" class="form-control">
                    <form:option value="assignment">任务分配</form:option>
                    <form:option value="security-role">管理角色</form:option>
                    <form:option value="user">普通角色</form:option>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right">是否系统数据:</label></td>
            <td class="width-35">
                <form:select path="sysData" class="form-control ">
                    <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
                <span class="help-inline">“是”代表此数据只有超级管理员能进行修改，“否”则表示拥有角色修改人员的权限都能进行修改</span>
            </td>
            <td class="width-15"><label class="pull-right">是否可用</label></td>
            <td class="width-35">
                <form:select path="useable" class="form-control ">
                    <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
                <span class="help-inline">“是”代表此数据可用，“否”则表示此数据不可用</span>
            </td>
        </tr>
        <tr>
            <td class="width-15"><label class="pull-right">备注:</label></td>
            <td class="width-35" colspan="3"><form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control" cssStyle="resize: none;"/></td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>