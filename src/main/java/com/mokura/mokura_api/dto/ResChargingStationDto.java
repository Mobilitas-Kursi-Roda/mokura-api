package com.mokura.mokura_api.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResChargingStationDto {
    private Long id_charging_station;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private String thumbnail;
    private String url;
    private String distance;
}
