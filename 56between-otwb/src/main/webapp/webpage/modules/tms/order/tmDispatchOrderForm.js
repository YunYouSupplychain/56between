<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var tmDispatchOrderSiteRowIdx = 0, tmDispatchOrderSiteTpl, maxDispatchSiteSeq = 0;
$(document).ready(function () {
    initV();
    editController();

    tmDispatchOrderSiteTpl = $("#tmDispatchOrderSiteTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    /*表格全选复选框绑定事件*/
    $("#tmDispatchOrderSiteTable").find("input[name='btSelectAll']").on('click', function () {
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
    $('#carrierNameDBtnId').click(function () {
        carrierSelect();
    });

    initSite(${fns:toJson(tmDispatchOrderEntity.tmDispatchOrderSiteList)});
    initLabel();
    initTransport();

    top.layer.closeAll('dialog');// 关闭所有的信息框
});

function serializeJson($form) {
    var o = {};
    $.each($form.serializeArray(), function () {
        if (o[this.name] !== undefined && o[this.name] !== null) {
            o[this.name] = o[this.name] + "," + this.value;
        } else {
            o[this.name] = this.value || null;
        }
    });
    return o;
}

/*初始化值*/
function initV() {
    if ($("#id").val().length <= 0) {
        $("#orgId").val(jp.getCurrentOrg().orgId);
        $("#baseOrgId").val(tmOrg.id);
        $("#dispatchType").val("5");// 配送
        $("#transportType").val("3");// 陆运
        $("#dispatchStatus").val("00");// 创建
        $("#isException").val("N");
        $("#isAppInput").val("N");
        $("#dispatchTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
        $("#dataSource").val("00");// 手工单
        $("#dispatcher").val('${fns:getUser().name}');
    }
    $('#page1,#page2,#page3').find('div[class*="date"]').datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
}

/*编辑控制*/
function editController() {
    var isNew = $("#id").val().length <= 0;// 是否是新增未保存数据
    var orderStatus = $("#dispatchStatus").val();// 状态
    if ("00" === orderStatus) {// 新建
        $("#save").attr('disabled', false);
        $("#audit").attr('disabled', isNew);
        $("#site_add").attr('disabled', isNew);
        $("#site_save").attr('disabled', isNew);
        $("#site_remove").attr('disabled', isNew);
    } else if ("10" === orderStatus) {// 已审核
        $("#cancelAudit").attr('disabled', false);
        $("#depart").attr('disabled', false);
    } else if ("20" === orderStatus) {// 已发车
    } else if ("90" === orderStatus) {// 取消

    } else if ("99" === orderStatus) {// 关闭
    }
    $("#annex").attr('disabled', isNew);
    $("#dispatchOutletName").prop('readonly', !isNew);
    $("#dispatchOutletNameSBtnId").prop('disabled', !isNew);
    $("#dispatchOutletNameDBtnId").prop('disabled', !isNew);
    $("#orderLabel_show").attr('disabled', isNew);
    $("#orderTransport_show").attr('disabled', isNew);
}

/*承运商选择后*/
function carrierSelect(data) {
    $("#carNo").val("");
    $("#driver").val("");
    $("#driverName").val("");
    $("#driverTel").val("");
    $("#copilot").val("");
    $("#copilotName").val("");
    $("#account").val("");
}

/*车辆选择后*/
function carSelect(data) {
    if (data) {
        if (data.carrierCode) {
            $("#carrierCode").val(data.carrierCode);
            $("#carrierName").val(data.carrierName);
        }
        $("#driver").val(data.mainDriver);
        $("#driverName").val(data.mainDriverName);
        $("#driverTel").val(data.mainDriverTel);
        $("#copilot").val(data.copilot);
        $("#copilotName").val(data.copilotName);
    }
}

/*司机选择后*/
function driverSelect(data) {
    if (data) {
        $("#driverTel").val(data.phone);
        $("#account").val(data.account);
    }
}

/*配送对象选择后*/
function transportObjSelect(data, idx) {
    if (data && data.hasOwnProperty("transportObjName")) {
        $("#tmDispatchOrderSiteList" + idx + "_outletName").val(data.transportObjName);
        $("#tmDispatchOrderSiteList" + idx + ">td").eq(4).text(data.transportObjName);
    }
}

/*表格增加行*/
function addRow(list, idx, tpl, row) {
    $(list).append(Mustache.render(tpl, {
        idx: idx, delBtn: true, row: row
    }));
    $(list + idx).find("select").each(function () {
        var value = $(this).attr("data-value");
        if ($(this).hasClass('selectpicker')) {
            $(this).selectpicker({noneSelectedText: '请选择'}).selectpicker('val', value.split(',')).selectpicker('refresh');
        } else {
            $(this).val(value);
        }
    });
    $(list + idx).find("input[type='checkbox']").each(function () {
        if (!$(this).val()) {
            $(this).val('N');
            return;
        }

        $(this).prop("checked", ("Y" === $(this).val()));
    });
    $(list + idx).find("input[name='btSelectItem']").on('click', function () {
        var $table = $(this).parents("table").eq(0);
        $table.find("input[name='btSelectAll']").prop('checked', $table.find("input[name='btSelectItem']").not("input:checked").length <= 0);
    });
}

/*表格删除行*/
function delRow(list, url, callback) {
    jp.confirm('确认要删除选中记录吗？', function () {
        jp.loading();
        // 获取选中的行
        var ids = [], idxs = [];
        $.map($(list).find("tr input[type='checkbox']:checked"), function ($element) {
            var idx = $($element).data("index");
            idxs.push(idx);
            var id = $(list + idx + "_id").val();
            if (id) {
                ids.push(id);
            }
        });
        // 页面表格删除
        del = function (indexs) {
            $.map(indexs, function (idx) {
                $(list + idx).remove();
            });
            jp.success("操作成功");
            callback();
        };
        if (url && ids.length > 0) {
            jp.post(url, {id: $("#id").val(), ids: ids.join(',')}, function (data) {
                if (data.success) {
                    del(idxs);
                } else {
                    jp.error(data.msg);
                }
            });
        } else {
            del(idxs);
        }
    });
}

/*保存运输订单基本信息、运输信息、配送信息*/
function save() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    var data = $('#inputForm').serialize();
    bq.closeDisabled(objs);

    jp.post("${ctx}/tms/order/tmDispatchOrder/save", data, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/order/tmDispatchOrder/form?id=" + data.body.entity.id;
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*审核*/
function audit() {
    jp.loading();
    var id = $("#id").val();
    jp.post("${ctx}/tms/order/tmDispatchOrder/audit", {ids: id}, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/order/tmDispatchOrder/form?id=" + id;
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*取消审核*/
function cancelAudit() {
    jp.loading();
    var id = $("#id").val();
    jp.post("${ctx}/tms/order/tmDispatchOrder/cancelAudit", {ids: id}, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/order/tmDispatchOrder/form?id=" + id;
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*发车*/
function depart() {
    jp.loading();
    var id = $("#id").val();
    jp.post("${ctx}/tms/order/tmDispatchOrder/depart", {ids: id}, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/order/tmDispatchOrder/form?id=" + id;
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

/*初始化配送点*/
function initSite(rows) {
    $('#carrierName').prop('readonly', false);
    $('#carrierNameSBtnId').prop('disabled', false);
    $('#carrierNameDBtnId').prop('disabled', false);
    if (rows === undefined || rows.length <= 0) return;
    $("#tmDispatchOrderSiteList").empty();

    tmDispatchOrderSiteRowIdx = 0;
    maxDispatchSiteSeq = 0;
    for (var i = 0; i < rows.length; i++) {
        addSite(rows[i]);
    }
}

/*新增配送点*/
function addSite(row) {
    if (row === undefined) {
        if ("${fns:getControlParam('IS_CARRIER_SERVICE_SCOPE_PATTERN')}" === "Y" && $("#carrierCode").val().length <= 0) {
            jp.warning("请选择承运商");
            return;
        }
        row = {dispatchNo: $("#dispatchNo").val(), orgId: $("#orgId").val(), baseOrgId: $("#baseOrgId").val(), recVer: 0, dispatchSeq: maxDispatchSiteSeq + 1};
    }
    addRow('#tmDispatchOrderSiteList', tmDispatchOrderSiteRowIdx, tmDispatchOrderSiteTpl, row);
    if ($("#dispatchStatus").val() === "00" && row.hasOwnProperty("receiveShip")) {
        $("#tmDispatchOrderSiteList" + tmDispatchOrderSiteRowIdx + "Btn").attr('disabled', row.receiveShip.length === 0);
    }
    if (row.hasOwnProperty("dispatchSeq") && row.dispatchSeq > maxDispatchSiteSeq) {
        maxDispatchSiteSeq = row.dispatchSeq;
    }
    tmDispatchOrderSiteRowIdx = tmDispatchOrderSiteRowIdx + 1;
    if ("${fns:getControlParam('IS_CARRIER_SERVICE_SCOPE_PATTERN')}" === "Y") {
        $('#carrierName').prop('readonly', true);
        $('#carrierNameSBtnId').prop('disabled', true);
        $('#carrierNameDBtnId').prop('disabled', true);
    }
}

/*保存配送点*/
function saveSite() {
    jp.loading();
    var validator = bq.tableValidate("#tmDispatchOrderSiteForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var dispatchNo = $("#dispatchNo").val();
    var orgId = $("#orgId").val();

    var disabledObjs = bq.openDisabled("#tmDispatchOrderSiteForm");
    var params = serializeJson($('#tmDispatchOrderSiteForm'));
    params['id'] = $("#id").val();
    bq.closeDisabled(disabledObjs);

    jp.post("${ctx}/tms/order/dispatch/site/save", params, function (data) {
        if (data.success) {
            jp.post("${ctx}/tms/order/dispatch/site/data", {dispatchNo: dispatchNo, orgId: orgId}, function (data) {
                initSite(data);
            });
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*删除配送点*/
function delSite() {
    delRow('#tmDispatchOrderSiteList', '${ctx}/tms/order/dispatch/site/delete', initSite);
}

/*打开选择订单模态框*/
function openSelectModal(idx) {
    var oldReceiveShip = $("#tmDispatchOrderSiteList" + idx + "_oldReceiveShip").val();
    var oldOutletCode = $("#tmDispatchOrderSiteList" + idx + "_oldOutletCode").val();
    var receiveShip = $("#tmDispatchOrderSiteList" + idx + "_receiveShip").val();
    var outletCode = $("#tmDispatchOrderSiteList" + idx + "_outletCode").val();
    var outletName = $("#tmDispatchOrderSiteList" + idx + "_outletName").val();
    if (oldOutletCode != outletCode || oldReceiveShip != receiveShip) {
        jp.warning("修改后请选保存配送点信息");
        return;
    }

    var $modal = $("#selectModal");
    $modal.data("idx", idx);
    $modal.data("receiveShip", receiveShip);
    $modal.find("span[class~='dispatchSite']").text(outletName);
    $.map(['page1', 'page2', 'page3'], function (page) {
        initRSSelectV(page);
    });

    initSelectLeftTable();
    initSelectRightTable();
    refreshSelectTab("page1");

    /*按钮绑定点击事件*/
    $(".search").off('click').on('click', function () {
        var page = $(this).data("page");
        refreshSelectTab(page);
    });
    $(".reset").off('click').on('click', function () {
        var page = $(this).data("page");
        var $searchForm = $("#" + page + " .searchForm");
        $searchForm.find('input').val("");
        $searchForm.find('select').val("");
        $searchForm.find('.select-item').val("");
        initRSSelectV(page);
    });
    $(".left-refresh").off('click').on('click', function () {
        var page = $(this).data("page");
        $("#" + page).find("table[class~='left-tab']").eq(0).bootstrapTable('refresh', {url: "${ctx}/tms/order/dispatch/site/opt/data"});
    });
    $(".right-refresh").off('click').on('click', function () {
        var page = $(this).data("page");
        $("#" + page).find("table[class~='right-tab']").eq(0).bootstrapTable('refresh', {url: "${ctx}/tms/order/dispatch/site/opted/data"});
    });
    $(".confirm").off('click').on('click', function () {
        jp.loading();
        var page = $(this).data("page");
        var rows = $("#" + page).find("table[class~='left-tab']").eq(0).bootstrapTable('getSelections');
        if (rows.length <= 0) {
            jp.warning("请选择记录");
            return;
        }
        var dispatchNo = $("#dispatchNo").val();
        var dispatchSiteOutletCode = $("#tmDispatchOrderSiteList" + idx + "_outletCode").val();
        var orgId = $("#orgId").val();
        $.map(rows, function (row) {
            row.dispatchNo = dispatchNo;
            row.dispatchSiteOutletCode = dispatchSiteOutletCode;
            row.orgId = orgId;
        });
        $.ajax({
            type: "POST",
            url: "${ctx}/tms/order/dispatch/site/opt/confirm",
            data: JSON.stringify(rows),
            dataType: "json",
            cache: false,
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    refreshSelectTab(page);
                    jp.success(data.msg);
                } else {
                    jp.error(data.msg);
                }
            }
        });
    });
    $(".confirm-all").off('click').on('click', function () {
        jp.loading();
        var page = $(this).data("page");
        jp.post("${ctx}/tms/order/dispatch/site/opt/confirmAll", selectLeftParams(page), function (data) {
            if (data.success) {
                refreshSelectTab(page);// 刷新加载表格数据
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });
    $(".un-confirm").off('click').on('click', function () {
        jp.loading();
        var page = $(this).data("page");
        var rows = $("#" + page).find("table[class~='right-tab']").eq(0).bootstrapTable('getSelections');
        if (rows.length <= 0) {
            jp.warning("请选择记录");
            return;
        }
        $.ajax({
            type: "POST",
            url: "${ctx}/tms/order/dispatch/site/opted/cancelConfirm",
            data: JSON.stringify(rows),
            dataType: "json",
            cache: false,
            contentType: "application/json",
            success: function (data) {
                if (data.success) {
                    refreshSelectTab(page);
                    jp.success(data.msg);
                } else {
                    jp.error(data.msg);
                }
            }
        });
    });
    $(".un-confirm-all").off('click').on('click', function () {
        jp.loading();
        var page = $(this).data("page");
        jp.post("${ctx}/tms/order/dispatch/site/opted/cancelConfirmAll", selectRightParams($("#selectModal").data("idx")), function (data) {
            if (data.success) {
                refreshSelectTab(page);// 刷新加载表格数据
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
    });

    // 默认展示第一个选项卡
    $("#selectModalTabs a:first").tab('show');
    // 选项卡切换绑定事件
    $("#selectModalTabs a").off('click').on('click', function (e) {
        e.preventDefault();//阻止a链接的跳转行为
        $(this).tab('show');//显示当前选中的链接及关联的content
        refreshSelectTab($(this).data("page"));// 刷新加载表格数据
    });

    // CSS样式
    var width = Math.floor(document.body.clientWidth * 0.90);
    var height = Math.floor(document.body.clientHeight * 0.9);
    $("#selectModal .modal-dialog").css('width', width);
    $("#selectModal .modal-body").css('height', height);
    $(".left,.right").css('width', Math.floor((width - 60) / 2)).css('height', height - 170);
    $(".left-tab,.right-tab").css('max-height', height - 250);
    $modal.off('hide.bs.modal').on('hide.bs.modal', function () {
        jp.loading('加载中...');
        window.location = "${ctx}/tms/order/tmDispatchOrder/form?id=" + $('#id').val();
    });

    // 显示窗口
    $modal.modal({backdrop: 'static', keyboard: false});
}

function initRSSelectV(page) {
    var receiveShip = $("#selectModal").data('receiveShip');
    var $receiveShip = $("#" + page + " .searchForm").find('select[name="receiveShip"]');
    $receiveShip.empty();
    $.map(receiveShip, function (v) {
        if ("R" === v) {
            $receiveShip.append('<option value="R">提货</option>');
        } else {
            $receiveShip.append('<option value="S">送货</option>');
        }
    });
}

/*可选Tab初始化*/
function initSelectLeftTable() {
    $.map($("table[class~='left-tab']"), function (tab) {
        var page = $(tab).data("page");
        $(tab).bootstrapTable('destroy');
        $(tab).bootstrapTable({
            cache: false,//是否使用缓存，默认为true
            sortName: 'labelNo',// 默认按字段labelNo排序
            pagination: true,//是否显示分页
            sidePagination: "client",//分页方式：client客户端分页，server服务端分页
            queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
                var searchParam = selectLeftParams(page);
                searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                return searchParam;
            },
            columns: [{
                checkbox: true
            }, {
                field: 'labelNo',
                title: '标签',
                sortable: true
            }, {
                field: 'transportNo',
                title: '运输单号',
                sortable: true
            }, {
                field: 'orderType',
                title: '订单类型',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_ORDER_TYPE'))}, value, "-");
                }
            }, {
                field: 'customerName',
                title: '客户',
                sortable: true
            }, {
                field: 'skuName',
                title: '商品',
                sortable: true
            }, {
                field: 'nowOutletName',
                title: '发货网点',
                sortable: true
            }, {
                field: 'receiveShip',
                title: '提货/送货',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_RECEIVE_SHIP'))}, value, "-");
                }
            }, {
                field: 'nextOutletName',
                title: '计划送往网点',
                sortable: true
            }, {
                field: 'nowCity',
                title: '发货城市',
                sortable: true
            }, {
                field: 'nextCity',
                title: '计划送往城市',
                sortable: true
            }, {
                field: 'customerNo',
                title: '客户单号',
                sortable: true
            }, {
                field: 'principalName',
                title: '委托方',
                sortable: true
            }]
        });
    });
}

/*已选Tab初始化*/
function initSelectRightTable() {
    $.map($("table[class~='right-tab']"), function (tab) {
        $(tab).bootstrapTable('destroy');
        $(tab).bootstrapTable({
            cache: false,//是否使用缓存，默认为true
            sortName: 'labelNo',// 默认按字段labelNo排序
            pagination: true,//是否显示分页
            sidePagination: "client",//分页方式：client客户端分页，server服务端分页
            queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
                var searchParam = selectRightParams($("#selectModal").data("idx"));
                searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                return searchParam;
            },
            formatNoMatches: function () {
                return "";
            },
            columns: [{
                checkbox: true
            }, {
                field: 'labelNo',
                title: '标签',
                sortable: true
            }, {
                field: 'transportNo',
                title: '运输单号',
                sortable: true
            }, {
                field: 'orderType',
                title: '订单类型',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TRANSPORT_ORDER_TYPE'))}, value, "-");
                }
            }, {
                field: 'customerName',
                title: '客户',
                sortable: true
            }, {
                field: 'skuName',
                title: '商品',
                sortable: true
            }, {
                field: 'nowOutletName',
                title: '发货网点',
                sortable: true
            }, {
                field: 'receiveShip',
                title: '提货/送货',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_RECEIVE_SHIP'))}, value, "-");
                }
            }, {
                field: 'nextOutletName',
                title: '计划送往网点',
                sortable: true
            }, {
                field: 'nowCity',
                title: '发货城市',
                sortable: true
            }, {
                field: 'nextCity',
                title: '计划送往城市',
                sortable: true
            }, {
                field: 'customerNo',
                title: '客户单号',
                sortable: true
            }, {
                field: 'principalName',
                title: '委托方',
                sortable: true
            }]
        });
    });
}

/*可选Tab查询参数*/
function selectLeftParams(page) {
    var idx = $("#selectModal").data("idx");
    var dispatchOutletCode = $('#dispatchOutletCode').val();// 派车网点
    var dispatchSiteOutletCode = $("#tmDispatchOrderSiteList" + idx + "_outletCode").val();// 配送点
    var baseOrgId = $("#baseOrgId").val();
    var orgId = $("#orgId").val();
    var dispatchNo = $("#dispatchNo").val();
    var carrierCode = $("#carrierCode").val();
    var rcvOutletCodes = [dispatchOutletCode];// 当前配载点前的所有提货点(若本身也是提货点，含本身)

    $.map($("#tmDispatchOrderSiteList tr"), function () {
        var index = $(this).data('index');
        if (index < idx) {
            var receiveShip = $("#tmDispatchOrderSiteList" + index + "_receiveShip").val();
            if (receiveShip.indexOf("R") !== -1) {
                rcvOutletCodes.push($("#tmDispatchOrderSiteList" + idx + "_outletCode").val());
            }
        }
    });

    var searchParam = $("#" + page + ">.searchForm").serializeJSON();
    searchParam.dataType = page;
    searchParam.dispatchNo = dispatchNo;
    searchParam.dispatchSiteOutletCode = dispatchSiteOutletCode;
    searchParam.baseOrgId = baseOrgId;
    searchParam.orgId = orgId;
    searchParam.carrierCode = carrierCode;
    searchParam.rcvOutletCodes = rcvOutletCodes.join(',');
    return searchParam;
}

/*已选Tab查询参数*/
function selectRightParams(idx) {
    var searchParam = {};
    searchParam.dispatchNo = $("#dispatchNo").val();
    searchParam.dispatchSiteOutletCode = $("#tmDispatchOrderSiteList" + idx + "_outletCode").val();
    searchParam.orgId = $("#orgId").val();
    searchParam.baseOrgId = $("#baseOrgId").val();
    return searchParam;
}

/*刷新加载可选/已选表格数据*/
function refreshSelectTab(page) {
    setTimeout(function () {
        var $page = $("#" + page);
        $page.find("table[class~='left-tab']").eq(0).bootstrapTable('refresh', {url: "${ctx}/tms/order/dispatch/site/opt/data"});
        $page.find("table[class~='right-tab']").eq(0).bootstrapTable('refresh', {url: "${ctx}/tms/order/dispatch/site/opted/data"});
    }, 200);
}

/*打开已选订单窗口*/
function openSelectedModal(idx) {
    var $modal = $("#selectedModal");
    $modal.data("idx", idx);
    $modal.find("span[class~='dispatchSite']").text($("#tmDispatchOrderSiteList" + idx + "_outletName").val());
    // 初始化表格
    initSelectedTable();

    // CSS样式
    $("#selectedModal .modal-dialog").css('width', Math.floor(document.body.clientWidth * 0.90));
    $("#selectedModal .modal-body").css('height', Math.floor(document.body.clientHeight * 0.8));

    // 显示窗口
    $modal.modal({backdrop: 'static', keyboard: false});
}

/*初始化已选订单窗口表格*/
function initSelectedTable() {
    var $tab = $("#selectedTable");
    $tab.bootstrapTable('destroy');
    $tab.bootstrapTable({
        url: "${ctx}/tms/order/dispatch/site/opted/data",
        cache: false,//是否使用缓存，默认为true
        sortName: 'labelNo',// 默认按字段labelNo排序
        pagination: true,//是否显示分页
        sidePagination: "client",//分页方式：client客户端分页，server服务端分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = selectRightParams($("#selectedModal").data("idx"));
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        formatNoMatches: function () {
            return "";
        },
        columns: [{
            checkbox: true
        }, {
            field: 'labelNo',
            title: '标签',
            sortable: true
        }, {
            field: 'transportNo',
            title: '运输单号',
            sortable: true
        }, {
            field: 'customerName',
            title: '客户',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品',
            sortable: true
        }, {
            field: 'nowOutletName',
            title: '发货网点',
            sortable: true
        }, {
            field: 'nextOutletName',
            title: '计划送往网点',
            sortable: true
        }, {
            field: 'receiveShip',
            title: '提货/送货',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_RECEIVE_SHIP'))}, value, "-");
            }
        }, {
            field: 'nowCity',
            title: '发货城市',
            sortable: true
        }, {
            field: 'nextCity',
            title: '计划送往城市',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        }, {
            field: 'principalName',
            title: '委托方',
            sortable: true
        }]
    });
}

/*初始化标签*/
function initLabel() {
    var $orderLabelTable = $("#tmDispatchOrderLabelTable");
    $orderLabelTable.bootstrapTable('destroy');
    $orderLabelTable.bootstrapTable({
        cache: false,//是否使用缓存，默认为true
        pagination: true,//是否显示分页
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = {};
            searchParam.dispatchNo = $("#dispatchNo").val();
            searchParam.receiveShip = 'R';
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onLoadSuccess: function (data) {
            $("#labelNum").text(data.total);
        },
        onLoadError: function (data) {
            $("#labelNum").text(0);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'labelNo',
            title: '标签',
            sortable: true
        }, {
            field: 'transportNo',
            title: '运输单号',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        }, {
            field: 'principalName',
            title: '委托方',
            sortable: true
        }, {
            field: 'customerName',
            title: '客户',
            sortable: true
        }, {
            field: 'shipName',
            title: '发货方',
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
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'qty',
            title: '数量',
            sortable: true
        }, {
            field: 'def1',
            title: '栈板号',
            sortable: true
        }, {
            field: 'def2',
            title: '框号',
            sortable: true
        }, {
            field: 'remarks',
            title: '是否App录入',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }]
    });
}

/*显示标签*/
function showLabel() {
    $("#tmDispatchOrderLabelTable").bootstrapTable('refresh', {url: "${ctx}/tms/order/dispatch/label/page"});
}

/*初始化标签*/
function initTransport() {
    var $transportTable = $("#transportTable");
    $transportTable.bootstrapTable('destroy');
    $transportTable.bootstrapTable({
        cache: false,//是否使用缓存，默认为true
        pagination: true,//是否显示分页
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = {};
            searchParam.dispatchNo = $("#dispatchNo").val();
            searchParam.receiveShip = 'R';
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onLoadSuccess: function (data) {
            $("#transportNum").text(data.total);
        },
        onLoadError: function (data) {
            $("#transportNum").text(0);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'transportNo',
            title: '运输单号',
            sortable: true
        }, {
            field: 'customerNo',
            title: '客户单号',
            sortable: true
        }, {
            field: 'orderTime',
            title: '受理时间',
            sortable: true
        }, {
            field: 'receiveOutletName',
            title: '揽收机构',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货方',
            sortable: true
        }, {
            field: 'consigneeCity',
            title: '目的地城市',
            sortable: true
        }, {
            field: 'consignee',
            title: '收货人',
            sortable: true
        }, {
            field: 'consigneeTel',
            title: '收货人电话',
            sortable: true
        }, {
            field: 'consigneeAddress',
            title: '收货地址',
            sortable: true
        }, {
            field: 'shipName',
            title: '发货方',
            sortable: true
        }, {
            field: 'shipCity',
            title: '发货城市',
            sortable: true
        }, {
            field: 'shipper',
            title: '发货人',
            sortable: true
        }, {
            field: 'shipperTel',
            title: '发货人电话',
            sortable: true
        }, {
            field: 'shipAddress',
            title: '发货人地址',
            sortable: true
        }, {
            field: 'orderDelivery.totalEaQty',
            title: '数量',
            sortable: true
        }, {
            field: 'orderDelivery.totalWeight',
            title: '重量',
            sortable: true
        }, {
            field: 'orderDelivery.totalCubic',
            title: '体积',
            sortable: true
        }]
    });
}

/*显示标签*/
function showTransport() {
    $("#transportTable").bootstrapTable('refresh', {url: "${ctx}/tms/order/tmDispatchOrder/transport/page"});
}


/******************************************附件start***********************************************************/
function annex() {
    $("#annexModal").modal({backdrop: 'static', keyboard: false});
    initAnnexTable();
}

function initAnnexTable() {
    $("#annexTable").bootstrapTable('destroy');
    $("#annexTable").bootstrapTable({
        height: 450,
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/sys/annex/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.type = 5;
            searchParam.pkId = $('#id').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'fileName',
            title: '附件名称'
        }, {
            field: 'fileSize',
            title: '附件大小'
        }, {
            field: 'uploadBy.name',
            title: '上传人'
        }, {
            field: 'uploadDate',
            title: '上传时间'
        }, {
            field: 'operate',
            title: '操作',
            align: 'center',
            formatter: function operateFormatter(value, row, index) {
                return [
                    '<a href="${ctx}/sys/annex/download?id=' + row.id + '" class="btn btn-primary">下载附件 </a>',
                ].join('');
            }
        }]
    });
}

function uploadAnnex() {
    $("#uploadFileModal").modal({backdrop: 'static', keyboard: false});
    $("#uploadFile").val('');
}

function removeAnnex() {
    var ids = $.map($("#annexTable").bootstrapTable('getSelections'), function (row) {
        return row.id;
    });
    if (ids.length <= 0) {
        jp.warning("请选择记录");
        return;
    }
    jp.loading();
    jp.post("${ctx}/sys/annex/delete", {ids: ids.join(",")}, function (data) {
        $("#annexTable").bootstrapTable('refresh');
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function uploadFileFnc() {
    jp.loading("上传中...");
    var formData = new FormData();
    formData.append("type", 5);
    formData.append("pkId", $('#id').val());
    var files = $("#uploadFile")[0].files;
    for (var i = 0; i < files.length; i++) {
        formData.append("files", files[i]);
    }
    $.ajax({
        url: "${ctx}/sys/annex/upload",
        type: "post",
        cache: false,
        contentType: false,
        processData: false,
        data: formData,
        success: function (data) {
            if (data.success) {
                jp.success(data.msg);
                $("#uploadFileModal").modal('hide');
                $("#annexTable").bootstrapTable('refresh');
            } else {
                jp.error(data.msg);
            }
        }, error: function (xhr, textStatus, errorThrown) {
            jp.error("附件大小超出限制");
        }
    });
}
/******************************************附件end***********************************************************/

</script>