<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>入库单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmAsnHeaderList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="ownerType" value="OWNER"/>
    <input type="hidden" id="supplierType" value="SUPPLIER"/>
    <input type="hidden" id="carrierType" value="CARRIER"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">入库单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmAsnEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="入库单号：">入库单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="asnNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="物流单号：">物流单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="logisticNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="入库单类型：">入库单类型</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="asnType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_ASN_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="货主：">货主</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="订单时间从：">&nbsp;订单时间从</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='beginOrderTime'>
                                            <input type='text' name="beginOrderTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="订单时间到：">&nbsp;订单时间到</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='endOrderTime'>
                                            <input type='text' name="endOrderTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="订单状态：">订单状态</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_ASN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="审核状态：">审核状态</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="auditStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="供应商：">供应商</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择供应商" cssClass="form-control"
                                                       fieldId="supplierCode" fieldName="supplierCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="supplierName" displayFieldName="supplierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="supplierSelectId" deleteButtonId="supplierDeleteId"
                                                       fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="supplierType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="承运商：">承运商</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择承运商" cssClass="form-control"
                                                       fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                                       fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="carrierType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="商流订单号：">商流订单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="def1" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="供应链订单号：">供应链订单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="def2" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right" title="作业任务号：">作业任务号</label></td>
                                    <td class="width-15">
                                        <form:input path="def3" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="def4" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">外部单号</label></td>
                                    <td class="width-15">
                                        <form:input path="def5" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:duplicate">
                    <button id="duplicate" class="btn btn-primary" disabled onclick="duplicate()"> 复制</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()">审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:costAlloc">
                    <button id="costAlloc" class="btn btn-primary" disabled onclick="costAlloc()">采购成本分摊</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()">取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:createQc">
                    <button id="createQc" class="btn btn-primary" disabled onclick="createQc()">生成质检单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:closeOrder">
                    <button id="closeOrder" class="btn btn-primary" disabled onclick="closeOrder()">关闭订单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:cancelOrder">
                    <button id="cancelOrder" class="btn btn-primary" disabled onclick="cancelOrder()">取消订单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="interface:rt:receiveFeedBackWms">
                    <button id="feedBackRT" class="btn btn-primary" disabled onclick="feedBackRT()">收货反馈</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:import">
                    <button id="importOrder" class="btn btn-info" onclick="importOrder()"><i class="fa fa-file-excel-o"></i> 导入</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmAsnHeader:serialReceive">
                    <button id="serialReceive" class="btn btn-primary" onclick="serialReceive()">序列号收货</button>
                </shiro:hasPermission>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">更多<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="inbound:banQinWmAsnHeader:holdOrder">
                            <li><a id="holdOrder" onclick="holdOrder()">冻结订单</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="inbound:banQinWmAsnHeader:cancelHold">
                            <li><a id="cancelHold" onclick="cancelHold()">取消冻结</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="inbound:banQinWmAsnHeader:createVoucherNo">
                            <li><a id="createVoucherNo" onclick="createVoucherNo()">生成凭证号</a></li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="inbound:banQinWmAsnHeader:cancelVoucherNo">
                            <li><a id="cancelVoucherNo" onclick="cancelVoucherNo()">取消凭证号</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <li><a id="printReceivingOrder" onclick="printReceivingOrder()">打印收货单</a></li>
                        <li><a id="printReceivingOrderLandscape" onclick="printReceivingOrderLandscape()">打印收货单(横版)</a></li>
                        <li><a id="printTraceLabel" onclick="printTraceLabel()">打印托盘标签</a></li>
                        <li><a id="printTraceLabelQrCode" onclick="printTraceLabelQrCode()">打印托盘标签(二维码)</a></li>
                        <%--<li><a id="printCheckReceiveOrder" onclick="printCheckReceiveOrder()">打印验收单</a></li>--%>
                    </ul>
                </div>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmAsnHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 分摊策略 -->
<div class="modal fade" id="apportionRuleModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">选择分摊策略</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td width="30%"><label class="pull-right">分摊策略</label></td>
                        <td width="70%">
                            <select id="apportion_rule" name="status" class="form-control m-b">
                                <option value=""></option>
                                <c:forEach items="${fns:getDictList('SYS_WM_APPORTION_RULE')}" var="dict">
                                    <option value="${dict.value}">${dict.label}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="apportionRuleConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
<!-- Excel上传弹出框 -->
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form class="form-horizontal" id="fileUploadForm" enctype="multipart/form-data">
        <div class="modal-dialog" style="width: 500px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">上传文件</h4>
                </div>
                <div class="modal-body">
                    <table class="table table-striped table-bordered table-condensed">
                        <tbody>
                        <tr>
                            <td class="active" style="width: 70%;">
                                <input type="file" id="uploadFileName" name="file"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" onclick="downloadTemplate()">下载导入模板</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="uploadFile()">上传</button>
                </div>
            </div>
        </div>
    </form>
</div>

<!-- 序列号收货 -->
<div class="modal fade" id="serialReceiveModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form class="form-horizontal" enctype="multipart/form-data">
        <div class="modal-dialog" style="width: 500px;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">上传文件</h4>
                </div>
                <div class="modal-body">
                    <table class="table table-striped table-bordered table-condensed">
                        <tbody>
                        <tr>
                            <td class="active" style="width: 70%;">
                                <input type="file" id="serialReceiveFile" name="file"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="serialReceiveConfirm()">确认</button>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>