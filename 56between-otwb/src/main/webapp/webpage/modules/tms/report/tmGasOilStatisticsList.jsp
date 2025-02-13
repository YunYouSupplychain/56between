<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>加油站卸油统计表</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/echarts.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <script type="text/javascript">
        $(function () {
            $('#baseOrgId').val(tmOrg.id);
            $('#search').click(function () {
                var index = jp.loading("加载中...");
                var validator = bq.headerSubmitCheck('#searchForm');
                if (!validator.isSuccess) {
                    jp.warning(validator.msg);
                    return;
                }

                initChart("DAY");
                initChart("MONTH");
                jp.close(index);
            });
            $('#reset').click(function () {
                $("#searchForm input").val("");
                $("#searchForm select").val("");
                $("#searchForm .select-item").html("");
            });
        });

        function initChart(chart) {
            document.getElementById(chart).removeAttribute('_echarts_instance_');
            var myChart = echarts.init(document.getElementById(chart));

            var searchParam = $("#searchForm").serializeJSON();
            searchParam.timeDimension = chart;
            searchParam.baseOrgId = tmOrg.id;
            searchParam.orgId = jp.getCurrentOrg().orgId;
            $.ajax({
                type: "POST",
                url: "${ctx}/tms/report/gasOilStatistics/data",
                data: searchParam,
                async: false,
                success: function (data) {
                    myChart.setOption(getOption(data, chart === "DAY" ? "(近1月)" : "(近1年)"));
                }
            });
        }

        function getOption(data, title) {
            var legendData = [], xAxis = [], seriesMap = {}, series = [];
            for (var i = 0; i < data.length; i++) {
                var row = data[i];
                if (row.hasOwnProperty("oilName")) {
                    if (legendData.indexOf(row.oilName) === -1) {
                        legendData.push(row.oilName);
                    }
                    if (seriesMap.hasOwnProperty(row.oilName)) {
                        seriesMap[row.oilName].push(row.qty);
                    } else {
                        seriesMap[row.oilName] = [row.qty];
                    }
                }
                if (row.hasOwnProperty("date") && xAxis.indexOf(row.date) === -1) {
                    xAxis.push(row.date);
                }
            }
            for (var oilName in seriesMap) {
                series.push({name: oilName, type: 'line', data: seriesMap[oilName]});
            }
            return {
                title: {text: $('#gasName').val() + '加油站卸油统计表' + title},
                tooltip: {trigger: 'axis', axisPointer: {type: 'cross', label: {backgroundColor: '#6a7985'}}},
                legend: {data: legendData, x: 'right', padding: [0, 120, 0, 0]},
                grid: {left: '3%', bottom: '8%', containLabel: true},
                toolbox: {show: true, feature: {saveAsImage: {show: true}}, top: -10, right: 60},
                xAxis: {type: 'category', boundaryGap: false, data: xAxis, axisLabel: {interval: 0, rotate: 48}},
                yAxis: {type: 'value'},
                series: series
            };
        }
    </script>
</head>
<body class="bg-white">
<div class="hidden">
    <input type="hidden" id="baseOrgId"/>
</div>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">加油站卸油统计表</h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>

            <form:form id="searchForm" modelAttribute="tmGasOilStatistics" class="form form-horizontal clearfix" onsubmit="return false;">
                <table class="table table-striped table-bordered table-condensed">
                    <tr>
                        <td style="width:10%;"><label class="pull-right"><font color="red">*</font>加油站：</label></td>
                        <td style="width:15%;">
                            <sys:grid title="加油站" url="${ctx}/tms/report/gasOilStatistics/getGas"
                                      fieldId="gasCode" fieldName="gasCode" fieldKeyName="gasCode"
                                      displayFieldId="gasName" displayFieldName="gasName" displayFieldKeyName="gasName"
                                      fieldLabels="加油站编码|加油站名称" fieldKeys="gasCode|gasName"
                                      searchLabels="加油站编码|加油站名称" searchKeys="gasCode|gasName"
                                      queryParams="orgId" queryParamValues="baseOrgId"
                                      cssClass="form-control required"/>
                        </td>
                        <td style="width:75%;"></td>
                    </tr>
                </table>
            </form:form>

            <!-- 工具栏 -->
            <div id="toolbar">
                <a id="search" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-search"></i> 查询</a>
                <a id="reset" class="btn btn-primary btn-rounded  btn-bordered btn-sm"><i class="fa fa-refresh"></i> 重置</a>
                <%--<shiro:hasPermission name="tms:report:gasOilStatistics:export">
                    <a id="export" class="btn btn-info"><i class="glyphicon glyphicon-export icon-share"></i> 导出</a>
                </shiro:hasPermission>--%>
            </div>
        </div>
    </div>
</div>
<div style="width: 100%;height: 600px;margin-top: 10px;">
    <div style="width: 100%;height: 100%;margin-bottom: 10px">
        <div id="DAY" style="width: 100%;height: 100%"></div>
    </div>
    <div style="width: 100%;height: 100%;">
        <div id="MONTH" style="width: 100%;height: 100%"></div>
    </div>
</div>
</body>
</html>