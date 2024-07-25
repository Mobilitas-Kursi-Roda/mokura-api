package com.mokura.mokura_api.controller.api.admin;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.Device;
import com.mokura.mokura_api.repository.DeviceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/device")
public class DeviceControllerAdmin {

    private final DeviceRepository deviceRepository;

    public DeviceControllerAdmin(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping
    public ResponseEntity<BaseResponseDto<List<Device>>> getDevices() {
        List<Device> devices = deviceRepository.findAll();
        return ResponseEntity.ok(new BaseResponseDto<>("success", devices));
    }
}
