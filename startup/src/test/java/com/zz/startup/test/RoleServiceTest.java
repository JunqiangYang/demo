package com.zz.startup.test;

import com.zz.startup.entity.Role;
import com.zz.startup.service.RoleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-config.xml")
public class RoleServiceTest {

    @Autowired
    RoleService roleService;

    @Test
    public void test_createRole(){
        Role role = new Role();
        role.setRoleName("admin");
        role.setAliasName("管理员");
        roleService.save(role);

        role = new Role();
        role.setRoleName("user");
        role.setAliasName("普通用户");
        roleService.save(role);
    }
}
