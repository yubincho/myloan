package com.example.loan.repository;

import com.example.loan.domain.Entry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long> {

    @Query(value = "SELECT * from ENTRY c where c.application_id = :applicationId", nativeQuery = true)
    Optional<Entry> findByApplicationId(@Param("applicationId") Long applicationId);
}
