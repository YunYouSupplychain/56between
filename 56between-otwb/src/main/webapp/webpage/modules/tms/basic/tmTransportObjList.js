<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#orgId").val(tmOrg.id);
    initTable();

    $('#btnExport').click(function () {
        bq.exportExcel('${ctx}/tms/basic/tmTransportObj/export', "业务对象信息", $('#searchForm').serializeJSON());
    });
    $("#search").click("click", function () {// 绑定查询按扭
        $('#tmTransportObjTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $("#orgId").val(tmOrg.id);
        $('#tmTransportObjTable').bootstrapTable('refresh');
    });
});

function initTable() {
    var $table = $('#tmTransportObjTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/basic/tmTransportObj/data",
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
            field: 'transportObjCode',
            title: '业务对象编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'transportObjName',
            title: '业务对象名称',
            sortable: true
        }, {
            field: 'transportObjShortName',
            title: '业务对象简称',
            sortable: true
        }, {
            field: 'transportObjType',
            title: '业务对象类型',
            sortable: true,
            formatter: function (value, row, index) {
                var valueArray = value.split(",");
                var labelArray = [];
                for (var i = 0; i < valueArray.length; i++) {
                    labelArray[i] = jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_OBJ_TYPE'))}, valueArray[i], "-");
                }
                return labelArray.join(",");
            }
        }, {
            field: 'contact',
            title: '联系人',
            sortable: true
        }, {
            field: 'phone',
            title: '联系电话',
            sortable: true
        }, {
            field: 'fax',
            title: '传真',
            sortable: true
        }, {
            field: 'email',
            title: '电子邮箱',
            sortable: true
        }, {
            field: 'url',
            title: '网址',
            sortable: true
        }, {
            field: 'unCode',
            title: '统一码',
            sortable: true
        }, {
            field: 'area',
            title: '所属城市',
            sortable: true
        }, {
            field: 'address',
            title: '详细地址',
            sortable: true
        }, {
            field: 'brand',
            title: '品牌',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_OBJ_BRAND'))}, value, "-");
            }
        }, {
            field: 'carrierMatchedOrg',
            title: '承运商对应机构',
            sortable: true
        }, {
            field: 'outletMatchedOrg',
            title: '网点对应机构',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#enable').prop('disabled', !length);
        $('#unable').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#tmTransportObjTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该业务对象信息记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/tms/basic/tmTransportObj/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#tmTransportObjTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function add() {
    jp.openDialog('业务对象信息', "${ctx}/tms/basic/tmTransportObj/form", '80%', '70%', $('#tmTransportObjTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialog('业务对象信息', "${ctx}/tms/basic/tmTransportObj/form?id=" + id, '80%', '70%', $('#tmTransportObjTable'));
}

function enable(flag) {
    jp.loading();
    jp.post("${ctx}/tms/basic/tmTransportObj/enable?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
        if (data.success) {
            $('#tmTransportObjTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}
</script>