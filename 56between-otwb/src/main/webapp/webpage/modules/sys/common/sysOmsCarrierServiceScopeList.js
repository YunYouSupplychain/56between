<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
    initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#sysOmsCarrierServiceScopeTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#sysOmsCarrierServiceScopeTable').bootstrapTable('refresh');
	});

});

function initTable() {
    var $table = $('#sysOmsCarrierServiceScopeTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/oms/carrierServiceScope/data",
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
            field: 'carrierCode',
            title: '承运商编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'carrierName',
            title: '承运商名称',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: true
        }, {
            field: 'groupName',
            title: '业务服务范围',
            sortable: true
        }, {
            field: 'maxWeight',
            title: '最大重量',
            sortable: true
        }, {
            field: 'maxVolume',
            title: '最大体积',
            sortable: true
        }, {
            field: 'maxCost',
            title: '最大费用',
            sortable: true
        }, {
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
	return $.map($("#sysOmsCarrierServiceScopeTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该承运商服务范围记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/sys/common/oms/carrierServiceScope/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#sysOmsCarrierServiceScopeTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('新增承运商服务范围', "${ctx}/sys/common/oms/carrierServiceScope/form", '800px', '420px', $('#sysOmsCarrierServiceScopeTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('编辑承运商服务范围', "${ctx}/sys/common/oms/carrierServiceScope/form?id=" + id, '800px', '420px', $('#sysOmsCarrierServiceScopeTable'));
}

function view(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialogView('查看承运商服务范围', "${ctx}/sys/common/oms/carrierServiceScope/form?id=" + id, '800px', '420px', $('#sysOmsCarrierServiceScopeTable'));
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/oms/carrierServiceScope/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/oms/carrierServiceScope/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

</script>