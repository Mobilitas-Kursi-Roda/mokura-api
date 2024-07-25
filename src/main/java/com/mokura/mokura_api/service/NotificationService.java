package com.mokura.mokura_api.service;

import com.mokura.mokura_api.entity.User;

import java.util.Map;

public interface NotificationService {
    Boolean sendNotificationToUser(User user, String title, String body);
}
