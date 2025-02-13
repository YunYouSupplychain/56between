<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>网点拓扑图管理</title>
    <meta name="decorator" content="ani"/>
    <script type="text/javascript" src="${ctxStatic}/tms/common.js"></script>
    <%@include file="tmOutletRelationList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">网点拓扑图列表 </h3>
        </div>
        <div class="panel-body">
            <sys:message content="${message}"/>
            <!-- 工具栏 -->
            <div class="row">
                <div class="col-sm-12">
                    <div class="pull-left treetable-bar">
                        <shiro:hasPermission name="basic:tmOutletRelation:add">
                            <a id="add" class="btn btn-primary" onclick="jp.openDialog('新建网点拓扑图', '${ctx}/tms/basic/tmOutletRelation/form','800px', '500px', $tmOutletRelationTreeTable)"> 新建</a>
                        </shiro:hasPermission>
                        <button class="btn btn-default" data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
                    </div>
                </div>
            </div>
            <table id="tmOutletRelationTreeTable" class="table table-hover">
                <thead>
                <tr>
                    <th>网点编码</th>
                    <th>网点名称</th>
                    <th>是否启用</th>
                    <th>备注信息</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody id="tmOutletRelationTreeTableList"></tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/html" id="tmOutletRelationTreeTableTpl">
    <td>
        {{d.row.code === undefined ? "": d.row.code}}
    </td>
    <td>
        {{d.row.name === undefined ? "": d.row.name}}
    </td>
    <td>
        {{d.row.delFlag === undefined ? "": d.row.delFlag}}
    </td>
    <td>
        {{d.row.remarks === undefined ? "": d.row.remarks}}
    </td>
    <td>
        <div class="btn-group">
            <button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
                <i class="fa fa-cog"></i>
                <span class="fa fa-chevron-down"></span>
            </button>
            <ul class="dropdown-menu" role="menu">
                <shiro:hasPermission name="basic:tmOutletRelation:view">
                    <li><a href="#" onclick="jp.openDialogView('查看网点拓扑图', '${ctx}/tms/basic/tmOutletRelation/form?id={{d.row.id}}','800px', '500px')"><i class="fa fa-search-plus"></i> 查看</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmOutletRelation:edit">
                    <li><a href="#" onclick="jp.openDialog('修改网点拓扑图', '${ctx}/tms/basic/tmOutletRelation/form?id={{d.row.id}}','800px', '500px', $tmOutletRelationTreeTable)"><i class="fa fa-edit"></i>
                        修改</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmOutletRelation:del">
                    <li><a onclick="return del(this, '{{d.row.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmOutletRelation:enable">
                    <li><a href="#" onclick="return enable('{{d.row.id}}')"> 启用停用</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="basic:tmOutletRelation:add">
                    <li><a href="#" onclick="jp.openDialog('添加下级网点拓扑图', '${ctx}/tms/basic/tmOutletRelation/form?parent.id={{d.row.id}}','800px', '500px', $tmOutletRelationTreeTable)"> 添加下级网点关系网</a>
                    </li>
                </shiro:hasPermission>
            </ul>
        </div>
    </td>
</script>
</body>
</html>