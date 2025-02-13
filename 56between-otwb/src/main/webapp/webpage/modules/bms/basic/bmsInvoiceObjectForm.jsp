<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>开票对象管理</title>
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
                    jp.post("${ctx}/bms/basic/bmsInvoiceObject/save", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.error(data.msg);
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

            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
            }
        });

        function addRow(list, idx, tpl, row) {
            if (!row) {
                tpl = tpl.replace("{{row.orgId}}", $('#orgId').val());
            }
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            $(list + idx).find("input[type='checkbox'], input[type='radio']").each(function () {
                var ss = $(this).attr("data-value").split(',');
                for (var i = 0; i < ss.length; i++) {
                    if ($(this).val() == ss[i]) {
                        $(this).attr("checked", "checked");
                    }
                }
            });
            $(list + idx).find(".form_datetime").each(function () {
                $(this).datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            });
        }

        function delRow(obj, prefix) {
            var id = $(prefix + "_id");
            var delFlag = $(prefix + "_delFlag");
            if (id.val() == "") {
                $(obj).parent().parent().remove();
            } else if (delFlag.val() == "0") {
                delFlag.val("1");
                $(obj).html("&divide;").attr("title", "撤销删除");
                $(obj).parent().parent().addClass("error");
            } else if (delFlag.val() == "1") {
                delFlag.val("0");
                $(obj).html("&times;").attr("title", "删除");
                $(obj).parent().parent().removeClass("error");
            }
        }

        function settleObjectAfterSelect(row, idx) {
            if (row) {
                $("#bmsInvoiceObjectDetailList" + idx + "_name").val(row.settleObjectName);
            } else {
                $("#bmsInvoiceObjectDetailList" + idx + "_name").val('');
            }
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="bmsInvoiceObject" action="${ctx}/bms/basic/bmsInvoiceObject/save" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">编码</label></td>
                        <td class="width-15">
                            <form:input path="code" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">名称</label></td>
                        <td class="width-15">
                            <form:input path="name" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right asterisk">负责人</label></td>
                        <td class="width-15">
                            <form:input path="principal" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td class="width-10"><label class="pull-right">开户行</label></td>
                        <td class="width-15">
                            <form:input path="bank" htmlEscape="false" class="form-control "/>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right">银行账户</label></td>
                        <td class="width-15">
                            <form:input path="bankAccount" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">电话</label></td>
                        <td class="width-15">
                            <form:input path="phone" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">地址</label></td>
                        <td class="width-15">
                            <form:input path="address" htmlEscape="false" class="form-control "/>
                        </td>
                        <td class="width-10"><label class="pull-right">备注信息</label></td>
                        <td colspan="3">
                            <form:input path="remarks" htmlEscape="false" class="form-control "/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">开票对象明细</h3>
            </div>
            <div class="panel-body">
                <a class="btn btn-white btn-sm"
                   onclick="addRow('#bmsInvoiceObjectDetailList', bmsInvoiceObjectDetailRowIdx, bmsInvoiceObjectDetailTpl);bmsInvoiceObjectDetailRowIdx = bmsInvoiceObjectDetailRowIdx + 1;"><i class="fa fa-plus"></i> 新增</a>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th>客户编码</th>
                        <th>客户名称</th>
                        <th width="10">&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody id="bmsInvoiceObjectDetailList"></tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="bmsInvoiceObjectDetailTpl">//<!--
<tr id="bmsInvoiceObjectDetailList{{idx}}">
    <td class="hide">
        <input id="bmsInvoiceObjectDetailList{{idx}}_id" name="bmsInvoiceObjectDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="bmsInvoiceObjectDetailList{{idx}}_delFlag" name="bmsInvoiceObjectDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="bmsInvoiceObjectDetailList{{idx}}_orgId" name="bmsInvoiceObjectDetailList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
    </td>
    <td>
        <sys:grid title="选择客户" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control required"
                  fieldId="" fieldName="" fieldKeyName=""
                  displayFieldId="bmsInvoiceObjectDetailList{{idx}}_code" displayFieldName="bmsInvoiceObjectDetailList[{{idx}}].code"
                  displayFieldKeyName="settleObjectCode" displayFieldValue="{{row.code}}"
                  fieldLabels="结算对象代码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                  searchLabels="结算对象代码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                  queryParams="orgId" queryParamValues="orgId"
                  afterSelect="settleObjectAfterSelect({{idx}})"/>
    </td>
    <td>
        <input id="bmsInvoiceObjectDetailList{{idx}}_name" readonly name="bmsInvoiceObjectDetailList[{{idx}}].name" type="text" value="{{row.name}}"    class="form-control "/>
    </td>
    <td class="text-center" width="10">
        {{#delBtn}}<span class="close" onclick="delRow(this, '#bmsInvoiceObjectDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
    </td>
</tr>//-->
</script>
<script type="text/javascript">
    var bmsInvoiceObjectDetailRowIdx = 0,
        bmsInvoiceObjectDetailTpl = $("#bmsInvoiceObjectDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $(document).ready(function () {
        var data = ${fns:toJson(bmsInvoiceObject.bmsInvoiceObjectDetailList)};
        for (var i = 0; i < data.length; i++) {
            addRow('#bmsInvoiceObjectDetailList', bmsInvoiceObjectDetailRowIdx, bmsInvoiceObjectDetailTpl, data[i]);
            bmsInvoiceObjectDetailRowIdx = bmsInvoiceObjectDetailRowIdx + 1;
        }
    });
</script>
</body>
</html>