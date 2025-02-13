<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysWmsZoneTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#sysWmsZoneTable').bootstrapTable('refresh');
    });
    $("#btnImport").click(function () {
        bq.importExcel('${ctx}/sys/common/wms/zone/import');
    });
});

function initTable() {
    $('#sysWmsZoneTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/wms/zone/data",
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
            field: 'zoneCode',
            title: '库区编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'zoneName',
            title: '库区名称',
            sortable: true
        }, {
            field: 'type',
            title: '库区类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ZONE_TYPE'))}, value, "-");
            }
        }, {
            field: 'areaCode',
            title: '所属区域编码',
            sortable: true
        }, {
            field: 'areaName',
            title: '所属区域名称',
            sortable: true
        }, {
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }]
    });
    $('#sysWmsZoneTable').on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysWmsZoneTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#sysWmsZoneTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该库区记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/wms/zone/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysWmsZoneTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增库区', "${ctx}/sys/common/wms/zone/form", '800px', '500px', $('#sysWmsZoneTable'));
}

function edit(id) {
    if(id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="sys:common:wms:zone:edit">
        jp.openDialog('编辑库区', "${ctx}/sys/common/wms/zone/form?id=" + id,'800px', '500px', $('#sysWmsZoneTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="sys:common:wms:zone:edit">
        jp.openDialogView('查看库区', "${ctx}/sys/common/wms/zone/form?id=" + id,'800px', '500px', $('#sysWmsZoneTable'));
    </shiro:lacksPermission>
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/wms/zone/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/wms/zone/syncAll", function (data) {
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
    bq.exportExcelNew("${ctx}/sys/common/wms/zone/export", "库区", $("#searchForm").serializeJSON())
}

</script>