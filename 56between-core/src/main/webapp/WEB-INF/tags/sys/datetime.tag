<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ attribute name="id" type="java.lang.String" required="true" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式" %>
<%@ attribute name="value" type="java.util.Date" required="false" description="值" %>
<%@ attribute name="disabled" type="java.lang.Boolean" required="false" description="是否不可用(true/false)" %>
<div id="${id}" class="input-group datetime">
    <input type="text" name="${name}" class="${cssClass}" value="<fmt:formatDate value="${value}" pattern="yyyy-MM-dd HH:mm:ss"/>" <c:if test="${disabled}">readonly</c:if>/>
    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
</div>
<script type="text/javascript">
    $(document).ready(function () {
        $("#${id}").datetimepicker({format: "YYYY-MM-DD HH:mm:ss"});
    })
</script>