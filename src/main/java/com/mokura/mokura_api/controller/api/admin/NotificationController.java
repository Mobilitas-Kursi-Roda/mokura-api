package com.mokura.mokura_api.controller.api.admin;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin/notification")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<BaseResponseDto<Object>> getAll()
    {
        return ResponseEntity.ok(new BaseResponseDto<>("success", notificationService.getList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<Object>> getAll(
            @PathVariable("id") Long id
    )
    {
        return ResponseEntity.ok(new BaseResponseDto<>("success", notificationService.getDetail(id)));
    }
}
