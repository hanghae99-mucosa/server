package com.hanghae99.mocosa.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hanghae99.mocosa.config.auth.UserDetailsImpl;
import com.hanghae99.mocosa.layer.dto.user.SigninRequestDto;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/signin");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        SigninRequestDto signinRequestdto = getSigninRequestDto(request);

        // 유저네임패스워드 토큰 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        signinRequestdto.getEmail(),
                        signinRequestdto.getPassword());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
                .withClaim("email", userDetails.getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        JSONObject responseJson = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        responseJson.put("token", JwtProperties.TOKEN_PREFIX + jwtToken);
        responseJson.put("email", userDetails.getUsername());
        response.getWriter().print(responseJson);
    }


    private SigninRequestDto getSigninRequestDto(HttpServletRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        SigninRequestDto signinRequestdto = null;
        try {
            signinRequestdto = mapper.readValue(request.getInputStream(), SigninRequestDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return signinRequestdto;
    }
}
