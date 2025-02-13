<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#banQinCdWhBomHeaderTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/kit/banQinCdWhBomHeader/data",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.orgId = jp.getCurrentOrg().orgId;
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		columns: [{
			checkbox: true
		}, {
			field: 'ownerName',
			title: '货主编码',
			sortable: true,
			formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'parentSkuName',
			title: '父件编码',
			sortable: true
		}, {
			field: 'kitType',
			title: '加工类型',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_KIT_TYPE'))}, value, "-");
			}
		}, {
			field: 'createBy.name',
			title: '创建人',
			sortable: true
		}, {
			field: 'createDate',
			title: '创建时间',
			sortable: true
		}, {
			field: 'updateBy.name',
			title: '更新人',
			sortable: true
		}, {
			field: 'updateDate',
			title: '更新时间',
			sortable: true
		}]
	});
	$('#banQinCdWhBomHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#banQinCdWhBomHeaderTable').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
	});

	$("#search").click("click", function () {// 绑定查询按扭
		$('#banQinCdWhBomHeaderTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$('#banQinCdWhBomHeaderTable').bootstrapTable('refresh');
	});
});

function getIdSelections() {
	return $.map($("#banQinCdWhBomHeaderTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该组合件记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/wms/kit/banQinCdWhBomHeader/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#banQinCdWhBomHeaderTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

function add() {
	jp.openBQDialog('新增组合件', "${ctx}/wms/kit/banQinCdWhBomHeader/form", '90%', '90%', $('#banQinCdWhBomHeaderTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id == undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('编辑组合件', "${ctx}/wms/kit/banQinCdWhBomHeader/form?id=" + id, '90%', '90%', $('#banQinCdWhBomHeaderTable'));
}

</script>