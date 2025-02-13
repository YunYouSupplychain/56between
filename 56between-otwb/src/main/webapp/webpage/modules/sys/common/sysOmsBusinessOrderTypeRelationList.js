<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	initTable();

	$("#search").click("click", function () {
		$('#sysOmsBusinessOrderTypeRelationTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#sysOmsBusinessOrderTypeRelationTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#sysOmsBusinessOrderTypeRelationTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/common/oms/businessOrderTypeRelation/data",
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
			field: 'businessOrderType',
			title: '业务订单类型',
			sortable: true,
			formatter: function (value, row, index) {
				return "<a href='javascript:view(\"" + row.id + "\")'>" + jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_BUSINESS_ORDER_TYPE'))}, value, "-") + "</a>"
			}
		}, {
			field: 'orderType',
			title: '作业任务类型',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_TASK_TYPE'))}, value, "-");
			}
		}, {
			field: 'pushSystem',
			title: '下发系统',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_PUSH_SYSTEM'))}, value, "-");
			}
		}, {
			field: 'dataSetName',
			title: '数据套',
			sortable: true
		}, {
			field: 'createDate',
			title: '创建时间',
			sortable: true
		}, {
			field: 'updateDate',
			title: '更新时间',
			sortable: true
		}]
	});
	$('#sysOmsBusinessOrderTypeRelationTable').on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
		var length = getIdSelections().length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#syncSelect').prop('disabled', !length);
	});
}

function getIdSelections() {
	return $.map($("#sysOmsBusinessOrderTypeRelationTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/oms/businessOrderTypeRelation/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#sysOmsBusinessOrderTypeRelationTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

function add() {
	jp.openDialog('新增订单类型关联关系', "${ctx}/sys/common/oms/businessOrderTypeRelation/form", '800px', '420px', $('#sysOmsBusinessOrderTypeRelationTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	<shiro:hasPermission name="sys:common:oms:businessOrderTypeRelation:edit">
		jp.openDialog('编辑订单类型关联', "${ctx}/sys/common/oms/businessOrderTypeRelation/form?id=" + id, '800px', '420px', $('#sysOmsBusinessOrderTypeRelationTable'));
	</shiro:hasPermission>
	<shiro:lacksPermission name="sys:common:oms:businessOrderTypeRelation:edit">
		jp.openDialogView('查看订单类型关联', "${ctx}/sys/common/oms/businessOrderTypeRelation/form?id=" + id, '800px', '420px', $('#sysOmsBusinessOrderTypeRelationTable'));
	</shiro:lacksPermission>
}

function view(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialogView('查看订单类型关联', "${ctx}/sys/common/oms/businessOrderTypeRelation/form?id=" + id, '800px', '420px', $('#sysOmsBusinessOrderTypeRelationTable'));
}

function syncSelect() {
	var rowIds = getIdSelections();
	if (rowIds.length < 1) {
		jp.warning("请勾选需要同步的记录");
		return;
	}
	jp.loading("同步中...");
	jp.post("${ctx}/sys/common/oms/businessOrderTypeRelation/syncSelect", {"ids": rowIds.join(',')}, function (data) {
		jp.alert(data.msg);
	});
}

function syncAll() {
	jp.confirm("确认同步全部数据？", function () {
		jp.loading("同步中...");
		jp.get("${ctx}/sys/common/oms/businessOrderTypeRelation/syncAll", function (data) {
			jp.alert(data.msg);
		});
	});
}
</script>