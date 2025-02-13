<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysBmsSubjectTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#sysBmsSubjectTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#sysBmsSubjectTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/bms/subject/data",
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
            field: 'billSubjectCode',
            title: '费用科目代码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'billSubjectName',
            title: '费用科目名称',
            sortable: true
        }, {
            field: 'billModule',
            title: '费用模块',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_MODULE'))}, value, "-");
            }
        }, {
            field: 'billCategory',
            title: '费用类别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_SUBJECT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'dataSetName',
            title: '数据套',
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
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    });
    $('#sysBmsSubjectTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysBmsSubjectTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#sysBmsSubjectTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该费用科目记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/bms/subject/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysBmsSubjectTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function add() {
    jp.openDialog('新增费用科目', "${ctx}/sys/common/bms/subject/form", '100%', '100%', $('#sysBmsSubjectTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="sys:common:bms:subject:edit">
        jp.openDialog('编辑费用科目', "${ctx}/sys/common/bms/subject/form?id=" + id, '100%', '100%', $('#sysBmsSubjectTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="sys:common:bms:subject:edit">
        jp.openDialogView('查看费用科目', "${ctx}/sys/common/bms/subject/form?id=" + id, '100%', '100%', $('#sysBmsSubjectTable'));
    </shiro:lacksPermission>
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/bms/subject/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/bms/subject/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}

</script>