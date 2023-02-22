package com.kontarook.carwashservice.carwashservice.controllers;

import com.kontarook.carwashservice.carwashservice.dto.AppointmentDTO;
import com.kontarook.carwashservice.carwashservice.exceptions.AppointmentException;
import com.kontarook.carwashservice.carwashservice.exceptions.AssistanceException;
import com.kontarook.carwashservice.carwashservice.exceptions.UserNotFoundException;
import com.kontarook.carwashservice.carwashservice.services.impl.AppointmentServiceImpl;
import com.kontarook.carwashservice.carwashservice.utils.AppointmentOnTimeRequest;
import com.kontarook.carwashservice.carwashservice.utils.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Log4j
@RestController
@Api(value = "Add, get and delete appointments")
@RequestMapping(value = "carwash/api/appointments")
public class AppointmentController {

    private final AppointmentServiceImpl appointmentServiceImpl;

    @Autowired
    public AppointmentController(AppointmentServiceImpl appointmentServiceImpl) {
        this.appointmentServiceImpl = appointmentServiceImpl;
    }

    @PostMapping("/")
    @ApiOperation("Add a new appointment on the nearest free time")
    public AppointmentDTO add(@RequestBody AppointmentDTO appointmentDto) {
        AppointmentOnTimeRequest appointment = new AppointmentOnTimeRequest();
        appointment.setAppointmentDTO(appointmentDto);
        AppointmentDTO appointmentDTO = appointmentServiceImpl.createAppointment(appointment);

        return appointmentDTO;
    }

    @PostMapping("/on-time")
    @ApiOperation("Add a new appointment on a specific time")
    public AppointmentDTO addAppointmentOnSpecificTime(
            @RequestBody AppointmentOnTimeRequest appointmentOnTimeRequest) {

        AppointmentDTO appointmentDTO = appointmentServiceImpl
                .createAppointment(appointmentOnTimeRequest);

        return appointmentDTO;
    }

    @GetMapping("/admin/all")
    @ApiOperation("Get all appointments (Admin rights only)")
    public List<AppointmentDTO> getAll() {

        List<AppointmentDTO> appointments = appointmentServiceImpl.getAll();

        return appointments;
    }

    @GetMapping("/admin/{id}")
    @ApiOperation("Get appointment by Appointment_ID (Admin rights only)")
    public AppointmentDTO get(@PathVariable Integer id) {
            AppointmentDTO appointmentDTO = appointmentServiceImpl.get(id);

            return appointmentDTO;
    }

    @GetMapping("/user")
    @ApiOperation("Get appointments user is waiting")
    public List<AppointmentDTO> getWaitingAssistanceByUser() {

        List<AppointmentDTO> appointments = appointmentServiceImpl.getWaitingAssistanceByUser();

        return appointments;
    }

    @GetMapping("/user/history")
    @ApiOperation("Get history of the user appointments")
    public List<AppointmentDTO> getUserHistory() {

        List<AppointmentDTO> appointments = appointmentServiceImpl.getUserHistory();

        return appointments;
    }

    @DeleteMapping("/admin/{id}")
    @ApiOperation("Delete any appointment by id (Admin rights)")
    public AppointmentDTO delete(@PathVariable Integer id) {

        AppointmentDTO appointmentDTO = appointmentServiceImpl.delete(id);
        return appointmentDTO;
    }

    @ExceptionHandler(AppointmentException.class)
    private ErrorResponse AppointmentNotFoundHandleException(AppointmentException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        return response;
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ErrorResponse UserNotFoundHandleException(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return response;
    }

    @ExceptionHandler(AssistanceException.class)
    private ErrorResponse UserNotFoundHandleException(AssistanceException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return response;
    }

    @ExceptionHandler(DateTimeParseException.class)
    private ErrorResponse UserNotFoundHandleException(DateTimeParseException e) {
        ErrorResponse response = new ErrorResponse(
                "Неверный формат даты",
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return response;
    }
}
