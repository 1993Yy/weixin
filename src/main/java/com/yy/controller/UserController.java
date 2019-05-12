package com.yy.controller;

import com.yy.common.config.Result;
import com.yy.entity.User;
import com.yy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Package: com.yy.controller
 * @ClassName: UserController
 * @Author: Created By Yy
 * @Date: 2019-05-12 10:35
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public Result getUserPage(User user, Pageable pageable){
        return new Result(userService.getPage(pageable,user));
    }
}
