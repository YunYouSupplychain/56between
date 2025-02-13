<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存交易管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmActTranList.js" %>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">库存交易列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmActTran" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <form:hidden path="fmOwner" />
                            <form:hidden path="fmSku" />
                            <form:hidden path="fmLot" />
                            <form:hidden path="fmLoc" />
                            <form:hidden path="fmId" />
                            <form:hidden path="toOwner" />
                            <form:hidden path="toSku" />
                            <form:hidden path="toLot" />
                            <form:hidden path="toLoc" />
                            <form:hidden path="toId" />
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="交易类型：">交易类型</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="tranType"  class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_TRAN_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="单据号：">单据号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="orderNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="交易时间从：">交易时间从</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='tranTimeFrom'>
                                            <input type='text' name="tranTimeFrom" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="交易时间到：">交易时间到</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='tranTimeTo'>
                                            <input type='text' name="tranTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="货主：">货主</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="fmToOwner" fieldName="fmToOwner" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                       displayFieldId="fmToOwnerName" displayFieldName="fmToOwnerName"
                                                       displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                       inputSearchKey="codeAndName" allowInput="true"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="商品：">商品</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                       fieldId="fmToSku" fieldName="fmToSku" fieldKeyName="skuCode" fieldValue=""
                                                       displayFieldId="fmToSkuName" displayFieldName="fmToSkuName"
                                                       displayFieldKeyName="skuName" displayFieldValue=""
                                                       selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="商品编码|商品名称|货主编码|货主名称" fieldKeys="skuCode|skuName|ownerCode|ownerName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                                       inputSearchKey="skuCodeAndName" allowInput="true"
                                                       queryParams="ownerCode" queryParamValues="fmToOwner">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="批次号：">批次号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="fmToLot" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="库位：">库位</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                       fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                       displayFieldId="fmToLoc" displayFieldName="fmToLoc"
                                                       displayFieldKeyName="locCode" displayFieldValue=""
                                                       selectButtonId="fmToLocSelectId" deleteButtonId="fmToLocDeleteId"
                                                       fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                       searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName"
                                                       inputSearchKey="codeAndName" allowInput="true">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="跟踪号：">跟踪号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="fmToId" htmlEscape="false" maxlength="64" class=" form-control"/>
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
                <shiro:hasPermission name="inventory:banQinWmActTran:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmActTranTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>