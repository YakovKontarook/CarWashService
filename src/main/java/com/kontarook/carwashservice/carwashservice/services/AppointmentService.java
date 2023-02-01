package com.kontarook.carwashservice.carwashservice.services;

import com.kontarook.carwashservice.carwashservice.dto.AppointmentDTO;
import com.kontarook.carwashservice.carwashservice.utils.AppointmentOnTimeRequest;

import java.util.List;

public interface AppointmentService {
    AppointmentDTO createAppointment(AppointmentOnTimeRequest appointmentTimeRequest);

    List<AppointmentDTO> getAll();

    AppointmentDTO get(Integer id);

    List<AppointmentDTO> getWaitingAssistanceByUser();

    List<AppointmentDTO> getUserHistory();

    AppointmentDTO delete(Integer id);
}
