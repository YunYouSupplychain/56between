<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#orgId").val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmTransportObjScopeTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$("#orgId").val(tmOrg.id);
		$('#tmTransportObjScopeTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#tmTransportObjScopeTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/basic/tmTransportObjScope/data",
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
			field: 'transportObjName',
			title: '业务对象',
			sortable: true
			, formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'transportScopeName',
			title: '业务服务范围',
			sortable: true
		}, {
			field: 'transportScopeType',
			title: '服务范围类型',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_SCOPE_TYPE'))}, value, "-");
			}
		}, {
			field: 'maxLoadWeight',
			title: '最大装载重量',
			sortable: true
		}, {
			field: 'maxLoadCubic',
			title: '最大装载体积',
			sortable: true
		}, {
			field: 'maxAmount',
			title: '最大金额',
			sortable: true
		}, {
			field: 'remarks',
			title: '备注信息',
			sortable: true
		}]
	});
	$('#tmTransportObjScopeTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $("#tmTransportObjScopeTable").bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
	});
}

function getIdSelections() {
	return $.map($("#tmTransportObjScopeTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该业务对象服务范围记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmTransportObjScope/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmTransportObjScopeTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('业务对象服务范围', "${ctx}/tms/basic/tmTransportObjScope/form", '800px', '420px', $('#tmTransportObjScopeTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('业务对象服务范围', "${ctx}/tms/basic/tmTransportObjScope/form?id=" + id, '800px', '420px', $('#tmTransportObjScopeTable'));
}

function genCarrierRoute() {
	jp.loading();
	jp.get("${ctx}/tms/basic/tmTransportObjScope/genCarrierRoute?orgId=" + tmOrg.id, function (data) {
		if (data.success) {
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

</script>