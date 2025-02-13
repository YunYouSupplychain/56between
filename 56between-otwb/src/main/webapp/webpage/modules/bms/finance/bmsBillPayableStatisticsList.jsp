<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>费用统计(应付)管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsBillPayableStatisticsList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="parentId" value="">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">费用统计(应付)列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsBillStatisticsEntity" class="form">
                            <form:hidden path="filterZero"/>
                            <form:hidden path="receivablePayable"/>
                            <form:hidden path="orgId"/>
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">结算对象编码</label></td>
                                    <td class="width-15">
                                        <form:input path="settleObjectCode" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">结算对象名称</label></td>
                                    <td class="width-15">
                                        <form:input path="settleObjectName" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">模型编码</label></td>
                                    <td class="width-15">
                                        <form:input path="settleModelCode" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BILL_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">系统合同编号</label></td>
                                    <td class="width-15">
                                        <form:input path="sysContractNo" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户合同编号</label></td>
                                    <td class="width-15">
                                        <form:input path="contractNo" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">费用模块</label></td>
                                    <td class="width-15">
                                        <form:select path="billModule" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BILL_MODULE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="仓库：">仓库</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/sys/office/companyData" title="选择仓库" cssClass="form-control"
                                                       fieldId="warehouseCode" fieldName="warehouseCode" fieldKeyName="code" fieldValue=""
                                                       displayFieldId="warehouseName" displayFieldName="warehouseName"
                                                       displayFieldKeyName="name" displayFieldValue=""
                                                       selectButtonId="warhouseSelectId" deleteButtonId="warhouseDeleteId"
                                                       fieldLabels="仓库编码|仓库名称" fieldKeys="code|name"
                                                       searchLabels="仓库编码|仓库名称" searchKeys="code|name"
                                                       queryParams="id" queryParamValues="parentId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">账单号</label></td>
                                    <td class="width-15">
                                        <form:input path="confirmNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">费用类别</label></td>
                                    <td class="width-15">
                                        <form:select path="billCategory" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_BILL_SUBJECT_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">费用科目</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择费用科目" url="${ctx}/bms/basic/bmsBillTerms/grid" cssClass="form-control"
                                                  fieldId="billSubjectCode" fieldName="billSubjectCode" fieldKeyName="billSubjectCode"
                                                  displayFieldId="billSubjectName" displayFieldName="billSubjectName" displayFieldKeyName="billSubjectName"
                                                  fieldLabels="费用科目代码|费用科目名称" fieldKeys="billSubjectCode|billSubjectName"
                                                  searchLabels="费用科目代码|费用科目名称" searchKeys="billSubjectCode|billSubjectName"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">计费条款</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择费用科目" url="${ctx}/bms/basic/bmsBillTerms/grid" cssClass="form-control"
                                                  fieldId="billTermsCode" fieldName="billTermsCode" fieldKeyName="billTermsCode"
                                                  displayFieldId="billTermsDesc" displayFieldName="billTermsDesc" displayFieldKeyName="billTermsDesc"
                                                  fieldLabels="计费条款代码|计费条款说明" fieldKeys="billTermsCode|billTermsDesc"
                                                  searchLabels="计费条款代码|计费条款说明" searchKeys="billTermsCode|billTermsDesc"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right" title="创建人：">创建人</label></td>
                                    <td class="width-15">
                                        <form:input path="creator" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="创建时间从：">创建时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='createTimeFm'>
                                            <input type='text' name="createTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="创建时间到：">创建时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='createTimeTo'>
                                            <input type='text' name="createTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="bms:finance:bmsBillStatistics:payable:export">
                    <button id="export" class="btn btn-info" onclick="exportExcel()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsBillStatisticsTable" data-toolbar="#toolbar" class="text-nowrap"></table>
            <div id="footer" style="display: none;">
                <table class="table-condensed" style="border-collapse: collapse;">
                    <tr>
                        <td><label class="pull-left">发生量：<span id="occurrenceQty"></span></label></td>
                        <td><label class="pull-left">计费量：<span id="billQty"></span></label></td>
                        <td><label class="pull-left">费用：<span id="cost"></span></label></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>