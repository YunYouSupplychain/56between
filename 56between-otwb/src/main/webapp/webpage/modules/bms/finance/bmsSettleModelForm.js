<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
var detailTpl, isShowRight = false;
$(document).ready(function () {
    detailTpl = $("#detailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $("#effectiveDateFm").datetimepicker({format: "YYYY-MM-DD"});
    $("#effectiveDateTo").datetimepicker({format: "YYYY-MM-DD"});
    if (!$("#id").val()) {
        $("#orgId").val(jp.getCurrentOrg().orgId);
    } else {
        $("#settleObjectSBtnId").prop("disabled", true);
        $("#settleObjectDBtnId").prop("disabled", true);
        $("#generate").attr("disabled", false);
    }

    buildDetails();
    $("#bmsSettleModelTermsTable").on("check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table", function () {
        var selections = $("#bmsSettleModelTermsTable").bootstrapTable("getSelections");
        $("#del").attr("disabled", !selections.length);
    });
    $("#bmsSettleModelTermsTable").on("load-success.bs.table", function (data) {
        showOne();
    });
});

function save() {
    var validate = bq.headerSubmitCheck("#inputForm");
    if (validate.isSuccess) {
        jp.post("${ctx}/bms/finance/bmsSettleModel/save", $("#inputForm").serialize(), function (data) {
            if (data.success) {
                window.location = "${ctx}/bms/finance/bmsSettleModel/form?id=" + data.body.entity.id;
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        });
    } else {
        jp.bqError(validate.msg);
    }
}

function getIdSelections() {
    return $.map($("#bmsSettleModelTermsTable").bootstrapTable("getSelections"), function (row) {
        return row.id;
    });
}

function addRow(list, idx, tpl, row) {
    $(list).append(Mustache.render(tpl, {
        idx: idx, delBtn: true, row: row
    }));
    $(list + idx).find("select").each(function () {
        $(this).val($(this).attr("data-value"));
    });
}

function delRow() {
    jp.confirm("确认要删除该记录吗？", function () {
        var idx = jp.loading();
        jp.post("${ctx}/bms/finance/bmsSettleModel/deleteDetail?ids=" + getIdSelections(), {}, function (data) {
            if (data.success) {
                jp.close(idx);
                $("#bmsSettleModelTermsTable").bootstrapTable("refresh");
            } else {
                jp.bqError(data.msg);
            }
        });
    });
}

function generate() {
    var validate = bq.headerSubmitCheck("table[class~='head']");
    if (!validate.isSuccess) {
        jp.bqError(validate.msg);
        return;
    }
    var id = $("#id").val();
    var settleObjectCode = $("#settleObjectCode").val();
    var orgId = $("#orgId").val();
    var url = "${ctx}/bms/finance/bmsSettleModel/selectContractSubject?id=" + id + "&settleObjectCode=" + settleObjectCode + "&orgId=" + orgId;
    jp.openDialog("合同科目", url, "80%", "80%", $("#bmsSettleModelTermsTable"));
}

function buildDetails() {
    $("#bmsSettleModelTermsTable").bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/finance/bmsSettleModel/findDetail",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.settleModelCode = $("#settleModelCode").val();
            searchParam.orgId = $("#orgId").val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            showDetails(row);
            jp.changeTableStyle($el);
        },
        onDblClickRow: function (row, $el) {// 双击行触发
            showOrHide(row);
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'sysContractNo',
            title: '系统合同编号',
            sortable: true
        }, {
            field: 'contractNo',
            title: '客户合同编号',
            sortable: true
        }, {
            field: 'subcontractNo',
            title: '子合同编号',
            sortable: true
        }, {
            field: 'billModule',
            title: '费用模块',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_MODULE'))}, value, "-");
            }
        }, {
            field: 'billSubjectName',
            title: '费用科目名称',
            sortable: true
        }, {
            field: 'receivablePayable',
            title: '应收应付',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_RECEIVABLE_PAYABLE'))}, value, "-");
            }
        }, {
            field: 'billCategory',
            title: '费用类别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_SUBJECT_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'billTermsDesc',
            title: '计费条款说明',
            sortable: true
        }, {
            field: 'outputObjects',
            title: '条款输出对象',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_OUTPUT_OBJECT'))}, value, "-");
            }
        }, {
            field: 'occurrenceQuantity',
            title: '发生量',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_OCCURRENCE_QUANTITY'))}, value, "-");
            }
        }, {
            field: 'formulaName',
            title: '计费公式',
            sortable: true
        }, {
            field: 'billSubjectCode',
            title: '费用科目代码',
            sortable: true
        }, {
            field: 'billTermsCode',
            title: '计费条款代码',
            sortable: true
        }, {
            field: 'orgName',
            title: '机构',
            sortable: true
        }]
    });
}

function refresh() {
    $("#bmsSettleModelTermsTable").bootstrapTable('refresh');
}

function showOne() {
    var $tab = $("#bmsSettleModelTermsTable");
    var rows = $tab.bootstrapTable("getData");
    if (rows.length > 0) {
        isShowRight = true;
        showOrHide(rows[0]);
        jp.changeTableStyle($tab.find("tbody tr").eq(0));
    } else {
        isShowRight = false;
        showOrHide();
    }
}

function showOrHide(row) {
    if (isShowRight) {
        showDetails(row);
        $('#tab-detail-right').show();
        $("#tab-detail-left").addClass("div-left");
    } else {
        $("#detailInfo").empty();
        $("#includeParams").empty();
        $("#excludeParams").empty();
        $('#tab-detail-right').hide();
        $("#tab-detail-left").removeClass("div-left");
    }
    $('#confirm').attr('disabled', !isShowRight);
    isShowRight = !isShowRight;
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

function showDetails(row) {
    $("#detailInfo").empty();
    $("#includeParams").empty();
    $("#excludeParams").empty();
    $("#detailInfo").append(Mustache.render(detailTpl, {row: row}));
    $("#detailInfo").find("select").each(function () {
        $(this).val($(this).data("value"));
    });

    if (row.hasOwnProperty("includeParams")) {
        createParamForm("#includeParams", row.includeParams);
    }
    if (row.hasOwnProperty("excludeParams")) {
        createParamForm("#excludeParams", row.excludeParams);
    }
}

function createParamForm(table, rows) {
    for (var i = 0; i < rows.length; i++) {
        // 检查table最后一个tr子元素
        var tdCount = $(table).find("tr:last-child>td").length;
        if (tdCount === 0 || tdCount === 9) {// 一行中第一个td为隐藏td，8个显示td
            $(table).append("<tr><td class='hide'></td></tr>");
        }
        var $curTr = $(table).find("tr:last-child");
        var $curHideTd = $curTr.find("td:first-child");
        var data = rows[i];
        var list = data.hasOwnProperty("includeOrExclude") && data.includeOrExclude === '0' ? "includeParams" : "excludeParams";
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

function paramSubmit() {
    jp.loading();
    jp.post("${ctx}/bms/finance/bmsSettleModel/saveDetailParameter", $("#detailForm").serialize(), function (data) {
        if (data.success) {
            $("#bmsSettleModelTermsTable").bootstrapTable('refresh');
            jp.success("保存成功");
        } else {
            jp.bqError(data.msg);
        }
    });
}

</script>