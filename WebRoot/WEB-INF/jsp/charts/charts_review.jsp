<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
<blockquote class="layui-elem-quote"><i class="layui-icon">&#xe857;</i> 组合图表展示&nbsp;&nbsp;&nbsp;&nbsp;
<button id="formchange" class="layui-btn layui-btn-sm" data-method="change"><i class="layui-icon">&#xe629;</i>转换为静态图表</button>
<button id="vertical" class="layui-btn layui-btn-sm layui-btn-primary " data-method="vertical"><i class="layui-icon">&#xe671;</i>垂直排列</button>
<button id="align" class="layui-btn layui-btn-sm layui-btn-primary layui-btn-disabled" data-method="align"><i class="layui-icon">&#xe65f;</i>平铺排列</button>
<button id="delete" class="layui-btn layui-btn-sm layui-btn-primary" data-method="deleteThis"><i class="layui-icon">&#xe640;</i>删除</button>
</blockquote>
<hr>
<div id="pictbarea" class="layer-photos-demo" style="max-width: 980px;display:none;overflow: auto;">
  <c:forEach items="${picDatas }" var="pic">
     <img style="width: 480px;" alt="" src='<c:out value='${HTTP_VIEW_WORKPATH }' /><c:out value="${pic.picUrl }" />' data-method="show">
  </c:forEach>
</div>

<div id="dytbarea" class="mydyarea" style="clear:both;">
<c:forEach items="${picDatas }" var="pic">
  <div style="width: 480px;height:320px;display: inline;float:left;" id="<c:out value='${pic.picUrl }' />"></div>
</c:forEach>
</div>

<script>
	layui.use(['flow'], function(){
	   var $ = layui.jquery;
	   <c:forEach items="${picDatas }" var="pic">
	      var id="<c:out value='${pic.picUrl }' ></c:out>";
	      var opt='<c:out value="${pic.picOption }" escapeXml="false" ></c:out>';
	      //console.info(id);
	      //console.info( $.parseJSON(opt));
	      var mainContainer = document.getElementById(id);
	      echarts.dispose(mainContainer);
	      var myChart = echarts.init(mainContainer);
	      var options =$.parseJSON(opt);
	      // 使用刚指定的配置项和数据显示图表。
	      myChart.setOption(options);
	     //chart_image_1511943608633.jpg
	   </c:forEach>
	   
	   //按钮事件集合
		  var excute_events={
				  show:function(obj){
					//调用示例
					  layer.photos({
					    photos: '#pictbarea'
					    ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
					  }); 
				  },
				  change:function(obj){
					    if($("#dytbarea").css("display")=="none"){
							$("#pictbarea").css("display","none");
							$("#dytbarea").css("display","");
							$("#formchange").html("<i class=\"layui-icon\">&#xe62c;</i>转换为静态图表");
							$("#formchange").removeClass("layui-btn-danger");
					    }else{
					    	$("#pictbarea").css("display","");
							$("#dytbarea").css("display","none");
							$("#formchange").html("<i class=\"layui-icon\">&#xe629;</i>转换为动态图表");
							$("#formchange").addClass("layui-btn-danger");
					    }
				  },
				  vertical:function(obj){
					   $(".layer-photos-demo img").css("width","680px").css("border","1px solid  #ccc").css("margin-top","1px");
					   $(".mydyarea div").css("width","960px").css("height","400px").css("margin-top","1px");
					   <c:forEach items="${picDatas }" var="pic">
					      var id="<c:out value='${pic.picUrl }' ></c:out>";
					      var mainContainer = document.getElementById(id);
					   	  echarts.getInstanceByDom(mainContainer).resize({width:'auto',height:'auto'});
					   </c:forEach>
					   $("#vertical").addClass("layui-btn-disabled"); 
					   $("#align").removeClass("layui-btn-disabled"); 
				  },
				  align:function(obj){
					  $(".layer-photos-demo img").css("width","480px").css("border","0px solid  #ccc").css("margin-top","0px");
					  $(".mydyarea div").css("width","480px").css("height","320px").css("margin-top","0px");
					  <c:forEach items="${picDatas }" var="pic">
				      	var id="<c:out value='${pic.picUrl }' ></c:out>";
				      	var mainContainer = document.getElementById(id);
				   	 	echarts.getInstanceByDom(mainContainer).resize({width:'auto',height:'auto'});
				  	  </c:forEach>
					  $("#vertical").removeClass("layui-btn-disabled"); 
					  $("#align").addClass("layui-btn-disabled"); 
				  },
				  deleteThis:function(obj){
					//配置一个透明的询问框
					   layer.confirm('是否删除当前图表展示页面？', {icon: 5, title:'操作提示'}, function(index){
						  var filename="<c:out value='${filename }' ></c:out>";
						     $.ajax({  
				                type:'post',  
				                traditional :true,  
				                url:'charts/delChartsShowPage',  
				                data:{filename:filename},  
				                success:function(data){  
				                	layer.msg(data["message"]);
				                	setTimeout(function(){
					                	window.location.reload();
				                	},1000);
				                }  
				            }); 
						   layer.close(index);
					   });
				  }
		   };
		   
		   $(".layer-photos-demo img").on('click',function(){
				  var othis=$(this),method=othis.data("method");
				  excute_events[method]?excute_events[method].call(this,othis):'';
		   });
		   $("button.layui-btn.layui-btn-sm").on('click',function(){
				  var othis=$(this),method=othis.data("method");
				  excute_events[method]?excute_events[method].call(this,othis):'';
		   });
		   
	});
</script>