<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initSoTable();
    initInvTable();
    laydate.render({elem: '#lotAtt01', theme: '#393D49'});
    laydate.render({elem: '#lotAtt02', theme: '#393D49'});
    laydate.render({elem: '#lotAtt03', theme: '#393D49'});
    laydate.render({elem: '#outboundTime', theme: '#393D49', type: 'datetime'});
    laydate.render({elem: '#soOrderTime', theme: '#393D49', type: 'datetime'});
});

function searchInv() {
    $('#searchModal').modal();
}

function initSoTable() {
    $('#soTable').bootstrapTable('destroy').bootstrapTable({
        method: 'post',//请求方法
        height: $(window).height() - 110,
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        onResetView: function () {
            var totalQty = 0, totalWeight = 0;
            $.map($('#soTable').bootstrapTable('getData'), function (row) {
                var qty = 0, weight = 0;
                if (row.hasOwnProperty("qty") && row.qty !== undefined){
                    qty = Number(row.qty);
                }
                if (row.hasOwnProperty("grossWeight") && row.grossWeight !== undefined) {
                    weight = Number(row.grossWeight);
                }
                totalQty += qty;
                totalWeight += qty * weight;
            })
            $('#total').text("总数量：" + totalQty + " 总重量：" + totalWeight);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'qty',
            title: '数量',
            sortable: true,
            cellStyle: function (value, row, index) {
                return {css: {"font-weight": "Bold"}}
            },
            formatter: function (value, row, index) {
                return "<input style='border: none; width: 100px;' maxlength='11' onkeyup='bq.numberValidator(this, 0, 0);' " + "onblur='validateQty(this," + row.qtyAvailable + "," + index + ");" + "' value='" + value + "'</input>";
            }
        }, {
            field: 'qtyAvailable',
            visible: false
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
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
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'locCode',
            title: '库位',
            sortable: true
        }, {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        }, {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        },{
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        }, {
            field: 'lotAtt04',
            title: '品质',
            sortable: true
        }, {
            field: 'lotAtt05',
            title: '批次属性05',
            sortable: true
        }, {
            field: 'lotAtt06',
            title: '批次属性06',
            sortable: true
        }, {
            field: 'lotAtt07',
            title: '批次属性07',
            sortable: true
        }, {
            field: 'lotAtt08',
            title: '批次属性08',
            sortable: true
        }, {
            field: 'lotAtt09',
            title: '批次属性09',
            sortable: true
        }, {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: true
        }, {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: true
        }, {
            field: 'lotAtt12',
            title: '批次属性12',
            sortable: true
        }]
    });
}

function initInvTable() {
    $('#invTable').bootstrapTable('destroy').bootstrapTable({
        height: $(window).height() - 90,
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.orgId = jp.getCurrentOrg().orgId;
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function(row, $el) {
            jp.changeTableStyle($el);
        },
        columns: [{
            checkbox: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true
        }, {
            field: 'ownerName',
            title: '货主名称',
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
            field: 'lotNum',
            title: '批次号',
            sortable: true
        }, {
            field: 'locCode',
            title: '库位',
            sortable: true
        }, {
            field: 'traceId',
            title: '跟踪号',
            sortable: true
        }, {
            field: 'qty',
            title: '库存数',
            sortable: true
        }, {
            field: 'qtyAvailable',
            title: '库存可用数',
            sortable: true
        }, {
            field: 'qtyHold',
            title: '冻结数',
            sortable: true
        }, {
            field: 'qtyAlloc',
            title: '分配数',
            sortable: true
        }, {
            field: 'qtyPk',
            title: '拣货数',
            sortable: true
        }, {
            field: 'qtyPaOut',
            title: '上架待出数',
            sortable: true
        }, {
            field: 'qtyMvOut',
            title: '移动待出数',
            sortable: true
        }, {
            field: 'qtyRpOut',
            title: '补货待出数',
            sortable: true
        }, {
            field: 'lotAtt01',
            title: '生产日期',
            sortable: true
        }, {
            field: 'lotAtt02',
            title: '失效日期',
            sortable: true
        }, {
            field: 'lotAtt03',
            title: '入库日期',
            sortable: true
        }, {
            field: 'lotAtt04',
            title: '品质',
            sortable: true
        }, {
            field: 'lotAtt05',
            title: '批次属性05',
            sortable: true
        }, {
            field: 'lotAtt06',
            title: '批次属性06',
            sortable: true
        }, {
            field: 'lotAtt07',
            title: '批次属性07',
            sortable: true
        }, {
            field: 'lotAtt08',
            title: '批次属性08',
            sortable: true
        }, {
            field: 'lotAtt09',
            title: '批次属性09',
            sortable: true
        }, {
            field: 'lotAtt10',
            title: '批次属性10',
            sortable: true
        }, {
            field: 'lotAtt11',
            title: '批次属性11',
            sortable: true
        }, {
            field: 'lotAtt12',
            title: '批次属性12',
            sortable: true
        }]
    });
}

function validateQty(obj, qty, index) {
    var curQty = $(obj).val();
    if (!curQty) {
        jp.warning("不能为空");
        $(obj).val(qty);
        return;
    }
    if (curQty === '0') {
        jp.warning("不能为0");
        $(obj).val(qty);
        return;
    }
    if (parseFloat(curQty) > parseFloat(qty)) {
        jp.bqWaring("不能大于库存可用数量");
        $(obj).val(qty);
        return;
    }
    saveData(index, curQty);
}

function saveData(index, value) {
    $("#soTable").bootstrapTable('updateCell', {
        index: index,
        field: 'qty',
        value: value
    });
}

function getSelections(obj) {
    return $.map($(obj).bootstrapTable('getSelections'), function (row) {
        return row
    });
}

function getSelectionIds(obj) {
    return $.map($(obj).bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function getData(obj) {
    return $.map($(obj).bootstrapTable('getData'), function (row) {
        return row
    });
}

function moveToLeft() {
    var selectRows = getSelections('#invTable');
    var hasRows = getData('#soTable');
    if (selectRows.length === 0) {
        jp.bqWaring("请选择库存数据！");
        return;
    }
    // 校验选中数据是否存在不同的货主
    if (isExistDiffOwner(selectRows)) {
        jp.bqWaring("选择的记录中存在不同的货主！")
        return;
    }

    if (hasRows.length === 0) {
        var newArr = selectRows.map(function (item) {
            item.qty = item.qtyAvailable;
            return item;
        });

        $('#soTable').bootstrapTable('append', newArr);
        return;
    }
    if (hasRows[0].ownerCode !== selectRows[0].ownerCode) {
        jp.bqWaring("选中的库存货主[" + selectRows[i].ownerCode + "]和已选择数据货主[" + hasRows[0].ownerCode + "]不一致！");
        return;
    }
    for (var i in selectRows) {
        for (var j in hasRows) {
            if (hasRows[j].id === selectRows[i].id) {
                $('#soTable').bootstrapTable('remove', {field: 'id', values: [hasRows[j].id]});
            }
        }
        selectRows[i].qty = selectRows[i].qtyAvailable;
        $('#soTable').bootstrapTable('load', $("#soTable").bootstrapTable('getData'));
        $('#soTable').bootstrapTable('append', selectRows[i]);
    }
    queryInvData();
}

function moveToRight() {
    var ids = getSelectionIds('#soTable');
    if (ids.length > 0) {
        $('#soTable').bootstrapTable('remove', {field: 'id', values: ids}).bootstrapTable('load', $("#soTable").bootstrapTable('getData'));
    }
    queryInvData();
}

function isExistDiffOwner(selectRows) {
    var flag = false;
    var firstOwner = selectRows[0].ownerCode;
    for (var index = 1; index < selectRows.length; index++) {
        if (selectRows[index].ownerCode !== firstOwner) {
            flag = true;
            break;
        }
    }
    return flag;
}

function createSo() {
    $('#createSo').focus();
    var rows = getData('#soTable');
    if (rows.length === 0) {
        jp.bqWaring("已选择数据列表为空，不能操作！")
        return;
    }
    $('#confirmModal').modal();
}

function queryConfirm() {
    queryInvData();
    $('#searchModal').modal('hide');
}

function queryInvData() {
    $('#invTable').bootstrapTable('refresh', {'url': "${ctx}/wms/outbound/banQinWmCreateSoByInv/data"});
}

function confirm() {
    var soOrderTime = $('#confirmModal #soOrderTime').val();
    if (!soOrderTime) {
        jp.bqWaring("订单时间不能为空");
        return;
    }
    var outboundTime = $('#confirmModal #outboundTime').val();
    var carrierCode = $('#confirmModal #carrierCode').val();
    var carrierName = $('#confirmModal #carrierName').val();
    var vehicleNo = $('#confirmModal #vehicleNo').val();
    var customerNo = $('#confirmModal #customerNo').val();
    var driver = $('#confirmModal #driver').val();
    var driverTel = $('#confirmModal #driverTel').val();
    var consigneeAddr = $('#confirmModal #consigneeAddr').val();
    var remarks = $('#confirmModal #remarks').val();

    var rows = $.map(getData('#soTable'), function (row) {
        row.soOrderTime = soOrderTime;
        row.outboundTime = outboundTime;
        row.carrierCode = carrierCode;
        row.carrierName = carrierName;
        row.customerNo = customerNo;
        row.vehicleNo = vehicleNo;
        row.driver = driver;
        row.driverTel = driverTel;
        row.consigneeAddr = consigneeAddr;
        row.remarks = remarks;
        return row
    });

    jp.loading();
    $.ajax({
        url: "${ctx}/wms/outbound/banQinWmCreateSoByInv/createSo",
        type: 'POST',
        data: JSON.stringify(rows),
        contentType: "application/json",
        success: function (data) {
            $('#confirmModal').modal('hide');
            if (data.success) {
                initSoTable();
                queryInvData();
                jp.bqWaring(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        }
    });
}

</script>