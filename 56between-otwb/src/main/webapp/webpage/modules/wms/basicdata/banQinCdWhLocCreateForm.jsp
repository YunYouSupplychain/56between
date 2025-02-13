<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>生成库位</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            // 初始化标签
            init();
        });
        
        /**
         * 初始化
         */ 
        function init() {
            $('#locDetailTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "client",// client客户端分页，server服务端分页
                data: [],
                columns: [{
                    checkbox: true
                },{
                    field: 'locCode',
                    title: '库位编码',
                    sortable: false
                },{
                    field: 'lane',
                    title: '通道',
                    sortable: false
                },{
                    field: 'seq',
                    title: '序号',
                    sortable: false
                },{
                    field: 'floor',
                    title: '层',
                    sortable: false
                },{
                    field: 'createStatus',
                    title: '生成状态',
                    sortable: false
                }]
            });
        }

        /**
         * 生成库位
         */
        function generateLoc() {
            if (!$('#locCode').val()) {
                jp.error('请选择库位模板');
                return;
            }
            // 前缀
            var profix = format($('#profix_gen').val());
            // 分隔符1
            var separator1 = format($('#separator_gen1').val());
            // 起始通道
            var lanFrom = format($('#lanFrom_gen').val());
            // 结束通道
            var lanTo = format($('#lanTo_gen').val());
            // 分隔符2
            var separator2 = format($('#separator_gen2').val());
            // 设值
            var lanDate = format($('#date_gen2').val());
            // 起始层
            var floorFrom = format($('#floorFrom_gen').val());
            // 结束层
            var floorTo = format($('#floorTo_gen').val());
            // 分隔符3
            var separator4 = format($('#separator_gen4').val());
            // 设值
            var floorDate = format($('#date_gen4').val());
            // 起始序列
            var seqFrom = format($('#seqFrom_gen').val());
            // 结束序列
            var seqTo = format($('#seqTo_gen').val());
            // 分隔符4
            var separator3 = format($('#separator_gen3').val());
            // 设值
            var seqDate = format($('#date_gen3').val());
            // 起始位
            var placeFrom = format($('#txt_placeFrom_gen').val());
            // 结束位
            var placeTo = format($('#txt_placeTo_gen').val());

            // 通道数据校验
            var lanLen = lanFrom.length;
            var error = "";
            error = (lanFrom === '0' || lanLen === 0) ? error + "起始通道不能为0" + "\n" : error;
            error = lanFrom.length !== lanTo.length ? error + "起始通道和结束通道的位数要一致" + "\n" : error;
            error = lanFrom > lanTo ? error + "起始通道不能小于结束通道" + "\n" : error;

            // 层数据校验
            var floorLen = floorFrom.length;
            error =  (floorTo.length > 0 && floorTo === '0') ? error + "起始层不能为0" + "\n" : error;
            error = floorFrom.length !== floorTo.length ? error + "起始层和结束层的位数要一致" + "\n" : error;
            error = floorFrom > floorTo ? error + "起始层不能小于结束层" + "\n" : error;

            // 序列数据校验
            var seqLen = seqFrom.length;
            error = (seqFrom === '0' || seqLen === 0) ? error + "起始序列不能为0" + "\n" : error;
            error = seqFrom.length !== seqTo.length ? error + "起始序列和结束序列的位数要一致" + "\n" : error;
            error = seqFrom > seqTo ? error + "起始序列不能小于结束序列" + "\n" : error;

            // 位数据校验
            var placeLen = placeFrom.length;
            error = (placeFrom.length > 0 && placeFrom === '0') ? error + "起始位不能为0" + "\n" : error;
            error = placeFrom.length !== placeTo.length ? error + "起始位和结束位的位数要一致" + "\n" : error;
            error = placeFrom > placeTo ? error + "起始位不能小于位序列" + "\n" : error;

            if (error) {
                jp.error(error);
                return;
            }

            var lans = [];
            var lanF = lanFrom;
            var lanT = lanTo;
            for (var r = lanF; r <= lanT; r++) {
                lans.push(r);
            }
            var floors = [];
            var floorF = floorFrom;
            var floorT = floorTo;
            for (var f = floorF; f <= floorT; f++) {
                floors.push(f);
            }
            var seqs = [];
            var seqF = seqFrom;
            var seqT = seqTo;
            for (var c = seqF; c <= seqT; c++) {
                seqs.push(c);
            }
            var places = [];
            var placeF = placeFrom;
            var placeT = placeTo;
            for (var p = placeF; p <= placeT; p++) {
                places.push(p);
            }

            var locArray = [];
            var sumLength = 0;
            // 本次生成的库位的总数量
            for (var i = 0; i < lans.length; i++) {
                for (var j = 0; j < floors.length; j++) {
                    for (var k = 0; k < seqs.length; k++) {
                        for (var l = 0; l < places.length; l++) {
                            var locModel = {};
                            locModel.locCode = profix + separator1 + getStr(lans[i], lanLen) + separator2 + getStr(seqs[k], seqLen) + separator3 + getStr(floors[j], floorLen) + separator4 + getStr(places[l], placeLen);
                            // 给库位通道，序列，层赋值
                            locModel.lane = lanDate === 'LANE' ? getStr(lans[i], lanLen) : '';
                            locModel.seq = lanDate === 'SEQ' ? getStr(lans[i], seqLen) : '';
                            locModel.floor = lanDate === 'FLOOR' ? getStr(lans[i], floorLen) : '';
                            locModel.lane = seqDate === 'LANE' ? getStr(seqs[k], lanLen) : locModel.lane;
                            locModel.seq = seqDate === 'SEQ' ? getStr(seqs[k], seqLen) : locModel.seq;
                            locModel.floor = seqDate === 'FLOOR' ? getStr(seqs[k], floorLen) : locModel.floor;
                            locModel.lane = floorDate === 'LANE' ? getStr(floors[j], lanLen) : locModel.lane;
                            locModel.seq = floorDate === 'SEQ' ? getStr(floors[j], seqLen) : locModel.seq;
                            locModel.floor = floorDate === 'FLOOR' ? getStr(floors[j], floorLen) : locModel.floor;
                            locModel.orgId = jp.getCurrentOrg().orgId;
                            locArray.push(locModel);
                            sumLength ++;
                        }
                    }
                }
            }
            var params = {locCode: $('#locCode').val(), locList: locArray, sumLength: sumLength};
            jp.loading();
            $.ajax({
                url: "${ctx}/wms/basicdata/banQinCdWhLoc/generateLoc",
                type: "post",
                data: JSON.stringify(params),
                contentType: "application/json",
                success: function (data) {
                    var list = data.body.list;
                    if (list) {
                        jp.success(data.msg);
                        var rows = [];
                        for (var i in list) {
                            rows.push(JSON.parse(JSON.stringify(list[i])));
                        }
                        $("#locDetailTable").bootstrapTable('load', rows);
                    }
                }
            })
        }

        function format(value) {
            return !value ? '' : value;
        }

        function getStr(no, len) {
            if (len > 0 && no !== 0) {
                var str = no;
                if (str.length !== len) {
                    var m = str.length;
                    for (var i = 0; i < len - m; i++) {
                        str = "0" + str;
                    }
                }
                return str;
            } else {
                return "";
            }
        }
        
        function remove() {
            $("#locDetailTable").bootstrapTable('remove', {field: 'locCode', values: getSelections()});
        }

        function getSelections() {
            return $.map($("#locDetailTable").bootstrapTable('getSelections'), function (row) {
                return row.locCode
            });
        }

        /**
         * 保存
         */
        function save() {
            var data = $('#locDetailTable').bootstrapTable('getData');
            if (data.length === 0) {
                jp.bqError("未生成库位，无法保存");
                return;
            }
            jp.loading();
            $.ajax({
                url: "${ctx}/wms/basicdata/banQinCdWhLoc/confirm",
                type: "post",
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function (data) {
                    if (data.success) {
                        jp.success(data.msg);
                        jp.close(parent.layer.getFrameIndex(window.name));
                    } else {
                        jp.bqError(data.msg);
                    }
                }
            })
        }

    </script>
</head>
<body>
<div style="width: 100%; height: 100%;">
    <div id="toolbar" style="padding-left: 10px;">
        <a class="btn btn-primary btn-sm" id="generateLoc" onclick="generateLoc()">生成库位</a>
        <a class="btn btn-primary btn-sm" id="save" onclick="save()">保存</a>
    </div>
    <form:form id="inputForm" modelAttribute="banQinCdWhLoc" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="orgId"/>
    <sys:message content="${message}"/>
    <table class="table table-bordered">
        <tbody>
            <tr>
                <td class="" width="10%"><label class="pull-right">模板库区：</label></td>
                <td class="" width="15%">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhZone/grid" title="选择库区" cssClass="form-control required"
                                   fieldId="zoneCode" fieldName="zoneCode" fieldKeyName="zoneCode" fieldValue="${banQinCdWhLoc.zoneCode}"
                                   displayFieldId="zoneName" displayFieldName="zoneName" displayFieldKeyName="zoneName" displayFieldValue="${banQinCdWhLoc.zoneName}"
                                   selectButtonId="zoneSelectId" deleteButtonId="zoneDeleteId"
                                   fieldLabels="库区编码|库区名称" fieldKeys="zoneCode|zoneName"
                                   searchLabels="库区编码|库区名称" searchKeys="zoneCode|zoneName" disabled="disabled">
                    </sys:popSelect>
                </td>
                <td class="" width="10%"><label class="pull-right"><font color="red">*</font>模板库位：</label></td>
                <td class="" width="15%">
                    <sys:popSelect url="${ctx}/wms/basicdata/banQinCdWhLoc/grid" title="选择库位" cssClass="form-control required"
                                   fieldId="" fieldName="" fieldKeyName="" fieldValue="${banQinCdWhLoc.locCode}" allowInput="true"
                                   displayFieldId="locCode" displayFieldName="locCode" displayFieldKeyName="locCode" displayFieldValue="${banQinCdWhLoc.locCode}"
                                   selectButtonId="locSelectId" deleteButtonId="locDeleteId"
                                   fieldLabels="库位编码|库位状态|使用类型|库区编码" fieldKeys="locCode|status|locUseType|zoneCode"
                                   searchLabels="库位编码" searchKeys="locCode" inputSearchKey="codeAndName"
                                   queryParams="zoneCode" queryParamValues="zoneCode">
                    </sys:popSelect>
                </td>
                <td class="" width="10%"></td>
                <td class="" width="15%"></td>
                <td class="" width="10%"></td>
                <td class="" width="15%"></td>
            </tr>
            <tr>
                <td class="" width="10%"><label class="pull-right">前缀：</label></td>
                <td class="" width="15%">
                    <input id="profix_gen" name="profix_gen" class="form-control" maxlength="10" />
                </td>
                <td class="" width="10%"><label class="pull-right">分隔符：</label></td>
                <td class="" width="15%">
                    <input id="separator_gen1" name="separator_gen1" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^@#*-]/g,'')"/>
                </td>
                <td class="" width="10%"></td>
                <td class="" width="15%"></td>
                <td class="" width="10%"></td>
                <td class="" width="15%"></td>
            </tr>
            <tr>
                <td class="" width="10%"><label class="pull-right">起始通道：</label></td>
                <td class="" width="15%">
                    <input id="lanFrom_gen" name="lanFrom_gen" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">结束通道：</label></td>
                <td class="" width="15%">
                    <input id="lanTo_gen" name="lanTo_gen" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">分隔符：</label></td>
                <td class="" width="15%">
                    <input id="separator_gen2" name="separator_gen2" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^@#*-]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">值设为：</label></td>
                <td class="" width="15%">
                    <select id="date_gen2" name="date_gen2" class="form-control m-b">
                        <option value=""></option>
                        <c:forEach items="${fns:getDictList('SYS_WM_LANE_SEQ_FLOOR')}" var="dict">
                            <option value="${dict.value}">${dict.label}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="" width="10%"><label class="pull-right">起始序号：</label></td>
                <td class="" width="15%">
                    <input id="seqFrom_gen" name="seqFrom_gen" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">结束序号：</label></td>
                <td class="" width="15%">
                    <input id="seqTo_gen" name="seqTo_gen" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">分隔符：</label></td>
                <td class="" width="15%">
                    <input id="separator_gen3" name="separator_gen3" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^@#*-]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">值设为：</label></td>
                <td class="" width="15%">
                    <select id="date_gen3" name="date_gen3" class="form-control m-b">
                        <option value=""></option>
                        <c:forEach items="${fns:getDictList('SYS_WM_LANE_SEQ_FLOOR')}" var="dict">
                            <option value="${dict.value}">${dict.label}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="" width="10%"><label class="pull-right">起始层：</label></td>
                <td class="" width="15%">
                    <input id="floorFrom_gen" name="floorFrom_gen" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">结束层：</label></td>
                <td class="" width="15%">
                    <input id="floorTo_gen" name="floorTo_gen" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">分隔符：</label></td>
                <td class="" width="15%">
                    <input id="separator_gen4" name="separator_gen4" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^@#*-]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">值设为：</label></td>
                <td class="" width="15%">
                    <select id="date_gen4" name="date_gen4" class="form-control m-b">
                        <option value=""></option>
                        <c:forEach items="${fns:getDictList('SYS_WM_LANE_SEQ_FLOOR')}" var="dict">
                            <option value="${dict.value}">${dict.label}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td class="" width="10%"><label class="pull-right">起始位：</label></td>
                <td class="" width="15%">
                    <input id="placeFrom_gen" name="placeFrom_gen" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
                <td class="" width="10%"><label class="pull-right">结束位：</label></td>
                <td class="" width="15%">
                    <input id="placeTo_gen" name="placeTo_gen" class="form-control" maxlength="3" onkeyup="value=value.replace(/[^\d]/g,'')"/>
                </td>
            </tr>
        </tbody>
    </table>
    </form:form>
    <div style="background-color: #2c3e50; width: 100%; height: 30px; color: white; line-height: 30px; margin-left: 10px;">&nbsp;&nbsp;库位明细</div>
    <div id="locDetailToolbar" style="width: 100%; padding: 5px 10px;">
        <a class="btn btn-danger btn-sm" id="remove" onclick="remove()">删除</a>
    </div>
    <div style="overflow: scroll; height: 300px; padding-left: 10px;">
        <table id="locDetailTable" class="text-nowrap"></table>
    </div>
</body>
</html>