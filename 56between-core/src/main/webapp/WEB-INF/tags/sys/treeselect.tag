<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）"%>
<%@ attribute name="value" type="java.lang.String" required="false" description="隐藏域值（ID）"%>
<%@ attribute name="labelName" type="java.lang.String" required="false" description="输入框名称（Name）"%>
<%@ attribute name="labelValue" type="java.lang.String" required="false" description="输入框值（Name）"%>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题"%>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址"%>
<%@ attribute name="checked" type="java.lang.Boolean" required="false" description="是否显示复选框，如果不需要返回父节点，请设置notAllowSelectParent为true"%>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）"%>
<%@ attribute name="isAll" type="java.lang.Boolean" required="false" description="是否列出全部数据，设置true则不进行数据权限过滤（目前仅对Office有效）"%>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点"%>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点"%>
<%@ attribute name="allowSearch" type="java.lang.Boolean" required="false" description="是否允许搜索"%>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式"%>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮"%>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled"%>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description=""%>
<input id="${id}Id" name="${name}" class="${cssClass} form-control" type="hidden" value="${value}"/>
<div class="input-group" style="width:100%">
	<input id="${id}Name" name="${labelName}" ${allowSearch?'':'readonly="readonly"'} type="text" value="${labelValue}"
		   class="${cssClass}" style="${cssStyle}" data-msg-required="${dataMsgRequired}"/>
	 <span class="input-group-btn">
		 <button type="button"  id="${id}Button" class="btn btn-primary
			<c:if test="${fn:contains(cssClass, 'input-sm')}"> btn-sm </c:if>
			<c:if test="${fn:contains(cssClass, 'input-lg')}"> btn-lg </c:if>
			${disabled} ${hideBtn ? 'hide' : ''}"><i class="fa fa-search"></i>
		 </button>
		 <button type="button" id="${id}DelButton" class="close" data-dismiss="alert" style="position: absolute; top: 5px; right: 32px; z-index: 999; display: block;">×</button>
	 </span>
</div>
<label id="${id}Name-error" class="error" for="${id}Name" style="display:none"></label>
<script type="text/javascript">
$(document).ready(function(){
	$("#${id}Name").off('keyup').on('keyup', function (e) {
		if (e.keyCode === 13 && !$(this).prop('readonly')) {// 输入框回车
			${id}Layer("${allowSearch}" === "true" ? $("#${id}Name").val() : "");
		}
	});
	$("#${id}Button").click(function () {
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")) {
			return true;
		}
		${id}Layer("");
	});
	$("#${id}DelButton").click(function(){
		// 是否限制选择，如果限制，设置为disabled
		if ($("#${id}Button").hasClass("disabled")){
			return true;
		}
		$("#${id}Id").val("");
		$("#${id}Name").val("");
		$("#${id}Name").focus();
	});
})
function ${id}Layer(text) {
	top.layer.open({
		type: 2,
		area: ['350px', '750px'],
		title: "${title}",
		ajaxData: {selectIds: $("#${id}Id").val()},
		content: "${ctx}/tag/treeSelect?url=" + encodeURIComponent("${url}")
				+ "&extId=${extId}&checked=${checked}&allowSearch=${allowSearch}&text=" + encodeURIComponent(text)
				+ "&isAll=${isAll}",
		btn: ['确定', '关闭'],
		yes: function (index, layero) {
			var tree = layero.find("iframe")[0].contentWindow.tree;
			var ids = [], codes = [], names = [], nodes;
			if ("${checked}" === "true") {
				nodes = tree.get_checked(true);
			} else {
				nodes = tree.get_selected(true);
			}
			for (var i = 0; i < nodes.length; i++) {
				<c:if test="${checked && notAllowSelectParent}">
				if (nodes[i].original.isParent) {
					// 如果为复选框选择，则过滤掉父节点
					continue;
				}</c:if>
				<c:if test="${notAllowSelectRoot}">
				if (nodes[i].original.level === 0) {
					top.layer.msg("不能选择根节点（" + nodes[i].text + "）请重新选择。", {icon: 0});
					return false;
				}</c:if>
				<c:if test="${notAllowSelectParent}">
				if (nodes[i].original.isParent) {
					top.layer.msg("不能选择父节点（" + nodes[i].text + "）请重新选择。", {icon: 0});
					return false;
				}</c:if>
				ids.push(nodes[i].id);
				// 从自定义属性中获取编码
				codes.push(nodes[i].a_attr.code);
				names.push(nodes[i].text);
				<c:if test="${!checked}">
				// 如果为非复选框选择，则返回第一个选择
				break;
				</c:if>
			}
			$("#${id}Id").val(ids.join(",").replace(/u_/ig, ""));
			$('#${id}Code').val(codes.join(","));
			$("#${id}Name").val(names.join(","));
			$("#${id}Name").focus();
			top.layer.close(index);
		},
		cancel: function (index) {}
	});
}
</script>