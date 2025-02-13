<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>快递接口信息管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            $table = table;
            $topIndex = index;
            jp.loading();
            save();
        }

        $(document).ready(function () {
            init();
        });

        function save() {
            var validate = bq.headerSubmitCheck('#inputForm');
            if (validate.isSuccess) {
                jp.loading();
                jp.post("${ctx}/wms/basicdata/banQinCdTrackingInfo/save", $('#inputForm').bq_serialize(), function (data) {
                    if (data.success) {
                        $table.bootstrapTable('refresh');
                        jp.success(data.msg);
                        jp.close($topIndex);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            } else {
                jp.bqError(validate.msg);
            }
        }

        function init() {
            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#recVer').val('0');
            }
        }
    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="banQinCdTrackingInfo" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <table class="table">
        <tbody>
        <tr>
            <td width="20%"><label class="pull-right"><font color="red">*</font>接口类型</label></td>
            <td width="80%">
                <form:select path="type" class="form-control m-b required">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('SYS_TRACKING_INTERFACE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td width="20%"><label class="pull-right"><font color="red">*</font>接口描述</label></td>
            <td width="80%">
                <form:input path="description" htmlEscape="false" maxlength="128" class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td width="20%"><label class="pull-right"><font color="red">*</font>接口地址</label></td>
            <td width="80%">
                <form:input path="url" htmlEscape="false" maxlength="150" class="form-control required"/>
            </td>
        </tr>
        <tr>
            <td width="20%"><label class="pull-right"><font color="red">*</font>接口参数</label></td>
            <td width="0%">
                <form:input path="params" htmlEscape="false" maxlength="300" class="form-control required"/>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>