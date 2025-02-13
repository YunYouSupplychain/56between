<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>拣货单管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWmPickHeaderList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">拣货列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWmPickHeaderEntity" class="form">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10">
                                        <label class="pull-right" title="拣货单号">拣货单号</label>
                                    </td>
                                    <td class="width-15">
                                        <form:input path="pickNo" htmlEscape="false" maxlength="32" class=" form-control"/>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="创建人">创建人</label>
                                    </td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/sys/user/popDate" title="选择用户" cssClass="form-control"
                                                       fieldId="createById" fieldName="createBy.id" fieldKeyName="id" fieldValue=""
                                                       displayFieldId="createByName" displayFieldName="createBy.name" displayFieldKeyName="name" displayFieldValue=""
                                                       selectButtonId="createBySelectButton" deleteButtonId="createByDelButton"
                                                       fieldLabels="登录名|姓名" fieldKeys="loginName|name"
                                                       searchLabels="登录名|姓名" searchKeys="loginName|name">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="创建时间从">&nbsp;创建时间从</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='createDateFm'>
                                            <input type='text' name="createDateFm" class="form-control"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
                                        </div>
                                    </td>
                                    <td class="width-10">
                                        <label class="pull-right" title="创建时间到">&nbsp;创建时间到</label>
                                    </td>
                                    <td class="width-15">
                                        <div class='input-group date' id='createDateTo'>
                                            <input type='text' name="createDateTo" class="form-control"/>
                                            <span class="input-group-addon">
                                                <span class="glyphicon glyphicon-calendar"></span>
                                            </span>
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
                <shiro:hasPermission name="outbound:banQinWmPickHeader:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown">打印<span class="caret"></span></button>
                    <ul class="dropdown-menu">
                        <shiro:hasPermission name="outbound:banQinWmPickHeader:printPickOrder">
                            <li><a id="printPickOrder" onclick="printPickOrder()">打印拣货单</a></li>
                        </shiro:hasPermission>
                    </ul>
                </div>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWmPickHeaderTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>