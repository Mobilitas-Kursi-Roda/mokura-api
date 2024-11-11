package com.mokura.mokura_api.service;

import com.mokura.mokura_api.entity.EmergencyNotif;

import java.util.List;

public interface EmgergencyNotifService {
    List<EmergencyNotif> getAll();
    EmergencyNotif getById(Long id);
}
