package com.neobank.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "operation_validations")
public class OperationValidation {

    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean approved;

    private String comment;

    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "agent_id")
    private User agent;

    @OneToOne
    @JoinColumn(name = "operation_id")
    private Operation operation;

}
