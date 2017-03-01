<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蒙华铁路湖北指挥部就餐信息曲线图</title>
<!--[if lt IE 9]>
<script type="text/javascript" src="js/compatible/html5shiv.js"></script>
<script type="text/javascript" src="js/compatible/respond.js"></script>
<![endif]-->
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/jquery/jquery.js"></script>
<link rel="stylesheet" href="js/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/echarts/echarts.min.js"></script>
<style type="text/css">
	#echartsDiv{
		height : 500px;
	}
</style>

<script type="text/javascript">
	$(document).ready(function(){ 
		getDateFormServer();
        $(window).resize(function(event){
          	if( eChartsObj != null && eChartsObj != undefined ){
          		 eChartsObj.resize();
          	}  	
        })	
	});
	
	var eChartsObj = null;
	var initEChatsFigure = function(_data){
	    var count = [],morning = [],afternoon = [],allDay = [],allDayNot = [];
        $.each(_data.columns, function(index, item){
            count.push( _data.count[item] || 0  );
            morning.push( _data.morning[item] || 0   );
            afternoon.push( _data.afternoon[item] || 0   );
            allDay.push( _data.allDay[item] || 0  );
            allDayNot.push( _data.allDayNot[item] || 0  );
        });
       
        var serDatas = [];
        serDatas.push({
        	datas : count,
        	name : "count",
        	label : "每天登记人数曲线"
        });
        serDatas.push({
        	datas : morning,
        	name : "morning",
        	label : "仅上午在食堂吃饭人数曲线"
        });
        serDatas.push({
        	datas : afternoon,
        	name : "afternoon",
        	label : "仅下午在食堂吃饭人数曲线"
        });
        serDatas.push({
        	datas : allDay,
        	name : "allDay",
        	label : "全天都在食堂吃饭人数曲线"
        });
        serDatas.push({
        	datas : allDayNot,
        	name : "allDayNot",
        	label : "全天都不在食堂吃饭人数曲线"
        });
        var legends = [];
		$.each(serDatas, function(index, item){
			legends.push(item.label);
		})
		var _option = {
			    tooltip:{
			        trigger:"axis"
			    },
			    title:{
			        left:"center",
			        text:"蒙华铁路湖北指挥部-就餐信息曲线图",
			        textStyle:{
			            fontSize:18
			        }
			    },
			    xAxis:{
			        boundaryGap:false,
			        type:"category",
			        data: _data.columns
			    },
			    yAxis:{
			        type:"value"
			    },
			    grid:{
			        height:"75%",
			        y:"17%",
			        show:true,
			        containLabel:true
			    },
			    legend:{
			        show:true,
			        top:"6%",
			        itemWidth:25,
			        itemHeight:14,
			        data:legends
			    },
			    dataZoom:[
			        {
			            show:true,
			            realtime:true,
			            height:30,
			            bottom:2,
			            startValue:0,
			            endValue:10
			        },
			        {
			            type:"inside",
			            xAxisIndex:[
			                0
			            ],
			            start:1,
			            end:100
			        }
			    ],
			    series:[
			        {
			            name:"vData0",
			            type:"line",
			            smooth:true,
			            symbol:"none",
			            sampling:"average",
			            data:[

			            ],
			            markLine:{
			                animation:false,
			                lineStyle:{
			                    normal:{
			                        type:"solid",
			                        width:2
			                    }
			                },
			                symbol:"circle",
			                symbolSize:2,
			                tooltip:{

			                },
			                data:[
			                    
			                   
			                ]
			            },
			            markPoint:{
			                animation:false,
			                symbolSize:15,
			                data:[
			                    {
			                        type:"min",
			                        label:{
			                            normal:{
			                                show:false
			                            }
			                        }
			                    },
			                    {
			                        type:"max",
			                        label:{
			                            normal:{
			                                show:false
			                            }
			                        }
			                    }
			                ]
			            }
			        }
			    ]
			} 
		_option.series = createSeriesInfo(serDatas);
		eChartsObj = echarts.init(document.getElementById("echartsDiv"));
		eChartsObj.setOption(_option);
	}
	
	var createSeriesInfo = function(serDatas){
        var series = [];

        var markLineOpt = {
            animation: false,
            lineStyle: {
                normal: {
                    type: 'solid'
                }
            },
            symbol : "circle",
            symbolSize : 2,
            lineStyle : {
                normal : {
                    type : "solid",
                    width : 2
                }
            },
            data: [
                {
                	type: 'average',	
                }
            ]
        };

        var markPointOpt = {
            animation: false,
            symbolSize : 15,
            data: [
                {
                    type : "min",
                    label : {
                        normal : {
                            show : false
                        }
                    }
                },
                {
                    type : "max",
                    label : {
                        normal : {
                            show : false
                        }
                    }
                }
            ]
        };

        $.each(serDatas, function(index, item){
            var obj = {};
            obj.name = item.label;
            obj.type = 'line';
            obj.smooth = true;
            obj.symbol = 'none';
            obj.sampling = 'average';
            obj.data = item.datas;
            obj.markLine = markLineOpt;
            obj.markPoint = markPointOpt;
            series.push(obj);
        });
        return series;
    };
	
	var getDateFormServer = function(){
		 $.ajax({
             url  : "figureData?"+Math.random(),
             data : null,
             type : "post",
             async: false,
             dataType : 'json',
             success : function(data){
             	if( data.result == 200 ){
             		initEChatsFigure(data);
             	}else{
             		 window.wxc.xcConfirm("获取数据失败！", window.wxc.xcConfirm.typeEnum.error, {
                          okBtnText : "确定"
                      });
             	}  
             },
             error : function(error){
                 window.wxc.xcConfirm("获取数据失败！", window.wxc.xcConfirm.typeEnum.error, {
                     okBtnText : "确定"
                 });
             }
         });
	}
	
</script>
	
</head>
<body>
	<div class="container col-lg-10 col-md-12 col-sm-12 col-xs-12 col-lg-offset-1">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h1 class="panel-title text-center" style="font-weight: bold; font-size: 18px;" >就餐信息曲线图</h1>
            </div>
            <div class="panel-body">
	            <div class="col-lg-12 col-md-12 col-sm-12 col-sm-12" style="margin-bottom : 20px;">
            		<div class="col-lg-6 col-md-6 col-sm-6 col-sm-6 text-left">
            			<a href="index" class="btn"><span class="label label-primary">登记就餐信息</span></a>
            		</div> 
            		<div class="col-lg-6 col-md-6 col-sm-6 col-sm-6 text-right">
            			<a href="table" class="btn"><span class="label label-primary">查看就餐信息表</span></a>
            		</div>
            	</div>
            	<div id="echartsDiv" class="col-lg-12 col-md-12 col-sm-12 col-sm-12"></div>
            </div>
        </div>
    </div>
</body>
</html>