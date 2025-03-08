package com.skilllease.dto;

import com.skilllease.exception.UserExceptionMessage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotBlank(message = UserExceptionMessage.EMAIL_NOT_BLANK)
    @Email(message = UserExceptionMessage.EMAIL_INVALID)
    private String email;

    @NotBlank(message = UserExceptionMessage.PASSWORD_NOT_BLANK)
    private String password;
}
