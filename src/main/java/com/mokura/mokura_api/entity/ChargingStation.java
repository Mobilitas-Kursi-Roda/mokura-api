package com.mokura.mokura_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "charging_stations")
public class ChargingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_charging_station;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private String thumbnail;
    private String url;
}
