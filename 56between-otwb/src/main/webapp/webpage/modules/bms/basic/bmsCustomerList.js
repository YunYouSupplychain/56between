<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#bmsCustomerTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#bmsCustomerTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#bmsCustomerTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsCustomer/data",
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
            field: 'ebcuCustomerNo',
            title: '客户代码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'ebcuNameCn',
            title: '中文名称',
            sortable: true
        }, {
            field: 'ebcuShortName',
            title: '简称',
            sortable: true
        }, {
            field: 'ebcuNameEn',
            title: '英文名称',
            sortable: true
        }, {
            field: 'ebcuType',
            title: '客户类型',
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
            field: 'ebcuIndustryType',
            title: '行业类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_INDUSTRY_TYPE'))}, value, "-");
            }
        }, {
            field: 'ebcuTel',
            title: '电话',
            sortable: true
        }, {
            field: 'ebcuFax',
            title: '传真',
            sortable: true
        }, {
            field: 'ebcuUrl',
            title: '网址',
            sortable: true
        }, {
            field: 'ebcuAddress',
            title: '地址',
            sortable: true
        }, {
            field: 'ebcuFinanceCode',
            title: '财务代码',
            sortable: true
        }, {
            field: 'ebcuTaxRegistNo',
            title: '平台税务登记号',
            sortable: true
        }, {
            field: 'ebcuBusinessNo',
            title: '工商登记号',
            sortable: true
        }, {
            field: 'ebcuMainBusiness',
            title: '主营业务',
            sortable: true
        }, {
            field: 'orgId',
            title: '机构',
            sortable: true
        }, {
            field: 'createBy.id',
            title: '创建人',
            sortable: true
        }, {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }, {
            field: 'updateBy.id',
            title: '更新人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '更新时间',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsCustomerTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsCustomerTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该客户记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/bmsCustomer/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsCustomerTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增客户', "${ctx}/bms/basic/bmsCustomer/form", '80%', '70%', $('#bmsCustomerTable'));
}

function edit(id) {//没有权限时，不显示确定按钮
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basic:bmsCustomer:edit">
        jp.openDialog('编辑客户', "${ctx}/bms/basic/bmsCustomer/form?id=" + id, '80%', '70%', $('#bmsCustomerTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basic:bmsCustomer:edit">
        jp.openDialogView('查看客户', "${ctx}/bms/basic/bmsCustomer/form?id=" + id, '80%', '70%', $('#bmsCustomerTable'));
    </shiro:lacksPermission>
}

</script>