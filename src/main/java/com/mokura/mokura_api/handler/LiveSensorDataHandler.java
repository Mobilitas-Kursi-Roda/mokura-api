package com.mokura.mokura_api.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokura.mokura_api.dto.ReqSendSensorDeviceWsDto;
import com.mokura.mokura_api.entity.Device;
import com.mokura.mokura_api.entity.DeviceRecord;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.repository.DeviceRepository;
import com.mokura.mokura_api.repository.UserRepository;
import com.mokura.mokura_api.service.DeviceRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LiveSensorDataHandler extends TextWebSocketHandler {

    @Autowired
    private DeviceRecordService deviceRecordService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private UserRepository userRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private static final Logger log = LoggerFactory.getLogger(LiveSensorDataHandler.class);

    private final Map<String, WebSocketSession> deviceSessions = new ConcurrentHashMap<>();
    private final List<WebSocketSession> adminSessions = new ArrayList<>();

    private final Map<String, Object> sendData = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String role = session.getUri().getQuery().split("=")[1];
        if (role.startsWith("admin")) {
            adminSessions.add(session);
            log.info("admin session added");
            sendMessageToAdmin();
        }else{
            String device_id = session.getUri().getQuery().split("=")[2];
            deviceSessions.put(device_id, session);
            log.info("New device session id: {}", device_id);
        }
        System.out.println(adminSessions);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        ReqSendSensorDeviceWsDto sensorDto = mapper.readValue(message.getPayload(), ReqSendSensorDeviceWsDto.class);

        DeviceRecord deviceRecord = new DeviceRecord();
        deviceRecord.setUserId(sensorDto.getUser_id());
        deviceRecord.setDeviceId(sensorDto.getDevice_id());
        deviceRecord.setCreated_at(LocalDateTime.now());
        deviceRecord.setBattery(sensorDto.getBattery());
        deviceRecord.setRpm(sensorDto.getRpm());
        deviceRecord.setSpeed(sensorDto.getSpeed());
        deviceRecord.setLatitude(sensorDto.getLatitude());
        deviceRecord.setLongitude(sensorDto.getLongitude());

        Optional<User> user = userRepository.findById(sensorDto.getUser_id());
        if (user.isPresent()) {
            sensorDto.setUsername(user.get().getFull_name());
        }

        sendData.put(deviceRecord.getDeviceId().toString(), sensorDto);

        deviceRecordService.saveDeviceRecord(deviceRecord);

        Optional<Device> device = deviceRepository.findById(deviceRecord.getDeviceId());
        device.ifPresent(d -> {
            d.setBattery(String.valueOf(sensorDto.getBattery()));
            d.setLatitude(sensorDto.getLatitude());
            d.setLongitude(sensorDto.getLongitude());
            d.setLast_update(Instant.now());
            deviceRepository.save(d);
        });

        sendMessageToAdmin();
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if(adminSessions.contains(session)){
            adminSessions.remove(session);
        }else{
            String deviceId = deviceSessions.entrySet()
                    .stream()
                    .filter(entry -> entry.getValue().equals(session))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(null);
            if(deviceId != null){
                deviceSessions.remove(deviceId);
                sendData.remove(deviceId);
            }
        }

        sendMessageToAdmin();
    }

//    public void sendMessageToUUID(String uuid, String message) throws Exception {
//        WebSocketSession session = sessions.get(uuid);
//        if (session != null && session.isOpen()) {
//            session.sendMessage(new TextMessage(message));
//        }
//    }

    public void sendMessageToAdmin() throws Exception {
        for (WebSocketSession session : adminSessions) {
            System.out.println(session);
            if(session == null) continue;
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(mapper.writeValueAsString(sendData)));
                } catch (Exception e) {
                    log.error("Error sending message to admin session", e);
                }
            }
            else {
                log.info("admin session id: {} closed", session.getUri().getQuery().split("=")[2]);
            }
        }
    }
}
