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
     * @return templates using thymeleaf
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

    // 查看订单详情
    // 校验订单归属
    @GetMapping("/show/order/{orderid}")
    public String showOrderDetail(Model model, @PathVariable String orderid) {
        String currentUserUid = userInfoUtil.getCurrentUserID();
        SysOrders sysOrder = sysOrdersDao.selectByOid(orderid);
        if (sysOrder != null && !sysOrder.getUid().equals(currentUserUid)){
            return "forward:/error";
        }
        if (orderid != null) {
            model.addAttribute("cOrder", sysOrder);
            return "orderDetail";
        }
        return "forward:/error";
    }

}