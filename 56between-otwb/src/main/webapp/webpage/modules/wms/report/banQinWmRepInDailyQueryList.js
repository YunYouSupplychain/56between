<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#rcvTimeFrom').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#rcvTimeTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#beginOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endOrderTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    countQty();

    $('#banQinWmRepInDailyQueryTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/report/banQinWmRepInDailyQuery/data",
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
            field: 'asnNo',
            title: '入库单号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '入库单行号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_STATUS'))}, value, "-");
            }
        }, {
            field: 'asnType',
            title: '入库单类型',
            sortable: true,
            formatter:function(value, row , index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_TYPE'))}, value, "-");
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
            field: 'orderTime',
            title: '订单时间',
            sortable: true
        }, {
            field: 'rcvTime',
            title: '收货时间',
            sortable: true
        }, {
            field: 'packCode',
            title: '包装规格',
            sortable: true
        }, {
            field: 'qtyAsnUom',
            title: '预收箱数',
            sortable: true
        }, {
            field: 'qtyAsnEa',
            title: '预收数EA',
            sortable: true
        }, {
            field: 'qtyRcvUom',
            title: '已收箱数',
            sortable: true
        }, {
            field: 'qtyRcvEa',
            title: '已收数EA',
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
            field: 'fmEta',
            title: '预计到货时间从',
            sortable: true
        }, {
            field: 'toEta',
            title: '预计到货时间到',
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
            sortable: true
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
            field: 'def5',
            title: '自定义5',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });

    $("#search").click("click", function () {
        $('#banQinWmRepInDailyQueryTable').bootstrapTable('refresh');
        countQty()
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmRepInDailyQueryTable').bootstrapTable('refresh');
    });
});

function countQty() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    jp.post("${ctx}/wms/report/banQinWmRepInDailyQuery/count", searchParam, function (data) {
        $('#totalAsnQty').val(data.totalQtyAsnEa);
        $('#totalRcvQty').val(data.totalQtyRcvEa);
    })
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/report/banQinWmRepInDailyQuery/export", "入库日报表记录", $("#searchForm"), function () {
        jp.close();
    });
}

</script>