<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">
var validateForm;
var $table; // 父页面table表格id
var $topIndex;//弹出窗口的 index
var contactsRowIdx = 0, contactsTpl;
$(document).ready(function () {
    validateForm = $("#inputForm").validate({
        submitHandler: function (form) {
            jp.post("${ctx}/sys/common/customer/save", $('#inputForm').serialize(), function (data) {
                if (data.success) {
                    $table.bootstrapTable('refresh');
                    jp.success(data.msg);
                    jp.close($topIndex);//关闭dialog
                } else {
                    jp.error(data.msg);
                }
            })
        },
        errorContainer: "#messageBox",
        errorPlacement: function (error, element) {
            $("#messageBox").text("输入有误，请先更正。");
            if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                error.appendTo(element.parent().parent());
            } else {
                error.insertAfter(element);
            }
        }
    });
    init();
});

function init() {
    if ($('#id').val()) {
        $('#code').prop('readonly', true);
        var isGeneralTaxpayer = '${sysCommonCustomerEntity.isGeneralTaxpayer}';
        $('#isGeneralTaxpayer').prop('checked', isGeneralTaxpayer === 'Y').val(isGeneralTaxpayer);
    } else {
        var $dataSet = ${fns:toJson(fns:getUserDataSet())};
        $('#dataSet').val($dataSet.code);
        $('#dataSetName').val($dataSet.name);
        $('#isGeneralTaxpayer').prop('checked', false).val('N');
    }
    $('#dataSetName').prop('readonly', true);
    $('#dataSetNameSBtnId').prop('disabled', true);
    $('#dataSetNameDBtnId').prop('disabled', true);
    var type = [];
    $("input[name='type']:checked").each(function () {
        type.push($(this).val());
    });
    if (type.indexOf("CARRIER") !== -1) {
        $("#carrierMatchedOrg").attr('readonly', false);
        $("#carrierMatchedOrgSBtnId").removeClass('disabled');
        $("#carrierMatchedOrgDBtnId").prop('disabled', false);
    } else {
        $("#carrierMatchedOrg").attr('readonly', true);
        $("#carrierMatchedOrgSBtnId").addClass('disabled');
        $("#carrierMatchedOrgDBtnId").prop('disabled', true);
    }
    if (type.indexOf("OUTLET") !== -1) {
        $("#outletMatchedOrg").attr('readonly', false);
        $("#outletMatchedOrgSBtnId").removeClass('disabled');
        $("#outletMatchedOrgDBtnId").prop('disabled', false);
    } else {
        $("#outletMatchedOrg").attr('readonly', true);
        $("#outletMatchedOrgSBtnId").addClass('disabled');
        $("#outletMatchedOrgDBtnId").prop('disabled', true);
    }
    if (type.indexOf("REPAIR") !== -1) {
        $("#repairPrice").prop('readonly', false);
    } else {
        $("#repairPrice").prop('readonly', true);
    }
    if (type.indexOf("SETTLEMENT") !== -1) {
        $("#settleName").attr('readonly', true);
        $("#settleNameSBtnId").addClass('disabled');
        $("#settleNameDBtnId").prop('disabled', true);
    } else {
        $("#settleName").attr('readonly', false);
        $("#settleNameSBtnId").removeClass('disabled');
        $("#settleNameDBtnId").prop('disabled', false);
    }
    $(".i-checks").on('ifChecked', function () {
        var v = $(this).val();
        if("CARRIER" === v){
            $("#carrierMatchedOrgId").val("");
            $("#carrierMatchedOrg").attr('readonly', false).val("");
            $("#carrierMatchedOrgSBtnId").removeClass('disabled');
            $("#carrierMatchedOrgDBtnId").prop('disabled', false);
        } else if("OUTLET" === v){
            $("#outletMatchedOrgId").val("");
            $("#outletMatchedOrg").attr('readonly', false).val("");
            $("#outletMatchedOrgSBtnId").removeClass('disabled');
            $("#outletMatchedOrgDBtnId").prop('disabled', false);
        } else if ("REPAIR" === v) {
            $("#repairPrice").prop('readonly', false).val("");
        } else if ("SETTLEMENT" === v){
            $("#settleCode").val("");
            $("#settleName").attr('readonly', true).val("");
            $("#settleNameSBtnId").addClass('disabled');
            $("#settleNameDBtnId").prop('disabled', true);
        }
    }).on('ifUnchecked', function () {
        var v = $(this).val();
        if("CARRIER" === v){
            $("#carrierMatchedOrgId").val("");
            $("#carrierMatchedOrg").attr('readonly', true).val("");
            $("#carrierMatchedOrgSBtnId").addClass('disabled');
            $("#carrierMatchedOrgDBtnId").prop('disabled', true);
        } else if("OUTLET" === v){
            $("#outletMatchedOrgId").val("");
            $("#outletMatchedOrg").attr('readonly', true).val("");
            $("#outletMatchedOrgSBtnId").addClass('disabled');
            $("#outletMatchedOrgDBtnId").prop('disabled', true);
        } else if ("REPAIR" === v) {
            $("#repairPrice").prop('readonly', true).val("");
        } else if ("SETTLEMENT" === v){
            $("#settleCode").val("");
            $("#settleName").attr('readonly', false).val("");
            $("#settleNameSBtnId").removeClass('disabled');
            $("#settleNameDBtnId").prop('disabled', false);
        }
    });
    initContacts(${fns:toJson(sysCommonCustomerEntity.contactsList)});
}

function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
    if (validateForm.form()) {
        $table = table;
        $topIndex = index;
        jp.loading();
        $("#inputForm").submit();
        return true;
    }
    return false;
}

function isDefaultChange(list, idx) {
    $(list).find("input[type='checkbox']").each(function () {
        if ($(this).prop('name').indexOf('isDefault') !== -1) {
            $(this).prop('checked', false).val('N');
        }
    });
    $(list + idx + '_isDefault').prop('checked', true).val('Y');
}

function initContacts(rows) {
    contactsTpl = $("#contactsTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $("#contactsTable").find("input[name='btSelectAll']").on('click', function () {// 表格全选复选框绑定事件
        $(this).parents("table").eq(0).find("input[name='btSelectItem']").prop('checked', $(this).prop('checked'));
    });
    if (rows === undefined || rows.length <= 0) return;
    $("#contactsList").empty();

    contactsRowIdx = 0;
    for (var i = 0; i < rows.length; i++) {
        addContacts(rows[i]);
    }
}

function addContacts(row) {
    if (row === undefined) {
        row = {recVer: 0};
    }
    addRow('#contactsList', contactsRowIdx, contactsTpl, row);
    contactsRowIdx = contactsRowIdx + 1;
}

function delContacts() {
    delRow('#contactsList', '${ctx}/sys/common/customer/contacts/deleteAll');
}

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

function isGeneralTaxpayerChange(flag) {
    $('#isGeneralTaxpayer').val(flag ? 'Y' : 'N');
}
</script>