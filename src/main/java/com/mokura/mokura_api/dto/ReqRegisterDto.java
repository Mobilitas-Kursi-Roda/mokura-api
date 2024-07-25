package com.mokura.mokura_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReqRegisterDto {
    private String email;
    private String username;
    private String password;
    private String full_name;
    private String phone;
}
