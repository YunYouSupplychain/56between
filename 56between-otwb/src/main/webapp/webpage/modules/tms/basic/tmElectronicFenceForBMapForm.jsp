<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <title>维护标注</title>
    <script src="${ctxStatic}/plugin/jquery/3.5.1/jquery-3.5.1.min.js"></script>
    <%--百度地图API--%>
    <script type="text/javascript" src="https://api.map.baidu.com/api?type=webgl&v=3.0&ak=${ak}"></script>
    <%--鼠标绘制工具--%>
    <link href="${ctxStatic}/plugin/BMapGLLib/DrawingManager.min.css" rel="stylesheet">
    <script src="${ctxStatic}/plugin/BMapGLLib/DrawingManager.min.js"></script>
    <%--<link href="${ctxStatic}/plugin/BMapGLLib/GeoUtils.min.css" rel="stylesheet">--%>
    <script src="${ctxStatic}/plugin/BMapGLLib/GeoUtils.min.js"></script>
    <script src="${ctxStatic}/plugin/BMapGLLib/InfoBox.min.js"></script>
    <%--信息窗口--%>
    <link href="${ctxStatic}/common/css/app-midnight-blue.css" rel="stylesheet">
    <link href="${ctxStatic}/common/css/bqStyle.css" rel="stylesheet">
    <script src="${ctxStatic}/common/js/56between.js"></script>
    <script type="text/javascript">var ctx = '${ctx}'</script>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <link href="${ctxStatic}/tms/basic/tmElectronicFenceForm.css" rel="stylesheet">
</head>
<body>
<div id="b-map" style="width: 100%; height: 100%;"></div>
<div class="address-search">
    <input type="text" id="address-search-input" class="form-control" placeholder="搜索地址..." autocomplete="off"/>
    <div id="address-search-result"></div>
</div>
<div class="map-marker-tool">
    <div class="btn-marker-point" id="btn-marker-point" data-tip="请在地图上选择一个中心点" title="标注点" style="display: block;" onclick="drawing(BMAP_DRAWING_MARKER)">
        <i class="icon-marker-point"></i>
    </div>
    <div class="btn-marker-polygon" id="btn-marker-polygon" data-tip="请在地图上通过鼠标点击画定一个区域" title="标注区域" style="display: block;" onclick="drawing(BMAP_DRAWING_POLYGON)">
        <i class="icon-marker-polygon"></i>
    </div>
</div>
<div id="map-marker-save">
    <form:form id="tmElectronicFenceForm" modelAttribute="tmElectronicFence" class="form-horizontal" onsubmit="return false;">
        <form:hidden path="id"/>
        <form:hidden path="recVer"/>
        <form:hidden path="orgId"/>
        <form:hidden path="centerLongitude"/>
        <form:hidden path="centerLatitude"/>
        <div class="col-md-12">
            <div class="col-xs-12 col-sm-6 col-md-6">
                <label class="label-item single-overflow pull-left" title="名称：">名称：</label>
                <form:input path="fenceName" class="form-control required"/>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6">
                <label class="label-item single-overflow pull-left" title="编码：">编码：</label>
                <form:input path="fenceCode" class="form-control" readonly="true"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="col-xs-12 col-sm-12 col-md-12">
                <label class="label-item single-overflow pull-left" title="机构：">机构：</label>
                <form:input path="orgName" class="form-control" readonly="true"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="col-xs-12 col-sm-12 col-md-12">
                <label class="label-item single-overflow pull-left" title="中心点坐标：">中心点坐标：</label>
                <input type="text" id="centerPoint" name="centerPoint" class="form-control"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="col-xs-12 col-sm-12 col-md-12">
                <label class="label-item single-overflow pull-left" title="省份：">省份：</label>
                <form:input path="fenceProvince" class="form-control"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="col-xs-12 col-sm-12 col-md-12">
                <label class="label-item single-overflow pull-left" title="城市：">城市：</label>
                <form:input path="fenceCity" class="form-control"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="col-xs-12 col-sm-12 col-md-12">
                <label class="label-item single-overflow pull-left" title="区：">区：</label>
                <form:input path="fenceDistrict" class="form-control"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="col-xs-12 col-sm-12 col-md-12">
                <label class="label-item single-overflow pull-left" title="地址：">地址：</label>
                <form:input path="fenceAddress" class="form-control"/>
            </div>
        </div>
        <div class="col-md-12">
            <div class="col-xs-12 col-sm-6 col-md-6">
                <label class="label-item single-overflow pull-left" title="类型：">类型：</label>
                <form:select path="fenceType" class="form-control" disabled="true">
                    <form:option value="" label=""/>
                    <form:options items="${fns:getDictList('TMS_ELECTRONIC_FENCE_TYPE')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </div>
            <div class="col-xs-12 col-sm-6 col-md-6 radius">
                <label class="label-item single-overflow pull-left" title="范围（米）：">范围（米）：</label>
                <form:input path="radius" class="form-control" onkeyup="radiusChange(this);"/>
            </div>
        </div>
    </form:form>
    <div class="col-md-12">
        <div class="col-xs-12 col-sm-6 col-md-6">
            <button type="button" id="cancel" class="btn btn-default" onclick="cancel()">取消</button>
        </div>
        <div class="col-xs-12 col-sm-6 col-md-6">
            <button type="button" id="submit" class="btn btn-default" onclick="save()">保存</button>
        </div>
    </div>
</div>
</body>
<script type="application/javascript">
    var radius = 500, $drawObj;

    /**
     * 开启绘制工具
     * drawingMode 绘制模式
     * BMAP_DRAWING_MARKER 点
     * BMAP_DRAWING_CIRCLE 圆
     * BMAP_DRAWING_POLYLINE 线
     * BMAP_DRAWING_POLYGON 多边形
     * BMAP_DRAWING_RECTANGLE 矩形
     */
    function drawing(drawingMode) {
        // 清除地图上所有覆盖物
        map.clearOverlays();
        drawingManager.setDrawingMode(drawingMode);
        drawingManager.open();
    }

    /**
     * 添加覆盖物 - 点
     * @param point 坐标点
     */
    function addMarker(point) {
        // 创建标注点
        var marker = new BMapGL.Marker(point, getStyleOptions());
        // 添加覆盖物
        map.addOverlay(marker);
        // 重设地图中心点
        map.centerAndZoom(point, 16);
        // 获取坐标点位置信息，并鼠标移入时显示，移除时隐藏
        getLocation(point, function (result) {
            if (result) {
                var address = result.addressComponents.streetNumber || result.surroundingPois.length <= 0 ? result.address : result.surroundingPois[0].address + result.surroundingPois[0].title;
                var infoBox = new BMapGLLib.InfoBox(map, ["<div>" + address + "</div>"], {
                    boxStyle: {
                        width: "200px",
                        minHeight: "60px",
                        padding: "10px",
                        fontSize: "14px",
                        background: "#fff"
                    },
                    disableClose: true,
                });
                marker.addEventListener("onmouseover", function () {
                    infoBox.open(marker);
                });
                marker.addEventListener("onmouseout", function () {
                    infoBox.close(marker);
                });
            }
        });
    }

    /**
     * 添加覆盖物 - 圆
     * @param point 中心点
     * @param radius 半径(米)
     */
    function addCircle(point, radius) {
        addMarker(point);
        $drawObj = new BMapGL.Circle(point, radius, getStyleOptions());
        map.addOverlay($drawObj);
    }

    /**
     * 添加覆盖物 - 多边形
     * @param points 点
     * @param centerPoint 中心点
     */
    function addPolygon(points, centerPoint) {
        addMarker(centerPoint);
        $drawObj = new BMapGL.Polygon(points, getStyleOptions());
        map.addOverlay($drawObj);
    }

    /**
     * 计算多组坐标点的中心点
     */
    function getCenterPoint(points) {
        var x = 0.0;
        var y = 0.0;
        for (var i = 0; i < points.length; i++) {
            x = x + points[i].lng;
            y = y + points[i].lat;
        }
        x = x / points.length;
        y = y / points.length;
        return new BMapGL.Point(x, y);
    }

    /**
     * 获取指定坐标点位置信息
     * @param point 坐标点
     * @param func 回调函数
     */
    function getLocation(point, func) {
        new BMapGL.Geocoder().getLocation(point, func, {poiRadius: 500, numPois: 3});
    }

    /**
     * 检索地点
     * @param place 检索地点
     */
    function searchPlace(place) {
        // 清除地图上所有覆盖物
        map.clearOverlays();
        // 智能搜索
        var localSearch = new BMapGL.LocalSearch(map, {
            onSearchComplete: function (results) {
                if (localSearch.getStatus() == BMAP_STATUS_SUCCESS) {
                    addMarker(results.getPoi(0).point);
                }
            }
        });
        localSearch.search(place);
    }

    /**
     * 范围change
     */
    function radiusChange(obj) {
        bq.numberValidator(obj, 0, 0);
        var radius = $('#radius').val();
        if (!radius) {
            return;
        }
        // 获取中心点坐标
        var point = $drawObj.getCenter();
        // 清除地图上所有覆盖物
        map.clearOverlays();
        addMarker(point);
        addCircle(point, radius);
    }

    /**
     * 取消
     */
    function cancel() {
        $('#map-marker-save').css("display", "none");
        $('#tmElectronicFenceForm>table').remove();
        // 清除地图上所有覆盖物
        map.clearOverlays();
        if ($('#id').val().length > 0) {
            initForm(getInitValue());
        }
    }

    /**
     * 保存
     */
    function save() {
        jp.loading();
        var disabledObjs = bq.openDisabled('#tmElectronicFenceForm');
        var params = $('#tmElectronicFenceForm').serialize();
        bq.closeDisabled(disabledObjs);
        jp.post("${ctx}/tms/basic/tmElectronicFence/save", params, function (data) {
            if (data.success) {
                jp.success(data.msg);
                jp.close();
            } else {
                jp.bqError(data.msg);
            }
        });
    }

    function getInitValue() {
        var tmElectronicFence = ${fns:toJson(tmElectronicFence)};
        tmElectronicFence.points = [];
        if (tmElectronicFence.hasOwnProperty("pointList") && tmElectronicFence.pointList) {
            var pointList = tmElectronicFence.pointList;
            for (var i = 0; i < pointList.length; i++) {
                tmElectronicFence.points.push(new BMapGL.Point(pointList[i].longitude, pointList[i].latitude));
            }
        }
        return tmElectronicFence;
    }

    function getStyleOptions() {
        return {
            strokeColor: '#5E87DB',// 边线颜色
            fillColor: '#5E87DB',  // 填充颜色。当参数为空时，圆形没有填充颜色
            strokeWeight: 2,       // 边线宽度，以像素为单位
            strokeOpacity: 1,      // 边线透明度，取值范围0-1
            fillOpacity: 0.2       // 填充透明度，取值范围0-1
        };
    }

    /**
     * 初始化Form表单
     */
    function initForm(tmElectronicFence) {
        // 清除地图上所有覆盖物
        map.clearOverlays();
        $('#map-marker-save').css("display", "block");
        for (var id in tmElectronicFence) {
            $('#' + id).val(tmElectronicFence[id]);
        }
        $('#centerPoint').val(tmElectronicFence.centerLongitude + "," + tmElectronicFence.centerLatitude);
        if (tmElectronicFence.id) {
            $('#fenceCode').attr('readonly', true);
            $('#orgName').attr('readonly', true);
            $('#centerPoint').attr('readonly', true);
        }

        if (tmElectronicFence.fenceType === BMAP_DRAWING_MARKER || tmElectronicFence.fenceType === BMAP_DRAWING_CIRCLE) {
            $('.radius').show();
            $('#radius').val(tmElectronicFence.radius);
            // 添加覆盖物 - 圆
            addCircle(new BMapGL.Point(tmElectronicFence.centerLongitude, tmElectronicFence.centerLatitude), tmElectronicFence.radius);
        } else if (tmElectronicFence.fenceType === BMAP_DRAWING_POLYGON) {
            $('.radius').hide();
            var str = "";
            var points = tmElectronicFence.points;
            for (var i = 0; i < points.length; i++) {
                str += '<tr id="pointList' + i + '">' +
                    '<td><input name="pointList[' + i + '].longitude" value="' + points[i].lng + '"/></td>' +
                    '<td><input name="pointList[' + i + '].latitude" value="' + points[i].lat + '"/></td>' +
                    '</tr>';
            }
            $('#tmElectronicFenceForm').append('<table style="display: none;"><tbody id="pointList">' + str + '</tbody></table>');
            // 添加覆盖物 - 多边形
            addPolygon(points, new BMapGL.Point(tmElectronicFence.centerLongitude, tmElectronicFence.centerLatitude));
        }
        $('#fenceName').focus();
    }

    function createMap() {
        window.map = new BMapGL.Map("b-map");
        // 设置地图中心点以及最大缩放级别
        map.centerAndZoom(new BMapGL.Point(116.404, 39.915), 16);
        // 开启鼠标滚轮缩放
        map.enableScrollWheelZoom(true);
    }

    function createDrawingManager() {
        // 实例化鼠标绘制工具
        window.drawingManager = new BMapGLLib.DrawingManager(map, {
            // isOpen: true,        // 是否开启绘制模式
            enableCalculate: false, // 绘制是否进行测距测面
            enableSorption: true,   // 是否开启边界吸附功能
            sorptiondistance: 20,   // 边界吸附距离
            markerOptions: getStyleOptions(),     // 点的样式
            circleOptions: getStyleOptions(),     // 圆的样式
            polygonOptions: getStyleOptions(),    // 多边形的样式
            rectangleOptions: getStyleOptions(),  // 矩形的样式
        });
    }

    function createSearchPlace() {
        // 检索：建立一个自动完成的对象
        new BMapGL.Autocomplete({"input": "address-search-input", "location": map})
            .addEventListener("onconfirm", function (e) {
                var _value = e.item.value;
                searchPlace(_value.province + _value.city + _value.district + _value.street + _value.streetNumber + _value.business);
            });
    }

    function addControls() {
        // 添加默认缩放平移控件
        map.addControl(new BMapGL.NavigationControl({
            anchor: BMAP_ANCHOR_TOP_LEFT,
            type: BMAP_NAVIGATION_CONTROL_LARGE,
            offset: new BMapGL.Size(10, 5)
        }));
        /*// 添加城市选择控件
        map.addControl(new BMapGL.CityListControl({
            anchor: BMAP_ANCHOR_TOP_LEFT,
            offset: new BMapGL.Size(10, 5)
        }));*/
    }

    function bindEvent() {
        // 检索：回车自动检索
        $('#address-search-input').on("keydown", function (e) {
            if (e.keyCode === 13) {
                searchPlace(e.currentTarget.value);
            }
        });
        // 绘制：绘制完成后
        drawingManager.addEventListener("overlaycomplete", function (e) {
            var drawingMode = e.drawingMode;
            var centerPoint = e.overlay.latLng;
            var points = [];
            var fenceType = drawingMode;
            if (drawingMode === BMAP_DRAWING_MARKER) {
                fenceType = BMAP_DRAWING_CIRCLE;
            }
            if (drawingMode === BMAP_DRAWING_POLYGON) {
                points = e.overlay.getPath();
                centerPoint = getCenterPoint(points);
            }
            // 获取中心点地址信息
            getLocation(centerPoint, function (result) {
                var fenceProvince = "";
                var fenceCity = "";
                var fenceDistrict = "";
                var fenceAddress = "";
                if (result) {
                    fenceProvince = result.addressComponents.province;
                    fenceCity = result.addressComponents.city;
                    fenceDistrict = result.addressComponents.district;
                    fenceAddress = result.addressComponents.streetNumber || result.surroundingPois.length <= 0 ? result.address : result.surroundingPois[0].address + result.surroundingPois[0].title;
                }
                var tmElectronicFence = {
                    fenceType: fenceType,
                    radius: radius,
                    centerLongitude: centerPoint.lng,
                    centerLatitude: centerPoint.lat,
                    fenceProvince: fenceProvince,
                    fenceCity: fenceCity,
                    fenceDistrict: fenceDistrict,
                    fenceAddress: fenceAddress,
                    points: points,
                    orgId: tmOrg.id,
                    orgName: tmOrg.name,
                    recVer: 0
                }
                initForm(tmElectronicFence);
            });
            drawingManager.close();
        });
    }

    $(document).ready(function () {
        createMap();
        createDrawingManager();
        createSearchPlace();
        bindEvent();

        var tmElectronicFence = getInitValue();
        if (tmElectronicFence && tmElectronicFence.hasOwnProperty("id") && tmElectronicFence.id) {
            initForm(tmElectronicFence);
        }
    });
</script>
</html>