package com.mokura.mokura_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqSendSensorDeviceWsDto {
    private Long user_id;
    private Long device_id;
    private double speed;
    private double rpm;
    private double battery;
    private String latitude;
    private String longitude;
    private Date created_at;
}
