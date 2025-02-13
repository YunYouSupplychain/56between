<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
var tmSparePoDetailRowIdx = 0, tmSparePoDetailTpl;
$(document).ready(function () {
    tmSparePoDetailTpl = $("#tmSparePoDetailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $("#orderTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    $("#tmSparePoDetailTable").find("input[name='btSelectAll']").on('click', function () {
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
    initV();
    editControl();
    top.layer.closeAll('dialog');// 关闭所有的信息框
});

function initV() {
    var isNew = !$('#id').val();
    if (isNew) {
        $('#orgId').val(jp.getCurrentOrg().orgId);
        $('#baseOrgId').val(tmOrg.id);
        $("#orderTime input").val(jp.dateFormat(new Date(), "YYYY-MM-dd hh:mm:ss"));
        $('#orderStatus').val("00");
        $('#orderType').val("1");
    } else {
        setTimeout(function () {
            initDetail();
            initScanInfoTable();
        }, 200);
    }
}

function editControl() {
    var isNew = !$('#id').val();
    if (isNew) {
        $('#addDetail').attr('disabled', true);
        $('#saveDetail').attr('disabled', true);
        $('#removeDetail').attr('disabled', true);
    } else {
        $('#addDetail').removeAttr('disabled');
        $('#saveDetail').removeAttr('disabled');
        $('#removeDetail').removeAttr('disabled');
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

function save() {
    jp.loading();
    var validator = bq.headerSubmitCheck('#inputForm');
    if (!validator.isSuccess) {
        jp.warning(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    var params = $('#inputForm').serialize();
    bq.closeDisabled(objs);
    jp.post("${ctx}/tms/spare/po/save", params, function (data) {
        if (data.success) {
            window.location = "${ctx}/tms/spare/po/form?id=" + data.body.entity.id;
        } else {
            jp.error(data.msg);
        }
    });
}

function initDetail() {
    tmSparePoDetailRowIdx = 0;
    $('#tmSparePoDetailList').empty();
    jp.post("${ctx}/tms/spare/po/detail/data", {sparePoNo: $('#sparePoNo').val(), orgId: $('#orgId').val()}, function (rows) {
        for (var i = 0; i < rows.length; i++) {
            addDetail(rows[i]);
        }
    });
}

function getNewLineNo() {
    var maxLineNo = 0, digits = 4, tbl = [];
    $.map($('#tmSparePoDetailList .lineNo'), function (obj) {
        var lineNo = Number($(obj).val());
        if (maxLineNo < lineNo) {
            maxLineNo = lineNo;
        }
    });
    var newLineNo = maxLineNo + 1;
    return (0 >= (digits = digits - newLineNo.toString().length)) ? newLineNo : (tbl[digits] || (tbl[digits] = Array(digits + 1).join(0))) + newLineNo;
}

function addDetail(row) {
    if (row === undefined) {
        row = {sparePoNo: $('#sparePoNo').val(), lineNo: getNewLineNo(), orgId: $('#orgId').val(), baseOrgId: $('#baseOrgId').val(), recVer: 0};
    }
    addRow('#tmSparePoDetailList', tmSparePoDetailRowIdx, tmSparePoDetailTpl, row);
    tmSparePoDetailRowIdx = tmSparePoDetailRowIdx + 1;
}

function saveDetail() {
    jp.loading();
    var validator = bq.tableValidate('#tmSparePoDetailForm');
    if (!validator.isSuccess){
        jp.warning(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#tmSparePoDetailForm");
    var params = $('#tmSparePoDetailForm').serialize();
    bq.closeDisabled(objs);
    jp.post("${ctx}/tms/spare/po/detail/save", params, function (data) {
        initDetail();
        if (data.success) {
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}

function removeDetail() {
    delRow('#tmSparePoDetailList', "${ctx}/tms/spare/po/detail/deleteAll");
}

function initScanInfoTable() {
    $('#tmSparePoScanInfoTable').bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/spare/po/scan/data",
        //查询参数,每次调用是会带上这个参数，可自定义
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.sparePoNo = $('#sparePoNo').val();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'sparePoNo',
            title: '入库单号',
            sortable: true
        }, {
            field: 'lineNo',
            title: '行号',
            sortable: true
        }, {
            field: 'orderType',
            title: '类型',
            sortable: true
        }, {
            field: 'fittingName',
            title: '备件',
            sortable: true
        }, {
            field: 'barcode',
            title: '条码',
            sortable: true
        }, {
            field: 'operateTime',
            title: '入库时间',
            sortable: true
        }, {
            field: 'supplierName',
            title: '供应商',
            sortable: true
        }, {
            field: 'price',
            title: '单价',
            sortable: true
        }, {
            field: 'operator',
            title: '操作人',
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
            title: '更新人',
            sortable: true
        }, {
            field: 'updateDate',
            title: '更新时间',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
}

</script>