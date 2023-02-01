package com.kontarook.carwashservice.carwashservice.controllers;

import com.kontarook.carwashservice.carwashservice.dto.AssistanceDTO;
import com.kontarook.carwashservice.carwashservice.exceptions.AssistanceException;
import com.kontarook.carwashservice.carwashservice.services.impl.AssistanceServiceImpl;
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
import java.util.List;

@Log4j
@RestController
@Api(value = "Add, get and delete assistance")
@RequestMapping(value = "carwash/api/assistance")
public class AssistanceController {

    private final AssistanceServiceImpl assistanceServiceImpl;

    @Autowired
    public AssistanceController(AssistanceServiceImpl assistanceServiceImpl) {
        this.assistanceServiceImpl = assistanceServiceImpl;
    }

    @PostMapping("/admin/")
    @ApiOperation("Add a new assistance (Admin rights only)")
    public ResponseEntity<Object> add(@RequestBody AssistanceDTO assistanceDTO) {
        assistanceServiceImpl.add(assistanceDTO);

        return new ResponseEntity<>(new ResponseBuilder()
                .put("Услуга", assistanceDTO).build(), HttpStatus.OK);
    }

    @GetMapping("/")
    @ApiOperation("Get a list of all assistances")
    public ResponseEntity<Object> getAll() {

        List<AssistanceDTO> assistances = assistanceServiceImpl.getAll();

        return new ResponseEntity<>(new ResponseBuilder()
                .put("Услуги", assistances)
                .build(), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @ApiOperation("Get assistance by Assistance_ID")
    public ResponseEntity<Object> get(@PathVariable Integer id) {
        AssistanceDTO assistanceDTO = assistanceServiceImpl.get(id);

        return new ResponseEntity<>(new ResponseBuilder()
                .put("Услуга", assistanceDTO)
                .build(), HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    @ApiOperation("Update any assistance by Assistance_ID (Admin rights only)")
    public ResponseEntity<Object> update(@PathVariable Integer id,
                                         @RequestBody AssistanceDTO updateAssistance
    ) {
        AssistanceDTO assistanceDTO = assistanceServiceImpl.update(id, updateAssistance);

        return new ResponseEntity<>(new ResponseBuilder()
                .put("Услуга изменена", assistanceDTO).build(), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    @ApiOperation("Delete any assistance by Assistance_ID (Admin rights only)")
    public ResponseEntity<Object> delete(@PathVariable Integer id) {

        AssistanceDTO assistanceDTO = assistanceServiceImpl.delete(id);
        return new ResponseEntity<>(new ResponseBuilder()
                .put("Услуга удалена", assistanceDTO).build(), HttpStatus.OK);
    }

    @ExceptionHandler({AssistanceException.class})
    private ResponseEntity<ErrorResponse> UserNotFoundHandleException(AssistanceException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
