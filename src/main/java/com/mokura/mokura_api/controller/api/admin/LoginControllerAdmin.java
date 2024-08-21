package com.mokura.mokura_api.controller.api.admin;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.dto.ReqLoginDto;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.repository.UserRepository;
import com.mokura.mokura_api.util.JWTUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/login")
public class LoginControllerAdmin {

    private final JWTUtil jwtUtil;

    private final UserRepository userRepository;

    public LoginControllerAdmin(UserRepository userRepository, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<BaseResponseDto<Object>> login(@RequestBody ReqLoginDto request){
//       Optional<User> user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail());
        User user = new User();
        user.setUsername("admin");
        user.setEmail("admin@mail.com");
        user.setPassword("admin");
        if(!user.getUsername().equals(request.getUsername())  && !user.getPassword().equals(request.getPassword())){
            return ResponseEntity.badRequest().body(new BaseResponseDto<>(
                    "login failed", null
            ));
        }
        String token = jwtUtil.generateToken(user);
        Map<String, Object> respMap = new HashMap();
        respMap.put("token", token);
        return ResponseEntity.ok(new BaseResponseDto<>(
                "login success", respMap
        ));

    }

}
