<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
     
<blockquote class="layui-elem-quote"><i class="layui-icon">&#xe614;</i> Excel导入参数管理</blockquote>
<form class="layui-form" action="">

  <div class="layui-form-item">
    <label class="layui-form-label">最大行数</label>
    <div class="layui-input-inline">
      <input type="text" name="allowmaxrows" required  lay-verify="allowmaxrows" value="<c:out value='${params.allowmaxrows}' />" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux">Excel文件允许导入的最大行数,最大值100000，最小值1，建议值2000</div>
  </div>
  <div class="layui-form-item">
    <label class="layui-form-label">每页条数</label>
    <div class="layui-input-inline">
      <input type="text" name="pageNumbers" required  lay-verify="pageNumbers" value="<c:out value='${params.pageNumbers}' />" placeholder="" autocomplete="off" class="layui-input">
    </div>
    <div class="layui-form-mid layui-word-aux">Excel文件允许导入预览分页每一页允许的最大数，最大值1000，最小值1，建议值100</div>
  </div>
   <div class="layui-form-item">
    <div class="layui-input-block">
      <button class="layui-btn" lay-submit lay-filter="formExcelManager">保存</button>
    </div>
  </div>
  
</form>
<script>
	//注意：导航 依赖 element 模块，否则无法进行功能性操作
	layui.use(['form'], function(){
	   var form = layui.form,$ = layui.jquery;
	   
	   form.render();
	   //自定义校验规则
	   form.verify({
		   allowmaxrows:[
   		     /^(?!0)(?:[0-9]{1,5}|100000)$/
   		     ,'数字的大小超过规定的值范围'
		   ] 
		   //我们既支持上述函数式的方式，也支持下述数组的形式
		   //数组的两个值分别代表：[正则匹配、匹配不符时的提示文字]
		   ,pageNumbers: [
			  /^(?!0)(?:[0-9]{1,3}|1000)$/
		     ,'数字的大小超过规定的值范围'
		   ] 
		 });      
	   //监听提交
	   form.on('submit(formExcelManager)', function(data){
		   var  formData=$.extend({},data.field);
		     $.ajax({  
                type:'post',  
                traditional :true,  
                url:'./dataimp/excelmanagerSave',
                data:formData,  
                success:function(data){  
                	layer.msg(data["message"]);
                }  
            }); 
	     return false;
	   });
	   
	});
</script>