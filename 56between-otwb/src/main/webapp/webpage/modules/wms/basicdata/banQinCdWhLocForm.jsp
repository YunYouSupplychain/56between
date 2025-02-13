<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>库位管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript">
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        var disabledObj = [];
        function doSubmit(table, index) {
            $table = table;
            $topIndex = index;
            jp.loading();
            save();
        }

        $(document).ready(function () {
            // 初始化标签
            init();
        });
        
        /**
         * 保存
         */
        function save() {
            var validate = bq.headerSubmitCheck('#inputForm');
            if (validate.isSuccess) {
                jp.loading();
                var disabledObjs = bq.openDisabled('#inputForm');
                var params = $('#inputForm').bq_serialize();
                bq.closeDisabled(disabledObjs);
                jp.post("${ctx}/wms/basicdata/banQinCdWhLoc/save", params, function (data) {
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
        
        /**
         * 初始化
         */ 
        function init() {
            $('#status').prop('disabled', true);
            if (!$('#id').val()) {
                $('#orgId').val(jp.getCurrentOrg().orgId);
                $('#isEnable').prop('checked', true).val('Y');
                $('#isMixSku').prop('checked', true).val('Y');
                $('#isMixLot').prop('checked', true).val('Y');
                $('#isLoseId').prop('checked', true).val('Y');
            } else {
                $('#locCode').prop('readonly', true);
                // 是否启用
                var isEnable = '${banQinCdWhLoc.isEnable}';
                $('#isEnable').prop('checked', isEnable === 'Y').val(isEnable);
                // 是否混商品
                var isMixSku = '${banQinCdWhLoc.isMixSku}';
                $('#isMixSku').prop('checked', isMixSku === 'Y').val(isMixSku);
                $('#maxMixSku').prop('readonly', !(isMixSku === 'Y'));
                // 是否混批次
                var isMixLot = '${banQinCdWhLoc.isMixLot}';
                $('#isMixLot').prop('checked', isMixLot === 'Y').val(isMixLot);
                $('#maxMixLot').prop('readonly', !(isMixLot === 'Y'));
                // 是否忽略跟踪号
                var isLoseId = '${banQinCdWhLoc.isLoseId}';
                $('#isLoseId').prop('checked', isLoseId === 'Y').val(isLoseId);
            }
        }

        /**
         * 启用勾选事件
         * @param flag
         */ 
        function enableChange(flag) {
            $('#isEnable').val(flag ? 'Y' : 'N');
        }
        
        /**
         * 是否混商品勾选事件
         * @param flag
         */
        function mixSkuChange(flag) {
            $('#isMixSku').val(flag ? 'Y' : 'N');
            $('#maxMixSku').prop('readonly', !flag);
        }

        /**
         * 是否混批次勾选事件
         * @param flag
         */
        function mixLotChange(flag) {
            $('#isMixLot').val(flag ? 'Y' : 'N');
            $('#maxMixLot').prop('readonly', !flag);
        }

        /**
         * 是否忽略跟踪号勾选事件
         * @param flag
         */
        function isLoseIdChange(flag) {
            $('#isLoseId').val(flag ? 'Y' : 'N');
            $('#isLoseId').prop('readonly', !flag);
        }
    </script>
</head>
<body>
<form:form id="inputForm" modelAttribute="banQinCdWhLoc" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <form:hidden path="recVer"/>
    <div class="wrapper wrapper-content-my">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">基础信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>库位编码</label></td>
                        <td class="" width="12%">
                            <form:input path="locCode" htmlEscape="false" class="form-control required" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>所属库区</label></td>
                        <td class="" width="12%">
                            <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control required"
                                           fieldId="zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="${banQinCdWhLoc.zoneCode}" allowInput="true"
                                           displayFieldId="zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue="${banQinCdWhLoc.zoneName}"
                                           selectButtonId="zoneSelectId" deleteButtonId="zoneDeleteId"
                                           fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                           searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName" inputSearchKey="zoneCodeAndName">
                            </sys:popSelect>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>库位状态</label></td>
                        <td class="" width="12%">
                            <form:select path="status" class="form-control m-b required">
                                <form:options items="${fns:getDictList('SYS_WM_LOC_STATUS')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">库位种类</label></td>
                        <td class="" width="12%">
                            <form:select path="category" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_CATEGORY')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>使用类型</label></td>
                        <td class="" width="12%">
                            <form:select path="locUseType" class="form-control m-b required">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_LOC_USE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>上架顺序</label></td>
                        <td class="" width="12%">
                            <form:input path="paSeq" htmlEscape="false" class="form-control required" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right"><font color="red">*</font>拣货顺序</label></td>
                        <td class="" width="12%">
                            <form:input path="pkSeq" htmlEscape="false" class="form-control required" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">库位组</label></td>
                        <td class="" width="12%">
                            <form:input path="locGroup" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">库位ABC</label></td>
                        <td class="" width="12%">
                            <form:select path="abc" class="form-control m-b">
                                <form:option value="" label=""/>
                                <form:options items="${fns:getDictList('SYS_WM_ABC')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </td>
                        <td class="" width="8%"><label class="pull-right">是否启用</label></td>
                        <td class="" width="12%">
                            <input id="isEnable" name="isEnable" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="enableChange(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">混商品</label></td>
                        <td class="" width="12%">
                            <input id="isMixSku" name="isMixSku" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="mixSkuChange(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">最大混商品数</label></td>
                        <td class="" width="12%">
                            <form:input path="maxMixSku" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">混批次</label></td>
                        <td class="" width="12%">
                            <input id="isMixLot" name="isMixLot" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="mixLotChange(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">最大混批次数量</label></td>
                        <td class="" width="12%">
                            <form:input path="maxMixLot" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">通道</label></td>
                        <td class="" width="12%">
                            <form:input path="lane" htmlEscape="false" class="form-control" maxlength="32"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">序号</label></td>
                        <td class="" width="12%">
                            <form:input path="seq" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">长</label></td>
                        <td class="" width="12%">
                            <form:input path="length" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">宽</label></td>
                        <td class="" width="12%">
                            <form:input path="width" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">高</label></td>
                        <td class="" width="12%">
                            <form:input path="height" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">层</label></td>
                        <td class="" width="12%">
                            <form:input path="floor" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">忽略跟踪号</label></td>
                        <td class="" width="12%">
                            <input id="isLoseId" name="isLoseId" type="checkbox" htmlEscape="false" class="myCheckbox" onclick="isLoseIdChange(this.checked)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">最大体积</label></td>
                        <td class="" width="12%">
                            <form:input path="maxCubic" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">最大重量</label></td>
                        <td class="" width="12%">
                            <form:input path="maxWeight" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">最大托盘数</label></td>
                        <td class="" width="12%">
                            <form:input path="maxPl" htmlEscape="false" class="form-control" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">X坐标</label></td>
                        <td class="" width="12%">
                            <form:input path="x" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">Y坐标</label></td>
                        <td class="" width="12%">
                            <form:input path="y" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">Z坐标</label></td>
                        <td class="" width="12%">
                            <form:input path="z" htmlEscape="false" class="form-control" onkeyup="bq.numberValidator(this, 2, 0)"/>
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
                <h3 class="panel-title">预留信息</h3>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tbody>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">自定义1</label></td>
                        <td class="" width="12%">
                            <form:input path="def1" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义2</label></td>
                        <td class="" width="12%">
                            <form:input path="def2" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义3</label></td>
                        <td class="" width="12%">
                            <form:input path="def3" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义4</label></td>
                        <td class="" width="12%">
                            <form:input path="def4" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">自定义5</label></td>
                        <td class="" width="12%">
                            <form:input path="def5" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义6</label></td>
                        <td class="" width="12%">
                            <form:input path="def6" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义7</label></td>
                        <td class="" width="12%">
                            <form:input path="def7" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义8</label></td>
                        <td class="" width="12%">
                            <form:input path="def8" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    <tr>
                        <td class="" width="8%"><label class="pull-right">自定义9</label></td>
                        <td class="" width="12%">
                            <form:input path="def9" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                        <td class="" width="8%"><label class="pull-right">自定义10</label></td>
                        <td class="" width="12%">
                            <form:input path="def10" htmlEscape="false" class="form-control" maxlength="64"/>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</form:form>
</body>
</html>