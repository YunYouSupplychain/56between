<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#orgId").val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmItemTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm  input").val("");
		$("#searchForm  select").val("");
		$("#searchForm  .select-item").html("");
		$("#orgId").val(tmOrg.id);
	});
});

function initTable() {
    var $table = $('#tmItemTable');
    $table.bootstrapTable({
        url: "${ctx}/tms/basic/tmItem/data",
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
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
            field: 'skuCode',
            title: '商品编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '客户编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '客户名称',
            sortable: true
        }, {
            field: 'skuType',
            title: '商品类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_SKU_TYPE'))}, value, "-");
            }
        }, {
            field: 'skuModel',
            title: '商品型号',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_SKU_MODEL'))}, value, "-");
            }
        }, {
            field: 'grossweight',
            title: '毛重',
            sortable: true
        }, {
            field: 'netweight',
            title: '净重',
            sortable: true
        }, {
            field: 'cubic',
            title: '体积',
            sortable: true
        }, {
            field: 'length',
            title: '长',
            sortable: true
        }, {
            field: 'width',
            title: '宽',
            sortable: true
        }, {
            field: 'height',
            title: '高',
            sortable: true
        }, {
            field: 'price',
            title: '单价',
            sortable: true
        }, {
            field: 'currency',
            title: '币种',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_CURRENCY'))}, value, "-");
            }
        }, {
            field: 'unit',
            title: '单位',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_UNIT'))}, value, "-");
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
    });
}

function getIdSelections() {
	return $.map($("#tmItemTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该商品信息记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmItem/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmItemTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('商品信息', "${ctx}/tms/basic/tmItem/form", '80%', '80%', $('#tmItemTable'));
}

function edit(id) {
	if (id === undefined) {
		id = getIdSelections();
	}
	jp.openDialog('商品信息', "${ctx}/tms/basic/tmItem/form?id=" + id, '80%', '80%', $('#tmItemTable'));
}

function enable(flag) {
	jp.loading();
	jp.post("${ctx}/tms/basic/tmItem/enable?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
		if (data.success) {
			$('#tmItemTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

</script>