package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import xyz.kmahyyg.eshopdemo.dao.SysOrdersDao;
import xyz.kmahyyg.eshopdemo.model.SysOrders;
import xyz.kmahyyg.eshopdemo.utils.UserInfoUtil;

import java.util.*;

@Controller
public class OrderController {
    @Autowired
    private SysOrdersDao sysOrdersDao;

    @Autowired
    private UserInfoUtil userInfoUtil;


    /**
     * 查看当前用户的历史订单
     * @param model
     * @return
     */
    @GetMapping("/show/user/order")
    public String showOrderOfCurrentUser(Model model) {
        String currentUserUid = userInfoUtil.getCurrentUserID();
            if (!currentUserUid.isEmpty()){
                List<SysOrders> allOrdersByUser = sysOrdersDao.selectByUserId(currentUserUid);
                model.addAttribute("orders",allOrdersByUser);
            }
        return "userorders";
    }

}
