package com.kontarook.carwashservice.carwashservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kontarook.carwashservice.carwashservice.entities.Assistance;
import com.kontarook.carwashservice.carwashservice.entities.User;
import io.swagger.annotations.ApiModelProperty;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentDTO {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(
            example = "[\"...\",\"...\",\"...\",\"...\"]",
            dataType = "int32")
    private List<Integer> assistancesIds = new ArrayList<>();

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(hidden = true)
    private User user;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(hidden = true)
    private List<Assistance> assistances = new ArrayList<>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    @ApiModelProperty(example = "dd-MM-yyyy HH:mm", hidden = true)
    private LocalDateTime startTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime endTime;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(hidden = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm")
    private LocalDateTime created;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(hidden = true)
    private Double totalPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @ApiModelProperty(hidden = true)
    private String tilStart;

    public void setTilStart(LocalDateTime startTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(currentTime, startTime);
        long minutes = duration.toMinutes();

        if (minutes >= 1440) {
            long days = minutes / 1440;
            tilStart = (days + "d " + (minutes % 1440) / 60 + "h " + (minutes % 1440) % 60 + "m");
        } else if (minutes >= 60) {
            long hours = minutes / 60;
            tilStart = (hours + "h " + minutes % 60 + "m");
        } else if (minutes < 60 && minutes > 0) {
            tilStart = (minutes + "m");
        } else if (minutes <= 0 && LocalDateTime.now().isBefore(endTime)) {
            tilStart = "Ваша запись началась";
        } else if (LocalDateTime.now().isAfter(endTime)) {
            tilStart = "Ваша запись закончилась";
        }
    }

    public List<Integer> getAssistancesIds() {
        return assistancesIds;
    }

    public void setAssistancesIds(List<Integer> assistancesIds) {
        this.assistancesIds = assistancesIds;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Assistance> getAssistances() {
        return assistances;
    }

    public void setAssistances(List<Assistance> assistances) {
        this.assistances = assistances;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime createdTimestamp) {
        this.created = createdTimestamp;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTilStart() {
        return tilStart;
    }

}
