<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库区库存报表</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmRepInvDailyByZoneList.js" %>
</head>
<body>
<div class="hide">
    <input id="byOwner_ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">库区库存报表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="wmRepInvDailyByZoneEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">货主</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="byOwner_ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">商品</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control"
                                                       fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                                       displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue=""
                                                       selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">批次号</label></td>
                                    <td class="width-15">
                                        <input name="lotNum" htmlEscape="false" maxlength="64" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">库区</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control required"
                                                       fieldId="zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="" allowInput="true"
                                                       displayFieldId="zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue=""
                                                       selectButtonId="zoneSelectId" deleteButtonId="zoneDeleteId"
                                                       fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                                       searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName">
                                        </sys:popSelect>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">生产日期</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='lotAtt01'>
                                            <input type='text' name="lotAtt01" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">失效日期</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='lotAtt02'>
                                            <input type='text' name="lotAtt02" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">入库日期</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='lotAtt03'>
                                            <input type='text' name="lotAtt03" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">品质</label></td>
                                    <td class="width-15">
                                        <form:select path="lotAtt04" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_QC_ATT')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">批次属性5</label></td>
                                    <td class="width-15">
                                        <form:input path="lotAtt05" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">批次属性6</label></td>
                                    <td class="width-15">
                                        <form:input path="lotAtt06" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">批次属性7</label></td>
                                    <td class="width-15">
                                        <form:input path="lotAtt07" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">批次属性8</label></td>
                                    <td class="width-15">
                                        <form:input path="lotAtt08" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">批次属性9</label></td>
                                    <td class="width-15">
                                        <form:input path="lotAtt09" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">批次属性10</label></td>
                                    <td class="width-15">
                                        <form:input path="lotAtt10" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">批次属性11</label></td>
                                    <td class="width-15">
                                        <form:input path="lotAtt11" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="label-item single-overflow pull-right">批次属性12</label></td>
                                    <td class="width-15">
                                        <form:input path="lotAtt12" htmlEscape="false" maxlength="64" class=" form-control"/>
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
                <shiro:hasPermission name="report:wmRepInvDaily:zone:export">
                    <button class="btn btn-primary" onclick="exportData()"><i class="fa fa-file-excel-o"></i> 导出</button>
                </shiro:hasPermission>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>

            <!-- 表格 -->
            <table id="wmRepInvDailyTable" data-toolbar="#toolbar" class="text-nowrap"></table>

            <div style="width: 100%; text-align: center;">
                <font size="3"><b><span>总数量</span><input id="totalQty" style="border:none;" readonly/></b></font>
                <font size="3"><b><span>总重量</span><input id="totalWeight" style="border:none;" readonly/></b></font>
            </div>
        </div>
    </div>
</div>
</body>
</html>