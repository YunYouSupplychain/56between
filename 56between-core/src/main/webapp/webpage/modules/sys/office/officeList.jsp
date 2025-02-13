<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>机构列表</title>
    <meta name="decorator" content="ani"/>
    <%@include file="officeList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">机构列表 </h3>
        </div>
        <div class="panel-body">
            <!-- 工具栏 -->
            <div class="row">
                <div class="col-sm-12">
                    <div class="pull-left treetable-bar">
                        <shiro:hasPermission name="sys:office:add">
                            <a id="add" class="btn btn-primary" onclick="jp.openDialog('新增机构', '${ctx}/sys/office/form','800px', '500px',$treeTable)"> 新建</a>
                        </shiro:hasPermission>
                        <button class="btn btn-default" data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
                    </div>
                </div>
            </div>
            <table id="treeTable" class="table table-hover">
                <thead>
                <tr>
                    <th>机构名称</th>
                    <th>归属区域</th>
                    <th>机构编码</th>
                    <th>机构类型</th>
                    <th>备注</th>
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
<script type="text/html" id="officeItemTpl">
    <td><a href="#" onclick="jp.openDialogView('查看机构', '${ctx}/sys/office/form?id={{d.id}}','800px', '500px')">{{d.name}}</a></td>
    <td>{{# if(d.area){ }} {{d.area.name}} {{# } }}</td>
    <td>{{d.code === undefined ? "": d.code}}</td>
    <td>{{d.typeLabel === undefined ? "": d.typeLabel }}</td>
    <td>{{d.remarks === undefined ? "":d.remarks}}</td>
    <td>
        <div class="btn-group">
            <button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
                <i class="fa fa-cog"></i>
                <span class="fa fa-chevron-down"></span>
            </button>
            <ul class="dropdown-menu" role="menu">
                <shiro:hasPermission name="sys:office:view">
                    <li><a href="#" onclick="jp.openDialogView('查看机构', '${ctx}/sys/office/form?id={{d.id}}','800px', '500px')"><i class="fa fa-search-plus"></i> 查看</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:office:edit">
                    <li><a href="#" onclick="jp.openDialog('修改机构', '${ctx}/sys/office/form?id={{d.id}}','800px', '500px', $treeTable)"><i class="fa fa-edit"></i> 修改</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:office:del">
                    <li><a onclick="return del(this, '{{d.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:office:add">
                    <li><a href="#" onclick="jp.openDialog('添加下级机构', '${ctx}/sys/office/form?parent.id={{d.id}}','800px', '500px', $treeTable)"><i class="fa fa-plus"></i> 添加下级机构</a></li>
                </shiro:hasPermission>
            </ul>
        </div>
    </td>
</script>
</body>
</html>