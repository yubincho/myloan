package com.example.loan.repository;


import com.example.loan.domain.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Long> {

    @Query(value = "SELECT * FROM BALANCE c WHERE c.application_id = :applicationId", nativeQuery = true)
    Optional<Balance> findByApplicationId(@Param("applicationId") Long applicationId);
}
