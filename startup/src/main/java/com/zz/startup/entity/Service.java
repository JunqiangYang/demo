package com.zz.startup.entity;

import com.zz.startup.annotation.Unique;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.annotation.Transient;

/**
 * 代表一个服务,如tomcat,mysql等
 */
public class Service extends BaseEntity {

    @NotBlank
    private String serverId;
    @Transient
    private String serverIp;

    @NotBlank
    @Unique
    private String name;
    private String aliasName;
    @NotBlank
    private String path;
    private String port;

    private String otherConfig; // 其他配置

    private String summary;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getOtherConfig() {
        return otherConfig;
    }

    public void setOtherConfig(String otherConfig) {
        this.otherConfig = otherConfig;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
