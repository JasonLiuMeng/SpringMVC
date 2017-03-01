<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>蒙华铁路湖北指挥部-就餐信息详单</title>
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

<link rel="stylesheet" type="text/css" href="js/datatable/css/dataTables.bootstrap.css">
<script type="text/javascript" src="js/datatable/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="js/datatable/js/dataTables.bootstrap.js"></script>

<link href="../css/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">

<script type="text/javascript" src="js/datetimepicker/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="js/datetimepicker/js/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<style type="text/css">
	table th, table td{
		text-align : center;
	}

	table.table-condensed > tbody > tr > td{
		display : inline-grid;
		cursor : pointer;
	}
	
	table.table-condensed > tbody > tr > td:hover{
		background : #45d8ff;
	}
</style>
<script>
	$(document).ready(function(){ 
		$('.form_date').datetimepicker({
	        language:  'zh-CN',
	        weekStart: 1,
	        todayBtn:  1,
			autoclose: 1,
			todayHighlight: 1,
			startView: 2,
			minView: 2,
			forceParse: 0
	    });
		
		
		$("#submitBtn").click(function(){
			var date = $("#dateControl").val();
			location.href="table?currentDate=" + date+"&" + Math.random();
		});
		
		$("#export_current").click(function(){
			var date = $("#dateControl").val();
			downloadFile(date);
		});
		$("#export_all").click(function(){
			downloadFile("all");
		});
		
	});
	
	var downloadFile = function(date){
		location.href = "exportFile?date=" + date + "&filename=123.csv&" + Math.random();
	}
	
</script>
</head>
<body>
	
	<div class="container col-lg-10 col-md-12 col-sm-12 col-xs-12 col-lg-offset-1">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h1 class="panel-title text-center" style="font-weight: bold; font-size: 18px;" >蒙华铁路湖北指挥部-就餐人员信息详单</h1>
            </div>
            <div class="panel-body">
            	<div class="col-lg-12 col-md-12 col-sm-12 col-sm-12" style="margin-bottom : 20px;">
            		<div class="col-lg-6 col-md-6 col-sm-6 col-sm-6 text-left">
            			<a href="index" class="btn"><span class="label label-primary">登记就餐信息</span></a>
            		</div> 
            		<div class="col-lg-6 col-md-6 col-sm-6 col-sm-6 text-right">
            			<a href="figure" class="btn"><span class="label label-primary">就餐人数变化曲线图</span></a>
            		</div>
            	</div>
            	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12 ">
            		<div class="form-group col-lg-9 col-md-9 col-sm-9">
	            		<label for="dateControl" class="col-lg-3 col-md-3 col-sm-3 control-label text-right">选择查询日期：</label>
		                <div class="input-group date form_date col-lg-9 col-md-9 col-sm-9" data-date="" data-date-format="yyyy-mm-dd" data-link-field="dateControl" data-link-format="yyyy-mm-dd">
		                    <input class="form-control" size="16" type="text" value="${currentDate}" data-date-format="yyyy-mm-dd" readonly>
		                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
		                </div>
						<input type="hidden" id="dateControl" value="${currentDate}" />
                    </div>  
                    <div class="col-lg-3 col-md-3 col-sm-3">
	                	 <button type="button" id="submitBtn" class="btn btn-default" role="button" >查询</button>
	             	</div>
	             	 <div class="col-lg-12 col-md-12 col-sm-12">
	             	 	<div class="col-lg-5 col-md-5 col-sm-5 text-right">
	             	 		<button type="button" id="export_current" class="btn btn-default" role="button" >导出当前</button>
	             	 	</div>
	             	 	<div class="col-lg-offset-1 col-md-offset-1 col-sm-offset-1 col-lg-5 col-md-5 col-sm-5">
	             	 		<button type="button" id="export_all" class="btn btn-default" role="button" >导出所有</button>
	             	 	</div>
	             	</div>
            	</div>
            	<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12" style="margin-top : 20px;">
				    <table id="infoTable" class="table table-striped table-bordered" cellspacing="0" width="100%">
						<thead>
							<tr>
								<th rowspan="2" >姓名</th>
								<th colspan="2" >是否在食堂吃饭</th>
								<th rowspan="2">日期</th>
							</tr>
					        <tr>
					            <th>上午</th>
					            <th>下午</th>
					        </tr>
						</thead>
						<tbody>
							<c:forEach var="person"  items="${personList}">
								<tr>
						            <td>${person.p_name}</td>
						            <td>
						            	<c:if test="${person.p_dining_m == 0}">
						            		<span style="color : red;">否</span>
						            	</c:if>
						            	<c:if test="${person.p_dining_m == 1}">
						            		<span style="color : blue;">是</span>
						            	</c:if>
						            </td>
						            <td>
						            	<c:if test="${person.p_dining_a == 0}">
						            		<span style="color : red;">否</span>
						            	</c:if>
						            	<c:if test="${person.p_dining_a == 1}">
						            		<span style="color : blue;">是</span>
						            	</c:if>
						            </td>
						            <td>${person.p_time}</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
            </div>
        </div>
    </div>
</body>
</html>