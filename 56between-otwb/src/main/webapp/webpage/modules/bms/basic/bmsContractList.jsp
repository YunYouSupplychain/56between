<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>合同管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsContractList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="orgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">合同列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="bmsContractEntity" class="form">
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right" title="结算对象：">结算对象</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control "
                                                  fieldId="settleObjectCode" fieldName="settleObjectCode" fieldKeyName="settleObjectCode"
                                                  displayFieldId="settleObjectName" displayFieldName="settleObjectName" displayFieldKeyName="settleObjectName"
                                                  fieldLabels="结算对象代码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                                                  searchLabels="结算对象代码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                                                  queryParams="orgId" queryParamValues="orgId"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="系统合同编号：">系统合同编号</label></td>
                                    <td class="width-15">
                                        <form:input path="sysContractNo" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="客户合同编号：">客户合同编号</label></td>
                                    <td class="width-15">
                                        <form:input path="contractNo" htmlEscape="false" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="合同状态：">合同状态</label></td>
                                    <td class="width-15">
                                        <form:select path="contractStatus" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('BMS_CONTRACT_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right" title="有效开始日期：">有效开始日期</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='effectiveDateFm'>
                                            <input type='text' name="effectiveDateFm" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="有效结束日期：">有效结束日期</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='effectiveDateTo'>
                                            <input type='text' name="effectiveDateTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="bms:bmsContract:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:copy">
                    <button id="copy" class="btn btn-primary" disabled onclick="copy()"> 复制</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:valid">
                    <button id="valid" class="btn btn-primary" disabled onclick="valid()"> 生效</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:invalid">
                    <button id="invalid" class="btn btn-primary" disabled onclick="invalid()"> 失效</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContractSkuPrice:edit">
                    <button id="skuPrice" class="btn btn-primary" disabled onclick="skuPrice()"> 商品价格</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:calc">
                    <button id="calc" class="btn btn-primary" disabled onclick="calc()">计算</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="bms:bmsContract:batchCalc">
                    <button id="batchCalc" class="btn btn-primary" onclick="batchCalc()">批量计算</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="bmsContractTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
<!-- 复制弹出窗 -->
<div id="copyModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog" style="width:450px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">复制</h4>
            </div>
            <div class="modal-body">
                <form id="copyForm" class="form" enctype="multipart/form-data">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td class="width-15"><label class="pull-right asterisk">机构</label></td>
                            <td class="width-35">
                                <sys:popSelect url="${ctx}/sys/office/companyData" title="选择机构" cssClass="form-control required"
                                               fieldId="cp_orgId" fieldName="orgId" fieldKeyName="id" fieldValue=""
                                               displayFieldId="cp_orgName" displayFieldName="orgName" displayFieldKeyName="name" displayFieldValue=""
                                               selectButtonId="orgSelectId" deleteButtonId="orgDeleteId"
                                               fieldLabels="机构编码|机构名称" fieldKeys="code|name"
                                               searchLabels="机构编码|机构名称" searchKeys="code|name">
                                </sys:popSelect>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15"><label class="pull-right asterisk">结算对象</label></td>
                            <td class="width-35">
                                <sys:grid title="选择结算对象" url="${ctx}/bms/basic/bmsSettleObject/grid" cssClass="form-control required"
                                          fieldId="cp_settleObjectCode" fieldName="settleObjectCode" fieldKeyName="settleObjectCode"
                                          displayFieldId="cp_settleObjectName" displayFieldName="settleObjectName" displayFieldKeyName="settleObjectName"
                                          fieldLabels="结算对象代码|结算对象名称" fieldKeys="settleObjectCode|settleObjectName"
                                          searchLabels="结算对象代码|结算对象名称" searchKeys="settleObjectCode|settleObjectName"
                                          queryParams="orgId" queryParamValues="cp_orgId"/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" onclick="copySave()">确认</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
<!-- 计算弹出窗 -->
<div id="calcModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static" data-keyboard="false">
    <div class="modal-dialog" style="width:400px">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">结算日期范围</h4>
            </div>
            <div class="modal-body">
                <form id="calcForm" class="form" enctype="multipart/form-data">
                    <table class="table">
                        <tbody>
                        <tr>
                            <td class="width-15"><label class="pull-right asterisk">日期从</label></td>
                            <td class="width-35">
                                <div class='input-group form_datetime' id='fmDate'>
                                    <input type='text' name="fmDate" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td class="width-15"><label class="pull-right asterisk">日期到</label></td>
                            <td class="width-35">
                                <div class='input-group form_datetime' id='toDate'>
                                    <input type='text' name="toDate" class="form-control required"/>
                                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                </div>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="calculate">计算</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>