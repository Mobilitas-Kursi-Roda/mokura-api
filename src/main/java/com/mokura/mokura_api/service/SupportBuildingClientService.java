package com.mokura.mokura_api.service;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.SupportBuilding;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface SupportBuildingClientService {
    ResponseEntity<BaseResponseDto<List<SupportBuilding>>> getSupportBuildings();
    ResponseEntity<BaseResponseDto<SupportBuilding>> getSupportBuildingById(Long id);
}
