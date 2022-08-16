package com.hanghae99.mocosa.layer.service;

import com.hanghae99.mocosa.config.exception.code.ErrorCode;
import com.hanghae99.mocosa.config.exception.custom.SignupException;
import com.hanghae99.mocosa.config.jwt.PasswordEncoder;
import com.hanghae99.mocosa.layer.dto.user.SignupRequestDto;
import com.hanghae99.mocosa.layer.dto.user.SignupResponseDto;
import com.hanghae99.mocosa.layer.model.User;
import com.hanghae99.mocosa.layer.model.UserRoleEnum;
import com.hanghae99.mocosa.layer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    public static final String EMAIL_REGEX = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public SignupResponseDto registerUser(SignupRequestDto signupRequestDto) {


        String email = signupRequestDto.getEmail();
        String password = signupRequestDto.getPassword();
        String passwordCheck = signupRequestDto.getPasswordCheck();

        validateSignupRequest(email, password, passwordCheck);

        User user = new User(email,passwordEncoder.encode(password),signupRequestDto.getAdmin());

        userRepository.save(user);
        return new SignupResponseDto("회원가입에 성공했습니다.");
    }

    private void validateSignupRequest(String email, String password, String passwordCheck) {
        User usernameUserFound = userRepository.findByEmail(email);

        if(usernameUserFound!=null){
            throw new SignupException(ErrorCode.SIGNUP_DUPLICATE_ID);
        }

        if(!isValidInput(email,EMAIL_REGEX)){
            throw new SignupException(ErrorCode.SIGNUP_BAD_ID);
        }

        if(!password.equals(passwordCheck)){
            throw new SignupException(ErrorCode.SIGNUP_BAD_PASSWORD);
        }

        if(!isValidInput(password,PASSWORD_REGEX)){
            throw new SignupException(ErrorCode.SIGNUP_BAD_PASSWORD);
        }
    }

    public static boolean isValidInput(String input,String REGEX) {
        boolean err = false;
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(input);
        if(m.matches()) {
            err = true;
        }
        return err;
    }
}
