package org.web.admin.controller;

import org.core.common.annotation.AnonymousAccess;
import org.core.common.constant.WebMappingConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class RootController {
    
    private final WebMappingConstant webMappingConstant;
    
    public RootController(WebMappingConstant webMappingConstant) {
        this.webMappingConstant = webMappingConstant;
    }
    
    @AnonymousAccess
    @GetMapping("/")
    public ModelAndView web() {
        return new ModelAndView(webMappingConstant.getPath());
    }
}
