package com.zz.startup.entity;

import com.zz.startup.annotation.Unique;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

/**
 * 一台Linux服务器
 */
public class Server extends BaseEntity {

    @NotBlank
    @Unique
    private String ip;
    private List<String> openPorts; // 防火墙开放端口
    private String loginName;
    private String loginPwd;
    private String summary;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<String> getOpenPorts() {
        return openPorts;
    }

    public void setOpenPorts(List<String> openPorts) {
        this.openPorts = openPorts;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }
}
