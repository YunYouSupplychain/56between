<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>树选择控件</title>
    <meta name="decorator" content="blank"/>
    <%@include file="/webpage/include/treeview.jsp" %>
    <script type="text/javascript">
        var tree;
        $(document).ready(function () {
            $('#jstree').jstree({
                'core': {
                    "multiple": true,
                    "animation": 0,
                    "themes": {"icons": true, "stripes": false},
                    'data': {
                        "url": "${ctx}${url}${fn:indexOf(url,'?')==-1?'?':'&'}&extId=${extId}&isAll=${isAll}&t=" + new Date().getTime(),
                        "dataType": "json"
                    }
                },
                'plugins': ['types', "search", <c:if test="${checked==true}">'checkbox', </c:if> 'wholerow'],
                "types": {
                    'default': {'icon': 'fa fa-file-text-o'},
                    '1': {'icon': 'fa fa-home'},
                    '4': {'icon': 'fa fa-cube'},
                    '5': {'icon': 'fa fa-cubes'},
                    '6': {'icon': 'fa fa-group'},
                    '7': {'icon': 'fa fa-sitemap'},
                    '8': {'icon': 'fa fa-tuck'},
                    'btn': {'icon': 'fa fa-square'}
                }
            }).on('loaded.jstree', function () {
                <c:if test="${not empty text}">
                searchTree();
                </c:if>
            });
            tree = $('#jstree').jstree(true);

            <c:if test="${allowSearch}">
            $('#search').val("${text}");
            var to = false;
            $('#search').keyup(function () {
                if (to) {
                    clearTimeout(to);
                }
                to = setTimeout(searchTree, 250);
            });
            $('#searchBtn').click(searchTree);
            </c:if>
        });

        function searchTree() {
            $('#jstree').jstree(true).search($('#search').val(), false, true);
        }
    </script>
</head>
<body>
<c:if test="${allowSearch}">
<!-- 搜索 -->
<div class="input-search" style="margin: 5px 20px 0 20px">
    <button id="searchBtn" type="submit" class="input-search-btn"><i class="fa fa-search" aria-hidden="true"></i></button>
    <input id="search" type="text" class="form-control input-sm" name="" placeholder="请输入关键词...">
</div></c:if>
<div class="row">
    <div id="jstree"></div>
</div>
</body>
</html>