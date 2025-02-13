<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>查看车辆位置</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <style>
        html, body, #map {
            height: 100%;
            overflow-y: hidden;
        }

        .infowindow {
            position: relative;
            width: 365px;
            background-color: #fff;
            font-family: '微软雅黑', serif;
            color: #323232;
            font-size: 12px;
            box-shadow: 0 0 10px rgb(0 0 0);
            border-radius: 10px;
            overflow: hidden;
            -webkit-user-select: text;
            -moz-user-select: text;
            -ms-user-select: text;
            user-select: text;
            top: -8px
        }

        .pop-infowindow.infowindow {
            overflow: visible;
            padding-top: 0;
            min-height: 220px;
            max-height: 250px;
        }

        .infowindow-title {
            height: 40px;
            background: #96a1a7;
            border-radius: 10px 10px 0 0;
            color: #fff;
            padding-left: 50px;
            font-size: 12px;
            position: relative;
        }

        .pop-infowindow.infowindow .close {
            color: #fff;
            text-shadow: none;
            opacity: .8;
            z-index: 4;
        }

        .infowindow .close {
            position: absolute;
            right: 8px;
            top: 8px;
        }

        .btn, a:link, button {
            -webkit-tap-highlight-color: rgba(169, 3, 41, .5);
        }

        .iconfont {
            font-family: "iconfont", serif !important;
            font-size: 16px;
            font-style: normal;
            -webkit-font-smoothing: antialiased;
            -moz-osx-font-smoothing: grayscale;
        }

        .close {
            float: right;
            font-size: 21px;
            font-weight: 700;
            line-height: 1;
            color: #000;
            text-shadow: 0 1px 0 #fff;
            filter: alpha(opacity=20);
            opacity: .2;
        }

        .icon-close:before {
            content: "×";
        }

        .infowindow-title .car-img {
            position: absolute;
            top: 6px;
            left: 12px;
            opacity: 0.4;
            width: 32px;
            vertical-align: middle;
            border: 0;
        }

        .infowindow-title .inner-font {
            border-left: 1px solid #c0c0c0;
            height: 100%;
            margin-left: 6px;
            padding-left: 12px;
        }

        .pop-infowindow table {
            width: 100%;
            padding: 0;
            cursor: text;
        }

        .pop-infowindow .tab-td-label {
            width: 80px;
            text-align: right;
        }

        .little-corner {
            width: 0;
            height: 0;
            position: absolute;
            left: 50%;
            margin-top: -9px;
            margin-left: -3px;
            border: 7px solid transparent;
            border-top: 8px solid white;
        }

        .pop-infowindow.infowindow {
            margin-bottom: 5px;
        }

        .little-corner {
            margin-top: -15px;
            margin-left: -8px;
        }
    </style>
</head>
<body>
<div id="map"></div>
<script type="text/template" id="infoWindowTpl">//<!--
<div class="infowindow pop-infowindow" gpsno="{{row.gpsNo}}"><a href="javascript:;" class="iconfont icon-close close"></a>
    <div class="infowindow-title" style="background: rgb(36, 142, 228);"><img src="${ctxStatic}/images/car.png" class="car-img">
        <div class="inner-font">
            <p><b class="pwCarnum">{{row.no}}</b></p>
            <p style="margin-top:-8px;">
                <span class="pwTrailMatch" style="display:none;margin-right:5px;">
                匹配：<span class="pwTrailMatch_txt"></span></span>
            </p>
        </div>
    </div>
    <div style="margin: 10px">
        <table>
            <tbody>
            <tr class="speed">
                <td class="tab-td-label">速度(km/h)：</td>
                <td>{{row.speed}}</td>
            </tr>
            <tr class="address">
                <td class="tab-td-label">当前位置：</td>
                <td>{{row.address}}</td>
            </tr>
            <tr class="gpsTime">
                <td class="tab-td-label">定位时间：</td>
                <td>{{row.time}}</td>
            </tr>
            <tr class="temp">
                <td class="tab-td-label">温度(℃)：</td>
                <td>
                    <span class="temp-item t-normal">一区:{{row.temperature.t1}}</span>
                    <span class="temp-item t-normal">二区:{{row.temperature.t2}}</span>
                    <span class="temp-item t-normal">三区:{{row.temperature.t3}}</span>
                    <span class="temp-item t-normal">四区:{{row.temperature.t4}}</span>
                </td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<div class="little-corner"><span class="inner"></span></div>//-->
</script>
<script>
    var infoWindowTpl;
    $(document).ready(function () {
        if (!`${ak}`) {
            top.layer.closeAll('iframe');
            jp.warning("百度地图授权码[ MAP_WEB_CLIENT_AK ]未维护，请前往系统控制参数维护！");
            return;
        }
        infoWindowTpl = $("#infoWindowTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
        let script = document.createElement("script");
        script.src = "https://api.map.baidu.com/api?v=3.0&ak=${ak}&callback=loadJs";
        script.type = "text/javascript";
        document.body.appendChild(script);
    });

    function loadJs() {
        let TextIconOverlayScript = document.createElement("script");
        TextIconOverlayScript.src = "${ctxStatic}/plugin/BMapLib/TextIconOverlay.min.js";
        TextIconOverlayScript.async = false;
        document.body.appendChild(TextIconOverlayScript);
        let MarkerClustererScript = document.createElement("script");
        MarkerClustererScript.src = "${ctxStatic}/plugin/BMapLib/MarkerClusterer.min.js";
        MarkerClustererScript.async = false;
        document.body.appendChild(MarkerClustererScript);
        let InfoBoxScript = document.createElement("script");
        InfoBoxScript.src = "${ctxStatic}/plugin/BMapLib/InfoBox.min.js";
        InfoBoxScript.async = false;
        InfoBoxScript.onload = initialize;
        document.body.appendChild(InfoBoxScript);
    }

    function initialize() {
        window.map = new BMap.Map("map", {coordsType: 5});
        map.centerAndZoom(new BMap.Point(109.055814, 32.674044), 6);// 初始化地图，设置中心点坐标和地图级别
        map.enableScrollWheelZoom(true);//开启鼠标滚轮缩放
        map.addControl(new BMap.NavigationControl());//左上角，添加默认缩放平移控件
        window.cluster = new BMapLib.MarkerClusterer(map);// 点聚合

        load();
    }

    function load() {
        var allRows = ${fns:toJson(dataList)};
        $('#all-num').text(allRows.length);
        loadMarkerClusterer(allRows);
    }

    function loadMarkerClusterer(rows) {
        cluster.clearMarkers();
        for (let i = 0; i < rows.length; i++) {
            let row = rows[i];
            if (row.lon === undefined || row.lat === undefined) {
                continue;
            }
            (function (row) {
                pointTranslate(new BMap.Point(row.lon, row.lat), function (point) {
                    let marker = new BMap.Marker(point, {icon: new BMap.Icon(ctxStatic + "/images/truck_moving.png", new BMap.Size(30, 30)), rotation: row.course});
                    marker.addEventListener('click', function () {
                        clickMarker(point, row);
                    });
                    cluster.addMarker(marker);
                });
            })(row);
        }
    }

    function clickMarker(position, row) {
        if (row === undefined) {
            return;
        }
        closeInfoBox();
        if (!row.hasOwnProperty("temperature") || !row.temperature) {
            row.temperature = {t1: -99999, t2: -99999, t3: -99999, t4: -99999};
        }
        window.infoBox = new BMapLib.InfoBox(map, Mustache.render(infoWindowTpl, {row: row}));
        window.infoBox.open(position);
        $('.icon-close').click(closeInfoBox);
        if (map.getZoom() < 12) {
            map.setZoom(12);
        }
        map.panTo(position);
    }

    function closeInfoBox() {
        if (window.infoBox) {
            window.infoBox.close();
        }
    }

    function pointTranslate(point, callback) {
        new BMap.Convertor().translate([point], 3, 5, function (data) {
            if (data.status == 0) {
                callback(data.points[0]);
            }
        });
    }
</script>
</body>
</html>