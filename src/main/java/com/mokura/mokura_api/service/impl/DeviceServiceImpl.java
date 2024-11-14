package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.entity.Device;
import com.mokura.mokura_api.repository.DeviceRepository;
import com.mokura.mokura_api.service.DeviceService;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Device getDeviceByBluetooth(String bluetoothId) {
        return deviceRepository.findByBluetooth(bluetoothId);
    }
}
