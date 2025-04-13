package com.skilllease.mapper;

import com.skilllease.dto.JobCreateDto;
import com.skilllease.dto.JobDto;
import com.skilllease.dto.UserDto;
import com.skilllease.entities.Job;
import com.skilllease.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface JobMapper {
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);

    // Convert Job entity to JobDto
    @Mapping(target = "employer", source = "employer", qualifiedByName = "mapUserToDto")
    JobDto toDto(Job job);

    // Convert List<Job> to List<JobDto>
    List<JobDto> toDtoList(List<Job> jobs);

    // Custom mapping to ignore `services` in User
    @Named("mapUserToDto")
    @Mapping(target = "services", ignore = true) // Prevent LazyInitializationException
    UserDto toDto(User user);
}
