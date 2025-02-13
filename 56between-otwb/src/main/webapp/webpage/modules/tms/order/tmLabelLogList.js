<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$("#tmLabelLogTable").bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
	});
});

function initTable() {
    var $table = $('#tmLabelLogTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url:"${ctx}/tms/order/tmLabelLog/data",
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.baseOrgId = tmOrg.id;
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            field: 'labelNo',
            title: '标签号',
            sortable: true
        }, {
            field: 'opType',
            title: '操作类型',
            sortable: true,
            formatter: function (value) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_LABEL_OP_TYPE'))}, value, "-");
            }
        }, {
            field: 'opTime',
            title: '操作时间',
            sortable: true
        }, {
            field: 'transportNo',
            title: '运输订单',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        }, {
            field: 'dispatchNo',
            title: '派车单',
            sortable: true
        }, {
            field: 'preOutletName',
            title: '上一个网点'
        }, {
            field: 'nowOutletName',
            title: '当前所在网点'
        }, {
            field: 'nextOutletName',
            title: '计划派往网点'
        }, {
            field: 'shipCity',
            title: '发货城市'
        }, {
            field: 'shipCode',
            title: '发货方编码'
        }, {
            field: 'shipName',
            title: '发货方名称'
        }, {
            field: 'shipper',
            title: '发货人'
        }, {
            field: 'shipperTel',
            title: '发货人电话'
        }, {
            field: 'shipAddress',
            title: '发货人地址'
        }, {
            field: 'consigneeCity',
            title: '目的地城市'
        }, {
            field: 'consigneeCode',
            title: '收货方编码'
        }, {
            field: 'consigneeName',
            title: '收货方名称'
        }, {
            field: 'consignee',
            title: '收货人'
        }, {
            field: 'consigneeTel',
            title: '收货人电话'
        }, {
            field: 'consigneeAddress',
            title: '收货人地址'
        }, {
            field: 'carrierName',
            title: '承运商'
        }, {
            field: 'vehicleNo',
            title: '车牌号'
        }, {
            field: 'ownerName',
            title: '货主'
        }, {
            field: 'skuName',
            title: '商品'
        }, {
            field: 'weight',
            title: '重量'
        }, {
            field: 'volume',
            title: '体积'
        }]
    });
}

</script>
