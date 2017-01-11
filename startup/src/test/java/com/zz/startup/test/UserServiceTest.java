package com.zz.startup.test;

import com.google.common.collect.Lists;
import com.zz.startup.entity.User;
import com.zz.startup.service.UserService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-config.xml")
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    public void test_deleteUser(){
        List<User> users = userService.findAll();
        userService.delete(users);
    }

    @After
    public void test_createUser() {
        User user = new User();
        user.setId("1");
        user.setUsername("admin");
        user.setPlainPassword("111111");
        user.setEmail("admin@admin.com");
        user.setCreationTime(new Date());
        user.setPermissions(Lists.newArrayList("*:*"));

        userService.createUser(user);
    }
}
