<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>菜单管理</title>
    <meta name="decorator" content="ani"/>
    <style>
        .myRadios span {
            padding-right: 10px;
        }
        .myRadios span label {
            padding-left: 5px;
        }
    </style>
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
                submitHandler: function (form) {// 必须写在验证前面，否则无法ajax提交
                    jp.loading();
                    jp.post("${ctx}/sys/menu/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            var current_id = data.body.menu.id;
                            var target = $treeTable.get(current_id);
                            var old_parent_id = target.attr("pid") == undefined ? '1' : target.attr("pid");
                            var current_parent_id = data.body.menu.parentId;
                            var current_parent_ids = data.body.menu.parentIds;
                            if (old_parent_id == current_parent_id) {
                                if (current_parent_id == '1') {
                                    $treeTable.refreshPoint(-1);
                                } else {
                                    $treeTable.refreshPoint(current_parent_id);
                                }
                            } else {
                                $treeTable.del(current_id);//刷新删除旧节点
                                $treeTable.initParents(current_parent_ids, "1");
                            }
                            jp.success(data.msg);
                        } else {
                            jp.error(data.msg);
                        }
                        jp.close($topIndex);//关闭dialog
                    });
                },
                errorPlacement: function (error, element) {// 自定义提示错误位置
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
<form:form id="inputForm" modelAttribute="menu" action="${ctx}/sys/menu/save" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-15"><label class="pull-right"><font color="red">*</font> 上级菜单</label></td>
            <td class="width-35"><sys:treeselect id="menu" name="parent.id" value="${menu.parent.id}" labelName="parent.name" labelValue="${menu.parent.name}" title="菜单" url="/sys/menu/treeData" extId="${menu.id}" allowSearch="true" cssClass="form-control required"/></td>
            <td class="width-15"><label class="pull-right"><font color="red">*</font> 名称</label></td>
            <td class="width-35"><form:input path="name" htmlEscape="false" maxlength="50" class="required form-control "/></td>
        </tr>
        <tr>
            <td class=""><label class="pull-right">链接:</label></td>
            <td><form:input path="href" htmlEscape="false" maxlength="2000" class="form-control "/><span class="help-inline">点击菜单跳转的页面</span></td>
            <td class=""><label class="pull-right">目标:</label></td>
            <td><form:input path="target" htmlEscape="false" maxlength="10" class="form-control "/><span class="help-inline">链接的目标窗口(mainFrame、_blank、_self、_parent、_top)</span></td>
        </tr>
        <tr>
            <td class=""><label class="pull-right">图标</label></td>
            <td><sys:iconselect id="icon" name="icon" value="${menu.icon}"/></td>
            <td class=""><label class="pull-right">菜单类型</label></td>
            <td class="myRadios"><form:radiobuttons path="type" items="${fns:getDictList('menu_type')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required i-checks "/><span class="help-inline"></span></td>
        </tr>
        <tr>
            <td class=""><label class="pull-right">可见</label></td>
            <td class="myRadios"><form:radiobuttons path="isShow" items="${fns:getDictList('show_hide')}" itemLabel="label" itemValue="value" htmlEscape="false" class="required i-checks "/><span class="help-inline">该菜单或操作是否显示到系统菜单中</span></td>
            <td class=""><label class="pull-right">权限标识</label></td>
            <td><form:input path="permission" htmlEscape="false" maxlength="100" class="form-control "/><span class="help-inline">如：@Requires Permissions("权限标识")</span></td>
        </tr>
        <tr>
            <td class=""><label class="pull-right">备注</label></td>
            <td colspan="3"><form:textarea path="remarks" htmlEscape="false" rows="1" maxlength="200" class="form-control" cssStyle="resize: none;"/></td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>