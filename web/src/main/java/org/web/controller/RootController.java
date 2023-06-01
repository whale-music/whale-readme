package org.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RootController {
    
    @Value("${web-mapping}")
    private String webMapping;
    
    @GetMapping("/")
    public ModelAndView web() {
        return new ModelAndView(webMapping);
    }
}
