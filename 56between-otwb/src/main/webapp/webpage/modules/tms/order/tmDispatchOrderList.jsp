<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>派车单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmDispatchOrderList.js" %>
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
            <h3 class="panel-title">派车单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmDispatchOrderEntity" class="form">
                            <form:hidden path="orgId"/>
                            <form:hidden path="baseOrgId"/>
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">派车单号</label></td>
                                    <td class="width-15">
                                        <form:input path="dispatchNo" htmlEscape="false" maxlength="20" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">派车时间从</label></td>
                                    <td class="width-15">
                                        <sys:datetime id="dispatchTimeFm" name="dispatchTimeFm" cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">派车时间到</label></td>
                                    <td class="width-15">
                                        <sys:datetime id="dispatchTimeTo" name="dispatchTimeTo" cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">派车单状态</label></td>
                                    <td class="width-15">
                                        <form:select path="dispatchStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_DISPATCH_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">派车单类型</label></td>
                                    <td class="width-15">
                                        <form:select path="dispatchType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_DISPATCH_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">发车时间从</label></td>
                                    <td class="width-15">
                                        <sys:datetime id="departureTimeFm" name="departureTimeFm" cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">发车时间到</label></td>
                                    <td class="width-15">
                                        <sys:datetime id="departureTimeTo" name="departureTimeTo" cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">车牌号</label></td>
                                    <td class="width-15">
                                        <form:input path="carNo" htmlEscape="false" maxlength="20" class=" form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择承运商" url="${ctx}/tms/basic/tmTransportObj/grid" cssClass="form-control"
                                                  fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="carrierType|baseOrgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">司机</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择司机" url="${ctx}/tms/basic/tmDriver/grid" cssClass="form-control"
                                                  fieldId="driver" fieldName="driver" fieldKeyName="code"
                                                  displayFieldId="driverName" displayFieldName="driverName" displayFieldKeyName="name"
                                                  fieldLabels="编码|姓名|承运商" fieldKeys="code|name|carrierName"
                                                  searchLabels="编码|姓名" searchKeys="code|name"
                                                  queryParams="orgId" queryParamValues="baseOrgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">起始地</label></td>
                                    <td class="width-15">
                                        <sys:area id="startAreaId" name="startAreaId" labelName="startAreaName"
                                                  allowSearch="true" cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">目的地</label></td>
                                    <td class="width-15">
                                        <sys:area id="endAreaId" name="endAreaId" labelName="endAreaName"
                                                  allowSearch="true" cssClass="form-control"/>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="order:tmDispatchOrder:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmDispatchOrder:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmDispatchOrder:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmDispatchOrder:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmDispatchOrder:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()"> 取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmDispatchOrder:depart">
                    <button id="depart" class="btn btn-primary" disabled onclick="depart()"> 发车</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmDispatchOrder:copy">
                    <button id="copy" class="btn btn-primary" disabled onclick="copy()"> 复制</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmDispatchOrder:annex">
                    <button id="annex" class="btn btn-primary" disabled onclick="annex()"> 附件</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmDispatchOrder:print">
                    <div class="btn-group">
                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <shiro:hasPermission name="tms:print:dispatchOrder:printDispatchOrder">
                                <li><a id="printDispatchOrder" onclick="printDispatchOrder()">打印送货单</a></li>
                            </shiro:hasPermission>
                        </ul>
                    </div>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmDispatchOrderTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
<%--附件--%>
<div id="annexModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1500px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">附件列表</h4>
            </div>
            <div class="modal-body">
                <form id="annexForm" class="form-horizontal" style="margin-top: 0">
                    <div id="annexToolbar">
                        <shiro:hasPermission name="order:tmDispatchOrder:uploadAnnex">
                            <a class="btn btn-primary" id="uploadAnnex" onclick="uploadAnnex()">上传附件</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="order:tmDispatchOrder:removeAnnex">
                            <a class="btn btn-primary" onclick="removeAnnex()">删除附件</a>
                        </shiro:hasPermission>
                    </div>
                    <table id="annexTable" class="text-nowrap" data-toolbar="#annexToolbar"></table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%--附件上传--%>
<div id="uploadFileModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 500px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">上传文件(限制大小10M)</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="uploadFileForm" enctype="multipart/form-data">
                    <table class="table table-striped table-bordered table-condensed">
                        <tbody>
                        <tr>
                            <td class="active" style="width: 70%;">
                                <input type="file" id="uploadFile" name="file"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="uploadFileFnc()">上传</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>