<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>业务库存</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmBusinessInvList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">业务库存</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmBusinessInvEntity" class="form form-horizontal well clearfix">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table table-striped table-bordered table-condensed">
                                <tbody>
                                <tr>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="状态：">状态：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:select path="orderType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_ACT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="库区：">库区：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align:middle;">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                                       fieldId="zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="" allowInput="true"
                                                       displayFieldId="zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue=""
                                                       selectButtonId="zoneSelectId" deleteButtonId="zoneDeleteId"
                                                       fieldLabels="库区代码|库区名称" fieldKeys="zoneCode|zoneName"
                                                       searchLabels="库区代码|库区名称" searchKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName">
                                        </sys:popSelect>
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
                                        <label class="label-item single-overflow pull-right" title="商品：">商品：</label>
                                    </td>
                                    <td>
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                       fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                       displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                       selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="订单号：">订单号：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:input path="orderNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="柜号：">柜号：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:input path="lotAtt06" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="结算月份：">结算月份：</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <div class='input-group date' id='lot'>
                                            <input type='text' name="lot" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="批次号">批次号</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:input path="lotNum" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="业务时间从">业务时间从</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <div class='input-group date' id='opTimeFm'>
                                            <input type='text' name="opTimeFm" class="form-control"/>
                                            <span class="input-group-addon">
                                               <span class="glyphicon glyphicon-calendar"></span>
                                           </span>
                                        </div>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="业务时间到">业务时间到</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <div class='input-group date' id='opTimeTo'>
                                            <input type='text' name="opTimeTo" class="form-control"/>
                                            <span class="input-group-addon">
                                               <span class="glyphicon glyphicon-calendar"></span>
                                           </span>
                                        </div>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;">
                                        <label class="label-item single-overflow pull-right" title="类型">类型</label>
                                    </td>
                                    <td style="width: 15%; vertical-align: middle;">
                                        <form:select path="tranType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_ACT_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width: 10%; vertical-align: middle;"></td>
                                    <td style="width: 15%; vertical-align: middle;"></td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="inventory:banQinWmBusinessInv:settle">
                    <button id="settle" class="btn btn-primary" onclick="settle()">结算</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmBusinessInv:reCalcAndSettle">
                    <button id="reCalcAndSettle" class="btn btn-primary" onclick="reCalcAndSettle()">重新计算并结算</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmBusinessInv:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="fa fa-file-excel-o"></i>导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmBusinessInvTable" data-toolbar="#toolbar" class="text-nowrap"></table>

            <div style="width: 100%; text-align: center;">
                <font size="3"><b><span>期初总数量</span><input id="totalBgQty" style="border:none;" readonly/></b></font>
                <font size="3"><b><span>进库总数量</span><input id="totalInQty" style="border:none;" readonly/></b></font>
                <font size="3"><b><span>出库总数量</span><input id="totalOutQty" style="border:none;" readonly/></b></font>
                <font size="3"><b><span>期末总数量</span><input id="totalEdQty" style="border:none;" readonly/></b></font>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="settleModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 350px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">请选择结算月份</h4>
            </div>
            <div class="modal-body">
                <input id="settleMonth" name="settleMonth" class="form-control" pattern="yyyy-MM"/>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="settleConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="calcModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">重新计算并结算</h4>
            </div>
            <div class="modal-body">
                <form class="form form-horizontal">
                <table class="table">
                    <tbody>
                    <tr>
                        <td><label class="label-item single-overflow pull-right" title="订单时间从："><font color="red">*</font>订单时间从：</label></td>
                        <td><input id="orderTimeFm" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    </tr>
                    <tr>
                        <td><label class="label-item single-overflow pull-right" title="订单时间到："><font color="red">*</font>订单时间到：</label></td>
                        <td><input id="orderTimeTo" class="form-control" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    </tr>
                    </tbody>
                </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="calcConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>