/**
 * 工具组件 对原有的工具进行封装，自定义某方法统一处理
 */
(function () {
    jp = {
        /**
         * 使用jp.open代替top.layer.open，参数使用完全一致，请参照layer官网
         */
        open: top.layer.open,
        info: function (msg) {
            return top.layer.msg(msg);
        },
        warning: function (msg) {
            return top.layer.msg(msg, {icon: 0, time: 3000});
        },
        success: function (msg) {
            return top.layer.msg(msg, {icon: 1});
        },
        error: function (msg) {
            return top.layer.msg(msg, {icon: 2, time: 3000});
        },
        /**
         * 加载层，一直阻塞浏览器窗口，必须手动调用close方法关闭
         * @param msg
         * @returns {*}
         */
        loading: function (msg) {
            if (!msg) {
                msg = '正在提交，请稍等...';
            }
            return top.layer.msg(msg, {
                icon: 16,
                shade: 0.01,
                time: 999999999
            });
        },
        /**
         * 关闭加载层
         * @param index
         */
        close: function (index) {
            if (index) {
                top.layer.close(index);
            } else {
                top.layer.closeAll();
            }
        },
        /**
         * alert弹出框，阻塞浏览器窗口
         * @param msg
         */
        alert: function (msg) {
            top.layer.alert(msg, {
                skin: 'layui-layer-lan',
                area: ['auto', 'auto'],
                icon: 0,
                anim: 4,
                closeBtn: 0
            });
        },
        /**
         * 询问框，阻塞浏览器窗口
         * @param msg
         * @param succFuc
         * @param cancelFuc
         * @returns {boolean}
         */
        confirm: function (msg, succFuc, cancelFuc) {
            top.layer.confirm(msg, {
                icon: 3, title: '系统提示', btn: ['是', '否'],
                success: function (layero) {
                    let btn = layero[0].getElementsByClassName('layui-layer-btn')[0].getElementsByTagName('A')[0];
                    btn.href = 'javascript:void(0)';
                    btn.focus();
                }
            }, function (index) {
                if (typeof succFuc == 'function') {
                    succFuc();
                } else {
                    location = succFuc;
                    jp.success("操作成功！", {icon: 1});
                }
                top.layer.close(index);
            }, function (index) {
                if (cancelFuc)
                    cancelFuc();
                top.layer.close(index);
            });
            return false;
        },
        prompt: function (title, href) {
            var index = top.layer.prompt({title: title, formType: 2}, function (text) {
                if (typeof href == 'function') {
                    href(text);
                } else {
                    location = href + encodeURIComponent(text);
                }
                top.layer.close(index);
            });

        },
        /**
         * 打开对话框(添加修改)
         * @param title
         * @param url
         * @param width
         * @param height
         * @param $table
         */
        openDialog: function (title, url, width, height, $table) {
            //是否使用响应式，使用百分比时，应设置为false
            let auto = true;
            if (width.indexOf("%") >= 0 || height.indexOf("%") >= 0) {
                auto = false;
            }
            top.layer.open({
                type: 2,
                area: [width, height],
                title: title,
                auto: auto,
                maxmin: false,
                content: url,
                btn: ['保存', '取消'],
                yes: function (index, layero) {
                    var iframeWin = layero.find('iframe')[0];
                    if (!$table) {
                        //如果不传递table对象过来，按约定的默认id获取table对象
                        $table = $('#table')
                    }
                    iframeWin.contentWindow.doSubmit($table, index);
                },
                cancel: function (index) {
                }
            });
        },
        /**
         * 打开对话框(查看)
         * @param title
         * @param url
         * @param width
         * @param height
         */
        openDialogView: function (title, url, width, height) {
            //是否使用响应式，使用百分比时，应设置为false
            let auto = true;
            if (width.indexOf("%") >= 0 || height.indexOf("%") >= 0) {
                auto = false;
            }
            top.layer.open({
                type: 2,
                area: [width, height],
                title: title,
                auto: auto,
                maxmin: false,
                content: url,
                btn: ['关闭'],
                cancel: function (index) {}
            });
        },
        /**
         * 用户选择框
         * @param isMultiSelect
         * @param yesFuc
         */
        openUserSelectDialog: function (isMultiSelect, yesFuc) {
            top.layer.open({
                type: 2,
                area: ['900px', '560px'],
                title: "选择用户",
                auto: true,
                maxmin: false,
                content: ctx + "/sys/user/userSelect?isMultiSelect=" + isMultiSelect,
                btn: ['确定', '关闭'],
                yes: function (index, layero) {
                    let ids = layero.find("iframe")[0].contentWindow.getIdSelections();
                    let names = layero.find("iframe")[0].contentWindow.getNameSelections();
                    if (ids.length === 0) {
                        jp.warning("请选择至少一个用户!");
                        return;
                    }
                    yesFuc(ids.join(","), names.join(","));
                    top.layer.close(index);
                },
                cancel: function (index) {
                    top.layer.close(index);
                }
            });
        },
        bqWaring: function (msg, callBack) {
            return top.layer.alert(msg, {
                icon: 0,
                success: function (layero) {
                    let btn = layero[0].getElementsByClassName('layui-layer-btn')[0].getElementsByTagName('A')[0];
                    btn.href = 'javascript:void(0)';
                    btn.focus();
                },
                end: function () {
                    if (typeof callBack == 'function') {
                        callBack();
                    }
                }
            });
        },
        bqError: function (msg) {
            return top.layer.alert(msg, {icon: 0});
        },
        /**
         * 打开自定义对话框(添加修改)
         * @param title
         * @param url
         * @param width
         * @param height
         * @param $table
         */
        openBQDialog: function (title, url, width, height, $table) {
            //是否使用响应式，使用百分比时，应设置为false
            let auto = true;
            if (width.indexOf("%") >= 0 || height.indexOf("%") >= 0) {
                auto = false;
            }
            top.layer.open({
                type: 2,
                area: [width, height],
                title: title,
                auto: auto,
                maxmin: false,
                content: url,
                cancel: function (index) {
                    sessionStorage.clear();
                },
                end: function () {
                    $table.bootstrapTable('refresh');
                }
            });
        },
        /**
         * layer之外的另一个选择toast风格消息提示框,直接使用jp.toastr调用
         */
        toastr: (function () {
            top.toastr.options = {
                "closeButton": true,
                "debug": false,
                "progressBar": true,
                "positionClass": "toast-top-right",
                "onclick": null,
                "showDuration": "400",
                "hideDuration": "5000",
                "timeOut": "10000",
                "extendedTimeOut": "1000",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut"
            }
            return top.toastr;
        })(),
        /**
         * 页面提示声音
         */
        voice: function () {
            let audio = document.createElement("audio");
            audio.src = ctxStatic + "/common/voice/default.wav";
            audio.play();
        },
        /**
         * 打开选项卡
         * @param url
         * @param title
         * @param isNew 为true时，打开一个新的选项卡；为false时，如果选项卡不存在，打开一个新的选项卡，如果已经存在，使已经存在的选项卡变为活跃状态。
         */
        openTab: function (url, title, isNew) {
            top.openTab(url, title, isNew);
        },
        /**
         * 打开一个窗体
         * @param url
         * @param name
         * @param width
         * @param height
         */
        windowOpen: function (url, name, width, height) {
            let top = parseInt((window.screen.height - height) / 2, 10),
                left = parseInt((window.screen.width - width) / 2, 10),
                options = "location=no,menubar=no,toolbar=no,dependent=yes,minimizable=no,modal=yes,alwaysRaised=yes," +
                    "resizable=yes,scrollbars=yes," + "width=" + width + ",height=" + height + ",top=" + top + ",left=" + left;
            window.open(url, name, options);
        },
        /**
         * 获取当前激活画面的父窗体对象
         * @returns {WindowProxy}
         */
        getParent: function () {
            return top.getActiveTab()[0].contentWindow;
        },
        validateForm: function (id) {
            return $(id).validate({
                errorPlacement: function (error, element) {
                    if (element.is(":checkbox") || element.is(":radio")) {
                        error.appendTo(element.parent().parent().parent().parent());
                    } else if (element.parent().is(".form_datetime") || element.parent().is(".input-append") || element.is(".mydatepicker")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            }).form();
        },
        /**
         * 打开图片预览框
         * @param url
         */
        showPic: function (url) {
            let json = {
                "data": [{"src": url}]
            };
            top.layer.photos({
                photos: json,
                anim: 0
            });
        },
        escapeHTML: function (a) {
            a = "" + a;
            return a.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&apos;");
        },
        unescapeHTML: function (a) {
            a = "" + a;
            return a.replace(/&lt;/g, "<").replace(/&gt;/g, ">").replace(/&amp;/g, "&").replace(/&quot;/g, '"').replace(/&apos;/g, "'");
        },
        get: function (url, callback) {
            $.ajax({
                url: url,
                method: "get",
                error: function (xhr) {
                    if (xhr.status === 0) {
                        jp.info("连接失败，请检查网络!")
                    } else if (xhr.status === 404) {
                        jp.info("请求地址不存在！");
                    } else if (xhr.status === 500) {
                        jp.info("异常操作！");
                    } else {
                        jp.info("未知错误!");
                    }
                },
                success: function (data) {
                    if (data.indexOf === "_login_page_") {
                        top.layer.alert("登录超时！");
                        location.reload(true);
                    } else {
                        callback(data);
                    }
                }
            });
        },
        post: function (url, data, callback) {
            $.ajax({
                url: url,
                method: "post",
                data: data,
                error: function (xhr) {
                    if (xhr.status === 0) {
                        jp.info("连接失败，请检查网络!")
                    } else if (xhr.status === 404) {
                        jp.info("请求地址不存在！");
                    } else if (xhr.status === 500) {
                        jp.info("异常操作！");
                    } else {
                        jp.info("未知错误!");
                    }
                },
                success: function (data) {
                    if (data.indexOf === "_login_page_") {
                        top.layer.alert("登录超时！");
                        location.reload(true);
                    } else {
                        callback(data);
                    }
                }
            });
        },
        /**
         * 获取字典标签
         * @param data
         * @param value
         * @param defaultValue
         * @returns {*}
         */
        getDictLabel: function (data, value, defaultValue) {
            for (let i = 0; i < data.length; i++) {
                let row = data[i];
                if (row.value === value) {
                    return row.label;
                }
            }
            return defaultValue;
        },
        /**
         * 获取右上角当前机构信息
         * @returns {{orgId, orgCode, orgName}}
         */
        getCurrentOrg: function () {
            return {
                orgId: window.top.document.getElementById('companyId').value,
                orgCode: window.top.document.getElementById('companyCode').value,
                orgName: window.top.document.getElementById('companyName').value
            }
        },
        /**
         * 解析弹出框设置的以英文逗号分隔的检索参数
         * @param queryParams 检索参数名
         * @param queryParamValues 检索参数值
         * @returns {string}
         */
        parseParams: function (queryParams, queryParamValues) {
            if (queryParams && queryParamValues) {
                let key = queryParams.split(",");
                let value = queryParamValues.split(",");
                if (key.length === value.length) {
                    let result = "|";
                    for (let i in key) {
                        result = result + key[i] + "=" + $('#' + value[i]).val() + "|";
                    }
                    return result;
                }
                return "";
            }
            return "";
        },
        /**
         * 改变表格背景色
         * @param $el
         */
        changeTableStyle: function ($el) {
            $('.info').removeClass('info');
            $($el).addClass('info');
        },
        /**
         * 改变表格背景色
         * @param $table
         * @param $el
         */
        changeTargetTableStyle: function ($table, $el) {
            $($table + ' .info').removeClass('info');
            $($el).addClass('info');
        },
        /**
         * 校验批量勾选是否存在不同平台的数据
         */
        isExistDifOrg: function (arrays) {
            return $.unique(arrays).length <= 1;
        },
        dateFormat: function (timestamp, format) {
            let _this = new Date(timestamp);
            let o = {
                "M+": _this.getMonth() + 1,
                "d+": _this.getDate(),
                "h+": _this.getHours(),
                "m+": _this.getMinutes(),
                "s+": _this.getSeconds(),
                "q+": Math.floor((_this.getMonth() + 3) / 3),
                "S": _this.getMilliseconds()
            };
            if (/(y+)/.test(format) || /(Y+)/.test(format)) {
                format = format.replace(RegExp.$1, (_this.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            for (let k in o) {
                if (new RegExp("(" + k + ")").test(format)) {
                    format = format.replace(RegExp.$1, RegExp.$1.length === 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
                }
            }
            return format;
        },
        /**
         * 模拟a标签下载
         * @param url
         * @param name
         */
        downloadFile: function (url, name) {
            $("<a></a>").attr("href", url).attr("download", name)[0].click();
        }
    }

    bq = {
        get: function (url, sync, callback) {
            $.ajax({
                url: url,
                method: "get",
                async: sync,
                error: function (xhr) {
                    if (xhr.status === 0) {
                        jp.info("连接失败，请检查网络!")
                    } else if (xhr.status === 404) {
                        jp.info("请求地址不存在！");
                    } else if (xhr.status === 500) {
                        jp.info("异常操作！");
                    } else {
                        jp.info("未知错误!");
                    }
                },
                success: function (data) {
                    if (data.indexOf === "_login_page_") {
                        top.layer.alert("登录超时！")
                        location.reload(true);
                    } else {
                        callback(data);
                    }
                }
            });
        },
        openWindow: function (name) {
            window.open('about:blank', name, 'height=600, width=1200, , toolbar=no, menubar=no, scrollbars=yes, resizable=no, location=no, status=no');
        },
        openPostWindow: function (url, queryParams, queryParamValues, name) {
            let tempForm = document.createElement("form");
            tempForm.id = "tempForm1";
            tempForm.method = "post";
            tempForm.action = url;
            tempForm.target = name;
            if (queryParams && queryParamValues) {
                let key = queryParams.split("|");
                let value = queryParamValues.split("|");
                if (key.length === value.length) {
                    for (let i in key) {
                        let hideInput = document.createElement("input");
                        hideInput.type = "hidden";
                        hideInput.name = key[i]
                        hideInput.value = value[i];
                        tempForm.appendChild(hideInput);
                    }
                }
            }
            if (tempForm.attachEvent) {
                tempForm.attachEvent("onsubmit", function () {
                    bq.openWindow(name);
                });
            } else if (tempForm.addEventListener) {
                tempForm.addEventListener("submit", function () {
                    bq.openWindow(name);
                }, false);
            }
            document.body.appendChild(tempForm);
            if (document.createEvent) {
                let evt = document.createEvent("MouseEvents");
                evt.initMouseEvent("submit", true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);
                tempForm.dispatchEvent(evt);
            } else if (tempForm.fireEvent) {
                tempForm.fireEvent('onsubmit');
            }
            tempForm.submit();
            document.body.removeChild(tempForm);
        },
        labelC: function (name, isRequired) {
            return '<td class="width-25"><label class="pull-left">' + (isRequired ? '<font color="red">*</font>' : '') + name + '</label></td>';
        },
        dateC: function (divId, inputId, inputName, isRequired) {
            var html = '<td class="width-25">';
            html += '<div class=\'input-group form_datetime\' id=\'' + divId + '\'>';
            html += '<input id="' + inputId + '" name="' + inputName + '" class="form-control' + (isRequired ? ' required' : '') + '" pattern="yyyy-MM-dd"/>';
            html += '<span class="input-group-addon">';
            html += '<span class="glyphicon glyphicon-calendar"></span>';
            html += '</span>';
            html += '</div>';
            html += '</td>';
            return html;
        },
        dateLC: function (inputId, inputName, isRequired) {
            var html = '<td class="width-25">';
            html += '<input id="' + inputId + '" name="' + inputName + '" class="form-control detail-date' + (isRequired ? ' required' : '') + '" pattern="yyyy-MM-dd"/>';
            html += '</td>';
            return html;
        },
        inputC: function (inputId, inputName, isRequired) {
            return '<td class="width-25"><input id="' + inputId + '" name="' + inputName + '" class="form-control' + (isRequired ? ' required' : '') + '" maxlength="64"/></td>';
        },
        inputNumberC: function (inputId, inputName, isRequired) {
            return '<td class="width-25"><input id="' + inputId + '" name="' + inputName + '" class="form-control' + (isRequired ? ' required' : '') + '" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="64"/></td>';
        },
        comboBoxC: function (selectId, selectName, key, isRequired) {
            var html = '<td class="width-25">';
            html += '<select id="' + selectId + '" name="' + selectName + '" class="form-control m-b' + (isRequired ? ' required' : '') + '">';
            html += '<option value=""></option>';
            // 获取数据字典中的值
            this.get(ctx + "/sys/dict/getByType?type=" + key, false, function (data) {
                if (data || data.length > 0) {
                    for (var i in data) {
                        if (selectName.substring(selectName.length - 1) === '4') {
                            html += '<option value="' + data[i].value + '">' + data[i].label + '</option>';
                        } else {
                            html += '<option value="' + data[i].label + '">' + data[i].label + '</option>';
                        }
                    }
                }
            });
            html += '</select>';
            html += '</td>';
            return html;
        },
        getLotLabelC: function (data, rowCount) {
            var count = 0, preCount = 0;
            var trArray = [];
            var tr = '<tr>';
            for (var x in data) {
                if (count !== 0 && count % rowCount === 0 && count !== preCount) {
                    tr += '</tr>';
                    trArray.push(tr);
                    tr = '<tr>';
                    preCount = count;
                }
                var control = data[x];
                if ('F' !== control.inputControl) {
                    count++;
                    tr += bq.labelC(control.title, 'R' === control.inputControl);
                }
            }
            if (tr !== '<tr>') {
                // 补齐标签个数
                tr += bq.getBlankLabel(tr, rowCount);
                tr += '</tr>';
                trArray.push(tr);
            }
            return trArray;
        },
        getLotTextC: function (data, prefix, rowCount) {
            var count = 0, preCount = 0;
            var trArray = [];
            var tr = '<tr>';
            for (var x in data) {
                if (count !== 0 && count % rowCount === 0 && count !== preCount) {
                    tr += '</tr>';
                    trArray.push(tr);
                    tr = '<tr>';
                    preCount = count;
                }
                var control = data[x];
                var seq = control.lotAtt;
                seq = seq.substring(seq.length - 2);
                var inputId = prefix + seq;
                var inputName = prefix.indexOf('_') !== -1 ? prefix.split('_')[1] + seq : prefix + seq;
                if ('F' !== control.inputControl) {
                    count++;
                    if ('DateField' === control.fieldType || 'DateTimeField' === control.fieldType) {
                        tr += bq.dateLC(inputId, inputName, 'R' === control.inputControl);
                    } else if ('TextInput' === control.fieldType || '' === control.fieldType) {
                        tr += bq.inputC(inputId, inputName, 'R' === control.inputControl);
                    } else if ('ComboBox' === control.fieldType) {
                        tr += bq.comboBoxC(inputId, inputName, control.key, 'R' === control.inputControl);
                    } else if ('NumberField' === control.fieldType) {
                        tr += bq.inputNumberC(inputId, inputName, 'R' === control.inputControl);
                    }
                }
            }
            if (tr !== '<tr>') {
                // 补齐标签个数
                tr += bq.getBlankLabel(tr, rowCount);
                tr += '</tr>';
                trArray.push(tr);
            }
            return trArray;
        },
        getLotAttTab: function (data, prefix, rowCount) {
            if (!rowCount) rowCount = 4;
            var result = '<tbody>';
            var labelArray = this.getLotLabelC(data, rowCount);
            var inputArray = this.getLotTextC(data, prefix, rowCount);
            for (var x in labelArray) {
                result += labelArray[x] + inputArray[x];
            }
            result += '</tbody>';
            return result;
        },
        getBlankLabel: function (html, count) {
            var result = '';
            var length = html.split("</td>").length - 1;
            if (length > 0) {
                for (var i = 0, len = count - length; i < len; i++) {
                    result += '<td class="width-25"></td>';
                }
            }
            return result;
        },
        horizontalLabelC: function (name, isRequired) {
            return '<td class="width-10"><label class="pull-right">' + (isRequired ? '<font color="red">*</font>' : '') + name + '：</label></td>';
        },
        horizontalDateLC: function (inputId, inputName, isRequired) {
            var html = '<td class="width-15">';
            html += '<input id="' + inputId + '" name="' + inputName + '" class="form-control detail-date' + (isRequired ? ' required' : '') + '" pattern="yyyy-MM-dd"/>';
            html += '</td>';
            return html;
        },
        horizontalInputC: function (inputId, inputName, isRequired) {
            return '<td class="width-15"><input id="' + inputId + '" name="' + inputName + '" class="form-control' + (isRequired ? ' required' : '') + '" maxlength="64"/></td>';
        },
        horizontalInputNumberC: function (inputId, inputName, isRequired) {
            return '<td class="width-15"><input id="' + inputId + '" name="' + inputName + '" class="form-control' + (isRequired ? ' required' : '') + '" onkeyup="value=value.replace(/[^\\d.]/g,\'\')" maxlength="64"/></td>';
        },
        horizontalComboBoxC: function (selectId, selectName, key, isRequired) {
            var html = '<td class="width-15">';
            html += '<select id="' + selectId + '" name="' + selectName + '" class="form-control m-b' + (isRequired ? ' required' : '') + '">';
            html += '<option value=""></option>';
            // 获取数据字典中的值
            this.get(ctx + "/sys/dict/getByType?type=" + key, false, function (data) {
                if (data || data.length > 0) {
                    for (var i in data) {
                        if (selectName.substring(selectName.length - 1) === '4') {
                            html += '<option value="' + data[i].value + '">' + data[i].label + '</option>';
                        } else {
                            html += '<option value="' + data[i].label + '">' + data[i].label + '</option>';
                        }
                    }
                }
            });
            html += '</select>';
            html += '</td>';
            return html;
        },
        getHorizontalLotAttTab: function (data, prefix, rowCount) {
            if (!rowCount) rowCount = 4;
            var result = '<tbody>';
            var count = 0, preCount = 0;
            var tr = '<tr>';
            for (var x in data) {
                if (count !== 0 && count % rowCount === 0 && count !== preCount) {
                    tr += '</tr>';
                    result += tr;
                    tr = '<tr>';
                    preCount = count;
                }
                var control = data[x];
                var seq = control.lotAtt;
                seq = seq.substring(seq.length - 2);
                var inputId = prefix + seq;
                var inputName = prefix.indexOf('_') !== -1 ? prefix.split('_')[1] + seq : prefix + seq;
                if ('F' !== control.inputControl) {
                    tr += bq.horizontalLabelC(control.title, 'R' === control.inputControl);
                    if ('DateField' === control.fieldType || 'DateTimeField' === control.fieldType) {
                        tr += bq.horizontalDateLC(inputId, inputName, 'R' === control.inputControl);
                    } else if ('TextInput' === control.fieldType || '' === control.fieldType) {
                        tr += bq.horizontalInputC(inputId, inputName, 'R' === control.inputControl);
                    } else if ('ComboBox' === control.fieldType) {
                        tr += bq.horizontalComboBoxC(inputId, inputName, control.key, 'R' === control.inputControl);
                    } else if ('NumberField' === control.fieldType) {
                        tr += bq.horizontalInputNumberC(inputId, inputName, 'R' === control.inputControl);
                    }
                    count++;
                }
            }
            if (tr !== '<tr>') {
                // 补齐标签个数
                tr += bq.getHorizontalBlankLabel(tr, rowCount);
                tr += '</tr>';
                result += tr;
            }
            result += '</tbody>';
            return result;
        },
        getHorizontalBlankLabel: function (html, count) {
            var result = '';
            var length = html.split("</td>").length - 1;
            if (length > 0) {
                for (var i = 0, len = count - length / 2; i < len; i++) {
                    result += '<td class="width-10"></td><td class="width-15"></td>';
                }
            }
            return result;
        },
        /**
         * 头部表单验证
         * @param obj
         */
        headerSubmitCheck: function (obj) {
            var flag = true;
            var errorMsg = '';
            $(obj).find(".required").each(function () {
                if (!$(this).val()) {
                    $(this).addClass('form-error');
                    $(this).attr("placeholder", "不能为空");
                    flag = false;
                    if (!errorMsg) {
                        if ($(this).parent().prop('tagName') === 'td'.toUpperCase()) {
                            errorMsg = $(this).parent().prev().text().replace('*', '').replace('：', '') + "不能为空！";
                        } else if ($(this).parent().parent().prop('tagName') === 'td'.toUpperCase()) {
                            errorMsg = $(this).parent().parent().prev().text().replace('*', '').replace('：', '') + "不能为空！";
                        }
                    }
                }
            });
            return {isSuccess: flag, msg: errorMsg};
        },
        /**
         * 明细表单验证
         * @returns {boolean}
         */
        detailSubmitCheck: function (obj, rowCount) {
            if (!rowCount) rowCount = 4;
            var flag = true;
            var errorMsg = '';
            var tab = obj;
            $(obj).find(".required").each(function () {
                if (!$(this).val()) {
                    $(this).addClass('form-error');
                    $(this).attr("placeholder", "不能为空");
                    flag = false;
                    if (!errorMsg) {
                        var index = '';
                        if ($(this).parent().prop('tagName') === 'td'.toUpperCase()) {
                            index = $(tab + ' .bq-table tr td').index($(this).parent());
                            rowCount = $(this).parent().siblings().length;
                        } else if ($(this).parent().parent().prop('tagName') === 'td'.toUpperCase()) {
                            index = $(tab + ' .bq-table tr td').index($(this).parent().parent());
                            rowCount = $(this).parent().parent().siblings().length;
                        }
                        errorMsg = $(tab + " .bq-table tr td").eq(index - rowCount - 1).text().replace('*', '').replace('：', '') + "不能为空！";
                    }
                }
            });
            return {isSuccess: flag, msg: errorMsg};
        },
        /**
         * table中单元格必填校验,仅限格式<table><thead><tr><th></th></tr></thead><tbody><tr><td></td></tr></tbody></table>使用
         * @param obj table对象
         * @returns {{isSuccess: boolean, msg: string}}
         */
        tableValidate: function (obj) {
            var isSuccess = true;
            var errorMsg = '';
            $(obj).find(".required").each(function () {
                var $this = $(this);
                if (!$this.val()) {
                    $this.addClass('form-error');
                    $this.attr("placeholder", "不能为空");
                    isSuccess = false;
                    if (!errorMsg) {
                        var i = -1;
                        $this.parents("tr").children("td").each(function () {
                            i = i + 1;
                            if ($(this).is($this.parents("td").eq(0))) {
                                return false;
                            }
                        });
                        errorMsg = $this.parents("tbody").eq(0).prev("thead").children("tr").eq(0).children("th").eq(i).text().replace('*', '') + "不能为空";
                    }
                }
            });
            return {isSuccess: isSuccess, msg: errorMsg};
        },
        parseParams: function (queryParams, queryParamValues) {
            if (queryParams && queryParamValues) {
                var key = queryParams.split("|");
                var value = queryParamValues.split("|");
                if (key.length === value.length) {
                    var result = "&";
                    for (var i in key) {
                        result = result + key[i] + "=" + encodeURIComponent($('#' + value[i]).val()) + "&";
                    }
                    return result;
                }
                return "";
            }
            return "";
        },
        getQueryParamsValue: function (queryParamValues) {
            if (queryParamValues) {
                var value = queryParamValues.split("|");
                var result = [];
                for (var i in value) {
                    result.push(encodeURIComponent($('#' + value[i]).val()));
                }
                return result.join('|');
            }
            return "";
        },
        /**
         * 对象转url参数
         * @param {*} data
         */
        objToUrlParams: function (data) {
            var _result = [];
            for (var key in data) {
                var value = data[key];
                // 去掉为空的参数
                if (['', undefined, null].includes(value)) {
                    continue;
                }
                _result.push(key + '=' + encodeURIComponent(value));
            }
            return _result.length ? _result.join('&') : '';
        },
        openDisabled: function (obj) {
            var disabledObj = [];
            $(obj + " :disabled").each(function () {
                if (parseInt($(this).val()) !== -1) {
                    $(this).prop("disabled", false);
                    disabledObj.push($(this));
                }
            });
            return disabledObj;
        },
        closeDisabled: function (disabledObj) {
            for (var i = 0; i < disabledObj.length; i++) {
                $(disabledObj[i]).prop("disabled", true);
            }
        },
        /**
         * 文本框数字校验器
         * @param obj 输入框对象
         * @param limit 0:整数 大于0:保留小数位 其它:不限
         * @param plusMinus 0:非负数 1:负数 其它:不限
         */
        numberValidator: function (obj, limit, plusMinus) {
            // 整数
            var intRegExp = new RegExp("^(-?(0|[1-9][0-9]*))$");
            // 非负整数
            var nonnegativeIntRegExp = new RegExp("^(0|[1-9][0-9]*)$");
            // 负整数
            var negtiveIntRegExp = new RegExp("^-([1-9][0-9]*)$");

            // 浮点数
            var floatRegExp = new RegExp("^(-?(0|([1-9][0-9]*)))(\\.\\d{0," + limit + "})?$");
            // 非负浮点数
            var nonnegativeFloatRegExp = new RegExp("^(0|([1-9][0-9]*))(\\.\\d{0," + limit + "})?$");
            // 负浮点数
            var negtiveFloatRegExp = new RegExp("^-(0|([1-9][0-9]*))(\\.\\d{0," + limit + "})?$");

            var regExp = new RegExp("^(-?(0|([1-9][0-9]*)))(\\.\\d+)?$");
            var nonnegativeRegExp = new RegExp("^(0|([1-9][0-9]*))(\\.\\d+)?$");
            var negtiveRegExp = new RegExp("^-(0|([1-9][0-9]*))(\\.\\d+)?$");

            var value = obj.value;
            if (limit === 0) { // 整数
                if (plusMinus === 0 && !nonnegativeIntRegExp.test(value)) { // 非负数
                    obj.value = value.substring(0, value.length - 1);
                } else if (plusMinus === 1 && !negtiveIntRegExp.test(value) && !/^(-?)$/.test(value)) { // 负数
                    obj.value = value.substring(0, value.length - 1);
                } else if (!intRegExp.test(value) && !/^(-?)$/.test(value)) {
                    obj.value = value.substring(0, value.length - 1);
                }
            } else if (limit > 0) { // 浮点数(限制小数位)
                if (plusMinus === 0 && !nonnegativeFloatRegExp.test(value) && !/^(0|([1-9][0-9]*))(\.)?$/.test(value)) { // 非负数
                    obj.value = value.substring(0, value.length - 1);
                } else if (plusMinus === 1 && !negtiveFloatRegExp.test(value) && !/^-$/.test(value) && !/^(-(0|([1-9][0-9]*)))(\.)?$/.test(value)) { // 负数
                    obj.value = value.substring(0, value.length - 1);
                } else if (!floatRegExp.test(value) && !/^(-?)$/.test(value) && !/^(-?(0|([1-9][0-9]*)))(\.)?$/.test(value)) {
                    obj.value = value.substring(0, value.length - 1);
                }
            } else {
                if (plusMinus === 0 && !nonnegativeRegExp.test(value) && !/^(0|([1-9][0-9]*))(\.)?$/.test(value)) { // 非负数
                    obj.value = value.substring(0, value.length - 1);
                } else if (plusMinus === 1 && !negtiveRegExp.test(value) && !/^-$/.test(value) && !/^(-(0|([1-9][0-9]*)))(\.)?$/.test(value)) { // 负数
                    obj.value = value.substring(0, value.length - 1);
                } else if (!regExp.test(value) && !/^(-?)$/.test(value) && !/^(-?(0|([1-9][0-9]*)))(\.)?$/.test(value)) {
                    obj.value = value.substring(0, value.length - 1);
                }
            }
        },
        /**
         * 复制
         * @param source
         * @param target
         */
        copyProperties: function (source, target) {
            for (obj in source) {
                target[obj] = source[obj];
            }
        },
        /**
         * 根据查询表单导出excel(xlsx)
         * @param url 请求路径
         * @param fileName 下载文件名(不带后缀名)
         * @param $searchForm 条件表单
         * @param callback 回调函数
         */
        exportExcel: function (url, fileName, $searchForm, callback) {
            var idx = jp.loading("导出中...");
            var httpRequest = new XMLHttpRequest();
            httpRequest.open("POST", url, true);
            httpRequest.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            httpRequest.responseType = "blob";
            httpRequest.onload = function (oEvent) {
                var elink = document.createElement('a');
                elink.download = fileName + ".xlsx";
                elink.style.display = 'none';
                elink.href = URL.createObjectURL(new Blob([httpRequest.response]));
                document.body.appendChild(elink);
                elink.click();
                document.body.removeChild(elink);
            };
            // 回调函数
            httpRequest.onreadystatechange = function (oEvent) {
                if (callback) {
                    callback();
                }
                jp.close(idx);
            };
            httpRequest.send($searchForm.serialize()); // $("#searchForm").serialize()  提取表单数据
        },
        /**
         * 根据查询表单导出excel(xlsx)
         * @param url 请求路径
         * @param fileName 下载文件名(不带后缀名)
         * @param data json对象数据
         * @param callback 回调函数
         */
        exportExcelNew: function (url, fileName, data, callback) {
            var idx = jp.loading("正在导出，请稍后...");
            var xhr;
            if (window.XMLHttpRequest) {
                xhr = new XMLHttpRequest();
            } else if (window.ActiveXObject) {
                xhr = new ActiveXObject("Microsoft.XMLHTTP");
            } else {
                jp.warning("浏览器不支持");
                return;
            }
            xhr.open("post", url);
            xhr.setRequestHeader("Content-Type", "application/json;charset=utf-8");
            xhr.responseType = "blob";// 将返回的数据转为blod
            xhr.onloadend = function (oEvent) {
                var resContentType = xhr.getResponseHeader("Content-Type");// 返回类型
                if (xhr.status === 200 && resContentType) {
                    resContentType = resContentType.toLowerCase();
                    if (resContentType.startsWith("application/octet-stream")) {// 返回文件流，下载
                        var elink = document.createElement('a');// 在DOM中创建<a>标签元素
                        elink.download = fileName + ".xlsx";
                        elink.style.display = 'none';
                        var bold = new Blob([xhr.response]);
                        elink.href = URL.createObjectURL(bold);
                        document.body.appendChild(elink);
                        elink.click();// 调用点击事件触发下载
                        document.body.removeChild(elink);// 移除创建的<a>标签元素
                        try {
                            if (typeof (eval(callback)) === "function") {
                                callback();// 调用回调函数
                            }
                        } catch (e) {
                        }
                        jp.close(idx);// 关闭提示层
                    } else if (resContentType.startsWith("application/json")) {// 返回json
                        var fr = new FileReader();
                        fr.readAsText(new Blob([xhr.response]));// 将blod数据转text
                        fr.onload = function (e) {
                            try {
                                jp.error(JSON.parse(fr.result).msg);
                            } catch (e) {
                                jp.error("导出失败");
                            }
                        };
                        fr.error = function () {
                            jp.error("导出失败");
                        };
                    } else {// 其它
                        jp.error("导出失败");
                    }
                } else {
                    jp.error("导出失败");
                }
            };
            xhr.send(JSON.stringify(data));
        },
        importExcel: function (url) {
            jp.open({
                type: 1,
                area: '400px',
                title: "导入数据",
                content: '<form method="post" action="' + url + '" enctype="multipart/form-data" style="padding-left:20px;text-align:center;"><br/>' +
                    '<input type="hidden" name="orgId"/>' +
                    '<input type="file" name="file" style="width:330px"/>导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！<br/>' +
                    '</form>',
                btn: ['下载模板', '确定', '关闭'],
                btn1: function () {
                    window.location = url + "/template";
                },
                btn2: function (index, layero) {
                    // 注意：使用此方法的界面必须引入<sys:message content="${message}"/>，否则提示信息无法输出，loading提示会一直存在
                    jp.loading(' 正在导入，请稍等...');
                    var $form = layero.find('form');
                    $form.find("input[name='orgId']").val(jp.getCurrentOrg().orgId);
                    // 表单提交成功后，从服务器返回的url在当前tab中展示
                    $form.attr("target", top.getActiveTab().attr("name"));
                    $form.submit();
                    jp.close(index);
                },
                btn3: function (index) {
                    jp.close(index);
                }
            });
        },
        /**
         * 校验上传文件的大小
         * @param file 文件
         * @param maxsize 限制大小
         * @returns {boolean}
         */
        checkUploadFileSize: function (file, maxsize) {
            var errMsg = "上传的附件文件不能超过2M！！！";
            var tipMsg = "您的浏览器暂不支持计算上传文件的大小，确保上传文件不要超过2M，建议使用IE、FireFox、Chrome浏览器。";
            var browserCfg = {};
            var ua = window.navigator.userAgent;
            if (ua.indexOf("MSIE") >= 1) {
                browserCfg.ie = true;
            } else if (ua.indexOf("Firefox") >= 1) {
                browserCfg.firefox = true;
            } else if (ua.indexOf("Chrome") >= 1) {
                browserCfg.chrome = true;
            }
            try {
                var filesize = 0;
                if (browserCfg.firefox || browserCfg.chrome) {
                    filesize = file.size;
                } else if (browserCfg.ie) {
                    filesize = file.size;
                } else {
                    jp.alert("非IE 谷歌" + tipMsg);
                    return false;
                }
                if (filesize === -1) {
                    jp.alert(tipMsg);
                    return false;
                } else if (filesize > maxsize) {
                    jp.alert(errMsg);
                    return false;
                } else {
                    return true;
                }
            } catch (e) {
                jp.alert(e);
                return false;
            }
        },
        failVoice: function () {
            bq.voice("F");
        },
        successVoice: function () {
            bq.voice("S");
        },
        voice: function (type) {
            var fileName = type === 'F' ? 'fail.wav' : 'success.wav';
            var audio = document.createElement("audio");
            audio.src = ctxStatic + "/common/voice/" + fileName;
            audio.play();
        },
        serializeJson: function (obj) {
            var o = {};
            var a = obj.serializeArray();
            $.each(a, function () {
                if (o[this.name] !== undefined) {
                    if (o[this.name] == null || !o[this.name].push) {
                        o[this.name] = [o[this.name]];
                    }
                    o[this.name].push(this.value || null);
                } else {
                    o[this.name] = this.value || null;
                }
            });
            return o;
        },
        /**
         * 后台静默打印
         */
        printWayBill: function (images) {
            var imageList = images;
            if (imageList) {
                var LODOP = getLodop(document.getElementById('LODOP'), document.getElementById('LODOP_EM'));
                if (LODOP) {
                    LODOP.PRINT_INIT("");
                    for (var i = 0; i < imageList.length; i++) {
                        LODOP.NEWPAGE();
                        LODOP.ADD_PRINT_IMAGE(0, 0, "100%", "100%", "<img src='data:image/png;base64," + imageList[i] + "'/>");
                    }
                    LODOP.SET_LICENSES(LODOP_ZH_CN_NAME, LODOP_ZH_CN_LICENSES, LODOP_ZH_TW_NAME, LODOP_ZH_TW_LICENSES);
                    LODOP.SET_LICENSES(LODOP_THIRD_NAME, LODOP_THIRD_LICENSES, LODOP_EB_NAME, LODOP_EN_LICENSES);
                    LODOP.PRINT();
                }
            }
        },
        getSysControlParam: function (paramCode, orgId) {
            var result;
            this.get(ctx + "/sys/sysControlParams/getValue?paramCode=" + paramCode + "&orgId=" + orgId, false, function (data) {
                result = data;
            });
            return result;
        }
    }

    /**
     * 解决jquery序列化checkbox radio未选择状态不能获取值问题
     */
    $.fn.bq_serialize = function () {
        let a = this.serializeArray();
        let $radio = $('input[type=radio],input[type=checkbox]', this);
        let temp = {};
        $.each($radio, function () {
            if (!temp.hasOwnProperty(this.name) && $("input[name='" + this.name + "']:checked").length === 0) {
                temp[this.name] = "";
                a.push({name: this.name, value: "N"});
            }
        });
        return jQuery.param(a);
    }
    /**
     * 给Number类型增加 加法函数
     * @param arg
     * @returns {number}
     */
    Number.prototype.add = function (arg) {
        let l1 = this.toString().indexOf('.') > 0 ? this.toString().split(".")[1].length : 0,
            l2 = arg.toString().indexOf('.') > 0 ? arg.toString().split(".")[1].length : 0,
            pw = Math.pow(10, Math.max(l1, l2));
        return (arg * pw + pw * this) / pw;
    };
    /**
     * 给Number类型增加 减法函数
     * @param arg
     * @returns {string}
     */
    Number.prototype.sub = function (arg) {
        let l1 = this.toString().indexOf('.') > 0 ? this.toString().split(".")[1].length : 0,
            l2 = arg.toString().indexOf('.') > 0 ? arg.toString().split(".")[1].length : 0,
            pw = Math.pow(10, Math.max(l1, l2)),
            //动态控制精度长度
            l = (l1 >= l2) ? l1 : l2;
        return ((this * pw - arg * pw) / pw).toFixed(l);
    };
    /**
     * 给Number类型增加 乘法函数
     * @param arg
     * @returns {number}
     */
    Number.prototype.mul = function (arg) {
        let pw = 0;
        pw += this.toString().indexOf('.') > 0 ? this.toString().split(".")[1].length : 0;
        pw += arg.toString().indexOf('.') > 0 ? arg.toString().split(".")[1].length : 0;
        return Number(arg.toString().replace(".", "")) * Number(this.toString().replace(".", "")) / Math.pow(10, pw);
    };
    /**
     * 给Number类型增加 除法函数
     * @param arg
     * @returns {number}
     */
    Number.prototype.div = function (arg) {
        let l1 = this.toString().indexOf('.') > 0 ? this.toString().split(".")[1].length : 0,
            l2 = arg.toString().indexOf('.') > 0 ? arg.toString().split(".")[1].length : 0;
        with (Math) {
            r1 = Number(this.toString().replace(".", ""));
            r2 = Number(arg.toString().replace(".", ""));
            return (r1 / r2) * pow(10, l2 - l1);
        }
    }
    /**
     * 给Date类型增加 加法函数
     * @param type
     * @param num
     * @returns {Date}
     */
    Date.prototype.addTime = function (type, num) {
        if (type === "Day") {
            this.setDate(this.getDate() + num);
            return this;
        } else if (type === "Month") {
            this.setMonth(this.getMonth() + num);
            return this;
        } else if (type === "Year") {
            this.setFullYear(this.getFullYear() + num);
            return this;
        }
        return this;
    }

})(jQuery);
