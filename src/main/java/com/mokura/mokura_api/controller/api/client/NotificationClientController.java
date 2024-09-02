package com.mokura.mokura_api.controller.api.client;

import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.repository.UserRepository;
import com.mokura.mokura_api.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
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

    private final NotificationService notificationService;

    public NotificationClientController(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @PostMapping("/send-token")
    public void sendToken(@RequestParam("token") String token, @RequestParam("user_id") Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setTokenFcm(token);
            userRepository.save(user);
        });
    }

    @PostMapping("/send-token-admin")
    public void sendTokenAdmin(@RequestParam("token") String token) {
        userRepository.findByEmail("admin@mail.com").ifPresent(user->{
            user.setTokenFcm(token);
            userRepository.save(user);
        });
    }

    @PostMapping("/send-message")
    public void sendMessage(@RequestParam("title") String title, @RequestParam("message") String message, @RequestParam("user_id") Long userId) {
        Optional<User> user = userRepository.findById(userId);
        System.out.println(userId);
        user.ifPresent(value -> notificationService.sendNotificationToUser(value, title, message, "1"));
        if (user.isEmpty()) log.error("send notification: user not found");
    }
}
