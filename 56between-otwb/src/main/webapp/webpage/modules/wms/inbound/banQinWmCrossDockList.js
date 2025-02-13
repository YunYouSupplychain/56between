<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var currentDockRow;
var currentDock1Row;
var queryCondition1;
var queryCondition2;
$(document).ready(function () {
    initTab1();
    initTab2();
    lay('.laydate').each(function(){
        laydate.render({
            elem: this, theme: '#393D49', type: 'datetime'
        });
    });
});

function search1() {
    $('#search1Modal').modal();
}

function queryForm1() {
    query1();
    $('#search1Modal').modal('hide');
}

function query1() {
    $('#dockTable').bootstrapTable('refresh', {'url': "${ctx}/wms/crossDock/banQinWmCrossDock/data1"});
    queryCondition1 = $("#searchForm1").serializeJSON();
}

function initTab1() {
    $('#dockTable').bootstrapTable({
        method: 'post',//请求方法
        showRefresh: true,//显示刷新按钮
        cache: false,//是否使用缓存
        sidePagination: "server",//client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm1").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.ownerCodes = searchParam.ownerCode ? [searchParam.ownerCode] : null;
            searchParam.skuCodes = searchParam.skuCode ? [searchParam.skuCode] : null;
            searchParam.asnTypes = searchParam.asnType ? [searchParam.asnType] : null;
            searchParam.soTypes = searchParam.soType ? [searchParam.soType] : null;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            jp.changeTableStyle($el);
            getWmCrossDockDetailEntity(row);
            currentDockRow = row;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
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
            field: 'qty',
            title: '库存数',
            sortable: true
        }, {
            field: 'asnNum',
            title: '入库单数',
            sortable: true
        }, {
            field: 'qtyPlanEa',
            title: '计划收货数',
            sortable: true
        }, {
            field: 'fmEta',
            title: '最早预计到货时间',
            sortable: true
        }, {
            field: 'toEta',
            title: '最晚预计到货时间',
            sortable: true
        }, {
            field: 'soNum',
            title: '出库单数',
            sortable: true
        }, {
            field: 'qtySoEa',
            title: '计划发货数',
            sortable: true
        }, {
            field: 'fmEtd',
            title: '最早预计发货时间',
            sortable: true
        }, {
            field: 'toEtd',
            title: '最晚预计发货时间',
            sortable: true
        }]
    });

    $('#asnTable').bootstrapTable({
        height: $(window).height() - 350,
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        columns: [{
            checkbox: true
        }, {
            field: 'asnNo',
            title: '入库单号',
            sortable: false
        }, {
            field: 'asnType',
            title: '入库单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_TYPE'))}, value, "-");
            }
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_STATUS'))}, value, "-");
            }
        }, {
            field: 'skuName',
            title: '商品',
            sortable: true
        }, {
            field: 'qtyPlanUom',
            title: '预收数',
            sortable: true
        }, {
            field: 'qtyPlanEa',
            title: '预收数EA',
            sortable: true
        }, {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }]
    });

    $('#soTable').bootstrapTable({
        height: $(window).height() - 350,
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        columns: [{
            checkbox: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'soType',
            title: '出库单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))}, value, "-");
            }
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
            field: 'skuName',
            title: '商品',
            sortable: true
        }, {
            field: 'qtySoUom',
            title: '订货数',
            sortable: true
        }, {
            field: 'qtySoEa',
            title: '订货数EA',
            sortable: true
        }, {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }]
    });

    $("#tab-1").find('.fixed-table-toolbar').find('button[name="refresh"]').off('click').on('click', function () {
        query1();
        $('#asnTable').bootstrapTable('removeAll');
        $('#soTable').bootstrapTable('removeAll');
    });

}

function initTab2() {
    $('#dock1Table').bootstrapTable({
        method: 'post',
        showRefresh: true,//显示刷新按钮
        cache: false,//是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm2").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.ownerCodes = searchParam.ownerCode ? [searchParam.ownerCode] : null;
            searchParam.skuCodes = searchParam.skuCode ? [searchParam.skuCode] : null;
            searchParam.statuss = searchParam.status ? [searchParam.status] : null;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            jp.changeTableStyle($el);
            getWmAsnReceiveEntity(row);
            currentDock1Row = row;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'status',
            title: '越库收货状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_STATUS'))}, value, "-");
            }
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: true
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品',
            sortable: true
        }, {
            field: 'cdType',
            title: '越库类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_CD_TYPE'))}, value, "-");
            }
        }, {
            field: 'asnNo',
            title: '入库单号',
            sortable: true
        }, {
            field: 'rcvLineNo',
            title: '收货行号',
            sortable: true
        }, {
            field: 'qtyPlanUom',
            title: '越库数',
            sortable: true
        }, {
            field: 'qtyPlanEa',
            title: '越库数EA',
            sortable: true
        }, {
            field: 'toLoc',
            title: '收货库位',
            sortable: true
        }, {
            field: 'toId',
            title: '收货跟踪号',
            sortable: true
        }, {
            field: 'planId',
            title: '码盘跟踪号',
            sortable: true
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'soLineNo',
            title: '出库单行号',
            sortable: true
        }]
    });

    $('#asn1Table').bootstrapTable({
        height: $(window).height() - 330,
        cache: false,//是否使用缓存
        sidePagination: "server",//client客户端分页，server服务端分页
        columns: [{
            checkbox: true
        }, {
            field: 'asnNo',
            title: '入库单号',
            sortable: true
        }, {
            field: 'asnType',
            title: '入库单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_TYPE'))}, value, "-");
            }
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_STATUS'))}, value, "-");
            }
        }, {
            field: 'skuName',
            title: '商品',
            sortable: true
        }, {
            field: 'qtyPlanUom',
            title: '预收数',
            sortable: true
        }, {
            field: 'qtyPlanEa',
            title: '预收数EA',
            sortable: true
        }, {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }]
    });

    $('#so1Table').bootstrapTable({
        height: $(window).height() - 370,
        cache: false,//是否使用缓存
        sidePagination: "server",//client客户端分页，server服务端分页
        columns: [{
            checkbox: true
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'soType',
            title: '出库单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))}, value, "-");
            }
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
            field: 'skuName',
            title: '商品',
            sortable: true
        }, {
            field: 'qtySoUom',
            title: '订货数',
            sortable: true
        }, {
            field: 'qtySoEa',
            title: '订货数EA',
            sortable: true
        }, {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        }, {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        }]
    });

    $('#allocTable').bootstrapTable({
        height: $(window).height() - 370,
        cache: false,//是否使用缓存
        sidePagination: "server",//client客户端分页，server服务端分页
        columns: [{
            checkbox: true
        }, {
            field: 'allocId',
            title: '分配ID',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ALLOC_STATUS'))}, value, "-");
            }
        }, {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '出库单行号',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主',
            sortable: true
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'locCode',
            title: '库位',
            sortable: true
        }, {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        }, {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        }, {
            field: 'qtyUom',
            title: '数量',
            sortable: true
        }, {
            field: 'qtyEa',
            title: '数量EA',
            sortable: true
        }, {
            field: 'consigneeCode',
            title: '收货人',
            sortable: true
        }, {
            field: 'cdOutStep',
            title: '直接越库步骤',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_CD_OUT_STEP'))}, value, "-");
            }
        }]
    });

    $("#tab-2").find('.fixed-table-toolbar').find('button[name="refresh"]').off('click').on('click', function () {
        query2();
        $('#asn1Table').bootstrapTable('removeAll');
        $('#so1Table').bootstrapTable('removeAll');
        $('#soAllocTable').bootstrapTable('removeAll');
    });

}

function search2() {
    $('#search2Modal').modal();
}

function queryForm2() {
    query2();
    $('#search2Modal').modal('hide');
}

function query2() {
    $('#dock1Table').bootstrapTable('refresh', {'url': "${ctx}/wms/crossDock/banQinWmCrossDock/data2"});
    queryCondition2 = $("#searchForm2").serializeJSON();
}

function removeAllData(obj) {
    $(obj).bootstrapTable('removeAll')
}

function getWmCrossDockDetailEntity(row) {
    var params = {};
    $.extend(params, queryCondition1, row);
    params.ownerCodes = params.ownerCode ? [params.ownerCode] : null;
    params.skuCodes = params.skuCode ? [params.skuCode] : null;
    params.asnTypes = params.asnType ? [params.asnType] : null;
    params.soTypes = params.soType ? [params.soType] : null;
    $.ajax({
        url: "${ctx}/wms/crossDock/banQinWmCrossDock/getWmCrossDockDetailEntity",
        type: "post",
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                $("#asnTable").bootstrapTable('removeAll').bootstrapTable('load', data.body.asnList);
                $("#soTable").bootstrapTable('removeAll').bootstrapTable('load', data.body.soList);
            }
        }
    })
}

function getWmAsnReceiveEntity(row) {
    var params = {};
    $.extend(params, queryCondition2, row);
    params.ownerCodes = params.ownerCode ? [params.ownerCode] : null;
    params.skuCodes = params.skuCode ? [params.skuCode] : null;
    params.statuss = params.status ? [params.status] : null;
    $.ajax({
        url: "${ctx}/wms/crossDock/banQinWmCrossDock/getWmAsnReceiveEntity",
        type: "post",
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                $("#asn1Table").bootstrapTable('removeAll').bootstrapTable('load', data.body.asnList);
                $("#so1Table").bootstrapTable('removeAll').bootstrapTable('load', data.body.soList);
                $("#allocTable").bootstrapTable('removeAll').bootstrapTable('load', data.body.allocList);
            }
        }
    })
}

function getSelections($table) {
    return $.map($($table).bootstrapTable('getSelections'), function (row) {
        return row;
    });
}

function crossDockCreateTask() {
    var rows = getSelections('#dockTable');
    if (rows.length === 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/crossDock/banQinWmCrossDock/crossDockCreateTask",
        type: 'POST',
        data: JSON.stringify(rows),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                query1();
                removeAllData('#asnTable');
                removeAllData('#soTable');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function crossDocCreateTaskBySkuInDirect() {
    var rows = getSelections('#dockTable');
    if (rows.length === 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/crossDock/banQinWmCrossDock/crossDocCreateTaskBySkuInDirect",
        type: 'POST',
        data: JSON.stringify(rows),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                $('#dockTable').bootstrapTable('refresh', {'url': "${ctx}/wms/crossDock/banQinWmCrossDock/data1"});
                $('#asnTable').bootstrapTable('removeAll');
                $('#soTable').bootstrapTable('removeAll');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function crossDockDetail(method) {
    if (method === "crossDockCreateTaskDetail") {
        var rows1 = getSelections('#asnTable');
        if (rows1.length === 0) {
            jp.bqWaring("请选择入库单");
            return;
        }
        var rows2 = getSelections('#soTable');
        if (rows2.length === 0) {
            jp.bqWaring("请选择出库单");
            return;
        }
    }
    if (method === "createTaskByInDirect") {
        if (getSelections('#asnTable').length === 0 && getSelections('#soTable').length === 0) {
            jp.bqWaring("请选择记录");
            return;
        }
    }
    jp.loading();
    var params = {wmAsnDetailReceiveEntity: getSelections('#asnTable'), wmSoDetailEntity: getSelections('#soTable'), orgId: jp.getCurrentOrg().orgId};
    $.ajax({
        url: "${ctx}/wms/crossDock/banQinWmCrossDock/" + method,
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                query1();
                removeAllData('#asnTable');
                removeAllData('#soTable');
                // getWmCrossDockDetailEntity(currentDockRow);
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function excuteCrossDock() {
    var rows = getSelections('#dock1Table');
    if (rows.length === 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/crossDock/banQinWmCrossDock/crossDockBatchConfirm",
        type: 'POST',
        data: JSON.stringify(rows),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                query2();
                removeAllData('#asn1Table');
                removeAllData('#so1Table');
                removeAllData('#allocTable');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function cancelCrossDock() {
    var rows = getSelections('#dock1Table');
    if (rows.length === 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    jp.loading();
    $.ajax({
        url: "${ctx}/wms/crossDock/banQinWmCrossDock/crossDockCancelConfirm",
        type: 'POST',
        data: JSON.stringify(rows),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                query2();
                removeAllData('#asn1Table');
                removeAllData('#so1Table');
                removeAllData('#allocTable');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function removeDirect() {
    var rows = getSelections('#dock1Table');
    if (rows.length === 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    jp.confirm('确认要删除该记录吗？', function () {
        jp.loading();
        $.ajax({
            url: "${ctx}/wms/crossDock/banQinWmCrossDock/crossDockRemoveByDirect",
            type: 'POST',
            data: JSON.stringify(rows),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    query2();
                    removeAllData('#asn1Table');
                    removeAllData('#so1Table');
                    removeAllData('#allocTable');
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    });
}

function removeIndirect() {
    var rows = getSelections('#dock1Table');
    if (rows.length === 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    jp.confirm('确认要删除该记录吗？', function () {
        jp.loading();
        $.ajax({
            url: "${ctx}/wms/crossDock/banQinWmCrossDock/crossDockRemove",
            type: 'POST',
            data: JSON.stringify(rows),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    query2();
                    removeAllData('#asn1Table');
                    removeAllData('#so1Table');
                    removeAllData('#allocTable');
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    });
}

function crossDockConfirmDetail() {
    var rows1 = getSelections('#asn1Table');
    if (rows1.length === 0) {
        jp.bqWaring("请选择入库单");
        return;
    }
    if (currentDock1Row.status !== '00') {
        jp.bqWaring("订单头不是创建状态，不能操作");
        return;
    }
    jp.loading();
    var params = {wmAsnDetailReceiveEntity: getSelections('#asn1Table'), wmSoDetailEntity: getSelections('#so1Table')};
    $.ajax({
        url: "${ctx}/wms/crossDock/banQinWmCrossDock/crossDockConfirmDetail",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                // getWmAsnReceiveEntity(currentDock1Row);
                query2();
                removeAllData('#asn1Table');
                removeAllData('#so1Table');
                removeAllData('#allocTable');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function cancelRemarkDetail() {
    if (getSelections('#asn1Table').length === 0 && getSelections('#so1Table').length === 0) {
        jp.bqWaring("请选择记录");
        return;
    }
    if (currentDock1Row.status !== '00') {
        jp.bqWaring("不是创建状态，不能操作");
        return;
    }
    if (currentDock1Row.cdType === 'DIRECT') {
        jp.bqWaring("直接越库任务，不能操作");
        return;
    }
    jp.loading();
    var params = {wmAsnDetailReceiveEntity: getSelections('#asn1Table'), wmSoDetailEntity: getSelections('#so1Table')};
    $.ajax({
        url: "${ctx}/wms/crossDock/banQinWmCrossDock/cancelRemarkDetail",
        type: 'POST',
        data: JSON.stringify(params),
        contentType: "application/json",
        success: function (data) {
            if (data.success) {
                getWmAsnReceiveEntity(currentDock1Row);
                query2();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

</script>