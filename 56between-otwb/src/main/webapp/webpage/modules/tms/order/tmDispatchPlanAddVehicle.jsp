<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>追加需求计划</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script>
        $(document).ready(function () {
            $('#vehicleTable').bootstrapTable({
                cache: false,// 是否使用缓存
                pagination: true,// 是否显示分页
                sidePagination: "server",// client客户端分页，server服务端分页
                queryParams: function (params) {
                    var searchParam = $("#queryForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'carNo',
                    title: '车牌号',
                    sortable: true
                }]
            });
        });

        function getNoSelections() {
            return $.map($('#vehicleTable').bootstrapTable('getSelections'), function (row) {
                return row.carNo
            });
        }

        function save(index) {
            var carNos = getNoSelections();
            if (carNos.length === 0) {
                jp.bqError("请选择一条记录");
                return;
            }
            jp.loading();
            jp.post("${ctx}/tms/order/tmDispatchPlan/addVehicleConfirm", {planNo: $('#planNo').val(), orgId: $('#orgId').val(), carNos: carNos.join(',')}, function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    jp.close(index);
                } else {
                    jp.bqError(data.msg);
                }
            })
        }

        function queryDetail() {
            $("#vehicleTable").bootstrapTable('refresh', {url:"${ctx}/tms/basic/tmVehicle/grid"});
        }

        function resetForm() {
            $("#queryForm table input").val("");
        }

    </script>
</head>
<body>
<div class="hidden">
    <input id="planNo" name="planNo" value="${entity.planNo}" type="hidden">
    <input id="orgId" name="orgId" value="${entity.orgId}" type="hidden">
    <input id="baseOrgId" name="baseOrgId" value="${entity.baseOrgId}" type="hidden">
</div>
<div class="modal-body">
    <fieldset>
        <legend>查询条件</legend>
        <form id="queryForm">
            <input id="status" name="status" value="00" type="hidden">
            <table class="bq-table">
            <tr>
                <td><label class="pull-left">车牌号</label></td>
                <td><input id="carNo" name="carNo" class="form-control" maxlength="32"/></td>
                <td>
                    <button type="button" class="btn btn-primary btn-sm" onclick="queryDetail()"> &nbsp;查询&nbsp; </button>
                    <button type="button" class="btn btn-danger btn-sm" onclick="resetForm()"> &nbsp;重置&nbsp; </button>
                </td>
            </tr>
        </table>
        </form>
    </fieldset>
</div>
<div style="padding-left: 15px; padding-right: 15px;">
    <table id="vehicleTable" class="text-nowrap table-condensed"></table>
</div>
</body>
</html>