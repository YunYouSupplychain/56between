<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	if ($("#id").val().length > 0) {
		$('#code').prop('readonly', true);
	} else {
		$("#orgId").val(tmOrg.id);
	}

});

function doSubmit($table, index) {
	jp.loading();
	var validator = bq.headerSubmitCheck("#inputForm");
	if (!validator.isSuccess) {
		jp.bqWaring(validator.msg);
		return;
	}
	var disabledObjs = bq.openDisabled("#inputForm");
	var data = $('#inputForm').serialize();
    bq.closeDisabled(disabledObjs);
	jp.post("${ctx}/tms/basic/tmBusinessRoute/save", data, function (res) {
		if (res.success) {
			jp.success(res.msg);
			$table.bootstrapTable('refresh');
			jp.close(index);
		} else {
			jp.bqError(res.msg);
		}
	});
}

</script>