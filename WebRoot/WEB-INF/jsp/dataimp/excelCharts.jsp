<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
<div class="app-excel-charts">
<blockquote class="layui-elem-quote"><i class="layui-icon">&#xe62a;</i> 数据统计分析</blockquote>
    <c:if test="${currentUser.role=='admin' || currentUser.role=='charts_gen'}">
	<button class="layui-btn  layui-btn-sm" data-method="settings"><i class="layui-icon">&#xe620;</i>图表设置</button>
	<button class="layui-btn  layui-btn-sm layui-btn-normal" data-method="del"> <i class="layui-icon">&#xe640;</i>删除</button>
	</c:if>
	 <hr class="layui-bg-green">
	     <!-- 数据表格 -->
	<div class="layui-form">
	  <div class="site-data-flow">
	   <div id="page"></div>
		  <table class="layui-table">
		  </table>
	  </div>
	</div>
</div>
<script>
	//注意：导航 依赖 element 模块，否则无法进行功能性操作
	layui.use(['form','laypage'], function(){
	   var form = layui.form,$ = layui.jquery;
	   var laypage = layui.laypage;
	   var formData={}; //用来定义表单提交JSO你对象
	   var res=<c:out value="${data}" escapeXml="false"></c:out>;
	   var filename='<c:out value="${filename}"></c:out>';
	   var pageSize='<c:out value="${pageSize}"></c:out>';
	   $(function(){
		   if(res!="" && res!=null){
			   var tTable=$("table.layui-table");
				 tTable.html("");
				 var titleList=res["tableTitle"];
				 var thead=$('<thead></thead>');
				 var theadTr=$('<tr></tr>');
				 //动态生成title
				 for(var obj in titleList){
					 var tbtitle=titleList[obj];
					 theadTr.append("<th style='min-width:80px;'>"+tbtitle+"</th>");
				 }
				 thead.append(theadTr);
				 tTable.append(thead);
				 var tbody=$('<tbody class="dynamic-data"></tbody>');
				 var dataList=res["tableBody"];
				 //Excel一共有多少列
				 var cols=res["cells"];
				 //需要分页
				 if(dataList.length>pageSize){
					//执行一个laypage实例
					   laypage.render({
					     elem: 'page' //注意，这里的 test1 是 ID，不用加 # 号
					     ,count: dataList.length //数据总数，从服务端得到
					     ,limit:pageSize
					     ,jump: function(obj, first){
					    	    var start=(obj.curr-1)*obj.limit;
					    	    var end=obj.curr*obj.limit;
					    	    tbody.html("");
					    	    for(var obj=start; obj<end;obj++){
					    	    	if(dataList.length>obj){
					    	    		var trRow=dataList[obj];
							        	 var tr=$('<tr></tr>');
							        	 for(var i=0;i<cols;i++){
							        		 var cellVal=trRow[i];
							        		 if(typeof(cellVal) == "undefined"){
								        		 tr.append("<td></td>");
							        		 }else{
								        		 tr.append("<td>"+cellVal+"</td>");
							        		 }
							        	 }
							        	 tbody.append(tr);
					    	    	}
						         }
					      }
					   });
				 }else{
					 for(var obj in dataList){
				      	 var trRow=dataList[obj];
				      	 var len=trRow.length;
				      	 var tr=$('<tr></tr>');
				      	 for(var i=0;i<cols;i++){
				      		 var cellVal=trRow[i];
				      		if(typeof(cellVal) == "undefined"){
				        		 tr.append("<td></td>");
			        		 }else{
				        		 tr.append("<td>"+cellVal+"</td>");
			        		 }
			      	 	 }
			      	 tbody.append(tr);
			        }
				 }
		         
		        tTable.append(tbody);
		   }
	   });
	   form.render();
	   
	  //按钮事件集合
	  var excute_events={
			  del:function(){
			      layer.confirm('确认删除当前结点吗？', {icon: 5, title:'操作提示'}, function(index){
			    	   $.ajax({  
			                type:'post',  
			                traditional :true,  
			                url:'./dataimp/menudelete',
			                data:{"filename":filename},  
			                success:function(data){  
			                	layer.msg(data["message"]);
			                	setTimeout(function(){
				                	window.location.reload();
			                	},1000);
			                }  
			            }); 
			      });
			     
			  },
			  settings:function(){
				  menuclick("charts/show?filename="+filename); 
			  }
	   };
	   
	   $(".app-excel-charts .layui-btn.layui-btn-sm").on('click',function(){
			  var othis=$(this),method=othis.data("method");
			  excute_events[method]?excute_events[method].call(this,othis):'';
	   });
	   
	});
</script>