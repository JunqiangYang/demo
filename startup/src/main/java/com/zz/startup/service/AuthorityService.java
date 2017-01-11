package com.zz.startup.service;

import com.google.common.collect.Lists;
import com.zz.startup.entity.Authority;
import com.zz.startup.repository.AuthorityDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService extends BaseService<Authority, String> {

    @Autowired
    private AuthorityDao authorityDao;

    public void transform(Page<Authority> authorities) {
        for (Authority authority : authorities) {
            String parentId = authority.getParentId();
            if (!StringUtils.equals(parentId, "#")){
                Authority parentAuthority = get(parentId);
                authority.setParentName(parentAuthority.getName());
            } else {
                authority.setParentName("#");
            }
        }
    }

    public void createAuthority(Authority authority) {
        authorityDao.save(authority);

        String parentId = authority.getParentId();

        if (!StringUtils.equals(parentId, "#")) {
            Authority parent = get(parentId);
            List<String> children = parent.getChildren();
            if (children == null) {
                children = Lists.newArrayList();
            }
            children.add(authority.getId());
            parent.setChildren(children);
            authorityDao.save(parent);
        }
    }

    public void updateAuthority(String id, Authority authority) {

    }
}
