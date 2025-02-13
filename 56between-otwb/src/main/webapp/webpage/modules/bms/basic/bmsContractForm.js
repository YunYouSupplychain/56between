<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $('#effectiveDateFm').datetimepicker({format: "YYYY-MM-DD"});
    $('#effectiveDateTo').datetimepicker({format: "YYYY-MM-DD"});
    $('#checkAccountsTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initV();
});

function initV() {
    if (!$("#id").val()) {
        $("#orgId").val(jp.getCurrentOrg().orgId);
        $("#orgName").val(jp.getCurrentOrg().orgName);
        $("#contractStatus").val('00');
    } else {// 修改合同
        $("#settleObjectCode").prop("readonly", true);
        $("#settleObjectCodeSBtnId").prop("disabled", true);
        $("#settleObjectCodeDBtnId").prop("disabled", true);
        $("#addRow").prop("disabled", false);
    }
    buildCostItem();
}

function save() {
    jp.loading();
    var validate = bq.headerSubmitCheck("#inputForm");
    if (!validate.isSuccess) {
        jp.bqError(validate.msg);
        return;
    }
    var disabledObjs = bq.openDisabled('#inputForm');
    var params = $('#inputForm').serialize();
    bq.closeDisabled(disabledObjs);
    jp.post("${ctx}/bms/basic/bmsContract/save", params, function (data) {
        if (data.success) {
            window.location = "${ctx}/bms/basic/bmsContract/form?id=" + data.body.id;
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/**
 * 结算对象选择后触发事件
 * @param row 选中的结算对象
 */
function settleObjectAfterSelect(row) {
    if (row) {
        $('#settleObjectName').val(row.settleObjectName);
    }
}

function buildCostItem() {
    var $table = $('#bmsContractCostItemTable');
    $table.bootstrapTable({
        cache: false,//是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        //这个接口需要处理bootstrap table传递的固定参数,并返回特定格式的json数据
        url: "${ctx}/bms/basic/bmsContract/costItemData",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = $("#orgId").val();
            searchParam.sysContractNo = $("#sysContractNo").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            $(".info").removeClass("info");
            $el.addClass("info");
            $table.bootstrapTable('uncheckAll');
            $table.bootstrapTable("checkBy", {field: 'id', values: [row.id]});
        },
        columns: [{
            checkbox: true
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
            field: 'formulaName',
            title: '公式',
            sortable: true
        }, {
            field: 'maxAmount',
            title: '最大金额',
            sortable: true
        }, {
            field: 'minAmount',
            title: '最小金额',
            sortable: true
        }, {
            field: 'taxRate',
            title: '税率',
            sortable: true
        }, {
            field: 'coefficient',
            title: '合同系数',
            sortable: true
        }, {
            field: 'billSubjectCode',
            title: '费用科目代码',
            sortable: true
        }, {
            field: 'billTermsCode',
            title: '计费条款代码',
            sortable: true
        }]
    });
    $table.on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#editRow').prop('disabled', length !== 1);
        $('#delRow').prop('disabled', !length);
        if (length === 1) {
            var row = $table.bootstrapTable('getSelections')[0];
            if (row.billModule == "01") {// 仓储
                $('#storagePrice').prop('disabled', false);
                $('#transportPrice').prop('disabled', true);
            } else if (row.billModule == "02") {// 运输
                $('#storagePrice').prop('disabled', true);
                $('#transportPrice').prop('disabled', false);
            }
        } else {
            $('#storagePrice').prop('disabled', true);
            $('#transportPrice').prop('disabled', true);
        }
    });
}

function getCostItemIdSelections() {
    return $.map($("#bmsContractCostItemTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getCostItemSelections() {
    return $("#bmsContractCostItemTable").bootstrapTable('getSelections');
}

function addRow() {
    var url = "${ctx}/bms/basic/bmsContract/costItemForm?sysContractNo=" + $("#sysContractNo").val() + "&settleObjectCode=" + $('#settleObjectCode').val() + "&orgId=" + $("#orgId").val();
    jp.openDialog("新增费用项目", url, "80%", "80%", $("#bmsContractCostItemTable"));
}

function editRow(id) {
    if (id === undefined) {
        id = getCostItemIdSelections();
    }
    var url = "${ctx}/bms/basic/bmsContract/costItemForm?id=" + id + "&sysContractNo=" + $("#sysContractNo").val() + "&orgId=" + $("#orgId").val();
    jp.openDialog("编辑费用项目", url, "80%", "80%", $("#bmsContractCostItemTable"));
}

function delRow(ids) {
    jp.confirm('确认要删除吗？', function () {
        jp.loading();
        if (ids === undefined) {
            ids = getCostItemIdSelections();
        }
        jp.post("${ctx}/bms/basic/bmsContract/deleteCostItem?ids=" + ids, {}, function (data) {
            if (data.success) {
                $("#bmsContractCostItemTable").bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        });
    });
}

function storagePriceForm() {
    var row = getCostItemSelections()[0];
    var url = "${ctx}/bms/basic/bmsContract/storagePrice/list?costItemId=" + row.id;
    jp.openDialogView("仓储价格&nbsp;→&nbsp;" + row.billSubjectName + "&nbsp;&nbsp;&nbsp;" + row.billTermsDesc, url, "80%", "80%", $("#bmsContractCostItemTable"));
}

function transportPriceForm() {
    var row = getCostItemSelections()[0];
    var url = "${ctx}/bms/basic/bmsContract/transportPriceList?costItemId=" + row.id;
    jp.openDialog("运输价格&nbsp;→&nbsp;" + row.billSubjectName + "&nbsp;&nbsp;&nbsp;" + row.billTermsDesc, url, "80%", "80%", $("#bmsContractCostItemTable"));
}
</script>