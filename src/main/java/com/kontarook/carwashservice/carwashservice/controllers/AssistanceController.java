package com.kontarook.carwashservice.carwashservice.controllers;

import com.kontarook.carwashservice.carwashservice.dto.AssistanceDTO;
import com.kontarook.carwashservice.carwashservice.exceptions.AssistanceException;
import com.kontarook.carwashservice.carwashservice.services.impl.AssistanceServiceImpl;
import com.kontarook.carwashservice.carwashservice.utils.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    public AssistanceDTO add(@RequestBody AssistanceDTO assistanceDTO) {
        assistanceServiceImpl.add(assistanceDTO);

        return assistanceDTO;
    }

    @GetMapping("/")
    @ApiOperation("Get a list of all assistances")
    public List<AssistanceDTO> getAll() {

        List<AssistanceDTO> assistances = assistanceServiceImpl.getAll();

        return assistances;
    }


    @GetMapping("/{id}")
    @ApiOperation("Get assistance by Assistance_ID")
    public AssistanceDTO get(@PathVariable Integer id) {
        AssistanceDTO assistanceDTO = assistanceServiceImpl.get(id);

        return assistanceDTO;
    }

    @PutMapping("/admin/{id}")
    @ApiOperation("Update any assistance by Assistance_ID (Admin rights only)")
    public AssistanceDTO update(@PathVariable Integer id,
                                @RequestBody AssistanceDTO updateAssistance
    ) {
        AssistanceDTO assistanceDTO = assistanceServiceImpl.update(id, updateAssistance);

        return assistanceDTO;
    }

    @DeleteMapping("/admin/{id}")
    @ApiOperation("Delete any assistance by Assistance_ID (Admin rights only)")
    public AssistanceDTO delete(@PathVariable Integer id) {

        AssistanceDTO assistanceDTO = assistanceServiceImpl.delete(id);
        return assistanceDTO;
    }

    @ExceptionHandler({AssistanceException.class})
    private ErrorResponse UserNotFoundHandleException(AssistanceException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return response;
    }
}
