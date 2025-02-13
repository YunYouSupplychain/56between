<%@ page contentType = "text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
    initTable();

    $("#search").click("click", function () {// 绑定查询按扭
        $('#banQinCdWhSkuTable').bootstrapTable('refresh');
    });
    $("#reset").click("click", function () {// 绑定查询按扭
        $("#searchForm input").val("");
        $("#searchForm select").val("");
        $("#searchForm .select-item").html("");
    });
});

function initTable() {
    $('#banQinCdWhSkuTable').bootstrapTable({
        url: "${ctx}/wms/basicdata/banQinCdWhSku/data",
        showRefresh: true,// 显示刷新按钮
        showColumns: true,// 显示内容列下拉框
        showExport: true,// 显示到处按钮
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
        columns: [{
            checkbox: true
        }, {
            field: 'ownerCode',
            title: '货主编码',
            sortable: true,
            formatter: function (value, row, index) {
                return "<a href='javascript:edit(\"" + row.id + "\")'>" + value + "</a>";
            }
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
            field: 'foreignName',
            title: '商品外语名称',
            sortable: true
        }, {
            field: 'shortName',
            title: '商品简称',
            sortable: true
        }, {
            field: 'barCode',
            title: '商品条码',
            sortable: true
        }, {
            field: 'supBarCode',
            title: '供应商商品条码',
            sortable: true
        }, {
            field: 'cdpaFormat',
            title: '包装规格',
            sortable: true
        }, {
            field: 'lotName',
            title: '批次属性',
            sortable: true
        }, {
            field: 'abc',
            title: '商品ABC',
            sortable: true
        }, {
            field: 'groupCode',
            title: '商品组编码',
            sortable: true
        }, {
            field: 'materialCode',
            title: '物料编码',
            sortable: true
        }, {
            field: 'cubic',
            title: '体积',
            sortable: true
        }, {
            field: 'grossWeight',
            title: '毛重',
            sortable: true
        }, {
            field: 'netWeight',
            title: '净重',
            sortable: true
        }, {
            field: 'price',
            title: '单价',
            sortable: true
        }, {
            field: 'length',
            title: '长',
            sortable: true
        }, {
            field: 'width',
            title: '宽',
            sortable: true
        }, {
            field: 'height',
            title: '高',
            sortable: true
        }, {
            field: 'rcvUomName',
            title: '缺省收货单位',
            sortable: true
        }, {
            field: 'shipUomName',
            title: '缺省发货单位',
            sortable: true
        }, {
            field: 'printUomName',
            title: '缺省打印单位',
            sortable: true
        }, {
            field: 'maxLimit',
            title: '库存上限',
            sortable: true
        }, {
            field: 'minLimit',
            title: '库存下限',
            sortable: true
        }, {
            field: 'isValidity',
            title: '是否做效期控制',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'shelfLife',
            title: '保质期',
            sortable: true
        }, {
            field: 'lifeType',
            title: '周期类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'inLifeDays',
            title: '入库效期(天数)',
            sortable: true
        }, {
            field: 'outLifeDays',
            title: '出库效期(天数)',
            sortable: true
        }, {
            field: 'isOverRcv',
            title: '是否允许超收',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'overRcvPct',
            title: '超收百分比',
            sortable: true
        }, {
            field: 'paZoneName',
            title: '上架库区',
            sortable: true
        }, {
            field: 'paLoc',
            title: '上架库位',
            sortable: true
        }, {
            field: 'reserveCode',
            title: '上架库位指定规则',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_RESERVE_CODE'))}, value, "-");
            }
        }, {
            field: 'paRuleName',
            title: '上架规则',
            sortable: true
        }, {
            field: 'rotationRuleName',
            title: '库存周转规则',
            sortable: true
        }, {
            field: 'allocRuleName',
            title: '分配规则',
            sortable: true
        }, {
            field: 'cycleName',
            title: '循环级别',
            sortable: true
        }, {
            field: 'cdClass',
            title: '越库级别',
            sortable: true
        }, {
            field: 'lastCountTime',
            title: '上次循环盘点时间',
            sortable: true
        }, {
            field: 'firstInTime',
            title: '首次入库时间',
            sortable: true
        }, {
            field: 'style',
            title: '款号',
            sortable: true
        }, {
            field: 'color',
            title: '颜色',
            sortable: true
        }, {
            field: 'skuSize',
            title: '尺码',
            sortable: true
        }, {
            field: 'isDg',
            title: '是否危险品',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'dgClass',
            title: '危险品等级',
            sortable: true
        }, {
            field: 'unno',
            title: '危险品编号',
            sortable: true
        }, {
            field: 'isCold',
            title: '是否温控',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'minTemp',
            title: '最低温度',
            sortable: true
        }, {
            field: 'maxTemp',
            title: '最高温度',
            sortable: true
        }, {
            field: 'hsCode',
            title: '海关商品编码(HSCODE)',
            sortable: true
        }, {
            field: 'isSerial',
            title: '是否序列号管理',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'isParent',
            title: '是否为父件',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'isQc',
            title: '是否质检管理',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
            }
        }, {
            field: 'qcPhase',
            title: '质检阶段',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_QC_PHASE'))}, value, "-");
            }
        }, {
            field: 'qcRuleName',
            title: '质检规则',
            sortable: true
        }, {
            field: 'itemGroupName',
            title: '质检项组',
            sortable: true
        }, {
            field: 'rateGroup',
            title: '费率组编码',
            sortable: true
        }, {
            field: 'def1',
            title: '自定义1',
            sortable: true
        }, {
            field: 'def2',
            title: '自定义2',
            sortable: true
        }, {
            field: 'def3',
            title: '自定义3',
            sortable: true
        }, {
            field: 'def4',
            title: '自定义4',
            sortable: true
        }, {
            field: 'def5',
            title: '自定义5',
            sortable: true
        }, {
            field: 'def6',
            title: '自定义6',
            sortable: true
        }, {
            field: 'def7',
            title: '自定义7',
            sortable: true
        }, {
            field: 'def8',
            title: '自定义8',
            sortable: true
        }, {
            field: 'def9',
            title: '自定义9',
            sortable: true
        }, {
            field: 'def10',
            title: '自定义10',
            sortable: true
        }, {
            field: 'def11',
            title: '自定义11',
            sortable: true
        }, {
            field: 'def12',
            title: '自定义12',
            sortable: true
        }, {
            field: 'def13',
            title: '自定义13',
            sortable: true
        }, {
            field: 'def14',
            title: '自定义14',
            sortable: true
        }, {
            field: 'def15',
            title: '自定义15',
            sortable: true
        }, {
            field: 'periodOfValidity',
            title: '商品效期',
            sortable: true
        }, {
            field: 'validityUnit',
            title: '效期单位',
            sortable: true
        }, {
            field: 'typeCode',
            title: '商品类型',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_MATERIAL_TYPE'))}, value, "-");
            }
        }, {
            field: 'stockCurId',
            title: '采购币别',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_CURRENCY'))}, value, "-");
            }
        }, {
            field: 'quickCode',
            title: '快速录入码',
            sortable: true
        }, {
            field: 'formCode',
            title: '商品形态',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_MATERIAL_MORPHOLOGY'))}, value, "-");
            }
        }, {
            field: 'emergencyTel',
            title: '应急电话',
            sortable: true
        }, {
            field: 'effectiveDate',
            title: '生效日期',
            sortable: true
        }, {
            field: 'expirationDate',
            title: '失效日期',
            sortable: true
        }, {
            field: 'flashPoint',
            title: '闪点',
            sortable: true
        }, {
            field: 'burningPoint',
            title: '燃点',
            sortable: true
        }, {
            field: 'tempLevel',
            title: '商品温层',
            sortable: true,
            formatter: function (value, row, index) {
                return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_TEMPR_CATEGORY'))}, value, "-");
            }
        }, {
            field: 'remarks',
            title: '备注',
            sortable: true
        }]
    });
    $('#banQinCdWhSkuTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
        'check-all.bs.table uncheck-all.bs.table', function () {
        var length = $('#banQinCdWhSkuTable').bootstrapTable('getSelections').length;
        $('#remove').prop('disabled', !length);
        $('#edit').prop('disabled', length !== 1);
    });
}

function getIdSelections() {
    return $.map($("#banQinCdWhSkuTable").bootstrapTable('getSelections'), function (row) {
        return row.id
    });
}

function deleteAll() {
    jp.confirm('确认要删除该商品记录吗？', function () {
        jp.loading();
        jp.get("${ctx}/wms/basicdata/banQinCdWhSku/deleteAll?ids=" + getIdSelections(), function (data) {
            if (data.success) {
                $('#banQinCdWhSkuTable').bootstrapTable('refresh');
                jp.success(data.msg);
            } else {
                jp.bqError(data.msg);
            }
        })
    })
}

function add() {
    jp.openBQDialog('新增商品', "${ctx}/wms/basicdata/banQinCdWhSku/form", '90%', '90%', $('#banQinCdWhSkuTable'));
}

function edit(id) {
    if(id == undefined){
        id = getIdSelections();
    }
    jp.openBQDialog('编辑商品', "${ctx}/wms/basicdata/banQinCdWhSku/form?id=" + id,'90%', '90%', $('#banQinCdWhSkuTable'));
}

function importSku() {
    $("#uploadModal").modal();
    $("#uploadFileName").val('');
}

function downloadTemplate() {
    window.location = '${ctx}/wms/basicdata/banQinCdWhSku/import/template';
}

function uploadFile() {
    if (!$("#uploadFileName").val()) {
        jp.alert("请选择需要上传的文件");
        return;
    }
    jp.loading("正在导入中...");
    var file = $("#uploadFileName").get(0).files[0];
    var fm = new FormData();
    fm.append('file', file);
    fm.append('orgId', jp.getCurrentOrg().orgId);
    $.ajax({
        type: "post",
        url: "${ctx}/wms/basicdata/banQinCdWhSku/import",
        data: fm,
        async: false,
        cache: false,
        contentType: false,
        processData: false,
        // 传送请求数据
        success: function (data) {
            $("#uploadModal").modal('hide');
            $('#banQinCdWhSkuTable').bootstrapTable('refresh');
            jp.alert(data.msg);
        },
        error: function (data) {
            $("#uploadModal").modal('hide');
            jp.alert(data.msg);
        }
    });
}

function exportData() {
    $('#orgId').val(jp.getCurrentOrg().orgId);
    bq.exportExcel("${ctx}/wms/basicdata/banQinCdWhSku/export", "商品记录", $("#searchForm"), function () {
        jp.close();
    });
}

</script>