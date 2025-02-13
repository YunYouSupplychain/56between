<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#orgId').val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmObjRouteTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#tmObjRouteTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#tmObjRouteTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/basic/tmObjRoute/data",
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
			field: 'carrierName',
			title: '承运商',
			sortable: true,
			formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'auditStatus',
			title: '审核状态',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_AUDIT_STATUS'))}, value, "-");
			}
		}, {
			field: 'startObjCode',
			title: '起点对象编码',
			sortable: true
		}, {
			field: 'startObjName',
			title: '起点对象名称',
			sortable: true
		}, {
			field: 'startObjAddress',
			title: '起点地址',
			sortable: true
		}, {
			field: 'endObjCode',
			title: '终点对象编码',
			sortable: true
		}, {
			field: 'endObjName',
			title: '终点对象名称',
			sortable: true
		}, {
			field: 'endObjAddress',
			title: '终点地址',
			sortable: true
		}, {
			field: 'mileage',
			title: '里程',
			sortable: true
		}]
	});
	$('#tmObjRouteTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $("#tmObjRouteTable").bootstrapTable('getSelections').length;
		$('#edit').prop('disabled', length !== 1);
		$('#remove').prop('disabled', !length);
		$('#audit').prop('disabled', !length);
		$('#batchAudit').prop('disabled', !length);
		$('#cancelAudit').prop('disabled', !length);
		$('#batchCancelAudit').prop('disabled', !length);
	});
}

function getIdSelections() {
	return $.map($("#tmObjRouteTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmObjRoute/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmObjRouteTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('业务对象路由', "${ctx}/tms/basic/tmObjRoute/form", '800px', '460px', $('#tmObjRouteTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('业务对象路由', "${ctx}/tms/basic/tmObjRoute/form?id=" + id, '800px', '460px', $('#tmObjRouteTable'));
}

function audit() {
	jp.loading();
	jp.get("${ctx}/tms/basic/tmObjRoute/audit?ids=" + getIdSelections(), function (data) {
		if (data.success) {
			$("#tmObjRouteTable").bootstrapTable('refresh')
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

function batchAudit() {
	jp.loading();
	var searchParam = $("#searchForm").serializeJSON();
	searchParam.orgId = tmOrg.id;
	jp.post("${ctx}/tms/basic/tmObjRoute/batchAudit", searchParam, function (data) {
		if (data.success) {
			$("#tmObjRouteTable").bootstrapTable('refresh')
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

function cancelAudit() {
	jp.loading();
	jp.get("${ctx}/tms/basic/tmObjRoute/cancelAudit?ids=" + getIdSelections(), function (data) {
		if (data.success) {
			$("#tmObjRouteTable").bootstrapTable('refresh')
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

function batchCancelAudit() {
	jp.loading();
	var searchParam = $("#searchForm").serializeJSON();
	searchParam.orgId = tmOrg.id;
	jp.post("${ctx}/tms/basic/tmObjRoute/batchCancelAudit", searchParam, function (data) {
		if (data.success) {
			$("#tmObjRouteTable").bootstrapTable('refresh')
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

</script>