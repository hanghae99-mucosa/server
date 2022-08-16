package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.dto.user.SignupRequestDto;
import com.hanghae99.mocosa.layer.dto.user.SignupResponseDto;
import com.hanghae99.mocosa.layer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // 회원 가입 페이지
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(SignupRequestDto signupRequestDto){
        userService.registerUser(signupRequestDto);
        return "redirect:/login";
//        SignupResponseDto signupResponseDto = userService.registerUser(signupRequestDto);
//        return new ResponseEntity<>(signupResponseDto, HttpStatus.OK);
    }
}
