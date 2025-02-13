<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>复制平台数据</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <style>
        .myCheckBox{position:relative;padding:0 15px 0 25px;margin-bottom:7px;margin-top:0;display:inline-block}
        .myCheckBox input[type="checkbox"]{opacity:0;position:absolute;cursor:pointer;z-index:2;margin:-6px 0 0 0;top:50%;left:3px}
        .myCheckBox label:before{content:'';position:absolute;top:50%;left:0;margin-top:-9px;width:19px;height:18px;display:inline-block;border-radius:2px;border:1px solid #bbb;background:#fff}
        .myCheckBox input[type="checkbox"]:checked +label:after{position:absolute;display:inline-block;font-family:'Glyphicons Halflings',serif;content:"\e013";top:42%;left:3px;margin-top:-5px;font-size:11px;line-height:1;width:16px;height:16px;color:#333}
        .myCheckBox label{cursor:pointer;line-height:1.2;font-weight:normal;margin-bottom:0;text-align:left}
    </style>
    <script>
        function syncAll() {
            var selectSystem = checkOrg();
            var selectOrg = checkCompany();
            if (selectSystem.length > 0 && selectOrg) {
                jp.confirm("请确认是否执行", function () {
                    jp.loading();
                    jp.get("${ctx}/sys/common/dataSet/sync/confirm?systems=" + selectSystem + "&dataSet=${sysCommonDataSet.code}" + "&orgId=" + selectOrg, function (data) {
                        jp.alert(data.msg);
                    });
                });
            }
        }

        function checkOrg() {
            var selected = [];
            $("input[type='checkbox']").each(function () {
                if ($(this).prop('checked')) {
                    selected.push($(this).val());
                }
            });
            if (selected.length < 1) {
                jp.warning('请选择需要同步的平台');
            }
            return selected;
        }

        function checkCompany() {
            var orgId = $('#orgId').val();
            if (orgId.length < 1) {
                jp.warning('请选择需要同步的机构');
            }
            return orgId;
        }
    </script>
</head>
<body>
<div class="hide">
    <input id="dataSet" value="${sysCommonDataSet.code}">
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-body" style="height: 200px;">
            <table class="table table-striped table-bordered table-condensed">
                <tbody>
                <tr>
                    <td class="active" style="width: 20%; vertical-align: middle;">
                        <label class="pull-right"><font color="red">*</font>选择机构：</label>
                    </td>
                    <td class="active" style="width: 70%;">
                        <sys:grid title="选择机构" url="${ctx}/sys/common/dataSetOrgRelation/grid" cssClass="form-control required"
                                  fieldId="orgId" fieldName="orgId" fieldKeyName="orgId"
                                  displayFieldId="orgName" displayFieldName="orgName" displayFieldKeyName="orgName"
                                  fieldLabels="机构编码|机构名称" fieldKeys="orgCode|orgName"
                                  searchLabels="机构编码|机构名称" searchKeys="orgCode|orgName"
                                  queryParams="dataSet" queryParamValues="dataSet"/>
                    </td>
                </tr>
                <tr>
                    <td class="active" style="width: 20%; vertical-align: middle;">
                        <label class="pull-right"><font color="red">*</font>选择平台：</label>
                    </td>
                    <td class="active" style="width: 70%;">
                        <div class="myCheckBox"><input type="checkbox" id="OMS" value="OMS" checked><label for="OMS">订单管理系统&nbsp;</label></div>
                        <%--<div class="myCheckBox"><input type="checkbox" id="SMS" value="SMS" checked><label for="SMS">商超管理系统&nbsp;</label></div>--%>
                        <div class="myCheckBox"><input type="checkbox" id="WMS" value="WMS" checked><label for="WMS">仓储管理系统&nbsp;</label></div>
                        <div class="myCheckBox"><input type="checkbox" id="TMS" value="TMS" checked><label for="TMS">运输管理系统&nbsp;</label></div>
                        <div class="myCheckBox"><input type="checkbox" id="BMS" value="BMS" checked><label for="BMS">计费管理系统&nbsp;</label></div>
                    </td>
                </tr>
                </tbody>
            </table>
            <div style="text-align: center;">
                <button id="syncAll" class="btn btn-primary" style="width: 150px;" onclick="syncAll()"> 同步</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>