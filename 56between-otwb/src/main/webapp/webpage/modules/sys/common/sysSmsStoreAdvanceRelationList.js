<%@page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysSmsStoreAdvanceRelationTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#sysSmsStoreAdvanceRelationTable').bootstrapTable('refresh');
    });

});

function initTable() {
    $('#sysSmsStoreAdvanceRelationTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/sms/storeAdvanceRelation/data",
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
            field: 'storeCode',
            title: '门店编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'storeName',
            title: '门店名称',
            sortable: true
        }, {
            field: 'advanceCode',
            title: '垫资方编码',
            sortable: true
        }, {
            field: 'advanceName',
            title: '垫资方名称',
            sortable: true
        }, {
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }]
    });
    $('#sysSmsStoreAdvanceRelationTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysSmsStoreAdvanceRelationTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#sysSmsStoreAdvanceRelationTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该门店垫资方关联关系记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/sms/storeAdvanceRelation/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysSmsStoreAdvanceRelationTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add(){
    jp.openDialog('新增门店垫资方关联关系', "${ctx}/sys/common/sms/storeAdvanceRelation/form",'600px', '300px', $('#sysSmsStoreAdvanceRelationTable'));
}

function view(id) {
    if(id === undefined){
        id = getIdSelections();
    }
    jp.openDialogView('查看门店垫资方关联关系', "${ctx}/sys/common/sms/storeAdvanceRelation/form?id=" + id,'600px', '300px', $('#sysSmsStoreAdvanceRelationTable'));
}

function edit(id){
    if(id === undefined){
        id = getIdSelections();
    }
    jp.openDialog('编辑门店垫资方关联关系', "${ctx}/sys/common/sms/storeAdvanceRelation/form?id=" + id,'600px', '300px', $('#sysSmsStoreAdvanceRelationTable'));
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/sms/storeAdvanceRelation/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/sms/storeAdvanceRelation/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

</script>