<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>区域设置</title>
    <meta name="decorator" content="ani"/>
    <%@include file="/webpage/include/treeview.jsp" %>
    <script type="text/javascript">
        var validateForm;
        var $table; // 父页面table表格id
        var $topIndex;//弹出窗口的 index
        function doSubmit(table, index) {//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
            if (validateForm.form()) {
                $table = table;
                $topIndex = index;
                $("#inputForm").submit();
                return true;
            }
            return false;
        }

        $(document).ready(function () {
            $('#areaTree').jstree({
                'core': {
                    "multiple": true,
                    "animation": 0,
                    "themes": {"icons": true, "stripes": false},
                    'data': {
                        "url": "${ctx}/sys/common/tms/transportScope/treeData?headId=${sysTmsTransportScope.id}",
                        "dataType": "json" // needed only if you do not supply JSON headers
                    }
                },
                'plugins': ["checkbox", 'types', 'wholerow'],
                "types": {
                    'default': {'icon': 'fa fa-file-text-o'},
                    'html': {'icon': 'fa fa-file-code-o'},
                    'btn': {'icon': 'fa fa-square'}
                },
                'checkbox': {
                    'three_state': false,// 禁用级联选中
                    'cascade': 'undetermined|down|up'//有三个选项，up, down, undetermined; 使用前需要先禁用three_state
                }
            });

            validateForm = $("#inputForm").validate({
                submitHandler: function (form) {
                    var ref = $('#areaTree').jstree(true);
                    var ids = ref.get_selected();
                    /*$("#areaTree li").has("i[class*='jstree-undetermined']").each(function () {//取半选节点ID
                        ids += "," + $(this).attr("id");
                    });*/
                    $("#areaIds").val(ids);

                    jp.loading();
                    $.post("${ctx}/sys/common/tms/transportScope/saveArea", $('#inputForm').serialize(), function (data) {
                        if (data.success) {
                            jp.success(data.msg);
                        } else {
                            jp.error(data.msg);
                        }
                        jp.close($topIndex);//关闭dialog
                    });
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });

        });
    </script>
</head>
<body>
<div id="areaTree"></div>
<form:form id="inputForm" modelAttribute="sysTmsTransportScope" method="post" class="form-horizontal">
    <form:hidden path="id"/>
    <form:hidden path="code"/>
    <form:hidden path="name"/>
    <form:hidden path="dataSet"/>
    <form:hidden path="areaIds"/>
</form:form>
</body>
</html>