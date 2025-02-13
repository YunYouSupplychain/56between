<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    <meta name="decorator" content="ani"/>
    <!-- 引入 echarts.js -->
    <%@ include file="/webpage/include/echarts.jsp" %>
</head>
<body class="bg-white">
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="main" style="width: 100%;height: 100%"></div>
<script type="text/javascript">
    // 基于准备好的dom，初始化echarts实例4
    var myChart = echarts.init(document.getElementById('main'));
    window.onresize = myChart.resize;
    $(function () {
        jp.get("${ctx}${dataURL}", function (option) {
            // 指定图表的配置项和数据
            // 使用刚指定的配置项和数据显示图表。
            myChart.setOption(option);
        })
    })
</script>
</body>
</html>