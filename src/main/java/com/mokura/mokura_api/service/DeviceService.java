package com.mokura.mokura_api.service;

import com.mokura.mokura_api.entity.Device;

public interface DeviceService {
    Device getDeviceByBluetooth(String bluetoothId);
}
