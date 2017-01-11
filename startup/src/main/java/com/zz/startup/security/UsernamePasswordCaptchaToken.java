package com.zz.startup.security;

import org.apache.shiro.authc.UsernamePasswordToken;

public class UsernamePasswordCaptchaToken extends UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    private String captcha;

    public UsernamePasswordCaptchaToken() {
        super();
    }

    public UsernamePasswordCaptchaToken(String username, char[] password,
                                        boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}