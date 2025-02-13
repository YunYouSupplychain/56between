<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	$("#orgId").val(tmOrg.id);
	initTable();

	$("#search").click("click", function () {// 绑定查询按扭
		$('#tmVehicleTable').bootstrapTable('refresh');
	});
	$("#reset").click("click", function () {// 绑定查询按扭
		$("#searchForm input").val("");
		$("#searchForm select").val("");
		$("#searchForm .select-item").html("");
		$("#orgId").val(tmOrg.id);
		$("#transportObjType").val("CARRIER");
		$('#tmVehicleTable').bootstrapTable('refresh');
	});
});

function initTable() {
    var $table = $('#tmVehicleTable');
    $table.bootstrapTable({
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/tms/basic/tmVehicle/data",
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        columns: [{
            checkbox: true
        }, {
            field: 'carNo',
            title: '车牌号',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
        }, {
            field: 'transportEquipmentTypeName',
            title: '设备类型',
            sortable: true
        }, {
            field: 'carrierName',
            title: '承运商',
            sortable: true
        }, {
            field: 'vehicleTypeName',
            title: '车型',
            sortable: true
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_VEHICLE_STATUS'))}, value, "-");
            }
        }, {
            field: 'dispatchBaseName',
            title: '调度中心',
            sortable: true
        }, {
            field: 'mainDriverName',
            title: '主驾驶',
            sortable: true
        }, {
            field: 'copilotName',
            title: '副驾驶',
            sortable: true
        }, {
            field: 'trailer',
            title: '挂车',
            sortable: true
        }, {
            field: 'carType',
            title: '车辆类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_CAR_TYPE'))}, value, "-");
            }
        }, {
            field: 'carBrand',
            title: '车辆品牌',
            sortable: true
        }, {
            field: 'carModel',
            title: '车辆型号',
            sortable: true
        }, {
            field: 'carColor',
            title: '车辆颜色',
            sortable: true
        }, {
            field: 'carBodyNo',
            title: '车身号码',
            sortable: true
        }, {
            field: 'supplierCode',
            title: '供应商编码',
            sortable: true
        }, {
            field: 'supplierName',
            title: '供应商名称',
            sortable: true
        }, {
            field: 'ownership',
            title: '所有权',
            sortable: true
        }, {
            field: 'approvedLoadingWeight',
            title: '核定装载重量',
            sortable: true
        }, {
            field: 'approvedLoadingCubic',
            title: '核定装载体积',
            sortable: true
        }, {
            field: 'totalTractionWeight',
            title: '牵引总重量(吨)',
            sortable: true
        }, {
            field: 'equipmentQuality',
            title: '装备质量(吨)',
            sortable: true
        }, {
            field: 'doorNumber',
            title: '车门个数',
            sortable: true
        }, {
            field: 'length',
            title: '车长',
            sortable: true
        }, {
            field: 'width',
            title: '车宽',
            sortable: true
        }, {
            field: 'height',
            title: '车高',
            sortable: true
        }, {
            field: 'isTemperatureControl',
            title: '是否温控',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'temperatureType',
            title: '车辆温别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_TEMPERATURE_TYPE'))}, value, "-");
            }
        }, {
            field: 'minTemperature',
            title: '最低温',
            sortable: true
        }, {
            field: 'maxTemperature',
            title: '最高温',
            sortable: true
        }, {
            field: 'refrigerationEquipmentCode',
            title: '制冷设备编码',
            sortable: true
        }, {
            field: 'isRisk',
            title: '是否危品车',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'riskLevel',
            title: '危险等级',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('TMS_RISK_LEVEL'))}, value, "-");
            }
        }, {
            field: 'buyingTime',
            title: '购车时间',
            sortable: true
        }, {
            field: 'purchaseLocation',
            title: '购车地点',
            sortable: true
        }, {
            field: 'purchaseAmount',
            title: '购车金额',
            sortable: true
        }, {
            field: 'emissionStandard',
            title: '车辆排放标准',
            sortable: true
        }, {
            field: 'oilConsumption',
            title: '油耗(升)',
            sortable: true
        }, {
            field: 'mileage',
            title: '行车里程(公里)',
            sortable: true
        }, {
            field: 'horsepower',
            title: '马力(匹)',
            sortable: true
        }, {
            field: 'depreciableLife',
            title: '折旧年限(年)',
            sortable: true
        }, {
            field: 'scrappedLife',
            title: '报废年限(年)',
            sortable: true
        }, {
            field: 'activeTime',
            title: '启动时间',
            sortable: true
        }, {
            field: 'scrappedTime',
            title: '报废时间',
            sortable: true
        }, {
            field: 'salvageRate',
            title: '残值率(%)',
            sortable: true
        }, {
            field: 'axleNumber',
            title: '轴数',
            sortable: true
        }, {
            field: 'engineNo',
            title: '发动机号',
            sortable: true
        }, {
            field: 'oilType',
            title: '用油型号',
            sortable: true
        }, {
            field: 'registeredTime',
            title: '注册时间',
            sortable: true
        }, {
            field: 'registeredLocation',
            title: '注册地点',
            sortable: true
        }, {
            field: 'drivingLicenseNo',
            title: '行驶证号',
            sortable: true
        }, {
            field: 'drivingLicenseExpiryTime',
            title: '行驶证有效期',
            sortable: true
        }, {
            field: 'operatingLicenseExpiryTime',
            title: '运营证有效期',
            sortable: true
        }, {
            field: 'operatingDuration',
            title: '运营时长',
            sortable: true
        }, {
            field: 'tollCollectionTime',
            title: '通行缴费时间',
            sortable: true
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $table.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
        $('#enable').prop('disabled', !length);
        $('#unable').prop('disabled', !length);
        $('#updateStatus0').prop('disabled', !length);
        $('#updateStatus1').prop('disabled', !length);
    });
}

function getIdSelections() {
	return $.map($("#tmVehicleTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function deleteAll() {
	jp.confirm('确认要删除该车辆信息记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmVehicle/deleteAll?ids=" + getIdSelections(), function (data) {
			if (data.success) {
				$('#tmVehicleTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function add() {
	jp.openDialog('车辆信息', "${ctx}/tms/basic/tmVehicle/form", '90%', '90%', $('#tmVehicleTable'));
}

function edit(id){
  if (id === undefined) {
	  id = getIdSelections();
  }
  jp.openDialog('车辆信息', "${ctx}/tms/basic/tmVehicle/form?id=" + id,'90%', '90%', $('#tmVehicleTable'));
}

function enable(flag) {
	jp.loading();
	jp.post("${ctx}/tms/basic/tmVehicle/enable?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
		if (data.success) {
			$('#tmVehicleTable').bootstrapTable('refresh');
			jp.success(data.msg);
		} else {
			jp.error(data.msg);
		}
	});
}

function updateStatus(flag) {
    jp.loading();
    jp.post("${ctx}/tms/basic/tmVehicle/updateStatus?ids=" + getIdSelections() + "&flag=" + flag, {}, function (data) {
        if (data.success) {
            $('#tmVehicleTable').bootstrapTable('refresh');
            jp.success(data.msg);
        } else {
            jp.error(data.msg);
        }
    });
}


</script>