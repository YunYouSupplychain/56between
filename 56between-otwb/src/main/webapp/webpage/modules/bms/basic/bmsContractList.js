<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#effectiveDateFm').datetimepicker({format: "YYYY-MM-DD"});
    $('#effectiveDateTo').datetimepicker({format: "YYYY-MM-DD"});
    $('#orgId').val(jp.getCurrentOrg().orgId);
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#bmsContractTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
        $('#bmsContractTable').bootstrapTable('refresh');
    });
});

function initTable() {
    $('#bmsContractTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsContract/data",
        //查询参数,每次调用是会带上这个参数，可自定义
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
            field: 'sysContractNo',
            title: '系统合同编号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'contractNo',
            title: '客户合同编号',
            sortable: true
        }, {
            field: 'settleObjectCode',
            title: '结算对象代码',
            sortable: true
        }, {
            field: 'settleObjectName',
            title: '结算对象名称',
            sortable: true
        }, {
            field: 'contractStatus',
            title: '合同状态',
            sortable: true,
            formatter: function (value, row, index) {
                var text = jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_CONTRACT_STATUS'))}, value, "-");
                if (value === "00") {
                    text = '<font color="#1890FF">' + text + '</font>';
                } else if (value === "10") {
                    if (row.effectiveDateTo < jp.dateFormat(new Date().addTime("Day", 7), "yyyy-MM-dd")
                        && row.effectiveDateTo > jp.dateFormat(new Date(), "yyyy-MM-dd")) {
                        text = '<font color="#f1c232">' + text + '</font>';
                    } else {
                        text = '<font color="#3ecc00">' + text + '</font>';
                    }
                } else if (value === "90") {
                    text = '<font color="#ff0000">' + text + '</font>';
                }
                return text;
            }
        }, {
            field: 'effectiveDateFm',
            title: '有效开始日期',
            sortable: true
        }, {
            field: 'effectiveDateTo',
            title: '有效结束日期',
            sortable: true
        }, {
            field: 'subcontractNo',
            title: '子合同编号',
            sortable: true
        }, {
            field: 'remarks',
            title: '合同概述',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsContractTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#skuPrice').prop('disabled', length !== 1);
        $('#copy').prop('disabled', length !== 1);
        $('#valid').prop('disabled', length !== 1);
        $('#invalid').prop('disabled', length !== 1)
        $("#calc").prop('disabled', !length);
    });
}

function getIdSelections() {
    return $.map($("#bmsContractTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该合同记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/basic/bmsContract/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsContractTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function add() {
    jp.openBQDialog('新增合同', "${ctx}/bms/basic/bmsContract/form", '90%', '90%', $('#bmsContractTable'));
}

function edit(id) {
    if (id === undefined) {
        id = getIdSelections();
    }
    jp.openBQDialog('编辑合同', "${ctx}/bms/basic/bmsContract/form?id=" + id, '90%', '90%', $('#bmsContractTable'));
}

function valid() {
    jp.loading();
    jp.post("${ctx}/bms/basic/bmsContract/valid", {ids: getIdSelections().join(',')}, function (data) {
        $('#bmsContractTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

function invalid() {
    jp.loading();
    jp.post("${ctx}/bms/basic/bmsContract/invalid", {ids: getIdSelections().join(',')}, function (data) {
        $('#bmsContractTable').bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

function skuPrice() {
    jp.openDialog('商品价格', "${ctx}/bms/basic/bmsContractSkuPrice/skuPriceForm?contractId=" + getIdSelections(), '90%', '90%', $('#bmsContractTable'));
}

function copy() {
    $("#copyModal").modal();
    $("#copyForm input").val('');
    $("#copyForm select").val('');
    $("#cp_orgId").val(jp.getCurrentOrg().orgId);
    $("#cp_orgName").val(jp.getCurrentOrg().orgName);
}

function copySave() {
    var validate = bq.headerSubmitCheck("#copyForm");
    if (!validate.isSuccess) {
        jp.alert(validate.msg);
        return;
    }
    jp.loading();
    var orgId = $("#cp_orgId").val();
    var settleObjectCode = $("#cp_settleObjectCode").val();
    jp.get("${ctx}/bms/basic/bmsContract/copy?id=" + getIdSelections() + "&settleObjectCode=" + settleObjectCode + "&orgId=" + orgId, function (data) {
        if (data.success) {
            $('#bmsContractTable').bootstrapTable('refresh');
            jp.success(data.msg);
            $("#copyModal").modal('hide');
        } else {
            jp.bqError(data.msg);
        }
    });
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
        jp.get("${ctx}/bms/basic/bmsContract/calc?ids=" + getIdSelections() + "&fmDate=" + fmDate + "&toDate=" + toDate, function (data) {
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
            url: "${ctx}/bms/basic/bmsContract/batchCalc",
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


</script>