package com.skilllease.services.impl;

import com.skilllease.dao.ServiceRepository;
import com.skilllease.dto.ServiceCreateDto;
import com.skilllease.entities.Category;
import com.skilllease.entities.Role;
import com.skilllease.entities.Service;
import com.skilllease.entities.User;
import com.skilllease.exception.EntityNotFoundException;
import com.skilllease.exception.ErrorCode;
import com.skilllease.exception.UnauthorizedException;
import com.skilllease.mapper.ServiceMapper;
import com.skilllease.services.CategoryService;
import com.skilllease.services.FreelancerService;
import com.skilllease.services.ServiceService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class ServiceServiceImpl implements ServiceService {
    @Inject
    private ServiceRepository serviceRepository;
    @Inject
    private FreelancerService freelancerService;
    @Inject
    private CategoryService categoryService;

    @Transactional
    public Service createService(Long freelancerId, ServiceCreateDto dto) throws EntityNotFoundException {
        Service service = ServiceMapper.INSTANCE.toEntity(dto);
        // Validate category
        Category category = categoryService.findById(dto.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.NOT_FOUND));
        service.setCategory(category);

        // Set freelancer
        User freelancer = freelancerService.findById(freelancerId);

        if (!freelancer.getRole().equals(Role.FREELANCER)) {
            throw new UnauthorizedException("Only freelancers can add services");
        }
        service.setFreelancer(freelancer);

        return serviceRepository.save(service);
    }

    @Override
    public List<Service> getServiceByUserId(Long id) {
        return null;
    }
}
