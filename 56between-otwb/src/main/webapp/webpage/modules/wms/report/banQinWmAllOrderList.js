<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#orderTimeFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#orderTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#orderTimeFm input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 00:00:00");
	$("#orderTimeTo input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 23:59:59");

	$('#wmAllOrderTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/report/banQinWmAllOrder/data",
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
			field: 'customerOrderNo',
			title: '客户订单号',
			sortable: true
		}, {
			field: 'externalNo',
			title: '外部单号',
			sortable: true
		}, {
			field: 'soNo',
			title: '出库单号',
			sortable: true
		}, {
			field: 'trackingNo',
			title: '快递单号',
			sortable: true
		}, {
			field: 'soType',
			title: '出库单类型',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))}, value, "-");
			}
		}, {
			field: 'orderTime',
			title: '订单时间',
			sortable: true
		}, {
			field: 'ownerCode',
			title: '货主编码',
			sortable: true
		}, {
			field: 'ownerName',
			title: '货主名称',
			sortable: true
		}, {
			field: 'skuCode',
			title: '商品编码',
			sortable: true
		}, {
			field: 'skuName',
			title: '商品名称',
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
			field: 'consigneeAddress',
			title: '收货人地址',
			sortable: true
		}, {
			field: 'consigneeZip',
			title: '收货人邮编',
			sortable: true
		}, {
			field: 'consigneeArea',
			title: '收货人区域',
			sortable: true
		}, {
			field: 'deliveryName',
			title: '发货人',
			sortable: true
		}, {
			field: 'deliveryTel',
			title: '发货人电话',
			sortable: true
		}, {
			field: 'deliveryAddress',
			title: '发货人地址',
			sortable: true
		}, {
			field: 'deliveryZip',
			title: '发货人邮编',
			sortable: true
		}, {
			field: 'deliveryArea',
			title: '发货人区域',
			sortable: true
		}, {
			field: 'businessNo',
			title: '商流订单号',
			sortable: true
		}, {
			field: 'chainNo',
			title: '供应链订单号',
			sortable: true
		}, {
			field: 'taskNo',
			title: '供应链任务号',
			sortable: true
		}, {
			field: 'allocId',
			title: '分配明细ID',
			sortable: true
		}, {
			field: 'waveNo',
			title: '波次单号',
			sortable: true
		}, {
			field: 'locCode',
			title: '库位编码',
			sortable: true
		}, {
			field: 'traceId',
			title: '跟踪号',
			sortable: true
		}, {
			field: 'qtyEa',
			title: '数量',
			sortable: true
		}, {
			field: 'toLoc',
			title: '目标库位编码',
			sortable: true
		}, {
			field: 'toId',
			title: '目标跟踪号',
			sortable: true
		}, {
			field: 'pickOp',
			title: '拣货人',
			sortable: true
		}, {
			field: 'pickTime',
			title: '拣货时间',
			sortable: true
		}, {
			field: 'checkOp',
			title: '复核人',
			sortable: true
		}, {
			field: 'checkTime',
			title: '复核时间',
			sortable: true
		}, {
			field: 'packOp',
			title: '打包人',
			sortable: true
		}, {
			field: 'packTime',
			title: '打包时间',
			sortable: true
		}, {
			field: 'orgName',
			title: '仓库',
			sortable: true
		}, {
			field: 'caseNo',
			title: '打包箱号',
			sortable: true
		}, {
			field: 'carrierCode',
			title: '承运商编码',
			sortable: true
		}, {
			field: 'carrierName',
			title: '承运商名称',
			sortable: true
		}]
	});

	$("#search").click("click", function () {
		$('#wmAllOrderTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$('#wmAllOrderTable').bootstrapTable('refresh');
	});
});

function exportData() {
	$('#orgId').val(jp.getCurrentOrg().orgId);
	bq.exportExcel("${ctx}/wms/report/banQinWmAllOrder/export", "供应链全部订单数据记录", $("#searchForm"), function () {
		jp.close();
	});
}

</script>