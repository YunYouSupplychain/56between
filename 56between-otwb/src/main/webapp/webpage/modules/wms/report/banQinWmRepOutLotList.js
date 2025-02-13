<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#beginOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#beginOrderTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 00:00:00");
    $("#endOrderTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd") + " 23:59:59");
    $("#statusList").selectpicker('val', ['70', '80']).selectpicker('refresh');

    $('#banQinWmSoAllocTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/report/banQinWmRepOutLot/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.statuss = $('#statusList').val() ? $('#statusList').val().join(',') : '';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        },
        {
            field: 'lineNo',
            title: '出库单行号',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
            }
        },
            {
                field: 'orderTime',
                title: '订单时间',
                sortable: true
            },
            {
                field: 'shipTime',
                title: '发货时间',
                sortable: true
            },
        {
            field: 'ownerName',
            title: '货主',
            sortable: true
        },
        {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        },
        {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        },
        {
            field: 'locCode',
            title: '库位',
            sortable: true
        },
        {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        },
        {
            field: 'consigneeName',
            title: '收货人',
            sortable: true
        },
        {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        },
        {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        },
        {
            field: 'qtyUom',
            title: '分配数',
            sortable: true
        },
        {
            field: 'qtyEa',
            title: '分配数EA',
            sortable: true
        },
        {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        },
        {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        },
        {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        },
        {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        },
            {
                field: 'customerNo',
                title: '客户订单号',
                sortable: true
            },
            {
                field: 'consigneeName',
                title: '收货人名称',
                sortable: true
            },
            {
                field: 'consigneeTel',
                title: '收货人电话',
                sortable: true
            },
            {
                field: 'consigneeAddr',
                title: '收货人地址',
                sortable: true
            },{
                field: 'orderRemarks',
                title: '订单备注',
                sortable: true
            },
            {
                field: 'skuLength',
                title: '商品长',
                sortable: true
            },
            {
                field: 'skuWidth',
                title: '商品宽',
                sortable: true
            },
            {
                field: 'skuHeight',
                title: '商品高',
                sortable: true
            },
            {
                field: 'cubic',
                title: '商品体积',
                sortable: true
            },
            {
                field: 'grossWeight',
                title: '商品毛重',
                sortable: true
            },

        {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmSoAllocTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmSoAllocTable').bootstrapTable('refresh');
    });
});

function getSelections() {
    return $.map($("#banQinWmSoAllocTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function getIdSelections() {
    return $.map($("#banQinWmSoAllocTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmSoAllocTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/report/banQinWmRepOutLot/export", "出库批次明细", $("#searchForm"), function () {
        jp.close();
    });
}

</script>