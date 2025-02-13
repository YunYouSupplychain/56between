<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>销售单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmSaleHeaderList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">采购单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmSaleEntity" class="form form-horizontal well clearfix">
                            <table class="table table-striped table-bordered table-condensed">
                                <tbody>
                                <tr>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="销售单号：">销售单号：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:input path="saleNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="货主：">货主：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <input id="ownerType" value="OWNER" type="hidden">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="承运商：">承运商：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <input id="carrierType" value="CARRIER" type="hidden">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择承运商" cssClass="form-control"
                                                       fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                                       fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="carrierType">
                                        </sys:popSelect>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="状态：">状态：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_SALE_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="审核状态：">审核状态：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:select path="auditStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_AUDIT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="物流单号：">物流单号：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:input path="logisticNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="销售单类型：">销售单类型：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:select path="saleType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_SALE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                <shiro:hasPermission name="inbound:banQinWmSaleHeader:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmSaleHeader:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmSaleHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmSaleHeader:duplicate">
                    <button id="duplicate" class="btn btn-primary" disabled onclick="duplicate()">复制</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmSaleHeader:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()">审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmSaleHeader:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()">取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmSaleHeader:createAsn">
                    <button id="createAsn" class="btn btn-primary" disabled onclick="createSo()">生成ASN单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmSaleHeader:closeOrder">
                    <button id="closeOrder" class="btn btn-primary" disabled onclick="closeOrder()">关闭订单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inbound:banQinWmSaleHeader:cancelOrder">
                    <button id="cancelOrder" class="btn btn-primary" disabled onclick="cancelOrder()">取消订单</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmSaleHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>