package com.mokura.mokura_api.controller.api.client;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.EmergencyNotif;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.EmergencyNotifRepository;
import com.mokura.mokura_api.repository.UserRepository;
import com.mokura.mokura_api.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/client/notification")
public class NotificationClientController {

    private final UserRepository userRepository;
    private final EmergencyNotifRepository emergencyNotifRepository;

    private final NotificationService notificationService;

    public NotificationClientController(UserRepository userRepository, EmergencyNotifRepository emergencyNotifRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.emergencyNotifRepository = emergencyNotifRepository;
        this.notificationService = notificationService;
    }

    @PostMapping("/send-token")
    public ResponseEntity<BaseResponseDto<Object>> sendToken(@RequestParam("token") String token, @RequestParam("user_id") Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setTokenFcm(token);
            userRepository.save(user);
        });
        return ResponseEntity.ok(new BaseResponseDto<>("Success", null));
    }

    @PostMapping("/send-token-admin")
    public ResponseEntity<BaseResponseDto<Object>> sendTokenAdmin(@RequestParam("token") String token) {
        userRepository.findByEmail("admin@mail.com").ifPresent(user->{
            System.out.println("ada");
            user.setTokenFcm(token);
            userRepository.save(user);
        });
        return ResponseEntity.ok(new BaseResponseDto<>("Success", null));
    }

    @PostMapping("/send-message")
    public void sendMessage(@RequestParam("title") String title, @RequestParam("message") String message, @RequestParam("user_id") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        System.out.println(userId);
        user.ifPresent(value -> notificationService.sendNotificationToUser(value, title, message, "1"));
        if (user.isEmpty()) log.error("send notification: user not found");
    }

    @PostMapping("/emergency")
    public ResponseEntity<BaseResponseDto<Object>> sendEmergency(
            @RequestParam("title") String title,
            @RequestParam("message") String message,
            @RequestParam("lat") String lat,
            @RequestParam("lng") String lng,
            @RequestParam("user_id") Long user_id,
            @RequestParam("is_using_mokura") boolean is_using_mokura,
            @RequestParam(value = "device_id", required = false) String device_id
    ) {
        Optional<User> user = userRepository.findById(user_id);
        if (user.isEmpty()) throw new CustomBadRequestException("user_id not found");
        EmergencyNotif emergencyNotif = new EmergencyNotif();
        emergencyNotif.setUser(user.get());
        emergencyNotif.setLongitude(lng);
        emergencyNotif.setLatitude(lat);
        emergencyNotif.set_using_mokura(is_using_mokura);
        emergencyNotif.setDevice_id(device_id);
        emergencyNotifRepository.save(emergencyNotif);
        Optional<User> admin = userRepository.findByEmail("admin@mail.com");
        admin.ifPresent(value -> notificationService.sendNotificationToUser(value, title, message, "notif id"));
        return ResponseEntity.ok(new BaseResponseDto<>("Success", null));
    }
}
