package com.zz.startup.controller;

import com.google.common.collect.Lists;
import com.zz.startup.annotation.ValidatorId;
import com.zz.startup.entity.Role;
import com.zz.startup.entity.User;
import com.zz.startup.service.RoleService;
import com.zz.startup.service.UserService;
import com.zz.startup.util.Constants;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springside.modules.persistence.SearchFilter;
import org.springside.modules.web.Servlets;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Validated
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(Pageable pageable, HttpServletRequest request, Model model) {
        Map<String, SearchFilter> filters = SearchFilter.parse(Servlets.getParametersStartingWith(request, Constants.SEARCH_PREFIX));
        Page<User> users = userService.findPage(filters, pageable);
        model.addAttribute("users", users);
        return "user/list";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String show(@ValidatorId @PathVariable("id") String id, Model model) {
        User user = userService.get(id);
        model.addAttribute("user", user);
        return "user/show";
    }

    @RequestMapping(value = "new", method = RequestMethod.GET)
    public String _new() {
        return "user/new";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/new";
        }
        userService.createUser(user);

        redirectAttributes.addFlashAttribute("msg", "新增用户成功");
        return "redirect:/user/";
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@ValidatorId @PathVariable("id") String id, Model model) {
        User user = userService.get(id);
        model.addAttribute("user", user);
        return "user/edit";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    public String update(@PathVariable("id") String id, User user, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/edit";
        }

        userService.update(id, user);

        redirectAttributes.addFlashAttribute("msg", "更新用户成功");
        return "redirect:/user/";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@ValidatorId @PathVariable("id") String id,
                         RedirectAttributes redirectAttributes, HttpServletRequest request) {

        userService.delete(id);

        redirectAttributes.addFlashAttribute("msg", "删除用户成功");
        redirectAttributes.addAllAttributes(Servlets.getParametersStartingWith(request, ""));
        return "redirect:/user/";
    }

    @RequestMapping(value = "edit/{id}/role", method = RequestMethod.GET)
    public String editRole(@ValidatorId @PathVariable("id") String id, Model model) {
        User user = userService.get(id);
        List<Role> roles = roleService.findAll();

        if (user.getRoles() != null) {
            for (Role userRole : user.getRoles()) {
                for (Role role : roles) {
                    if (userRole.equals(role)) {
                        role.setChecked(true);
                        break;
                    }
                }
            }
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);

        return "user/edit_role";
    }

    @RequestMapping(value = "update/{id}/role", method = RequestMethod.POST)
    public String updateRole(@ValidatorId @PathVariable("id") String id, String[] roleIds, RedirectAttributes redirectAttributes) {
        User user = userService.get(id);
        if (ArrayUtils.isEmpty(roleIds)) {
            user.setRoles(new ArrayList<Role>());
        } else {
            List<Role> roles = roleService.get(Lists.newArrayList(roleIds));
            user.setRoles(roles);
        }

        userService.save(user);

        redirectAttributes.addFlashAttribute("msg", "更新用户角色成功");
        return "redirect:/user/";
    }

    @RequestMapping(value = "edit/{id}/permission", method = RequestMethod.GET)
    public String editPermission(@ValidatorId @PathVariable("id") String id, Model model) {
        User user = userService.get(id);
        model.addAttribute("user", user);

        return "user/edit_permission";
    }

    @RequestMapping(value = "update/{id}/permission", method = RequestMethod.POST)
    public String updatePermission(@ValidatorId @PathVariable("id") String id, String permission, RedirectAttributes redirectAttributes) {
        User user = userService.get(id);
        String[] permissions = StringUtils.split(permission, ",");
        user.setPermissions(Lists.newArrayList(permissions));
        userService.save(user);

        redirectAttributes.addFlashAttribute("msg", "更新用户权限成功");
        return "redirect:/user/";
    }
}
