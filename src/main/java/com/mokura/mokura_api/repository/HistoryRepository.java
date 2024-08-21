package com.mokura.mokura_api.repository;

import com.mokura.mokura_api.entity.Device;
import com.mokura.mokura_api.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> getHistoriesByDevice(Device device);

    @Query("SELECT h FROM History h WHERE h.device = :device AND h.start_date >= :startDate AND h.end_date <= :endDate")
    List<History> findHistoriesBetween(@Param("device") Device device,@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT h FROM History h WHERE h.device = :device AND h.start_date >= :startDate")
    List<History> findHistoriesStart(@Param("device") Device device, @Param("startDate") LocalDateTime startDate);
}
