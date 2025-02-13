<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    if ($("#id").val()) {
        createParamForm("#includeParams", ${fns:toJson(bmsContractCostItemEntity.includeParams)});
        createParamForm("#excludeParams", ${fns:toJson(bmsContractCostItemEntity.excludeParams)});
    }
});

function doSubmit($table, $topIndex) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
    jp.loading();
    var validate = bq.headerSubmitCheck("#inputForm");
    if (!validate.isSuccess) {
        jp.bqError(validate.msg);
        return false;
    }
    var disabledObjs = bq.openDisabled("#inputForm");
    var params = $('#inputForm').serialize();
    bq.closeDisabled(disabledObjs);
    jp.post("${ctx}/bms/basic/bmsContract/saveCostItem", params, function (data) {
        if (data.success) {
            $table.bootstrapTable('refresh');
            jp.success(data.msg);
            jp.close($topIndex);
        } else {
            jp.alert(data.msg);
        }
    });
    return true;
}

function billModuleChange() {
    $('#billSubjectCode').val('');
    $('#billSubjectName').val('');
    $('#billTermsCode').val('');
    $('#billTermsDesc').val('');
}

function billSubjectAfterSelect(row) {
    if (row) {
        $('#billSubjectName').val(row.billSubjectName);
        if (!$('#billModule').val()) {
            $('#billModule').val(row.billModule);
        }
    }
}

function billTermsAfterSelect(row) {
    if (row) {
        if (!$('#billModule').val()) {
            $('#billModule').val(row.billModule);
        }
        var settleObjectCode = $('#settleObjectCode').val();
        var orgId = $('#orgId').val();
        jp.get("${ctx}/bms/basic/bmsBillTerms/getTermsParams?billTermsCode=" + row.billTermsCode + "&settleObjectCode=" + encodeURIComponent(settleObjectCode) + "&orgId=" + orgId, function (data) {
            if (!data) {
                return;
            }
            var includeParams = [], excludeParams = [];
            for (var i = 0; i < data.length; i++) {
                includeParams.push(data[i]);
                excludeParams.push($.extend({}, data[i], {fieldValue: ""}));
            }
            createParamForm("#includeParams", includeParams);
            createParamForm("#excludeParams", excludeParams);
        });
    } else {
        createParamForm("#includeParams", []);
        createParamForm("#excludeParams", []);
    }
}

function createParamForm(table, rows) {
    if (!rows || !rows instanceof Array) {
        return;
    }
    $(table).empty();
    for (var i = 0; i < rows.length; i++) {
        // 检查table最后一个tr子元素
        var tdCount = $(table).find("tr:last-child>td").length;
        if (tdCount === 0 || tdCount === 9) {// 一行中第一个td为隐藏td，8个显示td
            $(table).append("<tr><td class='hide'></td></tr>");
        }
        var $curTr = $(table).find("tr:last-child");
        var $curHideTd = $curTr.find("td:first-child");
        var data = rows[i];
        var list = table === "#includeParams" ? "includeParams" : "excludeParams";
        var isShow = data.isEnable === 'Y' && data.isShow === 'Y';
        var html = "", id, name, value;

        if (!data.hasOwnProperty("fieldValue")) {
            data.fieldValue = "";
        }
        for (var key in data) {
            id = list + i + '_' + key;
            name = list + '[' + i + '].' + key;
            value = data[key];
            if (value instanceof Object) {
                continue;
            }
            if (isShow && key === "fieldValue") {
                html += createLabel(data.title);
                if (data.type === "SELECT") {
                    html += '<td class="width-15">' + createSelect(id, name, value, data.dictValueList) + '</td>';
                } else if (data.type === "DATE") {
                    html += '<td class="width-15">' + createDate(id, name, value) + '</td>';
                } else {
                    html += '<td class="width-15">' + createInput(true, id, name, value) + '</td>';
                }
            } else {
                $curHideTd.append(createInput(false, id, name, value))
            }
        }
        $curTr.append(html);

        if (isShow) {
            $.each($curTr.find("select"), function () {
                $(this).val($(this).data("value"));
            });
            $.each($curTr.find(".form_datetime"), function () {
                $(this).datetimepicker({format: "YYYY-MM-DD"});
            });
        }
    }
    var lastTrTdNum = $(table).find("tr:last-child>td").length;
    if (0 < lastTrTdNum && lastTrTdNum < 9) {
        // 补充空白
        var $lastTr = $(table).find("tr:last-child");
        for (var z = 1; z <= 9 - lastTrTdNum; z += 2) {
            $lastTr.append('<td class="width-10"></td>');
            $lastTr.append('<td class="width-15"></td>');
        }
    }
}

function createLabel(title) {
    return '<td class="width-10"><label class="pull-right">' + title + '</label></td>';
}

function createInput(isShow, id, name, value) {
    if (isShow) {
        return '<input type="text" class="form-control" id="' + id + '" name="' + name + '" value="' + value + '"/>';
    } else {
        return '<input type="hidden" id="' + id + '" name="' + name + '" value="' + value + '"/>';
    }
}

function createSelect(id, name, value, dictValueList) {
    var options = "";
    if (dictValueList !== undefined && dictValueList.length > 0) {
        for (var i = 0; i < dictValueList.length; i++) {
            options += '<option value="' + dictValueList[i].value + '">' + dictValueList[i].label + '</option>'
        }
    }
    return '<select id="' + id + '" name="' + name + '" data-value="' + value + '" class="form-control"><option></option>' + options + '</select>';
}

function createDate(id, name, value) {
    return '<div class="input-group form_datetime" id="' + id + '">' +
        '<input type="text" class="form-control" name="' + name + '" value="' + value + '"/>' +
        '<span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>' +
        '</div>'
}
</script>