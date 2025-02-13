<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#orgId").val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmTransportScopeTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$("#orgId").val(tmOrg.id);
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
		url: "${ctx}/tms/basic/tmTransportScope/data",
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
			field: 'operate',
			title: '操作',
			align: 'center',
			events: {
				'click .area': function (e, value, row, index) {
					jp.openDialog('区域管理 - 【' + row.code + "/" + row.name + "】", '${ctx}/tms/basic/tmTransportScope/area?id=' + row.id, '1000px', '650px');
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
		jp.get("${ctx}/tms/basic/tmTransportScope/deleteAll?ids=" + getIdSelections(), function (data) {
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
	jp.openDialog('业务服务范围', "${ctx}/tms/basic/tmTransportScope/form", '600px', '300px', $('#tmTransportScopeTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('业务服务范围', "${ctx}/tms/basic/tmTransportScope/form?id=" + id, '600px', '300px', $('#tmTransportScopeTable'));
}

</script>