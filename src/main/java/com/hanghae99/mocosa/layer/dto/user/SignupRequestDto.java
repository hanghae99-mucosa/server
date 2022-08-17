package com.hanghae99.mocosa.layer.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    String email;
    String password;
    String passwordCheck;
    Boolean admin;

    public SignupRequestDto() {
        admin = false;
    }
}
