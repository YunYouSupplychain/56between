<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#beginOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#beginOrderTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 00:00:00");
    $("#endOrderTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 23:59:59");
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmOrderCheckTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmOrderCheckTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#banQinWmOrderCheckTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmOrderCheck/data",
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
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'waveNo',
            title: '波次单号',
            sortable: true
        }, {
            field: 'customerOrderNo',
            title: '客户订单号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
            }
        }, {
            field: 'consigneeName',
            title: '客户名称',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主',
            sortable: true
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'qtySoEa',
            title: '订货数',
            sortable: true
        }, {
            field: 'qtyAllocEa',
            title: '分配数',
            sortable: true
        }, {
            field: 'qtyPkEa',
            title: '拣货数',
            sortable: true
        }, {
            field: 'qtyShipEa',
            title: '发货数',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }, {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        }, {
            field: 'cdType',
            title: '越库类型',
            sortable: true
        }, {
            field: 'oldLineNo',
            title: '原行号',
            sortable: true
        }]
    });
    $('#banQinWmOrderCheckTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' + 'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmOrderCheckTable').bootstrapTable('getSelections').length;
        var cStyle = length ? "auto" : "none";
        var fColor = length ? "#1E1E1E" : "#8A8A8A";
        $('#alloc').prop('disabled', !length);
        $('#manuaAlloc').prop('disabled', length !== 1);
        $('#picking').prop('disabled', !length);
        $('#cancelAlloc').css({"pointer-events": cStyle, "color": fColor});
        $('#cancelPicking').css({"pointer-events": cStyle, "color": fColor});
    });
}

function getIdSelections() {
    return $.map($("#banQinWmOrderCheckTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmOrderCheckTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

function getDetailSelections() {
    return $.map($('#banQinWmOrderCheckTable').bootstrapTable('getSelections'), function (row) {
        return "('" + row.soNo + "','" + row.lineNo + "')"
    });
}

/**
 * 分配
 */
function alloc() {
    commonMethod('alloc');
}

/**
 * 平均分配
 */
function allocAvg() {
    commonMethod('allocAvg');
}

/**
 * 手工分配
 */
function manuaAlloc() {
    var rows = $('#banQinWmOrderCheckTable').bootstrapTable('getSelections');
    if (rows.length !== 1) {
        jp.bqError("请选择一条数据!");
        return;
    }
    if (rows[0].status === '90') {
        jp.bqError("订单行[" + rows[0].soNo + "][" + rows[0].lineNo + "已取消，不能操作");
        return;
    }
    if (rows[0].status === '20' || rows[0].status === '40' || rows[0].status === '60' || rows[0].status === '80') {
        jp.bqError("非创建，非部分预配，非部分分配，非部分拣货，非部分发货状态，不能操作");
        return;
    }
    $(':input', '#allocDetailForm').val('');
    $('#manuaAllocInfoModal').modal();

    $('#allocDetail_waveNo').val(rows[0].waveNo);
    $('#allocDetail_soNo').val(rows[0].soNo);
    $('#allocDetail_lineNo').val(rows[0].lineNo);
    $('#allocDetail_ownerCode').val(rows[0].ownerCode);
    $('#allocDetail_ownerName').val(rows[0].ownerName);
    $('#allocDetail_status').val('40');
    $('#allocDetail_toLoc').val('SORTATION');
    $('#allocDetail_skuCodeParam').val(rows[0].skuCode);
    $('#allocDetail_packStatus').val('00');
    $('#allocDetail_orgId').val(rows[0].orgId);
    $('#allocDetail_reAllocQty').val(rows[0].qtySoEa - rows[0].qtyAllocEa);
}

/**
 * 手工分配提交
 */
function allocConfirm() {
    // 表单验证
    var validate = bq.detailSubmitCheck('#manuaAllocInfoModal');
    if (validate.isSuccess) {
        bq.openDisabled("#allocDetailForm");
        jp.loading("正在保存中...");
        var row = {};
        $.extend(row, bq.serializeJson($('#allocDetailForm')));
        $.ajax({
            url: "${ctx}/wms/outbound/banQinWmSoAlloc/manualAlloc",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    $('#manuaAllocInfoModal').modal('hide');
                    $('#banQinWmOrderCheckTable').bootstrapTable('refresh');
                } else {
                    closeDisable();
                    jp.bqError(data.msg);
                }
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

/**
 * 拣货确认
 */
function picking() {
    commonMethod('picking');
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
    jp.get("${ctx}/wms/outbound/banQinWmOrderCheck/" + method + "?soNo=" + getDetailSelections().join('@') + "&orgId=" + getOrgIdSelections()[0], function (data) {
        if (data.success) {
            $('#banQinWmOrderCheckTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function afterSelectAllocPack(row) {
    $('#allocDetail_uomQty').val(row.cdprQuantity);
    allocSoChange();
}

/**
 * 数量同步到EA
 */
function allocSoChange() {
    // 单位换算数量
    var uomQty = !$('#allocDetail_uomQty').val() ? 0 : $('#allocDetail_uomQty').val();
    // 分配数
    var qtyUom = !$('#allocDetail_qtyUom').val() ? 0 : $('#allocDetail_qtyUom').val();
    $('#allocDetail_qtyEa').val(Math.floor(qtyUom * 100) / 100 * uomQty);
}

</script>