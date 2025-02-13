<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmCarrierRouteTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#tmCarrierRouteTable').bootstrapTable('refresh');
	});

});

function initTable() {
	$('#tmCarrierRouteTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/common/tms/carrierRouteRelation/data",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		columns: [{
			checkbox: true
		}, {
			field: 'code',
			title: '编码',
			sortable: true
			, formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'name',
			title: '名称',
			sortable: true
		}, {
			field: 'carrierName',
			title: '承运商',
			sortable: true
		}, {
			field: 'origin',
			title: '起始地',
			sortable: true
		}, {
			field: 'destination',
			title: '目的地',
			sortable: true
		}, {
			field: 'delFlag',
			title: '启用停用',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('del_flag'))}, value, "-");
			}
		}, {
			field: 'mileage',
			title: '标准里程(km)',
			sortable: true
		}, {
			field: 'time',
			title: '标准时效(h)',
			sortable: true
		}, {
			field: 'dataSetName',
			title: '数据套',
			sortable: true
		}]
	});
	$('#tmCarrierRouteTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var ids = getIdSelections();
		$('#remove').prop('disabled', !ids.length);
		$('#edit').prop('disabled', ids.length !== 1);
		$('#enable').prop('disabled', !ids.length);
		$('#unable').prop('disabled', !ids.length);
		$('#syncSelect').prop('disabled', !length);
	});
}

function getIdSelections() {
	return $.map($("#tmCarrierRouteTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该路由信息记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/tms/carrierRouteRelation/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmCarrierRouteTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('承运商路由信息', "${ctx}/sys/common/tms/carrierRouteRelation/form", '100%', '100%', $('#tmCarrierRouteTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('承运商路由信息', "${ctx}/sys/common/tms/carrierRouteRelation/form?id=" + id, '100%', '100%', $('#tmCarrierRouteTable'));
}

function enable(flag) {
	jp.loading();
	jp.post("${ctx}/sys/common/tms/carrierRouteRelation/enable?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
		if (data.success) {
			$('#tmCarrierRouteTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

function syncSelect() {
	var rowIds = getIdSelections();
	if (rowIds.length < 1) {
		jp.warning("请勾选需要同步的记录");
		return;
	}
	jp.loading("同步中...");
	jp.post("${ctx}/sys/common/tms/carrierRouteRelation/syncSelect", {"ids": rowIds.join(',')}, function (data) {
		jp.alert(data.msg);
	});
}

function syncAll() {
	jp.confirm("确认同步全部数据？", function () {
		jp.loading("同步中...");
		jp.get("${ctx}/sys/common/tms/carrierRouteRelation/syncAll", function (data) {
			jp.alert(data.msg);
		});
	});
}
</script>