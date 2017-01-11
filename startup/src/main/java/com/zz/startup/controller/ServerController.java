package com.zz.startup.controller;

import com.zz.startup.annotation.ValidatorId;
import com.zz.startup.entity.Server;
import com.zz.startup.service.ServerService;
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
import java.util.Map;

@Controller
@RequestMapping("server")
public class ServerController {

    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(Pageable pageable, HttpServletRequest request, Model model) {
        Map<String, SearchFilter> filters = SearchFilter.parse(Servlets.getParametersStartingWith(request, Constants.SEARCH_PREFIX));
        Page<Server> servers = serverService.findPage(filters, pageable);
        model.addAttribute("servers", servers);

        return "server/list";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String show(@ValidatorId @PathVariable("id") String id, Model model) {
        Server server = serverService.get(id);
        model.addAttribute("server", server);
        return "server/show";
    }

    @RequestMapping(value = "new", method = RequestMethod.GET)
    public String _new() {
        return "server/new";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Server server, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "server/new";
        }

        serverService.save(server);

        redirectAttributes.addFlashAttribute("msg", "新增服务器成功");
        return "redirect:/server/";
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@ValidatorId @PathVariable("id") String id, Model model) {
        Server server = serverService.get(id);
        model.addAttribute("server", server);

        return "server/edit";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    public String update(@PathVariable("id") String id, Server server, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "server/edit";
        }

        serverService.update(id, server);

        redirectAttributes.addFlashAttribute("msg", "更新服务器成功");
        return "redirect:/server/";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@ValidatorId @PathVariable("id") String id,
                         RedirectAttributes redirectAttributes, HttpServletRequest request) {
        serverService.delete(id);

        redirectAttributes.addFlashAttribute("msg", "删除服务器成功");
        redirectAttributes.addAllAttributes(Servlets.getParametersStartingWith(request, ""));
        return "redirect:/server/";
    }
}
