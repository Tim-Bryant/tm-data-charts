<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<blockquote class="layui-elem-quote"><i class="layui-icon">&#xe622;</i> SQL Server数据库导入</blockquote>
<form class="layui-form" action="">
  <div class="layui-form-item">
    <label class="layui-form-label">服务器地址</label>
    <div class="layui-input-inline">
      <input type="text" name="sqlserver_ip" value="" required  lay-verify="required" placeholder="服务器地地址" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux">输入数据库服务器的IP或域名</div>
    <label class="layui-form-label">端口号</label>
    <div class="layui-input-inline">
      <input type="text" name="sqlserver_port" value="1433" required  lay-verify="required" placeholder="端口号" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux">1433是默认端口，其它端口请修改</div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">登录名</label>
    <div class="layui-input-inline">
      <input type="text" name="sqlserver_user" value="" required  lay-verify="required" placeholder="登录名" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux">仅支持 SQL Server 身份认证</div>
     <label class="layui-form-label">密码</label>
    <div class="layui-input-inline">
      <input type="password" name="sqlserver_pwd" value="" required  lay-verify="required" placeholder="密码" autocomplete="off" class="layui-input">
    </div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">数据库名</label>
    <div class="layui-input-inline">
      <input type="text" name="sqlserver_dbname" value="" required  lay-verify="required" placeholder="数据库名" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux">请输入需要查询的数据库名</div>
  </div>
  <div class="layui-form-item layui-form-text">
    <label class="layui-form-label">SQL语句</label>
    <div class="layui-input-block">
      <textarea name="sql_query" id="textarea-demo" required  lay-verify="required" placeholder="请输入SQL语句" class="layui-textarea"></textarea>
    </div>
  </div>
   <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn  layui-btn-danger" lay-submit lay-filter="querySQL">查询</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
    </div>
  </div>
</form>
  <hr class="layui-bg-green">
    <div class="layui-form">
			  <div class="layui-form-item">
			    <label class="layui-form-label">菜单节点名</label>
			    <div class="layui-input-inline">
			      <input type="text" name="menunodename" required  lay-verify="required" placeholder="菜单节点名" autocomplete="off" class="layui-input">
			    </div>
			      <button class="layui-btn layui-btn-primary savings" data-method="saveForMenu"><i class="layui-icon">&#xe63c;</i> 保存为菜单</button>
			  </div>
			  <div class="site-data-flow">
			      <div id="page"></div>
				  <table class="layui-table">
	     				<!-- 数据表格 -->
				  </table>
			  </div>
     </div>
		
<script>
	//注意：导航 依赖 element 模块，否则无法进行功能性操作
	layui.use(['form','layedit','upload','laypage'], function(){
	   var form = layui.form,$ = layui.jquery;
	   var formData={}; //用来定义表单提交JSO你对象
	   var laypage = layui.laypage;
	   form.render();
	   //监听提交
	   form.on('submit(querySQL)', function(data){
		   formData=$.extend({},data.field,{});
		   $.ajax({  
                type:'post',  
                traditional :true,  
                url:'./dataimp/parserSqlServer',  
                data:formData,
                beforeSend:function(){
                	 //加载层-风格4
    			    layer.msg('正在查询...', {
    				   icon: 16
    				   ,shade: 0.3
    				   , time: 6000*10
    			   });
                },
                success:function(datas){
                	 if(datas["success"]){
                		 setTimeout(function(){
   						  layer.closeAll();
                		  layer.msg(datas["message"]);
   		                },2000);
                		 var pageSize=datas["pageSize"]==null?10:datas["pageSize"];
                    	 var res=eval('('+datas["jsondata"]+')');
                    	 formData=$.extend({},datas);
                    	 var tTable=$("table.layui-table");
        				 tTable.html("");
        				 var titleList=res["tableTitle"];
        				 var thead=$('<thead></thead>');
        				 var theadTr=$('<tr></tr>');
        				 //动态生成title
        				 for(var obj in titleList){
        					 var tbtitle=titleList[obj];
        					 theadTr.append("<th>"+tbtitle+"</th>");
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
    					 }else{
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
                	 }
                	
                } ,
                error:function(datas){
                	layer.msg("有错误请联系管理员！");
                }
            });  
	     //layer.msg(JSON.stringify(data.field));
	     return false;
	   });
	   
	   
	   //触发事件
	   var active = {
			saveForMenu: function(args){
				var node=$("input[type='text'][name='menunodename']");
				var nodelVal=node.val();
				if(node&&nodelVal==""){
					layer.msg("菜单节点名不可以为空",function(){});
					return;
				}
				//console.info(formData);
				if(typeof(formData["tempfilename"] ) == "undefined"){
					layer.msg("数据库查询记录为空，不可以保存为菜单！",function(){});
					return;
				}
				
		       //配置一个透明的询问框
			   layer.confirm('是否添加节点【'+nodelVal+'】到菜单？', {icon: 5, title:'操作提示'}, function(index){
				   formData=$.extend({},formData);
				   var jsonMessage={"tempfilename":formData["tempfilename"],"menunodename":nodelVal};
				   if(formData["success"]){
				    //console.info(jsonMessage);
					 //layer.msg(JSON.stringify(data.field));
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
				   }
				   layer.close(index);
			   });
	    	}
	   };
	   
	   $('div.layui-form-item .layui-btn.savings').on('click', function(){
		    var othis = $(this), method = othis.data('method');
		    active[method] ? active[method].call(this, othis) : '';
	   });
	});
</script>