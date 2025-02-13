<%@ page contentType="text/html;charset=UTF-8"%>
<script>
$(document).ready(function () {
    $("#opTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initV();
    editController();
});

/*初始化值*/
function initV() {
    if ($("#id").val().length > 0) return;
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $("#baseOrgId").val(tmOrg.id);
    $("#opTime input").val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
}

/*编辑控制*/
function editController() {
    var isNew = $("#id").val().length <= 0;// 是否是新增未保存数据
    $("#save").attr('disabled', false);
}

/*保存单头*/
function save() {
    jp.loading();
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return;
    }
    var objs = bq.openDisabled("#inputForm");
    jp.post("${ctx}/tms/order/transport/track/save", $('#inputForm').serialize(), function (data) {
        bq.closeDisabled(objs);
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/transport/track/form?id=" + data.body.entity.id;
        } else {
            jp.bqError(data.msg);
        }
    });
}
</script>