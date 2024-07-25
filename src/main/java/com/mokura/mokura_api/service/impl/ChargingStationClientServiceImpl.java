package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.ChargingStation;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.ChargingStationRepository;
import com.mokura.mokura_api.service.ChargingStationClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChargingStationClientServiceImpl implements ChargingStationClientService {

    final ChargingStationRepository chargingStationRepository;

    private final HttpServletRequest httpServletRequest;

    public ChargingStationClientServiceImpl(ChargingStationRepository chargingStationRepository, HttpServletRequest httpServletRequest) {
        this.chargingStationRepository = chargingStationRepository;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ResponseEntity<BaseResponseDto<List<ChargingStation>>> getAll() {
        List<ChargingStation> chargingStations = chargingStationRepository.findAll();
        // Getting the base URL
        String baseUrl = String.format("%s://%s:%d", httpServletRequest.getScheme(), httpServletRequest.getServerName(), httpServletRequest.getServerPort());

        chargingStations.forEach((chargingStation) -> {
            chargingStation.setThumbnail(baseUrl+"/"+chargingStation.getThumbnail());
        });
        return ResponseEntity.ok(
                new BaseResponseDto<>("success", chargingStations)
        );
    }

    @Override
    public ResponseEntity<BaseResponseDto<ChargingStation>> getById(Long id) {
        Optional<ChargingStation> chargingStation = chargingStationRepository.findById(id);
        if (!chargingStation.isPresent()) {
            throw new CustomBadRequestException("Charging station not found");
        }
        return ResponseEntity.ok(new BaseResponseDto<>("success", chargingStation.get()));
    }
}
