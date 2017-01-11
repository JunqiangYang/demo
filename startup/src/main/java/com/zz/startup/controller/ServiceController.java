package com.zz.startup.controller;

import com.zz.startup.annotation.ValidatorId;
import com.zz.startup.entity.Server;
import com.zz.startup.entity.Service;
import com.zz.startup.service.ServerService;
import com.zz.startup.service.ServiceService;
import com.zz.startup.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
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
@RequestMapping("service")
public class ServiceController {

    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ServerService serverService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String list(Pageable pageable, HttpServletRequest request, Model model) {
        Map<String, SearchFilter> filters = SearchFilter.parse(Servlets.getParametersStartingWith(request, Constants.SEARCH_PREFIX));
        Page<Service> services = serviceService.findPage(filters, pageable);

        serviceService.buildServerIp(services);

        model.addAttribute("services", services);

        return "service/list";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public String show(@ValidatorId @PathVariable("id") String id, Model model) {
        Service service = serviceService.get(id);
        model.addAttribute("service", service);
        return "service/show";
    }

    @RequestMapping(value = "new", method = RequestMethod.GET)
    public String _new() {
        return "service/new";
    }

    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Service service, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "service/new";
        }

        serviceService.save(service);

        redirectAttributes.addFlashAttribute("msg", "新增服务成功");
        return "redirect:/service/";
    }

    @RequestMapping(value = "edit/{id}", method = RequestMethod.GET)
    public String edit(@ValidatorId @PathVariable("id") String id, Model model) {
        Service service = serviceService.get(id);
        model.addAttribute("service", service);
        return "service/edit";
    }

    @RequestMapping(value = "update/{id}", method = RequestMethod.POST)
    public String update(@PathVariable("id") String id, Service service, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "service/edit";
        }

        serviceService.update(id, service);

        redirectAttributes.addFlashAttribute("msg", "更新服务成功");
        return "redirect:/service/";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.GET)
    public String delete(@ValidatorId @PathVariable("id") String id,
                         RedirectAttributes redirectAttributes, HttpServletRequest request) {
        serviceService.delete(id);

        redirectAttributes.addFlashAttribute("msg", "删除服务成功");
        redirectAttributes.addAllAttributes(Servlets.getParametersStartingWith(request, ""));
        return "redirect:/service/";
    }

    @ModelAttribute("servers")
    private List<Server> getAllServers() {
        return serverService.findAll();
    }
}
