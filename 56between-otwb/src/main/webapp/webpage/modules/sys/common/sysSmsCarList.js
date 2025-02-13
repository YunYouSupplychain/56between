<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#sysSmsCarTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#sysSmsCarTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#sysSmsCarTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/common/sms/car/data",
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
			field: 'carNo',
			title: '车号',
			sortable: true
		}, {
			field: 'carrierCode',
			title: '承运商编码',
			sortable: true
		}, {
			field: 'carrierName',
			title: '承运商名称',
			sortable: true
		}, {
			field: 'carType',
			title: '车辆类型编码',
			sortable: true
		}, {
			field: 'carTypeName',
			title: '车辆类型名称',
			sortable: true
		}, {
			field: 'dataSetName',
			title: '数据套',
			sortable: true
		}, {
			field: 'engineNo',
			title: '发动机号',
			sortable: true
		}, {
			field: 'carryingCapacity',
			title: '承载量',
			sortable: true
		}, {
			field: 'safeCode',
			title: '保险证号',
			sortable: true
		}, {
			field: 'diver',
			title: '司机',
			sortable: true
		}, {
			field: 'phone',
			title: '手机号码',
			sortable: true
		}]
	});
	$('#sysSmsCarTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#sysSmsCarTable').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#syncSelect').prop('disabled', !length);
	});
}

function getIdSelections() {
	return $.map($("#sysSmsCarTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该车辆信息记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/sms/car/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#sysSmsCarTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

function add() {
	jp.openDialog('新增车辆信息', "${ctx}/sys/common/sms/car/form", '100%', '100%', $('#sysSmsCarTable'));
}
function edit(id){//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	<shiro:hasPermission name="sys:common:sms:car:edit">
	jp.openDialog('编辑车辆信息', "${ctx}/sys/common/sms/car/form?id=" + id,'100%', '100%', $('#sysSmsCarTable'));
	</shiro:hasPermission>
	<shiro:lacksPermission name="sys:common:sms:car:edit">
	jp.openDialogView('查看车辆信息', "${ctx}/sys/common/sms/car/form?id=" + id,'100%', '100%', $('#sysSmsCarTable'));
	</shiro:lacksPermission>
}

function syncSelect() {
	var rowIds = getIdSelections();
	if (rowIds.length < 1) {
		jp.warning("请勾选需要同步的记录");
		return;
	}
	jp.loading("同步中...");
	jp.post("${ctx}/sys/common/sms/car/syncSelect", {"ids": rowIds.join(',')}, function (data) {
		jp.alert(data.msg);
	});
}

function syncAll() {
	jp.confirm("确认同步全部数据？", function () {
		jp.loading("同步中...");
		jp.get("${ctx}/sys/common/sms/car/syncAll", function (data) {
			jp.alert(data.msg);
		});
	});
}

</script>