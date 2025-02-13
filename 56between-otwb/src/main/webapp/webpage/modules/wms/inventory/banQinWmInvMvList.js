<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();
});

function initTable() {
    $('#banQinWmInvMvTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmInvMv/data",
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
            field: 'ownerCode',
            title: '货主编码',
            sortable: true,
            events: {
                'click .view': function (e, value, row, index) {
                    var params = bq.objToUrlParams(row);
                    jp.openBQDialog('查看库存移动', "${ctx}/wms/inventory/banQinWmInvMv/form?" + params, '90%', '90%', $('#banQinWmInvMvTable'));
                }
            },
            formatter: function operateFormatter(value, row, index) {
                return ['<a href="#" class="view" title="查看" >' + value + ' </a>'].join('');
            }
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
            field: 'fmLoc',
            title: '库位编码',
            sortable: true
        }, {
            field: 'fmTraceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'fmQty',
            title: '库存数',
            sortable: true
        }, {
            field: 'qtyHold',
            title: '冻结数',
            sortable: true
        }, {
            field: 'availableQty',
            title: '库存可用数',
            sortable: true
        }]
    });

    $("#search").click("click", function () {
        $('#banQinWmInvMvTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmInvMvTable').bootstrapTable('refresh');
    });
}

function view(row) {
    jp.openBQDialog('查看库存移动', "${ctx}/wms/inventory/banQinWmInvMv/form?" + row, '90%', '90%', $('#banQinWmInvMvTable'));
}

</script>