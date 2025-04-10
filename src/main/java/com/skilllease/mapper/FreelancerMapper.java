package com.skilllease.mapper;

import com.skilllease.dto.ServiceCreateDto;
import com.skilllease.dto.UserDto;
import com.skilllease.entities.Service;
import com.skilllease.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface FreelancerMapper {
    FreelancerMapper INSTANCE = Mappers.getMapper(FreelancerMapper.class);

    @Mapping(target = "services", source = "services") // Map services separately
    UserDto toDto(User user);

    ServiceCreateDto toDto(Service service);

    List<ServiceCreateDto> toDtoList(List<Service> services);
}
