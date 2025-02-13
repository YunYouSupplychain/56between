<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>拣货任务管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmSoAllocList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">拣货任务列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmSoAllocEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">出库单号</label></td>
                                    <td class="width-15">
                                        <form:input path="soNo" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">波次单号</label></td>
                                    <td class="width-15">
                                        <form:input path="waveNo" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">拣货单号</label></td>
                                    <td class="width-15">
                                        <form:input path="pickNo" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">状态</label></td>
                                    <td class="width-15">
                                        <select id="statuss" name="statuss" class="form-control selectpicker" multiple>
                                            <option value=""></option>
                                            <c:forEach items="${fns:getDictList('SYS_WM_SO_STATUS')}" var="dict">
                                                <option value="${dict.value}">${dict.label}</option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">货主</label></td>
                                    <td class="width-15">
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
                                    <td class="width-10"><label class="pull-right">商品</label></td>
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
                                    <td class="width-10"><label class="pull-right">批次号</label></td>
                                    <td class="width-15">
                                        <form:input path="lotNum" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">源库位</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                       fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                       displayFieldId="locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                       selectButtonId="locCodeSelectId" deleteButtonId="locCodeDeleteId"
                                                       fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                       searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">源跟踪号</label></td>
                                    <td class="width-15">
                                        <form:input path="traceId" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">目标库位</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                       fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                       displayFieldId="toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                       selectButtonId="toLocSelectId" deleteButtonId="toLocDeleteId"
                                                       fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                       searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">目标跟踪号</label></td>
                                    <td class="width-15">
                                        <form:input path="toId" htmlEscape="false" maxlength="32" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">收货人</label></td>
                                    <td class="width-15">
                                        <input id="consigneeType" value="CONSIGNEE" type="hidden">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择收货人" cssClass="form-control"
                                                       fieldId="consigneeCode" fieldName="consigneeCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="consigneeName" displayFieldName="consigneeName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="consigneeSelectId" deleteButtonId="consigneeDeleteId"
                                                       fieldLabels="收货人编码|收货人名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="收货人编码|收货人名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="consigneeType">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">订单时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='beginOrderTime'>
                                            <input type='text' name="beginOrderTime" class="form-control"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">订单时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='endOrderTime'>
                                            <input type='text' name="endOrderTime" class="form-control"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">拣货任务ID</label></td>
                                    <td class="width-15">
                                        <form:input path="allocId" htmlEscape="false" maxlength="32" class="form-control"/>
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
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:picking">
                    <button id="picking" class="btn btn-primary" disabled onclick="picking()">拣货确认</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:shipment">
                    <button id="shipment" class="btn btn-primary" disabled onclick="shipment()">发货确认</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:cancelAlloc">
                    <button id="cancelAlloc" class="btn btn-primary" onclick="cancelAlloc()">取消分配</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:cancelPick">
                    <button id="cancelPick" class="btn btn-primary" onclick="cancelPick()">取消拣货</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:cancelShipment">
                    <button id="cancelShipment" class="btn btn-primary" onclick="cancelShipment()">取消发货</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:generatePick">
                    <button id="generatePick" class="btn btn-primary" onclick="generatePick()">生成拣货单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmSoAlloc:export">
                    <button id="export" class="btn btn-info" onclick="exportData()">导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmSoAllocTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 生成上架任务 -->
<div class="modal fade" id="createTaskPaModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 400px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">生成上架任务</h4>
            </div>
            <div class="modal-body">
                <table class="bq-table">
                    <tbody>
                    <tr>
                        <td class="width-20">
                            <input id="isTaskPa" type="checkBox" class="myCheckbox" onclick="isTaskPaChange(this.checked)">
                        </td>
                        <td class="width-80">
                            <label class="pull-left">是否生成上架任务</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20">
                            <input type="radio" id="allocLoc" name="loc" class="myRadio">
                        </td>
                        <td class="width-80">
                            <label class="pull-left">推荐分配库位</label>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-20">
                            <input type="radio" id="paRuleLoc" name="loc" class="myRadio">
                        </td>
                        <td class="width-80">
                            <label class="pull-left">上架规则计算库位</label>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="createTaskPaConfirm()">确认</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>