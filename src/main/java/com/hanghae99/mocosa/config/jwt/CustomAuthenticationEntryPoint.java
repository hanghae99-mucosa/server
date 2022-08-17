//package com.hanghae99.mocosa.config.jwt;
//
//import com.hanghae99.mocosa.config.exception.code.ErrorCode;
//import org.json.JSONObject;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Component
//public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
//        if(isLoginError(ex.getMessage())){
//            JSONObject responseJson = new JSONObject();
//            responseJson.put("message", ErrorCode.SIGNIN_NO_DATA.getErrorMessage());
//            response.setStatus(HttpStatus.BAD_REQUEST.value());
//            response.setContentType("application/json");
//            response.getWriter().print(responseJson);
//        }
//    }
//
//    private Boolean isLoginError(String msg){
//        if(msg.equals("Full authentication is required to access this resource"))
//            return true;
//        return false;
//    }
//}
