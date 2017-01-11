<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="menu" scope="request">
  <sitemesh:getProperty property='meta.menu' />
</c:set>
<c:set var="subMenu" scope="request">
  <sitemesh:getProperty property='meta.subMenu' />
</c:set>

<!doctype html>
<html class="no-js fixed-layout">
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <title>Amaze UI Admin</title>
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <meta name="renderer" content="webkit">
  <meta http-equiv="Cache-Control" content="no-siteapp"/>
  <link rel="icon" type="image/png" href="${ctx}/assets/i/favicon.png">
  <link rel="apple-touch-icon-precomposed" href="${ctx}/assets/i/app-icon72x72@2x.png">
  <meta name="apple-mobile-web-app-title" content="Amaze UI"/>
  <link rel="stylesheet" href="${ctx}/assets/css/amazeui.min.css"/>
  <link rel="stylesheet" href="${ctx}/assets/css/admin.css">
  <link rel="stylesheet" href="${ctx}/assets/css/app.css">
  <sitemesh:head/>
</head>

<body>
  <!--[if lte IE 9]>
  <p class="browsehappy">你正在使用<strong>过时</strong>的浏览器，Amaze UI 暂不支持。 请 <a href="http://browsehappy.com/" target="_blank">升级浏览器</a>
    以获得更好的体验！</p>
  <![endif]-->

  <%@ include file="/WEB-INF/layouts/header.jsp" %>

  <div class="am-cf admin-main">
    <%@ include file="/WEB-INF/layouts/menu.jsp" %>

    <div class="admin-content">
      <sitemesh:body/>
      <%@ include file="/WEB-INF/layouts/footer.jsp" %>
    </div>
  </div>

  <div class="am-modal am-modal-confirm" tabindex="-1" id="delete-confirm">
    <div class="am-modal-dialog">
      <div class="am-modal-hd">删除</div>
      <div class="am-modal-bd">
        你，确定要删除这条记录吗？
      </div>
      <div class="am-modal-footer">
        <span class="am-modal-btn" data-am-modal-cancel>取消</span>
        <span class="am-modal-btn" data-am-modal-confirm>确定</span>
      </div>
    </div>
  </div>

  <a href="#" class="am-icon-btn am-icon-th-list am-show-sm-only admin-menu" data-am-offcanvas="{target: '#admin-offcanvas'}"></a>

  <!--[if lt IE 9]>
  <script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
  <script src="http://cdn.staticfile.org/modernizr/2.8.3/modernizr.js"></script>
  <script src="${ctx}/assets/js/amazeui.ie8polyfill.min.js"></script>
  <![endif]-->

  <!--[if (gte IE 9)|!(IE)]><!-->
  <script src="${ctx}/assets/js/jquery.min.js"></script>
  <!--<![endif]-->
  <script src="${ctx}/assets/js/amazeui.min.js"></script>
  <script src="${ctx}/assets/js/app.js"></script>
</body>
</html>