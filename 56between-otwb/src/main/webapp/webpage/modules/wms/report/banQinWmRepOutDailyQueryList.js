<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#createTimeF').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#createTimeT').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#beginOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    countQty();

    $('#banQinWmRepOutDailyQueryTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/report/banQinWmRepOutDailyQuery/data",
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
            field: 'lineNo',
            title: '出库单行号',
            sortable: true
        }, {
            field: 'def5',
            title: '客户订单号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_STATUS'))}, value, "-");
            }
        }, {
            field: 'soType',
            title: '出库单类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SO_TYPE'))}, value, "-");
            }
        }, {
            field: 'logisticNo',
            title: '物流单号',
            sortable: true
        }, {
            field: 'logisticLineNo',
            title: '物流单行号',
            sortable: true
        }, {
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
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
            title: '商品',
            sortable: true
        }, {
            field: 'packCode',
            title: '包装规格',
            sortable: true
        }, {
            field: 'qtySoUom',
            title: '订货箱数',
            sortable: true
        }, {
            field: 'qtySoEa',
            title: '订货数EA',
            sortable: true
        }, {
            field: 'qtyShipUom',
            title: '发货箱数',
            sortable: true
        }, {
            field: 'qtyShipEa',
            title: '发货数EA',
            sortable: true
        }, {
            field: 'fmEtd',
            title: '预计发货时间从',
            sortable: true
        }, {
            field: 'toEtd',
            title: '预计发货时间到',
            sortable: true
        }, {
            field: 'consigneeCode',
            title: '收货人编码',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货人名称',
            sortable: true
        }, {
            field: 'def1',
            title: '自定义1',
            sortable: true
        }, {
            field: 'def2',
            title: '自定义2',
            sortable: true
        }, {
            field: 'def3',
            title: '自定义3',
            sortable: true
        }, {
            field: 'def4',
            title: '自定义4',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });

    $("#search").click("click", function () {
        $('#banQinWmRepOutDailyQueryTable').bootstrapTable('refresh');
        countQty();
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmRepOutDailyQueryTable').bootstrapTable('refresh');
    });
});

function countQty() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    jp.post("${ctx}/wms/report/banQinWmRepOutDailyQuery/count", searchParam, function (data) {
        $('#totalSoQty').val(data.totalSoEaQty);
        $('#totalShipQty').val(data.totalShipEaQty);
    })
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/report/banQinWmRepOutDailyQuery/export", "出库日报表记录", $("#searchForm"), function () {
        jp.close();
    });
}

</script>