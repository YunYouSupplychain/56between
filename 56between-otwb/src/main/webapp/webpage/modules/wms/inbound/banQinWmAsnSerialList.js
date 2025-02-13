<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var receive_currentRow;
var currentLoginName = '${fns:getUser().loginName}';
var enterFlag = true;
$(document).ready(function () {
    // 初始化序列号table
    initAsnSerialTable();
    // 初始化收货明细table
    initAsnReceiveTable();
    // 回车监听事件
    document.onkeydown = function(e) {
        var ev = document.all ? window.event : e;
        if(ev.keyCode === 13) {
            enterEvent(e);
        }
    }
});

function initAsnSerialTable() {
    $('#banQinWmAsnSerialTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.asnNo = $('#asnNo').val();
            searchParam.skuCode = $('#skuCode').val();
            searchParam.lineNo = $('#editor_lineNo').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            field: 'rcvLineNo',
            title: '收货明细行号',
            sortable: false
        }, {
            field: 'serialNo',
            title: '序列号',
            sortable: false
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: false
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_SERIAL_STATUS'))}, value, "-");
            }
        }]
    });
}

function initAsnReceiveTable() {
    $('#banQinWmAsnReceiveTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.asnNo = $('#asnNo').val();
            searchParam.skuCode = $('#skuCode').val();
            searchParam.status = "00";
            searchParam.isSerial = "Y";
            searchParam.planId = $('#traceId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onPostBody: function () {
            afterLoadReceiveTable();
        },
        onClickRow: function (row, $el) {
            clickReceiveDetail(row);
            jp.changeTableStyle($el);
        },
        columns: [{
            field: 'lineNo',
            title: '行号',
            sortable: false
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_ASN_STATUS'))}, value, "-");
            }
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: false
        }, {
            field: 'qtyPlanEa',
            title: '预收数EA',
            sortable: false
        }, {
            field: 'toId',
            title: '收货跟踪号',
            sortable: false
        }, {
            field: 'lotNum',
            title: '批次号',
            sortable: false
        }, {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: false
        }, {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: false
        }, {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: false
        }, {
            field: 'lotAtt04',
            title: '品质',
            sortable: false
        }, {
            field: 'lotAtt05',
            title: '批次属性05',
            sortable: false
        }, {
            field: 'lotAtt06',
            title: '批次属性06',
            sortable: false
        }, {
            field: 'lotAtt07',
            title: '批次属性07',
            sortable: false
        }, {
            field: 'lotAtt08',
            title: '批次属性08',
            sortable: false
        }, {
            field: 'lotAtt09',
            title: '批次属性09',
            sortable: false
        }, {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: false
        }, {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: false
        }, {
            field: 'lotAtt12',
            title: '批次属性12',
            sortable: false
        }]
    });
}

function enterEvent(e) {
    if (enterFlag) {
        enterFlag = false;
        var inputId = $(e.target).prop('id');
        var rows = $('#banQinWmAsnReceiveTable').bootstrapTable("getData");
        var isSave = checkIsEdit();
        switch (inputId) {
            case "asnNo":
                var asnNo = $('#asnNo').val();
                if (!asnNo) {
                    jp.warning("入库单号不能为空");
                    return;
                }
                $('#banQinWmAsnReceiveTable').bootstrapTable("refresh", {'url': "${ctx}/wms/inbound/banQinWmAsnDetailReceive/data"});
                $('#skuCode').focus();
                break;
            case "skuCode":
                if (isSave && rows.length > 0) {
                    jp.confirm("收货明细位收货，是否确认收货？", function () {
                        receiveConfirm();
                    }, function () {
                        query();
                        $('#traceId').focus();
                    })
                } else {
                    query();
                    $('#traceId').focus();
                }
                break;
            case "traceId":
                if (isSave && rows.length > 0) {
                    jp.confirm("收货明细位收货，是否确认收货？", function () {
                        receiveConfirm();
                    }, function () {
                        query();
                        focusLock();
                    })
                } else {
                    query();
                    focusLock();
                }
                break;
            case "serialNo":
                scanSerial();
                $('#serialNo').val('');
                break;
            default:
                enterFlag = true;
        }
    }
}

function checkIsEdit() {
    if (receive_currentRow) {
        return $('#editor_toId').val() !== receive_currentRow.toId || $('#editor_toLoc').val() !== receive_currentRow.toLoc;
    }
    return false;
}

/**
 * 查询
 */
function query() {
    var asnNo = $('#asnNo').val();
    if (!asnNo) {
        jp.warning("入库单号不能为空");
        enterFlag = true;
        return;
    }
    $('#banQinWmAsnReceiveTable').bootstrapTable("refresh", {'url': "${ctx}/wms/inbound/banQinWmAsnDetailReceive/data"});
}

/**
 * 重置
 */
function reset() {
    // 清空表单
    $(':input', '#searchForm').val('');
    $('#isSubtract').prop('checked', false);
    $('#banQinWmAsnSerialTable').bootstrapTable("removeAll");
    $('#banQinWmAsnReceiveTable').bootstrapTable("removeAll");
    // 解除锁定
    focusUnLock();
    $('#asnNo').prop('readonly', false).focus();
    receive_currentRow = null;
}

/**
 * 单击表格事件
 * @param row 当前行
 */
function clickReceiveDetail(row) {
    receive_currentRow = row;
    loadSearchForm();
}

/**
 * 渲染页面及加载第一条数据
 */
function afterLoadReceiveTable() {
    enterFlag = true;
    var data = $('#banQinWmAsnReceiveTable').bootstrapTable("getData");
    if (!$("#banQinWmAsnReceiveTable tr:eq(1)").hasClass('no-records-found')) {
        clickReceiveDetail(data[0]);
        jp.changeTableStyle($("#banQinWmAsnReceiveTable tr:eq(1)"));
    } else {
        // 清空表单
        $("input[id^=editor]").each(function() {$(this).val('');});
        $('#isSubtract').prop('checked', false);
        $('#banQinWmAsnSerialTable').bootstrapTable("removeAll");
        // 解除锁定
        focusUnLock();
    }
}

function loadSearchForm() {
    $('#asnNo').prop('readonly', true);
    $('#editor_lineNo').val(receive_currentRow.lineNo);
    $('#editor_qtyPlanEa').val(receive_currentRow.qtyPlanEa);
    $('#editor_qtyScanned').val(0);
    $('#editor_toId').val(receive_currentRow.toId);
    window["editor_toLoc"] =  receive_currentRow.toLoc;
    $('#editor_toLoc').val(receive_currentRow.toLoc);
    // 获取序列号
    loadAsnSerialTable();
    // 加载批次信息
    loadLotAtt();
}

/**
 * 查询序列号table
 */
function loadAsnSerialTable() {
    $('#banQinWmAsnSerialTable').bootstrapTable("refresh", {'url': "${ctx}/wms/inbound/banQinWmAsnSerial/data"});
}

/**
 * 加载批次属性控件
 */
function loadLotAtt() {
    var params = "ownerCode=" + receive_currentRow.ownerCode + "&skuCode=" + receive_currentRow.skuCode + "&orgId=" + receive_currentRow.orgId;
    bq.get("${ctx}/wms/basicdata/banQinCdWhLotDetail/getInfo?" + params, false, function (data) {
        if (data) {
            if (receive_currentRow.isQc === 'Y') {
                data[3].inputControl = 'R';
            }
            $('#lotAttTab').empty();
            var html = bq.getHorizontalLotAttTab(data, 'lotAtt');
            $('#lotAttTab').append(html);
            $('#lotAttTab .detail-date').each(function() {
                laydate.render({
                    elem: '#' + $(this).prop('id'),
                    theme: '#393D49'
                });
            });
            $("input[id^=lotAtt]").each(function() {
                var $name = $(this).prop('name');
                $(this).val(receive_currentRow[$name]);
            });
        }
    })
}

/**
 * 锁定序列号
 */
var serialT;
function focusLock() {
    serialT = setInterval(function(){
        $('#serialNo').focus();
    },60);
}

/**
 * 解锁序列号
 */
function focusUnLock() {
    if (serialT) {
        window.clearInterval(serialT);
    }
}

/**
 * 收货确认
 */
function receiveConfirm() {
    if (!receive_currentRow) {
        jp.warning("请选择一条收货明细");
        enterFlag = true;
        return;
    }
    var isValidate = bq.headerSubmitCheck('#searchForm');
    if (!isValidate.isSuccess) {
        jp.warning(isValidate.msg);
        enterFlag = true;
        return;
    }
    if (parseFloat($('#editor_qtyScanned').val()) <= 0) {
        jp.warning("已扫描数必须大于0");
        enterFlag = true;
        return;
    }

    // 收集收货数据
    collectReceive();
    jp.loading();
    $.ajax({
        headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
        type: 'POST',
        dataType: "json",
        data: JSON.stringify(receive_currentRow),
        url: "${ctx}/wms/inbound/banQinWmAsnHeader/serialReceiving",
        success: function (data) {
            if (data.success) {
                $('#traceId').val('');
                query();
                jp.success(data.msg);
            } else {
                jp.warning(data.msg);
            }
        }
    });

}

function collectReceive() {
    $("input[id^=lotAtt]").each(function() {
        var $name = $(this).prop('name');
        receive_currentRow[$name] = $(this).val();
    });
    receive_currentRow.toId = $('#editor_toId').val();
    receive_currentRow.toLoc = $('#editor_toLoc').val();
    receive_currentRow.serialList = $('#banQinWmAsnSerialTable').bootstrapTable("getData");
}

/**
 * 扫描序列号
 */
function scanSerial() {
    var skuCode = $('#skuCode').val();
    if (!skuCode) {
        jp.warning("商品不能为空");
        enterFlag = true;
        return;
    }
    var serialNo = $('#serialNo').val();
    if (!serialNo) {
        jp.warning("序列号不能为空");
        enterFlag = true;
        return;
    }
    var lineNo = $('#editor_lineNo').val();
    if (!lineNo) {
        jp.warning("请选择一条收货明细");
        enterFlag=true;
        return;
    }

    // 是否扣数
    var isSubtract = $('#isSubtract').prop('checked');
    // 扫描数
    var qtyScanned = parseFloat($('#editor_qtyScanned').val());
    // 序列号是否存在列表中
    var isExist = false;
    // 序列号是否在计划中
    var isPlan = false;
    var $serialTable = $('#banQinWmAsnSerialTable');
    // 如果有导入的序列号，存在在导入中，则更新状态为已扫描
    var items = $serialTable.bootstrapTable("getData");
    for (var i = 0; i < items.length; i++) {
        if (serialNo === items[i].serialNo) {
            if (isSubtract) {
                if (items[i].status === '30') {
                    break;
                }
                if (items[i].status === '10') {
                    $serialTable.bootstrapTable("remove", {field: 'uniqueId', values: [items[i].uniqueId]});
                } else {
                    items[i].status = '30';
                    items[i].rcvLineNo = null;
                    items[i].scanOp = null;
                    items[i].scanTime = null;
                }
                isExist=true;
            } else {
                if (items[i].status === '30') {
                    items[i].status = '40';
                    items[i].rcvLineNo = lineNo;
                    items[i].scanOp = currentLoginName;
                    items[i].scanTime = jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss");
                    isPlan = true;
                }
                isExist = true;
            }
            break;
        }
    }
    $serialTable.bootstrapTable("load", items);
    //是否扣数
    if (isSubtract) {
        if (!isExist) {
            jp.warning(serialNo + "序列号未扫描");
            enterFlag = true;
            return;
        }
        qtyScanned = qtyScanned - 1;
    } else {
        // 校验序列号是否在计划中，如果不存在，则提示
        if (isExist) {
            if (!isPlan) {
                jp.warning(serialNo + "序列号已扫描");
                enterFlag=true;
                return;
            }
        } else {
            // 添加之前校验是否允许超收
            var msg = beforeAddCheck();
            if (msg.success) {
                addAsnSerialHandler();
            } else {
                jp.warning(msg.msg);
                enterFlag = true;
                return;
            }
        }
        // 扫描数加1
        qtyScanned = qtyScanned + 1;
    }
    enterFlag = true;
    // 实际收货数
    receive_currentRow.currentQtyRcvEa = qtyScanned;
    $('#editor_qtyScanned').val(qtyScanned);
}

function beforeAddCheck() {
    var msg = {success: true, msg: ''};
    var isOverRcv = receive_currentRow.isOverRcv ? receive_currentRow.isOverRcv : 'N';
    var overRcvQty = receive_currentRow.overRcvPct ? parseFloat(receive_currentRow.overRcvPct) : 0;
    var qtyPlanEa = receive_currentRow.qtyPlanEa ? parseFloat(receive_currentRow.qtyPlanEa) : 0;
    if ("N" === isOverRcv && parseFloat($('#editor_qtyScanned').val()) + 1 > receive_currentRow.qtyPlanEa) {
        msg = {success: false, msg: '不允许超收'};
        return msg;
    }
    if ("Y" === isOverRcv) {
        if (overRcvQty !== 0 && (((parseFloat($('#editor_qtyScanned').val()) + 1 - qtyPlanEa) / qtyPlanEa) * 100 > overRcvQty)) {
            msg = {success: false, msg: '收货数超过超收比例'};
        }
    }
    return msg;
}

/**
 * 换单
 */
function changeOrder() {
    var isSave = checkIsEdit();
    var items = $('#banQinWmAsnSerialTable').bootstrapTable('getData');
    if (isSave || items.length > 0) {
        jp.confirm("收货明细位收货，是否确认收货？", function () {
            receiveConfirm();
        }, function () {
            reset();
        })
    } else {
        reset();
    }
}

/**
 * 序列号新增方法
 */
function addAsnSerialHandler() {
    var record = {};
    record.asnNo = $('#asnNo').val();
    record.ownerCode = receive_currentRow.ownerCode;
    record.status = '10';
    record.scanOp = currentLoginName;
    record.scanTime = jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss");
    record.rcvLineNo = $('#editor_lineNo').val();
    record.serialNo = $('#serialNo').val();
    record.skuCode = receive_currentRow.skuCode;
    record.headId = receive_currentRow.headId;
    record.orgId = receive_currentRow.orgId;
    record.uniqueId = guid();
    $('#banQinWmAsnSerialTable').bootstrapTable("append", record);
}

function guid() {
    return new Date().getTime();
}

</script>