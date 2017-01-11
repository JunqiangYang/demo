package com.zz.startup.service;

import com.zz.startup.entity.Server;
import com.zz.startup.repository.ServerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServerService extends BaseService<Server, String> {

    @Autowired
    private ServerDao serverDao;
}
