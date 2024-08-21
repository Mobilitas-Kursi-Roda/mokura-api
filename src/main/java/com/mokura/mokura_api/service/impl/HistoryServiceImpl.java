package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.Device;
import com.mokura.mokura_api.entity.DeviceRecord;
import com.mokura.mokura_api.entity.History;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.DeviceRecordRepository;
import com.mokura.mokura_api.repository.DeviceRepository;
import com.mokura.mokura_api.repository.HistoryRepository;
import com.mokura.mokura_api.service.HistoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceRecordRepository deviceRecordRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository, DeviceRepository deviceRepository, DeviceRecordRepository deviceRecordRepository) {
        this.historyRepository = historyRepository;
        this.deviceRepository = deviceRepository;
        this.deviceRecordRepository = deviceRecordRepository;
    }

    @Override
    public ResponseEntity<BaseResponseDto<List<History>>> getAllHistory() {
        return null;
    }

    @Override
    public ResponseEntity<BaseResponseDto<List<History>>> getHistoryByDevice(Long idDevice) {
        Device device = _getDeviceById(idDevice);
        List<History> histories = historyRepository.getHistoriesByDevice(device);
        return ResponseEntity.ok(new BaseResponseDto<>("success", histories));
    }

    @Override
    public ResponseEntity<BaseResponseDto<List<History>>> getHistoryByDeviceDateBetween(Long idDevice, LocalDateTime startDate, LocalDateTime endDate) {
        Device device = _getDeviceById(idDevice);
        List<History> histories = historyRepository.findHistoriesBetween(device, startDate, endDate);
        return ResponseEntity.ok(new BaseResponseDto<>("success", histories));
    }

    @Override
    public ResponseEntity<BaseResponseDto<List<History>>> getHistoryByDeviceDateAfter(Long idDevice, LocalDateTime startDate) {
        Device device = _getDeviceById(idDevice);
        List<History> histories = historyRepository.findHistoriesStart(device, startDate);
        return ResponseEntity.ok(new BaseResponseDto<>("success", histories));
    }

    @Override
    public ResponseEntity<BaseResponseDto<Map<Object, Object>>> getDetailHistoryById(Long id) {
        Optional<History> history = historyRepository.findById(id);
        if(history.isEmpty()) throw new CustomBadRequestException("History id not found");

        List<DeviceRecord> deviceRecords = deviceRecordRepository.getAllByDeviceAndTimeBetween(
                history.get().getDevice().getId_device(), history.get().getStart_date(), history.get().getEnd_date());

        Map<Object, Object> result = new LinkedHashMap<>();

        result.put("history", history.get());
        result.put("deviceRecords", deviceRecords);

        return ResponseEntity.ok(new BaseResponseDto<>("success", result));
    }

    private Device _getDeviceById(Long idDevice) {
        Optional<Device> device = deviceRepository.findById(idDevice);
        if (device.isEmpty()) {
            throw new CustomBadRequestException("Device not found");
        }

        return device.get();
    }
}
