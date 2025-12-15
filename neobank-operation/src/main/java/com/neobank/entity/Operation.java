package com.neobank.entity;

import com.neobank.enums.OperationStatus;
import com.neobank.enums.OperationType;
import com.neobank.entity.Account;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "operations")
public class Operation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    @Enumerated(EnumType.STRING)
    private OperationStatus status;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToOne(mappedBy = "operation", cascade = CascadeType.ALL)
    private OperationValidation validation;

}
