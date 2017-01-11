<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<meta name="menu" content="user" />
<meta name="subMenu" content="authority" />

<div class="am-cf am-padding">
  <div class="am-fl am-cf"><a href="${ctx}/authority/"> <strong class="am-text-primary am-text-lg">权限</strong></a> /
    <small>新建权限</small>
  </div>
</div>

<hr/>

<div class="am-g">
  <div class="am-u-sm-12 am-u-md-8">
    <form class="am-form am-form-horizontal" action="${ctx}/authority/create" method="post">
      <div class="am-form-group">
        <label for="authority-name" class="am-u-sm-3 am-form-label">权限名称</label>

        <div class="am-u-sm-9">
          <input type="text" id="authority-name" name="name" required />
        </div>
      </div>

      <div class="am-form-group">
        <label for="authority-permission" class="am-u-sm-3 am-form-label">权限模式</label>

        <div class="am-u-sm-9">
          <input type="text" id="authority-permission" name="permission" required />
        </div>
      </div>

      <div class="am-form-group">
        <label class="am-u-sm-3 am-form-label">父权限</label>
        <div class="am-u-sm-9">
          <select name="parentId" data-am-selected="{searchBox: 1, maxHeight: 200}">
            <option value="#">无</option>
            <c:forEach items="${authorities}" var="authority">
              <option value="${authority.id}">${authority.name}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="am-form-group">
        <label for="authority-summary" class="am-u-sm-3 am-form-label">摘要</label>
        <div class="am-u-sm-9">
          <textarea class="" rows="5" id="authority-summary" name="summary" placeholder="摘要"></textarea>
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