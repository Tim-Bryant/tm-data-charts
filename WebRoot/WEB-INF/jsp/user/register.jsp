<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
<div>
<blockquote class="layui-elem-quote"><i class="layui-icon">&#xe642;</i> 人员信息编辑</blockquote>
<form class="layui-form" action="">
  
  <div class="layui-form-item">
    <label class="layui-form-label">所属角色</label>
    <div class="layui-input-inline">
      <select name="role" lay-verify="required" lay-filter="dsTypeSelect">
         <option value=""></option>
         <option value="admin" <c:if test="${user.role=='admin' }">selected</c:if>>管理员</option>
         <option value="data_import" <c:if test="${user.role=='data_import' }">selected</c:if>>数据导入人员</option>
         <option value="charts_gen" <c:if test="${user.role=='charts_gen' }">selected</c:if>>报表采集人员</option>
         <option value="charts_review" <c:if test="${user.role=='charts_review' }">selected</c:if>>报表浏览人员</option>
      </select>
    </div>
  </div>
  
  <div class="layui-form-item">
    <label class="layui-form-label">用户名</label>
    <div class="layui-input-inline">
      <!-- 用来判断新增还是修改用户 -->
      <input type="hidden" name="isnewUser" value='<c:out value="${user.username}" />' />
      <input type="text" name="username" required  lay-verify="required" value='<c:out value="${user.username}" />' placeholder="请输入用户名" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux">用户名不可以重复</div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">密码</label>
    <div class="layui-input-inline">
      <input type="text" name="pwd" required  lay-verify="required" value='<c:out value="${user.pwd}" />' placeholder="请输入密码" autocomplete="off" class="layui-input">
    </div>
  </div>
  
  <div class="layui-form-item layui-form-text">
    <label class="layui-form-label">描述</label>
    <div class="layui-input-block">
      <textarea name="description" id="textarea-demo" placeholder="请输入内容" class="layui-textarea"><c:out value="${user.description}" /></textarea>
    </div>
  </div>
  <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit lay-filter="app_ds_submit">立即提交</button>
      <button type="reset" class="layui-btn layui-btn-primary">重置</button>
      <button type="button" data-method="confirmback" class="layui-btn layui-btn-primary">返回</button>
    </div>
  </div>
</form>
</div>
<script>
	//注意：导航 依赖 element 模块，否则无法进行功能性操作
	layui.use(['form','layer'], function(){
	   var $ = layui.jquery, layer = layui.layer; //独立版的layer无需执行这一句
	   var form = layui.form;
	   form.render();
	
	   //监听提交
	   form.on('submit(app_ds_submit)', function(data){
	     //layer.msg(JSON.stringify(data.field));
	     $.post("user/save",data.field,
	       function(data) {
	         if(data && data["message"]){
	        	 layer.msg(data["message"]);
	        	 if(data["isadd"]){
		        	 menuclick("user/userlist"); 
	        	 }
	         }
	       });
	     
	     return false;
	   }); 
	   //触发事件
	   var active = {
		 confirmback: function(args){
	       //配置一个透明的询问框
		   layer.confirm('是否返回到列表页面?', {icon: 5, title:'操作提示'}, function(index){
				  layer.close(index);
				  menuclick("user/userlist"); 
		   });
	     }
	   };
	   
	   $('.layui-input-block .layui-btn').on('click', function(){
		    var othis = $(this), method = othis.data('method');
		    active[method] ? active[method].call(this, othis) : '';
	   });
	   
	});
</script>