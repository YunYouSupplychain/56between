<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>需求计划管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmDemandPlanList.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER">
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="customerType" value="OWNER">
    <input type="hidden" id="outletType" value="OUTLET">
    <input type="hidden" id="carrierType" value="CARRIER">
    <input type="hidden" id="shipType" value="SHIP">
    <input type="hidden" id="consigneeType" value="CONSIGNEE">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">需求计划列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmDemandPlanEntity" class="form form-horizontal well clearfix">
                            <form:hidden path="orgId"/>
                            <form:hidden path="baseOrgId"/>
                            <table class="table table-striped table-bordered table-condensed">
                                <tr>
                                    <td style="width:10%;"><label class="pull-right">订单时间从：</label></td>
                                    <td style="width:15%;">
                                        <div class='input-group date' id='orderTimeFm'>
                                            <input type='text' name="orderTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">订单时间到：</label></td>
                                    <td style="width:15%;">
                                        <div class='input-group date' id='orderTimeTo'>
                                            <input type='text' name="orderTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">到达时间从：</label></td>
                                    <td style="width:15%;">
                                        <div class='input-group date' id='arrivalTimeFm'>
                                            <input type='text' name="arrivalTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">到达时间到：</label></td>
                                    <td style="width:15%;">
                                        <div class='input-group date' id='arrivalTimeTo'>
                                            <input type='text' name="arrivalTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%;"><label class="pull-right">需求计划单号：</label></td>
                                    <td style="width:15%;">
                                        <form:input path="planOrderNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">状态：</label></td>
                                    <td style="width:15%;">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_DEMAND_PLAN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">客户：</label></td>
                                    <td style="width:15%;">
                                        <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="transportObjCode" fieldValue=""
                                                  displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="transportObjName" displayFieldValue=""
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
                <shiro:hasPermission name="tms:order:tmDemandPlan:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmDemandPlan:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmDemandPlan:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmDemandPlanTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>