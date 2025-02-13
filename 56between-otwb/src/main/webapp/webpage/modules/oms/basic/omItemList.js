<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#omItemTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
    });
});

function initTable() {
    $('#omItemTable').bootstrapTable({
        url: "${ctx}/oms/basic/omItem/data",
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
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
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'skuName',
            title: '商品名称',
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
            field: 'shortName',
            title: '商品简称',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户商品编码',
            sortable: true
        }, {
            field: 'skuClassName',
            title: '品类',
            sortable: true
        }, {
            field: 'skuTempLayer',
            title: '温层',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('temperature_layer'))}, value, "-");
            }
        }, {
            field: 'skuType',
            title: '课别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('sku_type'))}, value, "-");
            }
        }, {
            field: 'packName',
            title: '包装规格',
            sortable: true
        }, {
            field: 'spec',
            title: '规格',
            sortable: true
        }, {
            field: 'unicode',
            title: '统一码',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#omItemTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#omItemTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该商品信息记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/basic/omItem/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#omItemTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增商品信息', "${ctx}/oms/basic/omItem/form", '80%', '80%', $('#omItemTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name = "basic:omItem:edit" >
        jp.openDialog('编辑商品信息', "${ctx}/oms/basic/omItem/form?id=" + id, '80%', '80%', $('#omItemTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name = "basic:omItem:edit" >
        view(id);
    </shiro:lacksPermission>
}

function view(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialogView('查看商品信息', "${ctx}/oms/basic/omItem/form?id=" + id, '80%', '80%', $('#omItemTable'));
}

</script>
