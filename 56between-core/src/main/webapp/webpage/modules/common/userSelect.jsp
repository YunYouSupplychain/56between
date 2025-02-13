<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>用户选择</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/anihead.jsp" %>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="/webpage/include/treeview.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            //bootstrap treeview初始化
            $('#jstree').jstree({
                'core': {
                    "multiple": false,
                    "animation": 0,
                    "themes": {"variant": "large", "icons": true, "stripes": true},
                    'data': {
                        "url": "${ctx}/sys/office/treeData",
                        "dataType": "json" // needed only if you do not supply JSON headers
                    }
                },
                "conditionalselect": function (node, event) {
                    return false;
                },
                'plugins': ['types', 'wholerow'],
                "types": {
                    'default': {'icon': 'fa fa-file-text-o'},
                    '1': {'icon': 'fa fa-home'},
                    '2': {'icon': 'fa fa-umbrella'},
                    '3': {'icon': 'fa fa-group'},
                    '4': {'icon': 'fa fa-file-text-o'}
                }
            }).bind("activate_node.jstree", function (obj, e) {
                // 处理代码
                // 获取当前节点
                var treeNode = e.node;
                var id = treeNode.id == '0' ? '' : treeNode.id;
                if (treeNode.level == 0) {//level=0 代表公司
                    $("#companyId").val(id);
                    $("#officeId").val("");
                } else {
                    $("#companyId").val("");
                    $("#officeId").val(id);
                }
                $('#table').bootstrapTable('refresh');
            }).on('loaded.jstree', function () {
                $("#jstree").jstree('open_all');
            });
            //初始化表格
            $('#table').bootstrapTable({
                cache: false,//是否使用缓存
                pagination: true,//是否显示分页
                sidePagination: "server",//client客户端分页，server服务端分页
                pageList: [5, 10, 'ALL'],
                url: "${ctx}/sys/user/list",
                queryParams: function (params) {
                    var searchParam = $("#searchForm").serializeJSON();
                    searchParam.pageNo = params.limit === undefined ? "1" : params.offset / params.limit + 1;
                    searchParam.pageSize = params.limit === undefined ? -1 : params.limit;
                    searchParam.orderBy = params.sort === undefined ? "" : params.sort + " " + params.order;
                    return searchParam;
                },
                columns: [{
                    <c:if test="${isMultiSelect}">
                    checkbox: true
                    </c:if>
                    <c:if test="${!isMultiSelect}">
                    radio: true
                    </c:if>
                }, {
                    field: 'loginName',
                    title: '登录名',
                    sortable: true
                }, {
                    field: 'name',
                    title: '姓名',
                    sortable: true,
                }, {
                    field: 'phone',
                    title: '电话',
                    sortable: true
                }, {
                    field: 'company.name',
                    title: '归属公司'
                }, {
                    field: 'office.name',
                    title: '归属机构'
                }]
            });
            $('#table').on('check.bs.table uncheck.bs.table load-success.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
                var length = $('#table').bootstrapTable('getSelections').length;
                $('#remove').prop('disabled', !length);
                $('#edit').prop('disabled', length != 1);
            });
            $("#search").click("click", function () {// 绑定查询按扭
                $('#table').bootstrapTable('refresh');
            });
            $("#reset").click("click", function () {// 绑定查询按扭
                $("#searchForm  input").val("");
                $("#searchForm  select").val("");
                zTreeObj.cancelSelectedNode();
                $('#table').bootstrapTable('refresh');
            });
        });

        function getIdSelections() {
            return $.map($("#table").bootstrapTable('getSelections'), function (row) {
                return row.id
            });
        }

        function getNameSelections() {
            return $.map($("#table").bootstrapTable('getSelections'), function (row) {
                return row.name
            });
        }

        function getSelections() {
            return $.map($("#table").bootstrapTable('getSelections'), function (row) {
                return row
            });
        }
    </script>
</head>
<body class="bg-white">

<div class="wrapper wrapper-content">
    <div class="row">
        <div class="col-sm-4 col-md-3">
            <div id="jstree"></div>
        </div>
        <div class="col-sm-8 col-md-9 animated fadeInRight">
            <!-- 搜索框-->
            <div class="accordion-group">
                <div id="collapseTwo" class="accordion-body">
                    <div class="accordion-inner">
                        <form id="searchForm" class="form form-horizontal well clearfix">
                            <input type="hidden" id="companyId" name="company.id"/>
                            <input type="hidden" id="officeId" name="office.id"/>
                            <div class="col-sm-4">
                                <input type="text" name="loginName" maxlength="100" class="form-control" placeholder="登录名"/>
                            </div>
                            <div class="col-sm-4">
                                <input type="text" name="name" maxlength="100" class=" form-control" placeholder="姓名"/>
                            </div>
                            <div class="col-sm-4">
                                <a id="search" class="btn btn-primary"><i class="fa fa-search"></i> 查询</a>
                                <a id="reset" class="btn btn-primary"><i class="fa fa-refresh"></i> 重置</a>
                            </div>
                        </form>
                    </div>
                </div>
            </div><!-- 搜索框结束 -->
            <!-- 表格 -->
            <table id="table" data-toolbar="#toolbar" data-minimum-count-columns="2" data-id-field="id"></table>
        </div>
    </div>
</div>
</body>
</html>