package com.mokura.mokura_api.service;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.dto.ReqLoginDto;
import com.mokura.mokura_api.dto.ReqRegisterDto;
import com.mokura.mokura_api.dto.ResLoginClient;
import org.springframework.http.ResponseEntity;

public interface AuthClientService {
    ResponseEntity<BaseResponseDto<ResLoginClient>> login(ReqLoginDto reqLoginDto);
    ResponseEntity<BaseResponseDto<Object>> register(ReqRegisterDto reqRegisterDto);
    ResponseEntity<BaseResponseDto<Boolean>> checkUsernameOrEmail(String key);
}
