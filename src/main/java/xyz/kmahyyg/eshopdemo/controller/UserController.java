package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.kmahyyg.eshopdemo.common.PublicResponse;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @PreAuthorize("permitAll()")
    @RequestMapping("/show/user/login")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/show/user/register")
    public String toRegister() {
        return "register";
    }

    @RequestMapping("/show/user/logout")
    @ResponseBody
    public ResponseEntity<PublicResponse> showLogout(HttpServletRequest request){
        request.getSession().invalidate();
        return new ResponseEntity<PublicResponse>(new PublicResponse(0, "Logged Out!"), HttpStatus.OK);
    }
}
