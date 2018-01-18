<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
<div class="app-excel-charts-settings">
	<blockquote class="layui-elem-quote"><i class="layui-icon">&#xe62c;</i> 图表设置</blockquote>
	<form class="layui-form" action="">
	 <fieldset class="layui-elem-field">
	  <legend>图表类型</legend>
	  <div class="layui-field-box">
	      <input type="radio" name="chart_type" lay-filter="sexdro" value="zt" title="柱图" checked>
	      <input type="radio" name="chart_type" lay-filter="sexdro" value="bt" title="饼图">
	      <input type="radio" name="chart_type" lay-filter="sexdro" value="zxt" title="折线图">
	      <input type="radio" name="chart_type" lay-filter="sexdro" value="zkt" title="柱状K线组合图">
	      <input type="radio" name="chart_type" lay-filter="sexdro" value="ybp" title="仪表盘">
	  </div>
	 </fieldset>
	 <fieldset class="layui-elem-field">
	  <legend>基础属性</legend>
	  <div class="layui-field-box">
	   <div class="layui-form">
	      <div class="layui-inline">
	        <label class="layui-form-label">图表名称</label>
	        <div class="layui-input-inline">
	          <input type="text" name="chart_name" required lay-verify="required" placeholder="请输入图表名称" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <div class="layui-inline">
	        <label class="layui-form-label">X轴数据</label>
	        <div class="layui-input-inline">
	        <select name="x_data" required lay-verify="required">
	          <option value="">请选择一个字段</option>
	          <c:forEach items="${xycells }" var="celltitle" varStatus="idx">
		          <option value="<c:out value='${idx.index }' />"><c:out value="${celltitle }" /></option>
	          </c:forEach>
	        </select>
	        </div>
	      </div>
	      <div class="layui-inline">
	      	<label class="layui-form-label">Y轴数据</label>
	      	<div class="layui-input-inline">
	        <select name="y_data" required lay-verify="required">
	          <option value="">请选择一个字段</option>
	          <c:forEach items="${xycells }" var="celltitle" varStatus="idx">
		          <option value="<c:out value='${idx.index }' />"><c:out value="${celltitle }" /> </option>
	          </c:forEach>
	        </select>
	        </div>
	      </div>
	    </div> 
	  </div>
	 </fieldset>
	 <fieldset class="layui-elem-field settings" id="zt"  style="">
	  <legend>属性设置</legend>
	  <div class="layui-field-box">
	  	<div class="layui-form">
	      <div class="layui-inline">
	        <label class="layui-form-label">Y轴标题</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zt_ytitle" placeholder="Y轴标题" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	     <div class="layui-inline">
	        <label class="layui-form-label">Y轴最大刻度</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zt_ymaxval" lay-verify="" placeholder="Y轴最大刻度" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	       <div class="layui-inline">
	        <label class="layui-form-label">Y轴最小刻度</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zt_yminval" lay-verify="" placeholder="Y轴最小刻度" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <hr>
	      <div class="layui-inline">
	        <label class="layui-form-label">Y轴刻度间距</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zt_yspace" lay-verify="" placeholder="Y轴刻度间距" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	       <div class="layui-inline">
	        <label class="layui-form-label">Y轴单位</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zt_yunit" placeholder="Y轴单位" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <div class="layui-inline">
	        <label class="layui-form-label">X轴标题</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zt_xtitle" placeholder="X轴标题" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <hr>
	      <div class="layui-inline">
	        <label class="layui-form-label"><b>开启双Y轴</b></label>
	        <div class="layui-input-inline">
			      <input type="checkbox" name="zt_mutiYswitch" lay-filter="zt_mutiYswitch" lay-skin="switch">
	        </div>
	      </div>
	      <div class="layui-inline zt_mutiYswitch" style="display:none;">
	      	<label class="layui-form-label">第二Y轴数据</label>
	      	<div class="layui-input-inline">
	         <select name="zt_y2_data">
	          <c:forEach items="${xycells }" var="celltitle" varStatus="idx">
		          <option value="<c:out value='${idx.index }' />"><c:out value="${celltitle }" /> </option>
	          </c:forEach>
	        </select>
	        </div>
	      </div>
	    </div>
	  </div>
	 </fieldset>
	 <fieldset class="layui-elem-field settings" id="bt" style="display: none;">
	  <legend>属性设置</legend>
	  <div class="layui-field-box">
	  	<div class="layui-form">
	       <div class="layui-inline">
	        <label class="layui-form-label">Y轴单位</label>
	        <div class="layui-input-inline">
	          <input type="text" name="bt_yunit" placeholder="Y轴单位" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	    </div>
	  </div>
	 </fieldset>
	 <fieldset class="layui-elem-field settings" id="zxt" style="display: none;">
	  <legend>属性设置</legend>
	  <div class="layui-field-box">
	  	<div class="layui-form">
	      <div class="layui-inline">
	        <label class="layui-form-label">Y轴标题</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zxt_ytitle" placeholder="Y轴标题" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	     <div class="layui-inline">
	        <label class="layui-form-label">Y轴最大刻度</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zxt_ymaxval" placeholder="Y轴最大刻度" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	       <div class="layui-inline">
	        <label class="layui-form-label">Y轴最小刻度</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zxt_yminval" placeholder="Y轴最小刻度" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <hr>
	      <div class="layui-inline">
	        <label class="layui-form-label">Y轴刻度间距</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zxt_yspace" placeholder="Y轴刻度间距" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	       <div class="layui-inline">
	        <label class="layui-form-label">Y轴单位</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zxt_yunit" placeholder="Y轴单位" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <div class="layui-inline">
	        <label class="layui-form-label">X轴标题</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zxt_xtitle" placeholder="X轴标题" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	    </div>
	 
	  </div>
	 </fieldset>
	 <fieldset class="layui-elem-field settings" id="zkt"  style="display: none;">
	  <legend>属性设置</legend>
	  <div class="layui-field-box">
	  	<div class="layui-form">
	      <div class="layui-inline">
	        <label class="layui-form-label">Y轴标题</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zkt_ytitle" placeholder="Y轴标题" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	     <div class="layui-inline">
	        <label class="layui-form-label">Y轴最大刻度</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zkt_ymaxval" lay-verify="" placeholder="Y轴最大刻度" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	       <div class="layui-inline">
	        <label class="layui-form-label">Y轴最小刻度</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zkt_yminval" lay-verify="" placeholder="Y轴最小刻度" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <hr>
	      <div class="layui-inline">
	        <label class="layui-form-label">Y轴刻度间距</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zkt_yspace" lay-verify="" placeholder="Y轴刻度间距" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	       <div class="layui-inline">
	        <label class="layui-form-label">Y轴单位</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zkt_yunit" placeholder="Y轴单位" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <div class="layui-inline">
	        <label class="layui-form-label">X轴标题</label>
	        <div class="layui-input-inline">
	          <input type="text" name="zkt_xtitle" placeholder="X轴标题" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <hr>
	      <div class="layui-inline">
	        <label class="layui-form-label"><b>开启K线</b></label>
	        <div class="layui-input-inline">
			      <input type="checkbox" name="zkt_mutiYswitch" lay-filter="zkt_mutiYswitch" lay-skin="switch">
	        </div>
	      </div>
	      <div class="layui-inline zkt_mutiYswitch" style="display:none;">
	      	<label class="layui-form-label">K线轴数据</label>
	      	<div class="layui-input-inline">
	         <select name="zkt_y2_data">
	          <c:forEach items="${xycells }" var="celltitle" varStatus="idx">
		          <option value="<c:out value='${idx.index }' />"><c:out value="${celltitle }" /> </option>
	          </c:forEach>
	        </select>
	        </div>
	      </div>
	    </div>
	  </div>
	 </fieldset>
	 <fieldset class="layui-elem-field settings" id="ybp" style="display: none;">
	  <legend>属性设置</legend>
	  <div class="layui-field-box">
	  	<div class="layui-form">
	     <div class="layui-inline">
	        <label class="layui-form-label">最小刻度</label>
	        <div class="layui-input-inline">
	          <input type="text" name="ybp_minval" placeholder="最小刻度" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	       <div class="layui-inline">
	        <label class="layui-form-label">最大刻度</label>
	        <div class="layui-input-inline">
	          <input type="text" name="ybp_maxval" placeholder="最大刻度" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <div class="layui-inline">
	        <label class="layui-form-label">刻度分割数</label>
	        <div class="layui-input-inline">
	          <input type="text" name="ybp_splitNumber" placeholder="刻度分割数" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	       <hr>
	       
	       <div class="layui-inline">
	        <label class="layui-form-label">X轴/Y轴含义</label>
	        <div class="layui-input-inline">
	          <input type="text" name="ybp_xdividey" placeholder="X轴除以Y轴含义" autocomplete="off" class="layui-input">
	        </div>
	      </div>
	      <div class="layui-inline">
	      	<div class="layui-form-mid layui-word-aux" ><font style="color:#FF5722;">注:仪表盘的数据值是使用X轴  / Y轴得到，且由于仪表盘特殊性，请减少数据源行数。</font></div>
	       </div>
	      </div>
	    </div>
	 </fieldset>
	 
	 
	 <fieldset class="layui-elem-field" id="chart_color">
	  <legend>颜色设置</legend>
	  <div class="layui-field-box">
	  	<div class="layui-form">
	  		 <div class="layui-input-inline">
	           <div style="display:inline-block ; background-color:#009688;width: 24px;height: 24px;">&nbsp;</div>
	           <input type="radio" name="charts_color" lay-filter="ztcolordro" value="#009688" title="墨绿" checked>
	           <div style="display:inline-block ; background-color:#5FB878;width: 24px;height: 24px;">&nbsp;</div>
	     	   <input type="radio" name="charts_color" lay-filter="ztcolordro" value="#5FB878" title="青绿">
	           <div style="display:inline-block ; background-color:#393D49;width: 24px;height: 24px;">&nbsp;</div>
	           <input type="radio" name="charts_color" lay-filter="ztcolordro" value="#393D49" title="雅黑">
	           <div style="display:inline-block ; background-color:#1E9FFF;width: 24px;height: 24px;">&nbsp;</div>
	           <input type="radio" name="charts_color" lay-filter="ztcolordro" value="#1E9FFF" title="蓝色">
	           <div style="display:inline-block ; background-color:#FF5722;width: 24px;height: 24px;">&nbsp;</div>
	           <input type="radio" name="charts_color" lay-filter="ztcolordro" value="#FF5722" title="赤色">
	           <div style="display:inline-block ; background-color:#FFB800;width: 24px;height: 24px;">&nbsp;</div>
	           <input type="radio" name="charts_color" lay-filter="ztcolordro" value="#FFB800" title="橙色">
	           <div style="display:inline-block ; background-color:#c2c2c2;width: 24px;height: 24px;">&nbsp;</div>
	           <input type="radio" name="charts_color" lay-filter="ztcolordro" value="#c2c2c2" title="银灰">
	        </div>
	  	</div>
	   </div>
	 </fieldset>
	 <div class="layui-form-item " style="text-align: center;">
	    <div class="excel-charts-settings">
	  	    <button type="button" class="layui-btn layui-btn-normal" lay-submit lay-filter="review" data-method="review"><i class="layui-icon">&#xe64a;</i>预览</button>
			<button type="button" class="layui-btn layui-btn-danger" lay-submit lay-filter="saved" data-method="saveCharts"><i class="layui-icon">&#xe618;</i>保存</button>
	    </div>
	 </div>
	
	 <fieldset class="layui-elem-field">
	  <legend>图表预览</legend>
	  <div class="layui-field-box">
		  <div id="echartmain" style="width: 800px;height:450px;"></div>	
	  </div>
	 </fieldset>
	</form>	
</div>

<script>
	//注意：导航 依赖 element 模块，否则无法进行功能性操作
	layui.use(['form','element'], function(){
	   var form = layui.form,$ = layui.jquery,element=layui.element;
	   
	   form.render();
	   var filename='<c:out value="${filename}"></c:out>';
	   
	   
	   //监听柱图开启多Y轴开关动态
	   form.on('switch(zt_mutiYswitch)',  function(data){
		   //console.log(data.elem); //得到checkbox原始DOM对象
		   //console.log(data.elem.checked); //开关是否开启，true或者false
		  // console.log(data.value); //开关value值，也可以通过data.elem.value得到
		   //console.log(data.othis); //得到美化后的DOM对象
		   if(data.elem.checked){
			   $("div.layui-inline.zt_mutiYswitch").css('display',"inline-block");
		   }else{
			   $("div.layui-inline.zt_mutiYswitch").css('display',"none");
		   }
		   
	   });
	   
	 //监听柱图开启多Y轴开关动态
	   form.on('switch(zkt_mutiYswitch)',  function(data){
		   //console.log(data.elem); //得到checkbox原始DOM对象
		   //console.log(data.elem.checked); //开关是否开启，true或者false
		  // console.log(data.value); //开关value值，也可以通过data.elem.value得到
		   //console.log(data.othis); //得到美化后的DOM对象
		   if(data.elem.checked){
			   $("div.layui-inline.zkt_mutiYswitch").css('display',"inline-block");
		   }else{
			   $("div.layui-inline.zkt_mutiYswitch").css('display',"none");
		   }
		   
	   });
	   
	   //监听radio元素的动态
	   form.on('radio(sexdro)',  function(data){
		   //console.log(data.elem); //得到radio原始DOM对象
		   //console.log(data.value); //被点击的radio的value值
		   var id=data.value;
		   $("fieldset.layui-elem-field.settings").css('display',"none");
		   $("#"+id).css('display',"block");
		   
	   });
	   
	   //监听提交
	   form.on('submit(review)', function(data){
	       //layer.msg(JSON.stringify(data.field));
	       var params=$.extend({},data.field,{"filename":filename});
		   $.ajax({  
               type:'post',  
               traditional :true,  
               url:'./charts/review',  
               data:params,
               success:function(data){  
	               if(data){
	            	   if(data["chartType"]=="zt"){//如果是柱图
	            		   var y1y2Axis=new Array();
	            		   var yAxis={};
	            	       var seriesData=[];
		            	    if(params.zt_ymaxval && params.zt_yminval && params.zt_yspace){
		            	    	var ymax=parseInt(params.zt_ymaxval);
		            	    	var ymin=parseInt(params.zt_yminval);
		            	    	var yspace=parseInt(params.zt_yspace);
		            	    	yAxis={type:'value',min: ymin, max: ymax,interval:yspace};
		            	    }else{
		            	    	yAxis={type:'value'};
		            	    }
		            	    if(params.zt_ytitle){//添加y轴标题信息.自定义就取自定义值
		            	    	var yname=params.zt_ytitle;
		            	    	yAxis=$.extend({},yAxis,{"name":yname});
		            	    }else{//没有自定义就使用列名
		            	    	yAxis=$.extend({},yAxis,{"name":data["legend"][0]});
		            	    } 
		            	    if(params.zt_yunit){//单位不为空,添加单位信息
		            	    	var axisLabel={axisLabel:{
			                        formatter: function (value, index) {
			                          return value+" "+params.zt_yunit;
			                        }
			                       }
		            	    	}
		            	    	yAxis=$.extend({},yAxis,axisLabel);
		            	    }
		            	    y1y2Axis.push(yAxis);
		            	    //构造多Y数据源
		            	    if(data["y2data"].length>0){
		            	    	seriesData=[{
		    						name : data["legend"][0],
		    						type : 'bar',
		    						data : data["ydata"],
		    						itemStyle : {//设置柱体颜色
		    							normal : {
		    								color : params.charts_color
		    							}
		    						 }
		    					   },
		    					   {
		    							name : data["legend"][1],
		    							type : 'bar',
		    							 yAxisIndex: 1,
		    							data : data["y2data"]
		    						} 
		    					];
		            	    	var y2Axis={type:'value',name:data["legend"][1]};
		            	    	y1y2Axis.push(y2Axis);
		            	    }else{
		            	    	seriesData=[{
		    						name : data["legend"][0],
		    						type : 'bar',
		    						data : data["ydata"],
		    						itemStyle : {//设置柱体颜色
		    							normal : {
		    								color : params.charts_color
		    							}
		    						 }
		    					   }
		    					]
		            	    }
	         			var chart=genCharts(data["chartType"],"echartmain",params.chart_name,data["legend"],data["xdata"],seriesData,params.zt_xtitle,params.charts_color,y1y2Axis,true);
	            	   }else if(data["chartType"]=="bt"){//如果是饼图
	            		   var yAxis={trigger: 'item', formatter: "{a} <br/>{b} : {c} ({d}%)"};
		            	    if(params.bt_yunit){//单位不为空,添加单位信息
		            	    	//鼠标悬浮弹窗提示  
		            	    	var tips={
						             formatter:"{a} <br/>{b} : {c} "+params.bt_yunit+" ({d}%)" 
						        }
		            	    	yAxis=$.extend({},yAxis,tips);
		            	    }
		            	    var seriesData=[ {
								name : params.chart_name,
								type : 'pie',
								center : [ '50%', '50%' ],
								data : data["ydata"],
								label : yAxis,
								itemStyle : {
									normal : {
										borderWidth : 1,
										borderColor : '#FFF'
									},
									emphasis : {
										shadowBlur : 10,
										shadowOffsetX : 0,
										shadowColor : 'rgba(0, 0, 0, 0.5)'
									}
								}
							} ];
	         			 genCharts(data["chartType"],"echartmain",params.chart_name,data["legend"],data["xdata"],seriesData,"",params.charts_color,yAxis,true);
	            	   }else if(data["chartType"]=="zxt"){//如果是折线图
	            		   var yAxis={};
		            	    if(params.zxt_ymaxval && params.zxt_yminval && params.zxt_yspace){
		            	    	var ymax=parseInt(params.zxt_ymaxval);
		            	    	var ymin=parseInt(params.zxt_yminval);
		            	    	var yspace=parseInt(params.zxt_yspace);
		            	    	yAxis={type:'value',min: ymin, max: ymax,interval:yspace};
		            	    }else{
		            	    	yAxis={type:'value'};
		            	    }
		            	    if(params.zxt_ytitle){//添加y轴标题信息
		            	    	var yname=params.zxt_ytitle;
		            	    	yAxis=$.extend({},yAxis,{"name":yname});
		            	    } 
		            	    if(params.zxt_yunit){//单位不为空,添加单位信息
		            	    	var axisLabel={axisLabel:{
			                        formatter: function (value, index) {
			                          return value+" "+params.zxt_yunit;
			                        }
			                       }
		            	    	}
		            	    	yAxis=$.extend({},yAxis,axisLabel);
		            	    }
		            	    var ydata=data["ydata"];
		            	    var lineStyle={
		                        normal:{
		                            color: params.charts_color //连线颜色
		                        }
		                    };
		            	    var itemStyle={
		                        normal:{
		                        	color: params.charts_color  //图标颜色
		                        }
		                    },
		            	    ydata=$.extend({},ydata,{"lineStyle":lineStyle});
		            	    ydata=$.extend({},ydata,{"itemStyle":itemStyle});
	         			 genCharts(data["chartType"],"echartmain",params.chart_name,data["legend"],data["xdata"],ydata,params.zxt_xtitle,params.charts_color,yAxis,true);
	            	   }else if(data["chartType"]=="zkt"){//如果是柱K图
	            		   
	            		   var y1y2Axis=new Array();
	            		   var yAxis={};
	            	       var seriesData=[];
		            	    if(params.zkt_ymaxval && params.zkt_yminval && params.zkt_yspace){
		            	    	var ymax=parseInt(params.zkt_ymaxval);
		            	    	var ymin=parseInt(params.zkt_yminval);
		            	    	var yspace=parseInt(params.zkt_yspace);
		            	    	yAxis={type:'value',min: ymin, max: ymax,interval:yspace};
		            	    }else{
		            	    	yAxis={type:'value'};
		            	    }
		            	    if(params.zkt_ytitle){//添加y轴标题信息.自定义就取自定义值
		            	    	var yname=params.zkt_ytitle;
		            	    	yAxis=$.extend({},yAxis,{"name":yname});
		            	    }else{//没有自定义就使用列名
		            	    	yAxis=$.extend({},yAxis,{"name":data["legend"][0]});
		            	    } 
		            	    if(params.zkt_yunit){//单位不为空,添加单位信息
		            	    	var axisLabel={axisLabel:{
			                        formatter: function (value, index) {
			                          return value+" "+params.zkt_yunit;
			                        }
			                       }
		            	    	}
		            	    	yAxis=$.extend({},yAxis,axisLabel);
		            	    }
		            	    y1y2Axis.push(yAxis);
		            	    //构造多Y数据源
		            	    if(data["y2data"].length>0){
		            	    	seriesData=[{
		    						name : data["legend"][0],
		    						type : 'bar',
		    						data : data["ydata"],
		    						itemStyle : {//设置柱体颜色
		    							normal : {
		    								color : params.charts_color
		    							}
		    						 }
		    					   },
		    					   {
		    							name : data["legend"][1],
		    							type : 'line',
		    							 yAxisIndex: 1,
		    							data : data["y2data"]
		    						} 
		    					];
		            	    	var y2Axis={type:'value',name:data["legend"][1]};
		            	    	y1y2Axis.push(y2Axis);
		            	    }else{
		            	    	seriesData=[{
		    						name : data["legend"][0],
		    						type : 'bar',
		    						data : data["ydata"],
		    						itemStyle : {//设置柱体颜色
		    							normal : {
		    								color : params.charts_color
		    							}
		    						 }
		    					   }
		    					]
		            	    }
	         			 genCharts(data["chartType"],"echartmain",params.chart_name,data["legend"],data["xdata"],seriesData,params.zt_xtitle,params.charts_color,y1y2Axis,true);
	            	   }else if(data["chartType"]=="ybp"){//如果是仪表盘
	            		   var seriesData=[];
	            	       var serval={};
	            		   if(params.ybp_minval && params.ybp_maxval && params.ybp_splitNumber){
		            	    	var ymax=parseInt(params.ybp_maxval);
		            	    	var ymin=parseInt(params.ybp_minval);
		            	    	var yspace=parseInt(params.ybp_splitNumber);
		            	    	serval={ name: params.ybp_xdividey,
		            	                type: 'gauge',
		            	                detail: {formatter:'{value}%'},min: ymin, max: ymax,splitNumber:yspace,
		            	                data:data["data"]};
		            	    }else{
		            	    	serval={ name: params.ybp_xdividey,
		            	                type: 'gauge',
		            	                detail: {formatter:'{value}%'},data:data["data"]};
		            	    }
	            		   seriesData.push(serval);
	            		   genCharts(data["chartType"],"echartmain",params.chart_name,data["legend"],data["data"],seriesData,params.zxt_xtitle,params.charts_color,yAxis,true);
	            	   }
	               } 	
               }  
	       });   
	     return false;
	   });
	   
	   //监听提交
	   form.on('submit(saved)', function(data){
		 //layer.msg(JSON.stringify(data.field));
	       var params=$.extend({},data.field,{"filename":filename});
		   $.ajax({  
               type:'post',  
               traditional :true,  
               url:'./charts/review',  
               data:params,
               success:function(data){  
               	//console.info(data);
	               if(data){
	            	   if(data["chartType"]=="zt"){//如果是柱图
	            		   var y1y2Axis=new Array();
	            		   var yAxis={};
	            	       var seriesData=[];
		            	    if(params.zt_ymaxval && params.zt_yminval && params.zt_yspace){
		            	    	var ymax=parseInt(params.zt_ymaxval);
		            	    	var ymin=parseInt(params.zt_yminval);
		            	    	var yspace=parseInt(params.zt_yspace);
		            	    	yAxis={type:'value',min: ymin, max: ymax,interval:yspace};
		            	    }else{
		            	    	yAxis={type:'value'};
		            	    }
		            	    if(params.zt_ytitle){//添加y轴标题信息.自定义就取自定义值
		            	    	var yname=params.zt_ytitle;
		            	    	yAxis=$.extend({},yAxis,{"name":yname});
		            	    }else{//没有自定义就使用列名
		            	    	yAxis=$.extend({},yAxis,{"name":data["legend"][0]});
		            	    } 
		            	    if(params.zt_yunit){//单位不为空,添加单位信息
		            	    	var axisLabel={axisLabel:{
			                        formatter: function (value, index) {
			                          return value+" "+params.zt_yunit;
			                        }
			                       }
		            	    	}
		            	    	yAxis=$.extend({},yAxis,axisLabel);
		            	    }
		            	    y1y2Axis.push(yAxis);
		            	    //构造多Y数据源
		            	    if(data["y2data"].length>0){
		            	    	seriesData=[{
		    						name : data["legend"][0],
		    						type : 'bar',
		    						data : data["ydata"],
		    						itemStyle : {//设置柱体颜色
		    							normal : {
		    								color : params.charts_color
		    							}
		    						 }
		    					   },
		    					   {
		    							name : data["legend"][1],
		    							type : 'bar',
		    							 yAxisIndex: 1,
		    							data : data["y2data"]
		    						} 
		    					];
		            	    	var y2Axis={type:'value',name:data["legend"][1]};
		            	    	y1y2Axis.push(y2Axis);
		            	    }else{
		            	    	seriesData=[{
		    						name : data["legend"][0],
		    						type : 'bar',
		    						data : data["ydata"],
		    						itemStyle : {//设置柱体颜色
		    							normal : {
		    								color : params.charts_color
		    							}
		    						 }
		    					   }
		    					]
		            	    }
	         			 var chart=genCharts(data["chartType"],"echartmain",params.chart_name,data["legend"],data["xdata"],seriesData,params.zt_xtitle,params.charts_color,y1y2Axis,false);
	         			 //导出图片信息 
	         			  var picInfo = chart.getDataURL({
	         				 type:"png",// 导出的格式，可选 png, jpeg
	         				 pixelRatio: 2,// 导出的图片分辨率比例，默认为 1。
	         			     backgroundColor: '#fff', //导出的图片背景色，默认使用 option 里的 backgroundColor
	         			     excludeComponents:['toolbox'] //忽略组件的列表，例如要忽略 toolbox 就是 ['toolbox']
	         			 }); 
	         			 
	         			 var opt=chart.getOption();
	         			 //console.info(JSON.stringify(opt));
	         			 
						 $.ajax({
								type : "post",
								data : {
									baseimg : picInfo,
									option:JSON.stringify(opt)
								},
								url : './charts/genChartPic',
								async : true,
								success : function(data) {
									if(data){
										layer.msg(data["message"]);
									}
								},
								error : function(err) {
									layer.msg('图片保存失败');
								}
							});
						} else if (data["chartType"] == "bt") {//如果是饼图
							var yAxis = {
								trigger : 'item',
								formatter : "{a} <br/>{b} : {c} ({d}%)"
							};
							if (params.bt_yunit) {//单位不为空,添加单位信息
								//鼠标悬浮弹窗提示  
								var tips = {
									formatter : "{a} <br/>{b} : {c} "
											+ params.bt_yunit + " ({d}%)"
								}
								yAxis = $.extend({}, yAxis, tips);
							}
							var seriesData=[ {
								name : params.chart_name,
								type : 'pie',
								center : [ '50%', '50%' ],
								data : data["ydata"],
								label : yAxis,
								itemStyle : {
									normal : {
										borderWidth : 1,
										borderColor : '#FFF'
									},
									emphasis : {
										shadowBlur : 10,
										shadowOffsetX : 0,
										shadowColor : 'rgba(0, 0, 0, 0.5)'
									}
								}
							} ];
							var chart=genCharts(data["chartType"], "echartmain",
									params.chart_name, data["legend"],
									data["xdata"], seriesData, "",
									params.charts_color, yAxis,false);
							
							//导出图片信息 
		         			 var picInfo = chart.getDataURL({
		         				 type:"png",// 导出的格式，可选 png, jpeg
		         				 pixelRatio: 2,// 导出的图片分辨率比例，默认为 1。
		         			     backgroundColor: '#fff', //导出的图片背景色，默认使用 option 里的 backgroundColor
		         			     excludeComponents:['toolbox'] //忽略组件的列表，例如要忽略 toolbox 就是 ['toolbox']
		         			 });
		         			 var opt=chart.getOption();
							 $.ajax({
									type : "post",
									data : {
										baseimg : picInfo,
										option:JSON.stringify(opt)
									},
									url : './charts/genChartPic',
									async : true,
									success : function(data) {
										if(data){
											layer.msg(data["message"]);
										}
									},
									error : function(err) {
										layer.msg('图片保存失败');
									}
								});
							
						} else if (data["chartType"] == "zxt") {//如果是折线图
							var yAxis = {};
							if (params.zxt_ymaxval && params.zxt_yminval
									&& params.zxt_yspace) {
								var ymax = parseInt(params.zxt_ymaxval);
								var ymin = parseInt(params.zxt_yminval);
								var yspace = parseInt(params.zxt_yspace);
								yAxis = {
									type : 'value',
									min : ymin,
									max : ymax,
									interval : yspace
								};
							} else {
								yAxis = {
									type : 'value'
								};
							}
							if (params.zxt_ytitle) {//添加y轴标题信息
								var yname = params.zxt_ytitle;
								yAxis = $.extend({}, yAxis, {
									"name" : yname
								});
							}
							if (params.zxt_yunit) {//单位不为空,添加单位信息
								var axisLabel = {
									axisLabel : {
										formatter : function(value, index) {
											return value + " "
													+ params.zxt_yunit;
										}
									}
								}
								yAxis = $.extend({}, yAxis, axisLabel);
							}
							var ydata = data["ydata"];
							var lineStyle = {
								normal : {
									color : params.charts_color
								//连线颜色
								}
							};
							var itemStyle = {
								normal : {
									color : params.charts_color
								//图标颜色
								}
							}, ydata = $.extend({}, ydata, {
								"lineStyle" : lineStyle
							});
							ydata = $.extend({}, ydata, {
								"itemStyle" : itemStyle
							});
							var chart=genCharts(data["chartType"], "echartmain",
									params.chart_name, data["legend"],
									data["xdata"], ydata, params.zxt_xtitle,
									params.charts_color, yAxis,false);
							
							//导出图片信息 
		         			 var picInfo = chart.getDataURL({
		         				 type:"png",// 导出的格式，可选 png, jpeg
		         				 pixelRatio: 2,// 导出的图片分辨率比例，默认为 1。
		         			     backgroundColor: '#fff', //导出的图片背景色，默认使用 option 里的 backgroundColor
		         			     excludeComponents:['toolbox'] //忽略组件的列表，例如要忽略 toolbox 就是 ['toolbox']
		         			 });
		         			 var opt=chart.getOption();
							 $.ajax({
									type : "post",
									data : {
										baseimg : picInfo,
										option:JSON.stringify(opt)
									},
									url : './charts/genChartPic',
									async : true,
									success : function(data) {
										if(data){
											layer.msg(data["message"]);
										}
									},
									error : function(err) {
										layer.msg('图片保存失败');
									}
								});
						}else if(data["chartType"]=="zkt"){//如果是柱K图
		            		   
		            		   var y1y2Axis=new Array();
		            		   var yAxis={};
		            	       var seriesData=[];
			            	    if(params.zkt_ymaxval && params.zkt_yminval && params.zkt_yspace){
			            	    	var ymax=parseInt(params.zkt_ymaxval);
			            	    	var ymin=parseInt(params.zkt_yminval);
			            	    	var yspace=parseInt(params.zkt_yspace);
			            	    	yAxis={type:'value',min: ymin, max: ymax,interval:yspace};
			            	    }else{
			            	    	yAxis={type:'value'};
			            	    }
			            	    if(params.zkt_ytitle){//添加y轴标题信息.自定义就取自定义值
			            	    	var yname=params.zkt_ytitle;
			            	    	yAxis=$.extend({},yAxis,{"name":yname});
			            	    }else{//没有自定义就使用列名
			            	    	yAxis=$.extend({},yAxis,{"name":data["legend"][0]});
			            	    } 
			            	    if(params.zkt_yunit){//单位不为空,添加单位信息
			            	    	var axisLabel={axisLabel:{
				                        formatter: function (value, index) {
				                          return value+" "+params.zkt_yunit;
				                        }
				                       }
			            	    	}
			            	    	yAxis=$.extend({},yAxis,axisLabel);
			            	    }
			            	    y1y2Axis.push(yAxis);
			            	    //构造多Y数据源
			            	    if(data["y2data"].length>0){
			            	    	seriesData=[{
			    						name : data["legend"][0],
			    						type : 'bar',
			    						data : data["ydata"],
			    						itemStyle : {//设置柱体颜色
			    							normal : {
			    								color : params.charts_color
			    							}
			    						 }
			    					   },
			    					   {
			    							name : data["legend"][1],
			    							type : 'line',
			    							 yAxisIndex: 1,
			    							data : data["y2data"]
			    						} 
			    					];
			            	    	var y2Axis={type:'value',name:data["legend"][1]};
			            	    	y1y2Axis.push(y2Axis);
			            	    }else{
			            	    	seriesData=[{
			    						name : data["legend"][0],
			    						type : 'bar',
			    						data : data["ydata"],
			    						itemStyle : {//设置柱体颜色
			    							normal : {
			    								color : params.charts_color
			    							}
			    						 }
			    					   }
			    					]
			            	    }
		         			 var chart=genCharts(data["chartType"],"echartmain",params.chart_name,data["legend"],data["xdata"],seriesData,params.zt_xtitle,params.charts_color,y1y2Axis,false);
		            		 
		         			//导出图片信息 
		         			 var picInfo = chart.getDataURL({
		         				 type:"png",// 导出的格式，可选 png, jpeg
		         				 pixelRatio: 2,// 导出的图片分辨率比例，默认为 1。
		         			     backgroundColor: '#fff', //导出的图片背景色，默认使用 option 里的 backgroundColor
		         			     excludeComponents:['toolbox'] //忽略组件的列表，例如要忽略 toolbox 就是 ['toolbox']
		         			 });
		         			 var opt=chart.getOption();
							 $.ajax({
									type : "post",
									data : {
										baseimg : picInfo,
										option:JSON.stringify(opt)
									},
									url : './charts/genChartPic',
									async : true,
									success : function(data) {
										if(data){
											layer.msg(data["message"]);
										}
									},
									error : function(err) {
										layer.msg('图片保存失败');
									}
								});
		            		   
		            	   }else if(data["chartType"]=="ybp"){//如果是仪表盘
		            		   var seriesData=[];
		            	       var serval={};
		            		   if(params.ybp_minval && params.ybp_maxval && params.ybp_splitNumber){
			            	    	var ymax=parseInt(params.ybp_maxval);
			            	    	var ymin=parseInt(params.ybp_minval);
			            	    	var yspace=parseInt(params.ybp_splitNumber);
			            	    	serval={ name: params.ybp_xdividey,
			            	                type: 'gauge',
			            	                detail: {formatter:'{value}%'},min: ymin, max: ymax,splitNumber:yspace,
			            	                data:data["data"]};
			            	    }else{
			            	    	serval={ name: params.ybp_xdividey,
			            	                type: 'gauge',
			            	                detail: {formatter:'{value}%'},data:data["data"]};
			            	    }
		            		   seriesData.push(serval);
		            		   var chart=genCharts(data["chartType"],"echartmain",params.chart_name,data["legend"],data["data"],seriesData,params.zxt_xtitle,params.charts_color,yAxis,false);
		            		 //导出图片信息 
			         			 var picInfo = chart.getDataURL({
			         				 type:"png",// 导出的格式，可选 png, jpeg
			         				 pixelRatio: 2,// 导出的图片分辨率比例，默认为 1。
			         			     backgroundColor: '#fff', //导出的图片背景色，默认使用 option 里的 backgroundColor
			         			     excludeComponents:['toolbox'] //忽略组件的列表，例如要忽略 toolbox 就是 ['toolbox']
			         			 });
			         			 var opt=chart.getOption();
								 $.ajax({
										type : "post",
										data : {
											baseimg : picInfo,
											option:JSON.stringify(opt)
										},
										url : './charts/genChartPic',
										async : true,
										success : function(data) {
											if(data){
												layer.msg(data["message"]);
											}
										},
										error : function(err) {
											layer.msg('图片保存失败');
										}
									});
		            	   }
					}
				}
			});
			return false;
		});

		//自定义生成图表方法\
		/**
		 * domId  生成图表的DOM id
		 * chartName 生成图表的名称
		 * legendArray 图例数组
		 * xdata x轴数据
		 * ydata y轴数据
		 * xname x轴名称
		 * charts_color 轴体颜色
		 * yAxis yAxis json对象
		 */
		var genCharts = function(chartsType, domId, chartName, legendArray,
				xdata, seriesData,xname, charts_color, yAxis,animation) {

			var mainContainer = document.getElementById(domId);
			echarts.dispose(mainContainer);
			var myChart = echarts.init(mainContainer);
			var option = {};
			//如果是柱图
			if (chartsType == "zt") {
				// 指定图表的配置项和数据
				option = {
					title : {
						text : chartName
					},
					animation:animation,
					tooltip : {},
					legend : {
						data : legendArray
					},
					xAxis : {
						name : xname,
						data : xdata,
						axisLabel : {
							interval : 0,
							rotate : 90
						}
					},
					yAxis : yAxis,
					series:seriesData,
					toolbox : {//负责将图表保存为图片
						show : true,
						orient : 'vertical',//工具栏 icon 的布局朝向
						right : '60',
						feature : {
							saveAsImage : {
								show : true,
								excludeComponents : [ 'toolbox' ],
								pixelRatio : 2
							}
						}
					}
				};
			} else if (chartsType == "bt") {//如果是饼图
				option = {
					title : {
						text : chartName,
						subtext : '',
						x : 'center'
					},
					animation:animation,
					tooltip : yAxis,
					legend : {
						orient : 'vertical',
						left : 'left',
						data : legendArray
					},
					series : seriesData,
					toolbox : {//负责将图表保存为图片
						show : true,
						orient : 'vertical',//工具栏 icon 的布局朝向
						right : '60',
						feature : {
							saveAsImage : {
								show : true,
								excludeComponents : [ 'toolbox' ],
								pixelRatio : 2
							}
						}
					}
				};
			} else if (chartsType == "zxt") {
				option = {
					title : {
						text : chartName
					},
					animation:animation,
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : legendArray
					},
					grid : {
						left : '3%',
						right : '4%',
						bottom : '3%',
						containLabel : true
					},
					toolbox : {
						show : true,
						orient : 'vertical',//工具栏 icon 的布局朝向
						right : '60',
						feature : {
							saveAsImage : {
								show : true,
								excludeComponents : [ 'toolbox' ],
								pixelRatio : 2
							}
						}
					},
					xAxis : {
						type : 'category',
						name : xname,
						data : xdata,
						axisLabel : {
							interval : 0,
							rotate : 25
						}
					},
					yAxis : yAxis,
					series : seriesData
				};
			}else if (chartsType == "zkt") {//柱K图
				// 指定图表的配置项和数据
				option = {
					title : {
						text : chartName
					},
					animation:animation,
					tooltip: {
					        trigger: 'axis',
					        axisPointer: {
					            type: 'cross',
					            crossStyle: {
					                color: '#999'
					            }
					        }
					    },
					legend : {
						data : legendArray
					},
					xAxis : {
						name : xname,
						data : xdata,
						type: 'category',
						axisLabel : {
							interval : 0,
							rotate : 90
						}
					},
					yAxis : yAxis,
					series:seriesData,
					toolbox : {//负责将图表保存为图片
						show : true,
						orient : 'vertical',//工具栏 icon 的布局朝向
						right : '60',
						feature : {
							saveAsImage : {
								show : true,
								excludeComponents : [ 'toolbox' ],
								pixelRatio : 2
							}
						}
					}
				};
			}else if (chartsType == "ybp") {//仪表盘
				option = {
				  tooltip : {
				        formatter: "{a} <br/>{b} : {c}%"
				    },
				    toolbox: {
				        feature: {
				            restore: {},
				            saveAsImage: {}
				        }
				    },
				    series: seriesData
				};
			}

			// 使用刚指定的配置项和数据显示图表。
			myChart.setOption(option);
			return myChart;
		}

	});
</script>