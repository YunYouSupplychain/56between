<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>数据套管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        var relationRowIdx = 0, relationTpl;
        $(document).ready(function () {
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    var disabledObjs = bq.openDisabled('#inputForm');
                    var params = $('#inputForm').serialize();
                    bq.closeDisabled(disabledObjs);
                    jp.post("${ctx}/sys/common/dataSet/save", params, function (data) {
                        if (data.success) {
                            $table.bootstrapTable('refresh');
                            jp.success(data.msg);
                            jp.close($topIndex);//关闭dialog
                        } else {
                            jp.error(data.msg);
                        }
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

            init();
        });

        function init() {
            if ($('#id').val()) {
                $('#code').prop('readonly', true);
            } else {
                $('#isDefault').val('N');
            }
            initRelation(${fns:toJson(sysCommonDataSetEntity.relationList)});
        }

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

        function orgAfterSelect(row, idx) {
            if (row) {
                $('#relationList' + idx + '_orgName').text(row.name);
            }
        }

        function initRelation(rows) {
            relationTpl = $("#relationTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
            $("#relationTable").find("input[name='btSelectAll']").on('click', function () {// 表格全选复选框绑定事件
                $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
            });
            if (rows === undefined || rows.length <= 0) return;
            $("#relationList").empty();

            relationRowIdx = 0;
            for (var i = 0; i < rows.length; i++) {
                addRelation(rows[i]);
            }
        }

        function addRelation(row) {
            if (row === undefined) {
                row = {recVer: 0};
            }
            addRow('#relationList', relationRowIdx, relationTpl, row);
            relationRowIdx = relationRowIdx + 1;
        }

        function delRelation() {
            delRow('#relationList', '${ctx}/sys/common/dataSetOrgRelation/deleteAll');
        }

        function addRow(list, idx, tpl, row) {
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            $(list + idx).find("input[type='checkbox']").each(function () {
                if (!$(this).val()) {
                    $(this).val('N');
                    return;
                }
                $(this).prop("checked", ("Y" === $(this).val()));
            });
            $(list + idx).find(".form_datetime").each(function () {
                $(this).datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            });
            $(list + idx).find("input[name='btSelectItem']").on('click', function () {
                var $table = $(this).parents("table").eq(0);
                $table.find("input[name='btSelectAll']").prop('checked', $table.find("input[name='btSelectItem']").not("input:checked").length <= 0);
            });
        }

        function delRow(list, url) {
            jp.confirm('确认要删除选中记录吗？', function () {
                jp.loading();
                var ids = [];// 获取选中行ID
                var idxs = [];// 获取选中行索引
                $.map($(list).find("tr input[type='checkbox']:checked"), function ($element) {
                    var idx = $($element).data("index");
                    idxs.push(idx);
                    var id = $(list + idx + "_id").val();
                    if (id) {
                        ids.push(id);
                    }
                });
                del = function (indexs) {// 页面表格删除
                    $.map(indexs, function (idx) {
                        $(list + idx).remove();
                    });
                    jp.success("操作成功");
                };
                if (url && ids.length > 0) {
                    jp.post(url, {ids: ids.join(',')}, function (data) {
                        if (data.success) {
                            del(idxs);
                        } else {
                            jp.error(data.msg);
                        }
                    });
                } else {
                    del(idxs);
                }
            });
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="sysCommonDataSetEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td style="width:10%;"><label class="pull-right"><font color="red">*</font>编码</label></td>
                        <td style="width:15%;">
                            <form:input path="code" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td style="width:10%;"><label class="pull-right"><font color="red">*</font>名称</label></td>
                        <td style="width:25%;">
                            <form:input path="name" htmlEscape="false" class="form-control required"/>
                        </td>
                        <td style="width:10%;"><label class="pull-right">是否默认</label></td>
                        <td style="width:15%;">
                            <form:select path="isDefault" cssClass="form-control" disabled="true">
                                <form:option value=""/>
                                <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
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
                <h3 class="panel-title">机构信息</h3>
            </div>
            <div class="panel-body">
                <div id="relationToolbar" style="padding-bottom: 5px;">
                    <shiro:hasPermission name="sys:common:dataSetOrgRelation:add">
                        <a id="relation_add" class="btn btn-primary" onclick="addRelation()"> 新增</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="sys:common:dataSetOrgRelation:del">
                        <a id="relation_remove" class="btn btn-danger" onclick="delRelation()"> 删除</a>
                    </shiro:hasPermission>
                </div>
                <table id="relationTable" class="table text-nowrap" data-toolbar="#relationToolbar">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th style="width:36px;vertical-align:middle">
                            <label><input type="checkbox" name="btSelectAll" style="width: 16px;height: 16px;"/></label>
                        </th>
                        <th style="width: 30%;">机构编码<font color="red">*</font></th>
                        <th>机构名称</th>
                    </tr>
                    </thead>
                    <tbody id="relationList"></tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="relationTpl">//<!--
    <tr id="relationList{{idx}}" data-index="{{idx}}">
        <td class="hide">
            <input type="hidden" id="relationList{{idx}}_id" name="relationList[{{idx}}].id" value="{{row.id}}"/>
            <input type="hidden" id="relationList{{idx}}_dataSet" name="relationList[{{idx}}].dataSet" value="{{row.dataSet}}"/>
            <input type="hidden" id="relationList{{idx}}_recVer" name="relationList[{{idx}}].recVer" value="{{row.recVer}}"/>
        </td>
        <td style="vertical-align: middle">
            <input type="checkbox" name="btSelectItem" data-index="{{idx}}" style="width: 16px;height: 16px;"/>
        </td>
        <td>
            <sys:grid title="选择机构" url="${ctx}/sys/office/companyData" cssClass="form-control required"
                      fieldId="relationList{{idx}}_orgId" fieldName="relationList[{{idx}}].orgId" fieldKeyName="id" fieldValue="{{row.orgId}}"
                      displayFieldId="relationList{{idx}}_orgCode" displayFieldName="relationList[{{idx}}].orgCode" displayFieldKeyName="code" displayFieldValue="{{row.orgCode}}"
                      fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                      searchLabels="机构编码|机构名称" searchKeys="code|name"
                      afterSelect="orgAfterSelect({{idx}})"/>
        </td>
        <td><span id="relationList{{idx}}_orgName" class="form-control">{{row.orgName}}</span></td>
    </tr>//-->
</script>
</body>
</html>