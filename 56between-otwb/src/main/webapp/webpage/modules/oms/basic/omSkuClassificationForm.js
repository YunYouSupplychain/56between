<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	if ($("#id").val().length > 0) {
		$('#code').prop('readonly', true);
	} else {
		$("#orgId").val(jp.getCurrentOrg().orgId);
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
	var params = $('#inputForm').serialize();
	bq.closeDisabled(disabledObjs);
	jp.post("${ctx}/oms/basic/skuClassification/save", params, function (data) {
		if (data.success) {
			jp.success(data.msg);
			$table.bootstrapTable('refresh');
			jp.close(index);
		} else {
			jp.bqError(data.msg);
		}
	});
}
</script>