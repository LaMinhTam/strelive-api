package com.skilllease.mapper;
import com.skilllease.dto.ServiceDto;
import com.skilllease.entities.Service;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.time.LocalDateTime;

@Mapper(componentModel = "jakarta")
public interface ServiceMapper {
    ServiceMapper INSTANCE = Mappers.getMapper(ServiceMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "createdAt", expression = "java(getCurrentDateTime())")
    Service toEntity(ServiceDto dto);

    // Define a custom method to get the current LocalDateTime
    default LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }
}
