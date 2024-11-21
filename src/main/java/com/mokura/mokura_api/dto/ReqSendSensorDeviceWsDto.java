package com.mokura.mokura_api.dto;

import com.google.type.DateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqSendSensorDeviceWsDto {
    private Long user_id;
    private Long device_id;
    private String device_name;
    private double speed;
    private double rpm;
    private double battery;
    private String latitude;
    private String longitude;
    private String created_at;
    private String username = null;
}
