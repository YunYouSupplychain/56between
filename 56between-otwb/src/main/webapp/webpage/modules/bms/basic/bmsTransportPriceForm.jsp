<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>运输价格管理</title>
    <meta name="decorator" content="ani"/>
    <style type="text/css">
        .toolbar {
            width: 100%;
            padding: 5px 0;
        }

        input[type="checkbox"] {
            width: 20px;
            height: 20px;
        }
    </style>
</head>
<body>
<form:form id="inputForm" modelAttribute="bmsTransportPriceEntity" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <form:hidden path="fkId"/>
    <input type="hidden" id="isUseStep" name="isUseStep" value="Y"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">运输价格</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">起点</label></td>
                        <td class="width-15">
                            <sys:area id="startPlaceId" name="startPlaceId" value=""
                                      codeName="startPlaceCode" codeValue="${bmsTransportPriceEntity.startPlaceCode}"
                                      labelName="startPlaceName" labelValue="${bmsTransportPriceEntity.startPlaceName}"
                                      allowSearch="true" cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">终点</label></td>
                        <td class="width-15">
                            <sys:area id="endPlaceId" name="endPlaceId" value=""
                                      codeName="endPlaceCode" codeValue="${bmsTransportPriceEntity.endPlaceCode}"
                                      labelName="endPlaceName" labelValue="${bmsTransportPriceEntity.endPlaceName}"
                                      allowSearch="true" cssClass="form-control"/>
                        </td>
                        <td class="width-10"><label class="pull-right">区域编码</label></td>
                        <td class="width-15">
                            <sys:grid title="选择区域" url="${ctx}/sys/common/region/grid" cssClass="form-control"
                                      fieldId="regionCode" fieldName="regionCode"
                                      fieldKeyName="code" fieldValue="${bmsTransportPriceEntity.regionCode}"
                                      displayFieldId="regionName" displayFieldName="regionName"
                                      displayFieldKeyName="name" displayFieldValue="${bmsTransportPriceEntity.regionName}"
                                      fieldLabels="区域编码|区域名称" fieldKeys="code|name"
                                      searchLabels="区域编码|区域名称" searchKeys="code|name"/>
                        </td>
                        <td class="width-10"><label class="pull-right">车型</label></td>
                        <td class="width-15">
                            <form:select path="carTypeCode" class="form-control">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('TMS_CAR_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="width-10"><label class="pull-right asterisk">单价</label></td>
                        <td class="width-15">
                            <form:input path="price" htmlEscape="false" class="form-control required" maxlength="18" onkeyup="bq.numberValidator(this, 8, 0)"/>
                        </td>
                        <td class="width-10"><label class="pull-right">物流点数</label></td>
                        <td class="width-15">
                            <form:input path="logisticsPoints" htmlEscape="false" class="form-control" maxlength="18" onkeyup="bq.numberValidator(this, 8, 0)"/>
                        </td>
                        <td class="width-10">
                            <input type="checkbox" class="pull-right" id="isAccumulationMethod" name="isAccumulationMethod" value="${bmsTransportPriceEntity.isAccumulationMethod}"/>
                        </td>
                        <td class="width-15">
                            <label class="pull-left">是否阶梯分段累加方式计算</label>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">阶梯价格</h3>
            </div>
            <div class="panel-body">
                <div class="toolbar">
                    <shiro:hasPermission name="bms:bmsTransportGroup:add">
                        <a id="addRow" class="btn btn-primary" onclick="addStepPrice();">新增</a>
                    </shiro:hasPermission>
                </div>
                <table id="bmsTransportStepPriceTable" class="table text-nowrap">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th class="asterisk">从(含当前值)</th>
                        <th>到(不含当前值)</th>
                        <th class="asterisk">单价</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="steppedPrices"></tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
<script type="text/template" id="steppedPricesTpl">//<!--
<tr id="steppedPrices{{idx}}">
    <td class="hide">
        <input id="steppedPrices{{idx}}_id" name="steppedPrices[{{idx}}].id" type="hidden" value="{{row.id}}"/>
        <input id="steppedPrices{{idx}}_delFlag" name="steppedPrices[{{idx}}].delFlag" type="hidden" value="0"/>
        <input id="steppedPrices{{idx}}_recVer" name="steppedPrices[{{idx}}].recVer" type="hidden" value="{{row.recVer}}"/>
        <input id="steppedPrices{{idx}}_orgId" name="steppedPrices[{{idx}}].orgId" type="hidden" value="{{row.orgId}}"/>
        <input id="steppedPrices{{idx}}_fkId" name="steppedPrices[{{idx}}].fkId" type="hidden" value="{{row.fkId}}"/>
    </td>
    <td>
        <input id="steppedPrices{{idx}}_fm" name="steppedPrices[{{idx}}].fm" type="text" value="{{row.fm}}" class="form-control required" maxlength="18" onkeyup="bq.numberValidator(this, 8, 0)"/>
    </td>
    <td>
        <input id="steppedPrices{{idx}}_to" name="steppedPrices[{{idx}}].to" type="text" value="{{row.to}}" class="form-control" maxlength="18" onkeyup="bq.numberValidator(this, 8, 0)"/>
    </td>
    <td>
        <input id="steppedPrices{{idx}}_price" name="steppedPrices[{{idx}}].price" type="text" value="{{row.price}}" class="form-control required" maxlength="18" onkeyup="bq.numberValidator(this, 8, 0)"/>
    </td>
    <td class="text-center" width="10">
        <a class="btn btn-danger" onclick="delRow('#steppedPrices{{idx}}')">删除</a>
    </td>
</tr>//-->
</script>
<script type="text/javascript">
    var steppedPricesRowIdx = 0, steppedPricesTpl = $("#steppedPricesTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
    $(document).ready(function () {
        var $isAccumulationMethod = $("#isAccumulationMethod");
        $isAccumulationMethod.on('click', function () {
            $(this).val($(this).is(":checked") ? "Y" : "N");
        });
        $isAccumulationMethod.attr('checked', $isAccumulationMethod.val() === "Y");

        var data = ${fns:toJson(bmsTransportPriceEntity.steppedPrices)};
        for (var i = 0; i < data.length; i++) {
            addStepPrice(data[i]);
        }
    });

    function doSubmit($table, $topIndex) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
        jp.loading();
        var disabledObjs = bq.openDisabled('#inputForm');
        var params = $('#inputForm').serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/bms/basic/bmsTransportGroup/saveTransportPrice", params, function (data) {
            if (data.success) {
                $table.bootstrapTable('refresh');
                jp.success(data.msg);
                jp.close($topIndex);//关闭dialog
            } else {
                jp.error(data.msg);
            }
        });
        return true;
    }

    function addStepPrice(row) {
        if (row === undefined) {
            row = {
                fkId: $('#id').val(),
                orgId: $('#orgId').val(),
                recVer: 0
            }
        }
        addRow('#steppedPrices', steppedPricesRowIdx, steppedPricesTpl, row);
        steppedPricesRowIdx = steppedPricesRowIdx + 1;
    }

    function addRow(list, idx, tpl, row) {
        $(list).append(Mustache.render(tpl, {idx: idx, delBtn: true, row: row}));
    }

    function delRow(prefix) {
        var $id = $(prefix + "_id");
        if (!$id.val()) {
            $(prefix).remove();
            return;
        }
        jp.confirm("确认要删除该记录吗？", function () {
            jp.get("${ctx}/bms/basic/bmsTransportGroup/deleteTransportStepPrice?transportStepPriceId=" + $id.val(), function (data) {
                if (data.success) {
                    $(prefix).remove();
                } else {
                    jp.bqError(data.msg);
                }
            });
        });
    }
</script>
</body>
</html>