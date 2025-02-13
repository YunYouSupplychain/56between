<%@ page contentType="text/html;charset=UTF-8"%>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#tmDispatchOrderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $("#baseOrgId").val(tmOrg.id);
    });
});

function initTable() {
    var $table = $('#tmDispatchOrderTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/tmDispatchOrder/transportCheckDispatchPage",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'dispatchNo',
            title: '派车单号',
            sortable: true
        }, {
            field: 'dispatchStatus',
            title: '派车单状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DISPATCH_STATUS'))}, value, "-");
            }
        }, {
            field: 'dispatchTime',
            title: '派车时间',
            sortable: true
        }, {
            field: 'dispatchType',
            title: '派车单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_DISPATCH_TYPE'))}, value, "-");
            }
        }, {
            field: 'transportType',
            title: '运输方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_METHOD'))}, value, "-");
            }
        }, {
            field: 'dispatchOutletName',
            title: '派车网点',
            sortable: true
        }, {
            field: 'dispatchPlanNo',
            title: '调度计划单号',
            sortable: true
        }, {
            field: 'dispatcher',
            title: '派车人',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        }, {
            field: 'carNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'driverName',
            title: '司机',
            sortable: true
        }, {
            field: 'copilotName',
            title: '副驾驶',
            sortable: true
        }, {
            field: 'driverTel',
            title: '司机电话',
            sortable: true
        }, {
            field: 'isException',
            title: '是否异常',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'isAppInput',
            title: '是否APP录入',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'totalQty',
            title: '发车总件数',
            sortable: true
        }, {
            field: 'receivedQty',
            title: '实收件数',
            sortable: true
        }, {
            field: 'totalWeight',
            title: '总重量',
            sortable: true
        }, {
            field: 'totalCubic',
            title: '总体积',
            sortable: true
        }, {
            field: 'totalAmount',
            title: '费用金额',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    });
}
</script>