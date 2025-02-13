<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库存查询</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmInvQuery.js" %>
</head>
<body>
<div class="hide">
    <input id="byOwner_ownerType" value="OWNER" type="hidden">
    <input id="bySku_ownerType" value="OWNER" type="hidden">
    <input id="byLot_ownerType" value="OWNER" type="hidden">
    <input id="byLoc_ownerType" value="OWNER" type="hidden">
    <input id="bySkuLoc_ownerType" value="OWNER" type="hidden">
    <input id="byLotLocTraceId_ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="tabs-container">
                <ul class="nav nav-tabs">
                    <li class="active"><a data-toggle="tab" href="#tab-1" aria-expanded="true">按货主查询</a></li>
                    <li><a data-toggle="tab" href="#tab-2" aria-expanded="true">按商品查询</a></li>
                    <li><a data-toggle="tab" href="#tab-3" aria-expanded="true">按批次查询</a></li>
                    <li><a data-toggle="tab" href="#tab-4" aria-expanded="true">按库位查询</a></li>
                    <li><a data-toggle="tab" href="#tab-5" aria-expanded="true">按商品/库位查询</a></li>
                    <li><a data-toggle="tab" href="#tab-6" aria-expanded="true">按批次/库位/跟踪号查询</a></li>
                </ul>
                <div class="tab-content">
                    <!-- 按货主查询 -->
                    <div id="tab-1" class="tab-pane fade in active">
                        <div class="accordion-group">
                            <div id="collapse1" class="accordion-body collapse">
                                <div class="accordion-inner">
                                    <form:form id="byOwner_searchForm" modelAttribute="banQinWmInvQuery" class="form">
                                        <table class="table well" style="border: 1px solid #e3e3e3;">
                                            <tbody>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="货主：">货主</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                                   fieldId="byOwner_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                                   displayFieldId="byOwner_ownerName" displayFieldName="ownerName"
                                                                   displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                                   selectButtonId="byOwner_ownerSelectId" deleteButtonId="byOwner_ownerDeleteId"
                                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="ebcuType" queryParamValues="byOwner_ownerType">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="商品：">商品</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                                   fieldId="byOwner_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                                                   displayFieldId="byOwner_skuName" displayFieldName="skuName"
                                                                   displayFieldKeyName="skuName" displayFieldValue=""
                                                                   selectButtonId="byOwner_skuSelectId" deleteButtonId="byOwner_skuDeleteId"
                                                                   fieldLabels="商品编码|商品名称|货主编码|货主名称" fieldKeys="skuCode|skuName|ownerCode|ownerName"
                                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                                                   inputSearchKey="skuCodeAndName" allowInput="true"
                                                                   queryParams="ownerCode" queryParamValues="byOwner_ownerCode">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库区：">库区</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                                                   fieldId="byOwner_zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue=""
                                                                   displayFieldId="byOwner_zoneName" displayFieldName="zoneName"
                                                                   displayFieldKeyName="zoneName" displayFieldValue=""
                                                                   selectButtonId="byOwner_zoneSelectId" deleteButtonId="byOwner_zoneDeleteId"
                                                                   fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                                                   searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName"
                                                                   inputSearchKey="zoneCodeAndName" allowInput="true">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库位：">库位</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                                   displayFieldId="byOwner_locCode" displayFieldName="locCode"
                                                                   displayFieldKeyName="locCode" displayFieldValue=""
                                                                   selectButtonId="byOwner_locCodeSelectId"
                                                                   deleteButtonId="byOwner_locCodeDeleteId"
                                                                   fieldLabels="库位编码|库区代码|库区名称"
                                                                   fieldKeys="locCode|zoneCode|zoneName"
                                                                   searchLabels="库位编码" searchKeys="locCode"
                                                                   queryParams="zoneCode" queryParamValues="byOwner_zoneCode"
                                                                   inputSearchKey="codeAndName" allowInput="true">
                                                    </sys:popSelect>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次号：">批次号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotNum" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="跟踪号：">跟踪号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="traceId" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="生产日期：">生产日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group date' id='byOwner_lotAtt01'>
                                                        <input name="lotAtt01" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="失效日期：">失效日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byOwner_lotAtt02'>
                                                        <input name="lotAtt02" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="入库日期：">入库日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byOwner_lotAtt03'>
                                                        <input name="lotAtt03" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="品质：">品质</label>
                                                </td>
                                                <td class="width-15">
                                                    <select name="lotAtt04" class="form-control m-b">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_QC_ATT')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性5：">批次属性5</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt05" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性6：">批次属性6</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt06" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性7：">批次属性7</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt07" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性8：">批次属性8</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt08" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性9：">批次属性9</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt09" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性10：">批次属性10</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt10" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性11：">批次属性11</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt11" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性12：">批次属性12</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt12" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                        <!-- 工具栏 -->
                        <div id="byOwner_toolbar">
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" onclick="queryInvLogByOwner()">
                                <i class="fa fa-search"></i> 查询库存交易
                            </a>
                            <a id="byOwner_search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                            <a id="byOwner_reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#collapse1" href="#collapse1">
                                <i class="fa fa-search"></i> 检索
                            </a>
                        </div>
                        <!-- 表格 -->
                        <table id="byOwner_invTable" class="text-nowrap" data-toolbar="#byOwner_toolbar"></table>
                    </div>
                    <!-- 按商品查询 -->
                    <div id="tab-2" class="tab-pane fade">
                        <div class="accordion-group">
                            <div id="collapse2" class="accordion-body collapse">
                                <div class="accordion-inner">
                                    <form:form id="bySku_searchForm" modelAttribute="banQinWmInvQuery" class="form">
                                        <table class="table well" style="border: 1px solid #e3e3e3;">
                                            <tbody>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="货主：">货主</label>
                                                </td>
                                                <td class="width-10">
                                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                                   fieldId="bySku_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                                   displayFieldId="bySku_ownerName" displayFieldName="ownerName"
                                                                   displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                                   selectButtonId="bySku_ownerSelectId" deleteButtonId="bySku_ownerDeleteId"
                                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="ebcuType" queryParamValues="bySku_ownerType">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="商品：">商品</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                                   fieldId="bySku_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                                                   displayFieldId="bySku_skuName" displayFieldName="skuName"
                                                                   displayFieldKeyName="skuName" displayFieldValue=""
                                                                   selectButtonId="bySku_skuSelectId" deleteButtonId="bySku_skuDeleteId"
                                                                   fieldLabels="商品编码|商品名称|货主编码|货主名称" fieldKeys="skuCode|skuName|ownerCode|ownerName"
                                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                                                   inputSearchKey="skuCodeAndName" allowInput="true"
                                                                   queryParams="ownerCode" queryParamValues="bySku_ownerCode">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库区：">库区</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                                                   fieldId="bySku_zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue=""
                                                                   displayFieldId="bySku_zoneName" displayFieldName="zoneName"
                                                                   displayFieldKeyName="zoneName" displayFieldValue=""
                                                                   selectButtonId="bySku_zoneSelectId" deleteButtonId="bySku_zoneDeleteId"
                                                                   fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                                                   searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName"
                                                                   inputSearchKey="zoneCodeAndName" allowInput="true">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库位：">库位</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                                   displayFieldId="bySku_locCode" displayFieldName="locCode"
                                                                   displayFieldKeyName="locCode" displayFieldValue=""
                                                                   selectButtonId="bySku_locCodeSelectId"
                                                                   deleteButtonId="bySku_ocCodeDeleteId"
                                                                   fieldLabels="库位编码|库区代码|库区名称"
                                                                   fieldKeys="locCode|zoneCode|zoneName"
                                                                   searchLabels="库位编码" searchKeys="locCode"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="zoneCode" queryParamValues="bySku_zoneCode">
                                                    </sys:popSelect>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次号：">批次号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotNum" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="跟踪号：">跟踪号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="traceId" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="生产日期：">生产日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group date' id='bySku_lotAtt01'>
                                                        <input name="lotAtt01" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="失效日期：">失效日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='bySku_lotAtt02'>
                                                        <input name="lotAtt02" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="入库日期：">入库日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='bySku_lotAtt03'>
                                                        <input name="lotAtt03" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="品质：">品质</label>
                                                </td>
                                                <td class="width-15">
                                                    <select name="lotAtt04" class="form-control m-b">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_QC_ATT')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性5：">批次属性5</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt05" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性6：">批次属性6</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt06" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性7：">批次属性7</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt07" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性8：">批次属性8</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt08" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性9：">批次属性9</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt09" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性10：">批次属性10</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt10" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性11：">批次属性11</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt11" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性12：">批次属性12</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt12" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                        <!-- 工具栏 -->
                        <div id="bySku_toolbar">
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" onclick="queryInvLogBySku()">
                                <i class="fa fa-search"></i> 查询库存交易
                            </a>
                            <a id="bySku_search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                            <a id="bySku_reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#collapse2" href="#collapse2">
                                <i class="fa fa-search"></i> 检索
                            </a>
                        </div>
                        <!-- 表格 -->
                        <table id="bySku_invTable" class="text-nowrap" data-toolbar="#bySku_toolbar"></table>
                    </div>
                    <!-- 按批次查询 -->
                    <div id="tab-3" class="tab-pane fade">
                        <div class="accordion-group">
                            <div id="collapse3" class="accordion-body collapse">
                                <div class="accordion-inner">
                                    <form:form id="byLot_searchForm" modelAttribute="banQinWmInvQuery" class="form">
                                        <table class="table well" style="border: 1px solid #e3e3e3;">
                                            <tbody>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="货主：">货主</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                                   fieldId="byLot_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                                   displayFieldId="byLot_ownerName" displayFieldName="ownerName"
                                                                   displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                                   selectButtonId="byLot_ownerSelectId" deleteButtonId="byLot_ownerDeleteId"
                                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="ebcuType" queryParamValues="byLot_ownerType">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="商品：">商品</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                                   fieldId="byLot_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                                                   displayFieldId="byLot_skuName" displayFieldName="skuName"
                                                                   displayFieldKeyName="skuName" displayFieldValue=""
                                                                   selectButtonId="byLot_skuSelectId" deleteButtonId="byLot_skuDeleteId"
                                                                   fieldLabels="商品编码|商品名称|货主编码|货主名称" fieldKeys="skuCode|skuName|ownerCode|ownerName"
                                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                                                   inputSearchKey="skuCodeAndName" allowInput="true"
                                                                   queryParams="ownerCode" queryParamValues="byLot_ownerCode">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库区：">库区</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                                                   fieldId="byLot_zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue=""
                                                                   displayFieldId="byLot_zoneName" displayFieldName="zoneName"
                                                                   displayFieldKeyName="zoneName" displayFieldValue=""
                                                                   selectButtonId="byLot_zoneSelectId" deleteButtonId="byLot_zoneDeleteId"
                                                                   fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                                                   searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName"
                                                                   inputSearchKey="zoneCodeAndName" allowInput="true">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库位：">库位</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                                   displayFieldId="byLot_locCode" displayFieldName="locCode"
                                                                   displayFieldKeyName="locCode" displayFieldValue=""
                                                                   selectButtonId="byLot_locCodeSelectId"
                                                                   deleteButtonId="byLot_ocCodeDeleteId"
                                                                   fieldLabels="库位编码|库区代码|库区名称"
                                                                   fieldKeys="locCode|zoneCode|zoneName"
                                                                   searchLabels="库位编码" searchKeys="locCode"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="zoneCode" queryParamValues="byLot_zoneCode">
                                                    </sys:popSelect>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次号：">批次号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotNum" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="跟踪号：">跟踪号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="traceId" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="生产日期：">生产日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byLot_lotAtt01'>
                                                        <input name="lotAtt01" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="失效日期：">失效日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byLot_lotAtt02'>
                                                        <input name="lotAtt02" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="入库日期：">入库日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byLot_lotAtt03'>
                                                        <input name="lotAtt03" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="品质：">品质</label>
                                                </td>
                                                <td class="width-15">
                                                    <select name="lotAtt04" class="form-control m-b">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_QC_ATT')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性5：">批次属性5</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt05" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性6：">批次属性6</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt06" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性7：">批次属性7</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt07" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性8：">批次属性8</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt08" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性9：">批次属性9</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt09" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性10：">批次属性10</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt10" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性11：">批次属性11</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt11" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性12：">批次属性12</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt12" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                        <!-- 工具栏 -->
                        <div id="byLot_toolbar">
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" onclick="queryInvLogByLot()">
                                <i class="fa fa-search"></i> 查询库存交易
                            </a>
                            <a id="byLot_search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                            <a id="byLot_reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#collapse3" href="#collapse3">
                                <i class="fa fa-search"></i> 检索
                            </a>
                        </div>
                        <!-- 表格 -->
                        <table id="byLot_invTable" class="text-nowrap" data-toolbar="#byLot_toolbar"></table>
                    </div>
                    <!-- 按库位查询 -->
                    <div id="tab-4" class="tab-pane fade">
                        <div class="accordion-group">
                            <div id="collapse4" class="accordion-body collapse">
                                <div class="accordion-inner">
                                    <form:form id="byLoc_searchForm" modelAttribute="banQinWmInvQuery" class="form">
                                        <table class="table well" style="border: 1px solid #e3e3e3;">
                                            <tbody>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="货主：">货主</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                                   fieldId="byLoc_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                                   displayFieldId="byLoc_ownerName" displayFieldName="ownerName"
                                                                   displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                                   selectButtonId="byLoc_ownerSelectId" deleteButtonId="byLoc_ownerDeleteId"
                                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="ebcuType" queryParamValues="byLoc_ownerType">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="商品：">商品</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                                   fieldId="byLoc_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                                                   displayFieldId="byLoc_skuName" displayFieldName="skuName"
                                                                   displayFieldKeyName="skuName" displayFieldValue=""
                                                                   selectButtonId="byLoc_skuSelectId" deleteButtonId="byLoc_skuDeleteId"
                                                                   fieldLabels="商品编码|商品名称|货主编码|货主名称" fieldKeys="skuCode|skuName|ownerCode|ownerName"
                                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                                                   inputSearchKey="skuCodeAndName" allowInput="true"
                                                                   queryParams="ownerCode" queryParamValues="byLoc_ownerCode">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库区：">库区</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                                                   fieldId="byLoc_zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue=""
                                                                   displayFieldId="byLoc_zoneName" displayFieldName="zoneName"
                                                                   displayFieldKeyName="zoneName" displayFieldValue=""
                                                                   selectButtonId="byLoc_zoneSelectId" deleteButtonId="byLoc_zoneDeleteId"
                                                                   fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                                                   searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName"
                                                                   inputSearchKey="zoneCodeAndName" allowInput="true">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库位：">库位</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                                   displayFieldId="byLoc_locCode" displayFieldName="locCode"
                                                                   displayFieldKeyName="locCode" displayFieldValue=""
                                                                   selectButtonId="byLoc_locCodeSelectId"
                                                                   deleteButtonId="byLoc_ocCodeDeleteId"
                                                                   fieldLabels="库位编码|库区代码|库区名称"
                                                                   fieldKeys="locCode|zoneCode|zoneName"
                                                                   searchLabels="库位编码" searchKeys="locCode"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="zoneCode" queryParamValues="byLoc_zoneCode">
                                                    </sys:popSelect>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次号：">批次号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotNum" htmlEscape="false" maxlength="64" class="form-control" value="${banQinWmInvQuery.lotNum}"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="跟踪号：">跟踪号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="traceId" htmlEscape="false" maxlength="64" class="form-control" value="${banQinWmInvQuery.traceId}"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="生产日期：">生产日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class="col-xs-12">
                                                        <div class='input-group form_datetime' id='byLoc_lotAtt01'>
                                                            <input name="lotAtt01" class="form-control "/>
                                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                        </div>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="失效日期：">失效日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byLoc_lotAtt02'>
                                                        <input name="lotAtt02" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="入库日期：">入库日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byLoc_lotAtt03'>
                                                        <input name="lotAtt03" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="品质：">品质</label>
                                                </td>
                                                <td class="width-15">
                                                    <select name="lotAtt04" class="form-control m-b">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_QC_ATT')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性5：">批次属性5</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt05" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性6：">批次属性6</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt06" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性7：">批次属性7</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt07" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性8：">批次属性8</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt08" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性9：">批次属性9</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt09" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性10：">批次属性10</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt10" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性11：">批次属性11</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt11" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性12：">批次属性12</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt12" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                        <!-- 工具栏 -->
                        <div id="byLoc_toolbar">
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" onclick="queryInvLogByLoc()">
                                <i class="fa fa-search"></i> 查询库存交易
                            </a>
                            <a id="byLoc_search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                            <a id="byLoc_reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#collapse4" href="#collapse4">
                                <i class="fa fa-search"></i> 检索
                            </a>
                        </div>
                        <!-- 表格 -->
                        <table id="byLoc_invTable" class="text-nowrap" data-toolbar="#byLoc_toolbar"></table>
                    </div>
                    <!-- 按商品/库位查询 -->
                    <div id="tab-5" class="tab-pane fade">
                        <div class="accordion-group">
                            <div id="collapse5" class="accordion-body collapse">
                                <div class="accordion-inner">
                                    <form:form id="bySkuLoc_searchForm" modelAttribute="banQinWmInvQuery" class="form">
                                        <table class="table well" style="border: 1px solid #e3e3e3;">
                                            <tbody>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="货主：">货主</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                                   fieldId="bySkuLoc_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                                   displayFieldId="bySkuLoc_ownerName" displayFieldName="ownerName"
                                                                   displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                                   selectButtonId="bySkuLoc_ownerSelectId" deleteButtonId="bySkuLoc_ownerDeleteId"
                                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="ebcuType" queryParamValues="bySkuLoc_ownerType">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="商品：">商品</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                                   fieldId="bySkuLoc_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                                                   displayFieldId="bySkuLoc_skuName" displayFieldName="skuName"
                                                                   displayFieldKeyName="skuName" displayFieldValue=""
                                                                   selectButtonId="bySkuLoc_skuSelectId" deleteButtonId="bySkuLoc_skuDeleteId"
                                                                   fieldLabels="商品编码|商品名称|货主编码|货主名称" fieldKeys="skuCode|skuName|ownerCode|ownerName"
                                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                                                   inputSearchKey="skuCodeAndName" allowInput="true"
                                                                   queryParams="ownerCode" queryParamValues="bySkuLoc_ownerCode">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库区：">库区</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                                                   fieldId="bySkuLoc_zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue=""
                                                                   displayFieldId="bySkuLoc_zoneName" displayFieldName="zoneName"
                                                                   displayFieldKeyName="zoneName" displayFieldValue=""
                                                                   selectButtonId="bySkuLoc_zoneSelectId" deleteButtonId="bySkuLoc_zoneDeleteId"
                                                                   fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                                                   searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName"
                                                                   inputSearchKey="zoneCodeAndName" allowInput="true">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库位：">库位</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                                   displayFieldId="bySkuLoc_locCode" displayFieldName="locCode"
                                                                   displayFieldKeyName="locCode" displayFieldValue=""
                                                                   selectButtonId="bySkuLoc_locCodeSelectId"
                                                                   deleteButtonId="bySkuLoc_ocCodeDeleteId"
                                                                   fieldLabels="库位编码|库区代码|库区名称"
                                                                   fieldKeys="locCode|zoneCode|zoneName"
                                                                   searchLabels="库位编码" searchKeys="locCode"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="zoneCode" queryParamValues="bySkuLoc_zoneCode">
                                                    </sys:popSelect>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次号：">批次号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotNum" htmlEscape="false" maxlength="64" class="form-control" value="${banQinWmInvQuery.lotNum}"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="跟踪号：">跟踪号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="traceId" htmlEscape="false" maxlength="64" class="form-control" value="${banQinWmInvQuery.traceId}"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="生产日期：">生产日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='bySkuLoc_lotAtt01'>
                                                        <input name="lotAtt01" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="失效日期：">失效日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='bySkuLoc_lotAtt02'>
                                                        <input name="lotAtt02" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="入库日期：">入库日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='bySkuLoc_lotAtt03'>
                                                        <input name="lotAtt03" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="品质：">品质</label>
                                                </td>
                                                <td class="width-15">
                                                    <select name="lotAtt04" class="form-control m-b">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_QC_ATT')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性5：">批次属性5</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt05" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性6：">批次属性6</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt06" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性7：">批次属性7</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt07" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性8：">批次属性8</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt08" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性9：">批次属性9</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt09" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性10：">批次属性10</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt10" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性11：">批次属性11</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt11" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性12：">批次属性12</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt12" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                        <!-- 工具栏 -->
                        <div id="bySkuLoc_toolbar">
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" onclick="queryInvLogBySkuLoc()">
                                <i class="fa fa-search"></i> 查询库存交易
                            </a>
                            <a id="bySkuLoc_search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                            <a id="bySkuLoc_reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#collapse5" href="#collapse5">
                                <i class="fa fa-search"></i> 检索
                            </a>
                        </div>
                        <!-- 表格 -->
                        <table id="bySkuLoc_invTable" class="text-nowrap" data-toolbar="#bySkuLoc_toolbar"></table>
                    </div>
                    <!-- 按批次/库位/跟踪号查询 -->
                    <div id="tab-6" class="tab-pane fade">
                        <div class="accordion-group">
                            <div id="collapse6" class="accordion-body collapse">
                                <div class="accordion-inner">
                                    <form:form id="byLotLocTraceId_searchForm" modelAttribute="banQinWmInvQuery" class="form">
                                        <table class="table well" style="border: 1px solid #e3e3e3;">
                                            <tbody>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="货主：">货主</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                                   fieldId="byLotLocTraceId_ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                                   displayFieldId="byLotLocTraceId_ownerName" displayFieldName="ownerName"
                                                                   displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                                   selectButtonId="byLotLocTraceId_ownerSelectId" deleteButtonId="byLotLocTraceId_ownerDeleteId"
                                                                   fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="ebcuType" queryParamValues="byLotLocTraceId_ownerType">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="商品：">商品</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                                   fieldId="byLotLocTraceId_skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue=""
                                                                   displayFieldId="byLotLocTraceId_skuName" displayFieldName="skuName"
                                                                   displayFieldKeyName="skuName" displayFieldValue=""
                                                                   selectButtonId="byLotLocTraceId_skuSelectId" deleteButtonId="byLotLocTraceId_skuDeleteId"
                                                                   fieldLabels="商品编码|商品名称|货主编码|货主名称" fieldKeys="skuCode|skuName|ownerCode|ownerName"
                                                                   searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                                                   inputSearchKey="skuCodeAndName" allowInput="true"
                                                                   queryParams="ownerCode" queryParamValues="byLotLocTraceId_ownerCode">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库区：">库区</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control"
                                                                   fieldId="byLotLocTraceId_zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue=""
                                                                   displayFieldId="byLotLocTraceId_zoneName" displayFieldName="zoneName"
                                                                   displayFieldKeyName="zoneName" displayFieldValue=""
                                                                   selectButtonId="byLotLocTraceId_zoneSelectId" deleteButtonId="byLotLocTraceId_zoneDeleteId"
                                                                   fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                                                   searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName"
                                                                   inputSearchKey="zoneCodeAndName" allowInput="true">
                                                    </sys:popSelect>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="库位：">库位</label>
                                                </td>
                                                <td class="width-15">
                                                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control"
                                                                   fieldId="" fieldName="" fieldKeyName="" fieldValue=""
                                                                   displayFieldId="byLotLocTraceId_locCode" displayFieldName="locCode"
                                                                   displayFieldKeyName="locCode" displayFieldValue=""
                                                                   selectButtonId="byLotLocTraceId_locCodeSelectId"
                                                                   deleteButtonId="byLotLocTraceId_ocCodeDeleteId"
                                                                   fieldLabels="库位编码|库区代码|库区名称"
                                                                   fieldKeys="locCode|zoneCode|zoneName"
                                                                   searchLabels="库位编码" searchKeys="locCode"
                                                                   inputSearchKey="codeAndName" allowInput="true"
                                                                   queryParams="zoneCode" queryParamValues="byLotLocTraceId_zoneCode">
                                                    </sys:popSelect>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次号：">批次号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotNum" htmlEscape="false" maxlength="64" class="form-control" value="${banQinWmInvQuery.lotNum}"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="跟踪号：">跟踪号</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="traceId" htmlEscape="false" maxlength="64" class="form-control" value="${banQinWmInvQuery.traceId}"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="生产日期：">生产日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byLotLocTraceId_lotAtt01'>
                                                        <input name="lotAtt01" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="失效日期：">失效日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byLotLocTraceId_lotAtt02'>
                                                        <input name="lotAtt02" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="入库日期：">入库日期</label>
                                                </td>
                                                <td class="width-15">
                                                    <div class='input-group form_datetime' id='byLotLocTraceId_lotAtt03'>
                                                        <input name="lotAtt03" class="form-control "/>
                                                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                                    </div>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="品质：">品质</label>
                                                </td>
                                                <td class="width-15">
                                                    <select name="lotAtt04" class="form-control m-b">
                                                        <option value=""></option>
                                                        <c:forEach items="${fns:getDictList('SYS_WM_QC_ATT')}" var="dict">
                                                            <option value="${dict.value}">${dict.label}</option>
                                                        </c:forEach>
                                                    </select>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性5：">批次属性5</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt05" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性6：">批次属性6</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt06" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性7：">批次属性7</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt07" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性8：">批次属性8</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt08" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性9：">批次属性9</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt09" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性10：">批次属性10</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt10" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性11：">批次属性11</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt11" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                                <td class="width-10">
                                                    <label class="pull-right" title="批次属性12：">批次属性12</label>
                                                </td>
                                                <td class="width-15">
                                                    <input name="lotAtt12" htmlEscape="false" maxlength="64" class="form-control"/>
                                                </td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </form:form>
                                </div>
                            </div>
                        </div>
                        <!-- 工具栏 -->
                        <div id="byLotLocTraceId_toolbar">
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" onclick="queryInvLogByLotLocTraceId()">
                                <i class="fa fa-search"></i> 查询库存交易
                            </a>
                            <a id="byLotLocTraceId_search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                            <a id="byLotLocTraceId_reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                            <shiro:hasPermission name="inventory:banQinWmInvQuery:import">
                                <button class="btn btn-info" onclick="importInv()"><i class="fa fa-file-excel-o"></i>期初库存导入</button>
                            </shiro:hasPermission>
                            <shiro:hasPermission name="inventory:banQinWmInvQuery:export">
                                <button class="btn btn-info" onclick="exportInv()"><i class="fa fa-file-excel-o"></i>导出</button>
                            </shiro:hasPermission>
                            <div class="btn-group">
                                <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                                <ul class="dropdown-menu">
                                    <shiro:hasPermission name="inventory:banQinWmInvQuery:printTraceLabel">
                                        <li><a id="printTraceLabel" onclick="printTraceLabel()">打印托盘标签</a></li>
                                    </shiro:hasPermission>
                                    <shiro:hasPermission name="inventory:banQinWmInvQuery:printTraceLabelQrCode">
                                        <li><a id="printTraceLabelQrCode" onclick="printTraceLabelQrCode()">打印托盘标签(二维码)</a></li>
                                    </shiro:hasPermission>
                                </ul>
                            </div>
                            <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#collapse6" href="#collapse6">
                                <i class="fa fa-search"></i> 检索
                            </a>
                        </div>
                        <!-- 表格 -->
                        <table id="byLotLocTraceId_invTable" class="text-nowrap" data-toolbar="#byLotLocTraceId_toolbar"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- Excel上传弹出框 -->
<div class="modal fade" id="uploadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <form class="form-horizontal" id="fileUploadForm" enctype="multipart/form-data">
        <div class="modal-dialog" style="width:500px">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">上传文件</h4>
                </div>
                <div class="modal-body">
                    <table class="table well">
                        <tbody>
                        <tr>
                            <td class="active" style="width: 70%;">
                                <input type="file" id="uploadFileName" name="file" accept=".xls,.xlsx"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" onclick="uploadFile()">上传</button>
                </div>
            </div>
        </div>
    </form>
</div>
</body>
</html>