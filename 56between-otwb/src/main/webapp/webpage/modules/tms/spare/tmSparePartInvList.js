<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#baseOrgId').val(tmOrg.id);
    $("#inboundTimeFm").datetimepicker({format: "YYYY-MM-DD 00:00:00"});
    $("#inboundTimeTo").datetimepicker({format: "YYYY-MM-DD 23:59:59"});
    initTable();

    $('#search').click(function () {
        $('#tmSparePartInvTable').bootstrapTable('refresh');
    });
    $('#reset').click(function () {
        $('#searchForm input').val('');
        $('#searchForm select').val('');
        $('#searchForm .select-item').val('');
        $('#tmSparePartInvTable').bootstrapTable('refresh');
    });
});

function initTable() {
    var $table = $('#tmSparePartInvTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/spare/inv/data",
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
            field: 'fittingCode',
            title: '备件编码',
            sortable: true
        }, {
            field: 'barcode',
            title: '条码',
            sortable: true
        }, {
            field: 'fittingName',
            title: '备件名称',
            sortable: true
        }, {
            field: 'supplierCode',
            title: '供应商编码',
            sortable: true
        }, {
            field: 'supplierName',
            title: '供应商名称',
            sortable: true
        }, {
            field: 'inboundTime',
            title: '入库时间',
            sortable: true
        }, {
            field: 'price',
            title: '单价',
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
            title: '更新人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '更新时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
}
</script>