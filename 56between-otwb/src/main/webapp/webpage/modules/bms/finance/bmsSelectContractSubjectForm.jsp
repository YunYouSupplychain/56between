<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>合同科目条款管理</title>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
</head>
<body>
<div class="tabs-container">
    <table id="bmsSettleModelTermsTable" class="text-nowrap"></table>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        buildTable();
        $('#bmsSettleModelTermsTable').bootstrapTable('load', ${fns:toJson(bmsSettleModelEntity.detailList)});
    });

    function buildTable() {
        $('#bmsSettleModelTermsTable').bootstrapTable({
            cache: false,// 是否使用缓存
            pagination: true,// 是否显示分页
            sidePagination: "client",// client客户端分页，server服务端分页
            columns: [{
                checkbox: true
            }, {
                field: 'settleObjectName',
                title: '结算对象',
                sortable: true
            }, {
                field: 'sysContractNo',
                title: '系统合同编号',
                sortable: true
            }, {
                field: 'contractNo',
                title: '客户合同编号',
                sortable: true
            }, {
                field: 'contractStatus',
                title: '合同状态',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_CONTRACT_STATUS'))}, value, "-");
                }
            }, {
                field: 'billModule',
                title: '费用模块',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_MODULE'))}, value, "-");
                }
            }, {
                field: 'billSubjectName',
                title: '费用科目名称',
                sortable: true
            }, {
                field: 'receivablePayable',
                title: '应收应付',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_RECEIVABLE_PAYABLE'))}, value, "-");
                }
            }, {
                field: 'billCategory',
                title: '费用类别',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_BILL_SUBJECT_CATEGORY'))}, value, "-");
                }
            }, {
                field: 'billTermsDesc',
                title: '计费条款说明',
                sortable: true
            }, {
                field: 'outputObjects',
                title: '输出对象',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_OUTPUT_OBJECT'))}, value, "-");
                }
            }, {
                field: 'occurrenceQuantity',
                title: '发生量',
                sortable: true,
                formatter: function (value, row, index) {
                    return jp.getDictLabel(${fns:toJson(fns:getDictList('BMS_OCCURRENCE_QUANTITY'))}, value, "-");
                }
            }, {
                field: 'formulaName',
                title: '计费公式',
                sortable: true
            }]
        });
    }

    function doSubmit($table, $topIndex) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
        var entity = ${fns:toJson(bmsSettleModelEntity)};
        var detailList = $("#bmsSettleModelTermsTable").bootstrapTable("getSelections");
        if (detailList.length <= 0) {
            jp.warning("必须选择一条数据!");
            return false;
        }
        entity.detailList = detailList;

        jp.loading();
        $.ajax({
            type: "POST",
            url: "${ctx}/bms/finance/bmsSettleModel/addDetail",
            contentType: 'application/json;charset=utf-8',
            data: JSON.stringify(entity),
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    jp.success(data.msg);
                    jp.close($topIndex);//关闭dialog
                    $table.bootstrapTable('refresh');
                } else {
                    jp.bqError(data.msg);
                }
            },
            error: function (e) {
                jp.error(e);
            }
        });
    }
</script>
</body>
</html>