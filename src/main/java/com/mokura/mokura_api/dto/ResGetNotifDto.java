package com.mokura.mokura_api.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResGetNotifDto {
    private Long emergency_notif_id;
    private Long user_id;
    private String user_full_name;
    private String user_phone_number;
    private String user_emergency_contact;
    private String latitude;
    private String longitude;
    private String device_id;
    private String device_name;
    private String device_battery;
    private boolean is_using_mokura = false;
    private String created_at;
}
