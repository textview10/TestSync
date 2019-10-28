package com.bignox.testsync.andserver;

import com.yanzhenjie.andserver.annotation.Controller;
import com.yanzhenjie.andserver.annotation.GetMapping;

/**
 * @author xu.wang
 * @date 2019/10/25 16:25
 * @desc
 */
@Controller
public class PageController {
    @GetMapping(path = "/")
    public String index() {
        // Equivalent to [return "/index"].
        return "forward:/index.html";
    }

}
