package com.mokura.mokura_api.repository;

import com.mokura.mokura_api.entity.ChargingStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChargingStationRepository extends JpaRepository<ChargingStation, Long> {

    @Query(value = "SELECT cs, (6371 * acos(cos(radians(:latitude)) * cos(radians(CAST(cs.latitude AS double))) * cos(radians(CAST(cs.longitude AS double)) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(CAST(cs.latitude AS double))))) AS distance " +
            "FROM ChargingStation cs " +
            "ORDER BY distance ASC")
    List<ChargingStation> findAllOrderByDistance(@Param("latitude") double latitude, @Param("longitude") double longitude);

}
