<%@ page contentType="text/html;charset=UTF-8" %>
<script>
var configData;
$(document).ready(function () {
    var idx = jp.loading("正在加载调度信息...");
    var params = "planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $("#baseOrgId").val();
    jp.get("${ctx}/tms/order/tmDispatchPlan/getDispatchInfo?" + params, function (data) {
        if (data.success) {
            configData = data.body.data;
            appendTrip(data.body.data);
        }
        jp.close(idx);
    });

    $('body').scroll(function () {
        var $toolbar = $('#toolbar');
        if ($('body').scrollTop() > 0) {
            $toolbar.addClass("scrollNav")//添加样式
        } else {
            $toolbar.removeClass("scrollNav")
        }
    });
});

function appendTrip(data) {
    var $tbody = $('#tbody');
    var content = '';
    for (var i = 0; i < data.length; i += 4) {
        content += createTr(data.slice(i, i + 4), i);
    }
    $tbody.append(content);
    // 给checkbox绑定点击事件
    $('input[name="detailSelect"]').on('click', function () {
        $('#recall').attr("disabled", $('input[name="detailSelect"]:checked').length !== 1);
    });
    $('.remarks').hover(function () {
        var e = $(this).offset();
        var left = e.left;
        var top = e.top + 25;
        $(this).next().css({left: left + "px", top: top + "px"}).show();
    }, function () {
        $(this).next().fadeOut();
    });
}

function createTr(data, index) {
    var content = '<tr>';
    if ((index + 1) % 16 === 1) content += createVehicleNo(data[0].vehicleNo);
    content += createCheckbox(data[0].vehicleNo, data[0].trip);
    content += createTrip(data[0].trip);
    content += createButton(data);
    for (var i = 0; i < data.length; i++) {
        var hasAudit = isAudit(data[i]);
        content += createBgTd(toNvlString(data[i].pickUpPointName), hasAudit);
        content += createBgTd(toNvlString(data[i].deliveryPointName), hasAudit);
        content += createBgTd(toNvlString(data[i].skuName), hasAudit);
        content += createBgTd(toNvlString(data[i].qty), hasAudit);
    }
    content += createTd(createDriver(data).name);
    content += createTd(creatEscort(data).name);
    content += creatRemarks(data[0].hasOwnProperty("remarks") ? data[0].remarks : "");
    content += '</tr>';
    return content;
}

function createVehicleNo(data) {
    return  '<td rowspan="4" style="font-size: 18px; font-weight: bold;">' + data + '</td>'
}

function createCheckbox(vehicleNo, trip) {
    return '<td><input type="checkbox" class="myCheckbox" name="detailSelect" value="' + vehicleNo + '@' + trip + '"/></td>';
}

function createTrip(trip) {
    var result = "";
    switch (trip) {
        case "1":
            result = "第一车";
            break;
        case "2":
            result = "第二车";
            break;
        case "3":
            result = "第三车";
            break;
        case "4":
            result = "第四车";
            break;
    }
    return "<td>" + result + "</td>>";
}

function isAudit(data) {
    return toNvlString(data.transportNo) || toNvlString(data.dispatchNo);
}

function createBgTd(data, hasAudit) {
    var td = '<td>';
    if (data && hasAudit) {
        td = "<td style='background: #caefca;'>";
    }
    return td + data + "</td>";
}

function createTd(data) {
    return '<td>' + data + "</td>";
}

function createButton(data) {
    var hasAudit = false;
    for (var i = 0; i < data.length; i++) {
        if (isAudit(data[i])) {
            hasAudit = true;
            break;
        }
    }
    var td = '<td><a ';
    if (hasAudit) td += 'style="pointer-events: none; color: #8A8A8A;';

    var vehicleType = data[0].hasOwnProperty("vehicleType") ? data[0].vehicleType : "";
    var remarks = data[0].hasOwnProperty("remarks") ? data[0].remarks : "";
    return td + ' onclick="editRows(\'' + data[0].vehicleNo + '\', \'' + data[0].trip + '\', \'' + createDriver(data).code + '\', \'' + createDriver(data).name + '\', \'' + creatEscort(data).code + '\', \'' + creatEscort(data).name + '\', \'' + vehicleType + '\', \'' + remarks + '\')">修改</></td>';
}

function createDriver(data) {
    var driver = {code: '', name: ''};
    for (var j = 0; j < data.length; j++) {
        if (data[j].driver) {
            driver.code = data[j].driver;
            driver.name = data[j].driverName;
            break;
        }
    }
    return driver;
}

function creatEscort(data) {
    var escort = {code: '', name: ''};
    for (var j = 0; j < data.length; j++) {
        if (data[j].escort) {
            escort.code = data[j].escort;
            escort.name = data[j].escortName;
            break;
        }
    }
    return escort;
}

function creatRemarks(data) {
    return '<td><p class="remarks">' + data + '</p><p>' + data + '</p></td>';
}

function toNvlString(obj) {
    return !obj ? "" : obj;
}


function editRows(vehicleNo, trip, driver, driverName, escort, escortName, vehicleType, remarks) {
    var viewParams = "planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $('#baseOrgId').val();
    var editParams = "&vehicleNo=" + encodeURIComponent(vehicleNo) + "&trip=" + trip + "&driver=" + encodeURIComponent(driver) + "&driverName=" + encodeURIComponent(driverName) + "&escort=" + encodeURIComponent(escort) + "&escortName=" + encodeURIComponent(escortName) + "&vehicleType=" + encodeURIComponent(vehicleType) + "&remarks=" + encodeURIComponent(remarks);
    top.layer.open({
        type: 2,
        area: ["60%", "55%"],
        title: "修改",
        maxmin: false,
        content: "${ctx}/tms/order/tmDispatchPlan/editForm?" + viewParams + editParams,
        btn: ['保存', '取消'],
        yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0];
            iframeWin.contentWindow.save(index);
        },
        cancel: function (index) {
        },
        end: function () {
            window.location = "${ctx}/tms/order/tmDispatchPlan/result?" + viewParams;
        }
    });
}

function dispatchAll() {
    var params = "planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $('#baseOrgId').val();
    top.layer.open({
        type: 2,
        area: ["85%", "70%"],
        title: "需求批量调度",
        maxmin: false,
        content: "${ctx}/tms/order/tmDispatchPlan/dispatchAllDialog?" + params,
        btn: ['确认', '关闭'],
        yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0];
            iframeWin.contentWindow.save(index);
        },
        cancel: function (index) {
        },
        end: function () {
            window.location = "${ctx}/tms/order/tmDispatchPlan/result?" + params;
        }
    });
}

function editAll() {
    var params = "planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $('#baseOrgId').val();
    top.layer.open({
        type: 2,
        area: ["90%", "80%"],
        title: "批量修改",
        maxmin: false,
        content: "${ctx}/tms/order/tmDispatchPlan/editAll?" + params,
        btn: ['确认', '关闭'],
        yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0];
            iframeWin.contentWindow.save(index);
        },
        cancel: function (index) {
        },
        end: function () {
            window.location = "${ctx}/tms/order/tmDispatchPlan/result?" + params;
        }
    });
}

function detailSelectChange(flag) {
    $("input[name='detailSelect']:checkbox").prop('checked', flag);
    if (flag && $("input[name='detailSelect']:checked").length === 0) {
        $('#selectAll').prop('checked', false);
    }
}

function audit() {
    var rows = [];
    $("input[name='detailSelect']:checked").each(function() {
        rows.push($(this).val());
    });
    if (rows.length === 0) {
        jp.bqError("请选择一条记录");
        return;
    }
    var params = "planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $('#baseOrgId').val();
    var vehicleParams = "&vehicleAndTrips=" + rows.join(',');
    jp.loading();
    jp.post("${ctx}/tms/order/tmDispatchPlan/audit?" + params + vehicleParams, null, function (data) {
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/tmDispatchPlan/result?" + params;
        } else {
            jp.bqError(data.msg);
        }
    })
}

function cancelAudit() {
    var rows = [];
    $("input[name='detailSelect']:checked").each(function() {
        rows.push($(this).val());
    });
    if (rows.length === 0) {
        jp.bqError("请选择一条记录");
        return;
    }
    var params = "planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $('#baseOrgId').val();
    var vehicleParams = "&vehicleAndTrips=" + rows.join(',');
    jp.loading();
    jp.post("${ctx}/tms/order/tmDispatchPlan/cancelAudit?" + params + vehicleParams, null, function (data) {
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/tmDispatchPlan/result?" + params;
        } else {
            jp.bqError(data.msg);
        }
    })
}

function queryPlanResult() {
    jp.openTab("${ctx}/tms/order/tmDispatchPlan/resultLis?planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $('#baseOrgId').val(), "需求调度结果查询", false);
}

function queryPlanDetail() {
    jp.openTab("${ctx}/tms/order/tmDispatchPlan/detailLis?planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $('#baseOrgId').val(), "需求计划明细查询", false);
}

function recall() {
    jp.loading();
    var params = "planNo=" + $('#planNo').val() + "&orgId=" + $('#orgId').val() + "&baseOrgId=" + $('#baseOrgId').val();
    jp.post("${ctx}/tms/order/tmDispatchPlan/recall?" + params + "&vehicleAndTrips=" + $("input[name='detailSelect']:checked").eq(0).val(), null, function (data) {
        if (data.success) {
            jp.success(data.msg);
            window.location = "${ctx}/tms/order/tmDispatchPlan/result?" + params;
        } else {
            jp.bqError(data.msg);
        }
    });
}

</script>