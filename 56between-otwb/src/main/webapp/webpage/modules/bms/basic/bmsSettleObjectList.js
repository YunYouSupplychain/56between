<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#bmsSettleObjectTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#bmsSettleObjectTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#bmsSettleObjectTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsSettleObject/data",
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
            field: 'settleObjectCode',
            title: '结算对象代码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'settleObjectName',
            title: '结算对象名称',
            sortable: true
        }, {
            field: 'settleCategory',
            title: '结算类别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'settleType',
            title: '结算方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_TYPE'))}, value, "-");
            }
        }]
    });
    $('#bmsSettleObjectTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsSettleObjectTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsSettleObjectTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该结算对象记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/bmsSettleObject/deleteAll?ids=" + getIdSelections(), function (data) {
            $('#bmsSettleObjectTable').bootstrapTable('refresh');
            if (data.success) {
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增结算对象', "${ctx}/bms/basic/bmsSettleObject/form", '800px', '420px', $('#bmsSettleObjectTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="bms:settleObject:edit">
        jp.openDialog('编辑结算对象', "${ctx}/bms/basic/bmsSettleObject/form?id=" + id, '800px', '420px', $('#bmsSettleObjectTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="bms:settleObject:edit">
        jp.openDialogView('查看结算对象', "${ctx}/bms/basic/bmsSettleObject/form?id=" + id, '800px', '420px', $('#bmsSettleObjectTable'));
    </shiro:lacksPermission>
}

</script>