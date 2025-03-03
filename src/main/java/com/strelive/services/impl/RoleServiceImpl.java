package com.strelive.services.impl;

import com.strelive.dao.RoleDAO;
import com.strelive.dao.RoleRepository;
import com.strelive.dto.RoleRequestDTO;
import com.strelive.dto.RoleResponseDTO;
import com.strelive.entities.Role;
import com.strelive.mapper.RoleMapper;
import com.strelive.services.RoleService;
import jakarta.ejb.Stateful;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class RoleServiceImpl implements RoleService {
    @Inject
    private RoleMapper roleMapper;
    @Inject
    private RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).orElse(null);
    }

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        Role role = roleMapper.toEntity(roleRequestDTO);
        role = roleRepository.save(role);
        return roleMapper.toResponseDTO(role);
    }

    @Override
    public RoleResponseDTO getRole(Long id) {
        Role role = roleRepository.findById(id).orElse(null);
        return roleMapper.toResponseDTO(role);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        return roleMapper.toResponseDTOList(roleRepository.findAll().toList());
    }
}
