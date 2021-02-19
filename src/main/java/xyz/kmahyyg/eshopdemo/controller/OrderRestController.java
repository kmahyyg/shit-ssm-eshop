package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.kmahyyg.eshopdemo.common.PublicResponse;
import xyz.kmahyyg.eshopdemo.dao.SysItemsDao;
import xyz.kmahyyg.eshopdemo.dao.SysOrdersDao;
import xyz.kmahyyg.eshopdemo.enums.OrderStatusEnum;
import xyz.kmahyyg.eshopdemo.model.SysItems;
import xyz.kmahyyg.eshopdemo.model.SysOrders;
import xyz.kmahyyg.eshopdemo.utils.UserInfoUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@RestController
public class OrderRestController {

    @Autowired
    private UserInfoUtil userInfoUtil;

    @Autowired
    private SysItemsDao sysItemsDao;

    @Autowired
    private SysOrdersDao sysOrdersDao;

    @PostMapping("/api/user/order")
    public ResponseEntity<Object> addOrder(@RequestBody String jsonParam) {
        PublicResponse pr = new PublicResponse(0, "success");
        String currentUserUid = userInfoUtil.getCurrentUserID();
        if (!currentUserUid.isEmpty()) {
            SysOrders record = new SysOrders();
            // 从用户提交的请求中拿到商品id和数量，计算价格
            try {
                JSONObject userSubmittedJson = new JSONObject(jsonParam);
                JSONArray userSubmittedCartItems = userSubmittedJson.getJSONArray("items");
                BigDecimal finalTotalPrice = BigDecimal.valueOf(0.00);
                for (int i = 0; i < userSubmittedCartItems.length(); i++) {
                    JSONObject singleItemInOrder = (JSONObject) userSubmittedCartItems.get(i);
                    int curItemId = Integer.parseInt(singleItemInOrder.getString("itemId"));
                    int curItemNo = Integer.parseInt(singleItemInOrder.getString("itemNo"));
                    SysItems curNewItem = sysItemsDao.selectById(curItemId);
                    if (curNewItem == null){throw new RuntimeException();}
                    BigDecimal curNewItemSinglePrice = curNewItem.getPrice();
                    finalTotalPrice = finalTotalPrice.add(curNewItemSinglePrice.multiply(BigDecimal.valueOf(curItemNo)));
                }
                record.setFinalPrice(finalTotalPrice);
                record.setOid(UUID.randomUUID().toString());
                record.setStatus(OrderStatusEnum.CREATED);
                record.setItems(jsonParam);
                record.setUid(currentUserUid);
                record.setGenTime(new Date().getTime() / 1000);
                record.setPaymentId(1);
                record.setDeliveryId(1);
                if (sysOrdersDao.insert(record) == 1){
                    return new ResponseEntity<>(pr, HttpStatus.OK);
                } else {
                    throw new RuntimeException();
                }
            } catch (Exception e){
                e.printStackTrace();
                pr.setStatus(1);
                pr.setMessage("Error!");
                return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
            }
        } else {
            pr.setStatus(-1);
            pr.setMessage("Error!");
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/api/user/order/{orderId}")
    public ResponseEntity<Object> deleteOrderOfCurrentUser(@PathVariable String orderId){
        PublicResponse pr = new PublicResponse(0, "success");
        String currentUserUid = userInfoUtil.getCurrentUserID();
        try {
            if(!orderId.isEmpty()) {
                SysOrders currentOrder = sysOrdersDao.selectByOid(orderId);
                if (currentOrder == null) {throw new RuntimeException();}
                // check if horizontal permission escalation vuln
                if (!Objects.equals(currentOrder.getUid(), currentUserUid)) {
                    pr.setStatus(-1);
                    pr.setMessage("The order belonging checked failed!");
                    return new ResponseEntity<>(pr, HttpStatus.UNAUTHORIZED);
                }
                if (sysOrdersDao.deleteOrderByOid(orderId) == 1){
                    return new ResponseEntity<>(pr, HttpStatus.OK);
                } else {
                    throw new RuntimeException();
                }
            } else {
                throw new RuntimeException();
            }
            // jump on the frontend!
        } catch (Exception e){
            pr.setStatus(1);
            pr.setMessage("Error!");
            e.printStackTrace();
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
    }
}
