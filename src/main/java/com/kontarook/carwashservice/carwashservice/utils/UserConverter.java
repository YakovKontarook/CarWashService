package com.kontarook.carwashservice.carwashservice.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kontarook.carwashservice.carwashservice.entities.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserConverter {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        return user;
    }

    public static UserConverter fromUser(User user) {
        UserConverter userConverter = new UserConverter();
        userConverter.setId(user.getId());
        userConverter.setUsername(user.getUsername());
        userConverter.setEmail(user.getEmail());
        return userConverter;
    }

    public UserConverter() {
    }

    public UserConverter(Integer id, String username, String firstName, String lastName, String email) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
