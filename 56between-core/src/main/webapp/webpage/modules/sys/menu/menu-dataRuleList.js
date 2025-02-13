<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#dataRuleTable').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/dataRule/data",
		queryParams: function (params) {
			var searchParam = {};
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			searchParam.menuId = $("#menuId").val();
			return searchParam;
		},
		columns: [{
			checkbox: true
		}, {
			field: 'name',
			title: '数据规则名称',
			sortable: true
			, formatter: function (value, row, index) {
				return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
			}
		}, {
			field: 'className',
			title: '实体类',
			sortable: true
		}, {
			field: 'field',
			title: '字段',
			sortable: true
		}, {
			field: 'express',
			title: '条件',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('data_rule_express'))}, value, "-");
			}
		}, {
			field: 'value',
			title: '规则值',
			sortable: true
		}, {
			field: 'sqlSegment',
			title: '自定义sql',
			sortable: true
		}, {
			field: 'remarks',
			title: '备注信息',
			sortable: true
		}]
	});
	$('#dataRuleTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#dataRuleTable').bootstrapTable('getSelections').length;
		$('#dataRuleDelButton').prop('disabled', !length);
		$('#dataRuleEditButton').prop('disabled', length !== 1);
	});

	$("#dataRuleAddButton").click(function () {
		jp.openDialog('添加数据规则', '${ctx}/sys/dataRule/form?menuId=' + $("#menuId").val(), '800px', '550px', $("#dataRuleTable"));
	});
	$("#dataRuleEditButton").click(function () {
		jp.openDialog('修改数据规则', '${ctx}/sys/dataRule/form?id=' + getIdSelections(), '800px', '550px', $("#dataRuleTable"));
	});
	$("#dataRuleDelButton").click(function () {
		jp.confirm('确认要删除该数据权限记录吗？', function () {
			jp.loading();
			jp.get("${ctx}/sys/dataRule/deleteAll?ids=" + getIdSelections(), function (data) {
				if (data.success) {
					$('#dataRuleTable').bootstrapTable('refresh');
					jp.success(data.msg);
				} else {
					jp.error(data.msg);
				}
			})
		})
	});
});

function getIdSelections() {
	return $.map($("#dataRuleTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function view(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialogView('数据规则', '${ctx}/sys/dataRule/form?id=' + id, '800px', '580px', $("#dataRuleTable"));
}
</script>