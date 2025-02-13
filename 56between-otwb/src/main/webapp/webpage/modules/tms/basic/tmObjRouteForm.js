<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	if ($("#id").val().length <= 0) {
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
	var params = $('#inputForm').serializeJSON();
	bq.closeDisabled(disabledObjs);
	jp.post("${ctx}/tms/basic/tmObjRoute/save", params, function (data){
		if (data.success) {
			jp.success(data.msg);
			$table.bootstrapTable('refresh');
			jp.close(index);
		} else {
			jp.bqError(data.msg);
		}
	});
}

function startObjAfterSelect(row) {
	if (row) {
		$('#startObjName').val(row.transportObjName);
		$('#startObjAddress').val(row.address);
	}
}

function endObjAfterSelect(row) {
	if (row) {
		$('#endObjName').val(row.transportObjName);
		$('#endObjAddress').val(row.address);
	}
}
</script>