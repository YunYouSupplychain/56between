<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>称重设备表管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="banQinWeighMachineInfoList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">称重设备表列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="banQinWeighMachineInfoEntity">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">设备编码</label></td>
                                    <td class="width-15">
                                        <form:input path="machineNo" htmlEscape="false" maxlength="64" class=" form-control"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">机构</label></td>
                                    <td class="width-15">
                                        <input type="hidden" id="currentOrgId">
                                        <sys:popSelect url="${ctx}/sys/office/companyData" title="选择机构" cssClass="form-control"
                                                       fieldId="orgId" fieldName="orgId" fieldKeyName="id" fieldValue=""
                                                       displayFieldId="orgName" displayFieldName="orgName" displayFieldKeyName="name" displayFieldValue=""
                                                       selectButtonId="orgSelectButton" deleteButtonId="orgDelButton"
                                                       fieldLabels="仓别编码|仓别名称" fieldKeys="code|name"
                                                       searchLabels="仓别编码|仓别名称" searchKeys="code|name"
                                                       queryParams="id" queryParamValues="currentOrgId">
                                        </sys:popSelect>
                                    </td>
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
                <shiro:hasPermission name="wms:weigh:banQinWeighMachineInfo:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="wms:weigh:banQinWeighMachineInfo:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="wms:weigh:banQinWeighMachineInfo:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="banQinWeighMachineInfoTable" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>