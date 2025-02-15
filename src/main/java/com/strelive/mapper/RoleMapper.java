package com.strelive.mapper;

import com.strelive.dto.RoleResponseDTO;
import com.strelive.dto.RoleRequestDTO;
import com.strelive.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface RoleMapper {
    @Mapping(target = "id", ignore = true)
    Role toEntity(RoleRequestDTO roleRequestDTO);

    @Mapping(target = "id", source = "id")
    RoleResponseDTO toResponseDTO(Role role);

    List<RoleResponseDTO> toResponseDTOList(List<Role> roles);
}