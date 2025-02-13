<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>快递单号导入更新管理</title>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmExpressInfoImportForm.js" %>
</head>
<body>
<form:form id="inputForm" modelAttribute="tmExpressInfoImport" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <sys:message content="${message}"/>
    <%--基本信息--%>
    <div class="tabs-container" style="min-height: 100px;">
        <ul class="nav nav-tabs">
            <li class="active"><a data-toggle="tab" href="#basicInfo" aria-expanded="true">基本信息</a></li>
        </ul>
        <div class="tab-content">
            <div id="basicInfo" class="tab-pane fade in active">
                <table class="table well">
                    <tr>
                        <td class="width-10"><label class="pull-right">导入批次号</label></td>
                        <td class="width-15">
                            <form:input path="importNo" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">文件名</label></td>
                        <td class="width-15">
                            <form:input path="fileName" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">导入原因</label></td>
                        <td class="width-15" colspan="3">
                            <form:input path="importReason" htmlEscape="false" class="form-control " readonly="true"/>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</form:form>
<%--明细列表--%>
<div class="tabs-container">
    <ul id="detailTabs" class="nav nav-tabs">
        <li class="active"><a data-toggle="tab" href="#tmExpressInforImportDetail" aria-expanded="true">导入明细</a></li>
    </ul>
    <div class="tab-content">
        <%--明细列表--%>
        <div id="tmExpressInforImportDetail" class="tab-pane fade in active">
            <table id="tmExpressInforImportDetailTable" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>