<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	if (!$("#id").val()) {
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
    var objs = bq.openDisabled("#inputForm");
    var data = $('#inputForm').serialize();
    bq.closeDisabled(objs);

    jp.post("${ctx}/tms/basic/tmVehicleType/save", data, function (data) {
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