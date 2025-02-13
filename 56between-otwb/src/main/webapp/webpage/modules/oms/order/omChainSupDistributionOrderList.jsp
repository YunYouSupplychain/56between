<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>供应商配送订单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="omChainSupDistributionOrderList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="ownerType" value="OWNER"/>
    <input type="hidden" id="supplierType" value="SUPPLIER"/>
    <input type="hidden" id="customerType" value="CONSIGNEE"/>
    <input type="hidden" id="orgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading"><h3 class="panel-title">供应商配送订单列表</h3></div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="omChainHeaderEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">供应链订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="chainNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单时间(从)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateFm'>
                                            <input type='text' name="orderDateFm" class="form-control" title="订单日期(从)"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单时间(到)</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='orderDateTo'>
                                            <input type='text' name="orderDateTo" class="form-control" title="订单日期(到)"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">业务订单类型</label></td>
                                    <td class="width-15">
                                        <form:select path="businessOrderType" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_BUSINESS_ORDER_TYPE_SUP')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">订单状态</label></td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_CHAIN_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">商流订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="businessNo" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">数据来源</label></td>
                                    <td class="width-15">
                                        <form:select path="dataSource" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_DATA_SOURCE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">货主</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control"
                                                       fieldId="owner" fieldName="owner" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSButtonId" deleteButtonId="ownerDButtonId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">供应商</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择供应商" cssClass="form-control"
                                                       fieldId="supplierCode" fieldName="supplierCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                       displayFieldId="supplierName" displayFieldName="supplierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="supplierSButtonId" deleteButtonId="supplierDButtonId"
                                                       fieldLabels="供应商编码|供应商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="供应商编码|供应商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                       queryParams="ebcuType" queryParamValues="supplierType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择客户" cssClass="form-control"
                                                       fieldId="customer" fieldName="customer" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                       displayFieldId="customerName" displayFieldName="customerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="customerSButtonId" deleteButtonId="customerDButtonId"
                                                       fieldLabels="客户编码|客户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="客户编码|客户名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                       queryParams="ebcuType" queryParamValues="customerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户机构</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/sys/sysCustomerOfficeRelation/grid" title="选择客户机构" cssClass="form-control"
                                                       fieldId="def10" fieldName="def10" fieldKeyName="orgCode" fieldValue=""
                                                       displayFieldId="orgName" displayFieldName="" displayFieldKeyName="orgName" displayFieldValue=""
                                                       selectButtonId="cusOrgSButtonId" deleteButtonId="cusOrgDButtonId"
                                                       fieldLabels="客户机构编码|客户机构名称" fieldKeys="orgCode|orgName"
                                                       searchLabels="客户机构编码|客户机构名称" searchKeys="orgCode|orgName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="order:omChainSupDistributionOrder:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omChainSupDistributionOrder:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omChainSupDistributionOrder:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omChainSupDistributionOrder:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omChainSupDistributionOrder:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()"> 取消审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="order:omChainSupDistributionOrder:createTask">
                    <button id="createTask" class="btn btn-primary" disabled onclick="createTask()"> 生成作业任务</button>
                </shiro:hasPermission>
                <shiro:hasAnyPermissions name="order:omChainSupDistributionOrder:dc:importLC,order:omChainSupDistributionOrder:dc:importLD,order:omChainSupDistributionOrder:dc:importV,order:omChainSupDistributionOrder:dc:importRF,order:omChainSupDistributionOrder:dc:importF">
                    <div class="btn-group">
                        <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown"><i class="fa fa-file-excel-o"></i> 直通导入<span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <shiro:hasPermission name="order:omChainSupDistributionOrder:dc:importLC">
                                <li><a id="importOrderRT" disabled onclick="importOrder('DC', 'DC_RT')">Excel导入(冷藏)</a></li>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order:omChainSupDistributionOrder:dc:importLD">
                                <li><a id="importOrderFT" disabled onclick="importOrder('DC', 'DC_FT')">Excel导入(冷冻)</a></li>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order:omChainSupDistributionOrder:dc:importV">
                                <li><a id="importOrderV" disabled onclick="importOrder('DC', 'DC_V')">Excel导入(蔬菜)</a></li>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order:omChainSupDistributionOrder:dc:importF">
                                <li><a id="importOrderF" disabled onclick="importOrder('DC', 'DC_F')">Excel导入(水果)</a></li>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order:omChainSupDistributionOrder:dc:importRF">
                                <li><a id="importOrderRF" disabled onclick="importOrder('DC', 'DC_RF')">Excel导入(RF)</a></li>
                            </shiro:hasPermission>
                        </ul>
                    </div>
                </shiro:hasAnyPermissions>
                <shiro:hasAnyPermissions name="order:omChainSupDistributionOrder:wms:importASN,order:omChainSupDistributionOrder:wms:importSO">
                    <div class="btn-group">
                        <button type="button" class="btn btn-info dropdown-toggle" data-toggle="dropdown"><i class="fa fa-file-excel-o"></i> 存储导入<span class="caret"></span></button>
                        <ul class="dropdown-menu">
                            <shiro:hasPermission name="order:omChainSupDistributionOrder:wms:importASN">
                                <li><a id="importOrderWmsAsn" disabled onclick="importOrder('WMS', 'WMS_ASN')">Excel导入(入库)</a></li>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="order:omChainSupDistributionOrder:wms:importSO">
                                <li><a id="importOrderWmsSo" disabled onclick="importOrder('WMS', 'WMS_SO')">Excel导入(出库)</a></li>
                            </shiro:hasPermission>
                        </ul>
                    </div>
                </shiro:hasAnyPermissions>
                <shiro:hasPermission name="order:omChainSupDistributionOrder:import">
                    <button id="importOrder" class="btn btn-info" onclick="importOrder()"><i class="fa fa-file-excel-o"></i> 导入</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="omChainHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
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
                    <table class="table table-bordered table-condensed">
                        <tbody>
                        <tr>
                            <td class="active" style="width: 70%;">
                                <input id="systemType" type="hidden">
                                <input id="uploadType" type="hidden">
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
</body>
</html>