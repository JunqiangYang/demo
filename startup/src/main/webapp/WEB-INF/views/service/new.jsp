<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<meta name="menu" content="service" />

<div class="am-cf am-padding">
  <div class="am-fl am-cf"><a href="${ctx}/service/"> <strong class="am-text-primary am-text-lg">服务</strong></a> /
    <small>新建服务</small>
  </div>
</div>

<hr/>

<div class="am-g">
  <div class="am-u-sm-12 am-u-md-8">
    <form class="am-form am-form-horizontal" action="${ctx}/service/create" method="post">
      <div class="am-form-group">
        <label for="name" class="am-u-sm-3 am-form-label">名称</label>
        <div class="am-u-sm-9">
          <input type="text" id="name" name="name" value="${service.name}" required />
        </div>
      </div>

      <div class="am-form-group">
        <label for="aliasName" class="am-u-sm-3 am-form-label">别名</label>
        <div class="am-u-sm-9">
          <input type="text" id="aliasName" name="aliasName" value="${service.aliasName}" />
        </div>
      </div>

      <div class="am-form-group">
        <label for="serverId" class="am-u-sm-3 am-form-label">所属服务器</label>
        <div class="am-u-sm-9">
          <select id="serverId" name="serverId" data-am-selected="{searchBox: 1, maxHeight: 200}">
            <c:forEach items="${servers}" var="server">
              <option value="${server.id}" <c:if test="${server.id eq service.serverId}">selected</c:if>>${server.ip}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="am-form-group">
        <label for="path" class="am-u-sm-3 am-form-label">路径</label>
        <div class="am-u-sm-9">
          <input type="text" id="path" name="path" value="${service.path}" required />
        </div>
      </div>

      <div class="am-form-group">
        <label for="port" class="am-u-sm-3 am-form-label">端口号</label>
        <div class="am-u-sm-9">
          <input type="text" id="port" name="port" value="${service.port}" required />
        </div>
      </div>

      <div class="am-form-group">
        <label for="service-otherConfig" class="am-u-sm-3 am-form-label">其他配置</label>
        <div class="am-u-sm-9">
          <textarea class="" rows="5" id="service-otherConfig" name="otherConfig" placeholder="其他配置"></textarea>
        </div>
      </div>

      <div class="am-form-group">
        <label for="serivce-summary" class="am-u-sm-3 am-form-label">摘要</label>
        <div class="am-u-sm-9">
          <textarea class="" rows="5" id="serivce-summary" name="summary" placeholder="摘要"></textarea>
        </div>
      </div>

      <div class="am-form-group">
        <div class="am-u-sm-9 am-u-sm-push-3">
          <button type="submit" class="am-btn am-btn-primary">保存</button>
        </div>
      </div>
    </form>
  </div>
</div>