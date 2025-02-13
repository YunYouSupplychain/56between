<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>批次属性管理</title>
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
            // 验证头部表单
            validateHeaderForm();
            // 初始化
            init();
        });

        /**
         * 验证头部表单
         */
        function validateHeaderForm() {
            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    bq.openDisabled("#inputForm");
                    jp.post("${ctx}/wms/basicdata/banQinCdWhLotHeader/save", $('#inputForm').serialize(), function (data) {
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
        }

        /**
         * 初始化
         */
        function init() {
            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/initialList", function (data) {
                    for (var i = 0; i < data.length; i++) {
                        addRow('#cdWhLotDetailList', i, cdWhLotDetailTpl, data[i])
                    }
                });
            } else {
                $("#lotCode").attr("readOnly", true);
                var params = "headerId=" + $('#id').val();
                params += "&orgId=" + $('#orgId').val();
                $.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/data?" + params, function (data) {
                    for (var i = 0; i < data.length; i++) {
                        addRow('#cdWhLotDetailList', i, cdWhLotDetailTpl, data[i])
                    }
                });
            }
        }

        /**
         * 添加明细
         * @param list
         * @param idx
         * @param tpl
         * @param row
         */
        function addRow(list, idx, tpl, row) {
            if (!row.id) {
                tpl = tpl.replace("{{row.orgId}}", $('#orgId').val());
            }
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            $(list + idx).find("input[type='text'], select").each(function () {
                if (idx <= 3 && ($(this).prop('name').indexOf('title') !== -1 || $(this).prop('name').indexOf('fieldType') !== -1 || $(this).prop('name').indexOf('key') !== -1)) {
                    $(this).prop('disabled', true);
                }
            });
        }

    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="banQinCdWhLotHeader" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
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
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>批次属性编码</label></td>
                        <td class="" width="12%">
                            <form:input path="lotCode" htmlEscape="false" class="form-control required" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>批次属性名称</label></td>
                        <td class="" width="12%">
                            <form:input path="lotName" htmlEscape="false" class="form-control required" maxlength="64"/>
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
                <h3 class="panel-title">批次属性明细</h3>
            </div>
            <div class="panel-body">
                <table id="cdWhLotDetailTable" class="table">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th>批次标签</th>
                        <th>输入控制</th>
                        <th>属性格式</th>
                        <th>属性选项</th>
                    </tr>
                    </thead>
                    <tbody id="cdWhLotDetailList">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="cdWhLotDetailTpl">//<!--
<tr id="cdWhLotDetailList{{idx}}">
    <td class="hide">
        <input id="cdWhLotDetailList{{idx}}_id" name="cdWhLotDetailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="cdWhLotDetailList{{idx}}_delFlag" name="cdWhLotDetailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="cdWhLotDetailList{{idx}}_orgId" name="cdWhLotDetailList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="cdWhLotDetailList{{idx}}_recVer" name="cdWhLotDetailList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
        <input id="cdWhLotDetailList{{idx}}_lotCode" name="cdWhLotDetailList[{{idx}}].lotCode" type="hidden" value="{{row.lotCode}}"/>
        <input id="cdWhLotDetailList{{idx}}_lotAtt" name="cdWhLotDetailList[{{idx}}].lotAtt" type="hidden" value="{{row.lotAtt}}"/>
    </td>
    <td>
        <input id="cdWhLotDetailList{{idx}}_title" name="cdWhLotDetailList[{{idx}}].title" type="text" value="{{row.title}}" class="form-control required" maxlength="64"/>
    </td>
    <td>
        <select id="cdWhLotDetailList{{idx}}_inputControl" name="cdWhLotDetailList[{{idx}}].inputControl" data-value="{{row.inputControl}}" class="form-control m-b required">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('SYS_WM_INPUT_CONTROL')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <select id="cdWhLotDetailList{{idx}}_fieldType" name="cdWhLotDetailList[{{idx}}].fieldType" data-value="{{row.fieldType}}" class="form-control m-b">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('SYS_WM_FIELD_TYPE')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <input id="cdWhLotDetailList{{idx}}_key" name="cdWhLotDetailList[{{idx}}].key" type="text" value="{{row.key}}" class="form-control" maxlength="32"/>
    </td>
</tr>//-->
</script>
<script type="text/javascript">
    var cdWhLotDetailTpl = $("#cdWhLotDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
</script>
</body>
</html>