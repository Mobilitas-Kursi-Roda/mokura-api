package com.mokura.mokura_api.controller.api.client;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.SupportBuilding;
import com.mokura.mokura_api.service.SupportBuildingClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/client/support-buildings")
public class SupportBuildingClientController {

    private final SupportBuildingClientService supportBuildingClientService;

    public SupportBuildingClientController(SupportBuildingClientService supportBuildingClientService) {
        this.supportBuildingClientService = supportBuildingClientService;
    }

    @GetMapping
    public ResponseEntity<BaseResponseDto<List<SupportBuilding>>> getSupportBuildings() {
        return supportBuildingClientService.getSupportBuildings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<SupportBuilding>> getSupportBuildingById(@PathVariable("id") Long id) {
        return supportBuildingClientService.getSupportBuildingById(id);
    }
}
