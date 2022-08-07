package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
}
