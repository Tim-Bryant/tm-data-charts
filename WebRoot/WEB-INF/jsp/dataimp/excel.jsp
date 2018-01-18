<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
<div>
<blockquote class="layui-elem-quote"><i class="layui-icon">&#xe622;</i> Excel文件导入</blockquote>
<form class="layui-form" action="">
  
  <div class="layui-form-item layui-form-text">
    <label class="layui-form-label">文件</label>
    <div class="layui-input-inline">
		<button type="button" class="layui-btn layui-btn-danger" id="excelFile">
		  <i class="layui-icon">&#xe67c;</i>请选择 Excel 文件
		</button>
    </div>
     <div class="layui-form-mid layui-word-aux">管理员设置的最大允许导入行数为：<span class="layui-badge layui-bg-orange"><c:out value='${params.allowmaxrows}' /></span>，如有问题请联系管理员更改！</div>
  </div>
  <!--   <div class="layui-form-item">
    <label class="layui-form-label">数据库表名</label>
    <div class="layui-input-block">
      <input type="text" name="dstablename" required  lay-verify="required" placeholder="请输入数据库表名" autocomplete="off" class="layui-input">
    </div>
  </div> -->
  <div class="layui-form-item">
    <label class="layui-form-label">数据菜单名</label>
    <div class="layui-input-inline">
      <input type="text" name="menunodename" required  lay-verify="required" placeholder="请输入数据菜单名" autocomplete="off" class="layui-input">
    </div>
  </div>
   <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit lay-filter="formDemo">保存</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
  </div>
  <hr class="layui-bg-green">
  	 <!-- 数据表格 -->
	<div class="layui-form">
	  <div class="site-data-flow">
    <div id="page"></div>
	  <table class="layui-table">
	  </table>
	  </div>
   </div>
</form>
</div>
<script>
	//注意：导航 依赖 element 模块，否则无法进行功能性操作
	layui.use(['form','layedit','upload','laypage'], function(){
	   var form = layui.form,$ = layui.jquery;
	   var formData={}; //用来定义表单提交JSO你对象
	   var upload = layui.upload;
	   var laypage = layui.laypage;
	   var fileopt={
		   elem:"#excelFile",
		   url:"./dataimp/parserExcel",
		   accept:"file",
		   exts:"xlsx|xls",
		   auto:true,
		   size:10240,//最大支持10M
		   field:"assofile",
		   before: function(obj){
				 //加载层-风格4
			    layer.msg('文件正在上传解析中...', {
				   icon: 16
				   ,shade: 0.3
				   , time: 6000*10
			   }); 
			},
			done : function(res,index,upload) {
				//console.info(res);
				//layer.closeAll(); //关闭loading
				//console.info(res["tableTitle"]);
				 formData=$.extend({},res);
				 var tTable=$("table.layui-table");
				 tTable.html("");
				 if(res["success"]){
					 setTimeout(function(){
						  layer.closeAll();
						  layer.msg("文件上传成功");				
		                },2000);
					 var exceldatajson=$.parseJSON(res["excelData"]);
					 var pageSize=res["pageSize"]==null?10:res["pageSize"];
					 //console.info(exceldatajson);
					 var titleList=exceldatajson["tableTitle"];
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
					 var dataList=exceldatajson["tableBody"];
					 //Excel一共有多少列
					 var cols=exceldatajson["cells"];
					 //需要分页
					 if(dataList.length>pageSize){
						//执行一个laypage实例
						   laypage.render({
						     elem: 'page' //注意，这里的 test1 是 ID，不用加 # 号
						     ,count: dataList.length //数据总数，从服务端得到
						     ,limit:pageSize
						     ,jump: function(obj, first){
						    	    //obj包含了当前分页的所有参数，比如：
						    	    //console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
						    	    //console.log(obj.limit); //得到每页显示的条数
						    	    var start=(obj.curr-1)*obj.limit;
						    	    var end=obj.curr*obj.limit;
						    	    //console.log(start); //得到每页显示的条数
						    	    //console.log(end); //得到每页显示的条数
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
					 }else{//不需要分页
						 for(var obj in dataList){
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
						//删除信息到不需要分页时 清除页面的分页组件
						 $("#page").html("");
					 }
			         tTable.append(tbody);
				 }else{
					 layer.alert(res["errermessage"],{icon: 5,title:"警告"} ,function(index){
						  //do something
						  layer.close(index);
					  });  
				 }
			},
			error:function(index,upload){
				layer.msg("对不起 ，文件上传服务器出错了，请联系管理员");
			}
	   };
	   //执行实例
	   var uploadInst = upload.render(fileopt);
	   form.render();
	   //监听提交
	   form.on('submit(formDemo)', function(data){
		   //console.info(data);
		   formData=$.extend({},data.field,formData);
		   //formData=$.extend({},formData,{"tableBody":JSON.stringify(formData["tableBody"])});
		   //formData=$.extend({},formData,{"tableTitle":JSON.stringify(formData["tableTitle"])});
		   //console.info(formData);
		   var jsonMessage={"tempfilename":formData["tempfilename"],"menunodename":formData["menunodename"]};
		   //console.info(jsonMessage);
		   //layer.msg(JSON.stringify(data.field));
		   if(formData["success"]){
			   $.ajax({  
	                type:'post',  
	                traditional :true,  
	                url:'./dataimp/excelSave',
	                data:jsonMessage,  
	                success:function(data){  
	                	layer.msg(data["message"]);
	                	setTimeout(function(){
		                	window.location.reload();
	                	},1000);
	                }  
	            }); 
		   }else{
	         layer.msg("对不起，Excel文件上传失败，不可以保存为菜单！",function(){});
		   }
	     //layer.msg(JSON.stringify(data.field));
	     return false;
	   });
	});
	
</script>