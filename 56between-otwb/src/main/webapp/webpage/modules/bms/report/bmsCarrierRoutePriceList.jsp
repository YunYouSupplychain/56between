<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>承运商路由价格管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsCarrierRoutePriceList.js" %>
</head>
<body>
<div class="hide">
    <input type="hidden" id="carrierTyep" value="CARRIER"/>
    <input type="hidden" id="orgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading"><h3 class="panel-title">承运商路由价格列表</h3></div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <form:form id="searchForm" modelAttribute="bmsCarrierRoutePrice" class="form well clearfix" cssStyle="margin-bottom: 0;">
                <table class="table table-bordered  table-condensed dataTables-example dataTable no-footer" style="margin-bottom: 0;">
                    <tr>
                        <td class="width-10"><label class="pull-right">承运商</label></td>
                        <td class="width-15">
                            <sys:grid title="选择承运商" url="${ctx}/bms/basic/bmsCustomer/pop" cssClass="form-control"
                                      fieldId="carrierCode" fieldName="carrierCode" fieldKeyName="ebcuCustomerNo"
                                      displayFieldId="carrierName" displayFieldName="carrierName" displayFieldKeyName="ebcuNameCn"
                                      fieldLabels="承运商编码" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                      searchLabels="承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                      queryParams="ebcuType|orgId" queryParamValues="carrierTyep|orgId"/>
                        </td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                        <td class="width-10"></td>
                        <td class="width-15"></td>
                    </tr>
                </table>
            </form:form>
            <!-- 工具栏 -->
            <div id="toolbar">
                <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-refresh"></i> 重置</a>
                <shiro:hasPermission name="bms:report:carrierRoutePrice:export">
                    <a id="export" class="btn btn-primary btn-rounded btn-bordered btn-sm">
                        <i class="glyphicon glyphicon-export icon-share"></i> 导出
                    </a>
                </shiro:hasPermission>
            </div>
            <!-- 表格 -->
            <table id="bmsCarrierRoutePriceTable" data-toolbar="#toolbar" class="text-nowrap"></table>
        </div>
    </div>
</div>
</body>
</html>