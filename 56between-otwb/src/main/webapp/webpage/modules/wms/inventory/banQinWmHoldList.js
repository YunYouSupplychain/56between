<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
var currentRow; // 明细当前行
var isShowTab = false; // 是否显示右边tab页
var disabledObj = [];
$(document).ready(function () {
    // 初始化table
    initTable();
    // 隐藏右边tab
    hideTabRight();
    // 监听事件
    listener();
});

/**
 * 监听失去焦点事件
 */
function listener() {
    $('.required').on('blur', function () {
        if ($(this).hasClass('form-error') && $(this).val()) {
            $(this).removeClass('form-error');
        }
    });
}

function initTable() {
    $('#banQinWmHoldTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/wms/inventory/banQinWmHold/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            clickDetail(row);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail(row);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'holdId',
            title: '冻结ID',
            sortable: true
        }, {
            field: 'holdType',
            title: '冻结方式',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_HOLD_TYPE'))}, value, "-");
            }
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
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
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'locCode',
            title: '库位编码',
            sortable: true
        }, {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'reasonCode',
            title: '冻结原因',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_HOLD_REASON'))}, value, "-");
            }
        }, {
            field: 'reason',
            title: '原因描述',
            sortable: true
        }, {
            field: 'isAllowMv',
            title: '是否可移动',
            sortable: true
        }, {
            field: 'isAllowAd',
            title: '是否可调整',
            sortable: true
        }, {
            field: 'isAllowTf',
            title: '是否可转移',
            sortable: true
        }, {
            field: 'holdOp',
            title: '冻结人',
            sortable: true
        }, {
            field: 'holdTime',
            title: '冻结时间',
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
            title: '修改人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '修改时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '仓库',
            sortable: true
        }]
    });
    $('#banQinWmHoldTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinWmHoldTable').bootstrapTable('getSelections').length;
        $('#thaw').prop('disabled', !length);
        $('#viewDetail').prop('disabled', length !== 1);
    });

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinWmHoldTable').bootstrapTable('refresh');
    });

    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        $('#banQinWmHoldTable').bootstrapTable('refresh');
    });
}

function getIdSelections() {
    return $.map($("#banQinWmHoldTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getSelections() {
    return $.map($("#banQinWmHoldTable").bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function getOrgIdSelections() {
    return $.map($("#banQinWmHoldTable").bootstrapTable('getSelections'), function (row) {
        return row.orgId
    });
}

/**
 * 修改表单中disable状态
 */
function openDisable(obj) {
    $(obj + " :disabled").each(function () {
        if ($(this).val()) {
            $(this).prop("disabled", false);
            disabledObj.push($(this));
        }
    });
    return true;
}

/**
 * 打开disabled状态
 */
function closeDisable() {
    for (var i = 0; i < disabledObj.length; i++) {
        $(disabledObj[i]).prop("disabled", true);
    }
    disabledObj = [];
    return true;
}

/**
 * 显示右边tab
 */
function showTabRight() {
    $('#tab-right').show();
    $('#tab-left').addClass("div-left");
    isShowTab = true;
}

/**
 * 隐藏右边tab
 */
function hideTabRight() {
    $('#tab-right').hide();
    $('#tab-left').removeClass("div-left");
    isShowTab = false;
}

/**
 * 单击表格事件
 * @param row 当前行
 */
function clickDetail(row) {
    currentRow = row;
    if (isShowTab) {
        // 表单赋值
        evaluate();
    }
}

/**
 * 双击表格事件
 * @param row 当前行
 */
function dbClickDetail(row) {
    currentRow = row;
    if (!isShowTab) {
        // 显示右边tab
        showTabRight();
        // 表单赋值
        evaluate();
    } else {
        // 隐藏右边tab
        hideTabRight();
    }
}

/**
 * 锁住按钮
 * @param flag
 */
function lock(flag) {
    $('#frost').prop('disabled', flag);
    $('#thaw').prop('disabled', flag);
    $('#viewDetail').prop('disabled', flag);
}

/**
 * 赋值
 */
function evaluate() {
    $("input[id^='detail']").each(function() {
        $(this).prop('readonly', true);
        var $taskId = $(this).attr('id');
        var $taskName = $(this).attr('name');
        $('#' + $taskId).val(eval("currentRow." + $taskName));
    });

    $("button[id^='detail']").each(function() {
        $(this).prop('disabled', true);
    });

    $("select[id^='detail']").each(function() {
        $(this).prop('disabled', true);
        var $taskId = $(this).attr('id');
        var $taskName = $(this).attr('name');
        $('#' + $taskId).val(eval("currentRow." + $taskName));
    });
}

/**
 * 冻结方式下拉框改变事件
 * @param obj
 */
function selectChange() {
    var value = $('#detail_holdType').val();
    if (value) {
        openDisabled();
        removeAllStyleStart();
        initLabel();
        clearMainInput();
        if ('1' === value) {
            $('#ownerLabel').append(getStartStyle());
            $('#detail_ownerName').addClass('required').prop('readonly', false);
            $('#detail_ownerSelectId').prop('disabled', false);
            $('#detail_ownerDeleteId').prop('disabled', false);
        } else if ('2' === value) {
            $('#ownerLabel').append(getStartStyle());
            $('#skuLabel').append(getStartStyle());
            $('#detail_ownerName').addClass('required').prop('readonly', false);
            $('#detail_skuName').addClass('required').prop('readonly', false);
            $('#detail_ownerSelectId').prop('disabled', false);
            $('#detail_ownerDeleteId').prop('disabled', false);
            $('#detail_skuSelectId').prop('disabled', false);
            $('#detail_skuDeleteId').prop('disabled', false);
        } else if ('3' === value) {
            $('#lotLabel').append(getStartStyle());
            $('#detail_lotNum').prop('readonly', false).addClass('required');
        } else if ('4' === value) {
            $('#locLabel').append(getStartStyle());
            $('#detail_locCode').addClass('required').prop('readonly', false);
            $('#detail_locSelectId').prop('disabled', false);
            $('#detail_locDeleteId').prop('disabled', false);
        } else if ('5' === value) {
            $('#traceLabel').append(getStartStyle());
            $('#detail_traceId').prop('readonly', false).addClass('required');
        }

        $('#detail_reasonCode').prop('disabled', false).addClass('required');
        $('#detail_reason').prop('readonly', false).addClass('required');
        $('#detail_isAllowMv').prop('disabled', false);
        $('#detail_isAllowAd').prop('disabled', false);
        $('#detail_isAllowTf').prop('disabled', false);
    }
}

function clearMainInput() {
    $('#detail_ownerCode').val('');
    $('#detail_ownerName').val('');
    $('#detail_skuCode').val('');
    $('#detail_skuName').val('');
    $('#detail_lotNum').val('');
    $('#detail_locCode').val('');
    $('#detail_traceId').val('');
}


/**
 * 初始化label
 */
function initLabel() {
    $('#detail_holdType').addClass('required');
}

/**
 * *样式
 * @returns {string}
 */
function getStartStyle() {
    return '<font class="myStart" color="red">*</font>';
}

/**
 * 移除所有*样式
 */
function removeAllStyleStart() {
    $('.myStart').remove();
}

/**
 * 其他控件不可用
 */
function openDisabled() {
    $("input[id^='detail']").each(function() {
        $(this).prop('readonly', true).removeClass("required");
    });
    $("button[id^='detail']").each(function() {
        $(this).prop('disabled', true);
    });
}

/**
 * checkbox单击事件
 */
function checkChange(obj) {
    $(obj).val(obj.checked ? 'Y' : 'N');
}

/**
 * 新增
 */
function add() {
    // 显示右边tab
    showTabRight();
    // 清空表单
    $(':input', '#inputForm').val('');
    // 冻结方式控件可用
    $('#detail_holdType').prop('disabled', false);
    // 冻结按钮可用
    $('#frost').prop('disabled', false);
    // 初始化按钮
    $('#detail_ownerSelectId').prop('disabled', true);
    $('#detail_ownerDeleteId').prop('disabled', true);
    $('#detail_ownerName').prop('readonly', true);
    $('#detail_skuSelectId').prop('disabled', true);
    $('#detail_skuDeleteId').prop('disabled', true);
    $('#detail_skuName').prop('readonly', true);
    $('#detail_locSelectId').prop('disabled', true);
    $('#detail_locDeleteId').prop('disabled', true);
    $('#detail_locCode').prop('readonly', true);
    $('#detail_isAllowMv').val('N');
    $('#detail_isAllowAd').val('N');
    $('#detail_isAllowTf').val('N');
    $('#detail_recVer').val(0);
}

/**
 * 明细表单验证
 * @returns {boolean}
 */
function detailSubmitCheck(obj) {
    var flag = true;
    $(obj).find(".required").each(function() {
        if (!$(this).val()) {
            $(this).addClass('form-error');
            $(this).attr("placeholder", "不能为空");
            flag = false;
        }
    });
    return flag;
}

/**
 * 冻结
 */
function frost() {
    if (!isShowTab) {
        jp.warning("当前无冻结记录");
        return;
    }
    // 表单验证
    if (detailSubmitCheck('#tab-right')) {
        // 保存前赋值
        beforeSave();
        openDisable('#inputForm');
        jp.loading("正在冻结中...");
        jp.post("${ctx}/wms/inventory/banQinWmHold/frost", $('#inputForm').bq_serialize(), function (data) {
            if (data.success) {
                $('#banQinWmHoldTable').bootstrapTable('refresh');
                hideTabRight();
                jp.success(data.msg);
            } else {
                closeDisable();
                jp.bqError(data.msg);
            }
        })
    }
}

/**
 * 保存前赋值
 */
function beforeSave() {
    if (!$('#detail_id').val()) {
        $('#detail_orgId').val(jp.getCurrentOrg().orgId);
        $('#detail_recVer').val(0);
    }
}

/**
 * 解冻
 */
function thaw() {
    if (!jp.isExistDifOrg(getOrgIdSelections())) {
        jp.warning("不同平台数据不能批量操作");
        return;
    }
    if (getOrgIdSelections()[0] !== jp.getCurrentOrg().orgId) {
        jp.warning("数据和所选平台不一致，不能操作");
        return;
    }
    var rowIds = getIdSelections();
    if (rowIds.length === 0) {
        jp.warning("请选择记录");
        return;
    }
    jp.loading("正在解冻中...");
    jp.get("${ctx}/wms/inventory/banQinWmHold/thaw?ids=" + rowIds, function (data) {
        if (data.success) {
            $('#banQinWmHoldTable').bootstrapTable('refresh');
            hideTabRight();
            jp.success(data.msg);
        } else {
            closeDisable();
            jp.bqError(data.msg);
        }
    })
}

/**
 * 查看库存冻结明细
 */
function viewDetail() {
    var rows = getSelections();
    if (rows.length !== 1) {
        jp.warning("请选择一条记录");
        return;
    }
    var params = "ownerCode=" + rows[0].ownerCode + "&skuCode=" + rows[0].skuCode;
    params += "&lotNum=" + rows[0].lotNum + "&locCode=" + rows[0].locCode + "&traceId=" + rows[0].traceId;
    jp.openTab("${ctx}/wms/inventory/banQinWmActHold/list?" + params, "库存冻结明细查询", false);
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/inventory/banQinWmHold/export", "库存冻结记录", $("#searchForm"), function () {
        jp.close();
    });
}

</script>