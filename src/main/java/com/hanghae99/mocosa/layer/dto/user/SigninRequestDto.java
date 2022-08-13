package com.hanghae99.mocosa.layer.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequestDto {
    String email;
    String password;
}
