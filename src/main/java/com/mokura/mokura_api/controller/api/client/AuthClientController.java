package com.mokura.mokura_api.controller.api.client;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.dto.ReqLoginDto;
import com.mokura.mokura_api.dto.ReqRegisterDto;
import com.mokura.mokura_api.dto.ResLoginClient;
import com.mokura.mokura_api.service.AuthClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/client/auth")
@Slf4j
public class AuthClientController {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final AuthClientService authClientService;

    public AuthClientController(AuthClientService authClientService) {
        this.authClientService = authClientService;
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<ResLoginClient>> login(@RequestBody ReqLoginDto reqLoginDto){
        log.info("login request: {}", reqLoginDto);
        return authClientService.login(reqLoginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDto<Object>> register(@RequestBody ReqRegisterDto reqRegisterDto){
        return authClientService.register(reqRegisterDto);
    }

    @PostMapping("/is-exist")
    public ResponseEntity<BaseResponseDto<Boolean>> checkUsernameEmail(@RequestBody Map<String, Object> req){
        return authClientService.checkUsernameOrEmail(req.get("key").toString());
    }
}
