package com.zz.startup.repository;

import com.zz.startup.entity.Server;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerDao extends BaseDao<Server, String> {
}
