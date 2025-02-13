<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>承运商服务范围管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="omCarrierServiceScopeList.js" %>
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
            <sys:message content="${message}"/>
            <!-- 搜索 -->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body collapse">
                    <div class="accordion-inner">
                        <form:form id="searchForm" modelAttribute="omCarrierServiceScopeEntity">
                            <table class="table well">
                                <tbody>
                                <tr>
                                    <td style="width: 10%;"><label class="pull-right">货主</label></td>
                                    <td style="width: 15%;">
                                        <sys:popSelect url="${ctx}/oms/basic/omCustomer/popData" title="选择货主" cssClass="form-control"
                                                       fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ebcuCustomerNo" fieldValue=""
                                                       displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ebcuNameCn" displayFieldValue=""
                                                       selectButtonId="ownerSelectId" deleteButtonId="ownerDeleteId"
                                                       fieldLabels="货主编码|货主名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="货主编码|货主名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                       queryParams="ebcuType" queryParamValues="ownerType">
                                        </sys:popSelect>
                                    </td>
                                    <td style="width: 10%;"><label class="pull-right">承运商</label></td>
                                    <td style="width: 15%;">
                                        <sys:popSelect title="选择承运商" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                                       fieldId="carrierCode" fieldKeyName="ebcuCustomerNo" fieldName="carrierCode" fieldValue=""
                                                       displayFieldId="carrierName" displayFieldKeyName="ebcuNameCn" displayFieldName="carrierName" displayFieldValue=""
                                                       fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                                       searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                                       selectButtonId="carrierSBtnId" deleteButtonId="carrierDBtnId"
                                                       queryParams="ebcuType" queryParamValues="carrierType"/>
                                    </td>
                                    <td style="width: 10%;"><label class="pull-right">业务服务范围</label></td>
                                    <td style="width: 15%;">
                                        <sys:popSelect title="选择业务服务范围" url="${ctx}/oms/basic/omCustomer/popData" cssClass="form-control"
                                                       fieldId="groupCode" fieldKeyName="groupCode" fieldName="groupCode" fieldValue=""
                                                       displayFieldId="groupName" displayFieldKeyName="groupName" displayFieldName="groupName" displayFieldValue=""
                                                       fieldLabels="业务服务范围编码|业务服务范围名称" fieldKeys="groupCode|groupName"
                                                       searchLabels="业务服务范围编码|业务服务范围名称" searchKeys="groupCode|groupName"
                                                       selectButtonId="groupSBtnId" deleteButtonId="groupDBtnId"/>
                                    </td>
                                    <td style="width: 10%;"></td>
                                    <td style="width: 15%;"></td>
                                </tr>
                                </tbody>
                            </table>
                        </form:form>
                    </div>
                </div>
            </div>
            <!-- 工具栏 -->
            <div id="toolbar">
                <shiro:hasPermission name="basic:omCarrierServiceScope:add">
                    <a id="add" class="btn btn-primary" onclick="add()"> 新建</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:omCarrierServiceScope:edit">
                    <button id="edit" class="btn btn-primary" disabled onclick="edit()"> 修改</button>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:omCarrierServiceScope:del">
                    <button id="remove" class="btn btn-danger" disabled onclick="deleteAll()"> 删除</button>
                </shiro:hasPermission>
                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                <a class="accordion-toggle btn btn-default" data-toggle="collapse" data-parent="#accordion2" href="#collapseTwo">
                    <i class="fa fa-search"></i> 检索
                </a>
            </div>
            <!-- 表格 -->
            <table id="omCarrierServiceScopeTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>