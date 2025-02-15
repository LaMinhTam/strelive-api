package com.strelive.services;

import com.strelive.dto.RoleRequestDTO;
import com.strelive.dto.RoleResponseDTO;
import com.strelive.entities.Role;

import java.util.List;

public interface RoleService {
    Role findByName(String name);

    RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO);

    RoleResponseDTO getRole(Long id);

    List<RoleResponseDTO> getAllRoles();
}
