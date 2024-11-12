package com.mokura.mokura_api.controller.api.client;

import com.mokura.mokura_api.dto.BaseResponseDto;
import com.mokura.mokura_api.entity.Device;
import com.mokura.mokura_api.entity.History;
import com.mokura.mokura_api.entity.User;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.DeviceRepository;
import com.mokura.mokura_api.repository.HistoryRepository;
import com.mokura.mokura_api.repository.UserRepository;
import com.mokura.mokura_api.service.HistoryService;
import com.mokura.mokura_api.service.impl.HistoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/client/histories")
public class HistoryClientController {

    private static final Logger log = LoggerFactory.getLogger(HistoryClientController.class);
    private final UserRepository userRepository;

    private final DeviceRepository deviceRepository;
    private final HistoryRepository historyRepository;

    private final HistoryService historyService;

    public HistoryClientController(
            UserRepository userRepository,
            DeviceRepository deviceRepository,
            HistoryRepository historyRepository,
            HistoryService historyService
    ) {
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.historyRepository = historyRepository;
        this.historyService = historyService;
    }

    @PostMapping("/send-session")
    public ResponseEntity<BaseResponseDto<History>> sendSession(@RequestBody Map<String, Object> request){
        log.info("here !");
        History history = new History();
        Optional<User> user = userRepository.findById( Long.parseLong(request.get("id_user").toString()) );
        Optional<Device> device = deviceRepository.findById( Long.parseLong(request.get("id_device").toString()));

        if (user.isEmpty()) throw new CustomBadRequestException("User not found");
        if (device.isEmpty()) throw new CustomBadRequestException("Device not found");

//        System.out.println(request.get("start_date"));

        history.setUser(user.get());
        history.setDevice(device.get());
        history.setDuration(Long.parseLong(request.get("duration").toString()));
        history.setDistances(Float.parseFloat(request.get("distances").toString()));
        history.setStart_date(LocalDateTime.parse(request.get("start_date").toString()));
        history.setEnd_date(LocalDateTime.parse(request.get("end_date").toString()));

        historyRepository.save(history);

        device.get().setLatitude(request.get("latitude").toString());
        device.get().setLongitude(request.get("longitude").toString());
        device.get().setStatus("Inactive");
        device.get().setLast_update(Instant.now());
        deviceRepository.save(device.get());

        return ResponseEntity.ok(new BaseResponseDto<>("success",history));
    }

    @GetMapping()
    public ResponseEntity<BaseResponseDto<List<History>>> getHistory(@RequestParam("id_user") Long idUser){
        return historyService.getByUserId(idUser);
    }
}
