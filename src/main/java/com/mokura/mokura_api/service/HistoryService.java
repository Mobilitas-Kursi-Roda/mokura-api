package com.mokura.mokura_api.service;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.dto.ResGetHistoryClientDto;
import com.mokura.mokura_api.entity.History;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface HistoryService {
    ResponseEntity<BaseResponseDto<List<History>>> getAllHistory();
    ResponseEntity<BaseResponseDto<List<History>>> getHistoryByDevice(Long idDevice);
    ResponseEntity<BaseResponseDto<List<History>>> getHistoryByDeviceDateBetween(Long idDevice, LocalDateTime startDate, LocalDateTime endDate);
    ResponseEntity<BaseResponseDto<List<History>>> getHistoryByDeviceDateAfter(Long idDevice, LocalDateTime startDate);
    ResponseEntity<BaseResponseDto<Map<Object, Object>>> getDetailHistoryById(Long id);
    ResponseEntity<BaseResponseDto<List<ResGetHistoryClientDto>>> getByUserId(Long userId);
}
