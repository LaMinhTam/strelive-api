package com.skilllease.mapper;

import com.skilllease.entities.User;
import com.skilllease.dto.RegisterRequest;
import com.skilllease.dto.UserRequestDTO;
import com.skilllease.dto.UserResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface UserMapper {
    User toEntity(UserRequestDTO userRequestDTO);

    User toEntity(RegisterRequest registerRequest);

    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toResponseDTOList(List<User> users);
}
