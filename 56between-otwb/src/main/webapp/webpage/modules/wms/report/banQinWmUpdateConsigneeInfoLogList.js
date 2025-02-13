<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#createDateFm').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#createDateTo').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

    $('#banQinWmUpdateConsigneeInfoLogTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/outbound/banQinWmUpdateConsigneeInfoLog/data",
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
        },
        {
            field: 'soNo',
            title: '出库单号',
            sortable: true
        },
        {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        },
        {
            field: 'extendNo',
            title: '外部单号',
            sortable: true
        },
        {
            field: 'consigneeName',
            title: '收货人',
            sortable: true
        },
        {
            field: 'consigneeTel',
            title: '收货人电话',
            sortable: true
        },
        {
            field: 'consigneeArea',
            title: '收货人区域',
            sortable: true
        },
        {
            field: 'consigneeAddr',
            title: '收货人地址',
            sortable: true
        },
        {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        },
        {
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        }]
    });

    $("#search").click("click", function () {
        $('#banQinWmUpdateConsigneeInfoLogTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmUpdateConsigneeInfoLogTable').bootstrapTable('refresh');
    });
});
</script>