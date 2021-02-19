package xyz.kmahyyg.eshopdemo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import xyz.kmahyyg.eshopdemo.dao.SysUsersDao;
import xyz.kmahyyg.eshopdemo.security.UserInfo;


@Component
public class UserInfoUtil {
    @Autowired
    private SysUsersDao sysUsersDao;

    public String getCurrentUserID(){
        UserInfo currentUser = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentUser != null) {
            // since it's authenticated, the all user are existed.
            String currentUsername = currentUser.getUsername();
            if (!currentUsername.isEmpty()){
                return sysUsersDao.selectByUserName(currentUsername).getUid();
            } else {
                return "";
            }
        }
        return "";
    }
}
