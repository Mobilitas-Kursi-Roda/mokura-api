package com.mokura.mokura_api.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.util.JWTUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class ApiJWTInterceptor implements HandlerInterceptor {

    private final JWTUtil jwtUtil;

    public ApiJWTInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");
        if (authorization == null) {
            BaseResponseDto responseDto = new BaseResponseDto();
            responseDto.setMessage("Unauthorized");
            unauthorized(responseDto, response);
            return false;
        }
        String jwtToken = authorization.substring("Bearer ".length());
        try {
            jwtUtil.parseToken(jwtToken);
        }catch (Exception e){
            BaseResponseDto responseDto = new BaseResponseDto();
            responseDto.setMessage("Unauthorized");
            responseDto.setData("Invalid token");
            unauthorized(responseDto, response);
            return false;
        }
        Claims claims = jwtUtil.parseToken(jwtToken);
        User user = new User();
        user.setUsername(claims.getSubject());
        user.setEmail(claims.get("email", String.class));
        if (!jwtUtil.isTokenValid(jwtToken, user)){
            BaseResponseDto responseDto = new BaseResponseDto();
            responseDto.setMessage("Unauthorized");
            unauthorized(responseDto, response);
            return false;
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    public HttpServletResponse unauthorized(BaseResponseDto baseResponseDto, HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(convertObjecttoJson(baseResponseDto));
        return response;
    }

    public String convertObjecttoJson(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
