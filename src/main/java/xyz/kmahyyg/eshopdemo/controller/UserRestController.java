package xyz.kmahyyg.eshopdemo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.kmahyyg.eshopdemo.common.PublicResponse;
import xyz.kmahyyg.eshopdemo.dao.SysUserCartDao;
import xyz.kmahyyg.eshopdemo.dao.SysUsersDao;
import xyz.kmahyyg.eshopdemo.enums.ErrorStatusEnum;
import xyz.kmahyyg.eshopdemo.model.SingleItemInCart;
import xyz.kmahyyg.eshopdemo.model.SingleUserCart;
import xyz.kmahyyg.eshopdemo.model.SysUserCart;
import xyz.kmahyyg.eshopdemo.model.SysUsers;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private static final String prefixKeyOfPhone = "user:tel:";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SysUsersDao sysUsersDao;

    @Autowired
    private SysUserCartDao sysUserCartDao;

    @PostMapping("/user/register")
    public ResponseEntity<Object> register(HttpServletRequest request) {
        PublicResponse pr = new PublicResponse(0, "success");
        // currently, we will save information in HttpSession Object
        // for distributed system, JWT and more precised control should be used.
        // check if username exists
        if (sysUsersDao.selectByUserName(request.getParameter("username").trim()) != null || request.getParameter("username").trim().isEmpty()){
            pr.setStatus(ErrorStatusEnum.FAILED_USER.ordinal());
            pr.setMessage("Cannot Register.");
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
        // check if phone number already registered
        if (sysUsersDao.selectByUserPhone(Long.parseLong(request.getParameter("phone").trim())) != null){
            pr.setStatus(ErrorStatusEnum.FAILED_USER.ordinal());
            pr.setMessage("Cannot Register.");
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
        // check if captcha is correct
        if (!request.getParameter("captcha").trim().isEmpty() && Objects.equals(stringRedisTemplate.opsForValue().get(prefixKeyOfPhone + request.getParameter("phone").trim()), request.getParameter("captcha"))){
            // then create new user
            String newPassword = new BCryptPasswordEncoder().encode(request.getParameter("password").trim());
            SysUsers newUser = new SysUsers();
            newUser.setUid(UUID.randomUUID().toString());
            newUser.setPassword(newPassword);
            newUser.setAvatar("/static/imgs/avatar.png");
            newUser.setUsername(request.getParameter("username"));
            newUser.setRole("ROLE_USER");
            newUser.setAddr(request.getParameter("address"));
            newUser.setPhone(Long.parseLong(request.getParameter("phone").trim()));
            newUser.setStatus(SysUsers.ACCOUNT_NORMAL);
            newUser.setPreferDelivery(1);
            newUser.setPreferPayment(1);
            // also create their cart
            SysUserCart newUserCart = new SysUserCart();
            newUserCart.setUid(newUser.getUid());
            // build an empty cart, admin should not have any item in cart since you should not use it for shopping
            SingleUserCart sNewUserCart = new SingleUserCart();
            List<SingleItemInCart> sNewUserCartLst = new ArrayList<>();
            sNewUserCart.setCart(sNewUserCartLst);
            try{
                newUserCart.setItems(sNewUserCart);
            } catch (Exception e){
                pr.setStatus(ErrorStatusEnum.FAILED_INTERNAL.ordinal());
                pr.setMessage(e.toString());
                e.printStackTrace();
                return new ResponseEntity<>(pr, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            if (sysUsersDao.insert(newUser) == 1 && sysUserCartDao.insert(newUserCart) == 1){
                return new ResponseEntity<>(pr, HttpStatus.OK);
            }
            return new ResponseEntity<>(pr, HttpStatus.INTERNAL_SERVER_ERROR);
        } else {
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/user/captcha")
    public ResponseEntity<PublicResponse> getCaptcha(HttpServletRequest request){
        PublicResponse pr = new PublicResponse(ErrorStatusEnum.SUCCESS.ordinal(), "OK!");
        String phoneNumberStr = request.getParameter("phone");
        try {
            long phoneNumber = Long.parseLong(phoneNumberStr);
            if (phoneNumber < 10000000000L || phoneNumber > 20000000000L){
                throw new NumberFormatException("Not a phone number!");
            }
        } catch (NumberFormatException e){
            pr.setStatus(ErrorStatusEnum.FAILED_USER.ordinal());
            pr.setMessage("Illegal Phone!");
            return new ResponseEntity<>(pr, HttpStatus.BAD_REQUEST);
        }
        if (stringRedisTemplate.opsForValue().get(prefixKeyOfPhone + phoneNumberStr) != null) {
            pr.setStatus(ErrorStatusEnum.FAILED_ABUSE.ordinal());
            pr.setMessage("429 Too Many Requests!");
            return new ResponseEntity<>(pr, HttpStatus.TOO_MANY_REQUESTS);
        }
        long upperBound = 99999999;
        long lowerBound = 10000000;
        long rand = (long) (Math.random()*(upperBound - lowerBound + 1) + lowerBound);
        stringRedisTemplate.opsForValue().set(prefixKeyOfPhone + phoneNumberStr, String.valueOf(rand), 2, TimeUnit.MINUTES);
        System.out.println(phoneNumberStr + " -- " + rand);
        return new ResponseEntity<>(pr, HttpStatus.OK);
    }
}
