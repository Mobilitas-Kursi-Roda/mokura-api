package com.mokura.mokura_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_user;
    private String username;

    @JsonIgnore
    private String password;
    private String email;
    private String full_name;
    private String phone_number;
    private String address;
    private String emergency_contact;
    private String picture;
    private String tokenFcm;
    private String latitude;
    private String longitude;

}
