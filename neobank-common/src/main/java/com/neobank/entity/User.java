package com.neobank.entity;

import com.neobank.enums.OperationStatus;
import com.neobank.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

//    private String fullName;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private Role role ;

    private LocalDateTime createdAt ;


    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (role == null) role = Role.CLIENT;
    }
}
