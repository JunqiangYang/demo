package com.zz.startup.controller;

import com.zz.startup.security.FormAuthenticationCaptchaFilter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        Subject subject = SecurityUtils.getSubject();
        Object object = subject.getPrincipal();
        if (object == null) {
            return "login";
        }
        return "redirect:/";
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String fail(@RequestParam(FormAuthenticationCaptchaFilter.DEFAULT_USERNAME_PARAM) String userName,
                       Model model) {
        Subject subject = SecurityUtils.getSubject();
        Object object = subject.getPrincipal();
        if (object == null) {
            model.addAttribute(FormAuthenticationCaptchaFilter.DEFAULT_USERNAME_PARAM, userName);
            return "login";
        }
        return "redirect:/";
    }

}
