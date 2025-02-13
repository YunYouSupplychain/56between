<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>门店垫资方关联关系管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <%@ include file="sysSmsStoreAdvanceRelationList.js" %>
</head>
<body>
<div class="hide">
    <input id="storeType" type="hidden" value="CONSIGNEE"/>
    <input id="advanceType" type="hidden" value="ADVANCE"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">门店垫资方关联关系列表</h3>
        </div>
        <div class="panel-body">
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="sysSmsStoreAdvanceRelationEntity">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td class="width-10"><label class="pull-right">门店</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择门店" url="${ctx}/sys/common/sms/customer/grid" cssClass="form-control"
                                                  fieldId="storeCode" fieldName="storeCode" fieldKeyName="code"
                                                  displayFieldId="storeName" displayFieldName="storeName" displayFieldKeyName="nameCn"
                                                  fieldLabels="门店编码|门店名称" fieldKeys="code|nameCn"
                                                  searchLabels="门店编码|门店名称" searchKeys="code|nameCn"
                                                  queryParams="dataSet|customerType" queryParamValues="dataSet|storeType"/>
                                    </td>
                                    <td class="width-10"><label class="pull-right">垫资方</label></td>
                                    <td class="width-15">
                                        <sys:grid title="选择垫资方" url="${ctx}/sys/common/sms/customer/grid" cssClass="form-control"
                                                  fieldId="advanceCode" fieldName="advanceCode" fieldKeyName="code"
                                                  displayFieldId="advanceName" displayFieldName="advanceName" displayFieldKeyName="nameCn"
                                                  fieldLabels="垫资方编码|垫资方名称" fieldKeys="code|nameCn"
                                                  searchLabels="垫资方编码|垫资方名称" searchKeys="code|nameCn"
                                                  queryParams="dataSet|customerType" queryParamValues="dataSet|advanceType"/>
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
                <shiro:hasPermission name="sys:common:sms:storeAdvanceRelation:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sms:storeAdvanceRelation:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sms:storeAdvanceRelation:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:common:sms:storeAdvanceRelation:sync">
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
            <table id="sysSmsStoreAdvanceRelationTable" data-toolbar="#toolbar"></table>
        </div>
    </div>
</div>
</body>
</html>