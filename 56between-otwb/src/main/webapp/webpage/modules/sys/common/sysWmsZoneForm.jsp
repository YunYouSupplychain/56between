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
                    jp.post("${ctx}/sys/common/wms/zone/save", $('#inputForm').serialize(), function (data) {
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
                var $dataSet = ${fns:toJson(fns:getUserDataSet())};
                $('#dataSet').val($dataSet.code);
                $('#dataSetName').val($dataSet.name);
            } else {
                $('#zoneCode').prop('readonly', true);
            }
            $('#dataSetName').prop('readonly', true);
            $('#dataSetNameSBtnId').prop('disabled', true);
            $('#dataSetNameDBtnId').prop('disabled', true);
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="sysWmsZone" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>库区编码</label></td>
            <td class="width-80">
                <form:input path="zoneCode" htmlEscape="false" class="form-control required" maxlength="32"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>库区名称</label></td>
            <td class="width-80">
                <form:input path="zoneName" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>库区类型</label></td>
            <td class="width-80">
                <form:select path="type" class="form-control m-b required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('SYS_WM_ZONE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>数据套</label></td>
            <td class="width-80">
                <sys:grid title="选择数据套" url="${ctx}/sys/common/dataSet/grid" cssClass="form-control required"
                          fieldId="dataSet" fieldName="dataSet" fieldKeyName="code" fieldValue="${sysWmsZone.dataSet}"
                          displayFieldId="dataSetName" displayFieldName="dataSetName" displayFieldKeyName="name" displayFieldValue="${sysWmsZone.dataSetName}"
                          fieldLabels="编码|名称" fieldKeys="code|name"
                          searchLabels="编码|名称" searchKeys="code|name"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right"><font color="red">*</font>所属区域</label></td>
            <td class="width-80">
                <sys:grid title="选择区域" url="${ctx}/sys/common/wms/area/grid" cssClass="form-control"
                          fieldId="areaCode" fieldName="areaCode" fieldKeyName="areaCode" fieldValue="${sysWmsZone.areaCode}"
                          displayFieldId="areaName" displayFieldName="areaName" displayFieldKeyName="areaName" displayFieldValue="${sysWmsZone.areaName}"
                          fieldLabels="区域编码|区域名称" fieldKeys="areaCode|areaName"
                          searchLabels="区域编码|区域名称" searchKeys="areaCode|areaName"
                          queryParams="dataSet" queryParamValues="dataSet"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right">备注</label></td>
            <td class="width-80">
                <form:input path="remarks" htmlEscape="false" rows="1" class="form-control" maxlength="200"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>