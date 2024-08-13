package com.mokura.mokura_api.service.impl;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Override
    public Boolean sendNotificationToUser(User user, String title, String body, String payload) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(user.getTokenFcm())
                .setNotification(notification)
                .putData("payload", payload)
                .build();

        System.out.println(user.getTokenFcm());
        try {
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent message: {}", response);
            return true;
        }catch (FirebaseMessagingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
