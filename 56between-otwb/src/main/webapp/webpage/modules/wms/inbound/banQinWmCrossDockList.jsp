<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>越库管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmCrossDockList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-body">
            <sys:message content="${message}"/>
            <div class="tabs-container">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">生成越库任务</a></li>
                    <li><a data-toggle="tab" href="#tab-2" aria-expanded="true">执行越库</a></li>
                </ul>
                <div class="tab-content">
                    <!-- 生成越库任务 -->
                    <div id="tab-1" class="tab-pane fade in active">
                        <div style="width: 100%;">
                            <div id="dock_toolbar">
                                <button id="crossDockCreateTask" class="btn btn-primary btn-sm" onclick="crossDockCreateTask()">生成越库任务(直接)</button>
                                <button id="crossDocCreateTaskBySkuInDirect" class="btn btn-primary btn-sm" onclick="crossDocCreateTaskBySkuInDirect()">标记越库(分拨)</button>
                                <button id="search1" class="btn btn-default btn-sm" onclick="search1()"><i class="fa fa-search"></i>检索</button>
                            </div>
                            <table id="dockTable" class="text-nowrap table-condensed" data-toolbar="#dock_toolbar" data-height="400"></table>
                        </div>
                        <div style="width: 100%; padding-top: 10px;">
                            <div id="detail_toolbar" style="padding-bottom: 10px;">
                                <button id="crossDockCreateTaskDetail" class="btn btn-primary btn-sm" onclick="crossDockDetail('crossDockCreateTaskDetail')">生成越库任务(直接)</button>
                                <button id="CreateTaskByInDirect" class="btn btn-primary btn-sm" onclick="crossDockDetail('createTaskByInDirect')">标记越库(分拨)</button>
                            </div>
                            <div id="detail_left" style="width: 50%; float: left;">
                                <table id="asnTable" class="text-nowrap table-condensed"></table>
                            </div>
                            <div style="width: 1%; float: left;">&nbsp;</div>
                            <div id="detail_right" style="width: 49%; float: left;">
                                <table id="soTable" class="text-nowrap table-condensed"></table>
                            </div>
                        </div>
                    </div>
                    <!-- 执行越库 -->
                    <div id="tab-2" class="tab-pane fade">
                        <div style="width: 100%;">
                            <div id="dock1_toolbar">
                                <button id="excuteCrossDock" class="btn btn-primary btn-sm" onclick="excuteCrossDock()">执行越库</button>
                                <button id="cancelCrossDock" class="btn btn-primary btn-sm" onclick="cancelCrossDock()">取消越库</button>
                                <button id="removeDirect" class="btn btn-primary btn-sm" onclick="removeDirect()">删除越库任务(直接)</button>
                                <button id="removeIndirect" class="btn btn-primary btn-sm" onclick="removeIndirect()">取消越库标记(分拨)</button>
                                <button id="search2" class="btn btn-default btn-sm" onclick="search2()"><i class="fa fa-search"></i>检索</button>
                            </div>
                            <table id="dock1Table" class="text-nowrap table-condensed" data-toolbar="#dock1_toolbar" data-height="400"></table>
                        </div>
                        <div style="width: 100%; padding-top: 10px;">
                            <div id="detail1_toolbar" style="padding-bottom: 10px;">
                                <button id="crossDockConfirmDetail" class="btn btn-primary btn-sm" onclick="crossDockConfirmDetail()">执行越库</button>
                                <button id="cancelRemarkDetail" class="btn btn-primary btn-sm" onclick="cancelRemarkDetail()">取消越库标记(分拨)</button>
                            </div>
                            <div id="detail1_left" style="width: 50%; float: left;">
                                <table id="asn1Table" class="text-nowrap table-condensed"></table>
                            </div>
                            <div style="width: 1%; float: left;">&nbsp;</div>
                            <div id="detail1_right" style="width: 49%; float: left;">
                                <div class="tabs-container">
                                    <ul class="nav nav-tabs">
                                        <li class="active"><a data-toggle="tab" href="#tab-2-1" aria-expanded="true">出库单明细</a></li>
                                        <li><a data-toggle="tab" href="#tab-2-2" aria-expanded="true">分配明细</a></li>
                                    </ul>
                                    <div class="tab-content">
                                        <div id="tab-2-1" class="tab-pane fade in active">
                                            <table id="so1Table" class="text-nowrap table-condensed"></table>
                                        </div>
                                        <div id="tab-2-2" class="tab-pane fade in">
                                            <table id="allocTable" class="text-nowrap table-condensed"></table>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="search1Modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1100px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">查询条件</h4>
            </div>
            <div class="modal-body">
                <form id="searchForm1">
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td><label class="pull-left">货主</label></td>
                            <td><label class="pull-left">商品</label></td>
                            <td><label class="pull-left">入库单号</label></td>
                            <td><label class="pull-left">入库单类型</label></td>
                        </tr>
                        <tr>
                            <td>
                                <input id="ownerType" value="OWNER" type="hidden">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="query1_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="query1_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="query1_ownerSelectId" deleteButtonId="query1_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerType">
                                </sys:popSelect>
                            </td>
                            <td>
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                               fieldId="query1_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="query1_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="query1_skuSelectId" deleteButtonId="query1_skuDeleteId"
                                               fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName">
                                </sys:popSelect>
                            </td>
                            <td>
                                <input id="query1_asnNo" name="asnNo" maxlength="64" class="form-control">
                            </td>
                            <td>
                                <select id="query1_asnType" name="asnType" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_ASN_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">预计到货时间从开始</label></td>
                            <td><label class="pull-left">预计到货时间从结束</label></td>
                            <td><label class="pull-left">预计到货时间到开始</label></td>
                            <td><label class="pull-left">预计到货时间到结束</label></td>
                        </tr>
                        <tr>
                            <td><input id="query1_fmEtaFm" name="fmEtaFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query1_fmEtaTo" name="fmEtaTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query1_toEtaFm" name="toEtaFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query1_toEtaTo" name="toEtaTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">预计发货时间从开始</label></td>
                            <td><label class="pull-left">预计发货时间从结束</label></td>
                            <td><label class="pull-left">预计发货时间到开始</label></td>
                            <td><label class="pull-left">预计发货时间到结束</label></td>
                        </tr>
                        <tr>
                            <td><input id="query1_fmEtdFm" name="fmEtdFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query1_fmEtdTo" name="fmEtdTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query1_toEtdFm" name="toEtdFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query1_toEtdTo" name="toEtdTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">出库单号</label></td>
                            <td><label class="pull-left">出库单类型</label></td>
                            <td><label class="pull-left">发运明细行总数(>=)</label></td>
                            <td><label class="pull-left">订货总数(>=)</label></td>
                        </tr>
                        <tr>
                            <td>
                                <input id="query1_soNo" name="soNo" maxlength="64" class="form-control">
                            </td>
                            <td>
                                <select id="query1_soType" name="soType" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_SO_TYPE')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                            <td>
                                <input id="query1_soLineNum" name="soLineNum" maxlength="11" class="form-control" onkeyup="bq.numberValidator(this, 2, 0);">
                            </td>
                            <td>
                                <input id="query1_qtySoEa" name="qtySoEa" maxlength="12" class="form-control" onkeyup="bq.numberValidator(this, 2, 0);">
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="queryForm1()">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="search2Modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 1100px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">查询条件</h4>
            </div>
            <div class="modal-body">
                <form id="searchForm2">
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td><label class="pull-left">货主</label></td>
                            <td><label class="pull-left">商品</label></td>
                            <td><label class="pull-left">入库单号</label></td>
                            <td><label class="pull-left">越库收货状态</label></td>
                        </tr>
                        <tr>
                            <td>
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="query2_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="query2_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="query2_ownerSelectId" deleteButtonId="query2_ownerDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerType">
                                </sys:popSelect>
                            </td>
                            <td>
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                               fieldId="query2_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="query2_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="query2_skuSelectId" deleteButtonId="query2_skuDeleteId"
                                               fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName">
                                </sys:popSelect>
                            </td>
                            <td>
                                <input id="query2_asnNo" name="asnNo" maxlength="64" class="form-control">
                            </td>
                            <td>
                                <select id="query2_status" name="status" class="form-control m-b">
                                    <option value=""></option>
                                    <c:forEach items="${fns:getDictList('SYS_WM_ASN_STATUS')}" var="dict">
                                        <option value="${dict.value}">${dict.label}</option>
                                    </c:forEach>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">预计到货时间从开始</label></td>
                            <td><label class="pull-left">预计到货时间从结束</label></td>
                            <td><label class="pull-left">预计到货时间到开始</label></td>
                            <td><label class="pull-left">预计到货时间到结束</label></td>
                        </tr>
                        <tr>
                            <td><input id="query2_fmEtaFm" name="fmEtaFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query2_fmEtaTo" name="fmEtaTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query2_toEtaFm" name="toEtaFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query2_toEtaTo" name="toEtaTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">预计发货时间从开始</label></td>
                            <td><label class="pull-left">预计发货时间从结束</label></td>
                            <td><label class="pull-left">预计发货时间到开始</label></td>
                            <td><label class="pull-left">预计发货时间到结束</label></td>
                        </tr>
                        <tr>
                            <td><input id="query2_fmEtdFm" name="fmEtdFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query2_fmEtdTo" name="fmEtdTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query2_toEtdFm" name="toEtdFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td><input id="query2_toEtdTo" name="toEtdTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        </tr>
                        <tr>
                            <td><label class="pull-left">出库单号</label></td>
                        </tr>
                        <tr>
                            <td><input id="query2_soNo" name="soNo" maxlength="64" class="form-control"></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="queryForm2()">确认</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>