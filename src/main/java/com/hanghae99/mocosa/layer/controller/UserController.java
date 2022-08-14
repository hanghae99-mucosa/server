package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.layer.dto.user.SignupRequestDto;
import com.hanghae99.mocosa.layer.dto.user.SignupResponseDto;
import com.hanghae99.mocosa.layer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/api/signup")
    public ResponseEntity<SignupResponseDto> registerUser(@RequestBody SignupRequestDto signupRequestDto){
        SignupResponseDto signupResponseDto = userService.registerUser(signupRequestDto);
        return new ResponseEntity<>(signupResponseDto, HttpStatus.OK);
    }
}
