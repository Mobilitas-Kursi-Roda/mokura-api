package com.mokura.mokura_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "device_records")
public class DeviceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id_device_record;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "id_user", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "id_device", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private Device device;

    @Column(name = "id_user", nullable = false)
    private Long userId;

    @Column(name = "id_device", nullable = false)
    private Long deviceId;

    private Date created_at;

    private String latitude;
    private String longitude;
    private double speed;
    private double battery;
    private double rpm;

}
