package com.zz.startup.test;

import com.zz.startup.entity.Authority;
import com.zz.startup.service.AuthorityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-config.xml")
public class AuthorityServiceTest {

    @Autowired
    AuthorityService authorityService;

    @Test
    public void test_createAuthority() {
        Authority authority = new Authority();
        authority.setCreationTime(new Date());
        authority.setParentId("#");
        authority.setName("用户");
        authority.setPermission("user:*");
        authorityService.save(authority);
    }
}
