
function initTimeLineOption(data) {
    var legendData = [];
    var timeLineData = [];
    var optionsData = [];
    var timeDataMap = {};
    var timeXValueMap = {};
    for (var key1 in data) {
        var timeData = key1.substring(0, 7);
        var chartCode = key1.substring(7);
        var seriesData = [];
        var xValueData = [];
        if (chartCode == 'SMS_WAREHOUSE_QTY_CHART') {
            timeLineData.push(timeData);
            for (var key2 in data[key1]) {
                var lineType = key2;
                if (legendData.indexOf(lineType) < 0) {
                    legendData.push(lineType);
                }
                var pointData = [];
                for (var i = 0; i < data[key1][key2].length; i++) {
                    var xValue = data[key1][key2][i].xvalue;
                    var yValue = data[key1][key2][i].yvalue;
                    pointData[i] = {
                        name : xValue,
                        value : yValue
                    };
                    if (xValueData.indexOf(xValue) < 0) {
                        xValueData.push(xValue);
                    }
                }
                var data1 = {
                    name : lineType,
                    type : 'line',
                    data : pointData
                };
                seriesData.push(data1);
            }
            timeXValueMap[timeData] = xValueData;
        } else {
            var piePointData1 = [];     // 内圆
            var piePointData2 = [];     // 外圆
            for (var key2 in data[key1]) {
                var lineType = key2;
                var pieValue = 0;
                for (var i = 0; i < data[key1][key2].length; i++) {
                    var xValue = data[key1][key2][i].xvalue;
                    var yValue = data[key1][key2][i].yvalue;
                    pieValue = pieValue + Number(yValue);
                    var pieData2 = {
                        name : lineType + xValue,
                        value : yValue
                    }
                    piePointData2.push(pieData2);
                }
                var pieData1 = {
                    name : lineType,
                    value : pieValue
                };
                piePointData1.push(pieData1);
            }
            var data1 = {
                name : '各仓操作数占比',
                type: 'pie',
                selectedMode: 'single',
                center: ['75%', '35%'],
                radius: [0, '30%'],
                label: {
                    normal: {
                        position: 'inside',
                        textStyle: {
                            fontSize: 10
                        }
                    }
                },
                labelLine: {
                    show: false
                },
                z: 100,
                data: piePointData1
            };
            var data2 = {
                name: '各仓操作数占比',
                type: 'pie',
                selectedMode: 'single',
                center: ['75%', '35%'],
                radius: ['25%', '35%'],
                label: {
                    normal: {
                        show: false
                    }
                },
                labelLine: {
                    show: false
                },
                z: 100,
                data: piePointData2
            };
            // seriesData.push(data1);
            // seriesData.push(data2);
        }
        if (timeDataMap.hasOwnProperty(timeData)) {
            var value = timeDataMap[timeData];
            timeDataMap[timeData] = value.concat(seriesData);
        } else {
            timeDataMap[timeData] = seriesData;
        }
    }
    for (var key in timeDataMap) {
        var optionData = {
            xAxis : [{
                'data' : timeXValueMap[key]
            }],
            title : {text : key + '各仓货量趋势图'},
            series : timeDataMap[key]
        }
        optionsData.push(optionData);
    }

    var option = {
        baseOption: {
            timeline: {
                axisType: 'category',
                // realtime: false,
                // loop: false,
                autoPlay: true,
                // currentIndex: 2,
                playInterval: 5000,
                // controlStyle: {
                //     position: 'left'
                // },
                data: timeLineData
            },
            title: {
                subtext: '数据统计自ASN订单'
            },
            tooltip: {
            },
            legend: {
                left: 'right',
                data: legendData
            },
            calculable : true,
            grid: {
                top: 80,
                bottom: 100,
                tooltip: {
                    trigger: 'axis',
                    axisPointer: {
                        type: 'shadow',
                        label: {
                            show: true,
                            formatter: function (params) {
                                return params.value.replace('\n', '');
                            }
                        }
                    }
                }
            },
            xAxis: [
                {
                    'type':'category',
                    'axisLabel':{'interval':0},
                    'data':[
                        '1号','2号','3号','4号','5号','6号','7号','8号','9号','10号',
                        '11号','12号','13号','14号','15号','16号','17号','18号','19号','20号',
                        '21号','22号','23号','24号','25号','26号','27号','28号','29号','30号',
                        '31号'
                    ],
                    splitLine: {show: false},
                    axisLabel: {
                        'interval' : 'auto',
                        'rotate' : 60
                    },
                }
            ],
            yAxis: [
                {
                    type: 'value',
                    name: '箱数'
                }
            ]
        },
        options: optionsData
    }
    return option;
}