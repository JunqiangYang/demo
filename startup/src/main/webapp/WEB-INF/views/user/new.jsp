<%@ page language="java" pageEncoding="UTF-8" %>
<meta name="menu" content="user" />
<meta name="subMenu" content="user" />

<div class="am-cf am-padding">
  <div class="am-fl am-cf"><a href="${ctx}/user/"> <strong class="am-text-primary am-text-lg">用户</strong></a> /
    <small>新建用户</small>
  </div>
</div>

<hr/>

<div class="am-g">
  <div class="am-u-sm-12 am-u-md-8">
    <form class="am-form am-form-horizontal" action="${ctx}/user/create" method="post">
      <div class="am-form-group">
        <label for="user-name" class="am-u-sm-3 am-form-label">姓名</label>

        <div class="am-u-sm-9">
          <input type="text" id="user-name" name="username" placeholder="姓名 / Name" required />
        </div>
      </div>

      <div class="am-form-group">
        <label for="user-email" class="am-u-sm-3 am-form-label">email</label>

        <div class="am-u-sm-9">
          <input type="email" id="user-email" name="email" placeholder="电子邮件 / Email" required>
        </div>
      </div>

      <div class="am-form-group">
        <label for="user-password" class="am-u-sm-3 am-form-label">密码</label>

        <div class="am-u-sm-9">
          <input type="password" id="user-password" name="plainPassword" placeholder="密码">
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
