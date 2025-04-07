package com.skilllease.dto;

import com.skilllease.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private String cvUrl;
    private String profilePictureUrl;
}
