<%@page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysSmsStoreClerkRelationTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#sysSmsStoreClerkRelationTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#sysSmsStoreClerkRelationTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/sms/storeClerkRelation/clerkData",
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
            field: 'clerkNo',
            title: '操作人编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.clerk + "\",\"" + row.dataSet + "\",\"" + row.dataSetName + "\")'>" + value + "</a>";
            }
        }, {
            field: 'clerkName',
            title: '操作人名称',
            sortable: true
        }, {
            field: 'clerkLoginName',
            title: '操作人登录名',
            sortable: true
        }, {
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }]
    });
    $('#sysSmsStoreClerkRelationTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysSmsStoreClerkRelationTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getClerkSelections() {
    return $.map($("#sysSmsStoreClerkRelationTable").bootstrapTable('getSelections'), function (row) {
        return row.clerk
    });
}

function add(){
    jp.openBQDialog('新增门店操作人关联关系', "${ctx}/sys/common/sms/storeClerkRelation/form",'80%', '80%', $('#sysSmsStoreClerkRelationTable'));
}

function view(clerk, dataSet, dataSetName) {
    if(clerk === undefined){
        var row = $("#sysSmsStoreClerkRelationTable").bootstrapTable('getSelections')[0];
        clerk = row.clerk;
        dataSet = row.dataSet;
        dataSetName = row.dataSetName;
    }
    jp.openBQDialog('查看门店操作人关联关系', "${ctx}/sys/common/sms/storeClerkRelation/form?clerk=" + clerk + "&dataSet=" + dataSet + "&dataSetName=" + dataSetName,'80%', '80%', $('#sysSmsStoreClerkRelationTable'));
}

function edit(clerk, dataSet, dataSetName){
    if(clerk === undefined){
        var row = $("#sysSmsStoreClerkRelationTable").bootstrapTable('getSelections')[0];
        clerk = row.clerk;
        dataSet = row.dataSet;
        dataSetName = row.dataSetName;
    }
    jp.openBQDialog('编辑门店操作人关联关系', "${ctx}/sys/common/sms/storeClerkRelation/form?clerk=" + clerk + "&dataSet=" + dataSet + "&dataSetName=" + dataSetName, '100%', '100%', $('#sysSmsStoreClerkRelationTable'));
}

function deleteByClerk() {
    jp.confirm('确认要删除该操作人门店关联关系记录吗？', function () {
        jp.loading();
        var row = $("#sysSmsStoreClerkRelationTable").bootstrapTable('getSelections')[0];
        jp.get("${ctx}/sys/common/sms/storeClerkRelation/deleteByClerk?clerks=" + getClerkSelections() + "&dataSet=" + row.dataSet + "&dataSetName=" + row.dataSetName, function (data) {
            if (data.success) {
                $('#sysSmsStoreClerkRelationTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function syncSelect() {
    var clerks = getClerkSelections();
    if (clerks.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/sms/storeClerkRelation/syncSelect", {"clerks": clerks.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/sms/storeClerkRelation/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

</script>