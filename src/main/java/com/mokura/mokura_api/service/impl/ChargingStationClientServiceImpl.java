package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.dto.ResChargingStationDto;
import com.mokura.mokura_api.entity.ChargingStation;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.ChargingStationRepository;
import com.mokura.mokura_api.service.ChargingStationClientService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public ResponseEntity<BaseResponseDto<List<ResChargingStationDto>>> getAll(double latitude, double longitude) {
        List<ChargingStation> chargingStations = chargingStationRepository.findAllOrderByDistance(latitude, longitude);
        // Getting the base URL
        String baseUrl = String.format("%s://%s:%d", httpServletRequest.getScheme(), httpServletRequest.getServerName(), httpServletRequest.getServerPort());
        List<ResChargingStationDto> resChargingStationDtoList = new ArrayList<>();

        chargingStations.forEach((chargingStation) -> {
            // Update thumbnail URL
            chargingStation.setThumbnail(baseUrl + "/" + chargingStation.getThumbnail());

            // Create DTO object and set attributes
            ResChargingStationDto res = new ResChargingStationDto();
            res.setId_charging_station(chargingStation.getId_charging_station());
            res.setName(chargingStation.getName());
            res.setAddress(chargingStation.getAddress());
            res.setLatitude(chargingStation.getLatitude());
            res.setLongitude(chargingStation.getLongitude());
            res.setThumbnail(chargingStation.getThumbnail());
            res.setUrl(chargingStation.getUrl());

            // Calculate distance in meters
            double distance = calculateDistanceInMeters(
                    latitude, longitude,
                    Double.parseDouble(chargingStation.getLatitude()),
                    Double.parseDouble(chargingStation.getLongitude())
            );

            // Format distance based on value
            if (distance < 1000) {
                res.setDistance(String.format("%.0fm", distance));  // Show as meters
            } else {
                res.setDistance(String.format("%.2fkm", distance / 1000));  // Show as kilometers
            }

            // Add to response list
            resChargingStationDtoList.add(res);
        });


        return ResponseEntity.ok(
                new BaseResponseDto<>("success", resChargingStationDtoList)
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

    public static double calculateDistanceInMeters(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Radius of the Earth in meters
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // Distance in meters

        return distance;
    }
}
