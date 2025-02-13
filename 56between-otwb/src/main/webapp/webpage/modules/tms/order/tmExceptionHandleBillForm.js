<%@ page contentType="text/html;charset=UTF-8"%>
<script>
var tmExceptionHandleBillDetailRowIdx = 0, tmExceptionHandleBillDetailTpl;
var tmExceptionHandleBillFeeRowIdx = 0, tmExceptionHandleBillFeeTpl;
var currUser = ${fns:toJson(fns:getUser())};

$(document).ready(function () {
    tmExceptionHandleBillDetailTpl = $("#tmExceptionHandleBillDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    tmExceptionHandleBillFeeTpl = $("#tmExceptionHandleBillFeeTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $("#registerTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#happenTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initV();
    editController();
    viewPic();

    /*表格全选复选框绑定事件*/
    $("#tmExceptionHandleBillDetailTable").find("input[name='btSelectAll']").on('click', function () {
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
    initExceptionHandleBillDetail(${fns:toJson(tmExceptionHandleBillEntity.tmExceptionHandleBillDetailList)});
    $("#tmExceptionHandleBillFeeTable").find("input[name='btSelectAll']").on('click', function () {
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
    initExceptionHandleBillFee(${fns:toJson(tmExceptionHandleBillEntity.tmExceptionHandleBillFeeList)});
});

/*初始化值*/
function initV() {
    if ($("#id").val().length > 0) return;
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $("#baseOrgId").val(tmOrg.id);

    $("#billStatus").val("00");// 新建
    $('#registerPerson').val(currUser.loginName);
    $("#registerTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
}

/*编辑控制*/
function editController() {
    var isNew = $("#id").val().length <= 0;// 是否是新增未保存数据
    var status = $("#billStatus").val();// 状态
    if ("00" === status) {// 新建
        $("#save").attr('disabled', false);
        $("#detail_add").attr('disabled', isNew);
        $("#detail_save").attr('disabled', isNew);
        $("#detail_remove").attr('disabled', isNew);
        $("#uploadPic").attr('disabled', isNew);
        $("#fee_add").attr('disabled', isNew);
        $("#fee_save").attr('disabled', isNew);
        $("#fee_remove").attr('disabled', isNew);
        $("#customerName").prop('readonly', isNew);
        $("#customerNameSBtnId").prop('disabled', isNew);
        $("#customerNameDBtnId").prop('disabled', isNew);
        $("#happenSysArea").prop('readonly', isNew);
        $("#happenSysAreaButton").prop('disabled', isNew);
        $("#happenSysAreaDelButton").prop('disabled', isNew);
    } else if ("10" === status) {// 已处理
        $("#save").attr('disabled', true);
        $("#detail_add").attr('disabled', true);
        $("#detail_save").attr('disabled', true);
        $("#detail_remove").attr('disabled', true);
        $("#uploadPic").attr('disabled', true);
        $("#fee_add").attr('disabled', true);
        $("#fee_save").attr('disabled', true);
        $("#fee_remove").attr('disabled', true);
    }
}

/*表格增加行*/
function addRow(list, idx, tpl, row) {
    $(list).append(Mustache.render(tpl, {
        idx: idx, delBtn: true, row: row
    }));
    $(list + idx).find("select").each(function () {
        $(this).val($(this).attr("data-value"));
    });
    $(list + idx).find("input[type='checkbox']").each(function () {
        if (!$(this).val()) {
            $(this).val('N');
            return;
        }
        $(this).prop("checked", ("Y" === $(this).val()));
    });
    $(list + idx).find(".form_datetime").each(function () {
        $(this).datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    });
    $(list + idx).find("input[name='btSelectItem']").on('click', function () {
        var $table = $(this).parents("table").eq(0);
        $table.find("input[name='btSelectAll']").prop('checked', $table.find("input[name='btSelectItem']").not("input:checked").length <= 0);
    });
}

/*表格删除行*/
function delRow(list, url) {
    jp.confirm('确认要删除选中记录吗？', function () {
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
        del = function (indexs) {// 页面表格删除
            $.map(indexs, function (idx) {
                $(list + idx).remove();
            });
            jp.success("操作成功");
        };
        if (url && ids.length > 0) {
            jp.post(url, {ids: ids.join(',')}, function (data) {
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

/*保存单头*/
function save() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    jp.post("${ctx}/tms/order/tmExceptionHandleBill/save", $('#inputForm').serialize(), function (data) {
        bq.closeDisabled(objs);
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/tmExceptionHandleBill/form?id=" + data.body.entity.id;
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*初始化明细*/
function initExceptionHandleBillDetail(rows) {
    if (rows === undefined || rows.length <= 0) return;
    $("#tmExceptionHandleBillDetailList").empty();

    tmExceptionHandleBillDetailRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addDetail(rows[i]);
    }
}

/*新增明细*/
function addDetail(row) {
    if (row === undefined) {
        row = {billNo: $("#billNo").val(), handleTime: jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"), handleUser: currUser.loginName, orgId: $("#orgId").val(), recVer: 0, baseOrgId: $("#baseOrgId").val()};
    }
    addRow('#tmExceptionHandleBillDetailList', tmExceptionHandleBillDetailRowIdx, tmExceptionHandleBillDetailTpl, row);
    tmExceptionHandleBillDetailRowIdx = tmExceptionHandleBillDetailRowIdx + 1;
}

/*保存明细*/
function saveDetail() {
    jp.loading();
    var validator = bq.tableValidate("#tmExceptionHandleBillDetailForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var billNo = $("#billNo").val();
    var orgId = $("#orgId").val();

    var params = bq.serializeJson($('#tmExceptionHandleBillDetailForm'));
    params.billNo = billNo;
    params.orgId = orgId;
    jp.post("${ctx}/tms/order/tmExceptionHandleBill/detail/save", params, function (data) {
        if (data.success) {
            jp.post("${ctx}/tms/order/tmExceptionHandleBill/detail/data", {billNo: billNo, orgId: orgId}, function (data) {
                initExceptionHandleBillDetail(data);
            });
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*删除明细*/
function delDetail() {
    delRow('#tmExceptionHandleBillDetailList', '${ctx}/tms/order/tmExceptionHandleBill/detail/delete');
}

/*初始化费用明细*/
function initExceptionHandleBillFee(rows) {
    if (rows === undefined || rows.length <= 0) return;
    $("#tmExceptionHandleBillFeeList").empty();

    tmExceptionHandleBillFeeRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addFeeDetail(rows[i]);
    }
}

/*新增费用明细*/
function addFeeDetail(row) {
    if (row === undefined) {
        row = {billNo: $("#billNo").val(), orgId: $("#orgId").val(), recVer: 0, baseOrgId: $("#baseOrgId").val()};
    }
    addRow('#tmExceptionHandleBillFeeList', tmExceptionHandleBillFeeRowIdx, tmExceptionHandleBillFeeTpl, row);
    tmExceptionHandleBillFeeRowIdx = tmExceptionHandleBillFeeRowIdx + 1;
}

/*保存费用明细*/
function saveFeeDetail() {
    jp.loading();
    var validator = bq.tableValidate("#tmExceptionHandleBillFeeForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var billNo = $("#billNo").val();
    var orgId = $("#orgId").val();

    var params = bq.serializeJson($('#tmExceptionHandleBillFeeForm'));
    params.billNo = billNo;
    params.orgId = orgId;
    jp.post("${ctx}/tms/order/tmExceptionHandleBill/feeDetail/save", params, function (data) {
        if (data.success) {
            jp.post("${ctx}/tms/order/tmExceptionHandleBill/feeDetail/data", {billNo: billNo, orgId: orgId}, function (data) {
                initExceptionHandleBillFee(data);
            });
            jp.success(data.msg);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*删除费用明细*/
function delFeeDetail() {
    delRow('#tmExceptionHandleBillFeeList', '${ctx}/tms/order/tmExceptionHandleBill/feeDetail/delete');
}

function transportSelect(data) {
    if (!data) return;
    $("#customerNo").val(data.customerNo);
    $("#consigneeCode").val(data.consigneeCode);
    $("#consigneeName").val(data.consigneeName);
    $("#customerCode").val(data.customerCode);
    $("#customerName").val(data.customerName);
}

function dispatchSelect(data) {
    if (!data) return;
    $("#carNo").val(data.carNo);
    $("#consigneeCode").val(data.driver);
}

/*************************************图片上传处理**********************/
function openUploadPic(){
    $(function () {
        $("#uploadPicModal").modal();
        $("#uploadFileName").val('');
    });
}

function uploadPic(){
    if($("#uploadFileName").val() == null || $("#uploadFileName").val() == ""){
        jp.alert("请选择需要上传的文件");
    }
    var file = $("#uploadFileName").get(0).files[0];
    var fm = new FormData();
    var billNo = $("#billNo").val();
    var orgId = $("#orgId").val();
    fm.append('billNo', billNo);
    fm.append('orgId', orgId);
    fm.append('file', file);
    $.ajax
    ({ //请求登录处理页
        type: "post",
        url: "${ctx}/tms/order/tmExceptionHandleBill/uploadPic",
        data: fm,
        async: false,
        contentType: false,
        processData: false,
        //传送请求数据
        success: function (result) {
            $("#uploadPicModal").modal('hide');  //手动关闭
            $('#tmExceptionHandleBillAttachmentDetailTable').bootstrapTable('refresh');
            jp.alert(result.msg);
        },
        error: function(data) {
            jp.alert(result.msg)
        }
    });
}

function viewPic(){
    $('#tmExceptionHandleBillAttachmentDetailTable').bootstrapTable('destroy');
    $('#tmExceptionHandleBillAttachmentDetailTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/order/tmExceptionHandleBill/getPicDetail",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orderNo = $('#billNo').val();
            searchParam.orderType = "EXCEPTION";
            searchParam.orgId = $('#orgId').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            field: 'fileName',
            title: '文件名',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href=\"" + row.fileUrl + "\" url=\"" + row.fileUrl + "\" target=\"_blank\">" + row.fileName + "</a>";
            }
        }, {
            field: 'uploadTime',
            title: '上传时间',
            sortable: true
        }, {
            field: 'uploadPerson',
            title: '上传人',
            sortable: true
        }, {
            field: 'uploadPath',
            title: '文件路径',
            sortable: true
        }]
    });
}

function openPic(filePath){
    window.open(filePath);
}
</script>