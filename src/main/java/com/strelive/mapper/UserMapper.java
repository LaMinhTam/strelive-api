package com.strelive.mapper;

import com.strelive.dto.RegisterRequest;
import com.strelive.dto.UserRequestDTO;
import com.strelive.dto.UserResponseDTO;
import com.strelive.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface UserMapper {
    User toEntity(UserRequestDTO userRequestDTO);

    User toEntity(RegisterRequest registerRequest);

    UserResponseDTO toResponseDTO(User user);

    List<UserResponseDTO> toResponseDTOList(List<User> users);
}
