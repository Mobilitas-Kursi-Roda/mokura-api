package com.mokura.mokura_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_device;
    private String device_name;
    private String bluetooth_id;
    private String status;
    private String latitude;
    private String longitude;
    private String battery;
    private Instant last_update;
}
