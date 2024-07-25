package com.mokura.mokura_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "support_buildings")
public class SupportBuilding {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_support_building;
    private String name;
    private String address;
    private String latitude;
    private String longitude;
    private String thumbnail;
    private String url;
}
