<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orderTimeFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#orderTimeTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#baseOrgId").val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
        $("#tmOrderLabelRouteTable").bootstrapTable('refresh', {url: "${ctx}/tms/order/transport/route/page"});
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
        $("#baseOrgId").val(tmOrg.id);
	});
});

function initTable() {
    $('#tmOrderLabelRouteTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
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
            field: 'transportNo',
            title: '运输订单',
            sortable: true
        }, {
            field: 'orderTime',
            title: '受理时间',
            sortable: true
        }, {
            field: 'labelNo',
            title: '标签号',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        }, {
            field: 'dispatchNo',
            title: '派车单',
            sortable: true,
            formatter: function (value) {
                return value === "*" ? "-" : value;
            }
        }, {
            field: 'preOutletName',
            title: '上一站经过网点',
            sortable: true
        }, {
            field: 'nowOutletName',
            title: '货物当前所在网点',
            sortable: true
        }, {
            field: 'nextOutletName',
            title: '下一站派送网点',
            sortable: true
        }, {
            field: 'shipName',
            title: '发货方',
            sortable: true
        }, {
            field: 'shipper',
            title: '发货人',
            sortable: true
        }, {
            field: 'shipperTel',
            title: '发货人电话',
            sortable: true
        }, {
            field: 'shipCity',
            title: '发货城市',
            sortable: true
        }, {
            field: 'shipAddress',
            title: '发货人地址',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货方',
            sortable: true
        }, {
            field: 'consignee',
            title: '收货人',
            sortable: true
        }, {
            field: 'consigneeTel',
            title: '收货人电话',
            sortable: true
        }, {
            field: 'consigneeCity',
            title: '目的地城市',
            sortable: true
        }, {
            field: 'consigneeAddress',
            title: '收货人地址',
            sortable: true
        }]
    });
}
</script>