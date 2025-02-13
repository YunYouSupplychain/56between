<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>网点拓扑图管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script type="text/javascript">
        var validateForm;
        var $tmOutletRelationTreeTable; // 父页面table表格id
        var $topIndex;//openDialog的 dialog index
        function doSubmit(treeTable, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if (validateForm.form()) {
                $tmOutletRelationTreeTable = treeTable;
                $topIndex = index;
                jp.loading();
                $("#inputForm").submit();
                return true;
            }
            return false;
        }

        $(document).ready(function () {
            if ($("#id").val().length <= 0) {
                $("#orgId").val(tmOrg.id);
            }
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    jp.post("${ctx}/tms/basic/tmOutletRelation/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            var current_id = data.body.entity.id;
                            var target = $tmOutletRelationTreeTable.get(current_id);
                            var old_parent_id = target.attr("pid") == undefined ? '1' : target.attr("pid");
                            var current_parent_id = data.body.entity.parentId;
                            var current_parent_ids = data.body.entity.parentIds;

                            if (old_parent_id == current_parent_id) {
                                if (current_parent_id == '0') {
                                    $tmOutletRelationTreeTable.refreshPoint(-1);
                                } else {
                                    $tmOutletRelationTreeTable.refreshPoint(current_parent_id);
                                }
                            } else {
                                $tmOutletRelationTreeTable.del(current_id);//刷新删除旧节点
                                $tmOutletRelationTreeTable.initParents(current_parent_ids, "0");
                            }
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
<body>
<form:form id="inputForm" modelAttribute="tmOutletRelation" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-15 active"><label class="pull-right"><font color="red">*</font>网点：</label></td>
            <td class="width-35">
                <input type="hidden" id="transportObjType" value="OUTLET"/>
                <sys:grid title="网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                          fieldId="code" fieldName="code" fieldKeyName="transportObjCode" fieldValue="${tmOutletRelation.code}"
                          displayFieldId="name" displayFieldName="name" displayFieldKeyName="transportObjName" displayFieldValue="${tmOutletRelation.name}"
                          fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                          searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                          queryParams="transportObjType|orgId" queryParamValues="transportObjType|orgId"
                          cssClass="form-control required"/>
            </td>
            <td class="width-15 active"><label class="pull-right">父级编号：</label></td>
            <td class="width-35">
                <sys:treeselect id="parent" name="parent.id" value="${tmOutletRelation.parent.id}"
                                labelName="parent.name" labelValue="${tmOutletRelation.parent.name}"
                                title="父级编号" url="/tms/basic/tmOutletRelation/treeData"
                                extId="${tmOutletRelation.id}" cssClass="form-control"/>
            </td>
        </tr>
        <tr>
			<td class="width-15 active"><label class="pull-right">备注信息：</label></td>
			<td colspan="3">
				<form:input path="remarks" htmlEscape="false" class="form-control " maxlength="255"/>
			</td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>