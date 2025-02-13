<%@ page contentType="text/html;charset=UTF-8" %>
<script type = "text/javascript">
var kitParentDetail_currentRow; // 父件明细当前行
var kitSubDetail_currentRow; // 子件明细当前行
var kitTask_currentRow; // 加工任务当前行
var kitDetail_currentRow; // 加工明细当前行
var paTask_currentRow; // 上架任务明细当前行
var kitParentDetail_isShowTab = false; // 是否显示右边父件明细tab页
var kitSubDetail_isShowTab = false; // 是否显示右边子件明细tab页
var kitTask_isShowTab = false; // 是否显示右边加工任务tab页
var kitDetail_isShowTab = false; // 是否显示右边加工明细tab页
var paTask_isShowTab = false; // 是否显示右边上架任务tab页
var currentLoginName = '${fns:getUser().loginName}';
$(document).ready(function () {
    // 初始化父件明细tab
    initKitParentDetailTable();
    // 初始化子件明细tab
    initKitSubDetailTable();
    // 初始化加工任务tab
    initKitTaskTable();
    // 初始化加工明细tab
    initKitDetailTable();
    // 初始化
    init();
    // 激活默认Tab页
    activeTab();
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

/**
 * 激活默认Tab页
 */
function activeTab() {
    var detailTabIndex = sessionStorage.getItem("KIT_" + currentLoginName +"_detailTab");
    detailTabIndex = !detailTabIndex ? 0 : detailTabIndex;
    // 默认选择加载Tab页
    $("#detailTab li:eq(" + detailTabIndex + ") a").tab('show');
}

/**
 * 显示右边tab
 * @param obj1 右边div Id
 * @param obj2 左边div Id
 */
function showTabRight(obj1, obj2) {
    $(obj1).show();
    $(obj2).addClass("div-left");
}

/**
 * 隐藏右边tab
 * @param obj1 右边div Id
 * @param obj2 左边div Id
 */
function hideTabRight(obj1, obj2) {
    $(obj1).hide();
    $(obj2).removeClass("div-left");
}

/**
 * 单击表格事件
 * @param tab tab页名称
 * @param row 当前行
 */
function clickDetail(tab, row) {
    switch (tab) {
        case "kitParentDetail":
            kitParentDetailClick(row);
            break;
        case "kitSubDetail":
            kitSubDetailClick(row);
            break;
        case "kitTask":
            kitTaskClick(row);
            break;
        case "kitDetail":
            kitDetailClick(row);
            break;
        case "paTask":
            paTaskClick(row);
            break;
    }
}

/**
 * 双击表格事件
 * @param tab tab页名称
 * @param row 当前行
 */
function dbClickDetail(tab, row) {
    switch (tab) {
        case "kitParentDetail":
            kitParentDetailDbClick(row);
            break;
        case "kitSubDetail":
            kitSubDetailDbClick(row);
            break;
        case "kitTask":
            kitTaskDbClick(row);
            break;
        case "kitDetail":
            kitDetailDbClick(row);
            break;
        case "paTask":
            paTaskDbClick(row);
            break;
    }
}

/**
 * 赋值
 */
function evaluate(prefix, currentRow) {
    $("input[id^=" + prefix + "]").each(function() {
        var $Id = $(this).attr('id');
        var $Name = $(this).attr('name');
        $('#' + $Id).val(eval("" + currentRow + "." + $Name));
    });
    $("select[id^=" + prefix + "]").each(function() {
        var $Id = $(this).attr('id');
        var $Name = $(this).attr('name');
        $('#' + $Id).val(eval("" + currentRow + "." + $Name));
    });
}

/**
 * 获取表格勾选行Ids
 * @param obj 表格Id
 */
function getSelections(obj) {
    return $.map($(obj).bootstrapTable('getSelections'), function (row) {
        return row
    });
}

/**
 * 获取表格勾选行Ids
 * @param obj 表格Id
 */
function getIdSelections(obj) {
    return $.map($(obj).bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

/*********************加工单开始*********************************/
function saveKitHeader() {
    jp.loading();
    var validate = bq.headerSubmitCheck("#inputForm");
    if (validate.isSuccess) {
        var disabledObjs = bq.openDisabled("#inputForm");
        var params = $('#inputForm').serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/wms/kit/banQinWmKitHeader/save", params, function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/kit/banQinWmKitHeader/form?id=" + data.body.entity.id;
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

function auditHandler() {
    commonMethod('audit');
}

function cancelAuditHandler() {
    commonMethod('cancelAudit');
}

function closeHandler() {
    commonMethod('closeOrder');
}

function cancelHeader() {
    commonMethod('cancelOrder');
}

/**
 * 公共调用方法
 * @param method 方法名
 */
function commonMethod(method) {
    jp.loading();
    jp.get("${ctx}/wms/kit/banQinWmKitHeader/" + method + "?ids=" + $('#id').val(), function (data) {
        if (data.success) {
            window.location = "${ctx}/wms/kit/banQinWmKitHeader/form?id=" + $('#id').val();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 初始化label
 */
function init() {
    // 初始化表头label
    initLabel();
    // 初始化按钮
    buttonControl();
}

/**
 * 初始化表头label
 */
function initLabel() {
    if (!$('#id').val()) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
        $("#kitLoc").val("WORKBENCH");
    } else {
        $('#kitType').prop('disabled', true);
        $('#ownerSelectId').prop('disabled', true);
        $('#ownerDeleteId').prop('disabled', true);
        $('#ownerCode').prop('disabled', true);
        $('#ownerName').prop('disabled', true);
    }
    // 公共信息
    $('#fmEtk').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#toEtk').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $('#auditTime').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
}
/*********************加工单结束*********************************/

/***********************按钮控制*************************************/
function buttonControl() {
    buttonAllByHeader(true);
    buttonAllByParent(true);
    buttonAllBySub(true);
    buttonAllByTaskKit(true);
    buttonAllByResult(true);
    buttonAllByPa(true);
    //加工单header
    buttonControlByHeader();
    //父件明细
    buttonControlByParent();
    //子件明细
    buttonControlBySub();
    //加工任务
    buttonControlByTask();
    //父件加工结果明细
    buttonControlByResult();
    //上架任务
    buttonControlByPa();
    if ($("#id").val()) {
        //订单关闭||订单取消
        if ($("#status").val() === "90" || $("#status").val() === "99") {
            buttonAllByHeader(false);
            $("#btn_duplicate").attr("disabled", false);
            buttonAllByParent(false);
            buttonAllBySub(false);
            buttonAllByTaskKit(false);
            buttonAllByResult(false);
            buttonAllByPa(false);
        }
    }
}

function buttonAllByHeader(flag) {
    $("#btn_save").attr("disabled", !flag);
    $("#btn_audit").attr("disabled", !flag);
    $("#btn_cancelAudit").attr("disabled", !flag);
    $("#btn_close").attr("disabled", !flag);
    $("#btn_cancel").attr("disabled", !flag);
}

function buttonAllByParent(flag) {
    $("#btn_parent_add").attr("disabled", !flag);
    $("#btn_parent_save").attr("disabled", !flag);
    $("#btn_parent_remove").attr("disabled", !flag);
    $("#btn_parent_duplicate").attr("disabled", !flag);
    $("#btn_parent_generateSub").attr("disabled", !flag);
    $("#btn_parent_cancelGenerateSub").attr("disabled", !flag);
    $("#btn_parent_cancel").attr("disabled", !flag);
}

function buttonAllBySub(flag) {
    $("#btn_sub_save").attr("disabled", !flag);
    $("#btn_sub_alloc").attr("disabled", !flag);
    $("#btn_sub_manualAlloc").attr("disabled", !flag);
    $("#btn_sub_picking").attr("disabled", !flag);
    $("#btn_sub_cancelAlloc").attr("disabled", !flag);
    $("#btn_sub_cancelPicking").attr("disabled", !flag);
}

function buttonAllByTaskKit(flag) {
    $("#btn_task_add").attr("disabled", !flag);
    $("#btn_task_save").attr("disabled", !flag);
    $("#btn_task_picking").attr("disabled", !flag);
    $("#btn_task_cancelAlloc").attr("disabled", !flag);
    $("#btn_task_cancel").attr("disabled", !flag);
}

function buttonAllByResult(flag) {
    $("#btn_result_kitConfirm").attr("disabled", !flag);
    $("#btn_result_cancelKit").attr("disabled", !flag);
    $("#btn_result_createTaskPa").attr("disabled", !flag);
}

function buttonAllByPa(flag) {
    $("#btn_pa_removeTaskPa").attr("disabled", !flag);
    $("#btn_pa_putawayConfirm").attr("disabled", !flag);
}

function buttonControlByHeader() {
    if (!$('#id').val()) {
        buttonAllByHeader(false);
        $("#btn_save").attr("disabled", false);
        return;
    }
    //状态控制
    //审核状态控制，未审核，都可执行
    if ($('#auditStatus').val() !== "00") {
        //审核，不可执行
        buttonAllByHeader(false);
        //审核，可执行
        $("#btn_duplicate").attr("disabled", false);
        //订单新增状态
        if ($('#status').val() === "00") {
            //不审核状态
            if ($('#auditStatus').val() === "90") {
                //如果不审核 并且未生成子件，那么保存、删除
                // if (this.subPagingGrid.store.length == 0) {
                //     $("#btn_save").attr("disabled", false);
                //     $("#btn_remove").attr("disabled", false);
                //     $("#btn_cancel").attr("disabled", false);
                // }
                $("#btn_cancelAudit").attr("disabled", true);
            } else {
                $("#btn_cancelAudit").attr("disabled", false);
            }
        } else if ($('#status').val() === "70") {
            //部分加工
        } else if ($('#status').val() === "80") {
            //完全加工
            $("#btn_close").attr("disabled", false);
        }
    }
}

function buttonControlByParent() {
    //加工单 单行操作
    if (!$("#id").val()) {
        buttonAllByParent(false);
        return;
    }
    // 审核状态控制，未审核
    if ($('#auditStatus').val() === "00") {
        //未审核，不可执行
        $("#btn_parent_generateSub").attr("disabled", true);
        $("#btn_parent_cancelGenerateSub").attr("disabled", true);
    }
    //父件明细批量操作
    if (!kitParentDetail_isShowTab) {
        //列表没有记录
        // if (!$("#kitParentDetailTable").bootstrapTable('getData').length) {
        //     buttonAllByParent(false);
        //     $("#btn_parent_add").attr("disabled", false);
        //     return;
        // }
        $("#btn_parent_save").attr("disabled", true);
        if ($('#auditStatus').val() !== "00") {
            // 已审核/不审核,订单状态控制
            //不可执行
            buttonAllByParent(false);
            //已审核，可执行
            if ($('#status').val() === "00") {
                //创建状态
                //不审核状态
                if ($('#auditStatus').val() === "90") {
                    //如果不审核，并且未生成子件
                    $("#btn_parent_add").attr("disabled", false);
                    $("#btn_parent_remove").attr("disabled", false);
                    $("#btn_parent_duplicate").attr("disabled", false);
                }
                $("#btn_parent_generateSub").attr("disabled", false);
                $("#btn_parent_cancelGenerateSub").attr("disabled", false);
                $("#btn_parent_cancel").attr("disabled", false);
            } else if ($('#status').val() === "70") {
                //部分加工
                $("#btn_parent_generateSub").attr("disabled", false);
                $("#btn_parent_cancelGenerateSub").attr("disabled", false);
                $("#btn_parent_cancel").attr("disabled", false);
            }
            //完全加工全部功能按扭不可用
        }
    } else {
        // 单记录操作
        if (!$('#kitParentDetail_id').val()) {
            buttonAllByParent(false);
            $("#btn_parent_add").attr("disabled", false);
            $("#btn_parent_save").attr("disabled", false);
            return;
        }
        // 己审核
        if ($('#auditStatus').val() !== "00") {
            //不可执行
            buttonAllByParent(false);
            //已审核，可执行
            if ($('#status').val() === "00") {
                //创建状态
                //不审核状态
                if ($('#auditStatus').val() === "90") {
                    //如果不审核，
                    $("#btn_parent_add").attr("disabled", false);
                    $("#btn_parent_save").attr("disabled", false);
                    $("#btn_parent_remove").attr("disabled", false);
                    $("#btn_parent_duplicate").attr("disabled", false);
                }
                //是否已生成子件
                if ($('#isCreateSub').val() === "Y") {
                    $("#btn_parent_save").attr("disabled", true);
                    $("#btn_parent_remove").attr("disabled", true);
                    $("#btn_parent_cancelGenerateSub").attr("disabled", false);
                    $("#btn_parent_cancel").attr("disabled", true);
                } else {
                    $("#btn_parent_generateSub").attr("disabled", false);
                    $("#btn_parent_cancel").attr("disabled", false);
                }
            }
            //部分加工、完全加工、关闭订单、取消订单全部功能按扭不可用
        }
    }
}

function buttonControlBySub() {
    //加工单 单行操作
    if (!$('#id').val()) {
        buttonAllBySub(false);
        return;
    }
    //加工单未审核
    if ($('#auditStatus').val() === "00") {
        buttonAllBySub(false);
        return;
    }
    //加工单已审核
    // if ($("#kitSubDetailTable").bootstrapTable('getData').length === 0) {
    //     //子件列表无记录
    //     buttonAllBySub(false);
    //     return;
    // }
    // 子件批量操作
    if (!kitSubDetail_isShowTab) {
        $("#btn_sub_save").attr("disabled", true);
        //加工单状态
        if ($('#status').val() === "00") {
            //创建
            buttonAllBySub(true);
            $("#btn_sub_save").attr("disabled", true);
        } else if ($('#status').val() === "70") {
            //部分加工
            buttonAllBySub(true);
            $("#btn_sub_save").attr("disabled", true);
        } else if ($('#status').val() === "80") {
            //完全加工
            buttonAllBySub(false);
        } else if ($('#status').val() === "90") {
            //取消订单
            buttonAllBySub(false);
        } else if ($('#status').val() === "99") {
            //关闭订单
            buttonAllBySub(false);
        }
    } else {
        // 子件单行操作，子件状态
        buttonAllBySub(false);
        if (kitSubDetail_currentRow.subSkuType === "ACC") {
            //子件为辅料时，不可修改
            buttonAllBySub(false);
            return;
        }
        if (kitSubDetail_currentRow.status === "00") {
            //创建状态
            $("#btn_sub_save").attr("disabled", false);
            $("#btn_sub_alloc").attr("disabled", false);
            $("#btn_sub_manualAlloc").attr("disabled", false);
        } else if (kitSubDetail_currentRow.status === "30") {
            //部分分配
            $("#btn_sub_alloc").attr("disabled", false);
            $("#btn_sub_manualAlloc").attr("disabled", false);
            $("#btn_sub_cancelAlloc").attr("disabled", false);
            $("#btn_sub_picking").attr("disabled", false);
        } else if (kitSubDetail_currentRow.status === "40") {
            //完全分配
            $("#btn_sub_cancelAlloc").attr("disabled", false);
            $("#btn_sub_picking").attr("disabled", false);
        } else if (kitSubDetail_currentRow.status === "50") {
            //部分拣货
            //完全分配+部分拣货，部分分配+部分完全拣货，部分分配+部分拣货
            if (kitSubDetail_currentRow.qtyPlanEa > kitSubDetail_currentRow.qtyPkEa + kitSubDetail_currentRow.qtyAllocEa) {
                //计划数>拣货数+分配数
                $("#btn_sub_alloc").attr("disabled", false);
                $("#btn_sub_manualAlloc").attr("disabled", false);
            }
            if (kitSubDetail_currentRow.qtyAllocEa > 0) {
                //分配数>0
                $("#btn_sub_cancelAlloc").attr("disabled", false);
            }
            $("#btn_sub_picking").attr("disabled", false);
            $("#btn_sub_cancelPicking").attr("disabled", false);
        } else if (kitSubDetail_currentRow.status === "60") {
            //完全拣货
            $("#btn_sub_cancelPicking").attr("disabled", false);
        } else if (kitSubDetail_currentRow.status === "70") {
            //部分加工
            //完全分配+完全拣货+部分加工，完全分配+部分拣货+部分完全加工，部分分配+部分完全拣货+部分完全加工，
            //部分分配+部分完全拣货+部分部分加工，部分分配+部分拣货+部分加工
            if (kitSubDetail_currentRow.qtyPlanEa > kitSubDetail_currentRow.qtyKitEa + kitSubDetail_currentRow.qtyPkEa + kitSubDetail_currentRow.qtyAllocEa) {
                //计划数>加工数+拣货数+分配数
                $("#btn_sub_alloc").attr("disabled", false);
                $("#btn_sub_manualAlloc").attr("disabled", false);
            }
            if (kitSubDetail_currentRow.qtyAllocEa > 0) {
                //分配数>0
                $("#btn_sub_cancelAlloc").attr("disabled", false);
                $("#btn_sub_picking").attr("disabled", false);
            }
            if (kitSubDetail_currentRow.qtyPkEa > 0) {
                //拣货数>0
                $("#btn_sub_cancelPicking").attr("disabled", false);
            }
        } else if (kitSubDetail_currentRow.status === "80") {
            //完全加工，全部按扭不可用
        }
    }

}

function buttonControlByTask() {
    //加工单 单行操作
    if (!$('#id').val()) {
        buttonAllByTaskKit(false);
        return;
    }
    //加工单未审核
    if ($('#auditStatus').val() === "00") {
        buttonAllByTaskKit(false);
        return;
    }
    // 加工任务批量操作
    if (!kitTask_isShowTab) {
        //加工单状态
        if ($('#status').val() === "00") {
            //创建
            buttonAllByTaskKit(true);
            $("#btn_task_save").attr("disabled", true);
        } else if ($('#status').val() === "70") {
            //部分加工
            buttonAllByTaskKit(true);
            $("#btn_task_save").attr("disabled", true);
        } else if ($('#status').val() === "80") {
            //完全加工
            buttonAllByTaskKit(false);
        } else if ($('#status').val() === "90") {
            //取消订单
            buttonAllByTaskKit(false);
        } else if ($('#status').val() === "99") {
            //关闭订单
            buttonAllByTaskKit(false);
        }
    } else {
        // 加工任务单行操作，加工任务状态
        buttonAllByTaskKit(false);
        if ($('#kitTask_status').val() === "40") {
            //完全分配
            $("#btn_task_save").attr("disabled", false);
            $("#btn_task_cancelAlloc").attr("disabled", false);
            $("#btn_task_picking").attr("disabled", false);
        } else if ($('#kitTask_status').val() === "60") {
            //完全拣货
            $("#btn_task_cancel").attr("disabled", false);
        } else if ($('#kitTask_status').val() === "80") {
            //完全加工，全部按扭不可用
        }
    }
}

function buttonControlByResult() {
    //加工单 单行操作
    if (!$('#id').val()) {
        buttonAllByResult(false);
        return;
    }
    //加工单未审核
    if ($('#auditStatus').val() === "00") {
        buttonAllByResult(false);
        return;
    }
    //加工单已审核
    if ($('#status').val() === "90") {
        //取消订单
        buttonAllByResult(false);
        $("#btn_step_saveStep").attr("disabled", true);
        return;
    } else if ($('#status').val() === "99") {
        //关闭订单
        buttonAllByResult(false);
        $("#btn_step_saveStep").attr("disabled", true);
        return;
    }
    // 加工结果批量操作
    if (!kitDetail_isShowTab) {
        //加工单状态
        if ($('#status').val() === "00") {
            //创建
            buttonAllByResult(false);
            $("#btn_result_kitConfirm").attr("disabled", false);
        } else if ($('#status').val() === "70") {
            //部分加工
            buttonAllByResult(true);
        } else if ($('#status').val() === "80") {
            //完全加工
            buttonAllByResult(true);
            $("#btn_result_kitConfirm").attr("disabled", true);
        }
    } else {
        //单行操作
        if (kitDetail_currentRow.status === "00") {
            //创建
            buttonAllByResult(false);
            $("#btn_step_saveStep").attr("disabled", true);
            $("#btn_result_kitConfirm").attr("disabled", false);
        } else if (kitDetail_currentRow.status === "80") {
            //完全加工
            buttonAllByResult(true);
            $("#btn_step_saveStep").attr("disabled", false);
            $("#btn_result_kitConfirm").attr("disabled", true);
        }
    }
}

function buttonControlByPa() {
    if ($("#paTaskTable").bootstrapTable('getData').length === 0) {
        //列表无记录
        buttonAllByPa(false);
        return;
    }
    //加工单 单行操作
    if (!$('#id').val()) {
        buttonAllByPa(false);
        return;
    }
    //加工单未审核
    if ($('#auditStatus').val() === "00") {
        buttonAllByPa(false);
        return;
    }
    if (!paTask_isShowTab) {
        //批量操作
        buttonAllByPa(true);
        if ($('#status').val() === "99") {
            //关闭订单
            buttonAllByPa(false);
        }
    } else {
        if ($('#paTask_id').val()) {
            if (paTask_currentRow.status === "00") {
                //创建
                buttonAllByPa(true);
            } else if (paTask_currentRow.status === "99") {
                //完成
                buttonAllByPa(false);
            }
        }
    }
}

/**************************************弹出框开始********************************************/
function kitParentSkuAfterSelect(row) {
    loadLotAtt({ownerCode: $('#ownerCode').val(), parentSkuCode: row.parentSkuCode, orgId: row.orgId});
}

/**************************************弹出框结束********************************************/
/**************************************父件明细开始********************************************/
function initKitParentDetailTable() {
    $('#kitParentDetailTable').bootstrapTable({
        cache: false,
        sidePagination: "server",
        url: "${ctx}/wms/kit/banQinWmKitParentDetail/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#orgId').val();
            searchParam.headerId = $('#id').val() ? $('#id').val() : '#';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            clickDetail('kitParentDetail', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('kitParentDetail', row);
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'parentLineNo',
            title: '行号',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_KIT_STATUS'))}, value, "-");
            }
        },
        {
            field: 'parentSkuCode',
            title: '商品编码',
            sortable: true
        },
        {
            field: 'parentSkuName',
            title: '商品名称',
            sortable: true
        },
        {
            field: 'qtyPlanEa',
            title: '预加工数',
            sortable: true
        },
        {
            field: 'qtyKitEa',
            title: '已加工数',
            sortable: true
        },
        {
            field: 'planKitLoc',
            title: '计划加工台',
            sortable: true
        },
        {
            field: 'paRuleName',
            title: '上架规则',
            sortable: true
        },
        {
            field: 'packDesc',
            title: '包装规则',
            sortable: true
        },
        {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        }]
    });
}

/**
 * 父件明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function kitParentDetailClick(row) {
    kitParentDetail_currentRow = row;
    if (kitParentDetail_isShowTab) {
        // 表单赋值
        evaluate('kitParentDetail', 'kitParentDetail_currentRow');
        // 按钮控制
        buttonControl();
    }
}

/**
 * 父件明细行双击事件
 * @param row 当前行
 */
function kitParentDetailDbClick(row) {
    kitParentDetail_currentRow = row;
    if (!kitParentDetail_isShowTab) {
        // 显示右边tab
        showTabRight('#kitParentDetail_tab-right', '#kitParentDetail_tab-left');
        kitParentDetail_isShowTab = true;
        // 加载批次控件
        loadLotAtt(row);
        // 表单赋值
        evaluate('kitParentDetail', 'kitParentDetail_currentRow');
        $('#kitParentDetail_isCreateSub').prop('checked', kitParentDetail_currentRow.isCreateSub === 'Y');
    } else {
        // 隐藏右边tab
        hideTabRight('#kitParentDetail_tab-right', '#kitParentDetail_tab-left');
        kitParentDetail_isShowTab = false;
    }
    $("#kitParentDetailTable").bootstrapTable('resetView');
    buttonControl();
}

/**
 * 加载批次属性控件
 */
function loadLotAtt(row) {
    var params = "ownerCode=" + row.ownerCode + "&skuCode=" + row.parentSkuCode + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            $('#kitParentDetailLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'kitParentDetail_lotAtt');
            $('#kitParentDetailLotAttTab').append(html);
            $('#kitParentDetailLotAttTab .detail-date').each(function() {
                laydate.render({
                    elem: '#' + $(this).prop('id'),
                    theme: '#393D49'
                });
            });
        }
    })
}

/**
 * 新增明细
 */
function addParentHandler() {
    // 显示右边Tab
    showTabRight('#kitParentDetail_tab-right', '#kitParentDetail_tab-left');
    kitParentDetail_isShowTab = true;
    kitParentDetail_currentRow = null;
    // 清空表单
    $(':input', '#kitParentDetailForm').val('');
    // 初始化
    initAddParentDetail();
    // 按钮控制
    buttonControl();
}

/**
 * 初始化明细
 */
function initAddParentDetail() {
    $('#kitParentDetail_status').prop('disabled', true);
    $("#kitParentDetail_status option:first").prop("selected", true);
    $('#kitParentDetail_isCreateSub').prop('disabled', true).val('N');
    $("#kitParentDetail_qtyPlanEa").val(0);
    $("#kitParentDetail_qtyPlanUom").val(0);
    $("#kitParentDetail_qtyKitEa").val(0);
    $("#kitParentDetail_qtyKitUom").val(0);
    $("#kitParentDetail_planKitLoc").val($("#kitLoc").val());
}

/**
 * 预加工数输入同步到EA
 */
function kitParentDetailQtyChange() {
    var uomQty = !$("#kitParentDetail_uomQty").val() ? 0 : $("#kitParentDetail_uomQty").val();
    var qtyPlanUom = !$("#kitParentDetail_qtyPlanUom").val() ? 0 : $("#kitParentDetail_qtyPlanUom").val();
    if (uomQty !== 0) {
        $("#kitParentDetail_qtyPlanEa").val(Math.floor(qtyPlanUom * 100) / 100 * uomQty);
    } else {
        $("#kitParentDetail_qtyPlanEa").val('');
    }
}

/**
 * 保存
 */
function saveParentHandler() {
    if (!kitParentDetail_isShowTab) {
        jp.warning("当前无保存记录!");
        return;
    }
    if (!$('#id').val()) {
        jp.warning("请先保存订单头!");
        return;
    }
    if ($('#kitParentDetail_qtyPlanUom').val() <= 0) {
        jp.bqError("计划加工数必须大于0");
        return;
    }
    var validate = bq.detailSubmitCheck("#kitParentDetailForm");
    if (validate.isSuccess) {
        // 保存前赋值
        beforeSave();
        jp.loading();
        var disabledObjs = bq.openDisabled("#kitParentDetailForm");
        var row = {};
        bq.copyProperties(kitParentDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#kitParentDetailForm')));
        bq.closeDisabled(disabledObjs);
        $.ajax({
            url: "${ctx}/wms/kit/banQinWmKitParentDetail/save",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    // tab页刷新
                    tabRefresh();
                    buttonControl();
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

/**
 * tab页刷新
 */
function tabRefresh() {
    // 父件明细tab
    $('#kitParentDetailTable').bootstrapTable('refresh');
    hideTabRight('#kitParentDetail_tab-right', '#kitParentDetail_tab-left');
    kitParentDetail_isShowTab = false;
    // 子件明细tab
    $('#kitSubDetailTable').bootstrapTable('refresh');
    hideTabRight('#kitSubDetail_tab-right', '#kitSubDetail_tab-left');
    kitSubDetail_isShowTab = false;
    // 加工任务tab
    $('#kitTaskTable').bootstrapTable('refresh');
    hideTabRight('#kitTask_tab-right', '#kitTask_tab-left');
    kitTask_isShowTab = false;
    // 加工明细tab
    $('#kitDetailTable').bootstrapTable('refresh');
    hideTabRight('#kitDetail_tab-right', '#kitDetail_tab-left');
    kitDetail_isShowTab = false;
}

/**
 * 保存前赋值
 */
function beforeSave() {
    if (!$('#kitParentDetail_id').val()) {
        $("#kitParentDetail_kitNo").val($("#kitNo").val());
        $("#kitParentDetail_ownerCode").val($("#ownerCode").val());
        $("#kitParentDetail_logisticNo").val($("#logisticNo").val());
        $("#kitParentDetail_orgId").val($("#orgId").val());
        $("#kitParentDetail_headerId").val($("#id").val());
    }
}

/**
 * 删除
 */
function removeParentHandler() {
    var rows = getSelections('#kitParentDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].lineNo);
    }
    jp.confirm("确认要删除吗？删除将不能恢复", function () {
        var params = "kitNo=" + $('#kitNo').val();
        params += "&parentLineNo=" + lineNoArray.join(',');
        params += "&orgId=" + $('#orgId').val();
        jp.loading();
        kitParentDetailCommonMethod('remove', params);
    })
}

/**
 * 复制
 */
function duplicateParentHandler() {
    var rowIds = getIdSelections('#kitParentDetailTable');
    if (rowIds.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    if (rowIds.length > 1) {
        jp.bqError("只能选择一条记录!");
        return;
    }
    jp.loading();
    var params = "id=" + rowIds;
    kitParentDetailCommonMethod('duplicate', params);
}

/**
 * 生成子件
 */
function generateSubHandler() {
    var rows = getSelections('#kitParentDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].parentLineNo);
    }
    var params = "kitNo=" + $('#kitNo').val();
    params += "&parentLineNo=" + lineNoArray.join(',');
    params += "&orgId=" + $('#orgId').val();
    jp.loading();
    kitParentDetailCommonMethod('genSub', params);
}

/**
 * 取消生成子件
 */
function cancelGenerateSubHandler() {
    var rows = getSelections('#kitParentDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].parentLineNo);
    }
    var params = "kitNo=" + $('#kitNo').val();
    params += "&parentLineNo=" + lineNoArray.join(',');
    params += "&orgId=" + $('#orgId').val();
    jp.loading();
    kitParentDetailCommonMethod('cancelGenSub', params);
}

/**
 * 取消订单行
 */
function cancelParentHandler() {
    var rows = getSelections('#kitParentDetailTable');
    if (rows.length === 0) {
        jp.bqError("请选择一条记录!");
        return;
    }
    var lineNoArray = [];
    for (var x in rows) {
        lineNoArray.push(rows[x].parentLineNo);
    }
    var params = "kitNo=" + $('#kitNo').val();
    params += "&parentLineNo=" + lineNoArray.join(',');
    params += "&orgId=" + $('#orgId').val();
    jp.loading();
    kitParentDetailCommonMethod('cancelLine', params);
}

/**
 * 父件明细公共方法
 * @param method
 * @param params
 */
function kitParentDetailCommonMethod(method, params) {
    jp.get("${ctx}/wms/kit/banQinWmKitParentDetail/" + method + "?" + params, function (data) {
        if (data.success) {
            // tab页刷新
            tabRefresh();
            // 按钮控制
            buttonControl();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**
 * 明细Tab页索引切换
 */
function detailTabChange(index) {
    sessionStorage.setItem("KIT_" + currentLoginName + "_detailTab", index);
}

/**************************************子件明细开始********************************************/
function initKitSubDetailTable() {
    $('#kitSubDetailTable').bootstrapTable({
        cache: false,
        sidePagination: "server",
        url: "${ctx}/wms/kit/banQinWmKitSubDetail/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#orgId').val();
            searchParam.headerId = $("#id").val() ? $('#id').val() : '#';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            clickDetail('kitSubDetail', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('kitSubDetail', row);
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'subLineNo',
            title: '行号',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SUB_KIT_STATUS'))}, value, "-");
            }
        },
        {
            field: 'parentLineNo',
            title: '父件明细行号',
            sortable: true
        },
        {
            field: 'subSkuCode',
            title: '子件编码',
            sortable: true
        },
        {
            field: 'subSkuName',
            title: '子件名称',
            sortable: true
        },
        {
            field: 'subSkuType',
            title: '子件类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SUB_SKU_TYPE'))}, value, "-");
            }
        },
        {
            field: 'qtyPlanEa',
            title: '待加工子件数EA',
            sortable: true
        },
        {
            field: 'qtyAllocEa',
            title: '分配数EA',
            sortable: true
        },
        {
            field: 'qtyPkEa',
            title: '拣货数EA',
            sortable: true
        },
        {
            field: 'qtyKitEa',
            title: '已加工数EA',
            sortable: true
        },
        {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        },
        {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        }]
    });
}

/**
 * 子件明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function kitSubDetailClick(row) {
    kitSubDetail_currentRow = row;
    if (kitSubDetail_isShowTab) {
        // 表单赋值
        evaluate('kitSubDetail', 'kitSubDetail_currentRow');
        // 按钮控制
        buttonControl();
    }
}

/**
 * 子件明细行双击事件
 * @param row 当前行
 */
function kitSubDetailDbClick(row) {
    kitSubDetail_currentRow = row;
    if (!kitSubDetail_isShowTab) {
        // 显示右边tab
        showTabRight('#kitSubDetail_tab-right', '#kitSubDetail_tab-left');
        kitSubDetail_isShowTab = true;
        // 批次屬性控件賦值
        kitSubDetailLoadLotAtt(row);
        // 表单赋值
        evaluate('kitSubDetail', 'kitSubDetail_currentRow');
        // 初始化控件状态
    } else {
        // 隐藏右边tab
        hideTabRight('#kitSubDetail_tab-right', '#kitSubDetail_tab-left');
        kitSubDetail_isShowTab = false;
    }
    $("#kitSubDetailTable").bootstrapTable('resetView');
    // 按钮控制
    buttonControl();
}

/**
 * 加载批次属性控件
 */
function kitSubDetailLoadLotAtt(row) {
    var params = "ownerCode=" + row.ownerCode + "&skuCode=" + row.subSkuCode + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            $('#kitSubDetailLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'kitSubDetail_lotAtt');
            html = html.replace(new RegExp("required", "g"), "");
            html = html.split('<font color="red">*</font>').join('');
            $('#kitSubDetailLotAttTab').append(html);
            $('#kitSubDetailLotAttTab .detail-date').each(function() {
                laydate.render({
                    elem: '#' + $(this).prop('id'),
                    theme: '#393D49'
                });
            });
        }
    })
}

/**
 * 保存
 */
function saveSubHandler() {
    if (!kitSubDetail_isShowTab) {
        jp.warning("当前无保存记录!");
        return;
    }
    if (!$('#id').val()) {
        jp.warning("请先保存订单头!");
        return;
    }
    var validate = bq.detailSubmitCheck("#kitSubDetailForm");
    if (validate.isSuccess) {
        jp.loading();
        var disabledObjs = bq.openDisabled("#kitSubDetailForm");
        var row = {};
        bq.copyProperties(kitSubDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#kitSubDetailForm')));
        bq.closeDisabled(disabledObjs);
        $.ajax({
            url: "${ctx}/wms/kit/banQinWmKitSubDetail/save",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    $("#kitSubDetailTable").bootstrapTable('refresh');
                    hideTabRight('#kitSubDetail_tab-right', '#kitSubDetail_tab-left');
                    kitSubDetail_isShowTab = false;
                    buttonControl();
                    jp.success(data.msg);
                } else {
                    jp.error(data.msg);
                }
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

/**
 * 分配
 */
function allocBySubHandler() {
    var rows = getSelections('#kitSubDetailTable');
    if (rows.length === 0) {
        jp.warning("请选择记录");
        return;
    }
    var lineNoArray = [];
    for (var i in rows) {
        lineNoArray.push(rows[i].subLineNo);
    }

    var params = "kitNo=" + $('#kitNo').val();
    params += '&subLineNo=' + lineNoArray.join(',');
    params += '&orgId=' + $('#orgId').val();
    jp.loading();
    kitSubDetailCommonMethod('alloc', params);
}

/**
 * 手工分配
 */
function manualAllocHandler() {
    addTaskHandler();
}

/**
 * 拣货确认
 */
function pickingBySubHandler() {
    var rows = getSelections('#kitSubDetailTable');
    if (rows.length === 0) {
        jp.warning("请选择记录");
        return;
    }
    var lineNoArray = [];
    for (var i in rows) {
        lineNoArray.push(rows[i].subLineNo);
    }

    var params = "kitNo=" + $('#kitNo').val();
    params += '&subLineNo=' + lineNoArray.join(',');
    params += '&orgId=' + $('#orgId').val();
    jp.loading();
    kitSubDetailCommonMethod('picking', params);
}

/**
 * 取消分配
 */
function cancelAllocBySubHandler() {
    var rows = getSelections('#kitSubDetailTable');
    if (rows.length === 0) {
        jp.warning("请选择记录");
        return;
    }
    var lineNoArray = [];
    for (var i in rows) {
        lineNoArray.push(rows[i].subLineNo);
    }

    var params = "kitNo=" + $('#kitNo').val();
    params += '&subLineNo=' + lineNoArray.join(',');
    params += '&orgId=' + $('#orgId').val();
    jp.loading();
    kitSubDetailCommonMethod('cancelAlloc', params);
}

/**
 * 取消拣货
 */
function cancelPickingBySubHandler() {
    var rows = getSelections('#kitSubDetailTable');
    if (rows.length === 0) {
        jp.warning("请选择记录");
        return;
    }
    var lineNoArray = [];
    for (var i in rows) {
        lineNoArray.push(rows[i].subLineNo);
    }

    var params = "kitNo=" + $('#kitNo').val();
    params += '&subLineNo=' + lineNoArray.join(',');
    params += '&orgId=' + $('#orgId').val();
    jp.loading();
    kitSubDetailCommonMethod('cancelPicking', params);
}

/**
 * 子件明细公共方法
 * @param method
 * @param params
 */
function kitSubDetailCommonMethod(method, params) {
    jp.get("${ctx}/wms/kit/banQinWmKitSubDetail/" + method + "?" + params, function (data) {
        if (data.success) {
            // tab页刷新
            tabRefresh();
            // 按钮控制
            buttonControl();
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    })
}

/**************************************子件明细结束********************************************/

/**************************************加工任务开始********************************************/
function initKitTaskTable() {
    $('#kitTaskTable').bootstrapTable({
        cache: false,
        sidePagination: "server",
        url: "${ctx}/wms/kit/banQinWmTaskKit/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#orgId').val();
            searchParam.headerId = $("#id").val() ? $('#id').val() : '#';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            clickDetail('kitTask', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('kitTask', row);
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'kitTaskId',
            title: '加工任务ID',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SUB_KIT_STATUS'))}, value, "-");
            }
        },
        {
            field: 'subLineNo',
            title: '子件行号',
            sortable: true
        },
        {
            field: 'parentLineNo',
            title: '父件行号',
            sortable: true
        },
        {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        },
        {
            field: 'subSkuCode',
            title: '子件编码',
            sortable: true
        },
        {
            field: 'subSkuName',
            title: '子件名称',
            sortable: true
        },
        {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        },
        {
            field: 'locCode',
            title: '库位编码',
            sortable: true
        },
        {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        },
        {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        },
        {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        },
        {
            field: 'qtyUom',
            title: '数量',
            sortable: true
        },
        {
            field: 'qtyEa',
            title: '数量EA',
            sortable: true
        },
        {
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        },
        {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        },
        {
            field: 'pickOp',
            title: '拣货人',
            sortable: true
        },
        {
            field: 'pickTime',
            title: '拣货时间',
            sortable: true
        },
        {
            field: 'kitOp',
            title: '加工人',
            sortable: true
        },
        {
            field: 'kitTime',
            title: '加工时间',
            sortable: true
        },
        {
            field: 'kitLineNo',
            title: '加工行号',
            sortable: true
        }]
    });
}

/**
 * 加工任务行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function kitTaskClick(row) {
    kitTask_currentRow = row;
    if (kitTask_isShowTab) {
        // 表单赋值
        evaluate('kitTask', 'kitTask_currentRow');
        // 按钮控制
        buttonControl();
    }
}

/**
 * 加工任务行双击事件
 * @param row 当前行
 */
function kitTaskDbClick(row) {
    kitTask_currentRow = row;
    if (!kitTask_isShowTab) {
        // 显示右边tab
        showTabRight('#kitTask_tab-right', '#kitTask_tab-left');
        kitTask_isShowTab = true;
        // 表单赋值
        evaluate('kitTask', 'kitTask_currentRow');
        // 初始化控件状态
    } else {
        // 隐藏右边tab
        hideTabRight('#kitTask_tab-right', '#kitTask_tab-left');
        kitTask_isShowTab = false;
    }
    $("#kitTaskTable").bootstrapTable('resetView');
    // 按钮控制
    buttonControl();
}

/**
 * 新增加工任务
 */
function addTaskHandler() {
    var rows = getSelections('#kitSubDetailTable');
    if (rows.length !== 1) {
        jp.bqError("请选择一条子件明细!");
        return;
    }
    if (rows[0].status === '90') {
        jp.bqError("订单行[" + rows[0].soNo + "][" + rows[0].lineNo + "已取消，不能操作");
        return;
    }
    if (rows[0].status === '20' || rows[0].status === '40' || rows[0].status === '60' || rows[0].status === '80') {
        jp.bqError("非创建，非部分预配，非部分分配，非部分拣货，非部分发货状态，不能操作");
        return;
    }
    if ($('#auditStatus').val() === '00') {
        jp.bqError("订单未审核，不能操作");
        return;
    }
    if (rows[0].subSkuType !== "COMMON") {
        jp.bqError("子件类型不是普通，不能操作");
        return;
    }

    // 跳转到加工任务tab页
    $('a[href="#kitTaskInfo"]').tab('show');
    // 显示右边Tab
    showTabRight('#kitTask_tab-right', '#kitTask_tab-left');
    kitTask_isShowTab = true;
    kitTask_currentRow = null;
    // 清空表单
    $(':input', '#kitTaskForm').val('');
    $('#kitTaskForm_save').attr('disabled', false);
    // 初始化
    initAddKitTask(rows[0]);
    // 按钮控制
    buttonControl();
}

/**
 * 初始化新增加工任务
 */
function initAddKitTask(row) {
    $('#kitTask_kitNo').val(row.kitNo);
    $('#kitTask_parentLineNo').val(row.parentLineNo);
    $('#kitTask_subLineNo').val(row.subLineNo);
    $('#kitTask_ownerCode').val(row.ownerCode);
    $('#kitTask_ownerName').val(row.ownerName);
    $('#kitTask_status').val('40');
    $('#kitTask_toLoc').val(row.planKitLoc);
    $('#kitTask_toId').val("*");
    $('#kitTask_skuCodeParam').val(row.subSkuCode);
    $('#kitTask_headerId').val(row.headerId);
    $('#kitTask_orgId').val(row.orgId);
}

/**
 * 保存加工任务
 */
function saveTaskHandler() {
    if (!kitTask_isShowTab) {
        jp.warning("当前无保存记录!");
        return;
    }
    if (!$('#id').val()) {
        jp.warning("请先保存订单头!");
        return;
    }

    var validate = bq.detailSubmitCheck("#kitTaskForm");
    if (validate.isSuccess) {
        jp.loading();
        // 保存前赋值
        var disabledObjs = bq.openDisabled("#kitTaskForm");
        var row = {};
        bq.copyProperties(kitTask_currentRow, row);
        $.extend(row, bq.serializeJson($('#kitTaskForm')));
        bq.closeDisabled(disabledObjs);
        $.ajax({
            url: "${ctx}/wms/kit/banQinWmTaskKit/manualAlloc",
            type: 'POST',
            data: JSON.stringify(row),
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    tabRefresh();
                    buttonControl();
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

/**
 * 拣货确认
 */
function pickingByTaskHandler() {
    var rows = [];
    if (!kitTask_isShowTab) {
        rows = getSelections('#kitTaskTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#kitTask_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        var disabledObjs = bq.openDisabled("#kitTaskForm");
        var row = {};
        bq.copyProperties(kitTask_currentRow, row);
        $.extend(row, bq.serializeJson($('#kitTaskForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading();
    kitTaskCommonMethod('picking', rows);
}

/**
 * 取消分配
 */
function cancelAllocByTaskHandler() {
    var rows = [];
    if (!kitTask_isShowTab) {
        rows = getSelections('#kitTaskTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#kitTask_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        var disabledObjs = bq.openDisabled("#kitTaskForm");
        rows.push(kitTask_currentRow);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading();
    kitTaskCommonMethod('cancelAlloc', rows);
}

/**
 * 取消拣货
 */
function cancelPickingByTaskHandler() {
    if (!kitTask_isShowTab) {
        var rows = getSelections('#kitTaskTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    }
    view = 'kitTask';
    initPaTaskWindow();
}

/**
 * 加工任务公共方法
 * @param method
 * @param params
 */
function kitTaskCommonMethod(method, params) {
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(params),
        url: "${ctx}/wms/kit/banQinWmTaskKit/" + method,
        success: function (data) {
            if (data.success) {
                tabRefresh();
                buttonControl();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 数量输入同步到EA
 */
function kitTaskQtyChange() {
    var uomQty = !$("#kitTask_uomQty").val() ? 0 : $("#kitTask_uomQty").val();
    var qtyUom = !$("#kitTask_qtyUom").val() ? 0 : $("#kitTask_qtyUom").val();
    if (uomQty !== 0) {
        $("#kitTask_qtyEa").val(Math.floor(qtyUom * 100) / 100 * uomQty);
    } else {
        $("#kitTask_qtyEa").val('');
    }
}

/**************************************加工任务开始********************************************/
/**************************************加工明细开始********************************************/
function initKitDetailTable() {
    $('#kitDetailTable').bootstrapTable({
        cache: false,
        sidePagination: "server",
        url: "${ctx}/wms/kit/banQinWmKitResultDetail/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#orgId').val();
            searchParam.headerId = $("#id").val() ? $("#id").val() : '#';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            clickDetail('kitDetail', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('kitDetail', row);
        },
        columns: [{
            checkbox: true
        },
            {
                field: 'kitLineNo',
                title: '行号',
                sortable: true
            },
            {
                field: 'status',
                title: '状态',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_SUB_KIT_STATUS'))}, value, "-");
                }
            },
            {
                field: 'parentLineNo',
                title: '父件明细行号',
                sortable: true
            },
            {
                field: 'parentSkuCode',
                title: '父件编码',
                sortable: true
            },
            {
                field: 'qtyPlanEa',
                title: '预加工数',
                sortable: true
            },
            {
                field: 'qtyCompleteEa',
                title: '已加工数',
                sortable: true
            },
            {
                field: 'kitLoc',
                title: '加工台',
                sortable: true
            },
            {
                field: 'kitTraceId',
                title: '跟踪号',
                sortable: true
            },
            {
                field: 'reserveCode',
                title: '上架库位指定规则',
                sortable: true
            },
            {
                field: 'paRuleName',
                title: '上架规则',
                sortable: true
            },
            {
                field: 'packDesc',
                title: '包装规格',
                sortable: true
            },
            {
                field: 'uomDesc',
                title: '包装单位',
                sortable: true
            },
            {
                field: 'lotNum',
                title: '批次号',
                sortable: true
            },
            {
                field: 'lotAtt01',
                title: '生产日期',
                sortable: true
            },
            {
                field: 'lotAtt02',
                title: '失效日期',
                sortable: true
            },
            {
                field: 'lotAtt03',
                title: '入库日期',
                sortable: true
            },
            {
                field: 'lotAtt04',
                title: '品质',
                sortable: true
            },
            {
                field: 'lotAtt05',
                title: '批次属性5',
                sortable: true
            },
            {
                field: 'lotAtt06',
                title: '批次属性6',
                sortable: true
            },
            {
                field: 'lotAtt07',
                title: '批次属性7',
                sortable: true
            },
            {
                field: 'lotAtt08',
                title: '批次属性8',
                sortable: true
            },
            {
                field: 'lotAtt09',
                title: '批次属性9',
                sortable: true
            },
            {
                field: 'lotAtt10',
                title: '批次属性10',
                sortable: true
            },
            {
                field: 'lotAtt11',
                title: '批次属性11',
                sortable: true
            },
            {
                field: 'lotAtt12',
                title: '批次属性12',
                sortable: true
            },
            {
                field: 'paId',
                title: '上架任务ID',
                sortable: true
            }]
    });
}

/**
 * 加工明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function kitDetailClick(row) {
    kitDetail_currentRow = row;
    if (kitDetail_currentRow) {
        // 表单赋值
        evaluate('kitDetail', 'kitDetail_currentRow');
        // 按钮控制
        buttonControl();
    }
}

/**
 * 加工明细行双击事件
 * @param row 当前行
 */
function kitDetailDbClick(row) {
    kitDetail_currentRow = row;
    if (!kitDetail_isShowTab) {
        // 显示右边tab
        showTabRight('#kitDetail_tab-right', '#kitDetail_tab-left');
        kitDetail_isShowTab = true;
        // 批次屬性控件賦值
        kitDetailLoadLotAtt(row);
        // 表单赋值
        evaluate('kitDetail', 'kitDetail_currentRow');
        // 加载加工工序
        initKitStepTable();
    } else {
        // 隐藏右边tab
        hideTabRight('#kitDetail_tab-right', '#kitDetail_tab-left');
        kitDetail_isShowTab = false;
    }
    $("#kitDetailTable").bootstrapTable('resetView');
    // 按钮控制
    buttonControl();
}

/**
 * 加载批次属性控件
 */
function kitDetailLoadLotAtt(row) {
    var params = "ownerCode=" + row.ownerCode + "&skuCode=" + row.parentSkuCode + "&orgId=" + row.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            $('#kitDetailLotAttTab').empty();
            var html = bq.getLotAttTab(data, 'kitDetail_lotAtt');
            $('#kitDetailLotAttTab').append(html);
            $('#kitDetailLotAttTab .detail-date').each(function() {
                laydate.render({
                    elem: '#' + $(this).prop('id'),
                    theme: '#393D49'
                });
            });
        }
    })
}

/**
 * 加工确认
 */
function kitConfirmHandler() {
    var rows = [];
    if (!kitDetail_isShowTab) {
        rows = getSelections('#kitDetailTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#kitDetail_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        var disabledObjs = bq.openDisabled("#kitDetailForm");
        var row = {};
        bq.copyProperties(kitDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#kitDetailForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading();
    kitDetailCommonMethod('kitConfirm', rows);
}

/**
 * 取消加工
 */
function cancelKitHandler() {
    var rows = [];
    if (!kitDetail_isShowTab) {
        rows = getSelections('#kitDetailTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#kitDetail_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        var disabledObjs = bq.openDisabled("#kitDetailForm");
        rows.push(kitDetail_currentRow);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading();
    kitDetailCommonMethod('cancelKit', rows);
}

/**
 * 生成上架任务
 */
function createTaskPaHandler() {
    var rows = [];
    if (!kitDetail_isShowTab) {
        rows = getSelections('#kitDetailTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#kitDetail_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        var disabledObjs = bq.openDisabled("#kitDetailForm");
        var row = {};
        bq.copyProperties(kitDetail_currentRow, row);
        $.extend(row, bq.serializeJson($('#kitDetailForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading();
    kitDetailCommonMethod('createTask', rows);
}

/**
 * 加工明细公共方法
 * @param method
 * @param params
 */
function kitDetailCommonMethod(method, params) {
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(params),
        url: "${ctx}/wms/kit/banQinWmKitResultDetail/" + method,
        success: function (data) {
            if (data.success) {
                // 页面重载
                window.location = "${ctx}/wms/kit/banQinWmKitHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**
 * 数量输入同步到EA
 */
function kitDetailQtyChange() {
    var uomQty = !$("#kitDetail_uomQty").val() ? 0 : $("#kitDetail_uomQty").val();
    var qtyUom = !$("#kitDetail_qtyCurrentKitUom").val() ? 0 : $("#kitDetail_qtyCurrentKitUom").val();
    if (uomQty !== 0) {
        $("#kitDetail_qtyCurrentKitEa").val(Math.floor(qtyUom * 100) / 100 * uomQty);
    } else {
        $("#kitDetail_qtyCurrentKitEa").val('');
    }
}

function initKitStepTable() {
    $('#kitDetailStepTable').bootstrapTable('destroy').bootstrapTable({
        cache: false,
        sidePagination: "server",
        url: "${ctx}/wms/kit/banQinWmKitStep/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#orgId').val();
            searchParam.kitNo = kitDetail_currentRow.kitNo;
            searchParam.kitLineNo = kitDetail_currentRow.kitLineNo;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'stepLineNo',
            title: '工序行号',
            sortable: true
        },
        {
            field: 'step',
            title: '工序描述',
            sortable: true
        },
        {
            field: 'qtyKit',
            title: '加工数*',
            sortable: true
        },
        {
            field: 'qtyKitEa',
            title: '加工数EA',
            sortable: true
        },
        {
            field: 'kitOp',
            title: '加工人*',
            sortable: true
        },
        {
            field: 'kitTime',
            title: '加工时间',
            sortable: true
        }]
    });
}

/**************************************加工明细结束********************************************/
/**************************************上架任务开始********************************************/
function initPaTaskTable() {
    $('#paTaskTable').bootstrapTable('destroy');
    $('#paTaskTable').bootstrapTable({
        cache: false,
        sidePagination: "server",
        url: "${ctx}/wms/task/banQinWmTaskPa/grid",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#orgId').val();
            searchParam.orderNo = $("#kitNo").val() ? $("#kitNo").val() : '#';
            searchParam.orderType = "KIT";
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            clickDetail('paTask', row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {
            dbClickDetail('paTask', row);
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'paId',
            title: '上架任务ID',
            sortable: true
        },
        {
            field: 'lineNo',
            title: '上架任务行号',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_TASK_STATUS'))}, value, "-");
            }
        },
        {
            field: 'orderNo',
            title: '单据号',
            sortable: true
        },
        {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        },
        {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        },
        {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        },
        {
            field: 'fmLoc',
            title: '源库位编码',
            sortable: true
        },
        {
            field: 'fmId',
            title: '源跟踪号',
            sortable: true
        },
        {
            field: 'suggestLoc',
            title: '推荐库位',
            sortable: true
        },
        {
            field: 'packDesc',
            title: '包装规格',
            sortable: true
        },
        {
            field: 'uomDesc',
            title: '包装单位',
            sortable: true
        },
        {
            field: 'qtyPaUom',
            title: '上架数',
            sortable: true
        },
        {
            field: 'qtyPaEa',
            title: '上架数EA',
            sortable: true
        },
        {
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        },
        {
            field: 'toId',
            title: '目标跟踪号',
            sortable: true
        },
        {
            field: 'paOp',
            title: '上架操作人',
            sortable: true
        },
        {
            field: 'paTime',
            title: '上架时间',
            sortable: true
        },
        {
            field: 'reserveCode',
            title: '上架库位指定规则',
            sortable: true
        },
        {
            field: 'paRuleName',
            title: '上架规则',
            sortable: true
        }]
    });
}

/**
 * 上架任务明细行单击事件
 * @param tab tab页名称
 * @param row 当前行
 */
function paTaskClick(row) {
    paTask_currentRow = row;
    if (paTask_isShowTab) {
        // 表单赋值
        evaluate('paTask', 'paTask_currentRow');
        buttonControl();
    }
}

/**
 * 上架任务明细行双击事件
 * @param row 当前行
 */
function paTaskDbClick(row) {
    paTask_currentRow = row;
    if (!paTask_isShowTab) {
        // 显示右边tab
        showTabRight('#paTask_tab-right', '#paTask_tab-left');
        paTask_isShowTab = true;
        // 表单赋值
        evaluate('paTask', 'paTask_currentRow');
    } else {
        // 隐藏右边tab
        hideTabRight('#paTask_tab-right', '#paTask_tab-left');
        paTask_isShowTab = false;
    }
    $("#paTaskTable").bootstrapTable('resetView');
    buttonControl();
}

/**
 * 删除上架任务
 */
function removePaTask() {
    var rows = [];
    if (!paTask_isShowTab) {
        rows = getSelections('#paTaskTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 校验批次属性非空
        rows.push(paTask_currentRow);
    }
    jp.confirm('确认要删除该上架任务记录吗？', function () {
        jp.loading();
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: 'POST',
            dataType: "json",
            data: JSON.stringify(rows),
            url: "${ctx}/wms/kit/banQinWmKitResultDetail/removePaTask",
            success: function (data) {
                if (data.success) {
                    // 上架任务tab
                    $('#paTaskTable').bootstrapTable('refresh');
                    hideTabRight('#paTask_tab-right', '#paTask_tab-left');
                    paTask_isShowTab = false;
                    // 加工明细tab
                    $('#kitDetailTable').bootstrapTable('refresh');
                    hideTabRight('#kitDetail_tab-right', '#kitDetail_tab-left');
                    kitDetail_isShowTab = false;
                    jp.success(data.msg);
                } else {
                    jp.bqError(data.msg);
                }
            }
        });
    })
}

/**
 * 上架确认
 */
function confirmPaTask() {
    var rows = [];
    if (!paTask_isShowTab) {
        rows = getSelections('#paTaskTable');
        if (rows.length === 0) {
            jp.bqError("请选择一条记录!");
            return;
        }
    } else {
        // 非空校验
        var validate = bq.detailSubmitCheck('#paTask_tab-right');
        if(!validate.isSuccess) {
            jp.bqError(validate.msg);
            return;
        }
        var disabledObjs = bq.openDisabled("#paTaskForm");
        var row = {};
        bq.copyProperties(paTask_currentRow, row);
        $.extend(row, bq.serializeJson($('#paTaskForm')));
        rows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    jp.loading("上架确认中...");
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(rows),
        url: "${ctx}/wms/kit/banQinWmKitResultDetail/paConfirm",
        success: function (data) {
            if (data.success) {
                // 上架任务tab
                $('#paTaskTable').bootstrapTable('refresh');
                hideTabRight('#paTask_tab-right', '#paTask_tab-left');
                paTask_isShowTab = false;
                // 加工明细tab
                $('#kitDetailTable').bootstrapTable('refresh');
                hideTabRight('#kitDetail_tab-right', '#kitDetail_tab-left');
                kitDetail_isShowTab = false;
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

/**************************************上架任务结束********************************************/

/**************************************取消分配日志开始********************************************/
function initCancelAllocTable() {
    $('#cancelAllocTable').bootstrapTable('destroy');
    $('#cancelAllocTable').bootstrapTable({
        cache: false,
        sidePagination: "server",
        url: "${ctx}/wms/outbound/banQinWmDelAlloc/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = $('#orgId').val();
            searchParam.orderNo = $("#kitNo").val() ? $("#kitNo").val() : '#';
            searchParam.orderType = 'KIT';
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        },
        {
            field: 'allocId',
            title: '分配ID',
            sortable: true
        },
        {
            field: 'status',
            title: '状态',
            sortable: true
        },
        {
            field: 'orderNo',
            title: '加工单号',
            sortable: true
        },
        {
            field: 'ownerCode',
            title: '货主',
            sortable: true
        },
        {
            field: 'skuCode',
            title: '商品',
            sortable: true
        },
        {
            field: 'lotNum',
            title: '批次号',
            sortable: true
        },
        {
            field: 'locCode',
            title: '库位',
            sortable: true
        },
        {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        },
        {
            field: 'toLoc',
            title: '目标库位',
            sortable: true
        },
        {
            field: 'qtyUom',
            title: '分配数',
            sortable: true
        },
        {
            field: 'qtyEa',
            title: '分配数EA',
            sortable: true
        },
        {
            field: 'packCode',
            title: '包装规格',
            sortable: true
        },
        {
            field: 'uom',
            title: '包装单位',
            sortable: true
        },
        {
            field: 'op',
            title: '操作人',
            sortable: true
        },
        {
            field: 'opTime',
            title: '操作时间',
            sortable: true
        }]
    });
}

/**
 * 生成上架任务
 */
function createTaskPaByDelAllocHandler() {
    var rows = getSelections('#cancelAllocTable');
    if (rows.length === 0) {
        jp.bqError("请选择记录");
        return;
    }
    view = 'delAlloc';
    initPaTaskWindow();
}

function initPaTaskWindow() {
    $('#createTaskPaModal').modal();
    $('#isTaskPa').prop('checked', false).prop('disabled', false).val('N');
    $('#allocLoc').prop('checked', true).prop('disabled', true);
    $('#paRuleLoc').prop('checked', false).prop('disabled', true);
}

var view = '';
function createTaskPaConfirm() {
    switch (view) {
        case "kitTask": createPaTaskByKit(); break;
        case "delAlloc": createPaTaskByDel(); break;
    }
}

function createPaTaskByKit() {
    var allocRows = [];
    if (!kitTask_isShowTab) {
        allocRows = getSelections('#kitTaskTable');
    } else {
        var disabledObjs = bq.openDisabled('#kitTaskForm');
        var row = {};
        bq.copyProperties(kitTask_currentRow, row);
        $.extend(row, bq.serializeJson($('#kitTaskForm')));
        allocRows.push(row);
        bq.closeDisabled(disabledObjs);
    }
    var isAllocLoc = $('#allocLoc').prop('checked') ? 'Y' : 'N';
    var isTaskPa = $('#isTaskPa').val();
    for (var index = 0, length = allocRows.length; index < length; index++) {
        allocRows[index].isTaskPa = isTaskPa;
        allocRows[index].isAllocLoc = isAllocLoc;
    }
    jp.loading();
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(allocRows),
        url: "${ctx}/wms/kit/banQinWmTaskKit/cancelPick",
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/kit/banQinWmKitHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function createPaTaskByDel() {
    var rows = getSelections('#cancelAllocTable');
    for (var i = 0; i < rows.length; i++) {
        if (rows[i].status === '60') {
            rows[i].soNo = rows[i].orderNo;
            allocRows.push(rows[i]);
        }
    }
    if (allocRows.length === 0) {
        jp.bqError('请选择一条记录');
        return;
    }
    var isAllocLoc = $('#allocLoc').prop('checked') ? 'Y' : 'N';
    var isTaskPa = $('#isTaskPa').val();
    for (var index = 0, length = allocRows.length; index < length; index++) {
        allocRows[index].isTaskPa = isTaskPa;
        allocRows[index].isAllocLoc = isAllocLoc;
    }
    jp.loading();
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(allocRows),
        url: "${ctx}/wms/kit/banQinWmDelAlloc/cancelPick",
        success: function (data) {
            if (data.success) {
                window.location = "${ctx}/wms/kit/banQinWmKitHeader/form?id=" + $('#id').val();
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

function isTaskPaChange(flag) {
    $('#isTaskPa').val(flag ? 'Y' : 'N');
    $('#allocLoc').prop('disabled', !flag);
    $('#paRuleLoc').prop('disabled', !flag);
    if (!flag) {
        $('#allocLoc').prop('checked', true);
        $('#paRuleLoc').prop('checked', false);
    }
}
/**************************************取消分配日志结束********************************************/

</script>