package com.skilllease.dto;

import com.skilllease.entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String cvUrl;
    private LocalDateTime createdAt;
    private List<ServiceDto> services; // List of services provided by the freelancer
}
