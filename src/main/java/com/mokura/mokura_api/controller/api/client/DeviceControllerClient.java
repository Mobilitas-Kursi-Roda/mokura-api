package com.mokura.mokura_api.controller.api.client;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.Device;
import com.mokura.mokura_api.service.DeviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/client/devices")
public class DeviceControllerClient {

    private final DeviceService deviceService;

    public DeviceControllerClient(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/bluetooth")
    public ResponseEntity<BaseResponseDto<Device>> getByBluetooth(
            @RequestParam String bluetooth_id
    ) {
        return ResponseEntity.ok(new BaseResponseDto<>("success", deviceService.getDeviceByBluetooth(bluetooth_id)));
    }
}
