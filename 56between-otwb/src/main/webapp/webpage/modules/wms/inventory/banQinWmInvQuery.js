<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initByOwnerTable();
    initBySkuTable();
    initByLotTable();
    initByLocTable();
    initBySkuLocTable();
    initByLotLocTraceIdTable();
});

/**
 * 初始化按货主查询table
 */
function initByOwnerTable() {
    $('#byOwner_invTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmInvQuery/byOwner",
        queryParams: function (params) {
            var searchParam = $("#byOwner_searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
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
            field: 'printUom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'cdprDesc',
            title: '包装单位名称',
            sortable: true
        }, {
            field: 'qtyUom',
            title: '库存数',
            sortable: true
        }, {
            field: 'qty',
            title: '库存数EA',
            sortable: true
        }, {
            field: 'qtyAvailable',
            title: '库存可用数EA',
            sortable: true
        }, {
            field: 'qtyAlloc',
            title: '分配数EA',
            sortable: true
        }, {
            field: 'qtyPk',
            title: '拣货数EA',
            sortable: true
        }, {
            field: 'qtyHold',
            title: '冻结数EA',
            sortable: true
        }, {
            field: 'qtyPaOut',
            title: '上架待出数EA',
            sortable: true
        }, {
            field: 'qtyRpOut',
            title: '补货待出数EA',
            sortable: true
        }, {
            field: 'qtyMvOut',
            title: '移动待出数EA',
            sortable: true
        }, {
            field: 'qtyPaIn',
            title: '上架待入数EA',
            sortable: true
        }, {
            field: 'qtyRpIn',
            title: '补货待入数EA',
            sortable: true
        }, {
            field: 'qtyMvIn',
            title: '移动待入数EA',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $("#byOwner_search").click("click", function () {
        $('#byOwner_invTable').bootstrapTable('refresh');
    });
    $("#byOwner_reset").click("click", function () {
        $("#byOwner_searchForm input").val("");
        $("#byOwner_searchForm select").val("");
        $("#byOwner_searchForm .select-item").html("");
        $('#byOwner_invTable').bootstrapTable('refresh');
    });

    datetimepicker('#byOwner_lotAtt01');
    datetimepicker('#byOwner_lotAtt02');
    datetimepicker('#byOwner_lotAtt03');
}

/**
 * 初始化按商品查询table
 */
function initBySkuTable() {
    $('#bySku_invTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmInvQuery/bySku",
        queryParams: function (params) {
            var searchParam = $("#bySku_searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
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
            field: 'printUom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'cdprDesc',
            title: '包装单位名称',
            sortable: true
        }, {
            field: 'qtyUom',
            title: '库存数',
            sortable: true
        }, {
            field: 'qty',
            title: '库存数EA',
            sortable: true
        }, {
            field: 'qtyAvailable',
            title: '库存可用数EA',
            sortable: true
        }, {
            field: 'qtyAlloc',
            title: '分配数EA',
            sortable: true
        }, {
            field: 'qtyPk',
            title: '拣货数EA',
            sortable: true
        }, {
            field: 'qtyHold',
            title: '冻结数EA',
            sortable: true
        }, {
            field: 'qtyPaOut',
            title: '上架待出数EA',
            sortable: true
        }, {
            field: 'qtyRpOut',
            title: '补货待出数EA',
            sortable: true
        }, {
            field: 'qtyMvOut',
            title: '移动待出数EA',
            sortable: true
        }, {
            field: 'qtyPaIn',
            title: '上架待入数EA',
            sortable: true
        }, {
            field: 'qtyRpIn',
            title: '补货待入数EA',
            sortable: true
        }, {
            field: 'qtyMvIn',
            title: '移动待入数EA',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $("#bySku_search").click("click", function () {
        $('#bySku_invTable').bootstrapTable('refresh');
    });
    $("#bySku_reset").click("click", function () {
        $("#bySku_searchForm input").val("");
        $("#bySku_searchForm select").val("");
        $("#bySku_searchForm .select-item").html("");
        $('#bySku_invTable').bootstrapTable('refresh');
    });
    datetimepicker('#bySku_lotAtt01');
    datetimepicker('#bySku_lotAtt02');
    datetimepicker('#bySku_lotAtt03');
}

/**
 * 初始化按批次查询table
 */
function initByLotTable() {
    $('#byLot_invTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmInvQuery/byLot",
        queryParams: function (params) {
            var searchParam = $("#byLot_searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
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
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'printUom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'cdprDesc',
            title: '包装单位名称',
            sortable: true
        }, {
            field: 'qtyUom',
            title: '库存数',
            sortable: true
        }, {
            field: 'qty',
            title: '库存数EA',
            sortable: true
        }, {
            field: 'qtyAvailable',
            title: '库存可用数',
            sortable: true
        }, {
            field: 'qtyAlloc',
            title: '分配数EA',
            sortable: true
        }, {
            field: 'qtyPk',
            title: '拣货数EA',
            sortable: true
        }, {
            field: 'qtyHold',
            title: '冻结数EA',
            sortable: true
        }, {
            field: 'qtyPaOut',
            title: '上架待出数EA',
            sortable: true
        }, {
            field: 'qtyRpOut',
            title: '补货待出数EA',
            sortable: true
        }, {
            field: 'qtyMvOut',
            title: '移动待出数EA',
            sortable: true
        }, {
            field: 'qtyPaIn',
            title: '上架待入数EA',
            sortable: true
        }, {
            field: 'qtyRpIn',
            title: '补货待入数EA',
            sortable: true
        }, {
            field: 'qtyMvIn',
            title: '移动待入数EA',
            sortable: true
        }, {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        }, {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        }, {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        }, {
            field: 'lotAtt04',
            title: '品质',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_ATT'))}, value, "-");
            }
        }, {
            field: 'lotAtt05',
            title: '批次属性05',
            sortable: true
        }, {
            field: 'lotAtt06',
            title: '批次属性06',
            sortable: true
        }, {
            field: 'lotAtt07',
            title: '批次属性07',
            sortable: true
        }, {
            field: 'lotAtt08',
            title: '批次属性08',
            sortable: true
        }, {
            field: 'lotAtt09',
            title: '批次属性09',
            sortable: true
        }, {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: true
        }, {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: true
        }, {
            field: 'lotAtt12',
            title: '批次属性12',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $("#byLot_search").click("click", function () {
        $('#byLot_invTable').bootstrapTable('refresh');
    });
    $("#byLot_reset").click("click", function () {// 绑定查询按扭
        $("#byLot_searchForm input").val("");
        $("#byLot_searchForm select").val("");
        $("#byLot_searchForm .select-item").html("");
        $('#byLot_invTable').bootstrapTable('refresh');
    });
    datetimepicker('#byLot_lotAtt01');
    datetimepicker('#byLot_lotAtt02');
    datetimepicker('#byLot_lotAtt03');
}

/**
 * 初始化按库位查询table
 */
function initByLocTable() {
    $('#byLoc_invTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmInvQuery/byLoc",
        queryParams: function (params) {
            var searchParam = $("#byLoc_searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
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
            sortable: true
        }, {
            field: 'qty',
            title: '库存数',
            sortable: true
        }, {
            field: 'qtyAvailable',
            title: '库存可用数',
            sortable: true
        }, {
            field: 'qtyAlloc',
            title: '分配数',
            sortable: true
        }, {
            field: 'qtyPk',
            title: '拣货数',
            sortable: true
        }, {
            field: 'qtyHold',
            title: '冻结数',
            sortable: true
        }, {
            field: 'qtyPaOut',
            title: '上架待出数',
            sortable: true
        }, {
            field: 'qtyRpOut',
            title: '补货待出数',
            sortable: true
        }, {
            field: 'qtyMvOut',
            title: '移动待出数',
            sortable: true
        }, {
            field: 'qtyPaIn',
            title: '上架待入数',
            sortable: true
        }, {
            field: 'qtyRpIn',
            title: '补货待入数',
            sortable: true
        }, {
            field: 'qtyMvIn',
            title: '移动待入数',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $("#byLoc_search").click("click", function () {
        $('#byLoc_invTable').bootstrapTable('refresh');
    });
    $("#byLoc_reset").click("click", function () {// 绑定查询按扭
        $("#byLoc_searchForm input").val("");
        $("#byLoc_searchForm select").val("");
        $("#byLoc_searchForm .select-item").html("");
        $('#byLoc_invTable').bootstrapTable('refresh');
    });
    datetimepicker('#byLoc_lotAtt01');
    datetimepicker('#byLoc_lotAtt02');
    datetimepicker('#byLoc_lotAtt03');
}

/**
 * 初始化按商品/库位查询table
 */
function initBySkuLocTable() {
    $('#bySkuLoc_invTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmInvQuery/bySkuLoc",
        queryParams: function (params) {
            var searchParam = $("#bySkuLoc_searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
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
            field: 'locCode',
            title: '库位编码',
            sortable: true
        }, {
            field: 'printUom',
            title: '包装单位',
            sortable: true
        }, {
            field: 'cdprDesc',
            title: '包装单位名称',
            sortable: true
        }, {
            field: 'qtyUom',
            title: '库存数',
            sortable: true
        }, {
            field: 'qty',
            title: '库存数EA',
            sortable: true
        }, {
            field: 'qtyAvailable',
            title: '库存可用数EA',
            sortable: true
        }, {
            field: 'qtyAlloc',
            title: '分配数EA',
            sortable: true
        }, {
            field: 'qtyPk',
            title: '拣货数EA',
            sortable: true
        }, {
            field: 'qtyHold',
            title: '冻结数EA',
            sortable: true
        }, {
            field: 'qtyPaOut',
            title: '上架待出数EA',
            sortable: true
        }, {
            field: 'qtyRpOut',
            title: '补货待出数EA',
            sortable: true
        }, {
            field: 'qtyMvOut',
            title: '移动待出数EA',
            sortable: true
        }, {
            field: 'qtyPaIn',
            title: '上架待入数EA',
            sortable: true
        }, {
            field: 'qtyRpIn',
            title: '补货待入数EA',
            sortable: true
        }, {
            field: 'qtyMvIn',
            title: '移动待入数EA',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $("#bySkuLoc_search").click("click", function () {
        $('#bySkuLoc_invTable').bootstrapTable('refresh');
    });
    $("#bySkuLoc_reset").click("click", function () {
        $("#bySkuLoc_searchForm input").val("");
        $("#bySkuLoc_searchForm select").val("");
        $("#bySkuLoc_searchForm .select-item").html("");
        $('#bySkuLoc_invTable').bootstrapTable('refresh');
    });
    datetimepicker('#bySkuLoc_lotAtt01');
    datetimepicker('#bySkuLoc_lotAtt02');
    datetimepicker('#bySkuLoc_lotAtt03');
}

/**
 * 初始化按批次/库位/跟踪号查询table
 */
function initByLotLocTraceIdTable() {
    $('#byLotLocTraceId_invTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmInvQuery/byLotLocTraceId",
        queryParams: function (params) {
            var searchParam = $("#byLotLocTraceId_searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
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
            field: 'printUom',
            title: '包装单位',
            sortable: true
        }, {
        
            field: 'cdprDesc',
            title: '包装单位名称',
            sortable: true
        }, {
            field: 'qtyUom',
            title: '库存数',
            sortable: true
        }, {
            field: 'qty',
            title: '库存数EA',
            sortable: true
        }, {
            field: 'qtyAvailable',
            title: '库存可用数EA',
            sortable: true
        }, {
            field: 'qtyAlloc',
            title: '分配数EA',
            sortable: true
        }, {
            field: 'qtyPk',
            title: '拣货数EA',
            sortable: true
        }, {
            field: 'qtyHold',
            title: '冻结数EA',
            sortable: true
        }, {
            field: 'qtyPaOut',
            title: '上架待出数EA',
            sortable: true
        }, {
            field: 'qtyRpOut',
            title: '补货待出数EA',
            sortable: true
        }, {
            field: 'qtyMvOut',
            title: '移动待出数EA',
            sortable: true
        }, {
            field: 'qtyPaIn',
            title: '上架待入数EA',
            sortable: true
        }, {
            field: 'qtyRpIn',
            title: '补货待入数EA',
            sortable: true
        }, {
            field: 'qtyMvIn',
            title: '移动待入数EA',
            sortable: true
        }, {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        }, {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        }, {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        }, {
            field: 'lotAtt04',
            title: '品质',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_ATT'))}, value, "-");
            }
        }, {
            field: 'lotAtt05',
            title: '批次属性05',
            sortable: true
        }, {
            field: 'lotAtt06',
            title: '批次属性06',
            sortable: true
        }, {
            field: 'lotAtt07',
            title: '批次属性07',
            sortable: true
        }, {
            field: 'lotAtt08',
            title: '批次属性08',
            sortable: true
        }, {
            field: 'lotAtt09',
            title: '批次属性09',
            sortable: true
        }, {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: true
        }, {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: true
        }, {
            field: 'lotAtt12',
            title: '批次属性12',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $("#byLotLocTraceId_search").click("click", function () {
        $('#byLotLocTraceId_invTable').bootstrapTable('refresh');
    });
    $("#byLotLocTraceId_reset").click("click", function () {
        $("#byLotLocTraceId_searchForm input").val("");
        $("#byLotLocTraceId_searchForm select").val("");
        $("#byLotLocTraceId_searchForm .select-item").html("");
        $('#byLotLocTraceId_invTable').bootstrapTable('refresh');
    });
    datetimepicker('#byLotLocTraceId_lotAtt01');
    datetimepicker('#byLotLocTraceId_lotAtt02');
    datetimepicker('#byLotLocTraceId_lotAtt03');
}

function datetimepicker(obj) {
    $(obj).datetimepicker({format: "YYYY-MM-DD"});
}

/**
 * 获取table选中行Id
 * @param obj
 * @returns {*}
 */
function getSelections(obj) {
    return $.map($(obj).bootstrapTable('getSelections'), function (row) {
        return row
    });
}

/**
 * 按货主查询库存交易
 */
function queryInvLogByOwner() {
    var rows = getSelections('#byOwner_invTable');
    if (rows.length === 0) {
        jp.warning("请勾选记录!");
        return;
    }
    if (rows.length > 1) {
        jp.warning("只能选择一条记录!");
        return;
    }
    
    var params = "fmToOwner=" + rows[0].ownerCode;
    queryInvTransaction(params);
}

/**
 * 按商品查询库存交易
 */
function queryInvLogBySku() {
    var rows = getSelections('#bySku_invTable');
    if (rows.length === 0) {
        jp.warning("请勾选记录!");
        return;
    }
    if (rows.length > 1) {
        jp.warning("只能选择一条记录!");
        return;
    }
    
    var params = "fmToOwner=" + rows[0].ownerCode + "&fmToSku=" + rows[0].skuCode;
    queryInvTransaction(params);
}

/**
 * 按批次查询库存交易
 */
function queryInvLogByLot() {
    var rows = getSelections('#byLot_invTable');
    if (rows.length === 0) {
        jp.warning("请勾选记录!");
        return;
    }
    if (rows.length > 1) {
        jp.warning("只能选择一条记录!");
        return;
    }

    var params = "fmToOwner=" + rows[0].ownerCode + "&fmToSku=" + rows[0].skuCode + "&fmToLot=" + rows[0].lotNum;
    queryInvTransaction(params);
}

/**
 * 按库位查询库存交易
 */
function queryInvLogByLoc() {
    var rows = getSelections('#byLoc_invTable');
    if (rows.length === 0) {
        jp.warning("请勾选记录!");
        return;
    }
    if (rows.length > 1) {
        jp.warning("只能选择一条记录!");
        return;
    }

    var params = "fmToLoc=" + rows[0].locCode;
    queryInvTransaction(params);
}

/**
 * 按商品/库位查询库存交易
 */
function queryInvLogBySkuLoc() {
    var rows = getSelections('#bySkuLoc_invTable');
    if (rows.length === 0) {
        jp.warning("请勾选记录!");
        return;
    }
    if (rows.length > 1) {
        jp.warning("只能选择一条记录!");
        return;
    }

    var params = "fmToOwner=" + rows[0].ownerCode + "&fmToSku=" + rows[0].skuCode + "&fmToLoc=" + rows[0].locCode;
    queryInvTransaction(params);
}

/**
 * 按批次/库位/跟踪号查询库存交易
 */
function queryInvLogByLotLocTraceId() {
    var rows = getSelections('#byLotLocTraceId_invTable');
    if (rows.length === 0) {
        jp.warning("请勾选记录!");
        return;
    }
    if (rows.length > 1) {
        jp.warning("只能选择一条记录!");
        return;
    }

    var params = "fmToOwner=" + rows[0].ownerCode + "&fmToSku=" + rows[0].skuCode + "&fmToLot=" + rows[0].lotNum + "&fmToLoc=" + rows[0].locCode + "&fmToId=" + rows[0].traceId;
    queryInvTransaction(params);
}

/**
 * 查询库存交易
 */
function queryInvTransaction(params) {
    jp.openTab("${ctx}/wms/inventory/banQinWmActTran/list?" + params, "库存交易", false);
}

function importInv() {
    $("#uploadModal").modal();
    $("#uploadFileName").val('');
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
    fm.append('orgId', jp.getCurrentOrg().orgId);
    $.ajax({
        type: "post",
        url: "${ctx}/wms/inventory/banQinWmInvQuery/import",
        data: fm,
        cache: false,
        contentType: false,
        processData: false,
        // 传送请求数据
        success: function (data) {
            $("#uploadModal").modal('hide');
            $('#byLotLocTraceId_invTable').bootstrapTable('refresh');
            jp.alert(data.msg);
        },
        error: function (data) {
            $("#uploadModal").modal('hide');
            jp.alert(data.msg);
        }
    });
}

function exportInv() {
    var searchParam = $("#byLotLocTraceId_searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    jp.downloadFile("${ctx}/wms/inventory/banQinWmInvQuery/export?" + bq.objToUrlParams(searchParam));
}

function printTraceLabel() {
    var ids = $.map($('#byLotLocTraceId_invTable').bootstrapTable('getSelections'), function (row) {
        return row.id;
    });
    if (ids.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inventory/banQinWmInvQuery/printTraceLabel", 'ids', ids.join(','), '打印托盘标签');
}

function printTraceLabelQrCode() {
    var ids = $.map($('#byLotLocTraceId_invTable').bootstrapTable('getSelections'), function (row) {
        return row.id;
    });
    if (ids.length <= 0) {
        jp.error("请至少选择一条数据！");
        return;
    }
    bq.openPostWindow("${ctx}/wms/inventory/banQinWmInvQuery/printTraceLabelQrCode", 'ids', ids.join(','), '打印托盘标签(二维码)');
}

</script>