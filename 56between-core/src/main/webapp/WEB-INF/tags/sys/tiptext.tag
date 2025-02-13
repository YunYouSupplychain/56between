<%--提示文本--%>
<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="tiptext" type="java.lang.String" required="true" description="说明文本" %>
<%@ attribute name="href" type="java.lang.String" description="跳转链接" %>
<span class="tip-help">
    <span class="icon-help">
    <c:choose>
        <c:when test="${empty href || href eq null}">
            <a href="#">?</a>
        </c:when>
        <c:otherwise>
            <a href="${href}" target="_blank">?</a>
        </c:otherwise>
    </c:choose>
    </span>
    <span class="tip">
        <span class="text">${tiptext}</span>
    </span>
</span>