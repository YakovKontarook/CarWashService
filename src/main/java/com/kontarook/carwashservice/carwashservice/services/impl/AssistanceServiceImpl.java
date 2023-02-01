package com.kontarook.carwashservice.carwashservice.services.impl;

import com.kontarook.carwashservice.carwashservice.dto.AssistanceDTO;
import com.kontarook.carwashservice.carwashservice.entities.Assistance;
import com.kontarook.carwashservice.carwashservice.exceptions.AssistanceException;
import com.kontarook.carwashservice.carwashservice.repositories.AssistanceRepository;
import com.kontarook.carwashservice.carwashservice.services.AssistanceService;
import com.kontarook.carwashservice.carwashservice.utils.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssistanceServiceImpl implements AssistanceService {

    private final AssistanceRepository assistanceRepository;

    private final DtoConverter dtoConverter;

    @Autowired
    public AssistanceServiceImpl(AssistanceRepository assistanceRepository, DtoConverter dtoConverter) {
        this.assistanceRepository = assistanceRepository;
        this.dtoConverter = dtoConverter;
    }

    public AssistanceDTO add(AssistanceDTO assistanceDTO) {
        Assistance assistance = dtoConverter.convert(assistanceDTO, Assistance.class);
        assistanceRepository.save(assistance);
        return assistanceDTO;
    }

    public List<AssistanceDTO> getAll() {

        List<AssistanceDTO> assistants = assistanceRepository.findAll().stream()
                .map((service) -> dtoConverter.convert(service, AssistanceDTO.class))
                .collect(Collectors.toList());

        if (assistants.isEmpty()) throw new AssistanceException("Услуги не найдены");

        return assistants;

    }

    public AssistanceDTO get(Integer id) {
        Optional<Assistance> assistanceOptional = assistanceRepository.findById(id);

        if (assistanceOptional.isEmpty()) throw new AssistanceException("Указанная услуга не найдена");

        AssistanceDTO assistanceDTO = dtoConverter.convert(assistanceOptional.get(), AssistanceDTO.class);
        return assistanceDTO;
    }

    public AssistanceDTO update(Integer id, AssistanceDTO updateAssistance) {

        Optional<Assistance> assistanceOptional = assistanceRepository.findById(id);

        if (assistanceOptional.isEmpty()) throw new AssistanceException("Указанная услуга не найдена");

        Assistance assistance = assistanceOptional.get();
        assistance.setName(updateAssistance.getName());
        assistance.setDescription(updateAssistance.getDescription());
        assistance.setPrice(updateAssistance.getPrice());
        assistanceRepository.save(assistance);

        return updateAssistance;
    }

    public AssistanceDTO delete(Integer id) {
        Optional<Assistance> serviceOptional = assistanceRepository.findById(id);
        if (serviceOptional.isEmpty()) throw new AssistanceException("Указанная услуга не найдена");

        AssistanceDTO assistanceDTO = dtoConverter.convert(serviceOptional.get(), AssistanceDTO.class);

        assistanceRepository.deleteById(id);
        return assistanceDTO;
    }
}
