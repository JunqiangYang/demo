package com.zz.startup.controller;

import com.zz.startup.annotation.ValidatorId;
import com.zz.startup.entity.Authority;
import com.zz.startup.service.AuthorityService;
import com.zz.startup.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.web.Servlets;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("authority")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(Pageable pageable, HttpServletRequest request, Model model) {
        Map<String, SearchFilter> filters = SearchFilter.parse(Servlets.getParametersStartingWith(request, Constants.SEARCH_PREFIX));
        Page<Authority> authorities = authorityService.findPage(filters, pageable);
        authorityService.transform(authorities);

        model.addAttribute("authorities", authorities);
        return "authority/list";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String show(@ValidatorId @PathVariable("id") String id, Model model) {
        Authority authority = authorityService.get(id);
        model.addAttribute("authority", authority);
        return "authority/show";
    }

    @RequestMapping(value = "new", method = RequestMethod.GET)
    public String _new(Model model) {
        List<Authority> authorities = authorityService.findAll();
        model.addAttribute("authorities", authorities);
        return "authority/new";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Authority authority, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "authority/new";
        }

        authorityService.createAuthority(authority);

        redirectAttributes.addFlashAttribute("msg", "新增权限成功");
        return "redirect:/authority/";
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@ValidatorId @PathVariable("id") String id, Model model) {
        Authority authority = authorityService.get(id);
        model.addAttribute("authority", authority);

        return "authority/edit";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    public String update(@PathVariable("id") String id, Authority authority, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "authority/edit";
        }

        authorityService.update(id, authority);

        redirectAttributes.addFlashAttribute("msg", "更新权限成功");
        return "redirect:/authority/";
    }

}
