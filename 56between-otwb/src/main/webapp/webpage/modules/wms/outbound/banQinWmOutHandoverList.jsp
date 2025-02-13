<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>发货交接记录管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmOutHandoverList.js" %>
</head>
<body>
<div class="hide">
    <input id="carrierType" value="CARRIER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">发货交接记录列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmOutHandoverHeaderEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">交接单号</label></td>
                                    <td class="width-15">
                                        <form:input path="handoverNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择承运商" cssClass="form-control"
                                                       fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                                       displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="carrierSelectId" deleteButtonId="carrierDeleteId"
                                                       fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                                       queryParams="ebcuType" queryParamValues="carrierType">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"><label class="pull-right">交接时间从</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='handoverTimeFm'>
                                            <input type='text' name="handoverTimeFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">交接时间到</label></td>
                                    <td class="width-15">
                                        <div class='input-group form_datetime' id='handoverTimeTo'>
                                            <input type='text' name="handoverTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
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
                <shiro:hasPermission name="outbound:banQinWmOutHandover:gen">
                    <button id="gen" class="btn btn-primary" onclick="gen()"> 生成交接单</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmOutHandover:view">
                    <button id="view" class="btn btn-primary" disabled onclick="view()"> 查看</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmOutHandover:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="outbound:banQinWmOutHandover:printHandoverList">
                    <button id="printHandoverList" class="btn btn-primary" disabled onclick="printHandoverList()"> 打印交接清单</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmOutHandoverTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<div id="genModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width: 800px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">生成交接单</h4>
            </div>
            <div class="modal-body">
                <form id="genHandoverForm" class="form">
                    <input id="orgId" name="orgId" type="hidden"/>
                    <table class="table well" style="border: none">
                        <tbody>
                        <tr>
                            <td style="width: 150px; padding-top: 5px;">
                                <label class="pull-right asterisk">发货时间从</label>
                            </td>
                            <td style="width: 300px; padding-top: 5px;">
                                <div class='input-group form_datetime' id='shipTimeFm'>
                                    <input type='text' name="shipTimeFm" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                            <td style="width: 150px; padding-top: 5px;">
                                <label class="pull-right asterisk">发货时间到</label>
                            </td>
                            <td style="width: 300px; padding-top: 5px;">
                                <div class='input-group form_datetime' id='shipTimeTo'>
                                    <input type='text' name="shipTimeTo" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 150px; vertical-align: middle;">
                                <label class="pull-right asterisk">承运商</label>
                            </td>
                            <td style="width: 300px; vertical-align:middle;">
                                <sys:popSelect url="${ctx}/wms/customer/banQinEbCustomer/grid" title="选择承运商" cssClass="form-control"
                                               fieldId="gen_carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo" fieldValue="" allowInput="true"
                                               displayFieldId="gen_carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                               selectButtonId="carrierSBtnId" deleteButtonId="carrierDBtnId"
                                               fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                               searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn" inputSearchKey="codeAndName"
                                               queryParams="ebcuType" queryParamValues="carrierType">
                                </sys:popSelect>
                            </td>
                            <td></td>
                            <td></td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="genConfirm()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>