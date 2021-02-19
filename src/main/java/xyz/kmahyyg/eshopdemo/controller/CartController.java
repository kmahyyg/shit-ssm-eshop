package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import xyz.kmahyyg.eshopdemo.dao.SysItemsDao;
import xyz.kmahyyg.eshopdemo.dao.SysUserCartDao;
import xyz.kmahyyg.eshopdemo.model.*;
import xyz.kmahyyg.eshopdemo.utils.UserInfoUtil;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private SysUserCartDao sysUserCartDao;

    @Autowired
    private SysItemsDao sysItemsDao;

    @Autowired
    private UserInfoUtil userInfoUtil;

    //return cart page
    @RequestMapping("/show/user/cart")
    public String showCart(Model model) {
        String currentUserUid = userInfoUtil.getCurrentUserID();
            if (!currentUserUid.isEmpty()){
                SysUserCart userCart = sysUserCartDao.selectByUserId(currentUserUid);
                SingleUserCart cartLst = userCart.getItems();
                List<SingleItemInCart> itemsInCart = cartLst.getCart();
                List<ItemRepresentationInCart> itemsShowInCart = new ArrayList<>();
                // user cart might be empty
                if (!itemsInCart.isEmpty()) {
                    // for each item inside cart
                    // verify the number of item when inserting into database.
                    for (SingleItemInCart item:itemsInCart) {
                        ItemRepresentationInCart curItem = new ItemRepresentationInCart(sysItemsDao.selectById(item.getItemId()), item.getItemNum());
                        itemsShowInCart.add(curItem);
                    }
                }
                model.addAttribute("cartItems", itemsShowInCart);
        }
        return "cart";
    }


//    @RequestMapping("api/cart/showall")
//    @ResponseBody
//    public ResponseEntity<PublicResponse> ShowCartOfCurrentUser() {
//        UserInfo currentUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (currentUser != null) {
//            String currentUsername = currentUser.getUsername();
//            if (!currentUsername.isEmpty()){
//                String currentUserUid = sysUsersDao.selectByUserName(currentUsername).getUid();
//                List<SysUserCart> allCartItems = sysUserCartDao.selectByUserId(currentUserUid);
//            }
//            //TODO: render the orders data in template html
//        }
//
//        return null;
//    }

}
