<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="showType" type="java.lang.String" required="false" description="显示类型"%><!-- 0:隐藏tip, 1隐藏box,不设置显示全部 -->
<%@ attribute name="content" type="java.lang.String" required="true" description="消息内容"%>
<%@ attribute name="type" type="java.lang.String" description="消息类型：info、success、warning、error、loading"%>
<c:if test="${not empty content}">
	<c:if test="${not empty type}"><c:set var="ctype" value="${type}"/></c:if>
	<c:if test="${empty type}"><c:set var="ctype" value="${fn:indexOf(content,'失败') eq -1?'success':'danger'}"/></c:if>
	<c:if test="${showType == '1'}">
		<div id="messageBox" class="alert alert-${ctype}">${content}<button data-dismiss="alert" class="close">×</button></div>
	</c:if>
	<c:if test="${showType == '2'}">
		<script type="text/javascript">
			top.layer.alert("${content}", {title: '导入信息', area: ['550px', '450px']});
		</script>
	</c:if>
	<c:if test="${showType != '1' && showType != '2'}">
		<script type="text/javascript">
			top.layer.msg("${content}", {icon: "${ctype=='success'? 1:0}"});
		</script>
	</c:if>
</c:if>