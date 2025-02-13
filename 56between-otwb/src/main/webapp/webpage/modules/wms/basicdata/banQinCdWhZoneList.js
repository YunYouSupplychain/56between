<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#banQinCdWhZoneTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinCdWhZone/data",
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
        }]
    });
    $('#banQinCdWhZoneTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinCdWhZoneTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinCdWhZoneTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinCdWhZoneTable').bootstrapTable('refresh');
    });
    $("#btnImport").click(function () {
        bq.importExcel('${ctx}/wms/basicdata/banQinCdWhZone/import');
    });
});

function getIdSelections() {
    return $.map($("#banQinCdWhZoneTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getZoneCodeSelections() {
    return $.map($("#banQinCdWhZoneTable").bootstrapTable('getSelections'), function (row) {
        return row.zoneCode
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinCdWhZoneTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}


function deleteAll() {
    jp.confirm('确认要删除该库区记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdWhZone/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdWhZoneTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增库区', "${ctx}/wms/basicdata/banQinCdWhZone/form", '800px', '420px', $('#banQinCdWhZoneTable'));
}

function edit(id) {
    if(id == undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basicdata:banQinCdWhZone:edit">
        jp.openDialog('编辑库区', "${ctx}/wms/basicdata/banQinCdWhZone/form?id=" + id,'800px', '420px', $('#banQinCdWhZoneTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basicdata:banQinCdWhZone:edit">
        jp.openDialogView('查看库区', "${ctx}/wms/basicdata/banQinCdWhZone/form?id=" + id,'800px', '420px', $('#banQinCdWhZoneTable'));
    </shiro:lacksPermission>
}

function printBarcode() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var zoneCodes = getZoneCodeSelections();
    if (zoneCodes.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/basicdata/banQinCdWhZone/printBarcode", 'zoneCodes', zoneCodes.join(','), '打印库区标签');
}

function printQrcode() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var zoneCodes = getZoneCodeSelections();
    if (zoneCodes.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/basicdata/banQinCdWhZone/printQrcode", 'zoneCodes', zoneCodes.join(','), '打印库区标签(二维码)');
}

function exportExcel() {
    var validate = bq.headerSubmitCheck("#searchForm");
    if (!validate.isSuccess) {
        jp.alert(validate.msg);
        return;
    }
    bq.exportExcelNew("${ctx}/wms/basicdata/banQinCdWhZone/export", "库区", $("#searchForm").serializeJSON())
}


</script>