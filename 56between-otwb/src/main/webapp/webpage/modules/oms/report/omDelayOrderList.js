<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	init();

	$('#omDelayOrderTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/oms/report/omDelayOrder/data",
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
		},
		{
			field: 'orderDate',
			title: '订单时间',
			sortable: true
		},
		{
			field: 'owner',
			title: '货主编码',
			sortable: true
		},
		{
			field: 'ownerName',
			title: '货主名称',
			sortable: true
		},
		{
			field: 'consignee',
			title: '收货人',
			sortable: true
		},
		{
			field: 'consigneeTel',
			title: '收货人电话',
			sortable: true
		},
		{
			field: 'consigneeAddress',
			title: '收货人地址',
			sortable: true
		},
		{
			field: 'consigneeAddressArea',
			title: '收货人区域',
			sortable: true
		},
		{
			field: 'chainNo',
			title: '供应链订单号',
			sortable: true
		},
		{
			field: 'customerNo',
			title: '客户订单号',
			sortable: true
		},
		{
			field: 'def1',
			title: '外部单号',
			sortable: true
		},
		{
			field: 'skuCode',
			title: '缺货商品编码',
			sortable: true
		},
		{
			field: 'skuName',
			title: '缺货商品名称',
			sortable: true
		},
		{
			field: 'qty',
			title: '缺货数量',
			sortable: true
		}]
	});
	$("#search").click("click", function () {// 绑定查询按扭
		$('#omDelayOrderTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		init();
	});

	$("#orderDateFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#orderDateTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
});

function init() {
	$("#orderDateFm input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 00:00:00");
	$("#orderDateTo input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 23:59:59");
}

function exportData() {
	$('#orgId').val(jp.getCurrentOrg().orgId);
	bq.exportExcel("${ctx}/oms/report/omDelayOrder/export", "供应链订单卡单数据记录", $("#searchForm"), function () {
		jp.close();
	});
}

</script>