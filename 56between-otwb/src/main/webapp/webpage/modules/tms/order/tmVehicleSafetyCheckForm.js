<%@ page contentType="text/html;charset=UTF-8"%>
<script>
$(document).ready(function () {
    $("#checkDate").datetimepicker({format: "YYYY-MM-DD"});
    $("#arrivalTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});

    initV();
    editController();
});

/*初始化值*/
function initV() {
    if ($("#id").val().length > 0) return;
    $("#orgId").val(jp.getCurrentOrg().orgId);
    $("#baseOrgId").val(tmOrg.id);
    $("#checkDate input").val(jp.dateFormat(new Date(), "yyyy-MM-dd"));
}

/*编辑控制*/
function editController() {
    var isNew = $("#id").val().length <= 0;// 是否是新增未保存数据
    var status = $("#status").val();// 状态
    if ("00" === status) {// 新建
        $("#save").attr('disabled', false);
    } else if ("10" === status) {// 已处理
        $("#save").attr('disabled', true);
    }
    $("#ownerName").prop('readonly', !isNew);
    $("#ownerNameSBtnId").prop('disabled', !isNew);
    $("#ownerNameDBtnId").prop('disabled', !isNew);
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
    jp.post("${ctx}/tms/order/tmVehicleSafeTyCheck/save", $('#inputForm').serialize(), function (data) {
        bq.closeDisabled(objs);
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/tmVehicleSafeTyCheck/form?id=" + data.body.entity.id;
        } else {
            jp.bqError(data.msg);
        }
    });
}
</script>