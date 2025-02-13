<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmBusinessRouteTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#tmBusinessRouteTable').bootstrapTable('refresh');
	});

});

function initTable() {
    var $table = $('#tmBusinessRouteTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/tms/businessRoute/data",
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
            field: 'code',
            title: '编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'name',
            title: '路线',
            sortable: true
        }
        , {
            field: 'dataSetName',
            title: '数据套',
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
	return $.map($("#tmBusinessRouteTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该业务路线信息记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/tms/businessRoute/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmBusinessRouteTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('业务路线信息', "${ctx}/sys/common/tms/businessRoute/form", '800px', '500px', $('#tmBusinessRouteTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('业务路线信息', "${ctx}/sys/common/tms/businessRoute/form?id=" + id, '800px', '500px', $('#tmBusinessRouteTable'));
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/tms/businessRoute/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/tms/businessRoute/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

</script>