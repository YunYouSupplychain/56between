<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmFittingTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#tmFittingTable').bootstrapTable('refresh');
	});
});

function initTable() {
    var $table = $('#tmFittingTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/tms/fitting/data",
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
            field: 'fittingCode',
            title: '配件编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'fittingName',
            title: '配件名称',
            sortable: true
        }, {
            field: 'fittingModel',
            title: '配件型号',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_FITTING_MODEL'))}, value, "-");
            }
        }, {
            field: 'price',
            title: '单价',
            sortable: true
        }, {
            field: 'delFlag',
            title: '启用停用',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('del_flag'))}, value, "-");
            }
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
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#enable').prop('disabled', !length);
        $('#unable').prop('disabled', !length);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
	return $.map($("#tmFittingTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该配件信息记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/tms/fitting/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmFittingTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('配件信息', "${ctx}/sys/common/tms/fitting/form", '100%', '100%', $('#tmFittingTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('配件信息', "${ctx}/sys/common/tms/fitting/form?id=" + id, '100%', '100%', $('#tmFittingTable'));
}

function enable(flag) {
	jp.loading();
	jp.post("${ctx}/sys/common/tms/fitting/enable?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
		if (data.success) {
			$('#tmFittingTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/tms/fitting/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/tms/fitting/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

</script>