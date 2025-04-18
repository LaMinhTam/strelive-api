package com.skilllease.dao;

import com.skilllease.entities.Service;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;
import jakarta.data.repository.Update;

import java.util.List;

@Repository
public interface ServiceRepository extends CrudRepository<Service, Long>{
    @Query("SELECT s FROM Service s WHERE s.freelancer.id = :id")
    List<Service> findByUserId(Long id);

    @Query("SELECT s FROM Service s WHERE s.freelancer.id = :id")
    List<Service> findByFreelancerId(Long id);

    @Update
    Service update(Service service);
}
