package com.kontarook.carwashservice.carwashservice.controllers;

import com.kontarook.carwashservice.carwashservice.entities.User;
import com.kontarook.carwashservice.carwashservice.exceptions.JwtAuthenticationException;
import com.kontarook.carwashservice.carwashservice.exceptions.UserNotFoundException;
import com.kontarook.carwashservice.carwashservice.services.impl.UserServiceImpl;
import com.kontarook.carwashservice.carwashservice.utils.AuthenticationRequest;
import com.kontarook.carwashservice.carwashservice.utils.ErrorResponse;
import com.kontarook.carwashservice.carwashservice.utils.ResponseBuilder;
import com.kontarook.carwashservice.carwashservice.utils.SignUpRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;

@Log4j
@RestController
@RequestMapping(value = "carwash/api/auth")
public class AuthenticationController {

    private final UserServiceImpl userServiceImpl;

    public AuthenticationController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/login")
    @ApiOperation("Login")
    public ResponseEntity<Object> login(@RequestBody AuthenticationRequest authRequest) {

        String token = userServiceImpl.login(authRequest);

        return new ResponseEntity<>(new ResponseBuilder()
                .put("token", token)
                .build(), HttpStatus.OK);
    }

    @PostMapping("/signup")
    @ApiOperation("Signup")
    public ResponseEntity signUp(@RequestBody @Valid SignUpRequest signUpRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(new ResponseBuilder("Ошибка регистрации")
                    .put("Error", bindingResult.getFieldError().getDefaultMessage())
                    .build(), HttpStatus.NOT_ACCEPTABLE);
        }

        User registeredUser = userServiceImpl.signUp(signUpRequest);

        return new ResponseEntity<>(new ResponseBuilder()
                .put("User", registeredUser)
                .build(), HttpStatus.OK);
    }

    @ExceptionHandler({UserNotFoundException.class})
    private ResponseEntity<ErrorResponse> authenticationException(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({JwtAuthenticationException.class})
    private ResponseEntity<ErrorResponse> authenticationException(JwtAuthenticationException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}



