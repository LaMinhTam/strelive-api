package com.strelive.dao;

import com.strelive.entities.Stream;
import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Insert;
import jakarta.data.repository.Query;
import jakarta.data.repository.Repository;

import java.util.Optional;

@Repository
public interface StreamRepository extends CrudRepository<Stream, Long> {
    @Query("SELECT s FROM Stream s WHERE s.streamKey = :name")
    Optional<Stream> findByStreamKey(String name);

    @Insert
    Stream save(Stream stream);
}