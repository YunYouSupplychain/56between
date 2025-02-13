<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>需求调度结果查询</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmDispatchPlanResultList.js" %>
    <style>
        #tmDispatchPlanResultTable>thead>tr>th,
        #tmDispatchPlanResultTable>tbody>tr>td {
            border: 1px solid black;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">需求调度结果列表</h3>
        </div>
        <div class="panel-body">
            <input id="planNo" value="${entity.planNo}" type="hidden">
            <input id="orgId" value="${entity.orgId}" type="hidden">
            <input id="baseOrgId" name="baseOrgId" type="hidden">
            <input id="ownerType" value="OWNER" type="hidden">
            <!-- 搜索 -->
            <div style="padding-bottom: 10px;">
            <fieldset>
                <legend>查询条件</legend>
                <form id="searchForm">
                    <table class="table table-condensed" style="margin-bottom: 10px">
                    <tr>
                        <td style="width:10%;"><label class="pull-right">分类</label></td>
                        <td style="width:15%;">
                            <select name="status" class="form-control m-b">
                                <option value=""></option>
                                <c:forEach items="${fns:getDictList('TMS_TRANSPORT_OBJ_CLASSIFICATION')}" var="dict">
                                    <option value="${dict.value}">${dict.label}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td style="width:10%;"><label class="pull-right">加油站</label></td>
                        <td style="width:15%;">
                            <sys:grid title="客户" url="${ctx}/tms/basic/tmTransportObj/grid"
                                      fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="transportObjCode"
                                      displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="transportObjName"
                                      fieldLabels="客户编码|客户名称" fieldKeys="transportObjCode|transportObjName"
                                      searchLabels="客户编码|客户名称" searchKeys="transportObjCode|transportObjName"
                                      queryParams="transportObjType|orgId" queryParamValues="ownerType|baseOrgId"
                                      cssClass="form-control"/>
                        </td>
                        <td style="width: 10%; vertical-align: middle;"><label class="pull-right">油品</label></td>
                        <td style="width: 15%; vertical-align: middle;">
                            <sys:grid url="${ctx}/tms/basic/tmItem/grid" title="油品" cssClass="form-control"
                                      fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode"
                                      displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName"
                                      fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                      queryParams="ownerCode|orgId" queryParamValues="ownerCode|baseOrgId"
                                      searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName">
                            </sys:grid>
                        </td>
                        <td style="width: 10%; vertical-align: middle;"></td>
                        <td style="width: 15%; vertical-align: middle;">
                            <button id="search" type="button" class="btn btn-primary"><i class="fa fa-search"></i>查询</button>
                            <button id="reset" type="button" class="btn btn-default"><i class="fa fa-refresh"></i>重置</button>
                        </td>
                    </tr>
                </table>
                </form>
            </fieldset>
            </div>
            <!-- 表格 -->
            <table id="tmDispatchPlanResultTable" class="table-condensed table-striped text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>