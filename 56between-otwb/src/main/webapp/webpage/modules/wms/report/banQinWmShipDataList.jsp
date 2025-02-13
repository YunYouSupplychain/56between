<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>发货数据报表管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmShipDataList.js" %>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
    <input id="carrierType" value="CARRIER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">发货数据报表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="wmShipDataEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="快递单号：">快递单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="trackingNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="客户订单号：">客户订单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="customerOrderNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="外部单号：">外部单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="externalNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="波次单号：">波次单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="waveNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="出库单号：">出库单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="soNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="出库单类型：">出库单类型</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="soType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_SO_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="供应链订单号：">供应链订单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="chainNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="供应链任务号：">供应链任务号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="taskNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
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
                                    <td class="width-10">
                                        <label class="pull-right" title="商品：">商品</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                       fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                       displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                       selectButtonId="bskuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="发货时间从：">发货时间从</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='shipTimeFm'>
                                            <input type='text' name="shipTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="发货时间到：">发货时间到</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='shipTimeTo'>
                                            <input type='text' name="shipTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="订单时间从：">订单时间从</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderTimeFm'>
                                            <input type='text' name="orderTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="订单时间到：">订单时间到</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderTimeTo'>
                                            <input type='text' name="orderTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="箱号：">箱号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="caseNo" htmlEscape="false" maxlength="64" class="form-control"/>
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
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="是否有快递单号">是否有快递单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="isTracking" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
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
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <shiro:hasPermission name="report:banQinWmShipData:export">
                    <button class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="wmShipDataTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>