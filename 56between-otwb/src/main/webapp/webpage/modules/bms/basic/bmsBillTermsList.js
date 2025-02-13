<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#bmsBillTermsTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#bmsBillTermsTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#bmsBillTermsTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsBillTerms/data",
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
            field: 'billTermsCode',
            title: '计费条款代码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'billTermsDesc',
            title: '计费条款说明',
            sortable: true
        }, {
            field: 'billModule',
            title: '费用模块',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_MODULE'))}, value, "-");
            }
        }, {
            field: 'outputObjects',
            title: '输出对象',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_OUTPUT_OBJECT'))}, value, "-");
            }
        }, {
            field: 'occurrenceQuantity',
            title: '发生量',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_OCCURRENCE_QUANTITY'))}, value, "-");
            }
        }, {
            field: 'useIndustry',
            title: '行业',
            sortable: true
        }, {
            field: 'remarks',
            title: '逻辑说明',
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
        }]
    });

    $('#bmsBillTermsTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsBillTermsTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#duplicate').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsBillTermsTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该计费条款记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/bmsBillTerms/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsBillTermsTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function add() {
    jp.openDialog('新增计费条款', "${ctx}/bms/basic/bmsBillTerms/form", '100%', '100%', $('#bmsBillTermsTable'));
}

function duplicate() {
    jp.openDialog('编辑计费条款', "${ctx}/bms/basic/bmsBillTerms/duplicate?id=" + getIdSelections(), '100%', '100%', $('#bmsBillTermsTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="bms:bmsBillTerms:edit">
        jp.openDialog('编辑计费条款', "${ctx}/bms/basic/bmsBillTerms/form?id=" + id, '100%', '100%', $('#bmsBillTermsTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="bms:bmsBillTerms:edit">
        jp.openDialogView('查看计费条款', "${ctx}/bms/basic/bmsBillTerms/form?id=" + id, '100%', '100%', $('#bmsBillTermsTable'));
    </shiro:lacksPermission>
}

</script>