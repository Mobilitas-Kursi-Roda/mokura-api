package com.mokura.mokura_api.service.impl;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.mokura.mokura_api.dto.ResGetNotifDto;
import com.mokura.mokura_api.entity.EmergencyNotif;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.DeviceRepository;
import com.mokura.mokura_api.repository.EmergencyNotifRepository;
import com.mokura.mokura_api.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final EmergencyNotifRepository emergencyNotifRepository;
    private final DeviceRepository deviceRepository;

    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");

    public NotificationServiceImpl(
            EmergencyNotifRepository emergencyNotifRepository,
            DeviceRepository deviceRepository
    ) {
        this.emergencyNotifRepository = emergencyNotifRepository;
        this.deviceRepository = deviceRepository;
    }

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

    @Override
    public List<ResGetNotifDto> getList() {

        return emergencyNotifRepository.findAll().stream()
                // Sorting berdasarkan created_at secara descending
                .sorted((e1, e2) -> e2.getCreated_at().compareTo(e1.getCreated_at()))
                .map(emergencyNotif -> {
                    // Ambil device hanya jika device_id tidak null
                    String deviceName = "No Device";
                    String battery = null;
                    if (emergencyNotif.getDevice_id() != null) {
                        var device = deviceRepository.findById(Long.parseLong(emergencyNotif.getDevice_id()))
                                .orElse(null);
                        if (device != null) {
                            deviceName = device.getDevice_name();
                            battery = device.getBattery();
                        }
                    }

                    return new ResGetNotifDto(
                            emergencyNotif.getEmergency_notif_id(),
                            emergencyNotif.getUser().getId_user(),
                            emergencyNotif.getUser().getFull_name(),
                            emergencyNotif.getUser().getPhone_number(),
                            emergencyNotif.getUser().getEmergency_contact(),
                            emergencyNotif.getLatitude(),
                            emergencyNotif.getLongitude(),
                            emergencyNotif.getDevice_id(),
                            deviceName,
                            battery,
                            emergencyNotif.is_using_mokura(),
                            dateFormat.format(emergencyNotif.getCreated_at())
                    );
                })
                .toList();
    }

    @Override
    public ResGetNotifDto getDetail(Long emergencyNotificationId) {
        Optional<EmergencyNotif> emergencyNotifOpt = emergencyNotifRepository.findById(emergencyNotificationId);
        if (emergencyNotifOpt.isEmpty()) throw new CustomBadRequestException("Emergency notif with id: "+emergencyNotificationId+" not found");
        EmergencyNotif emergencyNotif = emergencyNotifOpt.get();
        // Ambil device hanya jika device_id tidak null
        String deviceName = "No Device";
        String battery = null;
        if (emergencyNotif.getDevice_id() != null) {
            var device = deviceRepository.findById(Long.parseLong(emergencyNotif.getDevice_id()))
                    .orElse(null);
            if (device != null) {
                deviceName = device.getDevice_name();
                battery = device.getBattery();
            }
        }
        return new ResGetNotifDto(
                emergencyNotif.getEmergency_notif_id(),
                emergencyNotif.getUser().getId_user(),
                emergencyNotif.getUser().getFull_name(),
                emergencyNotif.getUser().getPhone_number(),
                emergencyNotif.getUser().getEmergency_contact(),
                emergencyNotif.getLatitude(),
                emergencyNotif.getLongitude(),
                emergencyNotif.getDevice_id(),
                deviceName,
                battery,
                emergencyNotif.is_using_mokura(),
                dateFormat.format(emergencyNotif.getCreated_at())
        );
    }

}
