<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#beginEffectiveTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endEffectiveTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#beginExpirationTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#endExpirationTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#omItemPriceTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#omItemPriceTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#omItemPriceTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/oms/basic/omItemPrice/data",
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
            field: 'skuName',
            title: '商品',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:view(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'customerName',
            title: '往来户',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主',
            sortable: true
        }, {
            field: 'priceType',
            title: '价格类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_PRICE_TYPE'))}, value, "-");
            }
        }, {
            field: 'channel',
            title: '渠道价格',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_CHANNEL'))}, value, "-");
            }
        }, {
            field: 'auditStatus',
            title: '审核状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_AUDIT_STATUS'))}, value, "-");
            }
        }, {
            field: 'effectiveTime',
            title: '生效时间',
            sortable: true
        }, {
            field: 'expirationTime',
            title: '失效时间',
            sortable: true
        }, {
            field: 'isEnable',
            title: '是否启用',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'unit',
            title: '基本单位(不含税)',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_ITEM_UNIT'))}, value, "-");
            }
        }, {
            field: 'auxiliaryUnit',
            title: '辅助单位',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('OMS_UNIT'))}, value, "-");
            }
        }, {
            field: 'discount',
            title: '折扣(比例)',
            sortable: true
        }, {
            field: 'taxPrice',
            title: '含税单价',
            sortable: true
        }, {
            field: 'isAllowAdjustment',
            title: '是否允许调整',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'purchaseMultiple',
            title: '采购倍数',
            sortable: true
        }, {
            field: 'saleMultiple',
            title: '销售倍数',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#omItemPriceTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#audit').prop('disabled', !length);
        $('#cancelAudit').prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#omItemPriceTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该商品价格记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/oms/basic/omItemPrice/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#omItemPriceTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openDialog('新增商品价格', "${ctx}/oms/basic/omItemPrice/form", '80%', '80%', $('#omItemPriceTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    <shiro:hasPermission name="basic:omItemPrice:edit">
        jp.openDialog('编辑商品价格', "${ctx}/oms/basic/omItemPrice/form?id=" + id, '80%', '80%', $('#omItemPriceTable'));
    </shiro:hasPermission>
    <shiro:lacksPermission name="basic:omItemPrice:view">
        view(id);
    </shiro:lacksPermission>
}

function view(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openDialogView('查看商品价格', "${ctx}/oms/basic/omItemPrice/form?id=" + id, '80%', '80%', $('#omItemPriceTable'));
}

function audit() {
    jp.get("${ctx}/oms/basic/omItemPrice/audit?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omItemPriceTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

function cancelAudit() {
    jp.get("${ctx}/oms/basic/omItemPrice/cancelAudit?ids=" + getIdSelections(), function (data) {
        if (data.success) {
            $('#omItemPriceTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

</script>