<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
      <!-- 网页内容start -->  
		  <div class="app-timer-buttons">
		 	<blockquote class="layui-elem-quote"><i class="layui-icon">&#xe613;</i> 人员信息管理</blockquote>
			<button class="layui-btn   layui-btn-sm" data-method="add"><i class="layui-icon">&#xe654;</i>新增</button>
			<button class="layui-btn   layui-btn-sm layui-btn-normal" data-method="modify"> <i class="layui-icon">&#xe642;</i>修改</button>
			<button class="layui-btn   layui-btn-sm layui-btn-danger" data-method="deleteObj"><i class="layui-icon">&#xe640;</i>删除</button>
			<div class="layui-form">
		  <table class="layui-table">
		    <colgroup>
		      <col width="50">
		      <col width="250">
		      <col width="150">
		      <col>
		    </colgroup>
		    <thead>
		      <tr>
		        <th><input type="checkbox" name="" lay-skin="primary" lay-filter="allChoose"></th>
		        <th>用户名</th>
		        <th>角色</th>
		        <th>描述</th>
		      </tr> 
		    </thead>
		    <tbody class="dynamic-data">
		          <!-- 动态内容填充区域 -->
		    </tbody>
		  </table>
		  <div id="page"></div>
		</div>
		</div>
		<!-- 网页内容end --> 
		
		
<script>
	layui.use(['laypage','form'], function(){
		  var laypage = layui.laypage,$ = layui.jquery, form = layui.form;
		  var laypage = layui.laypage;
		  form.render();
		 //全选
		  form.on('checkbox(allChoose)', function(data){
		    var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]');
		    child.each(function(index, item){
		      item.checked = data.elem.checked;
		    });
		    form.render('checkbox');
		  });
		 
		 //角色编码和中文对应表
		 var map = {};
		 map["admin"]="管理员";
		 map["data_import"]="数据导入人员";
		 map["charts_gen"]="报表采集人员";
		 map["charts_review"]="报表浏览人员";
		 
		 
		//动态加载表格数据
		var dy_load=function(){
			$.post("user/listusers",{},
			       function(response) {
					 var tbody=$("tbody.dynamic-data");
					 tbody.html("");
					 var dataList=response["list"];
					 var pageSize=response["pageSize"];
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
						    	    		var trdata=dataList[obj];
								        	 var tr=$('<tr></tr>');
								        	 tr.append('<td><input type="checkbox" name="" lay-skin="primary" value="'+trdata["username"]+'"></td>');
								        	 tr.append('<td>'+trdata["username"]+'</td>');
								        	 tr.append('<td>'+map[trdata["role"]]+'</td>');
								        	 tr.append('<td>'+trdata["description"]+'</td>');
								        	 tbody.append(tr);
						    	    	}
							        }
						    	    //重新渲染表格数据
							         form.render();
						      }
						   });
					 }else{
						 for(var obj in dataList){
				        	 var trdata=dataList[obj];
				        	 var tr=$('<tr></tr>');
				        	 tr.append('<td><input type="checkbox" name="" lay-skin="primary" value="'+trdata["username"]+'"></td>');
				        	 tr.append('<td>'+trdata["username"]+'</td>');
				        	 tr.append('<td>'+map[trdata["role"]]+'</td>');
				        	 tr.append('<td>'+trdata["description"]+'</td>');
				        	 tbody.append(tr);
				         }
						 //删除信息到不需要分页时 清除页面的分页组件
						 $("#page").html("");
					 }
			         //重新渲染表格数据
			         form.render();
			  });
		};
		
	  //按钮事件集合
	  var excute_events={
		  add:function(){
			  menuclick("user/register");
		  },
		  modify:function(){
			  var chks=$(".layui-table tbody input:checkbox:checked");
			  if(chks && chks.length==1){
				  chks.each(function() {
					  var username=$(this).val();
					  menuclick("user/detail/"+username);
				  });
			  }else if(chks && chks.length>1){
				  layer.msg('对不起，只能选择一行记录',function(){});
			  }else{
				  //加不加function 弹出层会有抖动效果去区别
				  layer.msg('请选一行记录');
			  }
						  
		  }, 
		  deleteObj:function(){
			  var chks=$(".layui-table tbody input:checkbox:checked");
			  if(chks && chks.length>0){
				  var ids=""; 
				  chks.each(function() {
					  var uname=$(this).val();
					  ids+=uname+",";
				  });
				  //配置一个透明的询问框
				  layer.confirm('确认要删除吗?', {icon:5}, function(index){
						 layer.close(index);
						 //删除请求
						  $.post("user/delete",{users:ids},function(response){
							  if(response && response["message"]){
								  layer.msg(response["message"],
										  {time: 1000}); //2秒关闭（如果不配置，默认是3秒）;
								  dy_load();
							  }
						  });
				  });
			  }else{
				  layer.msg('请至少勾选一行记录');
			  }
		  }
	  };
	  $(".app-timer-buttons .layui-btn.layui-btn-sm").on('click',function(){
		  var othis=$(this),method=othis.data("method");
		  excute_events[method]?excute_events[method].call(this,othis):'';
	  });
	  
	  
	//页面加载完毕后加载数据
	 $(function(){
		 dy_load();
	 });
		 
	});
	
</script>