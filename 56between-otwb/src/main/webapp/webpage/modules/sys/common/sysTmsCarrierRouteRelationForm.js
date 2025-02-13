<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	if ($("#id").val().length > 0) {
		$('#code').prop('readonly', true);
	} else {
		var $dataSet = ${fns:toJson(fns:getUserDataSet())};
		$("#dataSet").val($dataSet.code);
		$("#dataSetName").val($dataSet.name);
	}
	$('#dataSetName').prop('readonly', true);
	$('#dataSetNameSBtnId').prop('disabled', true);
	$('#dataSetNameDBtnId').prop('disabled', true);

});

function doSubmit($table, index) {
	jp.loading();
	var validator = bq.headerSubmitCheck("#inputForm");
	if (!validator.isSuccess) {
		jp.bqWaring(validator.msg);
		return;
	}
	var disabledObjs = bq.openDisabled("#inputForm");
	jp.post("${ctx}/sys/common/tms/carrierRouteRelation/save", $('#inputForm').serialize(), function (data) {
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