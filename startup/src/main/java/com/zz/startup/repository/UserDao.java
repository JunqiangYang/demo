package com.zz.startup.repository;

import com.zz.startup.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends BaseDao<User, String> {
}
