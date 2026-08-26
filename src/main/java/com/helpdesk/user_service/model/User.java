package com.helpdesk.user_service.model;

import com.helpdesk.user_service.enums.UserRole;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "users_db")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User{
    
    //DLL -> Data Definition Language
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

}