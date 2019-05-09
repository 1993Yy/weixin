package com.yy.controller;

import com.yy.config.Result;
import com.yy.service.WeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Package: com.yy.controller
 * @ClassName: WeiXinController
 * @Author: Created By Yy
 * @Date: 2019-05-09 14:49
 */
@RestController
@RequestMapping("/weixin")
public class WeiXinController {

    @Autowired
    private WeiXinService weiXinService;

    @RequestMapping("/authorize")
    public void authorize(HttpServletRequest request, HttpServletResponse response){
        weiXinService.authorize(request, response);
    }

    @GetMapping("/getQR")
    public Result getQR(@RequestParam(value = "expireTime",defaultValue = "300") Integer expireTime,
                        @RequestParam(value = "senceID",defaultValue = "yang") String senceID){
        return new Result(weiXinService.getQR(expireTime, senceID));
    }
}
