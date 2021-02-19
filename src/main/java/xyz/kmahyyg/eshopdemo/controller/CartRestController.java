package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.kmahyyg.eshopdemo.common.PublicResponse;
import xyz.kmahyyg.eshopdemo.dao.SysItemsDao;
import xyz.kmahyyg.eshopdemo.dao.SysUserCartDao;
import xyz.kmahyyg.eshopdemo.model.SingleItemInCart;
import xyz.kmahyyg.eshopdemo.model.SingleUserCart;
import xyz.kmahyyg.eshopdemo.model.SysUserCart;
import xyz.kmahyyg.eshopdemo.utils.UserInfoUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CartRestController {

    @Autowired
    private SysUserCartDao sysUserCartDao;

    @Autowired
    private SysItemsDao sysItemsDao;

    @Autowired
    private UserInfoUtil userInfoUtil;


    @PostMapping("/user/cart")
    public ResponseEntity<Object> updateUserCartFromReq(HttpServletRequest request) {
        PublicResponse pr = new PublicResponse(0, "Ok");
        String currentUserID = userInfoUtil.getCurrentUserID();
        //由于在注册的时候每个用户都有一个空的购物车，所以只需要更新items即可，不需要插入
        //获取数据库中的当前用户的购物车信息
        SysUserCart currentCart = sysUserCartDao.selectByUserId(currentUserID);
        //获取CurrentCart中的items字段
        SingleUserCart currentItems = currentCart.getItems();
        List<SingleItemInCart> currentItemsList = currentItems.getCart();

        //处理获取到的数据
        int newItemId;
        int newItemNum;
        try {
            newItemId = Integer.parseInt(request.getParameter("itemId"));
            newItemNum = Integer.parseInt(request.getParameter("itemNum"));
            if (newItemNum <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            pr.setStatus(4);
            pr.setMessage("Please submit valid data!");
            e.printStackTrace();   // must print stacktrace for debugging purpose
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }

        if (sysItemsDao.selectById(newItemId) == null) {
            // the item id uploaded by user is not existing in database
            pr.setStatus(5);
            pr.setMessage("Please submit valid data!");  // unify the error message to avoid hacker
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }

        // the cart of current user is empty, directly update without checking existing items
        if (currentItemsList.size() == 0) {
            // build a new item
            SingleItemInCart NewItem = new SingleItemInCart();
            NewItem.setItemId(newItemId);
            NewItem.setItemNum(newItemNum);
            // directly insert
            currentItemsList.add(NewItem);
            currentItems.setCart(currentItemsList);
            currentCart.setItems(currentItems);
            sysUserCartDao.updateByUserId(currentCart);
            return new ResponseEntity<>(pr, HttpStatus.OK);
        } else {
            // current user's cart is not empty.
            //
            // build a flag for check if new item is parsed.
            // flag removed due to early return.
            // boolean newItemParsed = false;
            //
            for (SingleItemInCart currentItemInCart : currentItemsList) {
                // check if updated item already in the cart
                if (currentItemInCart.getItemId() == newItemId) {
                    //
                    // flag removed due to early return
                    // set flag to avoid useless loop
                    // newItemParsed = true;
                    //
                    // if already in cart, just change the value
                    // there will never be something in cart but with zero num.
                    currentItemInCart.setItemNum(newItemNum);
                    if (sysUserCartDao.updateByUserId(currentCart) == 1) {
                        return new ResponseEntity<>(pr, HttpStatus.OK);
                    } else {
                        pr.setStatus(3);
                        pr.setMessage("Internal Error Occurred!");
                        return new ResponseEntity<>(pr, HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                    // To Remove the specific item, use DELETE method instead of POST.
                }
            }
            // after for loop, if new item processed, it will not reach here, so
            // no need to use if here
            //
            // the updated item is NOT in the current user's cart
            // but current user's cart is not empty
            // build a new item
            SingleItemInCart newItemInCart = new SingleItemInCart();
            newItemInCart.setItemId(newItemId);
            newItemInCart.setItemNum(newItemNum);
            currentItemsList.add(newItemInCart);
            currentItems.setCart(currentItemsList);
            currentCart.setItems(currentItems);
            if (sysUserCartDao.updateByUserId(currentCart) == 1) {
                return new ResponseEntity<>(pr, HttpStatus.OK);
            } else {
                pr.setStatus(3);
                pr.setMessage("Internal Error Occurred!");
                return new ResponseEntity<>(pr, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @DeleteMapping("/user/cart")
    public ResponseEntity<Object> deleteUserCartItem(HttpServletRequest request){
        PublicResponse pr = new PublicResponse(0, "success");
        try{
            int toDeleteItemId = Integer.parseInt(request.getParameter("itemId"));
            String currentUserUid = userInfoUtil.getCurrentUserID();
            if(!currentUserUid.isEmpty()){
                SysUserCart userCart = sysUserCartDao.selectByUserId(currentUserUid);
                SingleUserCart cartLst = userCart.getItems();
                List<SingleItemInCart> itemsInCart = cartLst.getCart();
                if (itemsInCart.size() == 0) {throw new NumberFormatException();}
                for(SingleItemInCart currentItem: itemsInCart){
                    int currentItemId = currentItem.getItemId();
                    if(currentItemId == toDeleteItemId){
                        itemsInCart.remove(currentItem);
                        cartLst.setCart(itemsInCart);
                        userCart.setItems(cartLst);
                        if(sysUserCartDao.updateByUserId(userCart) == 1){
                            pr.setMessage("Item Delete Success!");
                            return new ResponseEntity<>(pr, HttpStatus.OK);
                        }else{
                            pr.setStatus(3);
                            pr.setMessage("Internal Error Occurred!");
                            return new ResponseEntity<>(pr, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                }
            }
        }catch(NumberFormatException e){
            pr.setStatus(4);
            pr.setMessage("Invalid Data!");
            e.printStackTrace();
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
        pr.setStatus(4);
        pr.setMessage("Invalid Data!");
        return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
    }
}
