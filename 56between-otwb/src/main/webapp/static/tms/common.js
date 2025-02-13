var tmOrg = {id: "*"};
$(function () {
    $.ajax({
        url: ctx + "/sys/office/getOrgCenter?officeId=" + jp.getCurrentOrg().orgId,
        method: "get",
        async: false,
        success: function (data) {
            if (data && data.hasOwnProperty("id")) {
                tmOrg = data;
            }
        }
    });
});