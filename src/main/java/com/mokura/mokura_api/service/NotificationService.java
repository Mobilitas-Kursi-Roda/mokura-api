package com.mokura.mokura_api.service;

import com.mokura.mokura_api.dto.ResGetNotifDto;
import com.mokura.mokura_api.entity.User;

import java.util.List;

public interface NotificationService {
    Boolean sendNotificationToUser(User user, String title, String body, String payload);
    List<ResGetNotifDto> getList();
}
