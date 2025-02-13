<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>菜单管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="menuList.js" %>
    <%@include file="menu-dataRuleList.js" %>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">菜单列表 </h3>
        </div>
        <div class="panel-body">
            <div class="row">
                <div id="left" class="col-sm-12">
                    <!-- 工具栏 -->
                    <div id="toolbar" class="treetable-bar">
                        <shiro:hasPermission name="sys:menu:add">
                            <a id="add" class="btn btn-primary" onclick="jp.openDialog('新建菜单', '${ctx}/sys/menu/form','1000px', '450px', $treeTable)">新建菜单</a>
                        </shiro:hasPermission>
                        <button class="btn btn-default" data-toggle="tooltip" data-placement="left" onclick="refresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
                    </div><!-- 工具栏结束 -->
                    <table id="treeTable" class="table table-hover">
                        <thead>
                        <tr>
                            <th>名称</th>
                            <th>链接</th>
                            <th>排序</th>
                            <th>显示/隐藏</th>
                            <th>权限标识</th>
                            <th>操作</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
                <div id="right" class="panel panel-default col-sm-6" style="display:none">
                    <div class="panel-heading">
                        <h3 class="panel-title"><label>数据规则列表，所属菜单: </label><font id="menuLabel"></font><input type="hidden" id="menuId"/></h3>
                    </div>
                    <div class="panel-body">
                        <div id="dataRuleToolbar">
                            <button id="dataRuleAddButton" class="btn btn-primary" title="添加规则"> 新建</button>
                            <button id="dataRuleEditButton" class="btn btn-primary" disabled title="修改规则"> 修改</button>
                            <button id="dataRuleDelButton" class="btn btn-danger" disabled title="删除规则"> 删除</button>
                        </div>
                        <!-- 表格 -->
                        <table id="dataRuleTable" data-toolbar="#dataRuleToolbar" data-id-field="id"></table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/html" id="menuItemTpl">
    <td><a href="#" onclick="jp.openDialogView('查看菜单', '${ctx}/sys/menu/form?id={{d.id}}','1000px', '450px')"> <i class="{{d.icon}}"></i> {{d.name}}</a></td>
    <td title="{{d.href}}">{{d.hideFullName}}</td>
    <td>
        <a href="javascript:;" onclick="sort('{{d.id}}',true);"><i class="fa  fa-long-arrow-up"></i></a>
        <a href="javascript:;" onclick="sort('{{d.id}}',false);"><i class="fa  fa-long-arrow-down"></i></a>
    </td>
    <td>
        {{# if(d.isShow){ }}<i class="fa fa-circle text-success ml5"></i>{{# }else{ }}
        <i class="fa fa-circle text-muted ml5"></i>{{# } }}
    </td>
    <td title="{{d.permission}}">
        {{d.hidePermission}}
    </td>
    <td>
        <div class="btn-group">
            <button type="button" class="btn  btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">
                <i class="fa fa-cog"></i>
                <span class="fa fa-chevron-down"></span>
            </button>
            <ul class="dropdown-menu" role="menu">
                <shiro:hasPermission name="sys:menu:view">
                    <li><a href="#" onclick="jp.openDialogView('查看菜单', '${ctx}/sys/menu/form?id={{d.id}}','1000px', '450px')"><i class="fa fa-search-plus"></i> 查看</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:menu:edit">
                    <li><a href="#" onclick="jp.openDialog('修改菜单', '${ctx}/sys/menu/form?id={{d.id}}','1000px', '450px', $treeTable)"><i class="fa fa-edit"></i> 修改</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:menu:del">
                    <li><a onclick="return del(this, '{{d.id}}')"><i class="fa fa-trash"></i> 删除</a></li>
                </shiro:hasPermission>
                <shiro:hasPermission name="sys:menu:add">
                    <li><a href="#" onclick="jp.openDialog('添加下级菜单', '${ctx}/sys/menu/form?parent.id={{d.id}}','1000px', '450px', $treeTable)"><i class="fa fa-plus"></i> 添加下级菜单</a></li>
                </shiro:hasPermission>
            </ul>
        </div>
        <shiro:hasPermission name="sys:role:datarule">
            <button type="button" onclick="setDataRule('{{d.id}}','{{d.name}}')" class="btn btn-primary btn-xs">
                <i class="fa fa-database"> 数据规则</i>
            </button>
        </shiro:hasPermission>
    </td>
</script>
</body>
</html>