<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#createDateFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#createDateTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmWvHeaderTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmWvHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmWvHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmWvHeader/data",
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
            field: 'waveNo',
            title: '波次单号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_WV_STATUS'))}, value, "-");
            }
        }, {
            field: 'def1',
            title: '打印次数',
            sortable: true
        }, {
            field: 'def2',
            title: '是否获取过面单',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'def3',
            title: '是否打印过面单',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'def4',
            title: '自定义4',
            sortable: true
        }, {
            field: 'def5',
            title: '自定义5',
            sortable: true
        }, {
            field: 'remarks',
            title: '波次生成规则',
            sortable: true
        }, {
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        }, {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }, {
            field: 'updateBy.name',
            title: '修改人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $('#banQinWmWvHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' + 'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmWvHeaderTable').bootstrapTable('getSelections').length;
        var cStyle = length ? "auto" : "none";
        var fColor = length ? "#1E1E1E" : "#8A8A8A";
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#alloc').prop('disabled', !length);
        $('#dispathPk').prop('disabled', !length);
        $('#picking').prop('disabled', !length);
        $('#shipment').prop('disabled', !length);
        $('#cancelAlloc').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelPicking').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelShipment').css({"pointer-events": cStyle, "color": fColor});
        $('#printPickingOrder').css({"pointer-events": cStyle, "color": fColor});
        $('#printWaveCombinePicking').css({"pointer-events": cStyle, "color": fColor});
        $('#printWaveCombinePickingLandscape').css({"pointer-events": cStyle, "color": fColor});
        $('#printPickingList').css({"pointer-events": cStyle, "color": fColor});
        $('#getWaybill').css({"pointer-events": cStyle, "color": fColor});
        $('#printWaybill').css({"pointer-events": cStyle, "color": fColor});
        $('#getAndPrintWaybill').css({"pointer-events": cStyle, "color": fColor});
    });
}

function getIdSelections() {
    return $.map($("#banQinWmWvHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getWaveNosSelections() {
    return $.map($("#banQinWmWvHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.waveNo
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmWvHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

/**
 * 查看
 * @param id
 */
function view(id) {
    jp.openBQDialog('查看波次计划', "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + id, '90%', '90%', $('#banQinWmWvHeaderTable'));
}

/**
 * 修改
 */
function edit() {
    jp.openBQDialog('编辑波次计划', "${ctx}/wms/outbound/banQinWmWvHeader/form?id=" + getIdSelections(), '90%', '90%', $('#banQinWmWvHeaderTable'));
}

/**
 * 删除
 */
function deleteAll() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.confirm('确认要删除该波次记录吗？', function () {
        if (!jp.isExistDifOrg(getOrgIdSelections())) {
            jp.warning("不同平台数据不能批量操作");
            return;
        } 
        commonMethod("deleteAll");
    })
}

/**
 * 分配
 */
function alloc() {
    commonMethod('alloc');
}

/**
 * 分派拣货
 */
function dispathPk() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    $('#dispatchPKModal').modal();
}

/**
 * 分派拣货确认
 */
function dispatchPKConfirm() {
    $('#dispatchPKModal').modal('hide');
    jp.loading();
    var $form = $('#dispathPkForm').bq_serialize();
    $form.waveNos = getWaveNosSelections().join(',');
    $form.orgId = jp.getCurrentOrg().orgId;
    jp.post("${ctx}/wms/outbound/banQinWmWvHeader/dispathPk", $form, function (data) {
        if (data.success) {
            $('#banQinWmWvHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 拣货确认
 */
function picking() {
    commonMethod('picking');
}

/**
 * 发货确认
 */
function shipment() {
    commonMethod('shipment');
}

/**
 * 取消分配
 */
function cancelAlloc() {
    commonMethod('cancelAlloc');
}

/**
 * 取消拣货
 */
function cancelPicking() {
    commonMethod('cancelPicking');
}

/**
 * 取消发货
 */
function cancelShipment() {
    commonMethod('cancelShipment');
}

/**
 * 公共调用方法
 * @param method 方法名
 */
function commonMethod(method) {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmWvHeader/" + method + "?waveNo=" + getWaveNosSelections() + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
        if (data.success) {
            $('#banQinWmWvHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 是否分区库
 * @param flag
 */
function isByZoneChange(flag) {
    $('#isByZone').val(flag ? 'Y' : 'N');
}

/**
 * 是否托盘任务
 * @param flag
 */
function isPlTaskChange(flag) {
    $('#isPlTask').val(flag ? 'Y' : 'N');
    $('#plLimit').prop('readonly', !flag);
    $('#plFloat').prop('readonly', !flag);
}

/**
 * 是否箱拣任务
 * @param flag
 */
function isCsTaskChange(flag) {
    $('#isCsTask').val(flag ? 'Y' : 'N');
    $('#csLimit').prop('readonly', !flag);
    $('#csFloat').prop('readonly', !flag);
}

/**
 * 是否件拣任务
 * @param flag
 */
function isEaTaskChange(flag) {
    $('#isEaTask').val(flag ? 'Y' : 'N');
    $('#eaLimit').prop('readonly', !flag);
    $('#eaFloat').prop('readonly', !flag);
}

/**
 * 打印分拣单
 */
function printWaveSorting() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmWvHeader/printWaveSorting", 'ids', rowIds.join(','), '打印分拣单');
}

/**
 * 打印合拣单
 */
function printWaveCombinePicking() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmWvHeader/printWaveCombinePicking", 'ids', rowIds.join(','), '打印合拣单');
}

/**
 * 打印合拣单（横版）
 */
function printWaveCombinePickingLandscape() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmWvHeader/printWaveCombinePickingLandscape", 'ids', rowIds.join(','), '打印合拣单');
}

/**
 * 打印分拣清单（装箱清单）
 */
function printPickingList() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/outbound/banQinWmWvHeader/printPickingList", 'ids', rowIds.join(','), '打印分拣清单');
}

/**
 * 获取面单
 */
function getWaybill() {
    commonMethod('getWaybill');
}

/**
 * 打印面单
 */
function printWaybill() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmWvHeader/printWaybill?waveNo=" + getWaveNosSelections() + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
        if (data.success) {
            $('#banQinWmWvHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
            // 后台打印
            bq.printWayBill(data.body.imageList);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/**
 * 获取面单并打印
 */
function getAndPrintWaybill() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    jp.loading();
    jp.get("${ctx}/wms/outbound/banQinWmWvHeader/getAndPrintWaybill?waveNo=" + getWaveNosSelections() + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
        if (data.success) {
            $('#banQinWmWvHeaderTable').bootstrapTable('refresh');
            jp.success(data.msg);
            // 后台打印
            bq.printWayBill(data.body.imageList);
        } else {
            jp.bqError(data.msg);
        }
    });
}
</script>