<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var parameterRowIdx, parameterTpl;
$(document).ready(function () {
    parameterRowIdx = 0;
    parameterTpl = $("#parameterTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    initParameters(${fns:toJson(bmsBillTermsEntity.parameters)});

    $("input[type='checkbox']").change(function () {
        if ($(this).prop("checked")) {
            $(this).val('Y');
        } else {
            $(this).val('N');
        }
    });
    if ($("#id").val()) {
        $("#billTermsCode").prop("readonly", true);
        /*$("#billTermsDesc").prop("readonly", true);
        $("#billModule").prop("disabled", true);
        $("#methodName").prop("disabled", true);
        $("#outputObjects").prop("disabled", true);*/
    }
});

function doSubmit($table, $topIndex) {
    jp.loading();
    var validate = bq.headerSubmitCheck("#inputForm");
    if (!validate.isSuccess) {
        jp.bqError(validate.msg);
        return false;
    }
    var disabledObjs = bq.openDisabled("#inputForm");
    var params = $('#inputForm').serialize();
    bq.closeDisabled(disabledObjs);
    jp.post("${ctx}/bms/basic/bmsBillTerms/save", params, function (data) {
        if (data.success) {
            $table.bootstrapTable('refresh');
            jp.success(data.msg);
            jp.close($topIndex);//关闭dialog
        } else {
            jp.bqError(data.msg);
        }
    });
    return true;
}

function initParameters(rows) {
    if (rows === undefined || rows.length <= 0) {
        return;
    }
    parameterRowIdx = 0
    for (var i = 0; i < rows.length; i++) {
        addRow(rows[i]);
    }
    $("#parameterTable").find("input[name='btSelectAll']").on('click', function () {// 表格全选复选框绑定事件
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
}

function addRow(row) {
    if (row === undefined) {
        row = {billTermsCode: $('#billTermsCode').val(), recVer: 0};
    }
    $('#parameters').append(Mustache.render(parameterTpl, {idx: parameterRowIdx, row: row}));
    $('#parameters' + parameterRowIdx).find("select").each(function () {
        $(this).val($(this).attr("data-value"));
    });
    $('#parameters' + parameterRowIdx).find("input[type='checkbox']").each(function () {
        if (!$(this).val()) {
            $(this).val('N');
            return;
        }
        $(this).prop("checked", ('Y' == $(this).val()));
    });
    $('#parameters' + parameterRowIdx).find("input[name='btSelectItem']").on('click', function () {
        var $table = $(this).parents("table").eq(0);
        $table.find("input[name='btSelectAll']").prop('checked', $table.find("input[name='btSelectItem']").not("input:checked").length <= 0);
    });
    typeChange(parameterRowIdx);
    chxChange(parameterRowIdx, "_isSettleDate");
    parameterRowIdx = parameterRowIdx + 1;
}


function delRow() {
    jp.confirm('确认要删除选中记录吗？', function () {
        jp.loading();
        var ids = [];// 获取选中行ID
        var idxs = [];// 获取选中行索引
        $.map($("#parameters").find("tr input[type='checkbox']:checked"), function ($element) {
            var idx = $($element).data("index");
            idxs.push(idx);
            var id = $("#parameters" + idx + "_id").val();
            if (id) {
                ids.push(id);
            }
        });
        del = function (indexs) {// 页面表格删除
            $.map(indexs, function (idx) {
                $("#parameters" + idx).remove();
            });
            jp.success("操作成功");
        };
        if (ids.length > 0) {
            jp.post("${ctx}/bms/basic/bmsBillTerms/deleteParameter", {ids: ids.join(',')}, function (data) {
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

function chxChange(idx, suffix) {
    if ($('#parameters' + idx + suffix).prop("checked")) {
        $('#parameters' + idx + suffix).val('Y');
    } else {
        $('#parameters' + idx + suffix).val('N');
    }
    if ("_isSettleDate" === suffix) {
        if ($('#parameters' + idx + suffix).prop("checked")) {
            $('#parameters' + idx + '_defaultValue').val('');
            $('#parameters' + idx + '_defaultValue').prop('readonly', true);
            $('#parameters' + idx + '_isShow').val('N');
            $('#parameters' + idx + '_isShow').prop('checked', false);
            $('#parameters' + idx + '_isShow').prop('disabled', true);
            $('.isSettleDate:not("#parameters' + idx + '_isSettleDate")').prop("checked", false);
            $('.isShow:not("#parameters' + idx + '_isShow")').prop("disabled", false);
        } else {
            $('#parameters' + idx + '_isShow').prop('disabled', false);
        }
    }
}

function typeChange(idx) {
    var type = $('#parameters' + idx + '_type').val();
    if ("SELECT" === type) {
        $('#parameters' + idx + '_fieldOption').prop('readonly', false);
        $('#parameters' + idx + '_defaultValue').prop('readonly', false);
        $('#parameters' + idx + '_isShow').prop('disabled', false);
        $('#parameters' + idx + '_isSettleDate').val('N');
        $('#parameters' + idx + '_isSettleDate').prop('checked', false);
        $('#parameters' + idx + '_isSettleDate').prop('disabled', true);
    } else if ("DATE" === type) {
        $('#parameters' + idx + '_fieldOption').val('');
        $('#parameters' + idx + '_fieldOption').prop('readonly', true);
        $('#parameters' + idx + '_defaultValue').val('');
        $('#parameters' + idx + '_defaultValue').prop('readonly', true);
        $('#parameters' + idx + '_isShow').prop('disabled', false);
        $('#parameters' + idx + '_isSettleDate').prop('disabled', false);
    } else if ("TEXT" === type) {
        $('#parameters' + idx + '_fieldOption').val('');
        $('#parameters' + idx + '_fieldOption').prop('readonly', true);
        $('#parameters' + idx + '_defaultValue').prop('readonly', false);
        $('#parameters' + idx + '_isShow').prop('disabled', false);
        $('#parameters' + idx + '_isSettleDate').val('N');
        $('#parameters' + idx + '_isSettleDate').prop('checked', false);
        $('#parameters' + idx + '_isSettleDate').prop('disabled', true);
    }
}
</script>