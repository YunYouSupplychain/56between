<%@ page contentType="text/html;charset=UTF-8" %>
<script>
$(document).ready(function () {
	if ($("#id").val().length > 0) {
		$('#skuCode').prop('readonly', true);
		$("#ownerName").prop('readonly', true);
		$("#ownerNameSBtnId").prop('disabled', true);
		$("#ownerNameDBtnId").prop('disabled', true);
		$('#skuBarcode_add').attr('disabled', false);
	} else {
		$("#orgId").val(tmOrg.id);
	}

	$('#tmItemBarcodeTable').bootstrapTable({
		cache: false,// 是否使用缓存
		pagination: true,// 是否显示分页
		sidePagination: "server",// client客户端分页，server服务端分页
		url: "${ctx}/tms/basic/tmItemBarcode/data",
		queryParams: function (params) {
			var searchParam = $("#searchForm").serializeJSON();
			searchParam.ownerCode = $("#ownerCode").val();
			searchParam.skuCode = $("#skuCode").val();
			searchParam.orgId = $("#orgId").val();
			searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
			searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
			searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
			return searchParam;
		},
		onDblClickRow: function (row, $el) {
		<shiro:hasPermission name="basic:tmItemBarcode:edit">
			editSkuBarcode(row);
		</shiro:hasPermission>
		},
		columns: [{
			checkbox: true
		},{
			field: 'barcode',
			title: '条码*',
			sortable: true
		},{
			field: 'isDefault',
			title: '是否默认',
			sortable: true,
			formatter: function (value, row, index) {
				return jp.getDictLabel(${fns:toJson(fns:getDictList('SYS_YES_NO'))}, value, "-");
			}
		}]
	});
	$('#tmItemBarcodeTable').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
		'check-all.bs.table uncheck-all.bs.table', function () {
		var length = $("#tmItemBarcodeTable").bootstrapTable('getSelections').length;
		$('#skuBarcode_remove').attr('disabled', !length);
		$('#skuBarcode_edit').attr('disabled', length !== 1);
	});
});

function doSubmit($table, index) {
	jp.loading();
	var validator = bq.headerSubmitCheck("#inputForm");
	if (!validator.isSuccess) {
		jp.bqWaring(validator.msg);
		return;
	}
	var disabledObjs = bq.openDisabled("#inputForm");
	jp.post("${ctx}/tms/basic/tmItem/save", $('#inputForm').serialize(), function (data) {
		if (data.success) {
			jp.success(data.msg);
			$table.bootstrapTable('refresh');
			jp.close(index);
		} else {
			bq.closeDisabled(disabledObjs);
			jp.bqError(data.msg);
		}
	});
}

function getSkuBarcodeIdSelections() {
	return $.map($("#tmItemBarcodeTable").bootstrapTable('getSelections'), function (row) {
		return row.id
	});
}

function addSkuBarcode() {
	skuBarcodeModal();
}

function editSkuBarcode(row) {
	if(row === undefined){
		row = $("#tmItemBarcodeTable").bootstrapTable('getSelections')[0];
	}
	skuBarcodeModal(row);
}

function skuBarcodeModal(row) {
	$("#tmItemBarcodeForm input").val("");
	$("#tmItemBarcodeForm select").val("");
	if (row !== undefined) {
		for (var attr in row) {
			if ($("#skuBarcode_" + attr).length > 0) {
				$("#skuBarcode_" + attr).val(row[attr]);
			}
		}
	}
	$("#skuBarcode_ownerCode").val($("#ownerCode").val());
	$("#skuBarcode_skuCode").val($("#skuCode").val());
	$("#skuBarcode_orgId").val($("#orgId").val());
	$("#skuBarcode_isDefault").prop('checked', $("#skuBarcode_isDefault").val() === "Y");
	$("#tmItemBarcodeModal").modal();
}

function saveSkuBarcode() {
	jp.loading();
	var validator = bq.headerSubmitCheck("#tmItemBarcodeForm");
	if (!validator.isSuccess){
		jp.bqWaring(validator.msg);
		return;
	}
	jp.post("${ctx}/tms/basic/tmItemBarcode/save", $('#tmItemBarcodeForm').serialize(), function (data) {
		if (data.success) {
			jp.success(data.msg);
			$('#tmItemBarcodeTable').bootstrapTable('refresh');
			$("#tmItemBarcodeModal").modal('hide');
		} else {
			jp.bqError(data.msg);
		}
	});
}

function delSkuBarcode() {
	jp.confirm('确认要删除该条码记录吗？', function () {
		jp.loading();
		jp.get("${ctx}/tms/basic/tmItemBarcode/deleteAll?ids=" + getSkuBarcodeIdSelections(), function (data) {
			if (data.success) {
				$('#tmItemBarcodeTable').bootstrapTable('refresh');
				jp.success(data.msg);
			} else {
				jp.error(data.msg);
			}
		});
	});
}

function isDefaultClick(obj) {
	if ($(obj).prop('checked')) {
		$(obj).val('Y');
	} else {
		$(obj).val('N');
	}
}

</script>