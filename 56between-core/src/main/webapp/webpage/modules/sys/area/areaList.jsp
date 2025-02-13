<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>区域列表</title>
    <meta name="decorator" content="ani"/>
    <%@include file="areaList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">区域列表 </h3>
        </div>
        <div class="panel-body">
            <!-- 工具栏 -->
            <div class="row">
                <div class="col-sm-12">
                    <div class="pull-left treetable-bar">
                        <shiro:hasPermission name="sys:area:add">
                            <a id="add" class="btn btn-primary" onclick="jp.openDialog('新增区域', '${ctx}/sys/area/form','800px', '240px',$treeTable)"></i> 新建</a>
                        </shiro:hasPermission>
                        <button class="btn btn-default" data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
                    </div>
                </div>
            </div>
            <table id="treeTable" class="table table-hover">
                <thead>
                <tr>
                    <th>区域名称</th>
                    <th>区域编码</th>
                    <th>区域类型</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            <br/>
        </div>
    </div>
</div>
<script type="text/html" id="areaItemTpl">
    <td><a href="#" onclick="jp.openDialogView('查看区域', '${ctx}/sys/area/form?id={{ d.id }}','800px', '240px')">{{ d.name }}</a></td>
    <td>{{ d.code === undefined? "" : d.code}}</td>
    <td>{{ d.type === undefined? "" : d.type}}</td>
    <td>
        <div class="btn-group">
            <button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
                <i class="fa fa-cog"></i>
                <span class="fa fa-chevron-down"></span>
            </button>
            <ul class="dropdown-menu" role="menu">
                <shiro:hasPermission name="sys:area:view">
                    <li><a href="#" onclick="jp.openDialogView('查看区域', '${ctx}/sys/area/form?id={{ d.id }}','800px', '240px')"><i class="fa fa-search-plus"></i> 查看</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:area:edit">
                    <li><a href="#" onclick="jp.openDialog('修改区域', '${ctx}/sys/area/form?id={{ d.id }}','800px', '240px', $treeTable)"><i class="fa fa-edit"></i> 修改</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:area:del">
                    <li><a onclick="return del(this, '{{ d.id }}')"><i class="fa fa-trash"></i> 删除</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:area:add">
                    <li><a href="#" onclick="jp.openDialog('添加下级区域', '${ctx}/sys/area/form?parent.id={{ d.id }}','800px', '240px', $treeTable)"><i class="fa fa-plus"></i> 添加下级区域</a></li>
                </shiro:hasPermission>
            </ul>
        </div>
    </td>
</script>
</body>
</html>