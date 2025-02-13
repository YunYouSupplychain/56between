<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>交接单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmHandoverOrderList.js" %>
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
            <h3 class="panel-title">交接单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmHandoverOrderEntity" class="form">
                            <form:hidden path="orgId"/>
                            <form:hidden path="baseOrgId"/>
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">交接单号</label></td>
                                    <td class="width-15">
                                        <form:input path="handoverNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">派车单号</label></td>
                                    <td class="width-15">
                                        <form:input path="dispatchNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">交接单状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_HANDOVER_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <sys:grid title="承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode" fieldValue=""
                                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName" displayFieldValue=""
                                                  fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="carrierType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">派车时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='dispatchTimeFm'>
                                            <input type='text' name="dispatchTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">派车时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='dispatchTimeTo'>
                                            <input type='text' name="dispatchTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">车牌号</label></td>
                                    <td class="width-15">
                                        <form:input path="carNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="tms:order:tmHandoverOrder:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmHandoverOrder:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmHandoverOrder:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmHandoverOrder:handover">
                    <button id="handover" class="btn btn-primary" disabled onclick="handover()"> 交接</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmHandoverOrderTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>