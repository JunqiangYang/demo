package com.zz.startup.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseDao<M, ID extends Serializable> extends MongoRepository<M, ID> {
}
