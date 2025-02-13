<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function() {
    $('#banQinWmCarrierTypeRelationTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/basicdata/banQinWmCarrierTypeRelation/data",
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
            title: '快递类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_CARRIER_TYPE_RELATION'))}, value, "-");
            }
        }, {
            field: 'description',
            title: '接口信息',
            sortable: true
        }, {
            field: 'mailType',
            title: '面单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_MAIL_TYPE'))}, value, "-");
            }
        }]
    });
    $('#banQinWmCarrierTypeRelationTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmCarrierTypeRelationTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $("#search").click("click", function() {// 绑定查询按扭
        $('#banQinWmCarrierTypeRelationTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function() {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#banQinWmCarrierTypeRelationTable').bootstrapTable('refresh');
    });
});

function getIdSelections() {
    return $.map($("#banQinWmCarrierTypeRelationTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll(){
    jp.confirm('确认要删除该承运商类型关系记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinWmCarrierTypeRelation/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinWmCarrierTypeRelationTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增承运商类型关系', "${ctx}/wms/basicdata/banQinWmCarrierTypeRelation/form", '600px', '300px', $('#banQinWmCarrierTypeRelationTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id == undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="wms:basicdata:banQinWmCarrierTypeRelation:edit">
        jp.openDialog('编辑承运商类型关系', "${ctx}/wms/basicdata/banQinWmCarrierTypeRelation/form?id=" + id,'600px', '200px', $('#banQinWmCarrierTypeRelationTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="wms:basicdata:banQinWmCarrierTypeRelation:edit">
        jp.openDialogView('查看承运商类型关系', "${ctx}/wms/basicdata/banQinWmCarrierTypeRelation/form?id=" + id,'600px', '300px', $('#banQinWmCarrierTypeRelationTable'));
    </shiro:lacksPermission>
}

</script>