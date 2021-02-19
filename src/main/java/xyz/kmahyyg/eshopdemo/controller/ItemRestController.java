package xyz.kmahyyg.eshopdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import xyz.kmahyyg.eshopdemo.utils.UserInputSanitizer;

@Controller
public class ItemRestController {
    @PostMapping("/api/search")
    public String search(HttpServletRequest request) {
        String keyword = request.getParameter("q");
        return "forward:/show/item/search?itemName=" + keyword;
    }
}
