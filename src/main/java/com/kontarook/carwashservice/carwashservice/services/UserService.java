package com.kontarook.carwashservice.carwashservice.services;

import com.kontarook.carwashservice.carwashservice.entities.User;
import com.kontarook.carwashservice.carwashservice.utils.AuthenticationRequest;
import com.kontarook.carwashservice.carwashservice.utils.SignUpRequest;

public interface UserService {
    public String login(AuthenticationRequest authRequest);

    public User signUp(SignUpRequest signUpRequest);
}
