package com.demo.controller;

import com.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
// @RequestMapping("/")
class UserController {
    private final UserService userService;
    // TODO
}
