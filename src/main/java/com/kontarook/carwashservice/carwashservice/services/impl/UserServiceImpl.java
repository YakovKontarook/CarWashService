package com.kontarook.carwashservice.carwashservice.services.impl;

import com.kontarook.carwashservice.carwashservice.entities.Role;
import com.kontarook.carwashservice.carwashservice.entities.User;
import com.kontarook.carwashservice.carwashservice.exceptions.JwtAuthenticationException;
import com.kontarook.carwashservice.carwashservice.exceptions.UserNotFoundException;
import com.kontarook.carwashservice.carwashservice.repositories.RoleRepository;
import com.kontarook.carwashservice.carwashservice.repositories.UserRepository;
import com.kontarook.carwashservice.carwashservice.security.jwt.JwtTokenProvider;
import com.kontarook.carwashservice.carwashservice.services.UserService;
import com.kontarook.carwashservice.carwashservice.utils.AuthenticationRequest;
import com.kontarook.carwashservice.carwashservice.utils.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider,
                           UserRepository userRepository,
                           RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(AuthenticationRequest authRequest) {

        try {
            String username = authRequest.getUsername();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

            User user = userRepository.findByUsername(username);
            if (user == null) throw new UserNotFoundException("Пользователь не найден");

            String token = jwtTokenProvider.createToken(username, user.getRoles(), user.getId(), user.getEmail());
            return token;

        } catch (AuthenticationException e) {
            throw new JwtAuthenticationException("Неправильное имя или пароль");
        }
    }

    public User signUp(SignUpRequest signUpRequest) {

        User user = signUpRequest.toUser();
        Role userRole = roleRepository.findByName("ROLE_USER");
        user.addRole(userRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            User registeredUser = userRepository.save(user);
            return registeredUser;
        } catch (Exception e) {
            throw new JwtAuthenticationException("Пользователь с таким именем или почтой уже существует");
        }
    }
}
