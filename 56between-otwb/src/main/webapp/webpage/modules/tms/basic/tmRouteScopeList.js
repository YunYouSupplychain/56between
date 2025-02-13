<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#orgId').val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmRouteScopeTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#tmRouteScopeTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#tmRouteScopeTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/basic/tmRouteScope/data",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.orgId = tmOrg.id;
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
			field: 'remarks',
			title: '备注信息',
			sortable: true
		}]
	});
	$('#tmRouteScopeTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $("#tmRouteScopeTable").bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
	});
}

function getIdSelections() {
	return $.map($("#tmRouteScopeTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmRouteScope/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmRouteScopeTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('路由范围', "${ctx}/tms/basic/tmRouteScope/form", '80%', '80%', $('#tmRouteScopeTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('路由范围', "${ctx}/tms/basic/tmRouteScope/form?id=" + id, '80%', '80%', $('#tmRouteScopeTable'));
}

function genRoute() {
	jp.loading();
	jp.get("${ctx}/tms/basic/tmRouteScope/genRoute?ids=" + getIdSelections(), function (data) {
		if (data.success) {
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

</script>