<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>配件信息管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysTmsFittingForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="sysTmsFittingEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="dataSet"/>
    <form:hidden path="delFlag"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tr>
            <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>配件编码：</label></td>
            <td style="width: 12%;">
                <form:input path="fittingCode" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
            <td style="width: 8%;"><label class="pull-right"><font color="red">*</font>配件名称：</label></td>
            <td style="width: 12%;">
                <form:input path="fittingName" htmlEscape="false" class="form-control required" maxlength="64"/>
            </td>
            <td style="width: 8%;"><label class="pull-right">配件型号：</label></td>
            <td style="width: 12%;">
                <form:select path="fittingModel" class="form-control ">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('TMS_FITTING_MODEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
            <td style="width: 8%;"><label class="pull-right">单价：</label></td>
            <td style="width: 12%;">
                <form:input path="price" htmlEscape="false" class="form-control " onkeyup="bq.numberValidator(this, 6, 0)"/>
            </td>
            <td style="width: 8%;"><label class="pull-right">自定义1：</label></td>
            <td style="width: 12%;">
                <form:input path="def1" htmlEscape="false" class="form-control " maxlength="64"/>
            </td>
        </tr>
        <tr>
            <td style="width: 8%;"><label class="pull-right">自定义2：</label></td>
            <td style="width: 12%;">
                <form:input path="def2" htmlEscape="false" class="form-control " maxlength="64"/>
            </td>
            <td style="width: 8%;"><label class="pull-right">自定义3：</label></td>
            <td style="width: 12%;">
                <form:input path="def3" htmlEscape="false" class="form-control " maxlength="64"/>
            </td>
            <td style="width: 8%;"><label class="pull-right">自定义4：</label></td>
            <td style="width: 12%;">
                <form:input path="def4" htmlEscape="false" class="form-control " maxlength="64"/>
            </td>
            <td style="width: 8%;"><label class="pull-right">自定义5：</label></td>
            <td style="width: 12%;">
                <form:input path="def5" htmlEscape="false" class="form-control " maxlength="64"/>
            </td>
            <td style="width: 8%;"></td>
            <td style="width: 12%;"></td>
        </tr>
    </table>
</form:form>
</body>
</html>