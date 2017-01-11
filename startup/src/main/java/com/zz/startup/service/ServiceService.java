package com.zz.startup.service;

import com.zz.startup.entity.Server;
import com.zz.startup.entity.Service;
import com.zz.startup.repository.ServerDao;
import com.zz.startup.repository.ServiceDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService extends BaseService<Service, String> {

    @Autowired
    private ServiceDao serviceDao;
    @Autowired
    private ServerDao serverDao;

    public void buildServerIp(Page<Service> services) {
        List<Server> servers = serverDao.findAll();
        for (Service service : services) {
            for (Server server : servers) {
                if (StringUtils.equals(service.getServerId(), server.getId())) {
                    service.setServerIp(server.getIp());
                    break;
                }
            }
        }
    }
}
