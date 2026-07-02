package com.example.Task_III.entity;

import com.example.Task_III.enumaration.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = true)
    private String username;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private String name;
    private String address;
    private String phone;
}
