<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    $("#dispatchTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initV();
    editController();
    initLabel();
    initSku();
    initImg();
    showLabel();
    // 默认展示第一个选项卡
    $("#detailTabs a:first").tab('show');
    // 选项卡切换绑定事件
    $("#detailTabs a").off('click').on('click', function (e) {
        e.preventDefault();//阻止a链接的跳转行为
        $(this).tab('show');//显示当前选中的链接及关联的content
        var href = $(this).attr('href');
        if (href === "#tmHandoverOrderLabelInfo") {
            showLabel();
        } else if (href === "#tmHandoverOrderImgInfo") {
            showImg();
        } else if (href === "#tmHandoverOrderSkuInfo") {
            showSku();
        }
    });
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
    if ($("#id").val().length > 0) return;
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $("#baseOrgId").val(tmOrg.id);
    $("#status").val("00");// 创建
}

/*编辑控制*/
function editController() {
    var isNew = $("#id").val().length <= 0;// 是否是新增未保存数据
    var orderStatus = $("#status").val();// 状态
    if ("00" === orderStatus) {// 新建
        $("#save").attr('disabled', false);
    } else if ("10" === orderStatus) {// 部分交接
    } else if ("20" === orderStatus) {// 已交接
        //$('#skuDetail_edit').attr('disabled', true);
    }
    $("#label_show").attr('disabled', isNew);
}

/*车辆选择后*/
function carSelect(data) {
    if (data) {
        $("#driver").val(data.mainDriver);
        $("#driverName").val(data.mainDriverName);
    }
}

/*司机选择后*/
function driverSelect(data) {
    if (data) {
        $("#driverTel").val(data.phone);
    }
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
    jp.post("${ctx}/tms/order/tmHandoverOrder/save", $('#inputForm').serialize(), function (data) {
        bq.closeDisabled(objs);
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/tmHandoverOrder/form?id=" + data.body.entity.id;
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*初始化标签*/
function initLabel() {
    var $orderLabelTable = $("#tmHandoverOrderLabelTable");
    $orderLabelTable.bootstrapTable('destroy');
    $orderLabelTable.bootstrapTable({
        cache: false,//是否使用缓存，默认为true
        pagination: true,//是否显示分页
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = {};
            searchParam.handoverNo = $("#handoverNo").val();
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
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_LABEL_HANDOVER_STATUS'))}, value, "-");
            }
        }, {
            field: 'receiveShip',
            title: '提/送',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_RECEIVE_SHIP'))}, value, "-");
            }
        }]
    });
}

/*显示标签*/
function showLabel() {
    $("#tmHandoverOrderLabelTable").bootstrapTable('refresh', {url: "${ctx}/tms/order/tmHandoverOrder/label/data"});
}

/*初始化商品明细*/
function initSku() {
    var $orderSkuTable = $("#tmHandoverOrderSkuTable");
    $orderSkuTable.bootstrapTable('destroy');
    $orderSkuTable.bootstrapTable({
        cache: false,//是否使用缓存，默认为true
        pagination: true,//是否显示分页
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = {};
            searchParam.handoverNo = $("#handoverNo").val();
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
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
            field: 'ownerName',
            title: '货主',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品',
            sortable: true
        }, {
            field: 'orderQty',
            title: '计划数量',
            sortable: true
        }, {
            field: 'actualQty',
            title: '实际数量',
            sortable: true
        }, {
            field: 'unloadingTime',
            title: '卸货时长',
            sortable: true
        }, {
            field: 'receiveShip',
            title: '提/送',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_RECEIVE_SHIP'))}, value, "-");
            }
        }]
    });
    $orderSkuTable.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $orderSkuTable.bootstrapTable('getSelections').length;
        $('#skuDetail_edit').attr('disabled', length !== 1);
    });
}

function getSkuIdSelections() {
    return $.map($("#tmHandoverOrderSkuTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

/*显示商品*/
function showSku() {
    $("#tmHandoverOrderSkuTable").bootstrapTable('refresh', {url: "${ctx}/tms/order/tmHandoverOrder/sku/data"});
}

function editSku() {
    var rows = $("#tmHandoverOrderSkuTable").bootstrapTable('getSelections');
    evaluate('detail_', rows[0]);
    $("#skuEditModal").modal();
}

/**
 * 赋值
 */
function evaluate(prefix, currentRow) {
    $("input[id^=" + prefix + "]").each(function() {
        var $Id = $(this).attr('id');
        var $Name = $(this).attr('name');
        $('#' + $Id).val(eval("" + 'currentRow' + "." + $Name));
    });
    $("select[id^=" + prefix + "]").each(function() {
        var $Id = $(this).attr('id');
        var $Name = $(this).attr('name');
        $('#' + $Id).val(eval("" + 'currentRow' + "." + $Name));
    });
}

function skuConfirmSave() {
    var validate = bq.headerSubmitCheck('#detailSaveForm');
    if (validate.isSuccess) {
        var disableObjs = bq.openDisabled('#detailSaveForm');
        jp.loading();
        jp.post("${ctx}/tms/order/tmHandoverOrder/sku/edit", $('#detailSaveForm').serialize(), function (data) {
            if (data.success) {
                $('#tmHandoverOrderSkuTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.error(data.msg);
            }
        });
        bq.closeDisabled(disableObjs);
        $("#skuEditModal").modal('hide');
    }
}

/*初始化图片列表*/
function initImg() {
    var $imgTable = $("#tmHandoverOrderImgTable");
    $imgTable.bootstrapTable('destroy');
    $imgTable.bootstrapTable({
        cache: false,//是否使用缓存，默认为true
        pagination: true,//是否显示分页
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = {};
            searchParam.orderNo = $("#handoverNo").val();
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'fileName',
            title: '文件名',
            sortable: true,
            formatter: function(value, row, index){
                return "<a href='javascript:openPic(\""+row.fileUrl+"\")'>"+value+"</a>";
            }
        }, {
            field: 'uploadPerson',
            title: '上传人',
            sortable: true
        }, {
            field: 'uploadTime',
            title: '上传时间',
            sortable: true
        }]
    });
}

/*显示图片信息*/
function showImg() {
    $("#tmHandoverOrderImgTable").bootstrapTable('refresh', {url: "${ctx}/tms/order/tmHandoverOrder/img/data"});
}

/*打开图片*/
function openPic(fileUrl) {
    window.open(fileUrl);
}
</script>