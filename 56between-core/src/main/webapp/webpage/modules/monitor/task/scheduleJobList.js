<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#table').bootstrapTable({
		showRefresh: true,// 显示刷新按钮
		showColumns: true,// 显示内容列下拉框
		showExport: true,// 显示到处按钮
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/monitor/scheduleJob/data",
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
			field: 'name',
			title: '任务名',
			sortable: true
		}, {
			field: 'status',
			title: '启用状态',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-");
			}
		}, {
			field: 'operate',
			title: '操作',
			align: 'center',
			events: {
				'click .resume': function (e, value, row, index) {
					resume(row.id);
				},
				'click .stop': function (e, value, row, index) {
					stop(row.id);
				}
			},
			formatter: function operateFormatter() {
				return [
					<shiro:hasPermission name="monitor:scheduleJob:resume">
						'<a href="#" class="resume" title="启动">【启动】</a>',
					</shiro:hasPermission>
					<shiro:hasPermission name="monitor:scheduleJob:stop">
						'<a href="#" class="stop" title="暂停">【暂停】 </a>'
					</shiro:hasPermission>
				].join('');
			}
		}, {
			field: 'group',
			title: '任务组',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('schedule_task_group'))}, value, "-");
			}
		}, {
			field: 'cronExpression',
			title: '定时规则',
			sortable: true
		}, {
			field: 'className',
			title: '任务类',
			sortable: true
		}, {
			field: 'description',
			title: '描述',
			sortable: true
		}]
	});
	$('#table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#table').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit,#stop,#resume,#startNow').prop('disabled', length !== 1);
	});
	$("#search").click("click", function () {// 绑定查询按扭
		$('#table').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$('#table').bootstrapTable('refresh');
	});
});

function getIdSelections() {
	return $.map($("#table").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该定时任务记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/monitor/scheduleJob/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#table').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

function add() {
	jp.openDialog('新建定时任务', '${ctx}/monitor/scheduleJob/form','1000px', '600px');
}

function edit() {
	jp.openDialog('修改定时任务',"${ctx}/monitor/scheduleJob/form?id=" + getIdSelections(), '1000px', '600px');
}

//暂停
function stop(id) {
	if (!id) {
		id = getIdSelections();
	}
	jp.confirm('确定要暂停任务？', function () {
		jp.loading();
		jp.get("${ctx}/monitor/scheduleJob/stop?id=" + id, function (data) {
			if (data.success) {
				$('#table').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

//恢复
function resume(id) {
	if (!id) {
		id = getIdSelections();
	}
	jp.confirm('确定要恢复任务？', function () {
		jp.loading();
		jp.get("${ctx}/monitor/scheduleJob/resume?id=" + id, function (data) {
			if (data.success) {
				$('#table').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}

//立即运行一次
function startNow() {
	jp.confirm('确定要立即运行一次该任务？', function () {
		jp.loading();
		jp.get("${ctx}/monitor/scheduleJob/startNow?id=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#table').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		})
	})
}
</script>