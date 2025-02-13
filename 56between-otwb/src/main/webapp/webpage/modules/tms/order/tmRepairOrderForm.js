<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var tmRepairOrderDetailRowIdx = 0, tmRepairOrderDetailTpl;
$(document).ready(function () {
    $("#orderTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    tmRepairOrderDetailTpl = $("#tmRepairOrderDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");

    initV();
    initImg();
    editController();
    $("#orderDetailTable").find("input[name='btSelectAll']").on('click', function () {
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
});

function initV() {
    if ($("#id").val().length > 0) {
        initDetail();
    } else {
        $("#orgId").val(jp.getCurrentOrg().orgId);
        $("#baseOrgId").val(tmOrg.id);
        $("#status").val("00");// 创建
        $("#orderTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
    }
}

function initDetail() {
    $('#tmRepairOrderDetailList').empty();
    jp.post("${ctx}/tms/order/tmRepairOrder/detail/data", {repairNo: $('#repairNo').val(), orgId: $('#orgId').val()}, function (data) {
        if (data) {
            for (var i = 0; i < data.length; i++) {
                addDetail(data[i]);
            }
        }
    });
}

/*初始化图片列表*/
function initImg() {
    var $imgTable = $("#tmRepairOrderImgTable");
    $imgTable.bootstrapTable('destroy');
    $imgTable.bootstrapTable({
        cache: false,//是否使用缓存，默认为true
        pagination: true,//是否显示分页
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = {};
            searchParam.orderNo = $("#repairNo").val();
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
                // return "<a href=\""+row.fileUrl+"\" url=\""+row.fileUrl+"\" target=\"_blank\">"+value+"</a>";
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
    $("#tmRepairOrderImgTable").bootstrapTable('refresh', {url: "${ctx}/tms/order/tmRepairOrder/img/data"});
}

/*打开图片*/
function openPic(fileUrl) {
    window.open(fileUrl);
}

function editController() {
    var isNew = $("#id").val().length <= 0;// 是否是新增未保存数据
    var status = $('#status').val();
    if (status === "00") {
        $('#orderDetail_add').attr('disabled', isNew);
        $("#orderDetail_save").attr('disabled', isNew);
        $("#orderDetail_remove").attr('disabled', isNew);
        $('#imgList').attr('disabled', isNew);
    }
}

/*保留报修信息*/
function save1() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    var data = $('#inputForm').serialize();
    bq.closeDisabled(objs);
    jp.post("${ctx}/tms/order/tmRepairOrder/saveForUnRepair", data, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/order/tmRepairOrder/form?id=" + data.body.id;
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*保留维修信息*/
function save2() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm2");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm2");
    var data = $('#inputForm2').serializeJSON();
    data.id = $('#id').val();
    bq.closeDisabled(objs);
    jp.post("${ctx}/tms/order/tmRepairOrder/saveForRepair", data, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/order/tmRepairOrder/form?id=" + data.body.id;
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

function carSelect(row) {
    if (row) {
        if (row.hasOwnProperty("mainDriver")) {
            $('#driver').val(row.mainDriver);
        }
        if (row.hasOwnProperty("mainDriverName")) {
            $('#driverName').val(row.mainDriverName);
        }
    }
}

function repairSelect(row, idx) {
    if (row) {
        $('#tmRepairOrderDetailList' + idx + '_repairPrice').val(row.repairPrice);
        costChange(idx);
    }
}

function skuSelect(row, idx) {
    if (row) {
        $('#tmRepairOrderDetailList' + idx + '_skuModel').val(row.fittingModel);
        $('#tmRepairOrderDetailList' + idx + '_price').val(row.price);
        amountChange(idx);
    }
}

function addDetail(row) {
    if (row === undefined) {
        row = {repairNo: $("#repairNo").val(), ownerCode: $("#ownerCode").val(), repairPrice: 0, qty: 0, price: 0, amount: 0, workHour: 0, workHourCost: 0, totalAmount: 0, orgId: $("#orgId").val(), recVer: 0, baseOrgId: $("#baseOrgId").val()};
    }
    addRow('#tmRepairOrderDetailList', tmRepairOrderDetailRowIdx, tmRepairOrderDetailTpl, row);
    tmRepairOrderDetailRowIdx = tmRepairOrderDetailRowIdx + 1;
}

function saveDetail() {
    jp.loading();
    var validator = bq.tableValidate("#orderDetailForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var id = $("#id").val();
    var repairNo = $("#repairNo").val();
    var orgId = $("#orgId").val();

    var params = bq.serializeJson($('#orderDetailForm'));
    params.id = id;
    params.repairNo = repairNo;
    params.orgId = orgId;
    jp.post("${ctx}/tms/order/tmRepairOrder/detail/save", params, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/order/tmRepairOrder/form?id=" + id;
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

function delDetail() {
    delRow('#tmRepairOrderDetailList', "${ctx}/tms/order/tmRepairOrder/detail/remove");
}

function addRow(list, idx, tpl, row) {
    $(list).append(Mustache.render(tpl, {
        idx: idx, delBtn: true, row: row
    }));
    $(list + idx).find("select").each(function () {
        $(this).val($(this).attr("data-value"));
    });
    $(list + idx).find("input[name='btSelectItem']").on('click', function () {
        var $table = $(this).parents("table").eq(0);
        $table.find("input[name='btSelectAll']").prop('checked', $table.find("input[name='btSelectItem']").not("input:checked").length <= 0);
    });
}

function delRow(list, url) {
    jp.loading();
    var ids = [];// 获取选中行ID
    var idxs = [];// 获取选中行索引
    $.map($(list).find("tr input[type='checkbox']:checked"), function ($element) {
        var idx = $($element).data("index");
        idxs.push(idx);
        var id = $(list + idx + "_id").val();
        if (id) {
            ids.push(id);
        }
    });
    if (ids.length <= 0) {
        jp.warning("请选择记录");
        return;
    }
    jp.confirm('确认要删除选中记录吗？', function () {
        del = function (indexs) {// 页面表格删除
            $.map(indexs, function (idx) {
                $(list + idx).remove();
            });
            jp.success("操作成功");
        };
        if (url && ids.length > 0) {
            jp.post(url, {repairNo: $('#repairNo').val(), ids: ids.join(',')}, function (data) {
                if (data.success) {
                    del(idxs);
                    window.location = "${ctx}/tms/order/tmRepairOrder/form?id=" + $('#id').val();
                } else {
                    jp.error(data.msg);
                }
            });
        } else {
            del(idxs);
        }
    });
}

function amountChange(idx) {
    var qty = Number($('#tmRepairOrderDetailList' + idx + '_qty').val());
    var price = Number($('#tmRepairOrderDetailList' + idx + '_price').val());
    var amount = qty * price;
    $('#tmRepairOrderDetailList' + idx + '_amount').val(amount);
    totalAmountChange(idx);
}

function costChange(idx) {
    var workHour = Number($('#tmRepairOrderDetailList' + idx + '_workHour').val());
    var repairPrice = Number($('#tmRepairOrderDetailList' + idx + '_repairPrice').val());
    var workHourCost = workHour * repairPrice;
    $('#tmRepairOrderDetailList' + idx + '_workHourCost').val(workHourCost);
    totalAmountChange(idx);
}

function totalAmountChange(idx) {
    var amount = Number($('#tmRepairOrderDetailList' + idx + '_amount').val());
    var workHourCost = Number($('#tmRepairOrderDetailList' + idx + '_workHourCost').val());
    var totalAmount = amount + workHourCost;
    $('#tmRepairOrderDetailList' + idx + '_totalAmount').val(totalAmount);
}

function imgList() {
    if (!$('#id').val()) {
        jp.bqError("单头未保存，不能操作！");
        return;
    }
    showImg();
    $('#imgInfoModal').modal();
}
</script>