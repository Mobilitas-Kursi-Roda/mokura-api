package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.entity.DeviceRecord;
import com.mokura.mokura_api.repository.DeviceRecordRepository;
import com.mokura.mokura_api.service.DeviceRecordService;
import org.springframework.stereotype.Service;

@Service
public class DeviceRecordServiceImpl implements DeviceRecordService {

    private final DeviceRecordRepository deviceRecordRepository;

    public DeviceRecordServiceImpl(DeviceRecordRepository deviceRecordRepository) {
        this.deviceRecordRepository = deviceRecordRepository;
    }

    @Override
    public void saveDeviceRecord(DeviceRecord deviceRecord) {
        deviceRecordRepository.save(deviceRecord);
    }
}
