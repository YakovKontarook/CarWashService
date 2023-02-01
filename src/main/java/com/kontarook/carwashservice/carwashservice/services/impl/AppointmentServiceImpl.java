package com.kontarook.carwashservice.carwashservice.services.impl;

import com.kontarook.carwashservice.carwashservice.dto.AppointmentDTO;
import com.kontarook.carwashservice.carwashservice.entities.Appointment;
import com.kontarook.carwashservice.carwashservice.entities.Assistance;
import com.kontarook.carwashservice.carwashservice.entities.User;
import com.kontarook.carwashservice.carwashservice.exceptions.AppointmentException;
import com.kontarook.carwashservice.carwashservice.exceptions.AssistanceException;
import com.kontarook.carwashservice.carwashservice.exceptions.UserNotFoundException;
import com.kontarook.carwashservice.carwashservice.repositories.AppointmentRepository;
import com.kontarook.carwashservice.carwashservice.repositories.AssistanceRepository;
import com.kontarook.carwashservice.carwashservice.repositories.UserRepository;
import com.kontarook.carwashservice.carwashservice.security.jwt.JwtUser;
import com.kontarook.carwashservice.carwashservice.services.AppointmentService;
import com.kontarook.carwashservice.carwashservice.utils.AppointmentOnTimeRequest;
import com.kontarook.carwashservice.carwashservice.utils.DtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final DtoConverter dtoConverter;

    private final AppointmentRepository appointmentRepository;

    private final UserRepository userRepository;

    private final AssistanceRepository assistanceRepository;

    @Autowired
    public AppointmentServiceImpl(DtoConverter dtoConverter, AppointmentRepository appointmentRepository,
                                  UserRepository userRepository, AssistanceRepository assistanceRepository) {
        this.dtoConverter = dtoConverter;
        this.appointmentRepository = appointmentRepository;
        this.userRepository = userRepository;
        this.assistanceRepository = assistanceRepository;
    }

    @Transactional
    public AppointmentDTO createAppointment(AppointmentOnTimeRequest appointmentTimeRequest) {

        AppointmentDTO appointmentDTO = appointmentTimeRequest.getAppointmentDTO();
        Appointment appointment = new Appointment();
        User user;
        List<Assistance> assistances;
        Long totalDurationOfAssistances;
        LocalDateTime endTime;
        Double totalPriceOfAssistences;
        LocalDateTime startTime;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Integer userId = jwtUser.getId();

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) throw new UserNotFoundException("Пользователь не найден");
        user = userOptional.get();

        assistances = assistanceRepository.findAllByIdIn(appointmentDTO.getAssistancesIds());
        if (assistances.isEmpty()) throw new AssistanceException("Указанные услуги не найдены");

        if (Objects.isNull(appointmentTimeRequest.getAppointmentTime())) {
            totalDurationOfAssistances = assistanceRepository
                    .findTotalDurationOfAssistances(assistances);
            Timestamp starttimeTimestamp = appointmentRepository
                    .findNearestAvailableAppointment(totalDurationOfAssistances);
            startTime = starttimeTimestamp.toLocalDateTime().plusMinutes(1);
            endTime = startTime.plusMinutes(totalDurationOfAssistances);
        } else {
            startTime = appointmentTimeRequest.getAppointmentTime().plusMinutes(1);
            if (startTime.isBefore(LocalDateTime.now())) throw new AppointmentException("Неверный формат даты");
            totalDurationOfAssistances = assistanceRepository.findTotalDurationOfAssistances(assistances);
            endTime = startTime.plusMinutes(totalDurationOfAssistances);
            List<Appointment> checkTimeIsFree = appointmentRepository
                    .checkAppointmentTimeIsFree(startTime, endTime);

            if (!checkTimeIsFree.isEmpty()) throw new AppointmentException("К сожалению это время занято");
        }

        if (assistances.isEmpty()) throw new AssistanceException("Указанные услуги не найдены");
        totalPriceOfAssistences = assistanceRepository.findTotalPriceOfAssistances(assistances);

        appointment.setUser(user);
        appointment.setStartTime(startTime);
        appointment.setAssistances(assistances);
        appointment.setEndTime(endTime);
        appointment.setTotalPrice(totalPriceOfAssistences);

        appointmentRepository.save(appointment);

        appointmentDTO = dtoConverter.convert(appointment, AppointmentDTO.class);
        appointmentDTO.setTilStart(appointment.getStartTime());
        appointmentDTO.setCreated(LocalDateTime.now());

        return appointmentDTO;
    }

    public List<AppointmentDTO> getAll() {
        List<AppointmentDTO> appointmentDTOs = appointmentRepository.findAll().stream()
                .map((appointment) -> dtoConverter.convert(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());

        if (appointmentDTOs.isEmpty()) throw new AppointmentException("Записи не найдены");

        for (AppointmentDTO appointmentDTO : appointmentDTOs) {
            appointmentDTO.setTilStart(appointmentDTO.getStartTime());
        }
        return appointmentDTOs;
    }

    public AppointmentDTO get(Integer id) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);

        if (appointmentOptional.isEmpty()) throw new AppointmentException("Запись не найдена");

        AppointmentDTO appointmentDTO = dtoConverter.convert(appointmentOptional.get(), AppointmentDTO.class);
        appointmentDTO.setTilStart(appointmentDTO.getStartTime());

        return appointmentDTO;
    }

    public List<AppointmentDTO> getWaitingAssistanceByUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Integer id = jwtUser.getId();

        List<AppointmentDTO> appointmentDTOs = appointmentRepository.findAllWaitingAssistanceByUserId(id).stream()
                .map((appointment) -> dtoConverter.convert(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());

        if (appointmentDTOs.isEmpty()) {
            Optional<User> userById = userRepository.findById(id);

            if (userById.isPresent()) {
                throw new AppointmentException("Не найдено записей на которые Вы записаны");
            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }
        }
        for (AppointmentDTO appointmentDTO : appointmentDTOs) {
            appointmentDTO.setTilStart(appointmentDTO.getStartTime());
        }
        return appointmentDTOs;
    }

    public List<AppointmentDTO> getUserHistory() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Integer id = jwtUser.getId();

        List<AppointmentDTO> appointmentDTOs = appointmentRepository.findAllByUserId(id).stream()
                .map((appointment) -> dtoConverter.convert(appointment, AppointmentDTO.class))
                .collect(Collectors.toList());

        if (appointmentDTOs.isEmpty()) {
            Optional<User> userById = userRepository.findById(id);

            if (userById.isPresent()) {
                throw new AppointmentException("Не найдено истории записей");
            } else {
                throw new UserNotFoundException("Пользователь не найден");
            }
        }

        for (AppointmentDTO appointmentDTO : appointmentDTOs) {
            appointmentDTO.setTilStart(appointmentDTO.getStartTime());
        }
        return appointmentDTOs;
    }

    public AppointmentDTO delete(Integer id) {
        Optional<Appointment> appointmentOptional = appointmentRepository.findById(id);
        if (appointmentOptional.isEmpty()) throw new AppointmentException("Запись не найдена");

        AppointmentDTO appointmentDTO = dtoConverter.convert(appointmentOptional.get(), AppointmentDTO.class);
        appointmentDTO.setTilStart(appointmentDTO.getStartTime());

        appointmentRepository.deleteById(id);
        return appointmentDTO;
    }

}
