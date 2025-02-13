<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>合同仓储价格管理</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/bootstraptable.jsp" %>
    <%@include file="bmsContractStoragePriceList.js" %>
</head>
<body>
<div style="margin: 10px; height: calc(100% - 150px); background-color: #FFFFFF;">
    <div class="col-xs-6 col-sm-6 col-md-6" style="padding: 0;">
        <shiro:hasPermission name="bms:bmsContract:add">
            <a id="add" class="btn btn-primary" onclick="add();">新增</a>
        </shiro:hasPermission>
        <shiro:hasPermission name="bms:bmsContract:edit">
            <button id="edit" class="btn btn-primary" disabled onclick="edit();">修改</button>
        </shiro:hasPermission>
        <shiro:hasPermission name="bms:bmsContract:del">
            <button id="remove" class="btn btn-danger" disabled onclick="deleteAll();">删除</button>
        </shiro:hasPermission>
    </div>
    <div class="col-xs-2 col-md-2 col-sm-2" style="padding: 0;">
        <input type="text" id="searchSku" class="form-control" onkeyup="skuSearch(event)" placeholder="输入品类或商品回车查询"/>
    </div>
    <div class="col-xs-8 col-sm-8 col-md-8" style="padding: 0">
        <table id="bmsContractStoragePriceTable" class="text-nowrap"></table>
    </div>
    <div class="col-xs-4 col-sm-4 col-md-4" style="padding: 0">
        <table id="bmsContractStorageSteppedPriceTable" class="text-nowrap"></table>
    </div>
</div>
</body>
</html>