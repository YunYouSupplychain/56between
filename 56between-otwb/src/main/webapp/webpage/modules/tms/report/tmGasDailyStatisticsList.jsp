<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>加油站每日统计表</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmGasDailyStatisticsList.js" %>
</head>
<body class="bg-white">
<div class="hidden">
    <input type="hidden" id="baseOrgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">加油站每日统计表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <form:form id="searchForm" modelAttribute="tmGasDailyStatistics" class="form form-horizontal clearfix" onsubmit="return false;">
                <table class="table table-striped table-bordered table-condensed">
                    <tr>
                        <td style="width:10%;"><label class="pull-right">加油站：</label></td>
                        <td style="width:15%;">
                            <sys:grid title="加油站" url="${ctx}/tms/report/gasOilStatistics/getGas"
                                      fieldId="gasCode" fieldName="gasCode" fieldKeyName="gasCode"
                                      displayFieldId="gasName" displayFieldName="gasName" displayFieldKeyName="gasName"
                                      fieldLabels="加油站编码|加油站名称" fieldKeys="gasCode|gasName"
                                      searchLabels="加油站编码|加油站名称" searchKeys="gasCode|gasName"
                                      queryParams="orgId" queryParamValues="baseOrgId"
                                      cssClass="form-control required"/>
                        </td>
                        <td style="width:10%;"><label class="pull-right">日期：</label></td>
                        <td style="width:15%;">
                            <div class='input-group date' id='queryDate'>
                                <input type='text' name="queryDate" class="form-control"/>
                                <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                            </div>
                        </td>
                        <td style="width:50%;"></td>
                    </tr>
                </table>
            </form:form>
            <!-- 工具栏 -->
            <div id="toolbar">
                <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-refresh"></i> 重置</a>
                <shiro:hasPermission name="tms:report:gasStationDailyStatistics:export">
                    <a id="export" class="btn btn-info"><i class="glyphicon glyphicon-export icon-share"></i> 导出</a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="tmGasDailyStatisticsTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>