<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	if ($("#id").val().length > 0) {
		$('#fittingCode').prop('readonly', true);
	} else {
		var $dataSet = ${fns:toJson(fns:getUserDataSet())};
		$("#dataSet").val($dataSet.code);
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
	jp.post("${ctx}/sys/common/tms/fitting/save", params, function (data) {
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