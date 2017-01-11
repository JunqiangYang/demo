package com.zz.startup.repository;

import com.zz.startup.entity.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends BaseDao<Role, String> {
}
