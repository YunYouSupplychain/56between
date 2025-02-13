<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$('#table').bootstrapTable({
		cache: false,// 是否使用缓存
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/sys/role/data",
		//查询参数,每次调用是会带上这个参数，可自定义
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
			title: '角色名称',
			formatter: function (value, row, index) {
				return '<a  href="#" onclick="jp.openDialogView(\'查看角色\', \'${ctx}/sys/role/form?id=' + row.id + '\',\'800px\', \'400px\')">' + value + '</a>'
			}
		}, {
			field: 'enname',
			title: '英文名称'
		}, {
			field: 'office.name',
			title: '归属机构'
		}, {
			field: 'useable',
			title: '状态',
			formatter: function (value, row, index) {
				return value == "0" ? '<font color="red">禁用</font>' : '<font color="green">正常</font>';
			}
		}, {
			field: 'operate',
			title: '操作',
			align: 'center',
			events: {
				'click .view': function (e, value, row, index) {
					jp.openDialogView('查看角色', '${ctx}/sys/role/form?id=' + row.id, '800px', '400px');
				},
				'click .edit': function (e, value, row, index) {
					jp.openDialog('编辑角色', '${ctx}/sys/role/form?id=' + row.id, '800px', '400px');
				},
				'click .del': function (e, value, row, index) {
					del(row.id);
				},
				'click .auth': function (e, value, row, index) {
					jp.openDialog('菜单权限-' + row.name, '${ctx}/sys/role/auth?id=' + row.id, '350px', '700px');
				},
				'click .assign': function (e, value, row, index) {
					$("#left").attr("class", "col-sm-6");
					setTimeout(function () {
						$("#right").fadeIn(500);
					}, 500)
					$("#roleLabel").html(row.name);
					$("#roleId").val(row.id);
					$('#userTable').bootstrapTable("refresh", {query: {id: row.id}})
				}
			},
			formatter: function operateFormatter(value, row, index) {
				return [
					<shiro:hasPermission name="sys:role:view">
						'<a href="#" class="view" title="查看"><i class="fa fa-eye"></i> </a>',
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:role:edit">
						<c:if test="${(role.sysData eq fns:getDictValue('是', 'yes_no', '1') && fns:getUser().admin)||!(role.sysData eq fns:getDictValue('是', 'yes_no', '1'))}">
							'<a href="#" class="edit" title="修改"><i class="fa fa-edit"></i> </a>',
						</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:role:del">
						'<a href="#" class="del" title="删除"><i class="fa fa-trash"></i> </a>',
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:role:assign">
						'<a href="#" class="auth" title="权限设置"><i class="fa fa-cog"></i> </a>',
					</shiro:hasPermission>
					<shiro:hasPermission name="sys:role:assign">
						'<a href="#" class="assign" title="分配用户"><i class="fa fa-users"></i> </a>'
					</shiro:hasPermission>
				].join('');
			}
		}]
	});
	$('#table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
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

function add() {
	jp.openDialog('新建角色', '${ctx}/sys/role/form', '800px', '400px')
}

function edit(id) {
	if (!id) {
		id = getIdSelections();
	}
	jp.openDialog('编辑角色', "${ctx}/sys/role/form?id=" + id, '800px', '400px')
}

function del(ids) {
	if (!ids) {
		ids = getIdSelections();
	}
	jp.confirm('确认要删除选中角色吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/role/delete?ids=" + ids, function (data) {
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