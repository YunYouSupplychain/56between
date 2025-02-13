<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#sysSmsCarTypeTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#sysSmsCarTypeTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#sysSmsCarTypeTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/common/sms/carType/data",
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
			field: 'typeCode',
			title: '车辆类型编码',
			sortable: true
			, formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'typeName',
			title: '车辆类型名称',
			sortable: true
		}, {
			field: 'dataSetName',
			title: '数据套',
			sortable: true
		}, {
			field: 'def1',
			title: '自定义1',
			sortable: true
		}, {
			field: 'def2',
			title: '自定义2',
			sortable: true
		}, {
			field: 'def3',
			title: '自定义3',
			sortable: true
		}, {
			field: 'def4',
			title: '自定义4',
			sortable: true
		}, {
			field: 'def5',
			title: '自定义5',
			sortable: true
		}]
	});
	$('#sysSmsCarTypeTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#sysSmsCarTypeTable').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#syncSelect').prop('disabled', !length);
	});
}

function getIdSelections() {
	return $.map($("#sysSmsCarTypeTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该车辆类型记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/sms/carType/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#sysSmsCarTypeTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

function add() {
	jp.openDialog('新增车辆类型', "${ctx}/sys/common/sms/carType/form", '100%', '100%', $('#sysSmsCarTypeTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	<shiro:hasPermission name="sys:common:sms:carType:edit">
	jp.openDialog('编辑车辆类型', "${ctx}/sys/common/sms/carType/form?id=" + id,'100%', '100%', $('#sysSmsCarTypeTable'));
	</shiro:hasPermission>
	<shiro:lacksPermission name="sys:common:sms:carType:edit">
	jp.openDialogView('查看车辆类型', "${ctx}/sys/common/sms/carType/form?id=" + id,'100%', '100%', $('#sysSmsCarTypeTable'));
	</shiro:lacksPermission>
}

function syncSelect() {
	var rowIds = getIdSelections();
	if (rowIds.length < 1) {
		jp.warning("请勾选需要同步的记录");
		return;
	}
	jp.loading("同步中...");
	jp.post("${ctx}/sys/common/sms/carType/syncSelect", {"ids": rowIds.join(',')}, function (data) {
		jp.alert(data.msg);
	});
}

function syncAll() {
	jp.confirm("确认同步全部数据？", function () {
		jp.loading("同步中...");
		jp.get("${ctx}/sys/common/sms/carType/syncAll", function (data) {
			jp.alert(data.msg);
		});
	});
}
</script>