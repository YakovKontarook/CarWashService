package com.kontarook.carwashservice.carwashservice.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kontarook.carwashservice.carwashservice.dto.AppointmentDTO;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

public class AppointmentOnTimeRequest {

    private AppointmentDTO appointmentDTO;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    @ApiModelProperty(example = "dd-MM-yyyy HH:mm")
    private LocalDateTime appointmentTime;

    public AppointmentDTO getAppointmentDTO() {
        return appointmentDTO;
    }

    public void setAppointmentDTO(AppointmentDTO appointmentDTO) {
        this.appointmentDTO = appointmentDTO;
    }

    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalDateTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}
