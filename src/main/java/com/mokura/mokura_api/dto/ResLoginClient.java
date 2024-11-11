package com.mokura.mokura_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResLoginClient {
    private Long user_id;
    private String username;
    private String fullname;
    private String email;
    private String token;
    private String emergency_contact;
    private Date expire;
}
