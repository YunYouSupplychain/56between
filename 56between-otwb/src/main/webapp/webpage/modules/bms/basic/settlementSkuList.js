<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orgId").val(jp.getCurrentOrg().orgId);
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#settlementSkuTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
    });
});

function initTable() {
    $('#settlementSkuTable').bootstrapTable({
        url: "${ctx}/bms/basic/settlementSku/data",
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
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
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
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
            field: 'skuClassName',
            title: '品类',
            sortable: true
        }, {
            field: 'skuSpec',
            title: '规格',
            sortable: true
        }, {
            field: 'length',
            title: '长',
            sortable: true
        }, {
            field: 'width',
            title: '宽',
            sortable: true
        }, {
            field: 'height',
            title: '高',
            sortable: true
        }, {
            field: 'grossWeight',
            title: '毛重',
            sortable: true
        }, {
            field: 'netWeight',
            title: '净重',
            sortable: true
        }, {
            field: 'volume',
            title: '体积',
            sortable: true
        }, {
            field: 'eaQuantity',
            title: '件-换算比例',
            sortable: true
        }, {
            field: 'ipQuantity',
            title: '小包装-换算比例',
            sortable: true
        }, {
            field: 'csQuantity',
            title: '箱-换算比例',
            sortable: true
        }, {
            field: 'plQuantity',
            title: '托-换算比例',
            sortable: true
        }, {
            field: 'otQuantity',
            title: '大包装-换算比例',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#settlementSkuTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#settlementSkuTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该结算商品记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/settlementSku/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#settlementSkuTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增结算商品', "${ctx}/bms/basic/settlementSku/form", '80%', '80%', $('#settlementSkuTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basic:settlementSku:edit">
        jp.openDialog('编辑结算商品', "${ctx}/bms/basic/settlementSku/form?id=" + id, '80%', '80%', $('#settlementSkuTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basic:settlementSku:edit">
        jp.openDialogView('查看结算商品', "${ctx}/bms/basic/settlementSku/form?id=" + id, '80%', '80%', $('#settlementSkuTable'));
    </shiro:lacksPermission>
}

</script>