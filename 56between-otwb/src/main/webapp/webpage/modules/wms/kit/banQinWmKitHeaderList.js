<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#fmEtk').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#toEtk').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

	$('#banQinWmKitHeaderTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/kit/banQinWmKitHeader/data",
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
			field: 'kitNo',
			title: '加工单号',
			sortable: true,
			formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'kitType',
			title: '加工类型',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_KIT_TYPE'))}, value, "-");
			}
		}, {
			field: 'status',
			title: '状态',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_KIT_STATUS'))}, value, "-");
			}
		}, {
			field: 'auditStatus',
			title: '审核状态',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_AUDIT_STATUS'))}, value, "-");
			}
		}, {
			field: 'logisticNo',
			title: '物流单号',
			sortable: true
		}, {
			field: 'ownerCode',
			title: '货主编码',
			sortable: true
		}, {
			field: 'fmEtk',
			title: '预计加工时间从',
			sortable: true
		}, {
			field: 'toEtk',
			title: '预计加工时间到',
			sortable: true
		}, {
			field: 'kitLoc',
			title: '计划加工台',
			sortable: true
		}, {
			field: 'auditOp',
			title: '审核人',
			sortable: true
		}, {
			field: 'auditTime',
			title: '审核时间',
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
		}, {
			field: 'remarks',
			title: '备注',
			sortable: true
		}, {
			field: 'orgName',
			title: '仓库名称',
			sortable: true
		}]
	});
	$('#banQinWmKitHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#banQinWmKitHeaderTable').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#duplicate').prop('disabled', length !== 1);
		$('#audit').prop('disabled', !length);
		$('#cancelAudit').prop('disabled', !length);
		$('#close').prop('disabled', !length);
		$('#cancel').prop('disabled', !length);
	});

	$("#search").click("click", function () {// 绑定查询按扭
		$('#banQinWmKitHeaderTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$('#banQinWmKitHeaderTable').bootstrapTable('refresh');
	});
});

function getIdSelections() {
	return $.map($("#banQinWmKitHeaderTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

/**
 * 校验机构一致性
 */
function checkUniqueOrg() {
	var unique = true;
	var orgId = jp.getCurrentOrg().orgId;
	$.each($("#banQinWmKitHeaderTable").bootstrapTable('getSelections'), function (i, row) {
		if(orgId != row.orgId){
			unique = false;
			return false;
		}
	})
	return unique;
}

/**
 * 删除
 */
function deleteAll() {
	jp.confirm('确认要删除该加工单记录吗？', function () {
		if(!checkUniqueOrg()){
			jp.warning("数据和所选平台不一致，不能操作")
			return;
		}
		jp.loading();
		jp.get("${ctx}/wms/kit/banQinWmKitHeader/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#banQinWmKitHeaderTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

/**
 * 新建
 */
function add() {
	jp.openBQDialog('新增加工单', "${ctx}/wms/kit/banQinWmKitHeader/form", '90%', '90%', $('#banQinWmKitHeaderTable'));
}

/**
 * 保存
 */
function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('编辑加工单', "${ctx}/wms/kit/banQinWmKitHeader/form?id=" + id,'90%', '90%', $('#banQinWmKitHeaderTable'));
}

/**
 * 复制
 */
function duplicateHandler() {
	if(!checkUniqueOrg()){
		jp.warning("数据和所选平台不一致，不能操作")
		return;
	}
	jp.loading();
	jp.get("${ctx}/wms/kit/banQinWmKitHeader/duplicate?id=" + getIdSelections(), function (data) {
		if (data.success) {
			$('#banQinWmKitHeaderTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	})
}

/**
 * 审核
 */
function auditHandler() {
	if(!checkUniqueOrg()){
		jp.warning("数据和所选平台不一致，不能操作")
		return;
	}
	jp.loading();
	jp.get("${ctx}/wms/kit/banQinWmKitHeader/audit?ids=" + getIdSelections(), function (data) {
		if (data.success) {
			$('#banQinWmKitHeaderTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	})
}

/**
 * 取消审核
 */
function cancelAuditHandler() {
	if(!checkUniqueOrg()){
		jp.warning("数据和所选平台不一致，不能操作")
		return;
	}
	jp.loading();
	jp.get("${ctx}/wms/kit/banQinWmKitHeader/cancelAudit?ids=" + getIdSelections(), function (data) {
		if (data.success) {
			$('#banQinWmKitHeaderTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	})
}

/**
 * 关闭订单
 */
function closeHandler() {
	if(!checkUniqueOrg()){
		jp.warning("数据和所选平台不一致，不能操作")
		return;
	}
	jp.loading();
	jp.get("${ctx}/wms/kit/banQinWmKitHeader/close?ids=" + getIdSelections(), function (data) {
		if (data.success) {
			$('#banQinWmKitHeaderTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	})
}

/**
 * 取消订单
 */
function cancelHeader() {
	if(!checkUniqueOrg()){
		jp.warning("数据和所选平台不一致，不能操作")
		return;
	}
	jp.loading();
	jp.get("${ctx}/wms/kit/banQinWmKitHeader/cancel?ids=" + getIdSelections(), function (data) {
		if (data.success) {
			$('#banQinWmKitHeaderTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	})
}

</script>