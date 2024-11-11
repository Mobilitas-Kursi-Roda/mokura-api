package com.mokura.mokura_api.service.impl;

import com.mokura.mokura_api.entity.EmergencyNotif;
import com.mokura.mokura_api.exception.CustomBadRequestException;
import com.mokura.mokura_api.repository.EmergencyNotifRepository;
import com.mokura.mokura_api.service.EmgergencyNotifService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmgergencyNotifServiceImpl implements EmgergencyNotifService {
    private final EmergencyNotifRepository emergencyNotifRepository;

    public EmgergencyNotifServiceImpl(EmergencyNotifRepository emergencyNotifRepository) {
        this.emergencyNotifRepository = emergencyNotifRepository;
    }

    @Override
    public List<EmergencyNotif> getAll() {
        return emergencyNotifRepository.findAll();
    }

    @Override
    public EmergencyNotif getById(Long id) {
        Optional<EmergencyNotif> emergencyNotif = emergencyNotifRepository.findById(id);
        if (emergencyNotif.isEmpty()) throw new CustomBadRequestException("EmergencyNotif not found");
        return emergencyNotif.get();
    }
}
