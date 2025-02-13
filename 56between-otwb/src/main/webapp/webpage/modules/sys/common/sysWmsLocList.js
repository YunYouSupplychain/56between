<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysWmsLocTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#sysWmsLocTable').bootstrapTable('refresh');
    });
    $("#btnImport").click(function () {
        bq.importExcel('${ctx}/sys/common/wms/loc/import');
    });
});

function initTable() {
    $('#sysWmsLocTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/wms/loc/data",
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
            field: 'locCode',
            title: '库位编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'zoneName',
            title: '所属库区',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LOC_STATUS'))}, value, "-");
            }
        }, {
            field: 'isEnable',
            title: '是否启用',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'category',
            title: '库位种类',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'locUseType',
            title: '使用类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_LOC_USE_TYPE'))}, value, "-");
            }
        }, {
            field: 'paSeq',
            title: '上架顺序',
            sortable: true
        }, {
            field: 'pkSeq',
            title: '拣货顺序',
            sortable: true
        }, {
            field: 'abc',
            title: '库位ABC',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ABC'))}, value, "-");
            }
        }, {
            field: 'dataSetName',
            title: '数据套',
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
            field: 'lane',
            title: '通道',
            sortable: true
        }, {
            field: 'seq',
            title: '序号',
            sortable: true
        }, {
            field: 'floor',
            title: '层',
            sortable: true
        }, {
            field: 'locGroup',
            title: '库位组',
            sortable: true
        }, {
            field: 'isMixSku',
            title: '是否允许混商品',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'maxMixSku',
            title: '最大混商品数量',
            sortable: true
        }, {
            field: 'isMixLot',
            title: '是否允许混批次',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'maxMixLot',
            title: '最大混批次数量',
            sortable: true
        }, {
            field: 'isLoseId',
            title: '是否忽略跟踪号',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'maxWeight',
            title: '最大重量',
            sortable: true
        }, {
            field: 'maxCubic',
            title: '最大体积',
            sortable: true
        }, {
            field: 'maxPl',
            title: '最大托盘数',
            sortable: true
        }, {
            field: 'x',
            title: 'X坐标',
            sortable: true
        }, {
            field: 'y',
            title: 'Y坐标',
            sortable: true
        }, {
            field: 'z',
            title: 'Z坐标',
            sortable: true
        }]
    });
    $('#sysWmsLocTable').on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysWmsLocTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#createLoc').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#sysWmsLocTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该库位记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/wms/loc/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysWmsLocTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增库位', "${ctx}/sys/common/wms/loc/form", '90%', '90%', $('#sysWmsLocTable'));
}

function edit(id) {
    if(id === undefined) {
        id = getIdSelections();
    }
    jp.openDialog('编辑库位', "${ctx}/sys/common/wms/loc/form?id=" + id,'90%', '90%', $('#sysWmsLocTable'));
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/wms/loc/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/wms/loc/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

function exportExcel() {
    var validate = bq.headerSubmitCheck("#searchForm");
    if (!validate.isSuccess) {
        jp.alert(validate.msg);
        return;
    }
    bq.exportExcelNew("${ctx}/sys/common/wms/loc/export", "库位", $("#searchForm").serializeJSON())
}

</script>