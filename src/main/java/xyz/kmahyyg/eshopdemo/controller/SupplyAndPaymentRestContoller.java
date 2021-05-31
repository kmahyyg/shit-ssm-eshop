package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.kmahyyg.eshopdemo.common.DeliveryResponse;
import xyz.kmahyyg.eshopdemo.common.PaymentResponse;
import xyz.kmahyyg.eshopdemo.common.PublicResponse;
import xyz.kmahyyg.eshopdemo.dao.SysItemsDao;
import xyz.kmahyyg.eshopdemo.dao.SysOrdersDao;
import xyz.kmahyyg.eshopdemo.enums.OrderStatusEnum;
import xyz.kmahyyg.eshopdemo.model.SysOrders;
import xyz.kmahyyg.eshopdemo.utils.UserInfoUtil;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class SupplyAndPaymentRestContoller {
    @Autowired
    private UserInfoUtil userInfoUtil;

    @Autowired
    private SysOrdersDao sysOrdersDao;

    @PostMapping("/api/delivery/all")
    public List<DeliveryResponse> showAllDelivery(){
        List<DeliveryResponse> dr = new ArrayList<DeliveryResponse>();
        dr.add(new DeliveryResponse(1, "SF"));
        dr.add(new DeliveryResponse(2,"JD"));
        dr.add(new DeliveryResponse(3,"CN"));
        return dr;
    }

    @PostMapping("/api/payment/all")
    public List<PaymentResponse> showAllPayment(){
        List<PaymentResponse> pr = new ArrayList<PaymentResponse>();
        pr.add(new PaymentResponse(1, "Alipay"));
        pr.add(new PaymentResponse(2,"PayPal"));
        return pr;
    }

    @PostMapping("/api/order/pay/{orderId}")
    public ResponseEntity<Object> payCurrentOrder(@PathVariable String orderId, HttpServletRequest request){
        PublicResponse pr = new PublicResponse(0, "success");
        String currentUserUid = userInfoUtil.getCurrentUserID();
        try {
            if (!orderId.isEmpty()){
                SysOrders currentOrder = sysOrdersDao.selectByOid(orderId);
                // only for OrderStatusEnum.WAIT_PAYMENT is working.
                if (currentOrder.getStatus() == OrderStatusEnum.WAIT_DELIVERY){
                    pr.setMessage("Already Paid.");
                    return new ResponseEntity<>(pr, HttpStatus.OK);
                }
                if (currentOrder.getUid().equals(currentUserUid)){
                    BigDecimal payAmount = new BigDecimal(request.getParameter("payAmount"));
                    int deliveryId = Integer.parseInt(request.getParameter("deliveryId"));
                    int paymentId = Integer.parseInt(request.getParameter("paymentId"));
                    if (payAmount.compareTo(currentOrder.getFinalPrice())>=0){
                        currentOrder.setStatus(OrderStatusEnum.WAIT_DELIVERY);
                        currentOrder.setPaymentId(paymentId);
                        currentOrder.setDeliveryId(deliveryId);
                        currentOrder.setPaidTime(new Date().getTime() / 1000);
                        int affected = sysOrdersDao.updateByOidSelective(currentOrder);
                        if (affected == 1){
                            return new ResponseEntity<>(pr, HttpStatus.OK);
                        } else {
                            throw new RuntimeException();
                        }
                    } else {
                        throw new RuntimeException();
                    }
                } else {
                    throw new RuntimeException();
                }
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            e.printStackTrace();
            pr.setStatus(1);
            pr.setMessage("Error!");
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/order/refund/{orderId}")
    public ResponseEntity<PublicResponse> refundOrder(@PathVariable String orderId){
        PublicResponse pr = new PublicResponse(0, "success");
        try {
            if (orderId != null) {
                SysOrders curOrder = sysOrdersDao.selectByOid(orderId);
                if (curOrder != null){
                    String currentUserID = userInfoUtil.getCurrentUserID();
                    if (curOrder.getUid().equals(currentUserID)){
                        if (curOrder.getStatus() == OrderStatusEnum.FINISHED){
                            curOrder.setStatus(OrderStatusEnum.REFUNDED);
                            curOrder.setRefundTime(new Date().getTime() / 1000);
                            int affected = sysOrdersDao.updateByOidSelective(curOrder);
                            if (affected != 1) {
                                throw new RuntimeException();
                            } else {
                                return new ResponseEntity<>(pr, HttpStatus.OK);
                            }
                        } else {
                         throw new RuntimeException();
                        }
                    } else {
                        throw new RuntimeException();
                    }
                } else {
                    throw new RuntimeException();
                }
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e){
            e.printStackTrace();
            pr.setStatus(1);
            pr.setMessage("Error!");
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/api/order/shipout/{orderId}")
    public ResponseEntity<PublicResponse> shipItemOutForOrder(@PathVariable String orderId){
        PublicResponse pr = new PublicResponse(0, "success");
        String currentUserID = userInfoUtil.getCurrentUserID();
        try {
            if (orderId != null){
                SysOrders curOrder = sysOrdersDao.selectByOid(orderId);
                if (curOrder != null){
                    if (curOrder.getUid().equals(currentUserID)){
                        if (curOrder.getStatus() == OrderStatusEnum.WAIT_DELIVERY){
                            curOrder.setStatus(OrderStatusEnum.FINISHED);
                            curOrder.setDoneTime(new Date().getTime() / 1000);
                            int affected = sysOrdersDao.updateByOidSelective(curOrder);
                            if (affected != 1){
                                throw new RuntimeException();
                            } else {
                                return new ResponseEntity<>(pr, HttpStatus.OK);
                            }
                        } else {
                            throw new RuntimeException();
                        }
                    } else {
                        throw new RuntimeException();
                    }
                } else {
                    throw new RuntimeException();
                }
            } else {
                throw new RuntimeException();
            }

        }catch(Exception e) {
            e.printStackTrace();
            pr.setStatus(1);
            pr.setMessage("Error!");
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
    }
}
