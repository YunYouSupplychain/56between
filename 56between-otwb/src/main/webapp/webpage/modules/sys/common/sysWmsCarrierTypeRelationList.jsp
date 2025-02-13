<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>承运商类型关系管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysWmsCarrierTypeRelationList.js" %>
</head>
<body>
<div class="hide">
    <input id="carrierType" value="CARRIER" type="hidden">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">承运商类型关系列表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysWmsCarrierTypeRelationEntity" class="form form-horizontal well clearfix">
                            <table class="table table-striped table-bordered table-condensed">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right" title="承运商：">承运商：</label></td>
                                    <td class="width-15">
                                        <sys:grid url="${ctx}/sys/common/wms/customer/grid" title="选择承运商" cssClass="form-control"
                                                  fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo"
                                                  displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn"
                                                  fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                  searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                  queryParams="ebcuType|dataSet" queryParamValues="carrierType|dataSet"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right" title="类型：">类型：</label></td>
                                    <td class="width-15">
                                        <form:select path="type" class="form-control m-b">
                                            <form:option value="" label=""/>
                                            <form:options items="${fns:getDictList('SYS_WM_CARRIER_TYPE_RELATION')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                                        </form:select>
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
                <shiro:hasPermission name="sys:common:wms:carrierTypeRelation:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:wms:carrierTypeRelation:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:wms:carrierTypeRelation:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:wms:carrierTypeRelation:sync">
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
            <table id="sysWmsCarrierTypeRelationTable" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>