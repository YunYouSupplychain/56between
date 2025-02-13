<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#sysCommonCustomerTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#sysCommonCustomerTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#sysCommonCustomerTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/common/customer/data",
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
            field: 'code',
            title: '客户编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'name',
            title: '客户名称',
            sortable: true
        }, {
            field: 'type',
            title: '类型',
            sortable: true,
            formatter: function (value, row, index) {
                var valueArray = value.split(",");
                var labelArray = [];
                for (var i = 0; i < valueArray.length; i++) {
                    labelArray[i] = jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_COMMON_CUSTOMER_TYPE'))}, valueArray[i], "-");
                }
                return labelArray.join(",");
            }
        }, {
            field: 'categories',
            title: '大类',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_MAJOR_CLASS'))}, value, value);
            }
        }, {
            field: 'industryType',
            title: '行业类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_INDUSTRY_TYPE'))}, value, value);
            }
        }, {
            field: 'scope',
            title: '范围',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_RANGE_TYPE'))}, value, value);
            }
        }, {
            field: 'dataSetName',
            title: '数据套',
            sortable: true
        }, {
            field: 'shortName',
            title: '客户简称',
            sortable: true
        }, {
            field: 'foreignName',
            title: '客户外语名称'
        }, {
            field: 'areaName',
            title: '所属城市'
        }, {
            field: 'address',
            title: '地址'
        }, {
            field: 'province',
            title: '省'
        }, {
            field: 'city',
            title: '市'
        }, {
            field: 'area',
            title: '区'
        }, {
            field: 'mail',
            title: '邮箱'
        }, {
            field: 'contacts',
            title: '联系人'
        }, {
            field: 'tel',
            title: '电话'
        }, {
            field: 'fax',
            title: '传真'
        }, {
            field: 'zipCode',
            title: '邮编'
        }]
    });
    $('#sysCommonCustomerTable').on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#sysCommonCustomerTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#syncSelect').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#sysCommonCustomerTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该客户信息记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/sys/common/customer/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#sysCommonCustomerTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增客户信息', "${ctx}/sys/common/customer/form", '90%', '90%', $('#sysCommonCustomerTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialog('编辑客户信息', "${ctx}/sys/common/customer/form?id=" + id, '90%', '90%', $('#sysCommonCustomerTable'));
}

function syncSelect() {
    var rowIds = getIdSelections();
    if (rowIds.length < 1) {
        jp.warning("请勾选需要同步的记录");
        return;
    }
    jp.loading("同步中...");
    jp.post("${ctx}/sys/common/customer/syncSelect", {"ids": rowIds.join(',')}, function (data) {
        jp.alert(data.msg);
    });
}

function syncAll() {
    jp.confirm("确认同步全部数据？", function () {
        jp.loading("同步中...");
        jp.get("${ctx}/sys/common/customer/syncAll", function (data) {
            jp.alert(data.msg);
        });
    });
}
</script>