package com.mokura.mokura_api.repository;

import com.mokura.mokura_api.entity.DeviceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface DeviceRecordRepository extends JpaRepository<DeviceRecord, Long> {

    @Query("SELECT d FROM DeviceRecord d WHERE d.deviceId = :deviceId AND d.created_at >= :startTime AND d.created_at <= :endTime")
    List<DeviceRecord> getAllByDeviceAndTimeBetween(Long deviceId, LocalDateTime startTime, LocalDateTime endTime);
}
