package com.kontarook.carwashservice.carwashservice.services;

import com.kontarook.carwashservice.carwashservice.dto.AssistanceDTO;

import java.util.List;

public interface AssistanceService {
    AssistanceDTO add(AssistanceDTO assistanceDTO);

    List<AssistanceDTO> getAll();

    AssistanceDTO get(Integer id);

    AssistanceDTO update(Integer id, AssistanceDTO updateAssistance);

    AssistanceDTO delete(Integer id);
}
