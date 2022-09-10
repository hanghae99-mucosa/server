package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.config.auth.UserDetailsImpl;
import com.hanghae99.mocosa.layer.dto.notify.NotifyRequestDto;
import com.hanghae99.mocosa.layer.dto.notify.NotifyResponseDto;
import com.hanghae99.mocosa.layer.model.UserRoleEnum;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import com.hanghae99.mocosa.layer.service.RestockNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Controller
@RequiredArgsConstructor
public class RestockNotificationController {

    private final RestockNotificationService restockNotificationService;
    private final UserRepository userRepository;

    @PostMapping("/api/notification")
    @ResponseBody
    public ResponseEntity<NotifyResponseDto> createNotify(@RequestBody NotifyRequestDto notifyRequestDto ,
                                                          @AuthenticationPrincipal UserDetailsImpl  userDetails) {

        NotifyResponseDto result = restockNotificationService.createNotify(notifyRequestDto, userDetails.getUser());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @DeleteMapping("/api/notification")
    @ResponseBody
    public ResponseEntity<NotifyResponseDto> deleteNotify(@RequestBody NotifyRequestDto notifyRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {

        NotifyResponseDto result = restockNotificationService.deleteNotify(notifyRequestDto, userDetails.getUser());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@AuthenticationPrincipal UserDetailsImpl  userDetails) {
        if (userDetails.getRole() == UserRoleEnum.ADMIN){
            return null;
        }
        return restockNotificationService.subscribe(userDetails.getUser());
    }
}
