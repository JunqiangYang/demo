package com.zz.startup.security;

import com.zz.startup.entity.User;
import com.zz.startup.security.ShiroDbRealm.ShiroUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.springframework.data.domain.AuditorAware;

public class UserAuditorAware implements AuditorAware<User> {

    public User getCurrentAuditor() {
        SecurityManager securityManager = ThreadContext.getSecurityManager();
        if (securityManager == null) {
            return null;
        }

        Subject subject = SecurityUtils.getSubject();
        Object object = subject.getPrincipal();
        if (object == null) {
            return null;
        }

        ShiroUser user = (ShiroUser) object;

        User u = new User();
        u.setId(user.getId());
        u.setUsername(user.getUsername());

        return u;
    }

}
