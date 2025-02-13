<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>调度计划管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmDispatchPlanList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">调度计划列表</h3>
        </div>
        <div class="panel-body">
            <div class="hidden">
                <input id="carrierType" value="CARRIER" type="hidden">
                <input id="outletType" value="OUTLET" type="hidden">
            </div>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse in">
                    <div class="accordion-inner">
                        <form id="searchForm" class="form form-horizontal clearfix">
                            <input type="hidden" id="baseOrgId" name="baseOrgId"/>
                            <table class="table table-bordered table-condensed" style="margin-bottom: 10px">
                                <tr>
                                    <td style="width:10%;"><label class="pull-right">调度计划号：</label></td>
                                    <td style="width:15%;">
                                        <input id="planNo" name="planNo" maxlength="32" class="form-control"/>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">调度时间从：</label></td>
                                    <td style="width:15%;">
                                        <div class='input-group date' id='dispatchTimeFm'>
                                            <input type='text' name="dispatchTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">调度时间到：</label></td>
                                    <td style="width:15%;">
                                        <div class='input-group date' id='dispatchTimeTo'>
                                            <input type='text' name="dispatchTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;"><label class="pull-right">状态</label></td>
                                    <td style="width:15%;">
                                        <select name="status" class="form-control m-b">
                                            <option value=""></option>
                                            <c:forEach items="${fns:getDictList('TMS_DISPATCH_PLAN_STATUS')}" var="dict">
                                                <option value="${dict.value}">${dict.label}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%;"><label class="pull-right">承运商：</label></td>
                                    <td style="width:15%;">
                                        <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="carrierCodeF" fieldName="carrierCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="carrierNameF" displayFieldName="carrierName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="carrierType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;"><label class="pull-right">创建人：</label></td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <sys:grid url="${ctx}/sys/user/popDate" title="选择用户" cssClass="form-control"
                                                  fieldId="createById" fieldName="createBy.id" fieldKeyName="id"
                                                  displayFieldId="createByName" displayFieldName="createBy.name" displayFieldKeyName="name"
                                                  fieldLabels="登录名|姓名" fieldKeys="loginName|name"
                                                  searchLabels="登录名|姓名" searchKeys="loginName|name">
                                        </sys:grid>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;"><label class="pull-right">&nbsp;创建时间从：</label></td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <div class='input-group date' id='createDateFm'>
                                            <input type='text' name="createDateFm" class="form-control"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;"><label class="pull-right">&nbsp;创建时间到：</label></td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <div class='input-group date' id='createDateTo'>
                                            <input type='text' name="createDateTo" class="form-control"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </td>
                                </tr>
                            </table>
                        </form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="tms:order:tmDispatchPlan:createInitPlan">
                    <button id="createInitPlan" class="btn btn-primary" onclick="createInitPlan()">生成初始调度计划</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmDispatchPlan:delete">
                    <button id="remove" class="btn btn-danger" onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmDispatchPlan:addDemandPlan">
                    <button id="addDemandPlan" class="btn btn-primary" onclick="addDemandPlan()">追加需求计划</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmDispatchPlan:addVehicle">
                    <button id="addVehicle" class="btn btn-primary" onclick="addVehicle()">追加车辆</button>
                </shiro:hasPermission>
                <button id="search" class="btn btn-default"><i class="fa fa-search"></i>查询</button>
                <button id="reset" class="btn btn-default"><i class="fa fa-refresh"></i>重置</button>
            </div>
            <!-- 表格 -->
            <table id="tmDispatchPlanTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 生成调度计划 -->
<div class="modal fade" id="createModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-dialog" style="width: 70%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">生成初始调度计划</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td><label class="pull-left"><font color="red">*</font>承运商:</label></td>
                        <td><label class="pull-left"><font color="red">*</font>派车网点:</label></td>
                        <td><label class="pull-left"><font color="red">*</font>调度时间:</label></td>
                        <td><label class="pull-left"><font color="red">*</font>送达开始时间:</label></td>
                        <td><label class="pull-left"><font color="red">*</font>送达结束时间:</label></td>
                    </tr>
                    <tr>
                        <td>
                            <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode"
                                      displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName"
                                      fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="carrierType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td>
                            <sys:grid title="派车网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="dispatchOutletCode" fieldName="dispatchOutletCode" fieldKeyName="transportObjCode"
                                      displayFieldId="dispatchOutletName" displayFieldName="dispatchOutletName" displayFieldKeyName="transportObjName"
                                      fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                      cssClass="form-control required" readonly="false"/>
                        </td>
                        <td><input id="dispatchTime" name="dispatchTime" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><input id="demandBeginTime" name="demandBeginTime" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td><input id="demandEndTime" name="demandEndTime" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="createConfirm()">生成</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>