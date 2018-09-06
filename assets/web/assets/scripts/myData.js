
//柱状图
function setBardata(id,name)
{
	 // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById(id));

        // 指定图表的配置项和数据
        var option = {
			
			color: ['#3398DB'],
			
            title: {
                text: ''
            },
            tooltip: {},
            legend: {
                data:[name]
            },
            xAxis: {
                data: ["电池1","电池2","电池3","电池4","电池5","电池6","电池7","电池8"]
            },
            yAxis: {},
            series: [{
                name: name,
                type: 'bar',
                data: [5, 20, 36, 10, 10, 20,5,20]
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
}

//折线图
function setLinedata(id)
{
		  // 基于准备好的dom，初始化echarts实例
        var myChart2 = echarts.init(document.getElementById(id));

        // 指定图表的配置项和数据
       var option2 = {
    xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    },
	
    yAxis: {
        type: 'value'
    },
	
	 legend: {
                data:['溫度','湿度'],				
            },
	
    series: [{
		
		
		color: '#3398DB',
		name: '溫度',
        data: [20, 10, 60, 30, 100,80,50],	
        type: 'line',
		
    },{
		
		color: '#91C7AE',
		name: '湿度',
		data: [10, 10, 30, 40, 110,50,80],
        type: 'line',
		
    }]
};
        // 使用刚指定的配置项和数据显示图表。
        myChart2.setOption(option2);
}

//饼图
function setPiedata(id)
{
			  // 基于准备好的dom，初始化echarts实例
        var myChart3 = echarts.init(document.getElementById(id));

        // 指定图表的配置项和数据
       var option3 = {
    title : {
        text: '',
        subtext: '',
        x:'center'
    },
    tooltip : {
        trigger: 'item',
        formatter: "{a} <br/>{b} : {c} ({d}%)"
    },
    legend: {
        orient: 'vertical',
        left: 'left',
        data: ['IT能耗','其它能耗']
    },
    series : [
        {
            name: '访问来源',
            type: 'pie',
            radius : '55%',
            center: ['50%', '60%'],
            data:[
                {
					value:335,
				    name:'IT能耗',
				    itemStyle: {
                       normal: {     
                         color: '#00C8C9',
                       }
                    }
				},
                {
					value:310, 
					name:'其它能耗',
					itemStyle: {
                       normal: {              
                         color: '#00A7C8',
                       }
                    }
				}
            ],
            itemStyle: {
                emphasis: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                }
            }
        }
    ]
};
        // 使用刚指定的配置项和数据显示图表。
        myChart3.setOption(option3);
}


//仪表盘
function setGrdata(id,name,min,max,value)
{
		  // 基于准备好的dom，初始化echarts实例
       var myChart4 = echarts.init(document.getElementById(id));

        // 指定图表的配置项和数据
       var 		option4 = {
       tooltip : {
           formatter: "{a} <br/>{b} : {c}%"
       },
       series: [
        {
			min:min,
            max:max,
            name: 'PUE',
            type: 'gauge',
            detail: {formatter:'{value}'},
			
		    axisLine: {            // 坐标轴线
                lineStyle: {       // 属性lineStyle控制线条样式
                    color: [[0.09, 'lime'],[0.82, '#1e90ff'],[1, '#ff4500']],
                    width: 10,
                    shadowColor : '#fff', //默认透明
                    shadowBlur: 10
                }
            },
			
			 splitLine: {           // 分隔线
                length :15,         // 属性length控制线长
                lineStyle: {       // 属性lineStyle（详见lineStyle）控制线条样式
                    width:3,
                    color: '#fff',
                    shadowColor : '#fff', //默认透明
                    shadowBlur: 10
                }
            },
            
			
			
            data: [{value: value, name: name}]
        }
    ]
};

        // 使用刚指定的配置项和数据显示图表。
        myChart4.setOption(option4);
		

}

//圆环
function setCriData(id,name,value)
{
		  // 基于准备好的dom，初始化echarts实例
    var myChart= echarts.init(document.getElementById(id));

	
	var option = {
    title: {
        text: '80%',
        x: 'center',
        y: 'center',
        textStyle: {
            fontWeight: 'normal',
            color: '#0580f2',
            fontSize: '25'
        }
    },
    color: ['rgba(176, 212, 251, 1)'], 
    legend: {
        show: true,
        itemGap: 12,
        data: [name, '02']
    },
   
    series: [{
        name: 'Line 1',
        type: 'pie',
        clockWise: true,
        radius: ['50%', '66%'],
        itemStyle: {
            normal: {
                label: {
                    show: false
                },
                labelLine: {
                    show: false
                }
            }
        },
        hoverAnimation: false, 
        data: [{
            value: value,
            name: name,
            itemStyle: {
                normal: {
                    color: { // 完成的圆环的颜色
                        colorStops: [{
                            offset: 0,
                            color: '#00cefc' // 0% 处的颜色
                        }, {
                            offset: 1,
                            color: '#367bec' // 100% 处的颜色
                        }]
                    },
                    label: {
                        show: false
                    },
                    labelLine: {
                        show: false
                    }
                } 
            }
        }, {
            
            value: 100-value
        }]
    }]
    };
	
	 myChart.setOption(option);
}

//温度计
function setWenduJi(id,values)
{
		  // 基于准备好的dom，初始化echarts实例
    var myChart= echarts.init(document.getElementById(id));

	
	var value =values;
var kd = [];
// 刻度使用柱状图模拟，短设置3，长的设置5；构造一个数据
for (var i = 0, len = 130; i <= len; i++) {
    if (i > 100 || i < 30) {
        kd.push('0')
    } else {
        if (i % 5 === 0) {
            kd.push('5');
        } else {
            kd.push('3');
        }
    }

}
console.log(kd)

var data = getData(value);
var mercuryColor = '#fd4d49';
var borderColor = '#fd4d49';

option = {
    title: {
        text: '温度计',
        show: false
    },
    yAxis: [{
        show: false,
        min: 0,
        max: 130,
    }, {
        show: false,
        data: [],
        min: 0,
        max: 130,
    }],
    xAxis: [{
        show: false,
        data: []
    }, {
        show: false,
        data: []
    }, {
        show: false,
        data: []
    }, {
        show: false,
        min: -110,
        max: 100,

    }],
    series: [{
        name: '条',
        type: 'bar',
        // 对应上面XAxis的第一个对象配置
        xAxisIndex: 0,
        data: data,
        barWidth: 13,
        itemStyle: {
            normal: {
                color: mercuryColor,
                barBorderRadius: 0,
            }
        },
        label: {
            normal: {
                show: true,
                position: 'top',
                formatter: function(param) {
                    // 因为柱状初始化为0，温度存在负值，所以，原本的0-100，改为0-130，0-30用于表示负值
                    return param.value - 30 + '°C';
                },
                textStyle: {
                    color: '#ccc',
                    fontSize: '10',
                }
            }
        },
        z: 2
    }, {
        name: '白框',
        type: 'bar',
        xAxisIndex: 1,
        barGap: '-100%',
        data: [129],
        barWidth: 20,
        itemStyle: {
            normal: {
                color: '#ffffff',
                barBorderRadius: 50,
            }
        },
        z: 1
    }, {
        name: '外框',
        type: 'bar',
        xAxisIndex: 2,
        barGap: '-100%',
        data: [130],
        barWidth: 30,
        itemStyle: {
            normal: {
                color: borderColor,
                barBorderRadius: 50,
            }
        },
        z: 0
    }, {
        name: '圆',
        type: 'scatter',
        hoverAnimation: false,
        data: [0],
        xAxisIndex: 0,
        symbolSize: 48,
        itemStyle: {
            normal: {
                color: mercuryColor,
                opacity: 1,
            }
        },
        z: 2
    }, {
        name: '白圆',
        type: 'scatter',
        hoverAnimation: false,
        data: [0],
        xAxisIndex: 1,
        symbolSize: 60,
        itemStyle: {
            normal: {
                color: '#ffffff',
                opacity: 1,
            }
        },
        z: 1
    }, {
        name: '外圆',
        type: 'scatter',
        hoverAnimation: false,
        data: [0],
        xAxisIndex: 2,
        symbolSize: 70,
        itemStyle: {
            normal: {
                color: borderColor,
                opacity: 1,
            }
        },
        z: 0
    }, {
        name: '刻度',
        type: 'bar',
        yAxisIndex: 1,
        xAxisIndex: 3,
        label: {
            normal: {
                show: true,
                position: 'right',
                distance: 5,
                color: '#525252',
                fontSize: 10,
                formatter: function(params) {
                    // 因为柱状初始化为0，温度存在负值，所以，原本的0-100，改为0-130，0-30用于表示负值
                    if (params.dataIndex > 100 || params.dataIndex < 30) {
                        return '';
                    } else {
                        if (params.dataIndex % 5 === 0) {
                            return params.dataIndex - 30;
                        } else {
                            return '';
                        }
                    }
                }
            }
        },
        barGap: '-100%',
        data: kd,
        barWidth: 1,
        itemStyle: {
            normal: {
                color: borderColor,
                barBorderRadius: 10,
            }
        },
        z: 0
    }]
};
	
	 myChart.setOption(option);
}

// 因为柱状初始化为0，温度存在负值，所以，原本的0-100，改为0-130，0-30用于表示负值
function getData(value) {
    return [value + 30];
}




function setStackLineData(id)
{
		  // 基于准备好的dom，初始化echarts实例
    var myChart= echarts.init(document.getElementById(id));

    var option = {
    title: {
        text: '',
        left: '50%',
        textAlign: 'center'
    },
    tooltip: {
        trigger: 'axis',
        axisPointer: {
            lineStyle: {
                color: '#ddd'
            }
        },
        backgroundColor: 'rgba(255,255,255,1)',
        padding: [5, 10],
        textStyle: {
            color: '#7588E4',
        },
        extraCssText: 'box-shadow: 0 0 5px rgba(0,0,0,0.3)'
    },
    legend: {
        right: 20,
        orient: 'vertical',
        data: ['温度','湿度']
    },
    xAxis: {
        type: 'category',
        data: ['00:00','2:00','4:00','6:00','8:00','10:00','12:00','14:00','16:00','18:00','20:00',"22:00"],
        boundaryGap: false,
        splitLine: {
            show: false,
            interval: 'auto',
            lineStyle: {
                color: ['#D4DFF5']
            }
        },
        axisTick: {
            show: false
        },
        axisLine: {
            lineStyle: {
                color: '#609ee9'
            }
        },
        axisLabel: {
            margin: 10,
            textStyle: {
                fontSize: 14
            }
        }
    },
    yAxis: {
        type: 'value',
        splitLine: {
            show: false,
            lineStyle: {
                color: ['#D4DFF5']
            }
        },
        axisTick: {
            show: false
        },
        axisLine: {
            lineStyle: {
                
                color: '#609ee9'
            }
        },
        axisLabel: {
            margin: 10,
            textStyle: {
                fontSize: 14
            }
        }
    },
    series: [{
        name: '出风温度',
        type: 'line',
        smooth: true,
        showSymbol: false,
        symbol: 'circle',
        symbolSize: 6,
        data: ['30', '20', '25', '27', '21', '34', '33', '32', '36', '37', '38', '45'],
        areaStyle: {
            normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: 'rgba(199, 237, 250,0.5)'
                }, {
                    offset: 1,
                    color: 'rgba(199, 237, 250,0.2)'
                }], false)
            }
        },
        itemStyle: {
            normal: {
                color: '#f7b851'
            }
        },
        lineStyle: {
            normal: {
                width: 1
            }
        }
    }, {
        name: '进风温度',
        type: 'line',
        smooth: true,
        showSymbol: false,
        symbol: 'circle',
        symbolSize: 6,
        data: ['50', '60', '45', '35', '70', '56', '65', '74', '67', '80', '69', '90'],
        areaStyle: {
            normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                    offset: 0,
                    color: 'rgba(216, 244, 247,1)'
                }, {
                    offset: 1,
                    color: 'rgba(216, 244, 247,1)'
                }], false)
            }
        },
        itemStyle: {
            normal: {
                color: '#58c8da'
            }
        },
        lineStyle: {
            normal: {
                width: 1
            }
        }
    }]
};
	
	 myChart.setOption(option);
}