<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#sysBmsInvoiceObjectTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#sysBmsInvoiceObjectTable').bootstrapTable('refresh');
	});

});

function initTable() {
	$('#sysBmsInvoiceObjectTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/common/bms/invoiceObject/data",
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
			field: 'principal',
			title: '负责人',
			sortable: true
		}, {
			field: 'bank',
			title: '开户行',
			sortable: true
		}, {
			field: 'bankAccount',
			title: '银行账户',
			sortable: true
		}, {
			field: 'phone',
			title: '电话',
			sortable: true
		}, {
			field: 'address',
			title: '地址',
			sortable: true
		}, {
			field: 'dataSetName',
			title: '数据套',
			sortable: true
		}, {
			field: 'remarks',
			title: '备注信息',
			sortable: true
		}]
	});

	$('#sysBmsInvoiceObjectTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#sysBmsInvoiceObjectTable').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#syncSelect').prop('disabled', !length);
	});
}

function getIdSelections() {
	return $.map($("#sysBmsInvoiceObjectTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该开票对象记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/bms/invoiceObject/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#sysBmsInvoiceObjectTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('新增开票对象', "${ctx}/sys/common/bms/invoiceObject/form", '100%', '100%', $('#sysBmsInvoiceObjectTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	<shiro:hasPermission name = "sys:common:bms:invoiceObject:edit" >
		jp.openDialog('编辑开票对象', "${ctx}/sys/common/bms/invoiceObject/form?id=" + id, '100%', '100%', $('#sysBmsInvoiceObjectTable'));
	</shiro:hasPermission>
	<shiro:lacksPermission name = "sys:common:bms:invoiceObject:edit" >
		jp.openDialogView('查看开票对象', "${ctx}/sys/common/bms/invoiceObject/form?id=" + id, '100%', '100%', $('#sysBmsInvoiceObjectTable'));
	</shiro:lacksPermission>
}

function syncSelect() {
	var rowIds = getIdSelections();
	if (rowIds.length < 1) {
		jp.warning("请勾选需要同步的记录");
		return;
	}
	jp.loading("同步中...");
	jp.post("${ctx}/sys/common/bms/invoiceObject/syncSelect", {"ids": rowIds.join(',')}, function (data) {
		jp.alert(data.msg);
	});
}

function syncAll() {
	jp.confirm("确认同步全部数据？", function () {
		jp.loading("同步中...");
		jp.get("${ctx}/sys/common/bms/invoiceObject/syncAll", function (data) {
			jp.alert(data.msg);
		});
	});
}

</script>
