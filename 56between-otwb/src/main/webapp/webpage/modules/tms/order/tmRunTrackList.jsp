<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <title>车辆位置</title>
    <script src="${ctxStatic}/plugin/jquery/3.5.1/jquery-3.5.1.min.js" type="application/javascript"></script>
    <script src="${ctxStatic}/common/js/56between.js" type="application/javascript"></script>
    <style type="text/css">
        body, html {
            width: 100%;
            height: 100%;
            margin: 0;
        }

        #all-map {
            height: 100%;
            width: 100%;
        }

        #r-result {
            width: 100%;
            font-size: 14px;
        }
    </style>
</head>
<body>
<div id="all-map"></div>
</body>
<script>
    const roadIcons = [
        '${ctxStatic}/images/Icon_road_blue_arrow.png',
        '${ctxStatic}/images/Icon_road_green_arrow.png',
        '${ctxStatic}/images/Icon_road_red_arrow.png'
    ]
    var loadPoints = [];

    function loadJs() {
        let LuShuScript = document.createElement("script");
        LuShuScript.src = "${ctxStatic}/plugin/BMapGLLib/LuShu_GL_min.js";
        LuShuScript.async = false;
        LuShuScript.onload = initialize;
        document.body.appendChild(LuShuScript);
    }

    function luShu(vehicleNo, points) {
        let polyline = new BMapGL.Polyline(points, {
            // 折线的宽度，以像素为单位
            strokeWeight: '6',
            // 折线的透明度，取值范围0 - 1
            strokeOpacity: 0.9,
            // 折线颜色
            strokeColor: '#33CC33'
        });
        map.addOverlay(polyline);
        let luShu = new BMapGLLib.LuShu(map, polyline.getPath(), {
            // 是否开启自动视野调整，如果开启那么路书在运动过程中会根据视野自动调整
            autoView: true,
            enableRotation: true,
            icon: carIcon,
            defaultContent: '',
            speed: 100
        });
        luShu.start();
    }

    function lineLayer(dataList = []) {
        let features = [], i = 0;
        let strokeTextureUrlStyleExpress = ['match', ['get', 'name']]
        for (let vehicleNo in dataList) {
            let points = dataList[vehicleNo];
            let length = points.length;

            strokeTextureUrlStyleExpress.push(vehicleNo);
            strokeTextureUrlStyleExpress.push(roadIcons[i++ % roadIcons.length]);

            let coordinates = [];
            $.map(points, function (row) {
                coordinates.push([row.lng, row.lat]);
            });
            features.push({
                "type": "Feature",
                "properties": {"name": vehicleNo},
                "geometry": {
                    "type": "LineString",
                    "coordinates": coordinates
                }
            });

            // 创建Marker标注，使用小车图标
            let point = new BMapGL.Point(points[length - 1].lng, points[length - 1].lat);
            geocoder(point, function (result) {
                let infoWindow = new BMapGL.InfoWindow(`<img style='float:left;margin:0 4px 22px' src='${ctxStatic}/images/Icon_wuliu.png'/><p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>` + result.address + `</p></div>`, {
                    width: 200,
                    title: vehicleNo
                });
                let marker = new BMapGL.Marker(point, {
                    icon: carIcon,
                    title: vehicleNo,
                    offset: new BMapGL.Size(3, -15)
                });
                // 点击标点时打开信息窗体
                marker.addEventListener('click', function () {
                    this.openInfoWindow(infoWindow);
                    let viewport = map.getViewport(points);
                    map.centerAndZoom(point, viewport.zoom);
                });
                // 信息窗体关闭时还原地图中心点位及zoom
                infoWindow.addEventListener('close', function () {
                    map.setViewport(loadPoints);
                });
                // 将标注添加到地图
                map.addOverlay(marker);
            });
        }
        strokeTextureUrlStyleExpress.push(roadIcons[0]);

        let lineLayer = new BMapGL.LineLayer({
            enablePicked: true,
            autoSelect: true,
            pickWidth: 30,
            pickHeight: 30,
            opacity: 1,
            selectedColor: 'blue', // 选中项颜色
            style: {
                sequence: false, // 是否采用间隔填充纹理，默认false
                marginLength: 8, // 间隔距离，默认16，单位像素
                borderColor: '#999',
                borderMask: true, // 是否受内部填充区域掩膜，默认true,如果存在borderWeight小于0，则自动切换false
                borderWeight: 0, // 描边宽度，可以设置负值
                strokeWeight: 8, // 描边线宽度，默认0
                strokeLineJoin: 'miter',// 描边线连接处类型, 可选'miter', 'round', 'bevel'
                strokeLineCap: 'round',// 描边线端头类型，可选'round', 'butt', 'square'，默认round
                strokeTextureUrl: strokeTextureUrlStyleExpress,// 填充纹理图片地址，默认是空
                strokeTextureWidth: 16,
                strokeTextureHeight: 64
            }
        });
        lineLayer.setData({
            "type": "FeatureCollection",
            "features": features
        });
        map.addNormalLayer(lineLayer);
    }

    function geocoder(point, fnc) {
        // 创建地理编码实例
        let myGeo = new BMapGL.Geocoder();
        // 根据坐标得到地址描述
        myGeo.getLocation(point, function (result) {
            if (result && typeof fnc === 'function') {
                fnc(result);
            }
        }, {
            poiRadius: 10,
            numPois: 2
        });
    }

    function initialize() {
        // 创建地图
        map = new BMapGL.Map("all-map");
        map.centerAndZoom(new BMapGL.Point(108.552500, 34.322700), 6);
        map.enableScrollWheelZoom(true);
        // 创建小车图标
        carIcon = new BMapGL.Icon("${ctxStatic}/images/Icon_wuliu.png", new BMapGL.Size(30, 30));

        let dataList = ${fns:toJson(dataList)};
        for (let vehicleNo in dataList) {
            let points = dataList[vehicleNo];
            /*luShu(vehicleNo, points);*/
            loadPoints = loadPoints.concat(points);
        }
        lineLayer(dataList);
        map.setViewport(loadPoints);
    }

    $(document).ready(function () {
        if (!`${ak}`) {
            top.layer.closeAll('iframe');
            jp.warning("百度地图授权码[ MAP_WEB_CLIENT_AK ]未维护，请前往系统控制参数维护！");
            return;
        }
        let script = document.createElement("script");
        script.src = "https://api.map.baidu.com/api?type=webgl&v=3.0&ak=${ak}&callback=loadJs";
        script.type = "text/javascript";
        document.body.appendChild(script);
    });
</script>
</html>