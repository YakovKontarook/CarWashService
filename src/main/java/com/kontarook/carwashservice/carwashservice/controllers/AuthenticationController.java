package com.kontarook.carwashservice.carwashservice.controllers;

import com.kontarook.carwashservice.carwashservice.entities.User;
import com.kontarook.carwashservice.carwashservice.exceptions.JwtAuthenticationException;
import com.kontarook.carwashservice.carwashservice.exceptions.UserNotFoundException;
import com.kontarook.carwashservice.carwashservice.services.impl.UserServiceImpl;
import com.kontarook.carwashservice.carwashservice.utils.AuthenticationRequest;
import com.kontarook.carwashservice.carwashservice.utils.ErrorResponse;
import com.kontarook.carwashservice.carwashservice.utils.SignUpRequest;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;
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
    public String login(@RequestBody AuthenticationRequest authRequest) {

        String token = userServiceImpl.login(authRequest);
        return token;
    }

    @PostMapping("/signup")
    @ApiOperation("Signup")
    public User signUp(@RequestBody @Valid SignUpRequest signUpRequest) {

        User registeredUser = userServiceImpl.signUp(signUpRequest);
        return registeredUser;
    }

    @ExceptionHandler({UserNotFoundException.class})
    private ErrorResponse authenticationException(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return response;
    }

    @ExceptionHandler({JwtAuthenticationException.class})
    private ErrorResponse authenticationException(JwtAuthenticationException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );
        log.error(e.getMessage());
        return response;
    }
}



