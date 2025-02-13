<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>异常处理单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmExceptionHandleBillList.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="principalType" value="CUSTOMER">
    <input type="hidden" id="settlementType" value="SETTLEMENT"/>
    <input type="hidden" id="customerType" value="OWNER">
    <input type="hidden" id="outletType" value="OUTLET">
    <input type="hidden" id="carrierType" value="CARRIER">
    <input type="hidden" id="shipType" value="SHIP">
    <input type="hidden" id="consigneeType" value="CONSIGNEE">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">异常处理单列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="tmExceptionHandleBillEntity" class="form">
                            <form:hidden path="orgId"/>
                            <form:hidden path="baseOrgId"/>
                            <table class="table well">
                                <tr>
                                    <td class="width-10"><label class="pull-right">异常类型</label></td>
                                    <td class="width-15">
                                        <form:select path="exceptionType" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('exception_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                    <td class="width-10"><label class="pull-right">登记人</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择用户" url="${ctx}/sys/user/loginName" cssClass="form-control"
                                                  fieldId="" fieldName="" fieldKeyName=""
                                                  displayFieldId="registerPerson" displayFieldName="registerPerson" displayFieldKeyName="loginName"
                                                  fieldLabels="登录名|工号|姓名" fieldKeys="loginName|no|name"
                                                  searchLabels="登录名|工号|姓名" searchKeys="loginName|no|name"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">登记日期从</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='registerTimeFrom'>
                                            <input type='text' name="registerTimeFrom" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                    <td class="width-10"><label class="pull-right">登记日期到</label></td>
                                    <td class="width-15">
                                        <div class='input-group date' id='registerTimeTo'>
                                            <input type='text' name="registerTimeTo" class="form-control"/>
                                            <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">异常单号</label></td>
                                    <td class="width-15">
                                        <form:input path="billNo" htmlEscape="false" maxlength="20" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">运输单号</label></td>
                                    <td class="width-15">
                                        <form:input path="transportNo" htmlEscape="false" maxlength="20" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户订单号</label></td>
                                    <td class="width-15">
                                        <form:input path="customerNo" htmlEscape="false" maxlength="20" class="form-control "/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">状态</label></td>
                                    <td class="width-15">
                                        <form:select path="billStatus" class="form-control">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('TMS_EXCEPTION_BILL_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="width-10"><label class="pull-right">责任人</label></td>
                                    <td class="width-15">
                                        <form:input path="liabilityPerson" htmlEscape="false" class="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">发生地点</label></td>
                                    <td class="width-15">
                                        <sys:area id="happenSysArea" name="happenSysAreaId" value=""
                                                  labelName="happenSysAreaName" labelValue=""
                                                  cssClass="form-control" allowSearch="true"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">网点</label></td>
                                    <td class="width-15">
                                        <sys:grid title="网点" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="outletCode" fieldName="outletCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="outletName" displayFieldName="outletName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="网点编码|网点名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="网点编码|网点名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">客户</label></td>
                                    <td class="width-15">
                                        <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                                  fieldId="customerCode" fieldName="customerCode" fieldKeyName="transportObjCode"
                                                  displayFieldId="customerName" displayFieldName="customerName" displayFieldKeyName="transportObjName"
                                                  fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                                  searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                                  queryParams="transportObjType|orgId" queryParamValues="customerType|baseOrgId"
                                                  cssClass="form-control"/>
                                    </td>
                                </tr>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="tms:order:tmExceptionHandle:add">
                    <a id="add" class="btn btn-primary" onclick="add()">新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:addFeeShare">
                    <a id="addFeeShare" class="btn btn-primary" onclick="addFeeShare()">分摊</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:submit">
                    <button id="submit" class="btn btn-primary" disabled onclick="submit()">提交</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:audit">
                    <button id="audit" class="btn btn-primary" disabled onclick="audit()">审核</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="tms:order:tmExceptionHandle:returnBack">
                    <button id="returnBack" class="btn btn-primary" disabled onclick="returnBack()">退回</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="tmExceptionHandleBillTable" class="text-nowrap" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>