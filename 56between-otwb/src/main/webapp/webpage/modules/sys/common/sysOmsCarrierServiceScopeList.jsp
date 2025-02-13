<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>承运商服务范围管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="sysOmsCarrierServiceScopeList.js" %>
</head>
<body>
<div class="hidden">
    <input type="hidden" id="ownerType" value="OWNER">
    <input type="hidden" id="carrierType" value="CARRIER"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">承运商服务范围列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysOmsCarrierServiceScopeEntity">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">货主</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择货主" url="${ctx}/sys/common/oms/customer/grid" cssClass="form-control"
                                                  fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo"
                                                  displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn"
                                                  fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                  searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                  queryParams="ebcuType|dataSet" queryParamValues="ownerType|dataSet"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">承运商</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择货主" url="${ctx}/sys/common/oms/customer/grid" cssClass="form-control"
                                                  fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo"
                                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn"
                                                  fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                  searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                  queryParams="ebcuType|dataSet" queryParamValues="carrierType|dataSet"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">业务服务范围</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择业务服务范围" url="${ctx}/sys/common/oms/businessServiceScope/grid" cssClass="form-control"
                                                  fieldId="groupCode" fieldName="groupCode" fieldKeyName="groupCode"
                                                  displayFieldId="groupName" displayFieldName="groupName" displayFieldKeyName="groupName"
                                                  fieldLabels="业务服务范围编码|业务服务范围名称" fieldKeys="groupCode|groupName"
                                                  searchLabels="业务服务范围编码|业务服务范围名称" searchKeys="groupCode|groupName"
                                                  queryParams="dataSet" queryParamValues="dataSet"/>
                                    </td>
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
                <shiro:hasPermission name="sys:common:oms:carrierServiceScope:add">
                    <a id="add" class="btn btn-primary" onclick="add()">新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:oms:carrierServiceScope:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()">修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:oms:carrierServiceScope:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()">删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:oms:carrierServiceScope:sync">
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
            <table id="sysOmsCarrierServiceScopeTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>