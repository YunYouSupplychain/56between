<%@ page contentType="text/html;charset=UTF-8" %>
<script>
function doSubmit($table, $topIndex) {
    var validate = bq.headerSubmitCheck("#inputForm");
    if (!validate.isSuccess) {
        jp.bqError(validate.msg);
        return false;
    }
    jp.loading();
    $('#billQty').val(1);
    $('#billStandard').val($('#cost').val());
    var disableObj = bq.openDisabled("#inputForm");
    var params = $('#inputForm').serialize();
    bq.closeDisabled(disableObj);
    jp.post("${ctx}/bms/finance/bmsBillDetail/save", params, function (data) {
        if (data.success) {
            $table.bootstrapTable('refresh');
            jp.success(data.msg);
            jp.close($topIndex);//关闭dialog
        } else {
            jp.bqError(data.msg);
        }
    });
}

$(document).ready(function () {
    $('#businessDate').datetimepicker({format: "YYYY-MM-DD"});

    if (!$('#id').val()) {
        $("#orgId").val(jp.getCurrentOrg().orgId);
        $("#warehouseCode").val(jp.getCurrentOrg().orgCode);
        $("#warehouseName").val(jp.getCurrentOrg().orgName);
        $("#status").val("01");
        $("#billTermsCode").val("MFT999999");
        $("#billTermsDesc").val("手工费");
    }
});

function settleObjectAfterSelect(row) {
    if (row) {
        $('#settleObjectName').val(row.settleObjectName);
        $('#settleCategory').val(row.settleCategory);
        $('#settleMethod').val(row.settleMethod);
    } else {
        $('#settleObjectName').val('');
        $('#settleCategory').val('');
        $('#settleMethod').val('');
    }
}

function billSubjectAfterSelect(row) {
    if (row) {
        $('#billSubjectName').val(row.billSubjectName);
        $('#billModule').val(row.billModule);
        $('#billCategory').val(row.billCategory);
    } else {
        $('#billSubjectName').val('');
        $('#billModule').val('');
        $('#billCategory').val('');
    }
}
</script>