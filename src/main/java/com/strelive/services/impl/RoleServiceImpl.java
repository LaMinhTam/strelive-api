package com.strelive.services.impl;

import com.strelive.dao.RoleDAO;
import com.strelive.dto.RoleRequestDTO;
import com.strelive.dto.RoleResponseDTO;
import com.strelive.entities.Role;
import com.strelive.mapper.RoleMapper;
import com.strelive.services.RoleService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;

@Stateless
public class RoleServiceImpl implements RoleService {
    @Inject
    private RoleDAO roleDAO;
    @Inject
    private RoleMapper roleMapper;

    @Override
    public Role findByName(String name) {
        return roleDAO.findByName(name).orElse(null);
    }

    @Override
    public RoleResponseDTO createRole(RoleRequestDTO roleRequestDTO) {
        Role role = roleMapper.toEntity(roleRequestDTO);
        role = roleDAO.save(role);
        return roleMapper.toResponseDTO(role);
    }

    @Override
    public RoleResponseDTO getRole(Long id) {
        Role role = roleDAO.findById(id).orElse(null);
        return roleMapper.toResponseDTO(role);
    }

    @Override
    public List<RoleResponseDTO> getAllRoles() {
        List<Role> roles = roleDAO.findAll();
        return roleMapper.toResponseDTOList(roles);
    }
}
