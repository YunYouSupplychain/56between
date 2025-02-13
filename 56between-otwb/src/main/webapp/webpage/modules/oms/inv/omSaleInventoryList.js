<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
    $('#parentId').val(jp.getCurrentOrg().orgId);

    $('#omSaleInventoryTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/inv/omSaleInventory/data",
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
            field: 'warehouseName',
            title: '仓库',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
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
            field: 'uomDesc',
            title: '单位',
            sortable: true
        }, {
            field: 'qty',
            title: '数量(件)',
            sortable: true
        }, {
            field: 'qtyAvailable',
            title: '可用数量(件)',
            sortable: true
        }]
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#omSaleInventoryTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#omSaleInventoryTable').bootstrapTable('refresh');
    });
});

</script>