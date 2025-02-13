<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>仓储价格管理</title>
    <meta name="decorator" content="ani"/>
    <style type="text/css">
        .toolbar {
            width: 100%
        }

        input[type="checkbox"] {
            width: 20px;
            height: 20px
        }
    </style>
</head>
<body>
<form:form id="inputForm" modelAttribute="bmsContractStoragePrice" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="recVer"/>
    <form:hidden path="orgId"/>
    <form:hidden path="fkId"/>
    <input type="hidden" id="isUseStep" name="isUseStep" value="Y"/>

    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">仓储价格</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="width-10"><label class="pull-right">品类编码</label></td>
                        <td class="width-15">
                            <sys:grid title="选择商品分类" url="${ctx}/bms/basic/skuClassification/grid" cssClass="form-control"
                                      fieldId="" fieldName="" fieldKeyName=""
                                      displayFieldId="skuClass" displayFieldName="skuClass"
                                      displayFieldKeyName="code" displayFieldValue="${bmsContractStoragePrice.skuClass}"
                                      fieldLabels="编码|名称" fieldKeys="code|name"
                                      searchLabels="编码|名称" searchKeys="code|name"
                                      queryParams="orgId" queryParamValues="orgId"
                                      afterSelect="skuClassAfterSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">品类名称</label></td>
                        <td class="width-15">
                            <form:input path="skuClassName" htmlEscape="false" class="form-control" readonly="true"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品编码</label></td>
                        <td class="width-15">
                            <sys:grid title="选择商品" url="${ctx}/bms/basic/settlementSku/grid" cssClass="form-control"
                                      fieldId="" fieldKeyName="" fieldName=""
                                      displayFieldId="skuCode" displayFieldKeyName="skuCode"
                                      displayFieldName="skuCode" displayFieldValue="${bmsContractStoragePrice.skuCode}"
                                      fieldLabels="商品编码|商品名称" fieldKeys="skuCode|skuName"
                                      searchLabels="商品编码|商品名称" searchKeys="skuCode|skuName"
                                      queryParams="skuClass|orgId" queryParamValues="skuClass|orgId"
                                      afterSelect="skuAfterSelect"/>
                        </td>
                        <td class="width-10"><label class="pull-right">商品名称</label></td>
                        <td class="width-15">
                            <form:input path="skuName" htmlEscape="false" class="form-control" readonly="true"/>
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
                            <input type="checkbox" class="pull-right" id="isAccumulationMethod" name="isAccumulationMethod"
                                   value="${bmsContractStoragePrice.isAccumulationMethod}"/>
                        </td>
                        <td class="width-15"><label class="pull-left">是否阶梯分段累加方式计算</label></td>
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
                <table id="bmsTransportStepPriceTable" class="table table-striped table-bordered table-condensed">
                    <thead>
                    <tr>
                        <th class="hide"></th>
                        <th class="asterisk">从(含当前值)</th>
                        <th>到(不含当前值)</th>
                        <th class="asterisk">单价</th>
                        <th width="80">&nbsp;</th>
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
        var id = $('#id').val()
        if (!id) {
            $('#price').val(1);
            $('#logisticsPoints').val(1);
        }
        var $isAccumulationMethod = $("#isAccumulationMethod");
        $isAccumulationMethod.on('click', function () {
            $(this).val($(this).is(":checked") ? "Y" : "N");
        });
        $isAccumulationMethod.attr('checked', $isAccumulationMethod.val() === "Y");

        var data = ${fns:toJson(bmsContractStoragePrice.steppedPrices)};
        for (var i = 0; i < data.length; i++) {
            addStepPrice(data[i]);
        }
    });

    function doSubmit($table, $topIndex) {
        jp.loading();
        var disabledObjs = bq.openDisabled('#inputForm');
        var params = $('#inputForm').serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/bms/basic/bmsContract/storagePrice/save", params, function (data) {
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
            jp.get("${ctx}/bms/basic/bmsContract/storageStepPrice/delete?stepPriceId=" + $id.val(), function (data) {
                if (data.success) {
                    $(prefix).remove();
                } else {
                    jp.bqError(data.msg);
                }
            });
        });
    }

    function skuAfterSelect(row) {
        if (row) {
            $('#skuName').val(row.skuName);
        }
    }

    function skuClassAfterSelect(row) {
        if (row) {
            $('#skuClassName').val(row.name);
            $('#skuCode').val('');
            $('#skuName').val('');
        }
    }
</script>
</body>
</html>