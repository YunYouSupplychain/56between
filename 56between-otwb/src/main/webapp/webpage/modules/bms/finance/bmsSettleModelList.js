<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#bmsSettleModelTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#bmsSettleModelTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#bmsSettleModelTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/finance/bmsSettleModel/data",
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
            field: 'settleModelCode',
            title: '模型编码',
            sortable: true
            , formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'settleObjectCode',
            title: '结算对象编码',
            sortable: true
        }, {
            field: 'settleObjectName',
            title: '结算对象名称',
            sortable: true
        }, {
            field: 'responsiblePerson',
            title: '负责人',
            sortable: true
        }, {
            field: 'createDate',
            title: '创建时间',
            sortable: true
        }, {
            field: 'createBy.name',
            title: '创建人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '更新时间',
            sortable: true
        }, {
            field: 'updateBy.name',
            title: '更新人',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注信息',
            sortable: true
        }]
    });
    $('#bmsSettleModelTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsSettleModelTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $("#calc").prop('disabled', !length);
        $("#copy").prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#bmsSettleModelTable").bootstrapTable('getSelections'), function (row) {
        return row.id;
    });
}

function deleteAll() {
    jp.confirm('确认要删除该结算模型记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/finance/bmsSettleModel/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsSettleModelTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        })
    })
}

function add() {
    jp.openBQDialog('新增结算模型', "${ctx}/bms/finance/bmsSettleModel/form", '90%', '90%', $('#bmsSettleModelTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openBQDialog('编辑结算模型', "${ctx}/bms/finance/bmsSettleModel/form?id=" + id, '90%', '90%', $('#bmsSettleModelTable'));
}

function calc() {
    $("#calcModal").modal();
    $("#calculate").off('click').on('click', function () {
        var validate = bq.headerSubmitCheck("#calcForm");
        if(!validate.isSuccess){
            jp.alert(validate.msg);
            return;
        }
        jp.loading("  正在计算，请稍等...");
        var fmDate = $("input[name='fmDate']").val();
        var toDate = $("input[name='toDate']").val();
        jp.get("${ctx}/bms/finance/bmsSettleModel/calc?ids=" + getIdSelections() + "&fmDate=" + fmDate + "&toDate=" + toDate, function (data) {
            if (data.success) {
                jp.success("计算完成");
                $("#calcModal").modal('hide');
            } else {
                jp.bqError(data.msg);
            }
        });
    });
    $("#fmDate").datetimepicker({format: "YYYY-MM-DD"});
    $("#toDate").datetimepicker({format: "YYYY-MM-DD"});
}

function batchCalc() {
    $("#calcModal").modal();
    $("#calculate").off('click').on('click', function () {
        var validate = bq.headerSubmitCheck("#calcForm");
        if(!validate.isSuccess){
            jp.alert(validate.msg);
            return;
        }
        jp.loading("  正在计算，请稍等...");
        var searchParam = $("#searchForm").serializeJSON();
        searchParam.orgId = jp.getCurrentOrg().orgId;
        searchParam.fmDate = $("input[name='fmDate']").val();
        searchParam.toDate = $("input[name='toDate']").val();
        $.ajax({
            url: "${ctx}/bms/finance/bmsSettleModel/batchCalc",
            method: "POST",
            dataType: "JSON",
            data: JSON.stringify(searchParam),
            contentType: "application/json;charset=UTF-8",
            error: function (xhr, textStatus) {
                jp.error("操作异常");
            },
            success: function (data, textStatus, jqXHR) {
                if (data.success) {
                    jp.success("计算完成");
                    $("#calcModal").modal('hide');
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    });
    $("#fmDate").datetimepicker({format: "YYYY-MM-DD"});
    $("#toDate").datetimepicker({format: "YYYY-MM-DD"});
}

function copy() {
    jp.loading();
    var row = $("#bmsSettleModelTable").bootstrapTable('getSelections')[0];
    jp.get("${ctx}/bms/finance/bmsSettleModel/copy?settleModelCode=" + row.settleModelCode + "&orgId=" + row.orgId, function (data) {
        if (data.success) {
            $('#bmsSettleModelTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

</script>