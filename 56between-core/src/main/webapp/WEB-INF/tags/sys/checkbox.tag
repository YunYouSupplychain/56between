<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="id"%>
<%@ attribute name="name" type="java.lang.String" required="true" description="name"%>
<%@ attribute name="items" type="java.util.ArrayList" required="true" description="字典对象"%>
<%@ attribute name="values" type="java.lang.String" required="true" description="复选框值"%>
<%@ attribute name="cssClass" type="java.lang.String" required="true" description="复选框值"%>
<c:forEach items="${items}" var="item" varStatus="status">
    <label style="padding-right: 10px;"><input name="${name}" type="checkbox" value="${item.value}"  <c:if test="${fn:contains(','.concat(values).concat(','), ','.concat(item.value).concat(','))}">checked</c:if> class="${cssClass}"/><span style="padding-left: 3px;"></span>${item.label} </label>
</c:forEach>
<input type="hidden" name="${name}" >
<label id="${name}-error" class="error" for="${name}" style="display: none;"></label>
