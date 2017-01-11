package com.zz.startup.repository;

import com.zz.startup.entity.Service;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceDao extends BaseDao<Service, String> {
}
