package com.neobank.repository;

import com.neobank.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findByAccount_User_Email(String email);
}
