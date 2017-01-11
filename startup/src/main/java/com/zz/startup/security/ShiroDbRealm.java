package com.zz.startup.security;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springside.modules.utils.Encodes;

import com.zz.startup.entity.Authority;
import com.zz.startup.entity.Role;
import com.zz.startup.entity.User;
import com.zz.startup.exception.CustomException;
import com.zz.startup.util.Constants;

public class ShiroDbRealm extends AuthorizingRealm {

    @Autowired
    protected MongoTemplate mongoTemplate;

    private User findByUsername(String username) {
        return mongoTemplate.findOne(new Query(Criteria.where("username").is(username)), User.class);
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        String username = "";
        if (authcToken instanceof UsernamePasswordToken) {
            UsernamePasswordToken upt = (UsernamePasswordToken) authcToken;
            username = upt.getUsername();
        }

        if (authcToken instanceof UsernamePasswordCaptchaToken) {
            UsernamePasswordCaptchaToken token = (UsernamePasswordCaptchaToken) authcToken;
            username = token.getUsername();
            String captcha = token.getCaptcha();
            Session s = SecurityUtils.getSubject().getSession();
            String exitCode = (String) s.getAttribute("code");
            if (null == captcha || !StringUtils.equalsIgnoreCase(captcha, exitCode)) {
                throw new CustomException("验证码错误");
            }
        }

        User user = findByUsername(username);
        if (user != null) {
            byte[] salt = Encodes.decodeHex(user.getSalt());
            return new SimpleAuthenticationInfo(new ShiroUser(user.getId(),
                    user.getUsername()), user.getPassword(),
                    ByteSource.Util.bytes(salt), getName());
        }

        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        User user = findByUsername(shiroUser.username);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        List<Role> roles = user.getRoles();
        if (roles != null) {
            for (Role role : roles) {
                List<Authority> auths = role.getAuthorities();
                if (auths != null) {
                    for (Authority auth : auths) {
                        info.addStringPermission(auth.getPermission());
                    }
                }
                info.addRole(role.getRoleName());
            }
        }

        List<String> permissions = user.getPermissions();
        if (permissions != null) {
            info.addStringPermissions(permissions);
        }

        return info;
    }

    @PostConstruct
    public void initCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Constants.HASH_ALGORITHM);
        matcher.setHashIterations(Constants.HASH_INTERATIONS);
        setCredentialsMatcher(matcher);
    }

    public static class ShiroUser implements Serializable {
        private static final long serialVersionUID = -1373760761780840081L;
        private String id;
        private String username;

        public ShiroUser(String id, String username) {
            this.id = id;
            this.username = username;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * <shiro:principal/>
         */
        @Override
        public String toString() {
            return username;
        }

    }
}
