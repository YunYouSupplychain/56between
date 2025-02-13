<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#businessDateFm').datetimepicker({format: "YYYY-MM-DD"});
    $('#businessDateTo').datetimepicker({format: "YYYY-MM-DD"});
    $("#businessDateFm input").val(jp.dateFormat(new Date().addTime("Month", -1), "yyyy-MM-dd"));
    $("#businessDateTo input").val(jp.dateFormat(new Date(), "yyyy-MM-dd"));
    $("#orgId").val(jp.getCurrentOrg().orgId);
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        searchForm();
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm  input").val("");
        $("#searchForm  select").val("");
        $("#searchForm  .select-item").html("");
    });
    $("#btnImport").click(function () {
        bq.importExcel("${ctx}/bms/finance/bmsBillDetail/import");
    });
});

function initTable() {
    $('#bmsBillDetailTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
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
            field: 'billNo',
            title: '费用单号',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true
            , formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_STATUS'))}, value, "-");
            }
        }, {
            field: 'confirmNo',
            title: '账单号',
            sortable: true
        }, {
            field: 'dateRange',
            title: '结算日期范围',
            sortable: true
        }, {
            field: 'businessDate',
            title: '业务日期',
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
            field: 'formulaParamsDesc',
            title: '公式参数值描述',
            sortable: true
        }, {
            field: 'remarks',
            title: '备注',
            sortable: true
        }, {
            field: 'sysOrderNo',
            title: '系统订单号',
            sortable: true
        }, {
            field: 'customerOrderNo',
            title: '客户订单号',
            sortable: true
        }, {
            field: 'consigneeCode',
            title: '收货方编码',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货方名称',
            sortable: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
            sortable: true
        }, {
            field: 'supplierCode',
            title: '供应商编码',
            sortable: true
        }, {
            field: 'supplierName',
            title: '供应商名称',
            sortable: true
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'carrierCode',
            title: '承运商编码',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商名称',
            sortable: true
        }, {
            field: 'carType',
            title: '车型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_CAR_TYPE'))}, value, "-");
            }
        }, {
            field: 'carNo',
            title: '车牌号',
            sortable: true
        }, {
            field: 'driver',
            title: '司机',
            sortable: true
        }, {
            field: 'route',
            title: '起点 / 终点',
            sortable: true
        }, {
            field: 'settleModelCode',
            title: '模型编码',
            sortable: true
        }, {
            field: 'billTermsCode',
            title: '计费条款代码',
            sortable: true
        }, {
            field: 'billTermsDesc',
            title: '计费条款说明',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
    $('#bmsBillDetailTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#bmsBillDetailTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#copy').prop('disabled', length !== 1);
    });
}

function searchForm() {
    var validate = bq.headerSubmitCheck("#searchForm");
    if (!validate.isSuccess) {
        jp.alert(validate.msg);
        return;
    }
    $('#bmsBillDetailTable').bootstrapTable('refresh', {url: "${ctx}/bms/finance/bmsBillDetail/data"});
    getTotal();
}

function getTotal() {
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    jp.post("${ctx}/bms/finance/bmsBillDetail/getTotal", searchParam, function (data) {
        if (data.success) {
            $('#footer').show();
            $('#occurrenceQty').html(data.body.sumOccurrenceQty);
            $('#billQty').html(data.body.sumBillQty);
            $('#cost').html(data.body.sumCost);
        }
    });
}

function getIdSelections() {
    return $.map($("#bmsBillDetailTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该费用明细记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/bms/finance/bmsBillDetail/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#bmsBillDetailTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function add() {
    jp.openDialog('手工费用', "${ctx}/bms/finance/bmsBillDetail/form", '800px', '600px', $('#bmsBillDetailTable'));
}

function genBill() {
    jp.confirm("是否确认按列表条件生成账单？", function () {
        jp.loading();
        var searchParam = $("#searchForm").serializeJSON();
        searchParam.orgId = jp.getCurrentOrg().orgId;
        jp.post("${ctx}/bms/finance/bmsBillDetail/genBill", searchParam, function (data) {
            if (data.success) {
                $('#bmsBillDetailTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
}

function removeByBillNo() {
    jp.open({
        type: 1,
        area: [500, 300],
        title: "按费用单号删除",
        content: $("#removeBox").html(),
        btn: ['确定', '关闭'],
        btn1: function (index, layero) {
            var searchParam = layero.find("#removeForm").serializeJSON();
            if (!searchParam.billNo) {
                jp.bqError("费用单号不能为空");
                return;
            }
            jp.loading('  正在删除，请稍等...');
            jp.get("${ctx}/bms/finance/bmsBillDetail/deleteByBillNo?billNo=" + searchParam.billNo + "&orgId=" + jp.getCurrentOrg().orgId, function (data) {
                if (data.success) {
                    searchForm();
                    jp.success("删除成功");
                    jp.close(index);
                } else {
                    jp.bqError(data.msg);
                }
            });
        },
        btn2: function (index) {
            jp.close(index);
        },
        success: function (layero, index) {
            layero.find("#parentId").val(jp.getCurrentOrg().orgId);
        }
    });
}

function exportExcel() {
    var validate = bq.headerSubmitCheck("#searchForm");
    if (!validate.isSuccess) {
        jp.alert(validate.msg);
        return;
    }
    var searchParam = $("#searchForm").serializeJSON();
    searchParam.orgId = jp.getCurrentOrg().orgId;
    bq.exportExcelNew("${ctx}/bms/finance/bmsBillDetail/export", "费用明细", searchParam)
}

</script>