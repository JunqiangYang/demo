<%@ page language="java" pageEncoding="UTF-8" %>
<meta name="menu" content="server" />

<div class="am-cf am-padding">
  <div class="am-fl am-cf"><a href="${ctx}/server/"> <strong class="am-text-primary am-text-lg">服务器</strong></a> /
    <small>修改服务器</small>
  </div>
</div>

<hr/>

<div class="am-g">
  <div class="am-u-sm-12 am-u-md-8">
    <form class="am-form am-form-horizontal" action="${ctx}/server/update/${server.id}" method="post">
      <div class="am-form-group">
        <label for="server-ip" class="am-u-sm-3 am-form-label">ip</label>

        <div class="am-u-sm-9">
          <input type="text" id="server-ip" name="ip" value="${server.ip}" required />
        </div>
      </div>

      <div class="am-form-group">
        <label for="server-loginName" class="am-u-sm-3 am-form-label">登录名</label>

        <div class="am-u-sm-9">
          <input type="text" id="server-loginName" name="loginName" value="${server.loginName}" required />
        </div>
      </div>

      <div class="am-form-group">
        <label for="server-loginPwd" class="am-u-sm-3 am-form-label">登录密码</label>

        <div class="am-u-sm-9">
          <input type="text" id="server-loginPwd" name="loginPwd" value="${server.loginPwd}" required />
        </div>
      </div>

      <div class="am-form-group">
        <label for="server-summary" class="am-u-sm-3 am-form-label">摘要</label>
        <div class="am-u-sm-9">
          <textarea class="" rows="5" id="server-summary" name="summary">${server.summary}</textarea>
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
