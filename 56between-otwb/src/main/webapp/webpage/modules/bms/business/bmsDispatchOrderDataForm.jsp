<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>派车单数据管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        function doSubmit($table, $topIndex) {
            jp.loading();
            var validate = bq.headerSubmitCheck("#inputForm");
            if (!validate.isSuccess) {
                jp.bqError(validate.msg);
                return false;
            }
            var disableObj = bq.openDisabled("#inputForm");
            var params = $('#inputForm').serialize();
            bq.closeDisabled(disableObj);
            jp.post("${ctx}/bms/business/bmsDispatchOrderData/save", params, function (data) {
                if (data.success) {
                    $table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close($topIndex);//关闭dialog
                } else {
                    jp.bqError(data.msg);
                }
            });
            return true;
        }

        $(document).ready(function () {
            $("#orderDate").datetimepicker({format: "YYYY-MM-DD"});
            $("#dispatchTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
            buildStoreTab();
        });

        function buildStoreTab() {
            $("#bmsDispatchOrderStoreDataTable").bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/bms/business/bmsDispatchOrderData/siteData",
                queryParams: function (params) {
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.headerId = $("#id").val();
                    searchParam.orgId = $("#orgId").val();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                columns: [{
                    field: 'dispatchSeq',
                    title: '配送顺序'
                }, {
                    field: 'outletCode',
                    title: '网点编码'
                }, {
                    field: 'outletName',
                    title: '网点名称'
                }, {
                    field: 'address',
                    title: '地址'
                }]
            });
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="bmsDispatchOrderDataEntity" class="form">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="driverCode"/>
    <form:hidden path="platform"/>
    <form:hidden path="sealNo"/>
    <form:hidden path="shift"/>
    <form:hidden path="totalQtyFrame"/>
    <form:hidden path="signBy"/>
    <form:hidden path="signTime"/>
    <form:hidden path="isFee"/>
    <sys:message content="${message}"/>
    <div class="tabs-container">
        <table class="table well">
            <tbody>
            <tr>
                <td class="width-10"><label class="pull-right">派车单号</label></td>
                <td class="width-15">
                    <form:input path="orderNo" htmlEscape="false" class="form-control"/>
                </td>
                <td class="width-10"><label class="pull-right">订单类型</label></td>
                <td class="width-15">
                    <form:select path="orderType" class="form-control">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('TMS_DISPATCH_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">订单日期</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='orderDate'>
                        <input type='text' name="orderDate" class="form-control"
                               value="<fmt:formatDate value="${bmsDispatchOrderDataEntity.orderDate}" pattern="yyyy-MM-dd"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
                <td class="width-10"><label class="pull-right">派车时间</label></td>
                <td class="width-15">
                    <div class='input-group form_datetime' id='dispatchTime'>
                        <input type='text' name="dispatchTime" class="form-control"
                               value="<fmt:formatDate value="${bmsDispatchOrderDataEntity.dispatchTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
                        <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                    </div>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">运输方式</label></td>
                <td class="width-15">
                    <form:select path="tranType" class="form-control">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('TMS_TRANSPORT_METHOD')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">承运商</label></td>
                <td class="width-15">
                    <input id="carrierType" type="hidden" value="CARRIER"/>
                    <sys:popSelect title="选择承运商" url="${ctx}/bms/basic/bmsCustomer/pop" cssClass="form-control"
                                   fieldId="carrierCode" fieldKeyName="ebcuCustomerNo"
                                   fieldName="carrierCode" fieldValue="${bmsDispatchOrderDataEntity.carrierCode}"
                                   displayFieldId="carrierName" displayFieldKeyName="ebcuNameCn"
                                   displayFieldName="carrierName" displayFieldValue="${bmsDispatchOrderDataEntity.carrierName}"
                                   selectButtonId="carrierSBtnId" deleteButtonId="carrierDBtnId"
                                   fieldLabels="承运商编码|承运商名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                                   searchLabels="承运商编码|承运商名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                                   queryParams="ebcuType" queryParamValues="carrierType"/>
                </td>
                <td class="width-10"><label class="pull-right">车型</label></td>
                <td class="width-15">
                    <form:select path="carType" class="form-control">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('TMS_CAR_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
                <td class="width-10"><label class="pull-right">车牌号</label></td>
                <td class="width-15">
                    <form:input path="vehicleNo" htmlEscape="false" class="form-control required"/>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">驾驶员</label></td>
                <td class="width-15">
                    <form:input path="driverName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
                <td class="width-10"><label class="pull-right">调度员</label></td>
                <td class="width-15">
                    <form:input path="dispatcher" htmlEscape="false" class="form-control"/>
                </td>
                <td class="width-10"><label class="pull-right">总EA数</label></td>
                <td class="width-15">
                    <form:input path="totalQtyEa" htmlEscape="false" class="form-control"/>
                </td>
                <td class="width-10"><label class="pull-right">数据来源</label></td>
                <td class="width-15">
                    <form:select path="dataSources" class="form-control">
                        <form:option value="" label=""/>
                        <form:options items="${fns:getDictList('SYS_DATA_SOURCE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                    </form:select>
                </td>
            </tr>
            <tr>
                <td class="width-10"><label class="pull-right">机构</label></td>
                <td class="width-15">
                    <form:input path="orgName" htmlEscape="false" class="form-control" readonly="true"/>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</form:form>
<div class="tabs-container">
    <table id="bmsDispatchOrderStoreDataTable" class="text-nowrap"></table>
</div>
</body>
</html>