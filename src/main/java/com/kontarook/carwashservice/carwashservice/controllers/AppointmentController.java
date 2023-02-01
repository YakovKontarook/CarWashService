package com.kontarook.carwashservice.carwashservice.controllers;

import com.kontarook.carwashservice.carwashservice.dto.AppointmentDTO;
import com.kontarook.carwashservice.carwashservice.exceptions.AppointmentException;
import com.kontarook.carwashservice.carwashservice.exceptions.AssistanceException;
import com.kontarook.carwashservice.carwashservice.exceptions.UserNotFoundException;
import com.kontarook.carwashservice.carwashservice.services.impl.AppointmentServiceImpl;
import com.kontarook.carwashservice.carwashservice.utils.AppointmentOnTimeRequest;
import com.kontarook.carwashservice.carwashservice.utils.ErrorResponse;
import com.kontarook.carwashservice.carwashservice.utils.ResponseBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Object> add(@RequestBody AppointmentDTO appointmentDto) {
        AppointmentOnTimeRequest appointment = new AppointmentOnTimeRequest();
        appointment.setAppointmentDTO(appointmentDto);
        AppointmentDTO appointmentDTO = appointmentServiceImpl.createAppointment(appointment);

        return new ResponseEntity<>(new ResponseBuilder()
                .put("Запись", appointmentDTO).build(), HttpStatus.OK);
    }

    @PostMapping("/on-time")
    @ApiOperation("Add a new appointment on a specific time")
    public ResponseEntity<Object> addAppointmentOnSpecificTime(
            @RequestBody AppointmentOnTimeRequest appointmentOnTimeRequest) {

        AppointmentDTO appointmentDTO = appointmentServiceImpl
                .createAppointment(appointmentOnTimeRequest);

        return new ResponseEntity<>(new ResponseBuilder()
                .put("Запись", appointmentDTO).build(), HttpStatus.OK);
    }

    @GetMapping("/admin/all")
    @ApiOperation("Get all appointments (Admin rights only)")
    public ResponseEntity<Object> getAll() {

        List<AppointmentDTO> appointments = appointmentServiceImpl.getAll();

        return new ResponseEntity<>(new ResponseBuilder()
                .put("Все записи", appointments)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    @ApiOperation("Get appointment by Appointment_ID (Admin rights only)")
    public ResponseEntity<Object> get(@PathVariable Integer id) {
            AppointmentDTO appointmentDTO = appointmentServiceImpl.get(id);

            return new ResponseEntity<>(new ResponseBuilder()
                    .put("Запись", appointmentDTO)
                    .build(), HttpStatus.OK);
    }

    @GetMapping("/user")
    @ApiOperation("Get appointments user is waiting")
    public ResponseEntity<Object> getWaitingAssistanceByUser() {

        List<AppointmentDTO> appointments = appointmentServiceImpl.getWaitingAssistanceByUser();

        return new ResponseEntity<>(new ResponseBuilder()
                .put("Ваши записи", appointments)
                .build(), HttpStatus.OK);
    }

    @GetMapping("/user/history")
    @ApiOperation("Get history of the user appointments")
    public ResponseEntity<Object> getUserHistory() {

        List<AppointmentDTO> appointments = appointmentServiceImpl.getUserHistory();

        return new ResponseEntity<>(new ResponseBuilder()
                .put("Ваши записи", appointments)
                .build(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    @ApiOperation("Delete any appointment by id (Admin rights)")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {

        AppointmentDTO appointmentDTO = appointmentServiceImpl.delete(id);
        return new ResponseEntity<>(new ResponseBuilder()
                .put("Запись удалена", appointmentDTO).build(), HttpStatus.OK);
    }

    @ExceptionHandler(AppointmentException.class)
    private ResponseEntity<ErrorResponse> AppointmentNotFoundHandleException(AppointmentException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ErrorResponse> UserNotFoundHandleException(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AssistanceException.class)
    private ResponseEntity<ErrorResponse> UserNotFoundHandleException(AssistanceException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DateTimeParseException.class)
    private ResponseEntity<ErrorResponse> UserNotFoundHandleException(DateTimeParseException e) {
        ErrorResponse response = new ErrorResponse(
                "Неверный формат даты",
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
