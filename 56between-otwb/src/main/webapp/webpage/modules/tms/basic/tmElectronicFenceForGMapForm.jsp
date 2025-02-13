<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <title>维护标注</title>
    <script src="${ctxStatic}/plugin/jquery/3.5.1/jquery-3.5.1.min.js"></script>
    <%--高德地图API--%>
    <script type="text/javascript" src="https://webapi.amap.com/maps?v=2.0&key=${ak}"></script>
    <%--信息窗口--%>
    <link href="${ctxStatic}/common/css/app-midnight-blue.css" rel="stylesheet">
    <link href="${ctxStatic}/common/css/bqStyle.css" rel="stylesheet">
    <script src="${ctxStatic}/common/js/56between.js"></script>
    <script type="text/javascript">var ctx = '${ctx}'</script>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <link href="${ctxStatic}/tms/basic/tmElectronicFenceForm.css" rel="stylesheet">
</head>
<body>
<div id="g-map" style="width: 100%; height: 100%;"></div>
<div class="address-search">
    <input type="text" id="address-search-input" class="form-control" placeholder="搜索地址..." autocomplete="off"/>
    <div id="address-search-result"></div>
</div>
<div class="map-marker-tool">
    <div class="btn-marker-point" id="btn-marker-point" data-tip="请在地图上选择一个中心点" title="标注点" style="display: block;" onclick="drawing(DRAWING_CIRCLE)">
        <i class="icon-marker-point"></i>
    </div>
    <div class="btn-marker-polygon" id="btn-marker-polygon" data-tip="请在地图上通过鼠标点击画定一个区域" title="标注区域" style="display: block;" onclick="drawing(DRAWING_POLYGON)">
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
    var DRAWING_MARKER = "marker", DRAWING_CIRCLE = "circle", DRAWING_RECTANGLE = "rectangle", DRAWING_POLYGON = "polygon";

    /**
     * 开启绘制工具
     * drawingMode 绘制模式
     * DRAWING_MARKER 点
     * DRAWING_CIRCLE 圆
     * DRAWING_POLYGON 多边形
     * DRAWING_RECTANGLE 矩形
     */
    function drawing(drawingMode) {
        // 清除地图上所有覆盖物
        drawingManager.close();
        map.clearMap();
        switch (drawingMode) {
            case DRAWING_CIRCLE:
                drawingManager.circle(getStyleOptions());
                break;
            case DRAWING_POLYGON:
                drawingManager.polygon(getStyleOptions());
                break;
        }
    }

    /**
     * 添加覆盖物 - 点
     * @param title     文字提示
     * @param location  坐标点
     * @param draggable 是否可拖拽
     */
    function addMarker(title, location, draggable) {
        map.add(new AMap.Marker({title, position: location, draggable: draggable || false}));
        map.setFitView();
    }

    /**
     * 添加覆盖物 - 圆
     * @param point 中心点
     * @param radius 半径(米)
     */
    function addCircle(point, radius) {
        addMarker('', point);
        map.add(new AMap.Circle({center: point, radius: radius}));
        map.setFitView();
    }

    /**
     * 添加覆盖物 - 多边形
     * @param points 点
     * @param point 中心点
     */
    function addPolygon(points, point) {
        addMarker('', point);
        map.add(new AMap.Polygon({path: points}));
        map.setFitView();
    }

    /**
     * 获取指定坐标点位置信息
     * @param point 坐标点
     * @param func 回调函数
     */
    function getLocation(point, func) {
        AMap.plugin(['AMap.Geocoder'], function () {
            new AMap.Geocoder({city: '全国', radius: 500, batch: false}).getAddress(point, func);
        });
    }

    /**
     * 检索地点
     * @param place 检索地点
     */
    function searchPlace(place) {
        // 清除地图上所有覆盖物
        map.clearMap();
        AMap.plugin(['AMap.PlaceSearch'], function () {
            new AMap.PlaceSearch({city: '全国', pageSize: 1}).search(place, function (status, result) {
                if (result.poiList.count <= 0) {
                    return;
                }
                var poi = result.poiList.pois[0];
                addMarker(poi.name, new AMap.LngLat(poi.location.lng, poi.location.lat));
            });
        });
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
        var circle = map.getAllOverlays('circle')[0];
        // 清除地图上所有覆盖物
        map.clearMap();
        addMarker("", circle.getCenter());
        addCircle(circle.getCenter(), radius);
    }

    /**
     * 取消
     */
    function cancel() {
        $('#map-marker-save').css("display", "none");
        $('#tmElectronicFenceForm>table').remove();
        // 清除地图上所有覆盖物
        map.clearMap();
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
                tmElectronicFence.points.push(new AMap.LngLat(pointList[i].longitude, pointList[i].latitude));
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
        map.clearMap();
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

        if (tmElectronicFence.fenceType === DRAWING_MARKER || tmElectronicFence.fenceType === DRAWING_CIRCLE) {
            $('.radius').show();
            $('#radius').val(tmElectronicFence.radius);
            // 添加覆盖物 - 圆
            addCircle(new AMap.LngLat(tmElectronicFence.centerLongitude, tmElectronicFence.centerLatitude), tmElectronicFence.radius);
        } else if (tmElectronicFence.fenceType === DRAWING_POLYGON) {
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
            addPolygon(points, new AMap.LngLat(tmElectronicFence.centerLongitude, tmElectronicFence.centerLatitude));
        }
        $('#fenceName').focus();
    }

    function createMap() {
        window.map = new AMap.Map("g-map", {center: [116.404, 39.915], zoom: 11});
    }

    function createDrawingManager() {
        AMap.plugin(['AMap.MouseTool'], function () {
            window.drawingManager = new AMap.MouseTool(map);
        });
    }

    function createSearchPlace() {
        AMap.plugin(['AMap.AutoComplete'], function () {
            new AMap.AutoComplete({input: "address-search-input"})
                .on("select", function (e) {
                    //注册监听，当选中某条记录时会触发
                    map.clearMap();
                    searchPlace(e.poi.name);
                });
        });
    }

    function bindEvent() {
        // 检索：回车自动检索
        $('#address-search-input').on("keydown", function (e) {
            if (e.keyCode !== 13) {
                return;
            }
            searchPlace(e.currentTarget.value);
        });
        // 绘制：绘制完成后
        drawingManager.on('draw', function (e) {
            var fenceType = '', centerPoint = {}, points = [];
            switch (e.obj.className) {
                case "Overlay.Circle":
                    fenceType = DRAWING_CIRCLE;
                    centerPoint = e.obj.getCenter();
                    break;
                case "Overlay.Polygon":
                    fenceType = DRAWING_POLYGON;
                    centerPoint = e.obj.getBounds().getCenter();
                    points = e.obj.getPath();
                    break;
            }
            // 获取中心点地址信息
            getLocation(centerPoint, function (status, result) {
                if (status === 'complete' && result.regeocode) {
                    var fenceProvince = result.regeocode.addressComponent.province;
                    var fenceCity = result.regeocode.addressComponent.city || result.regeocode.addressComponent.province;
                    var fenceDistrict = result.regeocode.addressComponent.district;
                    var fenceAddress = result.regeocode.formattedAddress;
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
                }
            });
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