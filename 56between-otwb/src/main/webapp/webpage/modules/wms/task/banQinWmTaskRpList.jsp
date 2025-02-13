<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>补货任务管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmTaskRpList.js" %>
</head>
<body>
<div class="hide">
    <input id="ownerType" value="OWNER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">补货任务列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmTaskRpEntity" class="form">
                            <input id="orgId" name="orgId" type="hidden">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="补货任务Id：">补货任务Id</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="rpId" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="状态：">状态</label>
                                    </td>
                                    <td class="width-15">
                                        <form:select path="status" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_TASK_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="补货时间从：">补货时间从</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='rpTimeF'>
                                            <input type='text' name="rpTimeF" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="补货时间到：">补货时间到</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='rpTimeT'>
                                            <input type='text' name="rpTimeT" class="form-control"/>
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
                                                       fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                                       searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="源库位：">源库位</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                                       fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                       displayFieldId="fmLoc" displayFieldName="fmLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                       selectButtonId="fmLocSelectId" deleteButtonId="fmLocDeleteId"
                                                       fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName"
                                                       searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="源跟踪号：">源跟踪号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="fmId" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="目标库位：">目标库位</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                                       fieldId="" fieldName="" fieldKeyName="" fieldValue="" allowInput="true"
                                                       displayFieldId="toLoc" displayFieldName="toLoc" displayFieldKeyName="locCode" displayFieldValue=""
                                                       selectButtonId="toLocSelectId" deleteButtonId="toLocDeleteId"
                                                       fieldLabels="库位编码|库区代码|库区名称" fieldKeys="locCode|zoneCode|zoneName" inputSearchKey="codeAndName"
                                                       searchLabels="库位编码|库区代码|库区名称" searchKeys="locCode|zoneCode|zoneName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="目标跟踪号：">目标跟踪号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="toId" htmlEscape="false" maxlength="64" class=" form-control"/>
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
                <shiro:hasPermission name="task:banQinWmTaskRp:createTask">
                    <button id="createTask" class="btn btn-primary" onclick="createTask()">生成补货任务</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="task:banQinWmTaskRp:cancelTask">
                    <button id="cancelTask" class="btn btn-primary" disabled onclick="cancelTask()">取消补货任务</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="task:banQinWmTaskRp:confirmTask">
                    <button id="confirmTask" class="btn btn-primary" disabled onclick="confirmTask()">补货确认</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="task:banQinWmTaskRp:export">
                    <button id="export" class="btn btn-info" onclick="exportData()"><i class="glyphicon glyphicon-export icon-share"></i> 导出</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmTaskRpTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 生成补货任务 -->
<div class="modal fade" id="createRpModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 500px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">生成补货任务</h4>
            </div>
            <div class="modal-body">
                <form id="dispatchPKForm">
                    <table class="bq-table">
                        <tbody>
                        <tr>
                            <td><label class="pull-left">货主</label></td>
                            <td><label class="pull-left">商品</label></td>
                        </tr>
                        <tr>
                            <td>
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择货主" cssClass="form-control"
                                               fieldId="ownerCodeC" fieldName="ownerCodeC" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="ownerNameC" displayFieldName="ownerNameC" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="ownerCSelectId" deleteButtonId="ownerCDeleteId"
                                               fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="ownerType">
                                </sys:popSelect>
                            </td>
                            <td>
                                <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhSku/grid" title="选择商品" cssClass="form-control required"
                                               fieldId="skuCodeC" fieldName="skuCodeC" fieldKeyName="skuCode" fieldValue="" allowInput="true"
                                               displayFieldId="skuNameC" displayFieldName="skuNameC" displayFieldKeyName="skuName" displayFieldValue=""
                                               selectButtonId="skuCSelectId" deleteButtonId="skuCDeleteId"
                                               fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                               searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName" inputSearchKey="skuCodeAndName"
                                               queryParams="ownerCode" queryParamValues="ownerCodeC">
                                </sys:popSelect>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" class="btn btn-primary" onclick="confirm()">确认</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>