package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.kmahyyg.eshopdemo.dao.SysItemsDao;
import xyz.kmahyyg.eshopdemo.model.SysItems;
import java.util.List;
import xyz.kmahyyg.eshopdemo.utils.UserInputSanitizer;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ItemController {
    @Autowired
    private SysItemsDao sysItemsDao;

    @RequestMapping("/show/item/detail")
    public String getItemDetail(Model model,Integer itemId){
        SysItems showItem = sysItemsDao.selectById(itemId);
        if(showItem != null){
            // USE WITH CAUTION
            // the user input of item detail from any shop owner should be untrusted.
            // Since this project does NOT include any management panel, so here I'm so lazy to sanitize about it.
            // Preventing from XSS is on the way.
            model.addAttribute("showItem", showItem);
            return "itemDetail";
        }else{
            return "error";
        }
    }
    @RequestMapping("/show/item/search")
    public String searchResult(Model model, @RequestParam("itemName") String itemName){
        List<SysItems> searchItems = sysItemsDao.selectByItemName(itemName);
        if(searchItems == null){
            return "error";
        }
        model.addAttribute("searchKey", UserInputSanitizer.SanitizeForHTML(itemName));
        model.addAttribute("searchResult", searchItems);
        return "search";
    }
}
