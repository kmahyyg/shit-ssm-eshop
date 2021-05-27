package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.kmahyyg.eshopdemo.common.PublicResponse;
import xyz.kmahyyg.eshopdemo.dao.SysUsersDao;
import xyz.kmahyyg.eshopdemo.model.SysUsers;
import xyz.kmahyyg.eshopdemo.utils.UserInfoUtil;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    @Autowired
    private SysUsersDao sysUsersDao;

    @Autowired
    private UserInfoUtil userInfoUtil;

    @PreAuthorize("permitAll()")
    @RequestMapping("/show/user/login")
    public String toLogin() {
        return "login";
    }

    @PreAuthorize("permitAll()")
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

    @RequestMapping("/show/user/modinfo")
    public String toModInfo(Model model) {
        String cuid = userInfoUtil.getCurrentUserID();
        SysUsers cUser = sysUsersDao.selectByUserId(cuid);
        if (cuid == null || cuid.equals("")){
            return "error";
        }
        model.addAttribute("cUser", cUser);
        return "usermod";
    }
}
