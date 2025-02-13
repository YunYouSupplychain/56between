<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initV();
    btnControl();
    initStatisticsTable();
    initDetailTable();
    initAddBillDetailTable();

    setTimeout(refreshStatisticsTable, 500);

    /*页签切换绑定事件*/
    $("#bmsBillTabs a").click(function (e) {
        //阻止a链接的跳转行为
        e.preventDefault();
        // 获取切换目标页签关联的Bootstrap表格ID
        var curTab = $(this).data("target-table");
        //显示当前选中的链接及关联的content
        $(this).tab('show');
        // 刷新加载表格数据
        if (curTab == "#bmsBillStatisticsTable") {
            refreshStatisticsTable();
        } else if (curTab == "#bmsBillDetailTable") {
            /*refreshDetailTable();*/
        }
    });
});

function initV() {
    var isNew = !$('#id').val();
    if (isNew) {
        $('#status').val("10");
        $('#orgId').val(jp.getCurrentOrg().orgId);
    }
}

function btnControl() {
    var isNew = !$('#id').val();
    $('#view').attr("disabled", isNew);
    $('#add').attr("disabled", isNew);
    $('#remove').attr("disabled", isNew);
    $('#settleObjName').prop('readonly', !isNew);
    $('#settleObjNameSBtnId').prop('disabled', !isNew);
    $('#settleObjNameDBtnId').prop('disabled', !isNew);
}

function save() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    var params = $('#inputForm').serialize();
    bq.closeDisabled(objs);
    jp.post("${ctx}/bms/finance/bmsBill/save", params, function (data) {
        if (data.success) {
            window.location = "${ctx}/bms/finance/bmsBill/form?id=" + data.body.entity.id;
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

function initStatisticsTable() {
    $('#bmsBillStatisticsTable').bootstrapTable({
        cache: false,
        pagination: true,
        sidePagination: "server",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.confirmNo = $('#confirmNo').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'warehouseCode',
            title: '仓库编码'
        }, {
            field: 'warehouseName',
            title: '仓库名称'
        }, {
            field: 'contractNo',
            title: '客户合同编号'
        }, {
            field: 'settleObjectCode',
            title: '结算对象代码'
        }, {
            field: 'settleObjectName',
            title: '结算对象名称',
            sortable: true
        }, {
            field: 'billModule',
            title: '费用模块',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_MODULE'))}, value, "-");
            }
        }, {
            field: 'billSubjectName',
            title: '费用科目名称',
            sortable: true
        }, {
            field: 'receivablePayable',
            title: '应收应付',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_RECEIVABLE_PAYABLE'))}, value, "-");
            }
        }, {
            field: 'occurrenceQty',
            title: '发生量',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billQty',
            title: '计费量',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billStandard',
            title: '计费标准值',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'cost',
            title: '费用',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'sysContractNo',
            title: '系统合同编号',
            sortable: true
        }, {
            field: 'settleMethod',
            title: '结算方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_TYPE'))}, value, "-");
            }
        }, {
            field: 'settleCategory',
            title: '结算类别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'billSubjectCode',
            title: '费用科目代码',
            sortable: true
        }, {
            field: 'billCategory',
            title: '费用类别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_SUBJECT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'billTermsCode',
            title: '计费条款代码',
            sortable: true
        }, {
            field: 'billTermsDesc',
            title: '计费条款说明',
            sortable: true
        }, {
            field: 'subcontractNo',
            title: '子合同编号',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
}

function refreshStatisticsTable() {
    if (!$('#id').val()) {
        return;
    }
    $('#bmsBillStatisticsTable').bootstrapTable('refresh', {url: '${ctx}/bms/finance/bmsBillStatistics/data'});
}

function initDetailTable() {
    var $table = $('#bmsBillDetailTable');
    $table.bootstrapTable({
        cache: false,
        pagination: true,
        sidePagination: "server",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.confirmNo = $('#confirmNo').val();
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'dateRange',
            title: '日期范围',
            sortable: true
        }, {
            field: 'warehouseCode',
            title: '仓库编码',
            sortable: true
        }, {
            field: 'warehouseName',
            title: '仓库名称',
            sortable: true
        }, {
            field: 'settleModelCode',
            title: '模型编码',
            sortable: true
        }, {
            field: 'sysContractNo',
            title: '系统合同编号',
            sortable: true
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
            field: 'billModule',
            title: '费用模块',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_MODULE'))}, value, "-");
            }
        }, {
            field: 'billSubjectName',
            title: '费用科目',
            sortable: true
        }, {
            field: 'billCategory',
            title: '费用类别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_SUBJECT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'billTermsDesc',
            title: '计费条款说明',
            sortable: true
        }, {
            field: 'receivablePayable',
            title: '应收应付',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_RECEIVABLE_PAYABLE'))}, value, "-");
            }
        }, {
            field: 'occurrenceQty',
            title: '发生量',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billQty',
            title: '计费量',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billStandard',
            title: '计费标准',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'cost',
            title: '费用',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'formulaName',
            title: '公式',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注',
            sortable: true
        }, {
            field: 'businessDate',
            title: '业务日期',
            sortable: true
        }, {
            field: 'sysOrderNo',
            title: '系统订单号',
            sortable: true
        }, {
            field: 'businessType',
            title: '订单类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_ORDER_TYPE'))}, value, "-");
            }
        }, {
            field: 'route',
            title: '起点 / 终点',
            sortable: true
        }, {
            field: 'carType',
            title: '车型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_CAR_TYPE'))}, value, "-");
            }
        }, {
            field: 'carNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'driver',
            title: '司机',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        $('#remove').attr('disabled', !$('#bmsBillDetailTable').bootstrapTable('getSelections').length);
    });
}

function refreshDetailTable() {
    if (!$('#id').val()) {
        return;
    }
    $('#bmsBillDetailTable').bootstrapTable('refresh', {url: '${ctx}/bms/finance/bmsBillDetail/data'});
}

function viewBillDetail() {
    refreshDetailTable();
}

function addBillDetail() {
    $('#addBillDetailModal').modal();
    $('#add_orgId').val($('#orgId').val());
    $('#add_settleObjectCode').val($('#settleObjCode').val());
    $('#add_billDateFm').datetimepicker({format: "YYYY-MM-DD 00:00:00"});
    $('#add_billDateTo').datetimepicker({format: "YYYY-MM-DD 23:59:59"});
    $('#addBillDetailTable').bootstrapTable('removeAll');
}

function addBillDetailConfirm() {
    var rows = $('#addBillDetailTable').bootstrapTable('getSelections');
    if (rows.length <= 0) {
        jp.warning("请选择记录");
        return;
    }
    jp.loading();
    var ids = $.map(rows, function (row) {
        return row.id;
    }).join(',');
    var confirmNo = $('#confirmNo').val();
    var orgId = $('#orgId').val();
    jp.post("${ctx}/bms/finance/bmsBill/addBillDetail", {
        confirmNo: confirmNo,
        orgId: orgId,
        ids: ids
    }, function (data) {
        if (data.success) {
            refreshDetailTable();
            $('#addBillDetailModal').modal('hide');
            $('#amount').val(data.body.entity.amount);
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function initAddBillDetailTable() {
    var $table = $('#addBillDetailTable');
    $table.bootstrapTable({
        cache: false,
        pagination: true,
        sidePagination: "server",
        queryParams: function (params) {
            var searchParam = $('#searchForm').serializeJSON();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'billSubjectName',
            title: '费用科目名称',
            sortable: true
        }, {
            field: 'warehouseCode',
            title: '仓库编码'
        }, {
            field: 'warehouseName',
            title: '仓库名称'
        }, {
            field: 'settleModelCode',
            title: '结算模型编码'
        }, {
            field: 'contractNo',
            title: '客户合同编号'
        }, {
            field: 'settleObjectCode',
            title: '结算对象代码'
        }, {
            field: 'settleObjectName',
            title: '结算对象名称'
        }, {
            field: 'billModule',
            title: '费用模块',
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_MODULE'))}, value, "-");
            }
        }, {
            field: 'occurrenceQty',
            title: '发生量',
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billStandard',
            title: '计费标准',
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billQty',
            title: '计费量',
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'cost',
            title: '费用',
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "900", "color": "red"}};
            }
        }, {
            field: 'billNo',
            title: '费用单号',
            sortable: true
        }, {
            field: 'settleMethod',
            title: '结算方式',
            formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_TYPE'))}, value, "-");
            }
        }, {
            field: 'settleCategory',
            title: '结算类别',
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_SETTLEMENT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'receivablePayable',
            title: '应收应付',
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_RECEIVABLE_PAYABLE'))}, value, "-");
            }
        }, {
            field: 'sysContractNo',
            title: '系统合同编号'
        }, {
            field: 'subcontractNo',
            title: '子合同编号'
        }, {
            field: 'billCategory',
            title: '费用类别',
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_SUBJECT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'billSubjectCode',
            title: '费用科目代码'
        }, {
            field: 'billTermsCode',
            title: '计费条款代码'
        }, {
            field: 'billTermsDesc',
            title: '计费条款说明'
        }, {
            field: 'formula',
            title: '公式'
        }, {
            field: 'businessType',
            title: '业务类型',
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('business_type_check'))}, value, "-");
            }
        }, {
            field: 'businessDate',
            title: '业务日期'
        }, {
            field: 'sysOrderNo',
            title: '系统订单号'
        }, {
            field: 'createDate',
            title: '创建时间'
        }, {
            field: 'updateDate',
            title: '修改时间'
        }, {
            field: 'remarks',
            title: '备注'
        }]
    });
    $("#search").click("click", function () {// 绑定查询按扭
        $('#addBillDetailTable').bootstrapTable('refresh', {url: '${ctx}/bms/finance/bmsBillDetail/data'});
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        var orgId = $('#orgId').val();
        var settleObjCode = $('#settleObjCode').val();
        $("#add_orgId").val(orgId);
        $('#add_status').val("01");
        $('#add_settleObjectCode').val(settleObjCode);
    });
}

function removeBillDetail() {
    jp.loading();
    var confirmNo = $('#confirmNo').val();
    var orgId = $('#orgId').val();
    var ids = $.map($('#bmsBillDetailTable').bootstrapTable('getSelections'), function (row) {
        return row.id;
    }).join(',');
    jp.post("${ctx}/bms/finance/bmsBill/removeBillDetail", {
        confirmNo: confirmNo,
        orgId: orgId,
        ids: ids
    }, function (data) {
        refreshDetailTable();
        if (data.success) {
            $('#amount').val(data.body.entity.amount);
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

</script>