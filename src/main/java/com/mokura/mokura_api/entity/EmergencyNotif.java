package com.mokura.mokura_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "emergency_notif")
public class EmergencyNotif {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long emergency_notif_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    private String latitude;
    private String longitude;
    private String device_id;
    private boolean is_using_mokura = false;
}
