<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysWmsCarrierTypeRelationTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#sysWmsCarrierTypeRelationTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#sysWmsCarrierTypeRelationTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/wms/carrierTypeRelation/data",
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
            field: 'carrierCode',
            title: '承运商编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'carrierName',
            title: '承运商名称',
            sortable: true
        }, {
            field: 'type',
            title: '类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_CARRIER_TYPE_RELATION'))}, value, "-");
            }
        }, {
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }]
    });
    $('#sysWmsCarrierTypeRelationTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysWmsCarrierTypeRelationTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length );
        $('#edit').prop('disabled', length!==1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#sysWmsCarrierTypeRelationTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll(){
    jp.confirm('确认要删除该承运商类型关系记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/wms/carrierTypeRelation/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysWmsCarrierTypeRelationTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add(){
    jp.openDialog('新增承运商类型关系', "${ctx}/sys/common/wms/carrierTypeRelation/form", '800px', '500px', $('#sysWmsCarrierTypeRelationTable'));
}

function edit(id){//没有权限时，不显示确定按钮
    if(id === undefined){
        id = getIdSelections();
    }
    <shiro:hasPermission name="sys:common:wms:carrierTypeRelation:edit">
        jp.openDialog('编辑承运商类型关系', "${ctx}/sys/common/wms/carrierTypeRelation/form?id=" + id,'800px', '500px', $('#sysWmsCarrierTypeRelationTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="sys:common:wms:carrierTypeRelation:edit">
        jp.openDialogView('查看承运商类型关系', "${ctx}/sys/common/wms/carrierTypeRelation/form?id=" + id,'800px', '500px', $('#sysWmsCarrierTypeRelationTable'));
    </shiro:lacksPermission>
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/wms/carrierTypeRelation/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/wms/carrierTypeRelation/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

</script>