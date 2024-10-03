package com.mokura.mokura_api.service;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.dto.ResChargingStationDto;
import com.mokura.mokura_api.entity.ChargingStation;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChargingStationClientService {
    ResponseEntity<BaseResponseDto<List<ResChargingStationDto>>> getAll(double latitude, double longitude);
    ResponseEntity<BaseResponseDto<ChargingStation>> getById(Long id);
}
