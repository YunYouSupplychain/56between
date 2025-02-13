<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>门店操作员关联关系管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysSmsStoreClerkRelationList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">门店操作员关联关系列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysSmsStoreClerkRelationEntity">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">操作人</label></td>
                                    <td class="width-15">
                                        <sys:popSelect url="${ctx}/sys/user/popDate" title="选择用户" cssClass="form-control"
                                                       fieldId="clerk" fieldName="clerk" fieldKeyName="id" fieldValue=""
                                                       displayFieldId="clerkName" displayFieldName="clerkName" displayFieldKeyName="name" displayFieldValue=""
                                                       selectButtonId="clerkSelectButton" deleteButtonId="clerkDelButton"
                                                       fieldLabels="工号|姓名|登录名" fieldKeys="no|name|loginName"
                                                       searchLabels="工号|姓名|登录名" searchKeys="no|name|loginName">
                                        </sys:popSelect>
                                    </td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                    <td class="width-10"></td>
                                    <td class="width-15"></td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="sys:common:sms:storeClerkRelation:add">
                    <button id="add" class="btn btn-primary" onclick="add()"> 新建</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sms:storeClerkRelation:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sms:storeClerkRelation:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteByClerk()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sms:storeClerkRelation:sync">
                    <button id="syncSelect" class="btn btn-primary" onclick="syncSelect()"> 同步选择数据</button>
                    <button id="syncAll" class="btn btn-primary" onclick="syncAll()"> 同步全部数据</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="sysSmsStoreClerkRelationTable" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>