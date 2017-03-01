<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蒙华铁路湖北指挥部-就餐信息登记</title>
<!--[if lt IE 9]>
<script type="text/javascript" src="js/compatible/html5shiv.js"></script>
<script type="text/javascript" src="js/compatible/respond.js"></script>
<![endif]-->
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/jquery/jquery.js"></script>
<link rel="stylesheet" href="js/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="js/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet" type="text/css" href="js/alert/css/xcConfirm.css">
<script type="text/javascript" src="js/alert/js/xcConfirm.js"></script>
<style>
	span.time{
		font-size: 18px; color : blue;
	}
	span.flag{
		font-size: 18px; color : red;
	}
</style>
 <script type="text/javascript">
		$(function(){
			var submitDiningInfo = function(obj){
                $.ajax({
                    url  : "submit?"+Math.random(),
                    data : obj,
                    type : "post",
                    async: false,
                    dataType : 'json',
                    success : function(data){
                    	if( data.result == 200 ){
                    		window.wxc.xcConfirm("提交成功！", window.wxc.xcConfirm.typeEnum.success, {
                                okBtnText : "确定",
                                onOk : function(){
                                    location.href = "table";
                                }
                            });
                    	}else if( data.result == 504 ){
                    		 window.wxc.xcConfirm("提交失败！当前时段无法提交！<br/>有效提交时间为： " + data.valid_time , window.wxc.xcConfirm.typeEnum.error, {
                                 okBtnText : "确定"
                             });
                    	}  
                    },
                    error : function(error){
                        window.wxc.xcConfirm("提交失败！出现未知异常！", window.wxc.xcConfirm.typeEnum.error, {
                            okBtnText : "确定"
                        });
                    }
                });
            };

            $("#mySubmitBtn").click(function(){
                var name = $("#p_name").val();
                var text = "";
                if(name == null || name == ""){
                    text = "姓名不能为空";
                    window.wxc.xcConfirm(text, window.wxc.xcConfirm.typeEnum.error, {
                        okBtnText : "确定"
                    });
                }else{
                    var obj = {};
                    var text = "您正在提交 <font style='font-size: 18px'>"+name+" </font>的就餐信息：";
                    var morning = $("input[type='radio'][name='dining_m']:checked").val();
                    var afternoon = $("input[type='radio'][name='dining_a']:checked").val();
                    obj.name = name;
                    obj.morning = morning;
                    obj.afternoon = afternoon;
                    var diningTimeText = "";
                    if( morning == afternoon ){
                    	diningTimeText = "<br/><span class='time'> 全天 : </span>";
                    	diningTimeText += getTooltipText(morning);
                    }else{
                    	if( morning != undefined ){
                        	diningTimeText += "<br/><span class='time'> 上午 : </span>";
                        	diningTimeText += getTooltipText(morning);
                        }
                    	if( afternoon != undefined ){
                        	diningTimeText += "<br/><span class='time'> 下午 : </span>";
                        	diningTimeText += getTooltipText(afternoon);
                        }
                    }
                   
                    text += (diningTimeText + "<br/>确认提交?");
                    window.wxc.xcConfirm(text, window.wxc.xcConfirm.typeEnum.warning, {
                        okBtnText : "确认",
                        cancelBtnText : "取消",
                        onOk:submitDiningInfo.bind(this, obj )
                    });
                }
            });
		});
        var getTooltipText = function(diningFlag){
        	 var diningText = (diningFlag == "0" ? "<span class='flag'>不在食堂吃饭</span>" : "<span class='flag'>在食堂吃饭</span>");
        	 return diningText;
        }
        
    </script>
</head>

<body>
    <div class="container col-lg-10 col-md-12 col-sm-12 col-xs-12 col-lg-offset-1">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h1 class="panel-title text-center" style="font-weight: bold; font-size: 18px;" >蒙华铁路湖北指挥部-登记就餐信息</h1>
            </div>
            <div class="panel-body">
            	<div class="col-lg-12 col-md-12 col-sm-12 col-sm-12" style="margin-bottom : 20px;">
            		<div class="col-lg-6 col-md-6 col-sm-6 col-sm-6 text-left">
            			<a href="table" class="btn"><span class="label label-primary">查看就餐信息表</span></a>
            		</div> 
            		<div class="col-lg-6 col-md-6 col-sm-6 col-sm-6 text-right">
            			<a href="figure" class="btn"><span class="label label-primary">就餐人数变化曲线图</span></a>
            		</div>
            	</div>
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label for="p_name" class="col-lg-4 col-md-4 col-sm-4 col-sm-4 control-label text-right">姓名</label>
                        <div class="col-lg-8 col-md-8 col-sm-8 col-sm-8">
                            <input type="text" class="form-control" id="p_name">
                        </div>
                    </div>
                    <hr />
                    <div class="form-group">
                        <label class="col-lg-5 col-md-5 col-sm-5 col-sm-5 control-label">是否在食堂吃饭</label>
                    </div>
                    <c:if test="${currentHour <= 12 }">
	                    <div class="form-group" id="morining_div">
	                        <label for="dining_m" class="col-lg-4 col-md-4 col-sm-4 col-sm-4 control-label text-right">上午</label>
	                        <div id="diningFlagDiv" class="col-lg-8 col-md-8 col-sm-8 col-sm-8 radio">
	                            <label class="radio-inline">
	                                <input type="radio" name="dining_m" id="optionsRadios1" value="1" checked>是
	                            </label>
	                            <label class="radio-inline">
	                                <input type="radio" name="dining_m" id="optionsRadios2" value="0">否
	                            </label>
	                        </div>
	                    </div>
                    </c:if>
                    <div class="form-group" id="afternoon_div">
                        <label for="dining_a" class="col-lg-4 col-md-4 col-sm-4 col-sm-4 control-label text-right">下午</label>
                        <div id="diningFlagDiv" class="col-lg-8 col-md-8 col-sm-8 col-sm-8 radio">
                            <label class="radio-inline">
                                <input type="radio" name="dining_a" id="optionsRadios3" value="1" checked>是
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="dining_a" id="optionsRadios4" value="0">否
                            </label>
                        </div>
                    </div>
                     <hr />
                    <div class="col-lg-offset-4 col-md-offset-4 col-sm-offset-4 col-sm-offset-4">
                        <input type="button" id="mySubmitBtn" class="btn btn-default" value="提交">
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>