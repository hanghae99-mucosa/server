package com.hanghae99.mocosa.layer.controller;

import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.AlarmException;
import com.hanghae99.mocosa.layer.dto.notify.NotifyRequestDto;
import com.hanghae99.mocosa.layer.dto.notify.NotifyResponseDto;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import com.hanghae99.mocosa.layer.service.RestockNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestockNotificationApiController {

    private final RestockNotificationService restockNotificationService;
    private final UserRepository userRepository;

    @PostMapping("/api/notification")
    public ResponseEntity<NotifyResponseDto> createNotify(@RequestBody NotifyRequestDto notifyRequestDto ,
                                                          @AuthenticationPrincipal UserDetailsImpl  userDetails) {

        NotifyResponseDto result = restockNotificationService.createNotify(notifyRequestDto, userDetails.getUser());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @DeleteMapping("/api/notification")
    public ResponseEntity<NotifyResponseDto> deleteNotify(@RequestBody NotifyRequestDto notifyRequestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl  userDetails) {

        NotifyResponseDto result = restockNotificationService.deleteNotify(notifyRequestDto, userDetails.getUser());

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
