<%@ page contentType="text/html;charset=UTF-8"%>
<script>
var $topIndex;//弹出窗口的 index
$(document).ready(function () {
    $("#dispatchTime").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    initV();
    initOrderLabel();
});

/*初始化值*/
function initV() {
    $('#dispatchOutletCode').val('DDS');
    $('#dispatchTime input').val(jp.dateFormat(new Date(), "yyyy-MM-dd hh:mm:ss"));
    $('#dispatchType').val('5');
    $('#baseOrgId').val(tmOrg.id);
    $('#orgId').val(jp.getCurrentOrg().orgId);
}

/*调度派车确认*/
function doSubmit(index) {
    $topIndex = index;
    var validator = bq.headerSubmitCheck("#inputForm");
    if (!validator.isSuccess) {
        jp.bqWaring(validator.msg);
        return false;
    }
    var data = $('#inputForm').serializeJSON();
    data.labelIds = getIdSelections().join(',');
    // 判断是否勾选了已配载的标签
    let rows = getHasDispatchSelections();
    if (rows.includes("Y")) {
        jp.confirm('选择的订单中存在已经配载的运输订单，是否取消原有配载关联，按照此次配载完成？', function () {
            checkVehicle(data);
        });
    } else {
        checkVehicle(data);
    }
}

function checkVehicle(reqData) {
    // 判断调度车辆是否已存在未发车的派车数据
    jp.post('${ctx}/tms/order/tmDirectDispatch/checkVehicle', reqData, function (data) {
        if (data.success) {
            if (data.body.hasVehicle) {
                top.layer.confirm($('#carNo').val() + '车辆的派车信息已经存在 ，是否要追加到此派车单中？', {
                    icon: 3, title: '系统提示', btn: ['是', '否', '创建新派车单'],
                    btn3: function (index, layero) {
                        reqData.isNew = 'Y';
                        directDispatch(reqData);
                    },
                    success: function (layero) {
                        let btn = layero[0].getElementsByClassName('layui-layer-btn')[0].getElementsByTagName('A')[0];
                        btn.href = 'javascript:void(0)';
                        btn.focus();
                    }
                }, function (index) {
                    reqData.isNew = 'N';
                    directDispatch(reqData);
                    top.layer.close(index);
                }, function (index) {
                    top.layer.close(index);
                });
                return false;
            } else {
                reqData.isNew = 'Y';
                directDispatch(reqData);
            }
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*调度派车确认*/
function directDispatch(reqData) {
    jp.loading();
    jp.post('${ctx}/tms/order/tmDirectDispatch/directDispatch', reqData, function (data) {
        if (data.success) {
            jp.success(data.msg);
            jp.close($topIndex);
        } else {
            jp.bqError(data.msg);
        }
    });
}

/*初始化标签*/
function initOrderLabel() {
    var $orderLabelTable = $("#orderLabelTable");
    $orderLabelTable.bootstrapTable('destroy');
    $orderLabelTable.bootstrapTable({
        cache: false,//是否使用缓存，默认为true
        pagination: true,//是否显示分页
        sidePagination: "server",//分页方式：client客户端分页，server服务端分页
        queryParams: function (params) {//查询参数,每次调用是会带上这个参数，可自定义
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        formatNoMatches: function () {
            return "";
        },
        columns: [{
            checkbox: true
        }, {
            field: 'transportNo',
            title: '运输订单',
            sortable: true
        }, {
            field: 'hasDispatch',
            title: '是否配载',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'labelNo',
            title: '标签',
            sortable: true
        }, {
            field: 'shipName',
            title: '发货方',
            sortable: true
        }, {
            field: 'shipCity',
            title: '发货城市',
            sortable: true
        }, {
            field: 'shipper',
            title: '发货人',
            sortable: true
        }, {
            field: 'shipAddress',
            title: '发货地址',
            sortable: true
        }, {
            field: 'consigneeName',
            title: '收货方',
            sortable: true
        }, {
            field: 'consigneeCity',
            title: '收货城市',
            sortable: true
        }, {
            field: 'consignee',
            title: '收货人',
            sortable: true
        }, {
            field: 'consigneeAddress',
            title: '收货地址',
            sortable: true
        }, {
            field: 'receiveOutletName',
            title: '提货网点',
            sortable: true
        }, {
            field: 'outletName',
            title: '配送网点',
            sortable: true
        }, {
            field: 'skuCode',
            title: '商品编码',
            sortable: true
        }, {
            field: 'skuName',
            title: '商品名称',
            sortable: true
        }, {
            field: 'qty',
            title: '数量',
            sortable: true
        }, {
            field: 'weight',
            title: '重量',
            sortable: true
        }, {
            field: 'cubic',
            title: '体积',
            sortable: true
        }]
    });
    $orderLabelTable.on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $orderLabelTable.bootstrapTable('getSelections').length;

    });

    // 查询
    $("#search").click("click", function () {
        refreshTable();
    });
    // 重置
    $("#reset").click("click", function () {
        $("#searchForm input:not(#transportIdList)").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
        refreshTable();
    });
}

/*查看标签*/
function refreshTable() {
    $("#orderLabelTable").bootstrapTable('refresh', {'url': '${ctx}/tms/order/tmDirectDispatch/page'});
}

/*获取表格选中行id*/
function getIdSelections() {
    return $.map($("#orderLabelTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getHasDispatchSelections() {
    return $.map($("#orderLabelTable").bootstrapTable('getSelections'), function (row) {
        return row.hasDispatch
    });
}

/*承运商选择后*/
function carrierSelect(data) {
    $("#carNo").val("");
    $("#driver").val("");
    $("#driverName").val("");
    $("#driverTel").val("");
}
</script>