<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix='fmt'%>
<!DOCTYPE html>
<head>
<meta charset="utf-8">
<title>数据报表管理</title>
<meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
  <meta name="apple-mobile-web-app-status-bar-style" content="black"> 
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="format-detection" content="telephone=no">
  <link rel="icon" href="images/ic.ico">
  <link rel="stylesheet" href="js/layui_2.2.4/css/layui.css"  media="all">
  <link rel="stylesheet" href="css/global.css" media="all">
  <style type="text/css">
	.flow-default{width: 600px; height: 400px; overflow: auto; font-size: 0;}
	.flow-default li{display: inline-block; margin: 0 5px; font-size: 14px; width: 48%;  margin-bottom: 10px; height: 100px; line-height: 100px; text-align: center; background-color: #eee;}
	.flow-default img{width: 100%; height: 100%;}
	.site-data-flow{max-width: 98%; overflow: auto; text-align: center;margin:0 auto;}
	
	.site-demo-flow{width: 98%; height: 350px;margin:1px auto; overflow: auto;padding:5px; text-align: left;background-color: #eeeeee;}
	.site-demo-flow img{width: 70%; margin-left: 0px;padding: 2px;   border: 1px #ccc solid;display: block;}
	.site-demo-flow img:hover{ border: 1px #FF5722 solid;cursor: pointer;}
	    @media screen and (min-width: 1201px) { 
		 .site-demo-flow img{width: 70%;}
		} 
		/* 设置了浏览器宽度不小于1201px时 abc 显示1200px宽度 */ 
		
		@media screen and (max-width: 1200px) { 
		.site-demo-flow img{width: 80%;}
		} 
		/* 设置了浏览器宽度不大于1200px时 abc 显示900px宽度 */ 
		
		@media screen and (max-width: 900px) { 
		.site-demo-flow img{width: 80%;}
		} 
		/* 设置了浏览器宽度不大于900px时 abc 显示200px宽度 */ 
  </style>
</head>
<body>
<!-- 让IE8/9支持媒体查询，从而兼容栅格 -->
<!--[if lt IE 9]>
  <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
  <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
<![endif]-->   
<div class="layui-layout layui-layout-admin">
  <div class="layui-header header header-demo">
  <div class="layui-main">
    <a class="logoText" href="./index">
    	<i class="layui-icon layui-anim layui-anim-rotate" style="font-size: 18px;">&#xe629;</i>&nbsp;DataReport App
    </a>
    <ul class="layui-nav" pc>
      <li class="layui-nav-item">
        <a href="javascript:void(0);"><i class="layui-icon">&#xe612;</i>&nbsp;<c:out value="${currentUser.username }" /> </a>
      </li>
       <li class="layui-nav-item">
        <a href="javascript: loginout();">注销 </a>
      </li>
      <li class="layui-nav-item">
       &nbsp;&nbsp;
      </li>
    </ul>
  </div>
</div>

<!-- 头部结束 -->
<div class="layui-side layui-bg-black">
    <div class="layui-side-scroll">
      
<ul class="layui-nav layui-nav-tree site-demo-nav">
  <c:if test="${currentUser.role=='admin' || currentUser.role=='data_import'}">
  <li class="layui-nav-item layui-nav-itemed">
    <a class="javascript:;" href="javascript:;"><i class="layui-anim layui-anim-rotate " style="font-size: 14px;">&#xe681;</i>&nbsp;导入方式</a>
    <dl class="layui-nav-child">
    <!-- layui-this -->
       <dd class="">
        <a href="javascript:void(0);"  data-url="dataimp/excel">Excel导入</a>
      </dd>
      <dd class="">
        <a href="javascript:void(0);"  data-url="dataimp/sqlserver">SQLServer导入</a>
      </dd>
      <dd class="">
        <a href="javascript:void(0);" data-url="dataimp/oracle">Oracle导入</a>
      </dd>
      <!--  <dd class="">
        <a href="javascript:void(0);" data-url="datasource/home">数据库管理</a>
      </dd> -->
      
    </dl>
  </li>
  </c:if>
  <c:if test="${currentUser.role=='admin' || currentUser.role=='data_import' || currentUser.role=='charts_gen'}">
  <li class="layui-nav-item layui-nav-itemed">
    <a class="javascript:;" href="javascript:;"><i class="layui-icon" style="font-size: 14px;">&#xe63c;</i>&nbsp;数据菜单</a>
    <dl class="layui-nav-child">
      <c:forEach items="${menulist }" var="menu">
        <dd class="">
	        <a href="javascript:void(0);" data-url="<c:out value='${menu.url}'/>?filename=<c:out value='${menu.filename}'/>"><c:out value="${menu.menuname}"/></a>
      	</dd>
      </c:forEach>
      
    </dl>
  </li>
  </c:if>
  <c:if test="${currentUser.role=='admin' || currentUser.role=='charts_review'}">
  <li class="layui-nav-item layui-nav-itemed">
    <a class="javascript:;" href="javascript:;"><i class="layui-icon" style="font-size: 14px;">&#xe62c;</i>&nbsp;统计报表</a>
    <dl class="layui-nav-child">
        <dd class="">
	        <a href="javascript:void(0);" data-url="charts/chartslist">报表图库</a>
      	</dd>
      	<c:forEach items="${menuChartList }" var="menuChart">
        <dd class="">
	        <a href="javascript:void(0);" data-url="<c:out value='${menuChart.url}'/>?filename=<c:out value='${menuChart.filename}'/>"><c:out value="${menuChart.menuChartName}"/></a>
      	</dd>
      </c:forEach>
    </dl>
  </li>
  </c:if>
  <c:if test="${currentUser.role=='admin'}">
  <li class="layui-nav-item layui-nav-itemed">
    <a class="javascript:;" href="javascript:;"><i class="layui-icon" style="font-size: 14px;">&#xe613;</i>&nbsp;系统管理</a>
    <dl class="layui-nav-child">
       <dd class="">
        <a href="javascript:void(0);"  data-url="user/userlist">人员管理</a>
      </dd>
      <dd class="">
        <a href="javascript:void(0);"  data-url="dataimp/excelmanager">Excel导入参数管理</a>
      </dd>
    </dl>
  </li>
  </c:if>
  
  <li class="layui-nav-item" style="height: 30px; text-align: center"></li>
	</ul>
  </div>
</div>
  <!-- 左侧导航结束 -->
  <div class="layui-body">
    <div id="dynamicContent" class="nav-content" style="overflow:visible;"></div>
  </div>
<!-- 内容栏目的底部 -->  
<div class="layui-footer footer footer-demo">
  <div class="layui-main">
    <p>&copy;2017 <a href="http://weibo.com/liulovexj" target="_baank">TimBryant</a> MIT license</p>
  </div>
</div>

<div class="site-tree-mobile layui-hide">
  <i class="layui-icon">&#xe602;</i>
</div>
<div class="site-mobile-shade"></div>
</div>
<script src="js/core/jquery-1.11.0.js" ></script>
<script src="js/layui_2.2.4/layui.js" charset="utf-8"></script>
<script src="js/echart/echarts.js"></script>
<!-- 引入 macarons 主题 -->
<script src="js/echart/theme/macarons.js"></script>
<script>
  $.ajaxSetup ({ 
	    cache: false //关闭AJAX相应的缓存 
  });

  $(function(){
	  menuclick("dataimp/welcome"); 
  });
  //按钮点击事件 
  var menuclick=function(url){
	  $("#dynamicContent").load(url,function(arg) {}); 
  };
  //注销
  var loginout=function(){
	  var layer = layui.layer;
	 //配置一个透明的询问框
	  layer.confirm('是否注销？', {icon: 5, title:'操作提示'}, function(index){
		  window.location.href="./loginout";
		  //关闭弹出层
		  layer.close(index);
	  });
  };
  
  //注意：导航 依赖 element 模块，否则无法进行功能性操作
  layui.use(['element','layer','util'], function(){
     var element = layui.element;
     var layer = layui.layer;
     var $ = layui.jquery;
     
     //自定义的页面跳转
     var level=function(url){
    	 var index = layer.load(3);
    	 $("#dynamicContent").load(url,function(arg) {
    	    setTimeout(function(){
    		 layer.close(index);
    	    },500);
    	 });
     };
     //动态实现跳转
     $("li.layui-nav-item dl.layui-nav-child dd a").on('click',function(){
		  var othis=$(this),url=othis.data("url");
		  level.call(this,url);
	 });
  });
</script>
</body>
</html>