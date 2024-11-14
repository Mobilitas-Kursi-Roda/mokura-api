package com.mokura.mokura_api.repository;

import com.mokura.mokura_api.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @Query("SELECT d FROM Device d " +
            "WHERE d.bluetooth_id = :bluetooth_id")
    Device findByBluetooth(@Param("bluetooth_id") String bluetooth_id);
}
