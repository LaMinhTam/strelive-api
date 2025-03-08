package com.skilllease.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String cvUrl;
    private LocalDateTime createdAt;
    private List<ServiceDto> services; // List of services provided by the freelancer
}
