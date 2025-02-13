<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输订单信息管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@ include file="tmTransportOrderList.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER"/>
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="customerType" value="OWNER"/>
    <input type="hidden" id="outletType" value="OUTLET"/>
    <input type="hidden" id="carrierType" value="CARRIER"/>
    <input type="hidden" id="shipType" value="SHIP"/>
    <input type="hidden" id="consigneeType" value="CONSIGNEE"/>
    <input type="hidden" id="baseOrgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">运输订单信息列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmTransportOrderEntity" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">运输单号</label></td>
                                    <td class="width-15">
                                        <form:input path="transportNo" htmlEscape="false" maxlength="20" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderTimeFm'>
                                            <input type='text' name="orderTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderTimeTo'>
                                            <input type='text' name="orderTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单类型</label></td>
                                    <td class="width-15">
                                        <form:select path="orderType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_TRANSPORT_ORDER_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">客户单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" maxlength="20" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">委托方</label></td>
                                    <td class="width-15">
                                        <sys:grid title="委托方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="principalCode" fieldName="principalCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="principalName" displayFieldName="principalName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="委托方编码|委托方名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="委托方编码|委托方名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="principalType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户</label></td>
                                    <td class="width-15">
                                        <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="customerCode" fieldName="customerCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="customerName" displayFieldName="customerName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单状态</label></td>
                                    <td class="width-15">
                                        <form:select path="orderStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_TRANSPORT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">发货方</label></td>
                                    <td class="width-15">
                                        <sys:grid title="发货方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="shipCode" fieldName="shipCode" fieldKeyName="transportObjCode" fieldValue=""
                                                  displayFieldId="shipName" displayFieldName="shipName" displayFieldKeyName="transportObjName" displayFieldValue=""
                                                  fieldLabels="发货方编码|发货方名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="发货方编码|发货方名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="shipType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">收货方</label></td>
                                    <td class="width-15">
                                        <sys:grid title="收货方" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="consigneeCode" fieldName="consigneeCode" fieldKeyName="transportObjCode" fieldValue=""
                                                  displayFieldId="consigneeName" displayFieldName="consigneeName" displayFieldKeyName="transportObjName" displayFieldValue=""
                                                  fieldLabels="收货方编码|收货方名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="收货方编码|收货方名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="consigneeType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">提货网点</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择提货网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="receiveOutletCode" fieldName="receiveOutletCode" fieldKeyName="transportObjCode" fieldValue=""
                                                  displayFieldId="receiveOutletName" displayFieldName="receiveOutletName" displayFieldKeyName="transportObjName" displayFieldValue=""
                                                  fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">配送网点</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择配送网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="outletCode" fieldName="outletCode" fieldKeyName="transportObjCode" fieldValue=""
                                                  displayFieldId="outletName" displayFieldName="outletName" displayFieldKeyName="transportObjName" displayFieldValue=""
                                                  fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">是否已派车</label></td>
                                    <td class="width-15">
                                        <form:select path="hasDispatch" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">来源单号</label></td>
                                    <td class="width-15">
                                        <form:input path="sourceNo" htmlEscape="false" maxlength="20" class="form-control"/>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar" style="">
                <shiro:hasPermission name="order:tmTransportOrder:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:copy">
                    <button id="copy" class="btn btn-primary" disabled onclick="copy()"> 复制</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()"> 取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:transport:label:add">
                    <button id="addLabel" class="btn btn-primary" disabled onclick="openAddLabelModal()"> 添加标签</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:receive">
                    <button id="receive" class="btn btn-primary" disabled onclick="receive()"> 揽收</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:cancelReceive">
                    <button id="cancelReceive" class="btn btn-primary" disabled onclick="cancelReceive()"> 取消揽收</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:directDispatch">
                    <button id="directDispatch" class="btn btn-primary" disabled onclick="directDispatch()"> 调度派车</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:directDispatchNew">
                    <button id="directDispatchNew" class="btn btn-primary" disabled onclick="directDispatchNew()"> 新建调度派车</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:sign">
                    <button id="sign" class="btn btn-primary" disabled onclick="openSignModal()"> 签收</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:tmTransportOrder:receipt">
                    <button id="receipt" class="btn btn-primary" disabled onclick="openReceiptModal()"> 回单</button>
                </shiro:hasPermission>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">更多<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="order:tmTransportOrder:checkDispatch">
                            <li><a id="checkDispatch" class="disabled" onclick="checkDispatch()"> 查看派车情况</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="order:tmTransportOrder:vehicleLocation">
                            <li><a id="vehicleLocation" class="disabled" onclick="vehicleLocation()"> 查看车辆位置</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="order:tmTransportOrder:runTrack">
                            <li><a id="runTrack" class="disabled" onclick="runTrack()"> 查看行车轨迹</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="order:tmTransportOrder:tracking">
                            <li><a id="tracking" class="disabled" onclick="tracking()"> 物流节点跟踪</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="order:tmTransportOrder:annex">
                            <li><a id="annex" class="disabled" onclick="annex()"> 附件</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <shiro:hasPermission name="order:tmTransportOrder:print">
                    <div class="btn-group">
                        <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <shiro:hasPermission name="tms:print:transportOrder:printTransportOrder">
                                <li><a id="printTransportOrder" onclick="printTransportOrder()">打印运输订单</a></li>
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
            <div id="tmTransportOrderTabs" class="tabs-container" style="margin: 0">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#all" data-target-table="#allTable" aria-expanded="true">全部</a></li>
                    <li class=""><a data-toggle="tab" href="#toAudit" data-target-table="#toAuditTable" aria-expanded="true">待审核(<span id="toAuditOrderQty"></span>)</a></li>
                    <li class=""><a data-toggle="tab" href="#toReceive" data-target-table="#toReceiveTable" aria-expanded="true">待收货(<span id="toReceiveOrderQty"></span>)</a></li>
                    <li class=""><a data-toggle="tab" href="#toDispatch" data-target-table="#toDispatchTable" aria-expanded="true">待配载(<span id="toDispatchOrderQty"></span>)</a></li>
                    <li class=""><a data-toggle="tab" href="#toSign" data-target-table="#toSignTable" aria-expanded="true">待签收(<span id="toSignOrderQty"></span>)</a></li>
                    <li class=""><a data-toggle="tab" href="#signed" data-target-table="#signedTable" aria-expanded="true">已签收(<span id="signedOrderQty"></span>)</a></li>
                </ul>
                <div class="tab-content">
                    <div id="all" class="tab-pane fade in active">
                        <table id="allTable" class="text-nowrap"></table>
                    </div>
                    <div id="toAudit" class="tab-pane fade">
                        <table id="toAuditTable" class="text-nowrap"></table>
                    </div>
                    <div id="toReceive" class="tab-pane fade">
                        <table id="toReceiveTable" class="text-nowrap"></table>
                    </div>
                    <div id="toDispatch" class="tab-pane fade">
                        <table id="toDispatchTable" class="text-nowrap"></table>
                    </div>
                    <div id="toSign" class="tab-pane fade">
                        <table id="toSignTable" class="text-nowrap"></table>
                    </div>
                    <div id="signed" class="tab-pane fade">
                        <table id="signedTable" class="text-nowrap"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--添加标签--%>
<div id="addLabelModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:1200px; ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">添加标签</h4>
            </div>
            <div class="modal-body">
                <form id="addLabelForm" class="form">
                    <table class="table well">
                        <tr>
                            <td class="width-10"><label class="pull-right asterisk">标签数量</label></td>
                            <td class="width-15">
                                <input id="labelQty" name="labelQty" class="form-control required" maxlength="18" onkeyup="bq.numberValidator(this, 0, 0)"/>
                            </td>
                            <td class="width-10"><label class="pull-right">商品</label></td>
                            <td class="width-15">
                                <input type="hidden" id="addLabel_transportNo"/>
                                <input type="hidden" id="addLabel_orgId"/>
                                <input type="hidden" id="lineNo"/>
                                <sys:grid title="选择商品" url="${ctx}/tms/order/transport/sku/page" cssClass="form-control"
                                          fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode"
                                          displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName"
                                          fieldLabels="行号|商品编码|商品名称" fieldKeys="lineNo|skuCode|skuName"
                                          searchLabels="行号|商品编码|商品名称" searchKeys="lineNo|skuCode|skuName"
                                          queryParams="transportNo|orgId" queryParamValues="addLabel_transportNo|addLabel_orgId"
                                          afterSelect="afterSelectSku"/>
                            </td>
                            <td class="width-10"><label class="pull-right">总数量</label></td>
                            <td class="width-15">
                                <input id="totalQty" name="totalQty" class="form-control" maxlength="18" onkeyup="bq.numberValidator(this, 4, 0)"/>
                            </td>
                            <td class="width-10"><label class="pull-right">总重量</label></td>
                            <td class="width-15">
                                <input id="totalWeight" name="totalWeight" class="form-control" maxlength="18" onkeyup="bq.numberValidator(this, 6, 0)"/>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-10"><label class="pull-right">总体积</label></td>
                            <td class="width-15">
                                <input id="totalCubic" name="totalCubic" class="form-control" maxlength="18" onkeyup="bq.numberValidator(this, 6, 0)"/>
                            </td>
                            <td class="width-10"></td>
                            <td class="width-15"></td>
                            <td class="width-10"></td>
                            <td class="width-15"></td>
                            <td class="width-10"></td>
                            <td class="width-15"></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="addLabel()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%--签收--%>
<div id="signModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:1200px; ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">签收信息</h4>
            </div>
            <div class="modal-body">
                <form id="signForm" class="form">
                    <table class="table well">
                        <tr>
                            <td style="width: 13%;"><label class="pull-right asterisk">签收人</label></td>
                            <td style="width: 20%;">
                                <label for="sign_signBy"><input id="sign_signBy" name="signBy" class="form-control required" maxlength="35"/></label>
                            </td>
                            <td style="width: 13%;"><label class="pull-right asterisk">签收时间</label></td>
                            <td style="width: 20%;">
                                <div class='input-group date form_datetime' id='sign_signTime'>
                                    <input type='text' name="signTime" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td style="width: 13%;"><label class="pull-right">备注</label></td>
                            <td style="width: 20%;">
                                <label for="sign_remarks"><input id="sign_remarks" name="remarks" class="form-control" maxlength="255"/></label>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="sign()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<%--回单--%>
<div id="receiptModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:1200px; ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">回单信息</h4>
            </div>
            <div class="modal-body">
                <form id="receiptForm" class="form">
                    <table class="table well">
                        <tr>
                            <td class="width-10"><label class="pull-right asterisk">回单人</label></td>
                            <td class="width-15">
                                <label for="receipt_receiptBy"><input id="receipt_receiptBy" name="receiptBy" class="form-control required" maxlength="35"/></label>
                            </td>
                            <td class="width-10"><label class="pull-right asterisk">回单时间</label></td>
                            <td class="width-15">
                                <div class='input-group date form_datetime' id='receipt_receiptTime'>
                                    <input type='text' name="receiptTime" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td class="width-10"><label class="pull-right">备注</label></td>
                            <td class="width-15">
                                <label for="receipt_remarks"><input id="receipt_remarks" name="remarks" class="form-control" maxlength="255"/></label>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="receipt()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
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
                <form id="annexForm" class="form" style="margin-top: 0">
                    <div id="annexToolbar">
                        <shiro:hasPermission name="order:tmTransportOrder:uploadAnnex">
                            <a class="btn btn-primary" id="uploadAnnex" onclick="uploadAnnex()">上传附件</a>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="order:tmTransportOrder:removeAnnex">
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
                <form class="form" id="uploadFileForm" enctype="multipart/form-data">
                    <table class="table well">
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
<%--调度派车--%>
<div id="directDispatchModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1200px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">调度派车</h4>
            </div>
            <div class="modal-body">
                <form id="directDispatchForm" class="form">
                    <input type="hidden" id="dispatchOutletCode" name="dispatchOutletCode"/>
                    <table id="directDispatchTable" class="table well">
                        <tr>
                            <td><label class="pull-right asterisk">派车时间</label></td>
                            <td>
                                <sys:datetime id="dispatchTime" name="dispatchTime" cssClass="form-control required"/>
                            </td>
                            <td><label class="pull-right asterisk">派车类型</label></td>
                            <td>
                                <select id="dispatchType" name="dispatchType" class="form-control required">
                                    <option></option>
                                    <c:forEach items="${fns:getDictList('TMS_DISPATCH_TYPE')}" var="item">
                                        <option value="${item.value}">${item.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td><label class="pull-right">班次</label></td>
                            <td>
                                <input type="text" id="shift" name="shift" class="form-control" maxlength="20"/>
                            </td>
                            <td><label class="pull-right">月台</label></td>
                            <td>
                                <input type="text" id="platform" name="platform" class="form-control" maxlength="20"/>
                            </td>
                        </tr>
                        <tr>
                            <td><label class="pull-right asterisk">承运商</label></td>
                            <td>
                                <sys:grid title="选择承运商" url="${ctx}/tms/basic/tmTransportObj/grid"
                                          fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="transportObjCode"
                                          displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="transportObjName"
                                          fieldLabels="承运商编码|承运商名称" fieldKeys="transportObjCode|transportObjName"
                                          searchLabels="承运商编码|承运商名称" searchKeys="transportObjCode|transportObjName"
                                          queryParams="transportObjType|orgId" queryParamValues="carrierType|baseOrgId"
                                          cssClass="form-control required" afterSelect="carrierSelect"/>
                            </td>
                            <td><label class="pull-right">车牌号</label></td>
                            <td>
                                <sys:grid title="选择车辆" url="${ctx}/tms/basic/tmVehicle/grid"
                                          fieldId="" fieldName="" fieldKeyName=""
                                          displayFieldId="carNo" displayFieldName="carNo" displayFieldKeyName="carNo"
                                          fieldLabels="车牌号|承运商|设备类型" fieldKeys="carNo|carrierName|transportEquipmentTypeName"
                                          searchLabels="车牌号" searchKeys="carNo"
                                          queryParams="carrierCode|orgId|opOrgId" queryParamValues="carrierCode|baseOrgId|orgId"
                                          cssClass="form-control" afterSelect="carSelect"/>
                            </td>
                            <td><label class="pull-right">司机</label></td>
                            <td>
                                <sys:grid title="选择司机" url="${ctx}/tms/basic/tmDriver/grid"
                                          fieldId="driver" fieldName="driver" fieldKeyName="code"
                                          displayFieldId="driverName" displayFieldName="driverName" displayFieldKeyName="name"
                                          fieldLabels="编码|姓名|承运商" fieldKeys="code|name|carrierName" searchLabels="编码|姓名" searchKeys="code|name"
                                          queryParams="carrierCode|orgId" queryParamValues="carrierCode|baseOrgId"
                                          cssClass="form-control" afterSelect="driverSelect"/>
                            </td>
                            <td><label class="pull-right">司机电话</label></td>
                            <td>
                                <input type="text" id="driverTel" name="driverTel" class="form-control" maxlength="20"/>
                            </td>
                        </tr>
                        <tr>
                            <td><label class="pull-right">派车人</label></td>
                            <td>
                                <input type="text" id="dispatcher" name="dispatcher" class="form-control" maxlength="35"/>
                            </td>
                            <td><label class="pull-right">起始地</label></td>
                            <td>
                                <sys:area id="startAreaId" name="startAreaId" labelName="startAreaName"
                                          allowSearch="true" cssClass="form-control"/>
                            </td>
                            <td><label class="pull-right">目的地</label></td>
                            <td>
                                <sys:area id="endAreaId" name="endAreaId" labelName="endAreaName"
                                          allowSearch="true" cssClass="form-control"/>
                            </td>
                        </tr>
                        <tr>
                            <td><label class="pull-right">备注</label></td>
                            <td colspan="7">
                                <input type="text" id="remarks" name="remarks" class="form-control" maxlength="250"/>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="directDispatchConfirm()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
