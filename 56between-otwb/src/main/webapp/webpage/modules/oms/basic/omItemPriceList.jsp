<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>商品价格管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="omItemPriceList.js" %>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">商品价格列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="omItemPriceEntity" class="form clearfix">
                            <table class="table table-bordered table-condensed">
                                <tbody>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">往来户</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择往来户" cssClass="form-control" allowInput="true"
                                                       fieldId="customerNo" fieldName="customerNo" fieldKeyName="ebcuCustomerNo" fieldValue="${omItemPriceEntity.customerNo}"
                                                       displayFieldId="customerName" displayFieldName="customerName" displayFieldKeyName="ebcuNameCn"
                                                       displayFieldValue="${omItemPriceEntity.customerName}"
                                                       selectButtonId="customerSelectId" deleteButtonId="customerDeleteId"
                                                       fieldLabels="往来户编码|往来户名称" fieldKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       searchLabels="往来户编码|往来户名称" searchKeys="ebcuCustomerNo|ebcuNameCn">
                                        </sys:popSelect>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">商品</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:popSelect url="${ctx}/oms/basic/omItem/popData" title="选择商品" cssClass="form-control" allowInput="true"
                                                       fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode" fieldValue="${omItemPriceEntity.skuCode}"
                                                       displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName" displayFieldValue="${omItemPriceEntity.skuName}"
                                                       selectButtonId="skuSelectId" deleteButtonId="skuDeleteId"
                                                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName" inputSearchKey="codeAndName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName">
                                        </sys:popSelect>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">渠道价格</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="channel" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_CHANNEL')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">价格类型</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <form:select path="priceType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('OMS_PRICE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">生效时间从</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <div class='input-group date' id='beginEffectiveTime'>
                                            <input type='text' name="beginEffectiveTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">生效时间到</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <div class='input-group date' id='endEffectiveTime'>
                                            <input type='text' name="endEffectiveTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">失效时间从</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <div class='input-group date' id='beginExpirationTime'>
                                            <input type='text' name="beginExpirationTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">失效时间到</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <div class='input-group date' id='endExpirationTime'>
                                            <input type='text' name="endExpirationTime" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width:10%;vertical-align:middle;"><label class="pull-right">货主</label></td>
                                    <td style="width:15%;vertical-align:middle;">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control required"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue="${omItemPriceEntity.ownerCode}"
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue="${omItemPriceEntity.ownerName}"
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
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
                <shiro:hasPermission name="basic:omItemPrice:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:omItemPrice:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:omItemPrice:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:omItemPrice:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()"> 审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:omItemPrice:cancelAudit">
                    <button id="cancelAudit" class="btn btn-primary" disabled onclick="cancelAudit()"> 取消审核</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="omItemPriceTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>