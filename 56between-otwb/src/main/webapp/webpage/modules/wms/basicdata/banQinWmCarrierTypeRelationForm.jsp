<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>承运商类型关系管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            $table = table;
            $topIndex = index;
            jp.loading();
            save();
        }

        $(document).ready(function () {
            init();
        });

        /**
         * 保存
         */
        function save() {
            var validate = bq.headerSubmitCheck('#inputForm');
            if (validate.isSuccess) {
                jp.loading();
                jp.post("${ctx}/wms/basicdata/banQinWmCarrierTypeRelation/save", $('#inputForm').bq_serialize(), function (data) {
                    if (data.success) {
                        $table.bootstrapTable('refresh');
                        jp.success(data.msg);
                        jp.close($topIndex);
                    } else {
                        jp.bqError(data.msg);
                    }
                })
            } else {
                jp.bqError(validate.msg);
            }
        }

        function init() {
            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#recVer').val('0');
            } else {
                typeChange($('#type'));
                $("#mailType").val('${banQinWmCarrierTypeRelationEntity.mailType}');
                $("#trackingInterfaceId").val('${banQinWmCarrierTypeRelationEntity.trackingInterfaceId}');
                $("#description").val('${banQinWmCarrierTypeRelationEntity.description}');
            }
        }

        function selectInterface() {
            if (!$('#type').val()) {
                jp.warning("快递类型不能为空!");
                return;
            }

            top.layer.open({
                type: 2,
                area: ['1000', '600px'],
                title: "选择接口",
                auto: true,
                name: 'friend',
                content: "${ctx}/tag/grid?url=" + encodeURIComponent("${ctx}/wms/basicdata/banQinCdTrackingInfo/grid?orgId=" + jp.getCurrentOrg().orgId + "|type=" + $('#type').val())
                    + "&fieldLabels=" + encodeURIComponent("接口描述|接口地址")
                    + "&fieldKeys=" + encodeURIComponent("description|url")
                    + "&searchLabels=" + encodeURIComponent("接口描述")
                    + "&searchKeys=" + encodeURIComponent("description") + "&isMultiSelected=false",
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var items = iframeWin.getSelections();
                    if (items == "") {
                        jp.warning("必须选择一条数据!");
                        return;
                    }
                    $("#trackingInterfaceId").val(items[0].id);
                    $("#description").val(items[0].description);
                    top.layer.close(index);
                },
                cancel: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0].contentWindow;
                    var row = iframeWin.currentRow;
                    if (row) {
                        $("#trackingInterfaceId").val(row.id);
                        $("#description").val(row.description);
                    }
                }
            });
        }

        function deleteInterface() {
            $('#trackingInterfaceId').val('');
            $('#description').val('');
        }
        
        function typeChange(obj) {
            deleteInterface();
            $("#mailType").empty();
            var value = obj.val();
            if (value) {
                $("#mailType").append("<option value=''></option>");
                switch (value) {
                    case "HTKY":
                        $("#mailType").append("<option value='BS1'>百世小面单</option>").append("<option value='BS2'>百世大面单</option>");
                        break;
                    case "SFEXPRESS":
                        $("#mailType").append("<option value='SF'>顺丰面单</option>");
                        break;
                    case "ZT":
                        $("#mailType").append("<option value='ZT'>自提面单</option>");
                        break;
                    case "ZTO":
                        $("#mailType").append("<option value='ZTO'>中通面单</option>");
                        break;
                    case "YUNDA":
                        $("#mailType").append("<option value='YUNDA'>韵达面单</option>");
                        break;
                    case "STO":
                        $("#mailType").append("<option value='STO'>申通面单</option>");
                        break;
                    case "YTO":
                        $("#mailType").append("<option value='YTO'>圆通面单</option>");
                        break;
                }
            }
        }

    </script>
</head>
<body class="bg-white">
<form:form id="inputForm" modelAttribute="banQinWmCarrierTypeRelationEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <table class="table">
        <tbody>
        <tr>
            <td class="width-20"><label class="pull-right asterisk">承运商</label></td>
            <td class="width-80">
                <input id="carrierType" value="CARRIER" type="hidden">
                <sys:grid title="选择承运商" url="${ctx}/wms/customer/banQinEbCustomer/grid" cssClass="form-control required"
                          fieldId="carrierCode" fieldName="carrierCode"
                          fieldKeyName="ebcuCustomerNo" fieldValue="${banQinWmCarrierTypeRelationEntity.carrierCode}"
                          displayFieldId="carrierName" displayFieldName="carrierName"
                          displayFieldKeyName="ebcuNameCn" displayFieldValue="${banQinWmCarrierTypeRelationEntity.carrierName}"
                          fieldLabels="编码|名称" fieldKeys="ebcuCustomerNo|ebcuNameCn"
                          searchLabels="编码|名称" searchKeys="ebcuCustomerNo|ebcuNameCn"
                          queryParams="ebcuType|orgId" queryParamValues="carrierType|orgId"/>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right asterisk">快递类型</label></td>
            <td class="width-80">
                <form:select path="type" class="form-control m-b required" onchange="typeChange($(this))">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('SYS_WM_CARRIER_TYPE_RELATION')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right">接口信息</label></td>
            <td class="width-80">
                <div class="input-group" style="width: 100%">
                    <input id="trackingInterfaceId" name="trackingInterfaceId" type="hidden" value="${banQinWmCarrierTypeRelationEntity.trackingInterfaceId}"/>
                    <input id="description" name="description" value="${banQinWmCarrierTypeRelationEntity.description}" class="form-control" readonly/>
                    <span class="input-group-btn">
                        <button type="button" onclick="selectInterface()" class="btn btn-primary"><i class="fa fa-search"></i></button>
                        <button type="button" onclick="deleteInterface()" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 53px; z-index: 999; display: block;">×</button>
                    </span>
                </div>
            </td>
        </tr>
        <tr>
            <td class="width-20"><label class="pull-right">面单类型</label></td>
            <td class="width-80">
                <select id="mailType" name="mailType" class="form-control"></select>
            </td>
        </tr>
        </tbody>
    </table>
</form:form>
</body>
</html>