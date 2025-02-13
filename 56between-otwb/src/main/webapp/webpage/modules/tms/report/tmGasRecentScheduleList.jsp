<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>加油站最近计划表</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmGasRecentScheduleList.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER"/>
    <input type="hidden" id="customerType" value="OWNER"/>
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="outletType" value="OUTLET">
    <input type="hidden" id="carrierType" value="CARRIER">
    <input type="hidden" id="shipType" value="SHIP">
    <input type="hidden" id="consigneeType" value="CONSIGNEE">
    <input type="hidden" id="baseOrgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">加油站最近计划表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmGasRecentSchedule" class="form form-horizontal clearfix">
                            <table class="table table-striped table-bordered table-condensed">
                                <tr>
                                    <td style="width:10%;"><label class="pull-right">客户：</label></td>
                                    <td style="width:15%;">
                                        <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td style="width:75%;"></td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-refresh"></i> 重置</a>
                <shiro:hasPermission name="tms:report:gasRecentSchedule:export">
                    <a id="export" class="btn btn-info"><i class="glyphicon glyphicon-export icon-share"></i> 导出</a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="tmGasRecentScheduleTable" data-toolbar="#toolbar" class="text-nowrap"></table>

        </div>
    </div>
</div>
</body>
</html>