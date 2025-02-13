<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存冻结管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmHoldList.js" %>
    <style type="text/css">
        .div-left {float:left; width:70%;}
        .div-right {float:right; width:30%;}
    </style>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">库存冻结列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmHoldEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="冻结ID：">冻结ID</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="holdId" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="冻结方式：">冻结方式</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="holdType"  class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_HOLD_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
                                    <td class="width-10">
                                        <label class="pull-right" title="商品：">商品</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control required"
                                                       fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                       displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                       selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="批次号：">批次号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="lotNum" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="库位：">库位</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                       fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                       displayFieldId="locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                       selectButtonId="locSelectId" deleteButtonId="locDeleteId"
                                                       fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                       searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="跟踪号：">跟踪号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="traceId" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="冻结原因：">冻结原因</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="reasonCode"  class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_HOLD_REASON')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="是否可移动：">是否可移动</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="isAllowMv"  class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="是否可调整：">是否可调整</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="isAllowAd"  class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_YES_NO')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="是否可转移：">是否可转移</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="isAllowTf"  class="form-control m-b">
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
                <shiro:hasPermission name="inventory:banQinWmHold:add">
                    <button id="add" class="btn btn-primary" onclick="add()">新建</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmHold:frost">
                    <button id="frost" class="btn btn-primary" disabled onclick="frost()">冻结</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmHold:thaw">
                    <button id="thaw" class="btn btn-primary" disabled onclick="thaw()">解冻</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmHold:viewDetail">
                    <button id="viewDetail" class="btn btn-primary" disabled onclick="viewDetail()">查看库存冻结明细</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="inventory:banQinWmHold:export">
                    <button id="export" class="btn btn-primary" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <div class="tab-content">
                <div id="tab-left" class="div-left" style="overflow: auto; height: 100%;">
                    <table id="banQinWmHoldTable" data-toolbar="#toolbar" class="text-nowrap"></table>
                </div>
                <div id="tab-right" class="div-right" style="min-height: 350px;border: 1px solid #dddddd;display: none;overflow-y: auto;">
                    <form:form id="inputForm" class="form">
                        <input type="hidden" id="detail_id" name="id">
                        <input type="hidden" id="detail_recVer" name="recVer">
                        <input type="hidden" id="detail_orgId" name="orgId">
                        <table class="bq-table">
                            <tbody>
                            <tr>
                                <td class="width-50">
                                    <label class="pull-left">冻结ID</label>
                                </td>
                                <td class="width-50">
                                    <label id="holdTypeLabel" class="pull-left asterisk">冻结方式</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <input id="detail_holdId" name="holdId" disabled htmlEscape="false" class="form-control">
                                </td>
                                <td class="width-50">
                                    <select id="detail_holdType" name="holdType" class="form-control m-b" onchange="selectChange()">
                                        <option value=""></option>
                                        <c:forEach items="${fns:getDictList('SYS_WM_HOLD_TYPE')}" var="dict">
                                            <option value="${dict.value}">${dict.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <label id="ownerLabel" class="pull-left">货主</label>
                                </td>
                                <td class="width-50">
                                    <label id="skuLabel" class="pull-left">商品</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                   fieldId="detail_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                   selectButtonId="detail_ownerSelectId" deleteButtonId="detail_ownerDeleteId"
                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                   queryParams="ebcuType" queryParamValues="ownerType">
                                    </sys:popSelect>
                                </td>
                                <td class="width-50">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control required"
                                                   fieldId="detail_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                   selectButtonId="detail_skuSelectId" deleteButtonId="detail_skuDeleteId"
                                                   fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                   concatId="detail_ownerCode,detail_ownerName"
                                                   concatName="ownerCode,ownerName">
                                    </sys:popSelect>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <label id="lotLabel" class="pull-left">批次号</label>
                                </td>
                                <td class="width-50">
                                    <label id="locLabel" class="pull-left">库位</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <input id="detail_lotNum" name="lotNum" readonly htmlEscape="false" class="form-control">
                                </td>
                                <td class="width-50">
                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                   displayFieldId="detail_locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue=""
                                                   selectButtonId="detail_locSelectId" deleteButtonId="detail_locDeleteId"
                                                   fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName"
                                                   searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName">
                                    </sys:popSelect>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <label id="traceLabel" class="pull-left">跟踪号</label>
                                </td>
                                <td class="width-50">
                                    <label class="pull-left">冻结原因<font color="red">*</font></label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <input id="detail_traceId" name="traceId" readonly htmlEscape="false" class="form-control " maxlength="32">
                                </td>
                                <td class="width-50">
                                    <select id="detail_reasonCode" name="reasonCode" disabled class="form-control m-b required">
                                        <option value=""></option>
                                        <c:forEach items="${fns:getDictList('SYS_WM_HOLD_REASON')}" var="dict">
                                            <option value="${dict.value}">${dict.label}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <label class="pull-left">是否可调整</label>
                                </td>
                                <td class="width-50">
                                    <label class="pull-left">原因描述<font color="red">*</font></label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <input id="detail_isAllowAd" type="checkbox" name="isAllowAd" disabled class="myCheckbox" onclick="checkChange(this)"/>
                                </td>
                                <td class="width-50">
                                    <input id="detail_reason" name="reason" htmlEscape="false" class="form-control" maxlength="512">
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <label class="pull-left">是否可移动</label>
                                </td>
                                <td class="width-50">
                                    <label class="pull-left">冻结人</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <input id="detail_isAllowMv" type="checkbox" name="isAllowMv" disabled class="myCheckbox" onclick="checkChange(this)"/>
                                </td>
                                <td class="width-50">
                                    <input id="detail_holdOp" name="holdOp" readonly htmlEscape="false" class="form-control">
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <label class="pull-left">是否可转移</label>
                                </td>
                                <td class="width-50">
                                    <label class="pull-left">冻结时间</label>
                                </td>
                            </tr>
                            <tr>
                                <td class="width-50">
                                    <input id="detail_isAllowTf" type="checkbox" name="isAllowTf" disabled class="myCheckbox" onclick="checkChange(this)"/>
                                </td>
                                <td class="width-50">
                                    <div class='input-group form_datetime'>
                                        <input type='text' id="detail_holdTime" disabled name="holdTime" class="form-control"/>
                                            <span class="input-group-addon">
                                            <span class="glyphicon glyphicon-calendar"></span>
                                        </span>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </form:form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>