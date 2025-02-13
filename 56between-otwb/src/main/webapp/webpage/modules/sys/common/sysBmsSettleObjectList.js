<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysBmsSettleObjectTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#sysBmsSettleObjectTable').bootstrapTable('refresh');
    });

});

function initTable() {
    $('#sysBmsSettleObjectTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/bms/settleObject/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
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
    $('#sysBmsSettleObjectTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysBmsSettleObjectTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#sysBmsSettleObjectTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该结算对象记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/bms/settleObject/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysBmsSettleObjectTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增结算对象', "${ctx}/sys/common/bms/settleObject/form", '100%', '100%', $('#sysBmsSettleObjectTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="sys:common:bms:settleObject:edit">
        jp.openDialog('编辑结算对象', "${ctx}/sys/common/bms/settleObject/form?id=" + id, '100%', '100%', $('#sysBmsSettleObjectTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="sys:common:bms:settleObject:edit">
        jp.openDialogView('查看结算对象', "${ctx}/sys/common/bms/settleObject/form?id=" + id, '100%', '100%', $('#sysBmsSettleObjectTable'));
    </shiro:lacksPermission>
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/bms/settleObject/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/bms/settleObject/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

</script>