package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminController {
    @RequestMapping("/show/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAdmin(){
        return "admin";
    }

}
