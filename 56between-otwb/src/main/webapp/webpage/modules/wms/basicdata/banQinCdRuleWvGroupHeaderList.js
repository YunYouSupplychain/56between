<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#banQinCdRuleWvGroupHeaderTable').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/wms/basicdata/banQinCdRuleWvGroupHeader/data",
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
		},{
			field: 'groupCode',
			title: '规则组编码',
			sortable: true,
			formatter: function (value, row, index) {
				return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
			}
		},{
			field: 'groupName',
			title: '规则组名称',
			sortable: true
		},{
			field: 'orderDateFm',
			title: '订单时间从',
			sortable: true
		},{
			field: 'orderDateTo',
			title: '订单时间到',
			sortable: true
		},{
			field: 'ownerCode',
			title: '货主编码',
			sortable: true
		},{
			field: 'ownerName',
			title: '货主名称',
			sortable: true
		},{
			field: 'skuCode',
			title: '商品编码',
			sortable: true
		},{
			field: 'skuName',
			title: '商品名称',
			sortable: true
		}]
	});
	$('#banQinCdRuleWvGroupHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#banQinCdRuleWvGroupHeaderTable').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
		$('#execute').prop('disabled', length !== 1);
	});

	$("#search").click("click", function () {// 绑定查询按扭
		$('#banQinCdRuleWvGroupHeaderTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$('#banQinCdRuleWvGroupHeaderTable').bootstrapTable('refresh');
	});
});

function getIdSelections() {
    return $.map($("#banQinCdRuleWvGroupHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该波次规则组记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdRuleWvGroupHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdRuleWvGroupHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
	jp.openBQDialog('新增波次规则组', "${ctx}/wms/basicdata/banQinCdRuleWvGroupHeader/form", '80%', '80%', $('#banQinCdRuleWvGroupHeaderTable'));
}

function edit(id) {
	if(id == undefined){
		id = getIdSelections();
	}
	jp.openBQDialog('查看波次规则组', "${ctx}/wms/basicdata/banQinCdRuleWvGroupHeader/form?id=" + id,'80%', '80%', $('#banQinCdRuleWvGroupHeaderTable'));
}

function execute() {
	jp.loading();
	jp.get("${ctx}/wms/basicdata/banQinCdRuleWvGroupHeader/createWave?id=" + getIdSelections(), function (data) {
		if (data.success) {
			$('#banQinCdRuleWvGroupHeaderTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.bqError(data.msg);
		}
	})
}

</script>