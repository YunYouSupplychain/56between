<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    var isDriver = '${tmAppUserInfoEntity.isDriver}';
    $('#isDriver').prop('checked', isDriver === 'Y').val(isDriver);
    var isCustomer = '${tmAppUserInfoEntity.isCustomer}';
    $('#isCustomer').prop('checked', isCustomer === 'Y').val(isCustomer);
    var isSafetyChecker = '${tmAppUserInfoEntity.isSafetyChecker}';
    $('#isSafetyChecker').prop('checked', isSafetyChecker === 'Y').val(isSafetyChecker);

    $('#def1').prop('readonly', true);
	if ($("#id").val().length > 0) {
		$('#loginName').prop('readonly', true);
        $('#password').prop('readonly', true);
        $('#name').prop('readonly', true);

        if ($('#isCustomer').val() === 'Y') {
            $("#transportObjNameSBtnId").prop('disabled', false);
            $("#transportObjNameDBtnId").prop('disabled', false);
		}
        if ($('#isDriver').val() === 'Y') {
            $("#driverNameSBtnId").prop('disabled', false);
            $("#driverNameDBtnId").prop('disabled', false);
        }
	}
});

function doSubmit($table, index) {
	jp.loading();
	var validator = bq.headerSubmitCheck("#inputForm");
	if (!validator.isSuccess) {
		jp.error(validator.msg);
		return;
	}
	var disabledObjs = bq.openDisabled("#inputForm");
	jp.post("${ctx}/tms/app/tmAppUserInfo/save", $('#inputForm').serialize(), function (data) {
		if (data.success) {
			jp.success(data.msg);
			$table.bootstrapTable('refresh');
			jp.close(index);
		} else {
			bq.closeDisabled(disabledObjs);
			jp.error(data.msg);
		}
	});
}

function orgSelect(data) {
    if (data) {
        $('#baseOrgId').val(data.baseOrgId);
    }
}

function isCustomerChange(flag, obj) {
    $(obj).val(flag ? "Y" : "N");
    if ("Y" === $(obj).val()) {
        $("#transportObjCode").val("");
        $("#transportObjName").val("");
        $("#transportObjNameSBtnId").prop('disabled', false);
        $("#transportObjNameDBtnId").prop('disabled', false);
    } else {
        $("#transportObjCode").val("");
        $("#transportObjName").val("");
        $("#transportObjNameSBtnId").prop('disabled', true);
        $("#transportObjNameDBtnId").prop('disabled', true);
	}
}

function isDriverChange(flag, obj) {
    $(obj).val(flag ? "Y" : "N");
    if ("Y" === $(obj).val()) {
        $("#driver").val("");
        $("#driverName").val("");
        $("#driverNameSBtnId").prop('disabled', false);
        $("#driverNameDBtnId").prop('disabled', false);
    } else {
        $("#driver").val("");
        $("#driverName").val("");
        $("#driverNameSBtnId").prop('disabled', true);
        $("#driverNameDBtnId").prop('disabled', true);
    }
}

function isSafetyCheckerChange(flag, obj) {
    $(obj).val(flag ? "Y" : "N");
}
</script>