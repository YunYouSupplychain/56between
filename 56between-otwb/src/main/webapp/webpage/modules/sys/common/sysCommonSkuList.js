<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysCommonSkuTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
    });
});

function initTable() {
    $('#sysCommonSkuTable').bootstrapTable({
        url: "${ctx}/sys/common/sku/data",
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
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
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
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }, {
            field: 'packCode',
            title: '包装代码'
        }, {
            field: 'packFormat',
            title: '包装规格'
        }, {
            field: 'skuShortName',
            title: '商品简称'
        }, {
            field: 'skuForeignName',
            title: '商品外语名称'
        }, {
            field: 'skuSpec',
            title: '规格'
        }, {
            field: 'filingTime',
            title: '建档时间'
        }, {
            field: 'price',
            title: '单价'
        }, {
            field: 'grossWeight',
            title: '毛重'
        }, {
            field: 'netWeight',
            title: '净重'
        }, {
            field: 'volume',
            title: '体积'
        }, {
            field: 'length',
            title: '长'
        }, {
            field: 'width',
            title: '宽'
        }, {
            field: 'height',
            title: '高'
        }]
    });
    $('#sysCommonSkuTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysCommonSkuTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#sysCommonSkuTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该商品信息记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/sku/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysCommonSkuTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增商品信息', "${ctx}/sys/common/sku/form", '90%', '90%', $('#sysCommonSkuTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialog('编辑商品信息', "${ctx}/sys/common/sku/form?id=" + id, '90%', '90%', $('#sysCommonSkuTable'));
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/sku/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/sku/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

function importSku() {
    $("#uploadModal").modal();
    $("#uploadFileName").val('');
}

function downloadTemplate() {
    window.location = '${ctx}/sys/common/sku/import/template';
}

function uploadFile() {
    if (!$("#uploadFileName").val()) {
        jp.alert("请选择需要上传的文件");
        return;
    }
    jp.loading("正在导入中...");
    var file = $("#uploadFileName").get(0).files[0];
    var fm = new FormData();
    fm.append('file', file);
    $.ajax({
        type: "post",
        url: "${ctx}/sys/common/sku/import",
        data: fm,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        success: function (data) {
            $("#uploadModal").modal('hide');
            $('#sysCommonSkuTable').bootstrapTable('refresh');
            jp.alert(data.msg);
        },
        error: function (data) {
            $("#uploadModal").modal('hide');
            jp.alert(data.msg);
        }
    });
}
</script>