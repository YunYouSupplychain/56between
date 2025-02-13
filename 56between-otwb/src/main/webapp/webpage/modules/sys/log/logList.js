<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
var $table;
$(document).ready(function() {
	$('#beginDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
	$('#endDate').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

	$table = $('#table').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/log/data",
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
			field: 'title',
			title: '操作菜单',
			sortable: true
		}, {
			field: 'createBy.name',
			title: '操作用户',
			sortable: true,
		}, {
			field: 'createBy.company.name',
			title: '所在公司',
			sortable: true
		}, {
			field: 'createBy.office.name',
			title: '归属机构',
			sortable: true
		}, {
			field: 'requestUri',
			title: 'URI',
			sortable: true
		}, {
			field: 'method',
			title: '提交方式',
			sortable: true
		}, {
			field: 'remoteAddr',
			title: '操作者IP',
			sortable: true
		}, {
			field: 'createDate',
			title: '操作时间',
			sortable: true
		}, {
			field: 'exception',
			title: '异常信息',
			sortable: true,
			formatter: function (value, row, index) {
				if (value) {
					return '<a class="detail-icon" href="javascript:detailException(' + index + ')">[查看异常]</a>' +
						'<a class="detail-icon" href="javascript:closeDetail(' + index + ')"></a>';
				} else {
					return '-';
				}
			}
		}]
	});
	$table.on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $table.bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
	});

	$("#search").click("click", function () {// 绑定查询按扭
		$table.bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$table.bootstrapTable('refresh');
	});
});

function getIdSelections() {
	return $.map($table.bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll(ids) {
	if (!ids) {
		ids = getIdSelections();
	}
	jp.confirm('确认要删除该日志吗？', function () {
		jp.loading();
		$.get("${ctx}/sys/log/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				jp.success(data.msg);
				$table.bootstrapTable('refresh');
			} else {
				jp.error(data.msg);
			}
		})
	})
}

function empty() {
	jp.confirm("确认要清空日志吗？", function () {
		jp.loading();
		$.get("${ctx}/sys/log/empty", function (data) {
			if (data.success) {
				jp.success(data.msg);
				$table.bootstrapTable('refresh');
			} else {
				jp.error(data.msg);
			}
		})
	})
}

function detailException(index) {
	$table.bootstrapTable('expandRow', index);
}

function closeDetail(index) {
	$table.bootstrapTable('collapseRow', index);
}

</script>