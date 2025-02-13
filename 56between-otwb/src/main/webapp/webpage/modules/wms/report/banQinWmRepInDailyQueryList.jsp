<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>入库日报表管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmRepInDailyQueryList.js" %>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">入库日报表列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmRepInDailyQueryEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="入库单号：">入库单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="asnNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="入库单类型：">入库单类型</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="asnType"  class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_ASN_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="状态：">状态</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="status"  class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_ASN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="物流单号：">物流单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="logisticNo" htmlEscape="false" maxlength="64" class=" form-control"/>
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
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                       queryParams="ebcuType" queryParamValues="ownerType" inputSearchKey="codeAndName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="商品：">商品</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                       fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                       displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                       selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                       queryParams="ownerCode" queryParamValues="ownerCode">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="收货时间从：">收货时间从</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='rcvTimeFrom'>
                                            <input type='text' name="rcvTimeFrom" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="收货时间到：">收货时间到</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='rcvTimeTo'>
                                            <input type='text' name="rcvTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <ty>
                                    <td class="width-10">
                                        <label class="pull-right" title="订单时间从：">订单时间从</label>
                                    </td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <div class='input-group date' id='beginOrderTime'>
                                            <input type='text' name="beginOrderTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="订单时间到：">订单时间到</label>
                                    </td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <div class='input-group date' id='endOrderTime'>
                                            <input type='text' name="endOrderTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </ty>
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
                <shiro:hasPermission name="report:banQinWmRepInDailyQuery:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmRepInDailyQueryTable" data-toolbar="#toolbar" class="text-nowrap"></table>
            <div style="width: 100%; text-align: center;">
                <font size="3"><b><span>预收总数量</span><input id="totalAsnQty" style="border:none;" readonly/></b></font>
                <font size="3"><b><span>已收总数量</span><input id="totalRcvQty" style="border:none;" readonly/></b></font>
            </div>
        </div>
    </div>
</div>
</body>
</html>