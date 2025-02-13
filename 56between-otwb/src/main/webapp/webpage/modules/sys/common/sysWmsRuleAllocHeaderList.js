<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysWmsRuleAllocHeaderTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#sysWmsRuleAllocHeaderTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#sysWmsRuleAllocHeaderTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/wms/ruleAllocHeader/data",
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
            field: 'ruleCode',
            title: '规则编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'ruleName',
            title: '规则名称',
            sortable: true
        }, {
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }]
    });
    $('#sysWmsRuleAllocHeaderTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysWmsRuleAllocHeaderTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#sysWmsRuleAllocHeaderTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该分配规则记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/wms/ruleAllocHeader/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysWmsRuleAllocHeaderTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openBQDialog('新增分配规则', "${ctx}/sys/common/wms/ruleAllocHeader/form", '80%', '80%', $('#sysWmsRuleAllocHeaderTable'));
}

function edit(id) {
    if(id === undefined){
        id = getIdSelections();
    }
    <shiro:hasPermission name="sys:common:wms:ruleAlloc:edit">
        jp.openBQDialog('编辑分配规则', "${ctx}/sys/common/wms/ruleAllocHeader/form?id=" + id,'80%', '80%', $('#sysWmsRuleAllocHeaderTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="sys:common:wms:ruleAlloc:edit">
        jp.openBQDialog('查看分配规则', "${ctx}/sys/common/wms/ruleAllocHeader/form?id=" + id,'80%', '80%', $('#sysWmsRuleAllocHeaderTable'));
    </shiro:lacksPermission>
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/wms/ruleAllocHeader/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/wms/ruleAllocHeader/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

</script>