<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
$(document).ready(function () {
	//zTree初始化
	$.getJSON("${ctx}/sys/office/bootstrap/treeData", function (data) {
		$('#jstree').treeview({
			data: data,
			levels: 5,
			onNodeSelected: function (event, treeNode) {
				var id = treeNode.id === '0' ? '' : treeNode.id;
				$("#officeId").val(id);
				$("#officeName").val(treeNode.text);
				$('#table').bootstrapTable('refresh');
			},
		});
	});

	//表格初始化
	$('#table').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/user/data",
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
			field: 'loginName',
			title: '登录名',
			sortable: true
		}, {
			field: 'name',
			title: '姓名',
			sortable: true,
		}, {
			field: 'phone',
			title: '电话',
			sortable: true
		}, {
			field: 'company.name',
			title: '归属公司'
		}, {
			field: 'office.name',
			title: '归属机构'
		}, {
			field: 'loginFlag',
			title: '是否允许登录',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, value, "-");
			}
		}]
	}).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $('#table').bootstrapTable('getSelections').length;
		$('#remove').prop('disabled', !length);
		$('#edit').prop('disabled', length !== 1);
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

function deleteAll(ids) {
	if (!ids) {
		ids = getIdSelections();
	}
	jp.confirm('确认要删除选中用户吗？', function () {
		jp.loading();
		$.get("${ctx}/sys/user/deleteAll?ids=" + ids, function (data) {
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
	jp.openDialog('新建用户', '${ctx}/sys/user/form','800px', '450px');
}

function edit(id) {
	if (!id) {
		id = getIdSelections();
	}
	jp.openDialog('编辑用户', "${ctx}/sys/user/form?id=" + id, '800px', '450px');
}
</script>