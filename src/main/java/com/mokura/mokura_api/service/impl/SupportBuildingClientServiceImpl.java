package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.SupportBuilding;
import com.mokura.mokura_api.repository.SupportBuildingRepository;
import com.mokura.mokura_api.service.SupportBuildingClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportBuildingClientServiceImpl implements SupportBuildingClientService {

    private final SupportBuildingRepository supportBuildingRepository;
    private final HttpServletRequest httpServletRequest;

    public SupportBuildingClientServiceImpl(SupportBuildingRepository supportBuildingRepository, HttpServletRequest httpServletRequest) {
        this.supportBuildingRepository = supportBuildingRepository;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ResponseEntity<BaseResponseDto<List<SupportBuilding>>> getSupportBuildings() {
        List<SupportBuilding> supportBuildings = supportBuildingRepository.findAll();
        // Getting the base URL
        String baseUrl = String.format("%s://%s:%d", httpServletRequest.getScheme(), httpServletRequest.getServerName(), httpServletRequest.getServerPort());

        supportBuildings.forEach((supportBuilding) -> {
            supportBuilding.setThumbnail(baseUrl+"/"+supportBuilding.getThumbnail());
        });
        return ResponseEntity.ok(
                new BaseResponseDto<>("success", supportBuildings)
        );
    }

    @Override
    public ResponseEntity<BaseResponseDto<SupportBuilding>> getSupportBuildingById(Long id) {
        SupportBuilding supportBuilding = supportBuildingRepository.findById(id).orElse(null);
        return ResponseEntity.ok(
                new BaseResponseDto<>("success", supportBuilding)
        );
    }
}
