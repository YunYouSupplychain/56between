<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <meta name="decorator" content="ani"/>
    <%@ include file="/webpage/include/bootstraptable.jsp" %>
    <script>
        var currentRow;
        $(document).ready(function () {
            $('#table').bootstrapTable({
                cache: false,//是否使用缓存
                pagination: true,//是否显示分页
                sidePagination: "server",//client客户端分页，server服务端分页
                singleSelect: ${!isMultiSelected},
                pageList: [10, 25, 50, 100],//可供选择的每页的行数
                url: "${url}",
                queryParams: function (params) {
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                onClickRow: function (row, $el) {
                    jp.changeTableStyle($el);
                },
                onDblClickRow: function (row, $el) {
                    currentRow = row;
                    rowDbClick();
                },
                columns: [{
                    checkbox: true
                } <c:forEach items="${fieldLabels}" var="name" varStatus="status">
                , {
                    field: '${fieldKeys[status.index]}',
                    title: '${fieldLabels[status.index]}',
                    sortable: true
                }</c:forEach>]
            });
            $("#search").click("click", function () {// 绑定查询按扭
                $('#table').bootstrapTable('refresh');
            });
            $("#reset").click("click", function () {// 绑定查询按扭
                $("#searchForm input:not(.no-reset)").val("");
                $("#searchForm input[name='codeAndName']").val("");
                $("#searchForm select").val("");
                $('#table').bootstrapTable('refresh');
            });
        });

        function getSelections() {
            return $.map($("#table").bootstrapTable('getSelections'), function (row) {
                return row
            });
        }

        function rowDbClick() {
            var parents = $(".layui-layer-iframe", window.parent.document);
            $(parents[parents.length - 1]).find('.layui-layer-close')[0].click();// 模拟右上角关闭
        }
    </script>
</head>
<body class="bg-white">
<div class="wrapper wrapper-content">
    <!-- 搜索 -->
    <div class="accordion-group">
        <div class="accordion-inner">
            <form id="searchForm" class="form well clearfix" onsubmit="return false;">
                <c:forEach items="${queryParams}" var="name" varStatus="status">
                    <input type="hidden" class="no-reset" name="${queryParams[status.index]}" value="${queryParamValues[status.index]}"/>
                </c:forEach>
                <c:forEach items="${searchLabels}" var="name" varStatus="status">
                    <div class="col-xs-12 col-sm-6 col-md-3" <c:if test="${status.index gt 3}">style="padding-top: 10px;"</c:if>>
                        <input name="${searchKeys[status.index]}" maxlength="64" class="form-control" placeholder="${searchLabels[status.index]}"/>
                    </div>
                </c:forEach>
                <div class="col-xs-12 col-sm-6 col-md-3" <c:if test="${fn:length(searchLabels) gt 4}">style="padding-top: 10px;"</c:if>>
                    <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                    <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                </div>
            </form>
        </div>
    </div>
    <!-- 表格 -->
    <table id="table" class="text-nowrap"></table>
</div>
</body>
</html>