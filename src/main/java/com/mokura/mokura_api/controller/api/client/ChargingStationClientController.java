package com.mokura.mokura_api.controller.api.client;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.dto.ResChargingStationDto;
import com.mokura.mokura_api.entity.ChargingStation;
import com.mokura.mokura_api.service.ChargingStationClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client/charging-stations")
public class ChargingStationClientController {

    final ChargingStationClientService chargingStationClientService;

    public ChargingStationClientController(ChargingStationClientService chargingStationClientService) {
        this.chargingStationClientService = chargingStationClientService;
    }

    @GetMapping
    public ResponseEntity<BaseResponseDto<List<ResChargingStationDto>>> getChargingStationss(
            @RequestParam double latitude,
            @RequestParam double longitude
    ) {
        return chargingStationClientService.getAll(latitude, longitude);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<ChargingStation>> getChargingStationsById(@PathVariable("id") Long id) {
        return chargingStationClientService.getById(id);
    }
}
