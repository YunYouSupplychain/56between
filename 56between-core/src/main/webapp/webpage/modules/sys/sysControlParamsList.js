<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#sysControlParamsTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/sysControlParams/data",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		columns: [{
			checkbox: true
		},{
			field: 'code',
			title: '参数代码',
			sortable: true,
			formatter: function (value, row, index) {
				return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
			}
		},{
			field: 'value',
			title: '参数值',
			sortable: true
		},{
			field: 'orgName',
			title: '机构',
			sortable: true
		},{
			field: 'type',
			title: '类别',
			sortable: true
		},{
			field: 'remarks',
			title: '说明',
			sortable: true
		}]
	});
	$('#sysControlParamsTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#sysControlParamsTable').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
	});
	$("#search").click("click", function () {// 绑定查询按扭
		$('#sysControlParamsTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$('#sysControlParamsTable').bootstrapTable('refresh');
	});
});

function getIdSelections() {
	return $.map($("#sysControlParamsTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该系统控制参数记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/sysControlParams/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#sysControlParamsTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

function add() {
	jp.openDialog('新增系统控制参数', "${ctx}/sys/sysControlParams/form", '800px', '280px', $('#sysControlParamsTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('编辑系统控制参数', "${ctx}/sys/sysControlParams/form?id=" + id, '800px', '280px', $('#sysControlParamsTable'));
}

function view(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialogView('查看系统控制参数', "${ctx}/sys/sysControlParams/form?id=" + id, '800px', '280px', $('#sysControlParamsTable'));
}

</script>