package com.kontarook.carwashservice.carwashservice.utils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.kontarook.carwashservice.carwashservice.entities.User;

public class SignUpRequest {
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min = 2, max = 30, message = "Имя должно быть длинной от 2х до 30 символов")
    private String username;
    @NotEmpty(message = "Email не может быть пустым")
    @Email(message = "Неверный формат почты")
    private String email;
    @Size(min = 8, message = "Пароль должен быть длиннее чем 8 символов")
    @NotEmpty(message = "Пароль не должен быть пустым")
    private String password;

    public User toUser() {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    public static UserConverter fromUser(User user) {
        UserConverter userConverter = new UserConverter();
        userConverter.setUsername(user.getUsername());
        userConverter.setEmail(user.getEmail());
        return userConverter;
    }

    public SignUpRequest() {
    }

    public SignUpRequest(String username, String firstName, String lastName, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
