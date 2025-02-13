<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmElectronicFenceTable').bootstrapTable('refresh');
	});

	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$('#tmElectronicFenceTable').bootstrapTable('refresh');
	});
});

function initTable() {
    var $table = $('#tmElectronicFenceTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/basic/tmElectronicFence/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = tmOrg.id;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'fenceName',
            title: '电子围栏名称',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'fenceProvince',
            title: '省份',
            sortable: true
        }, {
            field: 'fenceCity',
            title: '城市',
            sortable: true
        }, {
            field: 'fenceDistrict',
            title: '区',
            sortable: true
        }, {
            field: 'fenceType',
            title: '电子围栏类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_ELECTRONIC_FENCE_TYPE'))}, value, "-");
            }
        }, {
            field: 'fenceCode',
            title: '电子围栏编码',
            sortable: true
        }, {
            field: 'fenceAddress',
            title: '地址',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
	return $.map($("#tmElectronicFenceTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该电子围栏记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmElectronicFence/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmElectronicFenceTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openBQDialog('电子围栏', "${ctx}/tms/basic/tmElectronicFence/form", '90%', '90%', $('#tmElectronicFenceTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openBQDialog('电子围栏', "${ctx}/tms/basic/tmElectronicFence/form?id=" + id, '90%', '90%', $('#tmElectronicFenceTable'));
}

function enable(flag) {
	jp.loading();
	jp.post("${ctx}/tms/basic/tmElectronicFence/enable?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
		if (data.success) {
			$('#tmElectronicFenceTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

</script>