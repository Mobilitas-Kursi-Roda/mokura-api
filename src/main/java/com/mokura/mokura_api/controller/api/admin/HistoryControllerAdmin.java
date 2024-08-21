package com.mokura.mokura_api.controller.api.admin;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.Device;
import com.mokura.mokura_api.entity.History;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.DeviceRepository;
import com.mokura.mokura_api.repository.UserRepository;
import com.mokura.mokura_api.service.HistoryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/histories")
public class HistoryControllerAdmin {

    private final HistoryService historyService;

    public HistoryControllerAdmin(HistoryService historyService, UserRepository userRepository, DeviceRepository deviceRepository) {
        this.historyService = historyService;
    }

    @GetMapping()
    public ResponseEntity<BaseResponseDto<List<History>>> getHistory(
            @RequestParam(name = "deviceId", required = true) Long deviceId,
            @RequestParam(name = "startDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam(name = "endDate", required = false) LocalDateTime endDate
    ){
        if(startDate == null && endDate == null){
            return historyService.getHistoryByDevice(deviceId);
        }
        if(startDate != null && endDate == null){
            return  historyService.getHistoryByDeviceDateAfter(deviceId, startDate);
        }
        if(startDate != null && endDate != null){
            return historyService.getHistoryByDeviceDateBetween(deviceId, startDate, endDate);
        }
        return null;
    }

    @GetMapping("/get-details/{idHistory}")
    public ResponseEntity<BaseResponseDto<Map<Object, Object>>> getDetails(@PathVariable("idHistory") Long idHistory){
        return historyService.getDetailHistoryById(idHistory);
    }
}
