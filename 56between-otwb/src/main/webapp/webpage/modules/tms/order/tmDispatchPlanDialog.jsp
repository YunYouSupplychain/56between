<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>批量调度</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script>
        $(document).ready(function () {
            lay('.laydate').each(function(){
                laydate.render({
                    elem: this, theme: '#393D49', type: 'datetime'
                });
            });
        });

        function save(index) {
            var validate = beforeCheck();
            if (validate.isSuccess) {
                var disabled = bq.openDisabled('#form');
                jp.loading();
                jp.post("${ctx}/tms/order/tmDispatchPlan/dispatchAll", $('#form').bq_serialize(), function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        jp.close(index);
                    } else {
                        bq.closeDisabled(disabled);
                        jp.bqError(data.msg);
                    }
                })
            } else {
                jp.bqError(validate.msg);
            }
        }

        function beforeCheck() {
            var length = $('#detailList').children().length;
            var errorMsg = '', flag = true;
            for (var i = 0; i < length; i++) {
                var index = i + 1;
                var deliveryPoint = $('#detailList' + i + '_deliveryPoint').val();
                var qty = $('#detailList' + i + '_qty').val();
                var vehicleNo = $('#detailList' + i + '_vehicleNo').val();
                var wareRoom = $('#detailList' + i + '_wareRoom').val();
                var pickUpPoint = $('#detailList' + i + '_pickUpPoint').val();
                var trip = $('#detailList' + i + '_trip').val();
                if (!qty && !vehicleNo && !wareRoom && !pickUpPoint && !trip) continue;
                if (!qty) return setErrorMsg(index, '配载数量不能为空');
                if (!vehicleNo) return setErrorMsg(index, '车牌号不能为空');
                if (!wareRoom) return setErrorMsg(index, '仓室不能为空');
                if (!pickUpPoint) return setErrorMsg(index, '提货点不能为空');
                if (!trip) return setErrorMsg(index, '车次不能为空');
                if (deliveryPoint === pickUpPoint) {
                    return setErrorMsg(index, '提货点和送货点不能相同');
                }
            }
            if (length === 0) {
                errorMsg = '调度结果列表没有数据，无法确认！';
                flag = false;
            }
            return {isSuccess: flag, msg: errorMsg};
        }

        function setErrorMsg(index, msg) {
            return {isSuccess: false, msg: '第' + index + '行' + msg};
        }

        function addRow(list, idx, tpl, row) {
            $(list).append(Mustache.render(tpl, {
                idx: idx, delBtn: true, row: row
            }));
            $(list + idx).find("select").each(function () {
                $(this).val($(this).attr("data-value"));
            });
            var lineNo = $(list + idx + '_lineNo').val();
            $(list + idx + '_lineNo').val(parseFloat(lineNo) + 1);
        }

        function queryDetail() {
            var index = jp.loading();
            $('#detailList').empty();
            var params = $('#queryForm').serializeJSON();
            jp.post("${ctx}/tms/order/tmDispatchPlan/getDispatchInfoByLine", params, function (data) {
                if (data.success && data.body.data) {
                    var rowData = data.body.data;
                    if (rowData.length === 0) {
                        jp.bqError("不存在未配载的数据！");
                        return;
                    }
                    for (var i = 0; i < rowData.length; i++) {
                        addRow('#detailList', i, detailTpl, rowData[i]);
                    }
                }
                jp.close(index);
            });
        }

        function resetForm() {
            $("#queryForm table input").val("");
            $('#detailList').empty();
        }

        function deleteDetail(idx) {
            $('#detailList' + idx + '_qty').val('');
            $('#detailList' + idx + '_vehicleNo').val('');
            $('#detailList' + idx + '_wareRoom').val('');
            $('#detailList' + idx + '_trip').val('');
            $('#detailList' + idx + '_pickUpPoint').val('');
            $('#detailList' + idx + '_pickUpPointName').val('');
        }

    </script>
</head>
<body>
<div class="hidden">
    <input id="outletType" value="OUTLET" type="hidden">
</div>
<div class="modal-body">
    <fieldset>
        <legend>调度条件</legend>
        <form id="queryForm">
            <input id="planNo" name="planNo" value="${entity.planNo}" type="hidden">
            <input id="orgId" name="orgId" value="${entity.orgId}" type="hidden">
            <input id="baseOrgId" name="baseOrgId" value="${entity.baseOrgId}" type="hidden">
            <input id="isDispatch" name="isDispatch" value="N" type="hidden">
            <table class="bq-table">
                <tr>
                    <td><label class="pull-left">路线</label></td>
                    <td><label class="pull-left">开始送达时间</label></td>
                    <td><label class="pull-left">结束送达时间</label></td>
                    <td><label class="pull-left">客户</label></td>
                    <td><label class="pull-left">商品</label></td>
                    <td></td>
                </tr>
                <tr>
                    <td>
                        <sys:grid title="选择路线" url="${ctx}/tms/basic/tmBusinessRoute/grid"
                                  fieldId="lineCode" fieldName="lineCode" fieldKeyName="code"
                                  displayFieldId="lineName" displayFieldName="lineName" displayFieldKeyName="name"
                                  fieldLabels="编码|路线" fieldKeys="code|name"
                                  searchLabels="编码|路线" searchKeys="code|name"
                                  queryParams="orgId" queryParamValues="baseOrgId"
                                  cssClass="form-control" isMultiSelected="true"/>
                    </td>
                    <td><input id="arrivalTimeFm" name="arrivalTimeFm" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td><input id="arrivalTimeTo" name="arrivalTimeTo" class="form-control laydate" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                    <td>
                        <sys:grid title="选择客户" url="${ctx}/tms/order/tmDispatchPlan/getCustomer"
                                  fieldId="ownerCode" fieldName="ownerCode" fieldKeyName="ownerCode"
                                  displayFieldId="ownerName" displayFieldName="ownerName" displayFieldKeyName="ownerName"
                                  fieldLabels="客户编码|客户名称" fieldKeys="ownerCode|ownerName"
                                  searchLabels="客户编码|客户名称" searchKeys="ownerCode|ownerName"
                                  queryParams="planNo|orgId" queryParamValues="planNo|orgId"
                                  cssClass="form-control"/>
                    </td>
                    <td>
                        <sys:grid title="选择商品" url="${ctx}/tms/order/tmDispatchPlan/getSku"
                                  fieldId="skuCode" fieldName="skuCode" fieldKeyName="skuCode"
                                  displayFieldId="skuName" displayFieldName="skuName" displayFieldKeyName="skuName"
                                  fieldLabels="客户编码|客户名称|商品编码|商品名称|商品类型" fieldKeys="ownerCode|ownerName|skuCode|skuName|skuTypeDesc"
                                  searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                  queryParams="planNo|orgId|ownerCode" queryParamValues="planNo|orgId|ownerCode"
                                  cssClass="form-control"/>
                    </td>
                    <td>
                        <button type="button" class="btn btn-primary btn-sm" onclick="queryDetail()"> &nbsp;查询&nbsp; </button>
                        <button type="button" class="btn btn-danger btn-sm" onclick="resetForm()"> &nbsp;重置&nbsp; </button>
                    </td>
                </tr>
            </table>
        </form>
    </fieldset>
    <fieldset>
        <legend>调度结果</legend>
        <form id="form" class="form-horizontal">
            <table class="table table-bordered table-striped">
                <thead>
                <tr>
                    <th class="hide"></th>
                    <th>行号</th>
                    <th>客户</th>
                    <th>商品</th>
                    <th>计划数量</th>
                    <th><font color="red">*</font>配载数量</th>
                    <th>送达时间</th>
                    <th><font color="red">*</font>车牌号</th>
                    <th><font color="red">*</font>仓室</th>
                    <th><font color="red">*</font>提货点</th>
                    <th><font color="red">*</font>车次</th>
                    <th>路线</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="detailList"></tbody>
            </table>
        </form>
    </fieldset>
</div>
<script type="text/template" id="detailTpl">//<!--
<tr id="detailList{{idx}}">
    <td class="hide">
        <input id="detailList{{idx}}_id" name="detailList[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="detailList{{idx}}_delFlag" name="detailList[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="detailList{{idx}}_planNo" name="detailList[{{idx}}].planNo" type="hidden" value="{{row.planNo}}"/>
        <input id="detailList{{idx}}_orgId" name="detailList[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="detailList{{idx}}_recVer" name="detailList[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
        <input id="detailList{{idx}}_baseOrgId" name="detailList[{{idx}}].baseOrgId" type="hidden" value="{{row.baseOrgId}}"/>
        <input id="detailList{{idx}}_deliveryPoint" name="detailList[{{idx}}].deliveryPoint" type="hidden" value="{{row.deliveryPoint}}"/>
        <input id="detailList{{idx}}_skuCode" name="detailList[{{idx}}].skuCode" type="hidden" value="{{row.skuCode}}"/>
        <input id="detailList{{idx}}_skuType" type="hidden" value="{{row.skuType}}"/>
    </td>
    <td width="60px"><input id="detailList{{idx}}_lineNo" type="text" value="{{idx}}" class="form-control" readonly/></td>
    <td><input id="detailList{{idx}}_deliveryPointName" name="detailList[{{idx}}].deliveryPointName" type="text" value="{{row.deliveryPointName}}" class="form-control" readonly/></td>
    <td><input id="detailList{{idx}}_skuName" name="detailList[{{idx}}].skuName" type="text" value="{{row.skuName}}" class="form-control" readonly/></td>
    <td><input id="detailList{{idx}}_planQty" name="detailList[{{idx}}].planQty" type="text" value="{{row.planQty}}" class="form-control" readonly/></td>
    <td><input id="detailList{{idx}}_qty" name="detailList[{{idx}}].qty" type="text" value="{{row.qty}}" class="form-control required" onkeyup="bq.numberValidator(this, 2, 0)" maxlength="10"/></td>
    <td width="180px"><input id="detailList{{idx}}_arrivalTime" name="detailList[{{idx}}].arrivalTime" type="text" value="{{row.arrivalTime}}" class="form-control" readonly/></td>
    <td>
        <sys:grid title="车辆" url="${ctx}/tms/order/tmDispatchPlan/getVehicle"
              fieldId="" fieldName="" fieldKeyName="" fieldValue=""
              displayFieldId="detailList{{idx}}_vehicleNo" displayFieldName="detailList[{{idx}}].vehicleNo" displayFieldKeyName="vehicleNo" displayFieldValue="{{row.vehicleNo}}"
              fieldLabels="车牌号|车辆类型|设备类型" fieldKeys="vehicleNo|vehicleTypeDesc|equipmentTypeName"
              searchLabels="车牌号" searchKeys="vehicleNo"
              queryParams="orgId|planNo|baseOrgId|vehicleType" queryParamValues="orgId|planNo|baseOrgId|detailList{{idx}}_skuType"
              cssClass="form-control"/>

    </td>
    <td width="150px">
        <select id="detailList{{idx}}_wareRoom" name="detailList[{{idx}}].wareRoom" data-value="{{row.wareRoom}}" class="form-control m-b required">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('TMS_WARE_ROOM')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td>
        <sys:grid title="选择提货点" url="${ctx}/tms/basic/tmTransportObj/grid"
                  fieldId="detailList{{idx}}_pickUpPoint" fieldName="detailList[{{idx}}].pickUpPoint" fieldKeyName="transportObjCode" fieldValue="{{row.pickUpPoint}}"
                  displayFieldId="detailList{{idx}}_pickUpPointName" displayFieldName="detailList[{{idx}}].pickUpPointName" displayFieldKeyName="transportObjName" displayFieldValue="{{row.pickUpPointName}}"
                  fieldLabels="提货点编码|提货点名称" fieldKeys="transportObjCode|transportObjName"
                  searchLabels="提货点编码|提货点名称" searchKeys="transportObjCode|transportObjName"
                  queryParams="transportObjType|orgId" queryParamValues="outletType|baseOrgId"
                  cssClass="form-control required"/>
    </td>
    <td width="150px">
        <select id="detailList{{idx}}_trip" name="detailList[{{idx}}].trip" data-value="{{row.trip}}" class="form-control m-b required">
            <option value=""></option>
            <c:forEach items="${fns:getDictList('TMS_TRIP')}" var="dict">
                <option value="${dict.value}">${dict.label}</option>
            </c:forEach>
        </select>
    </td>
    <td><input id="detailList{{idx}}_lineName" name="detailList[{{idx}}].lineName" type="text" value="{{row.lineName}}" class="form-control" readonly/></td>
    <td><button id="detailList{{idx}}_delete" type="button" class="btn btn-danger btn-sm" onclick="deleteDetail({{idx}})">清空</button></td>
</tr>//-->
</script>
<script type="text/javascript">
    var detailTpl = $("#detailTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
</script>
</body>
</html>