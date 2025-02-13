<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    var transportObjType = [];
    $("input[name='transportObjType']:checked").each(function () {
        transportObjType.push($(this).val());
    });
    if (transportObjType.indexOf("CARRIER") !== -1) {
        $("#carrierMatchedOrg").attr('readonly', false);
        $("#carrierMatchedOrgSBtnId").removeClass('disabled');
        $("#carrierMatchedOrgDBtnId").prop('disabled', false);
    } else {
        $("#carrierMatchedOrg").attr('readonly', true);
        $("#carrierMatchedOrgSBtnId").addClass('disabled');
        $("#carrierMatchedOrgDBtnId").prop('disabled', true);
    }
    if (transportObjType.indexOf("OUTLET") !== -1) {
        $("#outletMatchedOrg").attr('readonly', false);
        $("#outletMatchedOrgSBtnId").removeClass('disabled');
        $("#outletMatchedOrgDBtnId").prop('disabled', false);
    } else {
        $("#outletMatchedOrg").attr('readonly', true);
        $("#outletMatchedOrgSBtnId").addClass('disabled');
        $("#outletMatchedOrgDBtnId").prop('disabled', true);
    }
    if (transportObjType.indexOf("REPAIR") !== -1) {
        $("#repairPrice").prop('readonly', false);
    } else {
        $("#repairPrice").prop('readonly', true);
    }
    if (transportObjType.indexOf("SETTLEMENT") !== -1) {
        $("#settleName").attr('readonly', true);
        $("#settleNameSBtnId").addClass('disabled');
        $("#settleNameDBtnId").prop('disabled', true);
    } else {
        $("#settleName").attr('readonly', false);
        $("#settleNameSBtnId").removeClass('disabled');
        $("#settleNameDBtnId").prop('disabled', false);
    }

    if ($("#id").val().length > 0) {
        $('#transportObjCode').prop('readonly', true);
    } else {
        $("#orgId").val(tmOrg.id);
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
});

function doSubmit($table, index) {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var disabledObjs = bq.openDisabled("#inputForm");
    jp.post("${ctx}/tms/basic/tmTransportObj/save", $('#inputForm').serialize(), function (data) {
        if (data.success) {
            jp.success(data.msg);
            $table.bootstrapTable('refresh');
            jp.close(index);
        } else {
            bq.closeDisabled(disabledObjs);
            jp.bqError(data.msg);
        }
    });
}

</script>