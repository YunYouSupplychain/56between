<%@ taglib prefix="for" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>采购单管理</title>
	<meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#wmPoDetailTable').bootstrapTable({
                cache: false,// 是否使用缓存
                sidePagination: "server",// client客户端分页，server服务端分页
                url: "${ctx}/wms/inbound/banQinWmPoHeader/createAsnData",
                queryParams: function (params) {
                    var searchParam = {};
                    searchParam.id = "${banQinWmPoEntity.id}";
                    searchParam.orgId = "${banQinWmPoEntity.orgId}";
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                onClickRow: function (row, $el) {
                    jp.changeTableStyle($el);
                },
                columns: [{
                    checkbox: true
                }, {
                    field: 'lineNo',
                    title: '行号',
                    sortable: true
                }, {
                    field: 'status',
                    title: '状态',
                    sortable: true,
                    formatter: function (value, row, index) {
                        return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_WM_PO_STATUS'))}, value, "-");
                    }
                }, {
                    field: 'skuCode',
                    title: '商品编码',
                    sortable: true
                }, {
                    field: 'skuName',
                    title: '商品名称',
                    sortable: true
                }, {
                    field: 'qtyPoEa',
                    title: '采购数EA',
                    sortable: true
                }, {
                    field: 'qtyAsnEa',
                    title: '预收数EA',
                    sortable: true
                }, {
                    field: 'qtyRcvEa',
                    title: '已收数EA',
                    sortable: true
                }, {
                    field: 'packCode',
                    title: '包装规格',
                    sortable: true
                }, {
                    field: 'uom',
                    title: '包装单位',
                    sortable: true
                }, {
                    field: 'price',
                    title: '单价',
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
                    title: '批次属性4',
                    sortable: true
                }, {
                    field: 'lotAtt05',
                    title: '批次属性5',
                    sortable: true
                }, {
                    field: 'lotAtt06',
                    title: '批次属性6',
                    sortable: true
                }, {
                    field: 'lotAtt07',
                    title: '批次属性7',
                    sortable: true
                }, {
                    field: 'lotAtt08',
                    title: '批次属性8',
                    sortable: true
                }, {
                    field: 'lotAtt09',
                    title: '批次属性9',
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
        })

        function getSelections() {
            return $.map($("#wmPoDetailTable").bootstrapTable('getSelections'), function (row) {
                return row
            });
        }
        
        function confirm() {
            var rows = getSelections();
            if (rows.length === 0) {
                jp.warning("请勾选记录!");
                return;
            }
            
            var params = {};
            params.ownerCode = $('#ownerCode').val();
            params.supplierCode = $('#supplierCode').val();
            params.orgId = "${banQinWmPoEntity.orgId}";
            params.wmPoDetailEntitys = rows;
            $.ajax({
                url: "${ctx}/wms/inbound/banQinWmPoHeader/createAsn",
                type: "post",
                data: JSON.stringify(params),
                contentType: "application/json",
                success: function (data) {
                    if (data.success) {
                        closeIndex();
                        jp.success(data.msg);
                    } else {
                        jp.bqError(data.msg);
                    }
                }
            });
        }
        
        function closeIndex() {
            jp.close(parent.layer.getFrameIndex(window.name));
        }
    </script>
</head>
<body>
<div style="width: 100%; height: 100%;">
    <div id="toolbar" style="width: 100%; padding: 10px 10px;">
        <a class="btn btn-primary btn-sm" onclick="confirm()">确定</a>
        <a class="btn btn-primary btn-sm" onclick="closeIndex()">关闭</a>
    </div>
    <table>
        <tr>
            <td class="width-10"><label class="pull-right">货主编码</label></td>
            <td class="width-15"><input id="ownerCode" name="ownerCode" value="${banQinWmPoEntity.ownerCode}" class="form-control" readonly></td>
            <td class="width-10"><label class="pull-right">供应商编码</label></td>
            <td class="width-15"><input id="supplierCode" name="supplierCode" value="${banQinWmPoEntity.supplierCode}" class="form-control" readonly></td>
            <td class="width-10">&nbsp;</td>
            <td class="width-20">&nbsp;</td>
            <td class="width-20">&nbsp;</td>
            <td class="width-20">&nbsp;</td>
        </tr>
    </table>
    <div style="padding: 10px 10px;">
        <table id="wmPoDetailTable" class="text-nowrap"></table>
    </div>
</div>
</body>
</html>