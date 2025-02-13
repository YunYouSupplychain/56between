<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库区管理</title>
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
                    jp.post("${ctx}/wms/basicdata/banQinCdWhZone/save", $('#inputForm').serialize(), function (data) {
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
                $('#zoneCode').prop('readonly', true);
            }
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="banQinCdWhZone" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <div class="form-group">
        <label class="col-sm-2 control-label"><font color="red">*</font>库区编码</label>
        <div class="col-sm-10">
            <form:input path="zoneCode" htmlEscape="false" class="form-control required" maxlength="32"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label"><font color="red">*</font>库区名称</label>
        <div class="col-sm-10">
            <form:input path="zoneName" htmlEscape="false" class="form-control required" maxlength="64"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label"><font color="red">*</font>库区类型</label>
        <div class="col-sm-10">
            <form:select path="type" class="form-control m-b required">
                <form:option value="" label=""/>
                <form:options items="${fns:getDictList('SYS_WM_ZONE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
            </form:select>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label"><font color="red">*</font>所属区域</label>
        <div class="col-sm-10">
            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhArea/grid" title="选择区域" cssClass="form-control required"
                           fieldId="areaCode" fieldName="areaCode" fieldKeyName="areaCode" fieldValue="${banQinCdWhZone.areaCode}" allowInput="true"
                           displayFieldId="areaName" displayFieldName="areaName" displayFieldKeyName="areaName" displayFieldValue="${banQinCdWhZone.areaName}"
                           selectButtonId="areaSelectId" deleteButtonId="areaDeleteId"
                           fieldLabels="区域编码|区域名称" fieldKeys="areaCode|areaName"
                           searchLabels="区域编码|区域名称" searchKeys="areaCode|areaName" inputSearchKey="areaCodeAndName">
            </sys:popSelect>
        </div>
    </div>
    <div class="form-group">
        <label class="col-sm-2 control-label">备注</label>
        <div class="col-sm-10">
            <form:textarea path="remarks" htmlEscape="false" rows="1" class="form-control" cssStyle="resize: none;"/>
        </div>
    </div>
</form:form>
</body>
</html>