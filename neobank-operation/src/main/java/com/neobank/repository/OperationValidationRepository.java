package com.neobank.repository;

import com.neobank.entity.Operation;
import com.neobank.entity.OperationValidation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OperationValidationRepository extends JpaRepository<OperationValidation, Long> {

    boolean existsByOperationId(Long operationId);

    Optional<OperationValidation> findByOperationId(Long operationId);

    List<OperationValidation> findByAgentId(Long agentId);
}
