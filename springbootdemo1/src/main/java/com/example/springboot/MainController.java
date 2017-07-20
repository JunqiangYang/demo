package com.example.springboot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Junqiang.Yang
 * @create 2017-07-10 17:18
 **/

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("name", "nn");
        return "index";
    }

}
