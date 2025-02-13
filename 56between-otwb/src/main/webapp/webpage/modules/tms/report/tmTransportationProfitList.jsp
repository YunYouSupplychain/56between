<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输利润率报表</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmTransportationProfitList.js" %>
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
            <h3 class="panel-title">运输利润率报表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmTransportationProfit" class="form form-horizontal clearfix">
                            <table class="table table-striped table-bordered table-condensed">
                                <tr>
                                    <td style="width:10%;"><label class="pull-right">运输单号：</label></td>
                                    <td style="width:15%;">
                                        <form:input path="transportNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">订单时间从：</label></td>
                                    <td style="width:15%;">
                                        <div class='input-group date' id='fmDate'>
                                            <input type='text' name="fmDate" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">订单时间到：</label></td>
                                    <td style="width:15%;">
                                        <div class='input-group date' id='toDate'>
                                            <input type='text' name="toDate" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">客户：</label></td>
                                    <td style="width:15%;">
                                        <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="customerCode" fieldName="customerCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="customerName" displayFieldName="customerName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
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
                <shiro:hasPermission name="tms:report:transportationProfit:export">
                    <a id="export" class="btn btn-info"><i class="fa fa-file-excel-o"></i> 导出</a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="tmTransportationProfitTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>