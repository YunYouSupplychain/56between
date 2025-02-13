<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();
});

function initTable() {
    var $bmsContractStoragePriceTable = $('#bmsContractStoragePriceTable');
    $bmsContractStoragePriceTable.bootstrapTable({
        cache: false,// 是否使用缓存
        pagination: true,// 是否显示分页
        sidePagination: "server",// client客户端分页，server服务端分页
        url: "${ctx}/bms/basic/bmsContract/storagePrice/data",
        queryParams: function (params) {
            var searchParam = {};
            searchParam.fkId = "${bmsContractCostItemEntity.id}";
            searchParam.orgId = "${bmsContractCostItemEntity.orgId}";
            searchParam.searchSku = $('#searchSku').val();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        onClickRow: function (row, $el) {
            $(".info").removeClass("info");
            $el.addClass("info");
            if (row.hasOwnProperty("isUseStep") && row.isUseStep === "Y") {
                refreshStepPriceTable(row.id, row.orgId);
            } else {
                refreshStepPriceTable();
            }
            $bmsContractStoragePriceTable.bootstrapTable('uncheckAll');
            $bmsContractStoragePriceTable.bootstrapTable("checkBy", {field: 'id', values: [row.id]});
        },
        onLoadSuccess: function (data) {
            if (data.total > 0) {
                $(".info").removeClass("info");
                $bmsContractStoragePriceTable.find("tbody>tr").eq(0).addClass("info");
                var row = data.rows[0];
                if (row.hasOwnProperty("isUseStep") && row.isUseStep === "Y") {
                    refreshStepPriceTable(row.id, row.orgId);
                } else {
                    refreshStepPriceTable();
                }
                $bmsContractStoragePriceTable.bootstrapTable("checkBy", {field: 'id', values: [row.id]});
            }
        },
        formatNoMatches: function () {
            return '-';
        },
        columns: [{
            checkbox: true
        }, {
            field: 'skuClass',
            title: '品类编码'
        }, {
            field: 'skuClassName',
            title: '品类名称'
        }, {
            field: 'skuCode',
            title: '商品编码'
        }, {
            field: 'skuName',
            title: '商品名称'
        }, {
            field: 'price',
            title: '价格'
        }, {
            field: 'logisticsPoints',
            title: '物流点数'
        }, {
            field: 'isAccumulationMethod',
            title: '是否阶梯分段累加方式计算',
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }]
    }).on('check.bs.table uncheck.bs.table load-success.bs.table check-all.bs.table uncheck-all.bs.table', function () {
        var length = $bmsContractStoragePriceTable.bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });

    $('#bmsContractStorageSteppedPriceTable').bootstrapTable({
        cache: false,// 是否使用缓存
        sidePagination: "server",// client客户端分页，server服务端分页
        queryParams: function (params) {
            var searchParam = $("#searchForm").serializeJSON();
            searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
            searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
            searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
            return searchParam;
        },
        formatNoMatches: function () {
            return '-';
        },
        columns: [{
            field: 'fm',
            title: '从(含当前值)'
        }, {
            field: 'to',
            title: '到(不含当前值)'
        }, {
            field: 'price',
            title: '单价'
        }]
    });
}

function refreshStepPriceTable(fkId, orgId) {
    if (fkId === undefined) {
        $('#bmsContractStorageSteppedPriceTable').bootstrapTable('removeAll');
    } else {
        $('#bmsContractStorageSteppedPriceTable').bootstrapTable('refresh', {url: '${ctx}/bms/basic/bmsContract/storagePrice/stepPrice/data?fkId=' + fkId + '&orgId=' + orgId});
    }
}


function getDetailIdSelections() {
    return $.map($("#bmsContractStoragePriceTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function add() {
    jp.openDialog("新增仓储价格", "${ctx}/bms/basic/bmsContract/storagePrice/form?fkId=${bmsContractCostItemEntity.id}&orgId=${bmsContractCostItemEntity.orgId}", "70%", "70%", $("#bmsContractStoragePriceTable"));
}

function edit(id) {
    if (id === undefined) {
        id = getDetailIdSelections();
    }
    jp.openDialog("编辑仓储价格", "${ctx}/bms/basic/bmsContract/storagePrice/form?id=" + id, "70%", "70%", $("#bmsContractStoragePriceTable"));
}

function deleteAll(ids) {
    jp.confirm('确认要删除吗？', function () {
        jp.loading();
        if (ids === undefined) {
            ids = getDetailIdSelections();
        }
        jp.post("${ctx}/bms/basic/bmsContract/deleteStoragePrice?ids=" + ids, {}, function (data) {
            if (data.success) {
                $("#bmsContractStoragePriceTable").bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        });
    });
}

function skuSearch(e) {
    if (e.keyCode === 13 || e.which === 13) {
        $('#bmsContractStoragePriceTable').bootstrapTable('refresh');
    }
}
</script>