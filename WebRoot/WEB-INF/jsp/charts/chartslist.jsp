<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
<div class="layui-form">
    <div class="layui-form-item">
	    <label class="layui-form-label">报表名称</label>
	    <div class="layui-input-inline">
	      <input type="text" name="charts_menu_name" required  lay-verify="required" placeholder="报表菜单名称" autocomplete="off" class="layui-input">
	    </div>
	    <button class="layui-btn layui-btn-normal" lay-submit lay-filter="formCharts"><i class="layui-icon">&#xe62c;</i>生成报表</button>
		<button type="button" class="layui-btn layui-btn-primary" data-method="chartsDelete"><i class="layui-icon">&#xe640;</i>删除</button>
   </div> 
  <div class="layui-tab layui-tab-brief">
  <ul class="layui-tab-title">
    <li class="layui-this">图表列表<span class="layui-badge">共:<c:out value='${imageSize }' />张</span></li>
  </ul>
  <div class="layui-tab-content">
  		<div class="site-demo-flow" id="LAY_demo3">
	  <div id="layer-photos-demo" class="layer-photos-demo">
	    <div class="layui-fluid">
		  <c:forEach items="${imageArray }" var="pic" varStatus="picIndex">
		    <c:if test="${picIndex.count % 3==1 }">
		       <div class="layui-row">
			</c:if> 
		    <div class="layui-col-md4">
		         <div style="">
			         <input type="checkbox" name="chart[show]" data-url="<c:out value='${pic.url }' />" data-filename="<c:out value='${pic.filename }' />"  data-option="<c:out value='${pic.option }' />"  lay-filter="chart[show]" title="<c:out value='${picIndex.count }' /> ">
		        	 <c:if test="${!empty pic.date}">
		        		 <span class="layui-badge-rim"><c:out value='${pic.date }' /></span>
		        	 </c:if>
		         </div>
				 <img lay-src="<c:out value='${HTTP_VIEW_WORKPATH }' /><c:out value='${pic.url }' />" data-method="show" title="<c:out value='${pic.filename }' />">
		    </div>
		    <c:if test="${picIndex.count % 3==0 }">
		      </div>
		      <hr>
		    </c:if>
		    <c:if test="${picIndex.last}">
		    	 </div>
		    </c:if>
		  </c:forEach>
		  </div>
		</div>
	   </div>
    </div>
  </div>
 </div>
<script>
	//注意：导航 依赖 element 模块，否则无法进行功能性操作
	layui.use(['flow','form'], function(){
	   var form = layui.form,$ = layui.jquery;
	   form.render();
	   var flow = layui.flow;
	   //根据当前窗口大小设置图片浏览区域的高度
	   $("#LAY_demo3").css("height",$(document).height()*0.55);
	   
	   //当你执行这样一个方法时，即对页面中的全部带有lay-src的img元素开启了懒加载（当然你也可以指定相关img）
	   //按屏加载图片
	   flow.lazyimg({
	     elem: '#LAY_demo3 img'
	     ,scrollElem: '#LAY_demo3' //一般不用设置，此处只是演示需要。
	   });
	   //监听提交
	   form.on('submit(formCharts)', function(data){
	     var checkeds=$("input:checked");
	     if(checkeds && checkeds.length < 1){
	    	 layer.msg("请至少选择一张图表",function(){});
	    	 return;
	     }
	     var selectedPicArray=new Array();
	     for(var i=0;i<checkeds.length;i++){
	     	//console.info($(checkeds[i]).data("url"));
	     	//console.info($(checkeds[i]).attr("title"));
	     	 var selectedPic=$.extend({},{"picOption":$(checkeds[i]).data("option"),"picUrl":$(checkeds[i]).data("url"),"index":$(checkeds[i]).attr("title")});
		     selectedPicArray.push(selectedPic);
	     }
	     //console.info(selectedPicArray);
	     var jsonMessage={"chartsdata":JSON.stringify(selectedPicArray),"charts_menu_name":data.field["charts_menu_name"]};
	     $.ajax({  
             type:'post',  
             traditional :true,  
             url:'./charts/genCharts',
             data:jsonMessage,  
             success:function(data){  
             	layer.msg(data["message"]);
             	setTimeout(function(){
                	window.location.reload();
            	},1000);
             }  
         });  
	     return false;
	   });
	   //按钮事件集合
	  var excute_events={
			  show:function(obj){
				  //console.info(obj);
				  //console.info(obj[0]);
				  //layer.msg("点击了图片");
				//调用示例
				  layer.photos({
				    photos: '#layer-photos-demo'
				    ,anim: 5 //0-6的选择，指定弹出图片动画类型，默认随机（请注意，3.0之前的版本用shift参数）
				  }); 
			  },
			  chartsDelete:function(obj){
				  var checkeds=$("input:checked");
				     if(checkeds && checkeds.length < 1){
				    	 layer.msg("请至少选择一张图表",function(){});
				    	 return;
				  }
			     var selectedPicArray=new Array();
			     for(var i=0;i<checkeds.length;i++){
			     	 //console.info($(checkeds[i]).data("filename"));
			     	 var selectedPic=$(checkeds[i]).data("filename");
				     selectedPicArray.push(selectedPic);
			     } 
			     var jsonMessage={"delPics":selectedPicArray};
			     $.ajax({  
		             type:'post',  
		             traditional :true,  
		             url:'./charts/delCharts',
		             data:jsonMessage,  
		             success:function(data){  
		             	layer.msg(data["message"]);
		             	setTimeout(function(){
		             		menuclick("charts/chartslist");
		            	},1000);
		             }  
		         });  
			     return false;
			  }
	   };
	   $(".site-demo-flow img").on('click',function(){
			  var othis=$(this),method=othis.data("method");
			  excute_events[method]?excute_events[method].call(this,othis):'';
	   });
	   $(".layui-form-item button.layui-btn-primary").on('click',function(){
		      var othis=$(this),method=othis.data("method");
			  excute_events[method]?excute_events[method].call(this,othis):'';
	   });
	});
</script>