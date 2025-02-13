<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmTransportScopeTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#tmTransportScopeTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#tmTransportScopeTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/common/tms/transportScope/data",
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
			title: '服务范围编码',
			sortable: true
			, formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'name',
			title: '服务范围名称',
			sortable: true
		}, {
			field: 'remarks',
			title: '服务范围描述',
			sortable: true
		}, {
			field: 'dataSetName',
			title: '数据套',
			sortable: true
		}, {
			field: 'operate',
			title: '操作',
			align: 'center',
			events: {
				'click .area': function (e, value, row, index) {
					jp.openDialog('区域管理 - 【' + row.code + "/" + row.name + "】", '${ctx}/sys/common/tms/transportScope/area?id=' + row.id, '1000px', '650px');
				}
			},
			formatter: function operateFormatter(value, row, index) {
				return ['<a href="#" class="area" title="区域设置"><i class="fa fa-cog"></i> </a>'].join('');
			}
		}]
	});
	$('#tmTransportScopeTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $("#tmTransportScopeTable").bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#syncSelect').prop('disabled', !length);
	});
}

function getIdSelections() {
	return $.map($("#tmTransportScopeTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该业务服务范围记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/tms/transportScope/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmTransportScopeTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('业务服务范围', "${ctx}/sys/common/tms/transportScope/form", '800px', '500px', $('#tmTransportScopeTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('业务服务范围', "${ctx}/sys/common/tms/transportScope/form?id=" + id, '800px', '500px', $('#tmTransportScopeTable'));
}

function syncSelect() {
	var rowIds = getIdSelections();
	if (rowIds.length < 1) {
		jp.warning("请勾选需要同步的记录");
		return;
	}
	jp.loading("同步中...");
	jp.post("${ctx}/sys/common/tms/transportScope/syncSelect", {"ids": rowIds.join(',')}, function (data) {
		jp.alert(data.msg);
	});
}

function syncAll() {
	jp.confirm("确认同步全部数据？", function () {
		jp.loading("同步中...");
		jp.get("${ctx}/sys/common/tms/transportScope/syncAll", function (data) {
			jp.alert(data.msg);
		});
	});
}

</script>