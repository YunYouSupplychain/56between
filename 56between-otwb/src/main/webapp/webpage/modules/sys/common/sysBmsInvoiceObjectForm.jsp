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
                    jp.post("${ctx}/sys/common/bms/invoiceObject/save", $('#inputForm').serialize(), function (data) {
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
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        });

        function addRow(list, idx, tpl, row) {
            if (!row) {
                tpl = tpl.replace("{{row.dataSet}}", $('#dataSet').val());
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
                    if ($(this).val() === ss[i]) {
                        $(this).attr("checked", "checked");
                    }
                }
            });
            $(list + idx).find(".form_datetime").each(function () {
                $(this).datetimepicker({
                    format: "YYYY-MM-DD HH:mm:ss"
                });
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

        function customerSelect(idx) {
            var dataSet = $('#dataSet').val();
            top.layer.open({
                type: 2,
                area: ['800px', '500px'],
                title: "选择客户",
                auto: true,
                name: 'friend',
                content: "${ctx}/tag/grid?url=" + encodeURIComponent("${ctx}/sys/common/bms/settleObject/grid?dataSet=" + dataSet)
                    + "&fieldLabels=" + encodeURIComponent("客户编码|客户名称")
                    + "&fieldKeys=" + encodeURIComponent("settleObjectCode|settleObjectName")
                    + "&searchLabels=" + encodeURIComponent("客户编码|客户名称")
                    + "&searchKeys=" + encodeURIComponent("settleObjectCode|settleObjectName") + "&isMultiSelected=false",
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow; // 得到iframe页的窗口对象，执行iframe页的方法：iframeWin.method();
                    var items = iframeWin.getSelections();
                    if (items == "") {
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    $("#sysBmsInvoiceObjectDetailList" + idx + "_code").val(items[0].settleObjectCode);
                    $("#sysBmsInvoiceObjectDetailList" + idx + "_name").val(items[0].settleObjectName);
                    top.layer.close(index);// 关闭对话框。
                },
                cancel: function (index) {
                }
            });
        }

        function customerDelete(idx) {
            $("#sysBmsInvoiceObjectDetailList" + idx + "_code").val('');
            $("#sysBmsInvoiceObjectDetailList" + idx + "_name").val('');
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysBmsInvoiceObject" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
        <tr>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>编码：</label></td>
            <td class="width-12">
                <form:input path="code" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>名称：</label></td>
            <td class="width-12">
                <form:input path="name" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>负责人：</label></td>
            <td class="width-12">
                <form:input path="principal" htmlEscape="false" class="form-control required"/>
            </td>
            <td class="width-8"><label class="pull-right">电话：</label></td>
            <td class="width-12">
                <form:input path="phone" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-8"><label class="pull-right"><font color="red">*</font>数据套：</label></td>
            <td class="width-12">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysBmsInvoiceObject.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysBmsInvoiceObject.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        <tr>
            <td class="width-8"><label class="pull-right">开户行：</label></td>
            <td class="width-12">
                <form:input path="bank" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-8"><label class="pull-right">银行账户：</label></td>
            <td class="width-12">
                <form:input path="bankAccount" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-8"><label class="pull-right">地址：</label></td>
            <td class="width-12">
                <form:input path="address" htmlEscape="false" class="form-control "/>
            </td>
            <td class="width-8"><label class="pull-right">备注信息：</label></td>
            <td colspan="3">
                <form:input path="remarks" htmlEscape="false" class="form-control "/>
            </td>
        </tr>
        </tbody>
    </table>
    <div class="tabs-container">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">开票对象明细：</a>
            </li>
        </ul>
        <div class="tab-content">
            <div id="tab-1" class="tab-pane fade in  active">
                <a class="btn btn-white btn-sm"
                   onclick="addRow('#sysBmsInvoiceObjectDetailList', sysBmsInvoiceObjectDetailRowIdx, sysBmsInvoiceObjectDetailTpl);sysBmsInvoiceObjectDetailRowIdx = sysBmsInvoiceObjectDetailRowIdx + 1;"
                   title="新增"><i class="fa fa-plus"></i> 新增</a>
                <table class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th>客户编码</th>
                        <th>客户名称</th>
                        <th width="10">&nbsp;</th>
                    </tr>
                    </thead>
                    <tbody id="sysBmsInvoiceObjectDetailList">
                    </tbody>
                </table>
                <script type="text/template" id="sysBmsInvoiceObjectDetailTpl">//<!--
				<tr id="sysBmsInvoiceObjectDetailList{{idx}}">
					<td class="hide">
						<input id="sysBmsInvoiceObjectDetailList{{idx}}_id" name="sysBmsInvoiceObjectDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
						<input id="sysBmsInvoiceObjectDetailList{{idx}}_delFlag" name="sysBmsInvoiceObjectDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
						<input id="sysBmsInvoiceObjectDetailList{{idx}}_dataSet" name="sysBmsInvoiceObjectDetailList[{{idx}}].dataSet" type="hidden" value="{{row.dataSet}}"/>
					</td>
					<td>
						<div class="input-group" style="width: 100%">
	                    	<input id="sysBmsInvoiceObjectDetailList{{idx}}_code" readonly name="sysBmsInvoiceObjectDetailList[{{idx}}].code" value="{{row.code}}" data-msg-required="" class="form-control required width:0px" style="" aria-required="true" type="text">
	                    	<span class="input-group-btn">
	                    		<button type="button" id="customerSelectButton" onclick="customerSelect({{idx}})" class="btn btn-primary"><i class="fa fa-search"></i></button>
	                    		<button type="button" id="customerDelButton" onclick="customerDelete({{idx}})" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
	                    	</span>
                        </div>
					</td>

					<td>
						<input id="sysBmsInvoiceObjectDetailList{{idx}}_name" readonly name="sysBmsInvoiceObjectDetailList[{{idx}}].name" type="text" value="{{row.name}}"    class="form-control "/>
					</td>
					
					<td class="text-center" width="10">
						{{#delBtn}}<span class="close" onclick="delRow(this, '#sysBmsInvoiceObjectDetailList{{idx}}')" title="删除">&times;</span>{{/delBtn}}
					</td>
				</tr>//-->
                </script>
                <script type="text/javascript">
                    var sysBmsInvoiceObjectDetailRowIdx = 0,
                        sysBmsInvoiceObjectDetailTpl = $("#sysBmsInvoiceObjectDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
                    $(document).ready(function () {
                        var data = ${fns:toJson(sysBmsInvoiceObject.sysBmsInvoiceObjectDetailList)};
                        for (var i = 0; i < data.length; i++) {
                            addRow('#sysBmsInvoiceObjectDetailList', sysBmsInvoiceObjectDetailRowIdx, sysBmsInvoiceObjectDetailTpl, data[i]);
                            sysBmsInvoiceObjectDetailRowIdx = sysBmsInvoiceObjectDetailRowIdx + 1;
                        }
                    });
                </script>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>