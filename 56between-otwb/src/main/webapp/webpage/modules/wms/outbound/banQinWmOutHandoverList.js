<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#handoverTimeFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#handoverTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	initTable();

	$("#search").click("click", function () {
		$('#banQinWmOutHandoverTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$("#carrierType").val("CARRIER");
		$('#banQinWmOutHandoverTable').bootstrapTable('refresh');
	});
});

function initTable() {
	$('#banQinWmOutHandoverTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/outbound/banQinWmOutHandover/data",
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
			field: 'handoverNo',
			title: '交接单号',
			sortable: true,
			formatter: function (value, row, index) {
				return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'carrierCode',
			title: '承运商编码',
			sortable: true
		}, {
			field: 'carrierName',
			title: '承运商名称',
			sortable: true
		}, {
			field: 'handoverTime',
			title: '交接时间',
			sortable: true
		}, {
			field: 'handoverOp',
			title: '交接人',
			sortable: true
		}, {
			field: 'shipTimeFm',
			title: '发运时间从',
			sortable: true
		}, {
			field: 'shipTimeTo',
			title: '发运时间到',
			sortable: true
		}, {
			field: 'orgName',
			title: '仓库',
			sortable: true
		}]
	});
	$('#banQinWmOutHandoverTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = getIdSelections().length;
		$('#remove').prop('disabled', !length);
		$('#view').prop('disabled', length !== 1);
		$("#printHandoverList").prop('disabled', length !== 1);
	});
}

function getIdSelections() {
	return $.map($("#banQinWmOutHandoverTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function getOrgIdSelections() {
	return $.map($("#banQinWmOutHandoverTable").bootstrapTable('getSelections'), function (row) {
		return row.orgId
	});
}

function view(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('交接单', "${ctx}/wms/outbound/banQinWmOutHandover/form?id=" + id, '90%', '90%', $('#banQinWmOutHandoverTable'));
}

function gen() {
	$('#genModal').modal();
	$("#shipTimeFm").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#shipTimeTo").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$("#genHandoverForm  input").val("");
	$("#genHandoverForm  select").val("");
	$("#genHandoverForm").find("#orgId").eq(0).val(jp.getCurrentOrg().orgId);
}

function genConfirm() {
	jp.loading();
	var validate = bq.headerSubmitCheck("#genHandoverForm");
	if (!validate.isSuccess) {
		jp.warning(validate.msg);
		return;
	}
	jp.post("${ctx}/wms/outbound/banQinWmOutHandover/gen", $("#genHandoverForm").serialize(), function (data) {
		if(data.success){
			jp.success(data.msg);
			$("#genModal").modal('hide');
			$('#banQinWmOutHandoverTable').bootstrapTable('refresh');
		}else {
			jp.bqError(data.msg);
		}
	});
}

function deleteAll() {
	if (!jp.isExistDifOrg(getOrgIdSelections())) {
		jp.warning("不同平台数据不能批量操作");
		return;
	}
	if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
		jp.warning("数据和所选平台不一致，不能操作");
		return;
	}
	jp.confirm('确认要删除该交接单记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/wms/outbound/banQinWmOutHandover/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#banQinWmOutHandoverTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.bqError(data.msg);
			}
		})
	})
}

function printHandoverList() {
	if (!jp.isExistDifOrg(getOrgIdSelections())) {
		jp.warning("不同平台数据不能批量操作");
		return;
	}
	if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
		jp.warning("数据和所选平台不一致，不能操作");
		return;
	}
	bq.openPostWindow("${ctx}/wms/outbound/banQinWmOutHandover/printHandoverList", "id", getIdSelections().join(','), '打印交接清单');
}
</script>