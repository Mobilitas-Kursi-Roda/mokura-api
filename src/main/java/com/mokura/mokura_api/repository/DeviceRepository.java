package com.mokura.mokura_api.repository;

import com.mokura.mokura_api.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
