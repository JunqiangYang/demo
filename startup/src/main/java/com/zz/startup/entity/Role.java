package com.zz.startup.entity;

import com.zz.startup.annotation.Unique;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

public class Role extends BaseEntity {

    @NotBlank
    @Unique
    private String roleName;
    private String aliasName;
    @DBRef
    private List<Authority> authorities;

    private String status;      // [enable|disable]

    @Transient
    private boolean checked;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public List<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return role != null && role.getId().equals(getId());
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (null == getId() ? 0 : getId().hashCode());
        return hash;
    }
}
