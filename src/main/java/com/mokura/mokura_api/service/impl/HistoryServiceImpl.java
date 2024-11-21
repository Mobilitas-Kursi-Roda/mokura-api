package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.dto.ResGetHistoryClientDto;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
        List<DeviceRecord> filteredDeviceRecords = deviceRecords.stream()
                .filter(dr -> !dr.getLatitude().isEmpty() && !dr.getLongitude().isEmpty())
                .sorted(Comparator.comparing(DeviceRecord::getCreated_at))
                .toList();

        Map<Object, Object> result = new LinkedHashMap<>();

        result.put("history", history.get());
        result.put("deviceRecords", filteredDeviceRecords);

        return ResponseEntity.ok(new BaseResponseDto<>("success", result));
    }

    @Override
    public ResponseEntity<BaseResponseDto<List<ResGetHistoryClientDto>>> getByUserId(Long userId) {
        List<History> historyList = historyRepository.findByIdUser(userId);

        DateTimeFormatter fullDateFormatter = DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy HH:mm", Locale.forLanguageTag("id-ID"));
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.forLanguageTag("id-ID"));

        List<ResGetHistoryClientDto> responseList = historyList.stream().map(history -> {
            ResGetHistoryClientDto resGetHistoryClientDto = new ResGetHistoryClientDto();
            resGetHistoryClientDto.setDevice_name(history.getDevice().getDevice_name());

            // Format start_date
            resGetHistoryClientDto.setStart_date(history.getStart_date().format(fullDateFormatter));

            // Format end_date: cek apakah tanggal sama dengan start_date
            if (history.getEnd_date().toLocalDate().equals(history.getStart_date().toLocalDate())) {
                // Jika hari yang sama, gunakan format jam saja
                resGetHistoryClientDto.setEnd_date(history.getEnd_date().format(timeFormatter));
            } else {
                // Jika berbeda, gunakan format lengkap
                resGetHistoryClientDto.setEnd_date(history.getEnd_date().format(fullDateFormatter));
            }

            // Set duration dalam menit jika lebih dari atau sama dengan 60 detik
            long durationInSeconds = history.getDuration();
            if (durationInSeconds >= 60) {
                long durationInMinutes = durationInSeconds / 60;
                resGetHistoryClientDto.setDuration(durationInMinutes + " menit");
            } else {
                resGetHistoryClientDto.setDuration(durationInSeconds + " detik");
            }

            return resGetHistoryClientDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(new BaseResponseDto<>("success", responseList));
    }


    private Device _getDeviceById(Long idDevice) {
        Optional<Device> device = deviceRepository.findById(idDevice);
        if (device.isEmpty()) {
            throw new CustomBadRequestException("Device not found");
        }

        return device.get();
    }
}
